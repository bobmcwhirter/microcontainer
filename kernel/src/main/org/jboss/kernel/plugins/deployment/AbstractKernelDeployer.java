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
package org.jboss.kernel.plugins.deployment;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.dependency.AbstractKernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.logging.Logger;
import org.jboss.util.JBossStringBuilder;

/**
 * An Kernel deployer.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="mailto:les.hazlewood@jboss.org">Les A. Hazlewood</a>
 * @version $Revision$
 */
public class AbstractKernelDeployer
{
   /** The log */
   private static final Logger log = Logger.getLogger(AbstractKernelDeployer.class);
   
   /** The kernel */
   protected Kernel kernel;

   /** The controller */
   protected KernelController controller;
   
   /** The required state */
   protected ControllerState requiredState;
   
   /** The mode */
   protected ControllerMode mode;
   
   /**
    * Create a new kernel deployer
    * 
    * @param kernel the kernel
    */
   public AbstractKernelDeployer(Kernel kernel)
   {
      this(kernel, null, null);
   }

   /**
    * Create a new kernel deployer with mode
    *
    * @param kernel the kernel
    * @param mode the controller mode
    */
   public AbstractKernelDeployer(Kernel kernel, ControllerMode mode)
   {
      this(kernel, null, mode);
   }

   /**
    * Create a new kernel deployer
    * 
    * @param kernel the kernel
    * @param requiredState the required state
    * @param mode the controller mode
    */
   public AbstractKernelDeployer(final Kernel kernel, ControllerState requiredState, ControllerMode mode)
   {
      if (kernel == null)
         throw new IllegalArgumentException("Null kernel");
      this.kernel = kernel;
      PrivilegedAction<Object> action = new PrivilegedAction<Object>()
      {
         public Object run()
         {
            controller = kernel.getController();
            return null;
         }
      };
      AccessController.doPrivileged(action);
      this.requiredState = requiredState;
      this.mode = mode;
   }

   /**
    * Deploy a deployment
    * 
    * @param deployment the deployment
    * @throws Throwable for any error
    */
   public void deploy(final KernelDeployment deployment) throws Throwable
   {
      if (deployment.isInstalled())
         throw new IllegalArgumentException("Already installed " + deployment.getName());

      try
      {
         deployBeans(controller, deployment);
         deployment.setInstalled(true);
      }
      catch (Throwable t)
      {
         undeploy(deployment);
         throw t;
      }
   }

   /**
    * Change a deployments state
    * 
    * @param deployment the deployment
    * @param state the state
    * @throws Throwable for any error
    */
   public void change(KernelDeployment deployment, ControllerState state) throws Throwable
   {
      if (deployment.isInstalled() == false)
         throw new IllegalStateException("Not installed " + deployment.getName());

      changeBeans(controller, deployment, state);
   }
   
   /**
    * Undeploy a deployment
    * 
    * @param deployment the deployment
    */
   public void undeploy(KernelDeployment deployment)
   {
      undeployBeans(controller, deployment);
      deployment.setInstalled(false);
   }
   
   /**
    * Validate all deployments
    * 
    * @throws Throwable for any error
    */
   public void validate() throws Throwable
   {
      Set<ControllerContext> notInstalled = controller.getNotInstalled();
      internalValidate(notInstalled);
   }
   
   /**
    * Validate a deployment
    * 
    * @param deployment the deployment
    * @throws Throwable for any error
    */
   public void validate(KernelDeployment deployment) throws Throwable
   {
      Set<ControllerContext> notInstalled = new HashSet<ControllerContext>(deployment.getInstalledContexts());
      internalValidate(notInstalled);
   }
   
