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
package org.jboss.kernel.plugins.dependency;

import java.util.Queue;

import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;

/**
 * Queue collection callback item.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class QueueCallbackItem extends CollectionCreatorCallbackItem<Queue<Object>>
{
   public QueueCallbackItem(Class<?> name, InvokeDispatchContext owner, AttributeInfo attribute)
   {
      super(CollectionCreator.QUEUE, name, owner, attribute);
   }

   public QueueCallbackItem(Class<?> name, ControllerState whenRequired, ControllerState dependentState, Cardinality cardinality, InvokeDispatchContext context, AttributeInfo attribute)
   {
      super(CollectionCreator.QUEUE, name, whenRequired, dependentState, cardinality, context, attribute);
   }
}
