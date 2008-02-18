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
package org.jboss.deployers.plugins.classloading;

import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.deployers.spi.deployer.DeploymentStages;
import org.jboss.deployers.structure.spi.classloading.Requirement;
import org.jboss.util.JBossStringBuilder;

/**
 * RequirementDependencyItem.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class RequirementDependencyItem extends AbstractDependencyItem
{
   private static final ControllerState CLASSLOADER = new ControllerState(DeploymentStages.CLASSLOADER.getName());

   /** The module */
   private Module module;
   
   /** The requirement */
   private Requirement requirement;
   
   /**
    * Create a new RequirementDependencyItem.
    * 
    * @param module the module
    * @param requirement the requirement
    * @throws IllegalArgumentException for a null parameter
    */
   public RequirementDependencyItem(Module module, Requirement requirement)
   {
      super(module != null ? module.getName() : null, null, CLASSLOADER, CLASSLOADER);
      if (module == null)
         throw new IllegalArgumentException("Null module");
      if (requirement == null)
         throw new IllegalArgumentException("Null requirement");
      this.module = module;
      this.requirement = requirement;
   }
   
   public boolean resolve(Controller controller)
   {
      Object iDependOn = module.resolve(controller, requirement);
      if (iDependOn != null)
      {
         ControllerContext context = controller.getContext(iDependOn, CLASSLOADER);
         if (context != null)
         {
            setIDependOn(context.getName());
            addDependsOnMe(controller, context);
            setResolved(true);
         }
         else
         {
            setResolved(false);
         }
      }
      return isResolved();
   }

   public boolean unresolved(Controller controller)
   {
      setIDependOn(null);
      setResolved(false);
      return true;
   }
   
   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      buffer.append(" requirement=").append(requirement);
   }
   
   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(getName()).append(" ").append(requirement);
   }

   public String toHumanReadableString()
   {
      return requirement.toString();
   }
}