   /**
    * Validate a deployment
    * 
    * @param notInstalled the not installed deployments
    * @throws Throwable for any error
    */
   protected void internalValidate(Set<ControllerContext> notInstalled) throws Throwable
   {
      List<ControllerState> states = controller.getStates();
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
            HashSet<ControllerContext> errors = new HashSet<ControllerContext>();
            HashSet<ControllerContext> incomplete = new HashSet<ControllerContext>();
            for (ControllerContext ctx : notInstalled)
            {
               if (ctx.getState().equals(ControllerState.ERROR))
                  errors.add(ctx);
               else
                  incomplete.add(ctx);
            }
            JBossStringBuilder buffer = new JBossStringBuilder();
            buffer.append("Incompletely deployed:\n");
            if (errors.size() != 0)
            {
               buffer.append("\n*** DEPLOYMENTS IN ERROR: Name -> Error\n");
               for (ControllerContext ctx : errors)
               {
                  buffer.append(ctx.getName()).append(" -> ").append(ctx.getError().toString()).append('\n');
               }
            }
            if (incomplete.size() != 0)
            {
               buffer.append("\n*** DEPLOYMENTS MISSING DEPENDENCIES: Name -> Dependency{Required State:Actual State}\n");
               for (ControllerContext ctx : incomplete)
               {
                  Object name = ctx.getName();
                  buffer.append(name).append(" -> ");
                  DependencyInfo dependsInfo = ctx.getDependencyInfo();
                  Set<DependencyItem> depends = dependsInfo.getIDependOn(null);
                  boolean first = true;
                  for (DependencyItem item : depends)
                  {
                     ControllerState dependentState = item.getDependentState();
                     ControllerState otherState = null;
                     ControllerContext other = null; 
                     Object iDependOn = item.getIDependOn();

                     if (name.equals(iDependOn) == false)
                     {
                        if (iDependOn != null)
                        {
                           other = controller.getContext(iDependOn, null);
                           if (other != null)
                              otherState = other.getState();
                        }

                        boolean print = true;
                        if (otherState != null && otherState.equals(ControllerState.ERROR) == false)
                        {
                           int index1 = states.indexOf(dependentState);
                           int index2 = states.indexOf(otherState);
                           if (index2 >= index1)
                              print = false;
                        }

                        if (print)
                        {
                           if (first)
                              first = false;
                           else
                              buffer.append(", ");

                           buffer.append(iDependOn).append('{').append(dependentState.getStateString());
                           buffer.append(':');
                           if (iDependOn == null)
                           {
                              buffer.append("** UNRESOLVED " + item.toHumanReadableString() + " **");
                           }
                           else
                           {
                              if (other == null)
                                 buffer.append("** NOT FOUND **");
                              else
                                 buffer.append(otherState.getStateString());
                           }
                           buffer.append('}');
                        }
                     }
                  }
                  buffer.append('\n');
               }
            }
            throw new IllegalStateException(buffer.toString());
         }
      }
   }

   /**
    * Deploy the beans in a deployment
    * 
    * @param controller the controller
    * @param deployment the deployment
    * @throws Throwable for any error
    */
   protected void deployBeans(KernelController controller, KernelDeployment deployment) throws Throwable
   {
      List<BeanMetaData> beans = deployment.getBeans();
      if (beans != null)
      {
         for (BeanMetaData metaData : beans)
         {
            KernelControllerContext context = deployBean(controller, metaData);
            deployment.addInstalledContext(context);
         }
      }
   }

   /**
    * Deploy a bean
    * 
    * @param controller the controller
    * @param bean the bean metadata
    * @return the KernelControllerContext
    * @throws Throwable for any error
    */
   protected KernelControllerContext deployBean(KernelController controller, BeanMetaData bean) throws Throwable
   {
      KernelControllerContext context = new AbstractKernelControllerContext(null, bean, null);
      if (requiredState != null)
         context.setRequiredState(requiredState);
      if (mode != null)
         context.setMode(mode);

      controller.install(context);
      return context;
   }

   /**
    * Change the beans in a deployment
    * 
    * @param controller the controller
    * @param deployment the deployment
    * @param state the state
    * @throws Throwable for any error
    */
   protected void changeBeans(KernelController controller, KernelDeployment deployment, ControllerState state) throws Throwable
   {
      List<KernelControllerContext> contexts = deployment.getInstalledContexts();
      if (contexts != null)
      {
         for (KernelControllerContext context : contexts)
         {
            changeBean(controller, context, state);
         }
      }
   }

   /**
    * Change a bean
    * 
    * @param controller the controller
    * @param context the context
    * @param state the state
    * @throws Throwable for any error
    */
   protected void changeBean(KernelController controller, KernelControllerContext context, ControllerState state) throws Throwable
   {
      if (ControllerMode.MANUAL.equals(context.getMode()) && ControllerState.ERROR.equals(context.getState()) == false)
         controller.change(context, state);
   }

   /**
    * Undeploy the beans in a deployment
    * 
    * @param controller the controller
    * @param deployment the deployment
    */
   protected void undeployBeans(KernelController controller, KernelDeployment deployment)
   {
      List<KernelControllerContext> contexts = deployment.getInstalledContexts();
      if (contexts.isEmpty() == false)
      {
         for (ListIterator<KernelControllerContext> i = contexts.listIterator(contexts.size()); i.hasPrevious();)
         {
            KernelControllerContext context = i.previous();
            try
            {
               undeployBean(controller, context);
               deployment.removeInstalledContext(context);
            }
            catch (Throwable ignored)
            {
               log.warn("Ignored error during uninstall of " + context, ignored);
            }
         }
      }
   }

   /**
    * Undeploy a bean
    * 
    * @param controller the controller
    * @param context the context of the bean
    * @throws Throwable for any error
    */
   protected void undeployBean(KernelController controller, KernelControllerContext context) throws Throwable
   {
      if (controller.isShutdown() == false)
         controller.uninstall(context.getName());
      else
         log.debug("Not undeploying " + context.getName() + " the controller is shutdown!");
   }
}
