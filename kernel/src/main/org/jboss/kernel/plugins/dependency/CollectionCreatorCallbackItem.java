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

import java.util.Collection;

import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;

/**
 * Collection callback item using CollectionCreator.
 *
 * @param <T> expected collection type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class CollectionCreatorCallbackItem<T extends Collection<Object>> extends CollectionCallbackItem<T>
{
   protected CollectionCreator<T> creator;

   public CollectionCreatorCallbackItem(CollectionCreator<T> creator, Class name, InvokeDispatchContext owner, AttributeInfo attribute)
   {
      this(creator, name, null, null, null, owner, attribute);
   }

   public CollectionCreatorCallbackItem(CollectionCreator<T> creator, Class name, ControllerState whenRequired, ControllerState dependentState, Cardinality cardinality, InvokeDispatchContext context, AttributeInfo attribute)
   {
      super(name, whenRequired, dependentState, cardinality, context, attribute);
      if (creator == null)
         throw new IllegalArgumentException("Null creator!");
      this.creator = creator;
   }

   protected T getCollectionParameterHolder()
   {
      return creator.createCollection();
   }

}
