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

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.dispatch.AttributeDispatchContext;
import org.jboss.util.JBossStringBuilder;

/**
 * Attribute callback item.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AttributeCallbackItem<T> extends AbstractCallbackItem<T>
{
   protected AttributeDispatchContext owner;
   protected String attribute;

   public AttributeCallbackItem(T name, AttributeDispatchContext owner, String attribute)
   {
      this(name, null, null, owner, attribute);
   }

   public AttributeCallbackItem(T name, ControllerState whenRequired, ControllerState dependentState, AttributeDispatchContext owner, String attribute)
   {
      super(name, whenRequired, dependentState);
      init(owner, attribute);
   }

   protected void init(AttributeDispatchContext owner, String attribute)
   {
      if (owner == null)
         throw new IllegalArgumentException("Null owner!");
      if (attribute == null)
         throw new IllegalArgumentException("Null attribute!");

      this.owner = owner;
      this.attribute = attribute;
   }

   protected void execute(Object target) throws Throwable
   {
      owner.set(attribute, target);      
   }

   protected void additionCallback(ControllerContext context) throws Throwable
   {
      execute(context.getTarget());
   }

   protected void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      buffer.append(" owner=").append(owner);
      buffer.append(" attribute=").append(attribute);
   }
}
