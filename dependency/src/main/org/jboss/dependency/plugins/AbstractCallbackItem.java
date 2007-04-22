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
package org.jboss.dependency.plugins;

import org.jboss.dependency.spi.CallbackItem;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Abstract callback item.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractCallbackItem<T> extends JBossObject implements CallbackItem<T>
{
   protected T name;
   protected ControllerState whenRequired = ControllerState.CONFIGURED;
   protected ControllerState dependentState = ControllerState.INSTALLED;
   protected Cardinality cardinality;

   protected AbstractCallbackItem(T name)
   {
      this.name = name;
   }

   protected AbstractCallbackItem(T name, ControllerState whenRequired, ControllerState dependentState, Cardinality cardinality)
   {
      this.name = name;
      if (whenRequired != null)
         this.whenRequired = whenRequired;
      if (dependentState != null)
         this.dependentState = dependentState;
      this.cardinality = cardinality;
   }

   public void ownerCallback(Controller controller) throws Throwable
   {
   }

   /**
    * Helper method.
    *
    * @param context new installed context
    * @throws Throwable for any error
    */
   protected void additionCallback(ControllerContext context) throws Throwable
   {
   }

   public void additionCallback(Controller controller, ControllerContext context) throws Throwable
   {
      additionCallback(context);
   }

   public T getIDependOn()
   {
      return name;
   }

   public ControllerState getWhenRequired()
   {
      return whenRequired;
   }

   public ControllerState getDependentState()
   {
      return dependentState;
   }

   public Cardinality getCardinality()
   {
      return cardinality;
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append("name=").append(name);
   }

   protected void toString(JBossStringBuilder buffer)
   {
      buffer.append("name=").append(name);
      buffer.append(" whenRequired=").append(whenRequired);
      buffer.append(" dependentState=").append(dependentState);
   }
}
