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
package org.jboss.reliance.drools.dependency;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.reliance.drools.core.RuleDependencyCheck;

/**
 * Drools based dependency.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class RuleBasedDependencyItem extends AbstractDependencyItem
{
   private Object action;
   private Object[] args;
   private String checkName = "identity";
   private boolean resolvedOnNullIdentity;

   public RuleBasedDependencyItem(Object name, Object action, Object... args)
   {
      this(name, action, null, null, args);
   }

   public RuleBasedDependencyItem(Object name, Object action, ControllerState whenRequired, ControllerState dependentState, Object... args)
   {
      super(name, null, whenRequired, dependentState);
      this.action = action;
      this.args = args;
   }

   public boolean resolve(Controller controller)
   {
      ControllerContext checkContext;
      if (getDependentState() == null)
         checkContext = controller.getInstalledContext(checkName);
      else
         checkContext = controller.getContext(checkName, getDependentState());

      if (checkContext != null)
      {
         Object checkObject = checkContext.getTarget();
         if (checkObject instanceof RuleDependencyCheck == false)
            throw new IllegalArgumentException("ControllerContext target not RuleDependencyCheck instance: " + checkContext);
         setIDependOn(checkContext.getName());
         setResolved(((RuleDependencyCheck)checkObject).canResolve(getName(), action, args));
         if (isResolved())
         {
            addDependsOnMe(controller, checkContext);
         }
      }
      else
      {
         setResolved(resolvedOnNullIdentity);
      }
      return isResolved();
   }

   /**
    * Set the check name.
    *
    * @param checkName the context check name
    */
   public void setCheckName(String checkName)
   {
      this.checkName = checkName;
   }

   /**
    * Set is resolved on null context.
    *
    * @param resolvedOnNullIdentity null on null context lookup
    */
   public void setResolvedOnNullIdentity(boolean resolvedOnNullIdentity)
   {
      this.resolvedOnNullIdentity = resolvedOnNullIdentity;
   }
}
