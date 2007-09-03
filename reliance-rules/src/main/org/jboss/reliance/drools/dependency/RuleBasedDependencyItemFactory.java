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

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;

/**
 * Rule dependency item factory.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class RuleBasedDependencyItemFactory implements DependencyItemFactory<ControllerContext>
{
   // dependency item attributes
   private Object name;
   private Object action;
   private ControllerState whenRequired = ControllerState.PRE_INSTALL;
   private ControllerState dependentState = ControllerState.INSTALLED;
   private Object[] args = new Object[0];
   private String identityName;
   private Boolean resolvedOnNullIdentity;

   public DependencyItem createDependencyItem(ControllerContext context)
   {
      Object currentName = name;
      if (currentName == null)
         currentName = context.getName();
      Object currentAction = action;
      if (currentAction == null)
         currentAction = whenRequired.getStateString();
      RuleBasedDependencyItem item = new RuleBasedDependencyItem(currentName, currentAction, whenRequired, dependentState, args);
      if (identityName != null)
         item.setCheckName(identityName);
      if (resolvedOnNullIdentity != null)
         item.setResolvedOnNullIdentity(resolvedOnNullIdentity);
      return item;
   }

   public void setName(Object name)
   {
      this.name = name;
   }

   public void setAction(Object action)
   {
      this.action = action;
   }

   public void setWhenRequired(String whenRequired)
   {
      this.whenRequired = new ControllerState(whenRequired);
   }

   public void setDependentState(ControllerState dependentState)
   {
      this.dependentState = dependentState;
   }

   public void setArgs(Object[] args)
   {
      this.args = args;
   }

   public void setIdentityName(String identityName)
   {
      this.identityName = identityName;
   }

   public void setResolvedOnNullIdentity(boolean resolvedOnNullIdentity)
   {
      this.resolvedOnNullIdentity = resolvedOnNullIdentity;
   }
}
