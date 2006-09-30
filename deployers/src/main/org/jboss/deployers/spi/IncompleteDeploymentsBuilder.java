/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.deployers.spi;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.deployers.spi.deployment.MainDeployer;
import org.jboss.deployers.spi.structure.DeploymentContext;

/**
 * IncompleteDeploymentBuilder.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class IncompleteDeploymentsBuilder
{
   /**
    * Get the root cause of a throwable
    * 
    * @param original the original
    * @return the root
    */
   private static Throwable getCause(Throwable original)
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
   
   /**
    * Build an incomplete deployment
    * 
    * @param main the main deployer
    * @param controller the controller
    * @return the incomplte deployments
    */
   public static IncompleteDeployments build(MainDeployer main, Controller controller)
   {
      Map<String, Throwable> deploymentsInError = null;
      Collection<String> deploymentsMissingDeployer = null;
      Map<String, Throwable> contextsInError = null;
      Map<String, Set<MissingDependency>> contextsMissingDependencies = null;
      
      if (main != null)
      {
         Collection<DeploymentContext> errors = main.getErrors();
         if (errors != null && errors.isEmpty() == false)
         {
            deploymentsInError = new HashMap<String, Throwable>();
            for (DeploymentContext context : errors)
               deploymentsInError.put(context.getName(), getCause(context.getProblem()));
         }
         
         
         Collection<DeploymentContext> missingDeployer = main.getMissingDeployer();
         if (missingDeployer != null && missingDeployer.isEmpty() == false)
         {
            deploymentsMissingDeployer = new HashSet<String>();
            for (DeploymentContext context : missingDeployer)
               deploymentsMissingDeployer.add(context.getName());
         }
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
                     contextsInError.put(context.getName().toString(), getCause(context.getError()));
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
                              // TODO allow the item to better describe itself
                              actualStateString = "** UNRESOLVED " + item + " **";
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
      
      return new IncompleteDeployments(deploymentsInError, deploymentsMissingDeployer, contextsInError, contextsMissingDependencies);
   }
}
