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
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;
import org.jboss.util.JBossStringBuilder;

/**
 * Single callback item.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SingleCallbackItem extends AbstractCallbackItem<Object>
{
   protected InvokeDispatchContext owner;
   protected String method;
   protected String signature;

   public SingleCallbackItem(Object name, InvokeDispatchContext owner, String method)
   {
      this(name, owner, method, null);
   }

   public SingleCallbackItem(Object name, InvokeDispatchContext owner, String method, String signature)
   {
      this(name, null, null, owner, method, signature);
   }

   public SingleCallbackItem(Object name, ControllerState whenRequired, ControllerState dependentState, InvokeDispatchContext owner, String method)
   {
      this(name, whenRequired, dependentState, owner, method, null);
   }

   public SingleCallbackItem(Object name, ControllerState whenRequired, ControllerState dependentState, InvokeDispatchContext owner, String method, String signature)
   {
      super(name, whenRequired, dependentState);
      init(owner, method, signature);
   }

   protected void init(InvokeDispatchContext owner, String method, String signature)
   {
      if (owner == null)
         throw new IllegalArgumentException("Null owner!");
      if (method == null)
         throw new IllegalArgumentException("Null method!");

      this.owner = owner;
      this.method = method;
      this.signature = signature;
   }

   protected void additionCallback(ControllerContext context) throws Throwable
   {
      Object target = context.getTarget();
      if (target != null)
      {
         if (signature == null)
            signature = target.getClass().getName();
         owner.invoke(method, new Object[]{target}, new String[]{signature});
      }
   }

   protected void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      buffer.append(" owner=").append(owner);
      buffer.append(" method=").append(method);
      buffer.append(" signature=").append(signature);
   }
}
