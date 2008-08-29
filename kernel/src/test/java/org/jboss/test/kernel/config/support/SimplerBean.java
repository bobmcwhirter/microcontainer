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
package org.jboss.test.kernel.config.support;

import java.io.Serializable;

/**
 * A simpler bean
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class SimplerBean implements Serializable
{
   private static final long serialVersionUID = -2041055615016745994L;

   /** preInstantiated */
   protected CustomCollection preInstantiatedCollection = new CustomCollection(true);

   /** preInstantiated */
   protected CustomSet preInstantiatedSet = new CustomSet(true);

   /** preInstantiated */
   protected CustomList preInstantiatedList = new CustomList(true);

   /** preInstantiated */
   protected CustomMap preInstantiatedMap = new CustomMap(true);

   public SimplerBean()
   {
   }

   public SimplerBean(String string)
   {
      preInstantiatedCollection.add(string);
      preInstantiatedList.add(string);
      preInstantiatedSet.add(string);
   }

   public SimplerBean(String key, String value)
   {
      preInstantiatedMap.put(key, value);
   }

   public CustomCollection getPreInstantiatedCollection()
   {
      return preInstantiatedCollection;
   }

   public CustomSet getPreInstantiatedSet()
   {
      return preInstantiatedSet;
   }

   public CustomList getPreInstantiatedList()
   {
      return preInstantiatedList;
   }

   public CustomMap getPreInstantiatedMap()
   {
      return preInstantiatedMap;
   }
}
