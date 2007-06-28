/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.deployers.plugins.deployers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.dependency.plugins.AbstractController;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerContextActions;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.deployers.client.spi.IncompleteDeploymentException;
import org.jboss.deployers.client.spi.IncompleteDeployments;
import org.jboss.deployers.client.spi.MissingDependency;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.DeploymentState;
import org.jboss.deployers.spi.Ordered;
import org.jboss.deployers.spi.deployer.Deployer;
import org.jboss.deployers.spi.deployer.Deployers;
import org.jboss.deployers.spi.deployer.DeploymentStage;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.spi.deployer.managed.ManagedObjectCreator;
import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.logging.Logger;
import org.jboss.managed.api.ManagedObject;

/**
 * DeployersImpl.
 *
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DeployersImpl implements Deployers, ControllerContextActions
{
   /** The log */
   private static final Logger log = Logger.getLogger(DeployersImpl.class);
   
   /** The dependency state machine */
   private AbstractController controller;
   
   /** The deployment stages by name */
   private Map<String, DeploymentStage> stages = new ConcurrentHashMap<String, DeploymentStage>();
   
   /** The deployers */
   private Set<DeployerWrapper> deployers = new HashSet<DeployerWrapper>();

   /** The deployers by stage and type */
   private Map<String, List<Deployer>> deployersByStage = new HashMap<String, List<Deployer>>();
   
   /**
    * Create a new DeployersImpl.
    *
    * @param controller the controller
    * @throws IllegalArgumentException for a null controller
    */
   public DeployersImpl(AbstractController controller)
   {
      this(controller, null);
   }
   
   /**
    * Create a new DeployersImpl.
    * 
    * @param controller the controller
    * @param deployers the deployers
    * @throws IllegalArgumentException for a null controller
    */
   public DeployersImpl(AbstractController controller, Set<Deployer> deployers)
   {
      if (controller == null)
         throw new IllegalArgumentException("Null controller");
      this.controller = controller;
      
      // Add the standard stages
      addDeploymentStage(DeploymentStages.NOT_INSTALLED);
      addDeploymentStage(DeploymentStages.PARSE);
      addDeploymentStage(DeploymentStages.DESCRIBE);
      addDeploymentStage(DeploymentStages.CLASSLOADER);
      addDeploymentStage(DeploymentStages.POST_CLASSLOADER);
      addDeploymentStage(DeploymentStages.REAL);
      addDeploymentStage(DeploymentStages.INSTALLED);
      
      // Create the deployers
      if (deployers != null)
         setDeployers(deployers);
   }
   
   /**
    * Get the deployers.
    * 
    * @return the deployers.
    */
   public Set<DeployerWrapper> getDeployerWrappers()
   {
      return deployers;
   }
   
   /**
    * Set the deployers.
    * 
    * @param deployers the deployers.
    * @throws IllegalArgumentException for null deployers
    */
   public void setDeployers(Set<Deployer> deployers)
   {
      if (deployers == null)
         throw new IllegalArgumentException("Null deployers");
      
      // Remove all the old deployers that are not in the new set
      HashSet<Deployer> oldDeployers = new HashSet<Deployer>(this.deployers);
      oldDeployers.removeAll(deployers);
      for (Deployer deployer : oldDeployers)
         removeDeployer(deployer);
      
      // Add all the new deployers that were not already present
      HashSet<Deployer> newDeployers = new HashSet<Deployer>(deployers);
      newDeployers.removeAll(this.deployers);
      for (Deployer deployer : newDeployers)
         addDeployer(deployer);
   }

   /**
    * Add a deployer
    * 
    * @param deployer the deployer
    */
   public synchronized void addDeployer(Deployer deployer)
   {
      if (deployer == null)
         throw new IllegalArgumentException("Null deployer");

      DeploymentStage stage = deployer.getStage();
      if (stage == null)
         throw new IllegalArgumentException("Deployer has no stage: " + deployer);

      addDeploymentStage(stage);
      
      DeployerWrapper wrapper = new DeployerWrapper(deployer);
      
      // Ignore duplicates
      if (deployers.contains(wrapper))
         return;
      
      String stageName = stage.getName();
      List<Deployer> deployers = deployersByStage.get(stageName);
      if (deployers == null)
         deployers = Collections.emptyList();
      deployers = sort(deployers, wrapper);
      deployersByStage.put(stageName, deployers);
      
      this.deployers.add(wrapper);
      
      StringBuilder builder = new StringBuilder();
      builder.append("Added deployer ").append(deployer).append(" for stage ").append(stageName).append('\n');
      for (Deployer temp : getDeployersList(stageName))
      {
         builder.append(temp);
         builder.append("{inputs=").append(temp.getInputs());
         builder.append(" outputs=").append(temp.getOutputs());
         builder.append("}\n");
      }
      log.debug(builder);
   }

   /**
    * Remove a deployer
    * 
    * @param deployer the deployer
    */
   public synchronized void removeDeployer(Deployer deployer)
   {
      if (deployer == null)
         throw new IllegalArgumentException("Null deployer");
      deployers.remove(new DeployerWrapper(deployer));

      DeploymentStage stage = deployer.getStage();
      if (stage == null)
      {
         log.warn("Deployer has no stage: " + deployer);
         return;
      }
      
      String stageName = stage.getName();
      List<Deployer> deployers = deployersByStage.get(stageName);
      if (deployers == null)
         return;
      
      deployers.remove(deployer);
      if (deployers.isEmpty())
         deployersByStage.remove(stageName);
      
      log.debug("Removed deployer " + deployer + " from stage " + stageName);
   }

   /**
    * Add a deployment stage
    * 
    * @param stage the deployment stage
    */
   protected synchronized void addDeploymentStage(DeploymentStage stage)
   {
      if (stage == null)
         throw new IllegalArgumentException("Null stage");
      
      // Already done?
      String stageName = stage.getName();
      if (stages.containsKey(stageName))
         return;

      ControllerState preceeds = null;
      String before = stage.getBefore();
      String after = stage.getAfter();
      if (before != null || after != null)
      {
         // Determine where to put the stage
         List<ControllerState> states = controller.getStates();
         for (int i = 0; i < states.size(); ++i)
         {
            ControllerState state = states.get(i);
            String stateName = state.getStateString();
            if (before != null && before.equals(stateName))
            {
               preceeds = state;
               break;
            }
            if (after != null && after.equals(stateName))
            {
               if (i < states.size()-1)
               {
                  preceeds = states.get(i+1);
                  break;
               }
            }
         }
      }

      controller.addState(new ControllerState(stageName), preceeds);
      stages.put(stageName, stage);
      log.debug("Added stage " + stageName + " before " + preceeds);
   }

   public Map<String, ManagedObject> getManagedObjects(DeploymentContext context) throws DeploymentException
   {
      if (context == null)
         throw new IllegalArgumentException("Null context");
      
      Map<String, ManagedObject> managedObjects = new HashMap<String, ManagedObject>();
      for (DeployerWrapper deployer : deployers)
         deployer.build(context.getDeploymentUnit(), managedObjects);
      
      return managedObjects;
   }

   /**
    * Get the ManagedObjectBuilder for a deployer.
    * 
    * @param deployer - the deployer to set the ManagedObjectBuilder for.
    * @return managedObjectBuilder for deployer, may be null
    * @throws IllegalArgumentException for a null deployer
    */
   public ManagedObjectCreator getDeployerManagedObjectBuilder(Deployer deployer)
   {
      if (deployer == null)
         throw new IllegalArgumentException("Null deployer");
      
      ManagedObjectCreator result = null;
      for (DeployerWrapper wrapper : deployers)
      {
         if (wrapper.equals(deployer))
            result = wrapper.getManagedObjectCreator();
      }
      return result;
   }

   /**
    * Set the ManagedObjectBuilder for a deployer. This allows one to override the given deployer
    * ManagedObjectBuilder or assign one when the deployer does not provide a ManagedObjectBuilder.
    * 
    * @param deployer - the deployer to set the ManagedObjectBuilder for.
    * @param managedObjectCreator the managed object builder to set to the deployer
    * @throws IllegalArgumentException for a null deployer
    */
   public void setDeployerManagedObjectBuilder(Deployer deployer, ManagedObjectCreator managedObjectCreator)
   {
      if (deployer == null)
         throw new IllegalArgumentException("Null deployer");

      for (DeployerWrapper wrapper : deployers)
      {
         if (wrapper.equals(deployer))
            wrapper.setManagedObjectCreator(managedObjectCreator);
      }
   }

   public void process(List<DeploymentContext> deploy, List<DeploymentContext> undeploy)
   {
      // There is something to undeploy
      if (undeploy != null && undeploy.isEmpty() == false)
      {
         // Build a list in reverse order
         List<DeploymentControllerContext> toUndeploy = new ArrayList<DeploymentControllerContext>();
         for (int i = undeploy.size()-1; i >= 0; --i)
         {
            DeploymentContext context = undeploy.get(i);
            context.setState(DeploymentState.UNDEPLOYING);
            log.debug("Undeploying " + context.getName());
            DeploymentControllerContext deploymentControllerContext = context.getTransientAttachments().getAttachment(DeploymentControllerContext.class);
            if (deploymentControllerContext == null)
            {
               log.warn("DeploymentContext has no DeploymentControllerContext during undeploy request, ignoring: " + context);
            }
            else
            {
               toUndeploy.add(deploymentControllerContext);
            }
         }

         // Go through the states in reverse order
         List<ControllerState> states = controller.getStates();
         for (int i = states.size()-1; i >= 0; --i)
         {
            ControllerState state = states.get(i);
            for (DeploymentControllerContext deploymentControllerContext : toUndeploy)
            {
               DeploymentContext context = deploymentControllerContext.getDeploymentContext();
               if (ControllerState.ERROR.equals(context.getState()) == false)
               {
                  try
                  {
                     controller.change(deploymentControllerContext, state);
                  }
                  catch (Throwable t)
                  {
                     log.warn("Error during undeploy", t);
                     context.setState(DeploymentState.ERROR);
                     context.setProblem(t);
                  }
               }
            }
         }

         // Uninstall the contexts
         for (DeploymentControllerContext deploymentControllerContext : toUndeploy)
         {
            DeploymentContext context = deploymentControllerContext.getDeploymentContext();
            context.getTransientAttachments().removeAttachment(DeploymentControllerContext.class);
            try
            {
               controller.uninstall(deploymentControllerContext.getName());
               context.setState(DeploymentState.UNDEPLOYED);
               // TODO perform with the deployer that created the classloader?
               context.removeClassLoader();
               log.debug("Fully Undeployed " + context.getName());
            }
            catch (Throwable t)
            {
               log.warn("Error during uninstall", t);
               context.setState(DeploymentState.ERROR);
               context.setProblem(t);
            }
         }
      }
      
      // There is something to deploy
      if (deploy != null && deploy.isEmpty() == false)
      {
         // Create the controller contexts
         for (DeploymentContext context : deploy)
         {
            DeploymentControllerContext deploymentControllerContext = new DeploymentControllerContext(context, this);
            try
            {
               controller.install(deploymentControllerContext);
               context.setState(DeploymentState.DEPLOYING);
               log.debug("Deploying " + context.getName());
               context.getTransientAttachments().addAttachment(DeploymentControllerContext.class, deploymentControllerContext);
            }
            catch (Throwable t)
            {
               context.setState(DeploymentState.ERROR);
               context.setProblem(t);
            }
         }

         // Go through the states in order
         List<ControllerState> states = controller.getStates();
         for (ControllerState state : states)
         {
            for (DeploymentContext context : deploy)
            {
               if (DeploymentState.ERROR.equals(context.getState()) == false)
               {
                  DeploymentControllerContext deploymentControllerContext = context.getTransientAttachments().getAttachment(DeploymentControllerContext.class);
                  try
                  {
                     controller.change(deploymentControllerContext, state);
                  }
                  catch (Throwable t)
                  {
                     context.setState(DeploymentState.ERROR);
                     context.setProblem(t);
                  }
               }
            }
         }
      }
   }

   /**
    * Get the root cause of a throwable
    * 
    * @param original the original
    * @return the root
    */
   private static Throwable getRootCause(Throwable original)
   {
      if (original == null)
         return null;
      Throwable result = original;
      Throwable cause = result.getCause();
      while (cause != null)
      {
         result = cause;
         cause = cause.getCause();
      }
      return result;
   }

   public void checkComplete(Collection<DeploymentContext> errors, Collection<DeploymentContext> missingDeployer) throws DeploymentException
   {
      Map<String, Throwable> deploymentsInError = null;
      Collection<String> deploymentsMissingDeployer = null;
      Map<String, Throwable> contextsInError = null;
      Map<String, Set<MissingDependency>> contextsMissingDependencies = null;

      if (errors != null && errors.isEmpty() == false)
      {
         deploymentsInError = new HashMap<String, Throwable>();
         for (DeploymentContext context : errors)
            deploymentsInError.put(context.getName(), getRootCause(context.getProblem()));
      }

      if (missingDeployer != null && missingDeployer.isEmpty() == false)
      {
         deploymentsMissingDeployer = new HashSet<String>();
         for (DeploymentContext context : missingDeployer)
            deploymentsMissingDeployer.add(context.getName());
      }
      
      
      if (controller != null)
      {
         List<ControllerState> states = controller.getStates();
         
         Set<ControllerContext> notInstalled = controller.getNotInstalled();
         if (notInstalled.isEmpty() == false)
         {
            for (Iterator<ControllerContext> i = notInstalled.iterator(); i.hasNext();)
            {
               ControllerContext context = i.next();
               if (context.getState().equals(context.getRequiredState()))
                  i.remove();
            }
            if (notInstalled.isEmpty() == false)
            {
               contextsInError = new HashMap<String, Throwable>();
               contextsMissingDependencies = new HashMap<String, Set<MissingDependency>>();
               for (ControllerContext context : notInstalled)
               {
                  if (context.getState().equals(ControllerState.ERROR))
                     contextsInError.put(context.getName().toString(), getRootCause(context.getError()));
                  else
                  {
                     String name = context.getName().toString();
                     Set<MissingDependency> dependencies = new HashSet<MissingDependency>();
                     DependencyInfo dependsInfo = context.getDependencyInfo();
                     for (DependencyItem item : dependsInfo.getIDependOn(null))
                     {
                        if (item.isResolved() == false)
                        {
                           String dependency;
                           ControllerState actualState = null;
                           String actualStateString;
                           Object iDependOn = item.getIDependOn();
                           if (iDependOn == null)
                           {
                              dependency = "<UNKNOWN>";
                              actualStateString = "** UNRESOLVED " + item.toHumanReadableString() + " **";
                           }
                           else
                           {
                              dependency = iDependOn.toString();
                              ControllerContext other = controller.getContext(item.getIDependOn(), null);
                              if (other == null)
                                 actualStateString = "** NOT FOUND **";
                              else
                              {
                                 actualState = other.getState();
                                 actualStateString = actualState.getStateString();
                              }
                           }
                           ControllerState requiredState = item.getWhenRequired();
                           String requiredStateString = requiredState.getStateString();
                           int required = states.indexOf(requiredState);
                           int actual = actualState == null ? -1 : states.indexOf(actualState);
                           if (required > actual)
                           {
                              MissingDependency missing = new MissingDependency(name, dependency, requiredStateString, actualStateString);
                              dependencies.add(missing);
                           }
                        }
                     }
                     contextsMissingDependencies.put(name, dependencies);
                  }
               }
            }
         }
      }
      
      IncompleteDeployments incomplete = new IncompleteDeployments(deploymentsInError, deploymentsMissingDeployer, contextsInError, contextsMissingDependencies);
      if (incomplete.isIncomplete())
         throw new IncompleteDeploymentException(incomplete);
   }

   public void checkComplete(DeploymentContext context) throws DeploymentException
   {
      Map<String, Throwable> deploymentsInError = null;
      Collection<String> deploymentsMissingDeployer = null;
      Map<String, Throwable> contextsInError = null;
      Map<String, Set<MissingDependency>> contextsMissingDependencies = null;

      if (context == null)
         throw new IllegalArgumentException("Null context");
      
      Throwable problem = context.getProblem();
      if (problem != null)
         deploymentsInError = Collections.singletonMap(context.getName(), problem);
      
      if (context.isDeployed() == false)
         deploymentsMissingDeployer = Collections.singleton(context.getName());

      // TODO go through controller contexts for the deployment + related contexts
      
      IncompleteDeployments incomplete = new IncompleteDeployments(deploymentsInError, deploymentsMissingDeployer, contextsInError, contextsMissingDependencies);
      if (incomplete.isIncomplete())
         throw new IncompleteDeploymentException(incomplete);
   }

   public void install(ControllerContext context, ControllerState fromState, ControllerState toState) throws Throwable
   {
      DeploymentControllerContext deploymentControllerContext = (DeploymentControllerContext) context;
      String stageName = toState.getStateString();
      
      DeploymentContext deploymentContext = deploymentControllerContext.getDeploymentContext();
      try
      {
         doInstall(deploymentContext, stageName, true);
      }
      finally
      {
         if (ControllerState.INSTALLED.equals(toState) && DeploymentState.DEPLOYING.equals(deploymentContext.getState()))
         {
            log.debug("Fully Deployed " + context.getName());
            deploymentContext.setState(DeploymentState.DEPLOYED);
            
         }
      }
   }

   /**
    * Do the install
    * 
    * @param context the context
    * @param stageName the stage
    * @param doComponents whether to do components
    * @throws Throwable for any problem
    */
   protected void doInstall(DeploymentContext context, String stageName, boolean doComponents) throws Throwable
   {
      List<Deployer> theDeployers = getDeployersList(stageName);
      
      if (log.isTraceEnabled())
         log.trace("Deployers for " + stageName + " " + theDeployers);
      
      if (theDeployers.isEmpty() == false)
      {
         int i = 0;
         try
         {
            while (i < theDeployers.size())
            {
               Deployer deployer = theDeployers.get(i);
               doInstall(deployer, context, doComponents);
               ++i;
            }
         }
         catch (Throwable t)
         {
            context.setState(DeploymentState.ERROR);
            context.setProblem(t);
            for (int j = i-1; j >= 0; --j)
            {
               Deployer deployer = theDeployers.get(j);
               doUninstall(deployer, context, true);
            }
            throw t;
         }
      }
   }

   /**
    * Do the install
    * 
    * @param deployer the deployer
    * @param context the context
    * @param doComponents whether to do components
    * @throws Throwable for any problem
    */
   protected void doInstall(Deployer deployer, DeploymentContext context, boolean doComponents) throws Throwable
   {
      List<DeploymentContext> currentComponents = context.getComponents();
      // Take a copy of the components so we don't start looping on newly added components
      // in the component deployers
      List<DeploymentContext> components = null;
      if (currentComponents != null && currentComponents.isEmpty() == false)
         components = new ArrayList<DeploymentContext>(currentComponents);

      DeploymentUnit unit = context.getDeploymentUnit();
      if (isRelevant(deployer, unit, context.isTopLevel(), context.isComponent()))
         deployer.deploy(unit);
      else if (log.isTraceEnabled())
         log.trace("Deployer " + deployer + " not relevant for " + context.getName());

      if (doComponents && components != null)
      {
         try
         {
            for (int i = 0; i < components.size(); ++i)
            {
               DeploymentContext component = components.get(i);
               try
               {
                  doInstall(deployer, component, true);
               }
               catch (DeploymentException e)
               {
                  // Unwind the previous components
                  for (int j = i - 1; j >= 0; --j)
                  {
                     component = components.get(j);
                     doUninstall(deployer, component, true);
                  }
                  throw e;
               }
            }
         }
         catch (DeploymentException e)
         {
            doUninstall(deployer, context, false);
            throw e;
         }
      }
   }
   
   public void uninstall(ControllerContext context, ControllerState fromState, ControllerState toState)
   {
      DeploymentControllerContext deploymentControllerContext = (DeploymentControllerContext) context;
      String stageName = fromState.getStateString();
      
      DeploymentContext deploymentContext = deploymentControllerContext.getDeploymentContext();
      doUninstall(deploymentContext, stageName, true);
   }

   /**
    * Do the uninstall
    * 
    * @param context the context
    * @param stageName the stage
    * @param doComponents whether to do components
    */
   protected void doUninstall(DeploymentContext context, String stageName, boolean doComponents)
   {
      List<Deployer> theDeployers = getDeployersList(stageName);
      
      if (log.isTraceEnabled())
         log.trace("Deployers for " + stageName + " " + theDeployers);

      if  (theDeployers.isEmpty() == false)
      {
         for (int i = theDeployers.size()-1; i >= 0; --i)
         {
            Deployer deployer = theDeployers.get(i);
            doUninstall(deployer, context, doComponents);
         }
      }
   }

   /**
    * Do the uninstall
    *
    * @param deployer the deployer
    * @param context the context
    * @param doComponents whether to do components
    */
   protected void doUninstall(Deployer deployer, DeploymentContext context, boolean doComponents)
   {
      if (doComponents)
      {
         List<DeploymentContext> components = context.getComponents();
         if (components != null && components.isEmpty() == false)
         {
            for (int i = components.size()-1; i >=  0; --i)
            {
               DeploymentContext component = components.get(i);
               doUninstall(deployer, component, doComponents);
            }
         }
      }

      DeploymentUnit unit = context.getDeploymentUnit();
      if (isRelevant(deployer, unit, context.isTopLevel(), context.isComponent()))
         deployer.undeploy(unit);
      else if (log.isTraceEnabled())
         log.trace("Deployer " + deployer + " not relevant for " + context.getName());
   }
   
   /**
    * Build a list of  deployers for this stage
    * 
    * @param stageName the stage name
    * @return the deployers
    */
   protected synchronized List<Deployer> getDeployersList(String stageName)
   {
      List<Deployer> deployers = deployersByStage.get(stageName);
      if (deployers == null || deployers.isEmpty())
         return Collections.emptyList();
      
      return deployers;
   }
   
   /**
    * Test whether a deployer is relevant
    * 
    * @param deployer deployer
    * @param unit the deployment unit
    * @param isTopLevel whether this is a top level deployment
    * @param isComponent whether this is a component
    * @return the deployers
    */
   protected boolean isRelevant(Deployer deployer, DeploymentUnit unit, boolean isTopLevel, boolean isComponent)
   {
      // Deployer only wants top level
      if (deployer.isTopLevelOnly() && isTopLevel == false)
         return false;

      // Deployer only wants components
      if (deployer.isComponentsOnly() && isComponent == false)
         return false;

      // Deployer doesn't wants components
      if (deployer.isWantComponents() == false && isComponent)
         return false;
      
      if (deployer.isAllInputs() == false)
      {
         // No attachment for the input type
         Class<?> input = deployer.getInput();
         if (input != null && unit.getAttachment(input) == null)
            return false;
      }
      return true;
   }
   
   /**
    * Sort the deployers
    * 
    * @param original the original deployers
    * @param newDeployer the new deployer
    * @return the sorted deployers
    */
   protected List<Deployer> sort(List<Deployer> original, Deployer newDeployer)
   {
      List<Deployer> result = new ArrayList<Deployer>(original);
      result.add(newDeployer);
      
      // Bubble sort :-)
      boolean changed = true;
      while (changed)
      {
         changed = false;

         for (int i = 0; i < result.size() -1; ++i)
         {
            int j = i+1;
            
            Deployer one = result.get(i);
            Deployer two = result.get(j);
            
            Set<String> oneOutputs = one.getOutputs();
            
            // Don't move if one outputs something for two
            boolean swap = true;
            if (oneOutputs.isEmpty() == false)
            {
               Set<String> twoInputs = two.getInputs();
               for (String output : oneOutputs)
               {
                  if (twoInputs.contains(output))
                  {
                     swap = false;
                     break;
                  }
               }
               
               if (swap == false)
                  continue;
            }
            
            // Move if one inputs from two
            swap = false;
            Set<String> twoOutputs = two.getOutputs();
            if (twoOutputs.isEmpty() == false)
            {
               Set<String> oneInputs = one.getInputs();
               for (String output : twoOutputs)
               {
                  if (oneInputs.contains(output))
                  {
                     swap = true;
                     break;
                  }
               }
            }
            
            // Move if the order is not correct
            if (Ordered.COMPARATOR.compare(one, two) > 0)
               swap = true;
            
            if (swap)
            {
               Collections.swap(result, i, j);
               changed = true;
            }
         }
      }
      
      // Now check the consistency
      // The new deployer should be before anything that accepts its outputs
      Set<String> outputs = newDeployer.getOutputs();
      if  (outputs.isEmpty() == false)
      {
         int location = result.indexOf(newDeployer);
         for (int i = 0; i < location; ++i)
         {
            Deployer other = result.get(i);
            Set<String> otherInputs = other.getInputs();
            Set<String> otherOutputs = other.getOutputs();
            if (otherInputs.isEmpty() == false)
            {
               for (String input : otherInputs)
               {
                  // Ignore transient usage
                  if (outputs.contains(input) && otherOutputs.contains(input) == false)
                  {
                     StringBuilder builder = new StringBuilder();
                     builder.append("Cannot add ").append(newDeployer).append(" it will cause a loop\n");
                     for (Deployer temp : result)
                     {
                        builder.append(temp);
                        builder.append("{inputs=").append(temp.getInputs());
                        builder.append(" outputs=").append(temp.getOutputs());
                        builder.append("}\n");
                     }
                     throw new IllegalStateException(builder.toString());
                  }
               }
            }
         }
      }
      
      return result;
   }
}
