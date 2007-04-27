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

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;

/**
 * Basic collection callback item factory.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BasicCollectionCallbackItemFactory implements CollectionCallbackItemFactory
{
   public CollectionCallbackItem createCollectionCallbackItem(
         Class<? extends Collection> parameterClass,
         Class name,
         InvokeDispatchContext context,
         AttributeInfo attribute)
   {
      return createCollectionCallbackItem(parameterClass, name, null, null, null, context, attribute);
   }

   public CollectionCallbackItem createCollectionCallbackItem(
         Class<? extends Collection> parameterClass,
         Class name,
         ControllerState whenRequired,
         ControllerState dependentState,
         Cardinality cardinality,
         InvokeDispatchContext context,
         AttributeInfo attribute)
   {
      if (parameterClass == null)
         throw new IllegalArgumentException("Null parameter class!");

      if (parameterClass.isAssignableFrom(ArrayList.class))
         return new ListCallbackItem(name, whenRequired, dependentState, cardinality, context, attribute);
      else if (parameterClass.isAssignableFrom(HashSet.class))
         return new SetCallbackItem(name, whenRequired, dependentState, cardinality, context, attribute);
      else if (parameterClass.isAssignableFrom(LinkedList.class))
         return new QueueCallbackItem(name, whenRequired, dependentState, cardinality, context, attribute);
      else
         return handleAdditionalCollectionClass(parameterClass, name, whenRequired, dependentState, cardinality, context, attribute);
   }

   /**
    * Override for additional handling of
    * collection callback item creaton via parameter class.
    *
    * @param parameterClass actual collection class
    * @param name demand name
    * @param whenRequired when required state
    * @param dependentState dependent state
    * @param cardinality the cardinality
    * @param context invoke owner
    * @param attribute the attribute
    * @return new exact collection callback item
    */
   protected CollectionCallbackItem handleAdditionalCollectionClass(
         Class<? extends Collection> parameterClass,
         Class name,
         ControllerState whenRequired,
         ControllerState dependentState,
         Cardinality cardinality,
         InvokeDispatchContext context,
         AttributeInfo attribute)
   {
      throw new IllegalArgumentException("No matching callback impl for parameter type: " + parameterClass);
   }
}
