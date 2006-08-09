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

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.util.JBossObject;
import org.jboss.util.collection.CollectionsFactory;

/**
 * Abstract controller.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractController extends JBossObject implements Controller
{
   /** The states in order List<ControllerState> */
   protected List<ControllerState> states = CollectionsFactory.createCopyOnWriteList();

   /** All contexts by name Map<Object, ControllerContext> */
   protected Map<Object, ControllerContext> allContexts = CollectionsFactory.createConcurrentReaderMap();

   /** The contexts by state Map<ControllerState, Set<ControllerContext>> */
   protected Map<ControllerState, Set<ControllerContext>> contextsByState = CollectionsFactory.createConcurrentReaderMap();

   /** The error contexts Set<ControllerContext> */
   protected Set<ControllerContext> errorContexts = CollectionsFactory.createCopyOnWriteSet();

   /** The contexts that are currently being installed */
   protected Set<ControllerContext> installing = CollectionsFactory.createCopyOnWriteSet();

   /** Whether an on demand context has been enabled */
   protected boolean onDemandEnabled = true;

   /**
    * Create an abstract controller
    * 
    * @throws Exception for any error
    */
   public AbstractController() throws Exception
   {
      addState(ControllerState.NOT_INSTALLED, null);
      addState(ControllerState.DESCRIBED, null);
      addState(ControllerState.INSTANTIATED, null);
      addState(ControllerState.CONFIGURED, null);
      addState(ControllerState.CREATE, null);
      addState(ControllerState.START, null);
      addState(ControllerState.INSTALLED, null);
   }

   public void addState(ControllerState state, ControllerState before)
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

      contextsByState.put(state, CollectionsFactory.createCopyOnWriteSet());
   }

   public synchronized ControllerContext getContext(Object name, ControllerState state)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      ControllerContext result = allContexts.get(name);
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

   public ControllerContext getInstalledContext(Object name)
   {
      return getContext(name, ControllerState.INSTALLED);
   }

   public synchronized Set<ControllerContext> getNotInstalled()
   {
      Set<ControllerContext> result = new HashSet<ControllerContext>(errorContexts);
      for (int i = 0; ControllerState.INSTALLED.equals(states.get(i)) == false; ++i)
      {
         Set<ControllerContext> stateContexts = contextsByState.get(states.get(i));
         result.addAll(stateContexts);
      }
      errorContexts.clear();
      return result;
   }

   public List<ControllerState> getStates()
   {
      return states;
   }

   public synchronized void install(ControllerContext context) throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      if (context == null)
         throw new IllegalArgumentException("Null context");

      Object name = context.getName();
      if (name == null)
         throw new IllegalArgumentException("Null name " + context.toShortString());

      if (allContexts.get(name) != null)
         throw new IllegalStateException(name + " is already installed.");

      install(context, trace);
   }

   public synchronized void change(ControllerContext context, ControllerState state) throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      if (context == null)
         throw new IllegalArgumentException("Null context");

      if (state == null)
         throw new IllegalArgumentException("Null state");

      change(context, state, trace);
   }

   public synchronized void enableOnDemand(ControllerContext context) throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      if (context == null)
         throw new IllegalArgumentException("Null context");

      enableOnDemand(context, trace);
   }

   public synchronized ControllerContext uninstall(Object name)
   {
      boolean trace = log.isTraceEnabled();

      if (name == null)
         throw new IllegalArgumentException("Null name");

      if (errorContexts.remove(name) && trace)
         log.trace("Tidied up context in error state: " + name);

      ControllerContext context = allContexts.get(name);
      if (context == null)
         throw new IllegalStateException("Not installed: " + name);

      if (trace)
         log.trace("Uninstalling " + context.toShortString());

      uninstallContext(context, ControllerState.NOT_INSTALLED, trace);

      allContexts.remove(name);
      return context;
   }

   /**
    * Install a context
    * 
    * @param context the context
    * @param trace whether trace is enabled
    * @throws Throwable for any error
    */
   protected void install(ControllerContext context, boolean trace) throws Throwable
   {
      Object name = context.getName();

      if (ControllerMode.AUTOMATIC.equals(context.getMode()))
         context.setRequiredState(ControllerState.INSTALLED);

      if (trace)
         log.trace("Installing " + context.toShortString());

      context.setController(this);
      DependencyInfo dependencies = context.getDependencyInfo();
      if (trace)
      {
         String dependsOn = null;
         if( dependencies != null )
         {
            Set set = dependencies.getIDependOn(null);
            if( set != null )
               dependsOn = set.toString();
         }
         log.trace("Dependencies for " + name + ": " + dependsOn);
      }

      if (incrementState(context, trace))
      {
         allContexts.put(context.getName(), context);
         resolveContexts(trace);
      }
      else
      {
         errorContexts.remove(context);
         throw context.getError();
      }
   }

   /**
    * Change a context's state
    * 
    * @param context the context
    * @param state the required state
    * @param trace whether trace is enabled
    * @throws Throwable for any error
    */
   protected void change(ControllerContext context, ControllerState state, boolean trace) throws Throwable
   {
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

   /**
    * Enable an on demand context
    * 
    * @param context the context
    * @param trace whether trace is enabled
    * @throws Throwable for any error
    */
   protected void enableOnDemand(ControllerContext context, boolean trace) throws Throwable
   {
      if (ControllerMode.ON_DEMAND.equals(context.getMode()) == false)
         throw new IllegalStateException("Context is not ON DEMAND: " + context.toShortString());

      if (allContexts.containsKey(context.getName()) == false)
         throw new IllegalStateException("Unknown context: " + context.toShortString());

      // Already done
      if (ControllerState.INSTALLED.equals(context.getRequiredState()))
         return;
      context.setRequiredState(ControllerState.INSTALLED);

      if (trace)
         log.trace("Enable onDemand: " + context.toShortString());

      onDemandEnabled = true;
   }

   /**
    * Increment state
    * 
    * @param context the context
    * @param trace whether trace is enabled
    * @return whether the suceeded
    */
   protected boolean incrementState(ControllerContext context, boolean trace)
   {
      ControllerState fromState = context.getState();

      Set fromContexts = null;

      int currentIndex = -1;
      if (ControllerState.ERROR.equals(fromState))
      {
         errorContexts.remove(context);
         try
         {
            install(context, ControllerState.ERROR, ControllerState.NOT_INSTALLED);
         }
         catch (Throwable t)
         {
            log.error("Error during initial installation: " + context.toShortString(), t);
            context.setError(t);
            errorContexts.add(context);
            return false;

         }
         Set<ControllerContext> notInstalled = contextsByState.get(ControllerState.NOT_INSTALLED);
         notInstalled.add(context);
      }
      else
      {
         currentIndex = states.indexOf(fromState);
         fromContexts = contextsByState.get(fromState);
         if (fromContexts.contains(context) == false)
            throw new IllegalStateException("Context not found in previous state: " + context.toShortString());
      }

      int toIndex = currentIndex + 1;
      ControllerState toState = states.get(toIndex);
      Set<ControllerContext> toContexts = contextsByState.get(toState);

      try
      {
         install(context, fromState, toState);
      }
      catch (Throwable t)
      {
         log.error("Error installing to " + toState.getStateString() + ": " + context.toShortString(), t);
         uninstallContext(context, ControllerState.NOT_INSTALLED, trace);
         errorContexts.add(context);
         context.setError(t);
         return false;
      }

      if (fromContexts != null)
         fromContexts.remove(context);
      toContexts.add(context);
      return true;
   }

   /**
    * Resolve unresolved contexts
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
         for (int i = 0; i < states.size()-1; ++i)
         {
            ControllerState fromState = states.get(i);
            ControllerState toState = states.get(i+1);
            if (resolveContexts(fromState, toState, trace))
            {
               resolutions = true;
               break;
            }
         }
      }

      if (trace)
      {
         for (int i = 0; i < states.size()-1; ++i)
         {
            ControllerState state = states.get(i);
            ControllerState nextState = states.get(i+1);
            Set<ControllerContext> stillUnresolved = contextsByState.get(state);
            if (stillUnresolved.isEmpty() == false)
            {
               for (Iterator j = stillUnresolved.iterator(); j.hasNext();)
               {
                  ControllerContext ctx = (ControllerContext) j.next();
                  if (advance(ctx))
                     log.trace("Still unresolved " + nextState.getStateString() + ": " + ctx);
               }
            }
         }
      }
   }

   /**
    * Resolve contexts
    * 
    * @param fromState the from state
    * @param toState the to state
    * @param trace whether trace is enabled
    * @return true when there were resolutions
    */
   protected boolean resolveContexts(ControllerState fromState, ControllerState toState, boolean trace)
   {
      boolean resolutions = false;
      Set<ControllerContext> unresolved = contextsByState.get(fromState);
      Set<ControllerContext> resolved = resolveContexts(unresolved, toState, trace);
      if (resolved.isEmpty() == false)
      {
         for (Iterator i = resolved.iterator(); i.hasNext();)
         {
            ControllerContext context = (ControllerContext) i.next();
            Object name = context.getName();
            if (installing.add(context) == false)
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
    * Resolve contexts
    * 
    * @param contexts the contexts
    * @param state the state
    * @param trace whether trace is enabled
    * @return the set of resolved contexts
    */
   protected Set<ControllerContext> resolveContexts(Set<ControllerContext> contexts, ControllerState state, boolean trace)
   {
      HashSet<ControllerContext> result = new HashSet<ControllerContext>();

      if (contexts.isEmpty() == false)
      {
         for (Iterator i = contexts.iterator(); i.hasNext();)
         {
            ControllerContext ctx = (ControllerContext) i.next();
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
    * 
    * @param context the context to uninstall
    * @param toState the target state
    * @param trace whether trace is enabled
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
    * Uninstall a context
    * 
    * @param context the context to uninstall
    * @param trace whether trace is enabled
    */
   protected void uninstallContext(ControllerContext context, boolean trace)
   {
      Object name = context.getName();

      ControllerState fromState = context.getState();
      int currentIndex = states.indexOf(fromState);

      if (trace)
         log.trace("Uninstalling " + name + " from " + fromState.getStateString());

      Set<ControllerContext> fromContexts = contextsByState.get(fromState);
      if (fromContexts == null || fromContexts.remove(context) == false)
      {
         log.error("INTERNAL ERROR: context not found in previous state " + fromState.getStateString() + " context=" + context.toShortString(), new Exception("STACKTRACE"));
         return;
      }

      DependencyInfo dependencies = context.getDependencyInfo();
      Set dependsOnMe = dependencies.getDependsOnMe(null);
      if (dependsOnMe.isEmpty() == false)
      {
         for (Iterator i = dependsOnMe.iterator(); i.hasNext();)
         {
            DependencyItem item = (DependencyItem) i.next();
            if (item.isResolved())
            {
               ControllerState dependentState = item.getDependentState();
               if (dependentState == null || dependentState.equals(fromState))
               {
                  item.unresolved(this);
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

      int toIndex = currentIndex-1;
      if (toIndex == -1)
      {
         context.setError(new IllegalStateException("Cannot uninstall from " + fromState));
         return;
      }

      ControllerState toState = states.get(toIndex);
      Set<ControllerContext> toContexts = contextsByState.get(toState);
      toContexts.add(context);

      try
      {
         uninstall(context, fromState, toState);
      }
      catch (Throwable t)
      {
         log.warn("Error uninstalling from " + fromState.getStateString() + ": " + context.toShortString(), t);
      }
   }

   /**
    * Install a context
    * 
    * @param context the context
    * @param fromState the from state
    * @param toState the toState
    * @throws Throwable for any error
    */
   protected void install(ControllerContext context, ControllerState fromState, ControllerState toState) throws Throwable
   {
      context.install(fromState, toState);
   }

   /**
    * Uninstall a context
    * 
    * @param context the context
    * @param fromState the from state
    * @param toState the to state
    */
   protected void uninstall(ControllerContext context, ControllerState fromState, ControllerState toState)
   {
      context.uninstall(fromState, toState);
   }

   /**
    * Whether we should advance the context
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
}
