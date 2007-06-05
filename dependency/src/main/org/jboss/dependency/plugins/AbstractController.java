/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.dependency.plugins;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.locks.ReentrantReadWriteLock;

import org.jboss.dependency.spi.CallbackItem;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.dependency.spi.LifecycleCallbackItem;
import org.jboss.util.JBossObject;

/**
 * Abstract controller.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractController extends JBossObject implements Controller
{
   /** The lock */
   private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();

   /** Whether we are shutdown */
   private boolean shutdown = false;
   
   /** The states in order List<ControllerState> */
   private List<ControllerState> states = new CopyOnWriteArrayList<ControllerState>();;

   /** All contexts by name Map<Object, ControllerContext> */
   private Map<Object, ControllerContext> allContexts = new ConcurrentHashMap<Object, ControllerContext>();

   /** The contexts by state Map<ControllerState, Set<ControllerContext>> */
   private Map<ControllerState, Set<ControllerContext>> contextsByState = new ConcurrentHashMap<ControllerState, Set<ControllerContext>>();;

   /** The error contexts Map<Name, ControllerContext> */
   private Map<Object, ControllerContext> errorContexts = new ConcurrentHashMap<Object, ControllerContext>();;

   /** The contexts that are currently being installed */
   private Set<ControllerContext> installing = new CopyOnWriteArraySet<ControllerContext>();;

   /** The child controllers */
   private Set<AbstractController> childControllers = new CopyOnWriteArraySet<AbstractController>();;

   /** The callback items */
   private Map<Object, Set<CallbackItem>> installCallbacks = new ConcurrentHashMap<Object, Set<CallbackItem>>();
   private Map<Object, Set<CallbackItem>> uninstallCallbacks = new ConcurrentHashMap<Object, Set<CallbackItem>>();

   /** Whether an on demand context has been enabled */
   private boolean onDemandEnabled = true;

   /**
    * Create an abstract controller
    *
    * @throws Exception for any error
    */
   public AbstractController() throws Exception
   {
      addState(ControllerState.NOT_INSTALLED, null);
      addState(ControllerState.PRE_INSTALL, null);
      addState(ControllerState.DESCRIBED, null);
      addState(ControllerState.INSTANTIATED, null);
      addState(ControllerState.CONFIGURED, null);
      addState(ControllerState.CREATE, null);
      addState(ControllerState.START, null);
      addState(ControllerState.INSTALLED, null);
   }

   public boolean isShutdown()
   {
      lockWrite();
      try
      {
         return shutdown;
      }
      finally
      {
         unlockWrite();
      }
   }

   /**
    * Check whether the controller is shutdown
    *
    * @throws IllegalStateException when already shutdown
    */
   public void checkShutdown()
   {
      lockWrite();
      try
      {
         if (shutdown)
            throw new IllegalStateException("Already shutdown");
      }
      finally
      {
         unlockWrite();
      }
   }

   // TODO need tests for shutdown
   public void shutdown()
   {
      lockWrite();
      try
      {
         Set<AbstractController> children = getControllers();
         if (children != null && children.isEmpty() == false)
         {
            for (AbstractController child : children)
            {
               try
               {
                  child.shutdown();
               }
               catch (Throwable t)
               {
                  log.warn("Error during shutdown of child: " + child, t);
               }
            }
         }
         
         Set<ControllerContext> contexts = getAllContexts();
         if (contexts != null && contexts.isEmpty() == false)
         {
            for (ControllerContext context : contexts)
            {
               try
               {
                  uninstall(context.getName());
               }
               catch (Throwable t)
               {
                  log.warn("Error during shutdown while uninstalling: " + context, t);
               }
            }
         }
      }
      finally
      {
         shutdown = true;
         unlockWrite();
      }
      
   }
   
   public void addState(ControllerState state, ControllerState before)
   {
      lockWrite();
      try
      {
         if (before == null)
         {
            states.add(state);
         }
         else
         {
            int index = states.indexOf(before);
            if (index == -1)
               throw new IllegalStateException(before + " is not a state in the controller.");
            states.add(index, state);
         }

         Set<ControllerContext> contexts =  new CopyOnWriteArraySet<ControllerContext>();
         contextsByState.put(state, contexts);
      }
      finally
      {
         unlockWrite();
      }
   }

   // TODO This api looks broken and unsafe
   //      1) It should not be public
   //      2) There should be parameter checking for public methods
   //      3) There should be locking when updating state
   //      4) Error handling?
   public void addControllerContext(ControllerContext context)
   {
      registerControllerContext(context);
   }

   // TODO This api looks broken and unsafe see above
   public void removeControllerContext(ControllerContext context)
   {
      unregisterControllerContext(context);
   }

   public Set<AbstractController> getControllers()
   {
      return childControllers;
   }

   // no need for locking - we are already locked

   public boolean addController(AbstractController controller)
   {
      return childControllers.add(controller);
   }

   public boolean removeController(AbstractController controller)
   {
      return childControllers.remove(controller);
   }

   /**
    * Whether the controller has contexts
    *
    * @return true when there are registered contexts
    */
   public boolean isActive()
   {
      lockRead();
      try
      {
         return allContexts.isEmpty() == false;
      }
      finally
      {
         unlockRead();
      }
   }

   public Set<ControllerContext> getAllContexts()
   {
      lockRead();
      try
      {
         LinkedHashSet<ControllerContext> result = new LinkedHashSet<ControllerContext>();
         for (int i = states.size()-1; i>=0; --i)
         {
            ControllerState state = states.get(i);
            result.addAll(contextsByState.get(state));
         }
         result.addAll(errorContexts.values());
         return result;
      }
      finally
      {
         unlockRead();
      }
   }

   public ControllerContext getContext(Object name, ControllerState state)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");

      lockRead();
      try
      {
         ControllerContext result = getRegisteredControllerContext(name, false);
         if (result != null && state != null)
         {
            int required = states.indexOf(state);
            if (required == -1)
               throw new IllegalArgumentException("Unknown state " + state + " states=" + states);
            int current = states.indexOf(result.getState());
            if (current < required)
               return null;
         }
         return result;
      }
      finally
      {
         unlockRead();
      }
   }

   public ControllerContext getInstalledContext(Object name)
   {
      return getContext(name, ControllerState.INSTALLED);
   }

   public Set<ControllerContext> getNotInstalled()
   {
      lockWrite();
      try
      {
         Set<ControllerContext> result = new HashSet<ControllerContext>(errorContexts.values());
         for (int i = 0; ControllerState.INSTALLED.equals(states.get(i)) == false; ++i)
         {
            Set<ControllerContext> stateContexts = getContextsByState(states.get(i));
            result.addAll(stateContexts);
         }
         return result;
      }
      finally
      {
         unlockWrite();
      }
   }

   public List<ControllerState> getStates()
   {
      return states;
   }

   public Set<ControllerContext> getContextsByState(ControllerState state)
   {
      return contextsByState.get(state);
   }

   public void install(ControllerContext context) throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      if (context == null)
         throw new IllegalArgumentException("Null context");

      Object name = context.getName();
      if (name == null)
         throw new IllegalArgumentException("Null name " + context.toShortString());
      
      install(context, trace);
   }

   public void change(ControllerContext context, ControllerState state) throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      if (context == null)
         throw new IllegalArgumentException("Null context");

      if (state == null)
         throw new IllegalArgumentException("Null state");

      change(context, state, trace);
   }

   public void enableOnDemand(ControllerContext context) throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      if (context == null)
         throw new IllegalArgumentException("Null context");

      enableOnDemand(context, trace);
   }

   public ControllerContext uninstall(Object name)
   {
      return uninstall(name, 0);
   }

   // todo - some better way to find context's by name
   // currently the first one found is used
   protected ControllerContext uninstall(Object name, int level)
   {
      boolean trace = log.isTraceEnabled();

      if (name == null)
         throw new IllegalArgumentException("Null name");

      lockWrite();
      try
      {
         if (errorContexts.remove(name) != null && trace)
            log.trace("Tidied up context in error state: " + name);

         ControllerContext context = getRegisteredControllerContext(name, false);
         if (context != null)
         {
            if (trace)
               log.trace("Uninstalling " + context.toShortString());

            uninstallContext(context, ControllerState.NOT_INSTALLED, trace);

            try
            {
               unregisterControllerContext(context);
            }
            catch (Throwable t)
            {
               log.warn("Error unregistering context: " + context.toShortString() + " with name: " + name);
            }
         }
         else
         {
            for (AbstractController controller : childControllers)
            {
               context = controller.uninstall(name, level + 1);
               if (context != null)
                  break;
            }
         }
         if (context == null && level == 0)
            throw new IllegalStateException("Not installed: " + name);
         return context;
      }
      finally
      {
         unlockWrite();
      }
   }

   /**
    * Install a context
    *
    * @param context the context
    * @param trace   whether trace is enabled
    * @throws Throwable for any error
    */
   protected void install(ControllerContext context, boolean trace) throws Throwable
   {
      lockWrite();
      try
      {
         checkShutdown();

         Object name = context.getName();

         // Check the name is not already registered
         if (getRegisteredControllerContext(name, false) != null)
            throw new IllegalStateException(name + " is already installed.");

         // Check any alias is not already registered
         Set<Object> aliases = context.getAliases();
         if (aliases != null && aliases.isEmpty() == false)
         {
            for (Object alias : aliases)
            {
               if (getRegisteredControllerContext(alias, false) != null)
                  throw new IllegalStateException(alias + " an alias of " + name + " is already installed.");
            }
         }

         if (ControllerMode.AUTOMATIC.equals(context.getMode()))
            context.setRequiredState(ControllerState.INSTALLED);

         if (trace)
            log.trace("Installing " + context.toShortString());

         context.setController(this);
         DependencyInfo dependencies = context.getDependencyInfo();
         if (trace)
         {
            String dependsOn = null;
            if (dependencies != null)
            {
               Set set = dependencies.getIDependOn(null);
               if (set != null)
                  dependsOn = set.toString();
            }
            log.trace("Dependencies for " + name + ": " + dependsOn);
         }

         boolean ok = incrementState(context, trace);
         if (ok)
         {
            try
            {
               registerControllerContext(context);
            }
            catch (Throwable t)
            {
               // This is probably unreachable? But let's be paranoid
               ok = false;
               throw t;
            }
         }
         if (ok)
         {
            resolveContexts(trace);
         }
         else
         {
            errorContexts.remove(context);
            throw context.getError();
         }
      }
      finally
      {
         unlockWrite();
      }
   }

   /**
    * Change a context's state
    *
    * @param context the context
    * @param state   the required state
    * @param trace   whether trace is enabled
    * @throws Throwable for any error
    */
   protected void change(ControllerContext context, ControllerState state, boolean trace) throws Throwable
   {
      lockWrite();
      try
      {
         checkShutdown();

         ControllerState fromState = context.getState();
         int currentIndex = states.indexOf(fromState);
         int requiredIndex = states.indexOf(state);
         if (requiredIndex == -1)
            throw new IllegalArgumentException("Unknown state: " + state);

         if (currentIndex == requiredIndex)
         {
            if (trace)
               log.trace("No change required toState=" + state.getStateString() + " " + context.toShortString());
            return;
         }

         if (trace)
            log.trace("Change toState=" + state.getStateString() + " " + context.toShortString());

         context.setRequiredState(state);

         if (currentIndex < requiredIndex)
            resolveContexts(trace);
         else
         {
            while (currentIndex > requiredIndex)
            {
               uninstallContext(context, trace);
               currentIndex = states.indexOf(context.getState());
            }
         }
      }
      finally
      {
         unlockWrite();
      }
   }

   /**
    * Enable an on demand context
    *
    * @param context the context
    * @param trace   whether trace is enabled
    * @throws Throwable for any error
    */
   protected void enableOnDemand(ControllerContext context, boolean trace) throws Throwable
   {
      lockWrite();
      try
      {
         checkShutdown();

         if (ControllerMode.ON_DEMAND.equals(context.getMode()) == false)
            throw new IllegalStateException("Context is not ON DEMAND: " + context.toShortString());

         // Sanity check
         getRegisteredControllerContext(context.getName(), true);

         // Already done
         if (ControllerState.INSTALLED.equals(context.getRequiredState()))
            return;
         context.setRequiredState(ControllerState.INSTALLED);

         if (trace)
            log.trace("Enable onDemand: " + context.toShortString());

         onDemandEnabled = true;
      }
      finally
      {
         unlockWrite();
      }
   }

   /**
    * Increment state<p>
    * <p/>
    * This method must be invoked with the write lock taken.
    *
    * @param context the context
    * @param trace   whether trace is enabled
    * @return whether the suceeded
    */
   protected boolean incrementState(ControllerContext context, boolean trace)
   {
      ControllerState fromState = context.getState();

      Controller fromController = context.getController();
      Set fromContexts = null;

      int currentIndex = -1;
      if (ControllerState.ERROR.equals(fromState))
      {
         errorContexts.remove(context);
         Throwable error = null;
         unlockWrite();
         try
         {
            install(context, ControllerState.ERROR, ControllerState.NOT_INSTALLED);
         }
         catch (Throwable t)
         {
            error = t;
         }
         finally
         {
            lockWrite();
            if (error != null)
            {
               log.error("Error during initial installation: " + context.toShortString(), error);
               context.setError(error);
               errorContexts.put(context.getName(), context);
               return false;
            }
         }
         Set<ControllerContext> notInstalled = fromController.getContextsByState(ControllerState.NOT_INSTALLED);
         notInstalled.add(context);
         context.setState(ControllerState.NOT_INSTALLED);
      }
      else
      {
         currentIndex = states.indexOf(fromState);
         fromContexts = fromController.getContextsByState(fromState);
         if (fromContexts.contains(context) == false)
            throw new IllegalStateException("Context not found in previous state: " + context.toShortString());
      }

      int toIndex = currentIndex + 1;
      ControllerState toState = states.get(toIndex);

      unlockWrite();
      Throwable error = null;
      try
      {
         install(context, fromState, toState);

         if (fromContexts != null)
            fromContexts.remove(context);
         Controller toController = context.getController();
         Set<ControllerContext> toContexts = toController.getContextsByState(toState);
         toContexts.add(context);
         context.setState(toState);

         handleInstallLifecycleCallbacks(context, toState);
         resolveCallbacks(context, toState, true);
      }
      catch (Throwable t)
      {
         error = t;
      }
      finally
      {
         lockWrite();
         if (error != null)
         {
            log.error("Error installing to " + toState.getStateString() + ": " + context.toShortString(), error);
            uninstallContext(context, ControllerState.NOT_INSTALLED, trace);
            errorContexts.put(context.getName(), context);
            context.setError(error);
            return false;
         }
      }

      return true;
   }

   /**
    * Resolve unresolved contexts<p>
    * <p/>
    * This method must be invoked with the write lock taken
    *
    * @param trace whether trace is enabled
    */
   protected void resolveContexts(boolean trace)
   {
      boolean resolutions = true;
      while (resolutions || onDemandEnabled)
      {
         onDemandEnabled = false;
         resolutions = false;
         for (int i = 0; i < states.size() - 1; ++i)
         {
            ControllerState fromState = states.get(i);
            ControllerState toState = states.get(i + 1);
            if (resolveContexts(fromState, toState, trace))
            {
               resolutions = true;
               break;
            }
         }
      }

      if (trace)
      {
         for (int i = 0; i < states.size() - 1; ++i)
         {
            ControllerState state = states.get(i);
            ControllerState nextState = states.get(i + 1);
            Set<ControllerContext> stillUnresolved = contextsByState.get(state);
            if (stillUnresolved.isEmpty() == false)
            {
               for (ControllerContext ctx : stillUnresolved)
               {
                  if (advance(ctx))
                     log.trace("Still unresolved " + nextState.getStateString() + ": " + ctx);
               }
            }
         }
      }

      // resolve child controllers
      for (AbstractController controller : childControllers)
      {
         controller.lockWrite();
         try
         {
            controller.resolveContexts(trace);
         }
         finally
         {
            controller.unlockWrite();
         }
      }
   }

   /**
    * Resolve contexts<p>
    * <p/>
    * This method must be invoked with the write lock taken
    *
    * @param fromState the from state
    * @param toState   the to state
    * @param trace     whether trace is enabled
    * @return true when there were resolutions
    */
   protected boolean resolveContexts(ControllerState fromState, ControllerState toState, boolean trace)
   {
      boolean resolutions = false;
      Set<ControllerContext> unresolved = contextsByState.get(fromState);
      Set<ControllerContext> resolved = resolveContexts(unresolved, toState, trace);
      if (resolved.isEmpty() == false)
      {
         for (ControllerContext context : resolved)
         {
            Object name = context.getName();
            if (fromState.equals(context.getState()) == false)
            {
               if (trace)
                  log.trace("Skipping already installed " + name + " for " + toState.getStateString());
            }
            else if (installing.add(context) == false)
            {
               if (trace)
                  log.trace("Already installing " + name + " for " + toState.getStateString());
            }
            else
            {
               try
               {
                  if (trace)
                     log.trace("Dependencies resolved " + name + " for " + toState.getStateString());

                  if (incrementState(context, trace))
                  {
                     resolutions = true;
                     if (trace)
                        log.trace(name + " " + toState.getStateString());
                  }
               }
               finally
               {
                  installing.remove(context);
               }
            }
         }
      }

      return resolutions;
   }

   /**
    * Resolve contexts<p>
    * <p/>
    * This method must be invoked with the write lock taken
    *
    * @param contexts the contexts
    * @param state    the state
    * @param trace    whether trace is enabled
    * @return the set of resolved contexts
    */
   protected Set<ControllerContext> resolveContexts(Set<ControllerContext> contexts, ControllerState state, boolean trace)
   {
      HashSet<ControllerContext> result = new HashSet<ControllerContext>();

      if (contexts.isEmpty() == false)
      {
         for (ControllerContext ctx : contexts)
         {
            if (advance(ctx))
            {
               DependencyInfo dependencies = ctx.getDependencyInfo();
               if (dependencies.resolveDependencies(this, state))
                  result.add(ctx);
            }
         }
      }

      return result;
   }

   /**
    * Uninstall a context
    * <p/>
    * This method must be invoked with the write lock taken
    *
    * @param context the context to uninstall
    * @param toState the target state
    * @param trace   whether trace is enabled
    */
   protected void uninstallContext(ControllerContext context, ControllerState toState, boolean trace)
   {
      int targetState = states.indexOf(toState);
      if (targetState == -1)
         log.error("INTERNAL ERROR: unknown state " + toState + " states=" + states, new Exception("STACKTRACE"));

      while (true)
      {
         ControllerState fromState = context.getState();
         if (ControllerState.ERROR.equals(fromState))
            return;
         int currentState = states.indexOf(fromState);
         if (currentState == -1)
            log.error("INTERNAL ERROR: current state not found: " + context.toShortString(), new Exception("STACKTRACE"));
         if (targetState > currentState)
            return;
         else
            uninstallContext(context, trace);
      }
   }

   /**
    * Uninstall a context<p>
    * <p/>
    * This method must be invoked with the write lock taken
    *
    * @param context the context to uninstall
    * @param trace   whether trace is enabled
    */
   protected void uninstallContext(ControllerContext context, boolean trace)
   {
      Object name = context.getName();

      ControllerState fromState = context.getState();
      int currentIndex = states.indexOf(fromState);

      if (trace)
         log.trace("Uninstalling " + name + " from " + fromState.getStateString());

      Controller fromController = context.getController();

      Set<ControllerContext> fromContexts = fromController.getContextsByState(fromState);
      if (fromContexts == null || fromContexts.remove(context) == false)
         throw new Error("INTERNAL ERROR: context not found in previous state " + fromState.getStateString() + " context=" + context.toShortString(), new Exception("STACKTRACE"));

      DependencyInfo dependencies = context.getDependencyInfo();
      Set<DependencyItem> dependsOnMe = dependencies.getDependsOnMe(null);
      if (dependsOnMe.isEmpty() == false)
      {
         for (DependencyItem item : dependsOnMe)
         {
            if (item.isResolved())
            {
               ControllerState dependentState = item.getDependentState();
               if (dependentState == null || dependentState.equals(fromState))
               {
                  if (item.unresolved(this))
                  {
                     ControllerContext dependent = getContext(item.getName(), null);
                     if (dependent != null)
                     {
                        ControllerState whenRequired = item.getWhenRequired();
                        if (whenRequired == null)
                           whenRequired = ControllerState.NOT_INSTALLED;
                        int proposed = states.indexOf(whenRequired);
                        int actual = states.indexOf(dependent.getState());
                        if (proposed <= actual)
                           uninstallContext(dependent, whenRequired, trace);
                     }
                  }
               }
            }
         }
      }

      int toIndex = currentIndex - 1;
      if (toIndex == -1)
      {
         context.setError(new IllegalStateException("Cannot uninstall from " + fromState));
         return;
      }

      ControllerState toState = states.get(toIndex);

      unlockWrite();
      try
      {
         resolveCallbacks(context, fromState, false);
         handleUninstallLifecycleCallbacks(context, toState);

         uninstall(context, fromState, toState);

         Controller toController = context.getController();
         Set<ControllerContext> toContexts = toController.getContextsByState(toState);
         toContexts.add(context);
         context.setState(toState);
      }
      catch (Throwable t)
      {
         log.warn("Error uninstalling from " + fromState.getStateString() + ": " + context.toShortString(), t);
      }
      finally
      {
         lockWrite();
      }
   }

   /**
    * Add callback item under demand name.
    *
    * @param name demand name
    * @param isInstallPhase install or uninstall phase
    * @param callback callback item
    */
   protected void addCallback(Object name, boolean isInstallPhase, CallbackItem callback)
   {
      lockWrite();
      try
      {
         Map<Object, Set<CallbackItem>> map = (isInstallPhase ? installCallbacks : uninstallCallbacks);
         Set<CallbackItem> callbacks = map.get(name);
         if (callbacks == null)
         {
            callbacks = new HashSet<CallbackItem>();
            map.put(name, callbacks);
         }
         callbacks.add(callback);
      }
      finally
      {
         unlockWrite();
      }
   }

   /**
    * Remove callback item under demand name.
    *
    * @param name demand name
    * @param isInstallPhase install or uninstall phase
    * @param callback callback item
    */
   protected void removeCallback(Object name, boolean isInstallPhase, CallbackItem callback)
   {
      lockWrite();
      try
      {
         Map<Object, Set<CallbackItem>> map = (isInstallPhase ? installCallbacks : uninstallCallbacks);
         Set<CallbackItem> callbacks = map.get(name);
         if (callbacks != null)
         {
            callbacks.remove(callback);
            if (callbacks.isEmpty())
               map.remove(name);
         }
      }
      finally
      {
         unlockWrite();
      }
   }

   /**
    * Get calbacks from context.
    *
    * @param context current context
    * @param isInstallPhase install or uninstall phase
    * @return callback items from dependency info
    */
   protected Set<CallbackItem> getDependencyCallbacks(ControllerContext context, boolean isInstallPhase)
   {
      DependencyInfo di = context.getDependencyInfo();
      if (di != null)
      {
         return isInstallPhase ? di.getInstallItems() : di.getUninstallItems();
      }
      return null;
   }

   /**
    * Get the matching callbacks.
    *
    * @param name demand name
    * @param isInstallPhase install or uninstall phase
    * @return all matching registered callbacks or empty set if no such item
    */
   protected Set<CallbackItem> getCallbacks(Object name, boolean isInstallPhase)
   {
      lockRead();
      try
      {
         Map<Object, Set<CallbackItem>> map = (isInstallPhase ? installCallbacks : uninstallCallbacks);
         Set<CallbackItem> callbacks = map.get(name);
         return callbacks != null ? callbacks : new HashSet<CallbackItem>();
      }
      finally
      {
         unlockRead();
      }
   }

   /**
    * Resolve callbacks.
    *
    * @param callbacks the callbacks
    * @param state current context state
    * @param execute do execute callback
    * @param isInstallPhase install or uninstall phase
    * @param type install or uninstall type
    */
   protected void resolveCallbacks(Set<CallbackItem> callbacks, ControllerState state, boolean execute, boolean isInstallPhase, boolean type)
   {
      if (callbacks != null && callbacks.isEmpty() == false)
      {
         for (CallbackItem callback : callbacks)
         {
            if (callback.getWhenRequired().equals(state))
            {
               if (isInstallPhase)
               {
                  addCallback(callback.getIDependOn(), type, callback);
               }
               else
               {
                  removeCallback(callback.getIDependOn(), type, callback);
               }
               if (execute)
               {
                  try
                  {
                     callback.ownerCallback(this, isInstallPhase);
                  }
                  catch (Throwable t)
                  {
                     log.warn("Broken callback: " + callback, t);
                  }
               }
            }
         }
      }
   }

   /**
    * Resolve callback items.
    *
    * @param context current context
    * @param state current context state
    * @param isInstallPhase install or uninstall phase
    */
   protected void resolveCallbacks(ControllerContext context, ControllerState state, boolean isInstallPhase)
   {
      try
      {
         // existing owner callbacks
         Set<CallbackItem> installs = getDependencyCallbacks(context, true);
         resolveCallbacks(installs, state, isInstallPhase, isInstallPhase, true);
         Set<CallbackItem> uninstalls = getDependencyCallbacks(context, false);
         resolveCallbacks(uninstalls, state, isInstallPhase == false, isInstallPhase, false);

         // change callbacks, applied only if context is autowire candidate
         if (isAutowireCandidate(context))
         {
            // match callbacks by name
            Set<CallbackItem> existingCallbacks = getCallbacks(context.getName(), isInstallPhase);
            // match by classes
            Collection<Class<?>> classes = getClassesImplemented(context.getTarget());
            if (classes != null && classes.isEmpty() == false)
            {
               for (Class clazz : classes)
               {
                  existingCallbacks.addAll(getCallbacks(clazz, isInstallPhase));
               }
            }

            // Do the installs if we are at the required state
            if (existingCallbacks != null && existingCallbacks.isEmpty() == false)
            {
               for(CallbackItem callback : existingCallbacks)
               {
                  if (state.equals(callback.getDependentState()))
                  {
                     try
                     {
                        callback.changeCallback(this, context, isInstallPhase);
                     }
                     catch (Throwable t)
                     {
                        log.warn("Broken callback: " + callback, t);
                     }
                  }
               }
            }
         }
      }
      // let's make sure we suppress any exceptions
      catch (Throwable t)
      {
         log.warn("Cannot resolve callbacks.", t);
      }
   }

   protected void handleInstallLifecycleCallbacks(ControllerContext context, ControllerState state) throws Throwable
   {
      handleLifecycleCallbacks(context, state, true);
   }

   protected void handleUninstallLifecycleCallbacks(ControllerContext context, ControllerState state) throws Throwable
   {
      int index = states.indexOf(state);
      ControllerState oldState = states.get(index + 1);
      handleLifecycleCallbacks(context, oldState, false);
   }

   protected void handleLifecycleCallbacks(ControllerContext context, ControllerState state, boolean install) throws Throwable
   {
      DependencyInfo di = context.getDependencyInfo();
      List<LifecycleCallbackItem> callbacks = di.getLifecycleCallbacks();
      for (LifecycleCallbackItem callback : callbacks)
      {
         if (callback.getWhenRequired().equals(state))
         {
            if (install)
               callback.install(context);
            else
               callback.uninstall(context);
         }
      }
   }
   
   /**
    * Can we use this context for autowiring.
    *
    * @param context the context
    * @return true if context could be used for autowiring
    */
   protected boolean isAutowireCandidate(ControllerContext context)
   {
      return true;
   }

   /**
    * Get implemented classes.
    *
    * @param target target value / bean
    * @return collection of implementing classes by target
    */
   protected Collection<Class<?>> getClassesImplemented(Object target)
   {
      if (target == null)
         return null;
      Set<Class<?>> classes = new HashSet<Class<?>>();
      traverseClass(target.getClass(), classes);
      return classes;
   }

   /**
    * Recurse over classes.
    *
    * @param clazz current class
    * @param classes classes holder set
    */
   protected void traverseClass(Class clazz, Set<Class<?>> classes)
   {
      if (clazz != null && Object.class.equals(clazz) == false)
      {
         classes.add(clazz);
         traverseClass(clazz.getSuperclass(), classes);
         Class[] interfaces = clazz.getInterfaces();
         // traverse interfaces
         for (Class intface : interfaces)
         {
            traverseClass(intface, classes);
         }
      }
   }

   /**
    * Install a context<p>
    * <p/>
    * This method must be invoked with NO locks taken
    *
    * @param context   the context
    * @param fromState the from state
    * @param toState   the toState
    * @throws Throwable for any error
    */
   protected void install(ControllerContext context, ControllerState fromState, ControllerState toState) throws Throwable
   {
      context.install(fromState, toState);
   }

   /**
    * Uninstall a context<p>
    * <p/>
    * This method must be invoked with NO locks taken
    *
    * @param context   the context
    * @param fromState the from state
    * @param toState   the to state
    */
   protected void uninstall(ControllerContext context, ControllerState fromState, ControllerState toState)
   {
      context.uninstall(fromState, toState);
   }

   /**
    * Whether we should advance the context<p>
    * <p/>
    * This method must be invoked with the write lock taken
    *
    * @param context the context
    * @return true when we should advance the context
    */
   protected boolean advance(ControllerContext context)
   {
      ControllerMode mode = context.getMode();

      // Never advance for disabled
      if (ControllerMode.DISABLED.equals(mode))
         return false;

      ControllerState fromState = context.getState();
      int fromIndex = states.indexOf(fromState);
      ControllerState requiredState = context.getRequiredState();
      int requiredIndex = states.indexOf(requiredState);

      return fromIndex < requiredIndex;
   }

   /**
    * Lock for read
    */
   protected void lockRead()
   {
      lock.readLock().lock();
   }

   /**
    * Unlock for read
    */
   protected void unlockRead()
   {
      lock.readLock().unlock();
   }

   /**
    * Lock for write
    */
   protected void lockWrite()
   {
      lock.writeLock().lock();
   }

   /**
    * Unlock for write
    */
   protected void unlockWrite()
   {
      lock.writeLock().unlock();
   }

   /**
    * Get a registered context<p>
    * <p/>
    * This method should be invoked with at least the read lock taken
    *
    * @param name      the name with which to register it
    * @param mustExist whether to throw an error when the context does not exist
    * @return context the registered context
    * @throws IllegalArgumentException for null parameters
    * @throws IllegalStateException    if the context if must exist is true and the context does not exist
    */
   protected ControllerContext getRegisteredControllerContext(Object name, boolean mustExist)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");

      ControllerContext result = allContexts.get(name);
      if (mustExist && result == null)
         throw new IllegalStateException("Context does not exist with name: " + name);
      return result;
   }

   /**
    * Register a context and all its aliases<p>
    * <p/>
    * This method must be invoked with the write lock taken
    *
    * @param context the context to register
    * @throws IllegalArgumentException for null parameters
    * @throws IllegalStateException    if the context is already registered with that name or alias
    */
   protected void registerControllerContext(ControllerContext context)
   {
      if (context == null)
         throw new IllegalArgumentException("Null context");

      Set<Object> aliases = context.getAliases();

      // Register the context
      Object name = context.getName();
      registerControllerContext(name, context);

      // Register the aliases
      if (aliases != null && aliases.isEmpty() == false)
      {
         int ok = 0;
         try
         {
            for (Object alias : aliases)
            {
               registerControllerContext(alias, context);
               ++ok;
            }
         }
         finally
         {
            // It didn't work
            if (ok != aliases.size() && ok > 0)
            {
               // Unregister the aliases we added
               for (Object alias : aliases)
               {
                  if (ok-- == 0)
                     break;
                  try
                  {
                     unregisterControllerContext(alias);
                  }
                  catch (Throwable ignored)
                  {
                     log.debug("Error unregistering alias: " + alias, ignored);
                  }
               }

               // Unregister the context
               try
               {
                  unregisterControllerContext(name);
               }
               catch (Throwable ignored)
               {
                  log.debug("Error unregistering context with name: " + name, ignored);
               }
            }
         }
      }
   }

   /**
    * Unregister a context and all its aliases<p>
    * <p/>
    * This method must be invoked with the write lock taken
    *
    * @param context the context
    * @throws IllegalArgumentException for null parameters
    * @throws IllegalStateException    if the context is not registered
    */
   protected void unregisterControllerContext(ControllerContext context)
   {
      if (context == null)
         throw new IllegalArgumentException("Null context");

      Set<Object> aliases = context.getAliases();

      // Unregister the context
      Object name = context.getName();
      unregisterControllerContext(name);

      // Unegister the aliases
      if (aliases != null && aliases.isEmpty() == false)
      {
         for (Object alias : aliases)
         {
            try
            {
               unregisterControllerContext(alias);
            }
            catch (Throwable ignored)
            {
               log.debug("Error unregistering alias: " + alias, ignored);
            }
         }
      }
   }

   /**
    * Register a context<p>
    * <p/>
    * This method must be invoked with the write lock taken<p>
    * <p/>
    * NOTE: You probably want to use the {@link #registerControllerContext(ControllerContext)}
    *
    * @param name    the name with which to register it
    * @param context the context to register
    * @throws IllegalArgumentException for null parameters
    * @throws IllegalStateException    if the context is already registered with that name
    */
   protected void registerControllerContext(Object name, ControllerContext context)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (context == null)
         throw new IllegalArgumentException("Null context");

      if (allContexts.containsKey(name) == false)
         allContexts.put(name, context);
      else
         throw new IllegalStateException("Unable to register context" + context.toShortString() + " name already exists: " + name);
   }

   /**
    * Unregister a context<p>
    * <p/>
    * This method must be invoked with the write lock taken<p>
    * <p/>
    * NOTE: You probably want to use the {@link #unregisterControllerContext(ControllerContext)}
    *
    * @param name the name it was registered with
    * @throws IllegalArgumentException for null parameters
    */
   protected void unregisterControllerContext(Object name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      allContexts.remove(name);
   }
}
