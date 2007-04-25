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
import java.util.List;
import java.util.Queue;
import java.util.Set;

/**
 * Collection creator.
 */
public interface CollectionCreator<T extends Collection<Object>>
{
   /**
    * Create new Collection instance.
    *
    * @return collection
    */
   T createCollection();

   public CollectionCreator<List<Object>> LIST = new CollectionCreator<List<Object>>()
   {
      public List<Object> createCollection()
      {
         return new ArrayList<Object>();
      }
   };

   public CollectionCreator<Set<Object>> SET = new CollectionCreator<Set<Object>>()
   {
      public Set<Object> createCollection()
      {
         return new HashSet<Object>();
      }
   };

   public CollectionCreator<Queue<Object>> QUEUE = new CollectionCreator<Queue<Object>>()
   {
      public Queue<Object> createCollection()
      {
         return new LinkedList<Object>();
      }
   };
}
