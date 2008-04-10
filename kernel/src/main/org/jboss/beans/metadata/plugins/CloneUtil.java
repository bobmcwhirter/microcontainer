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
package org.jboss.beans.metadata.plugins;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.jboss.beans.metadata.spi.MetaDataVisitorNode;

/**
 * Clone util helper.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
class CloneUtil
{
   /**
    * Simple clone object.
    *
    * @param <T> the type
    * @param original the original
    * @param expectedClass the expected class
    * @return clone
    */
   public static <T extends MetaDataVisitorNode> T cloneObject(T original, Class<T> expectedClass)
   {
      if (original == null)
         return null;

      Object clone = original.clone();

      if (expectedClass.isInstance(clone) == false)
         throw new IllegalArgumentException("Clone '" + clone + "' is not of expected class: " + expectedClass);

      return expectedClass.cast(clone);
   }

   /**
    * Simple collection clone.
    *
    * @param <T> the collection type
    * @param <U> the component type
    * @param collection the collection to be cloned
    * @param expectedClass expected collection class
    * @param componentType expected component class
    * @return cloned collection
    */
   public static <U extends MetaDataVisitorNode, T extends Collection<U>> T cloneCollection(T collection, Class<? extends T> expectedClass, Class<U> componentType)
   {
      if (collection == null)
         return null;

      try
      {
         T clone = expectedClass.newInstance();
         for (U item : collection)
               clone.add(cloneObject(item, componentType));

         return clone;
      }
      catch (Throwable t)
      {
         throw new RuntimeException(t);
      }
   }

   /**
    * Simple map clone.
    *
    * @param <T> the collection type
    * @param <U> the component type
    * @param map the map to clone
    * @param keyClass the key class
    * @param valueClass the value class
    * @return map clone
    */
   public static <U extends MetaDataVisitorNode, T extends MetaDataVisitorNode> Map<U, T> cloneMap(Map<U, T> map, Class<U> keyClass, Class<T> valueClass)
   {
      if (map == null)
         return null;

      Map<U, T> clone = new HashMap<U, T>();
      for (Map.Entry<U, T> entry : map.entrySet())
         clone.put(cloneObject(entry.getKey(), keyClass), cloneObject(entry.getValue(), valueClass));

      return clone;
   }
}
