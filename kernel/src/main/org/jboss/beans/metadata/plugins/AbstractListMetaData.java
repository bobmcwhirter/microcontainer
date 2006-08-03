/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
import java.util.List;
import java.util.ListIterator;

import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.util.JBossStringBuilder;

/**
 * List metadata.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractListMetaData extends AbstractCollectionMetaData implements List<MetaDataVisitorNode>
{
   /**
    * Create a new list value
    */
   public AbstractListMetaData()
   {
   }
   
   public void add(int index, MetaDataVisitorNode element)
   {
      collection.add(index, element);
   }

   public boolean addAll(int index, Collection<? extends MetaDataVisitorNode> c)
   {
      return collection.addAll(index, c);
   }

   public MetaDataVisitorNode get(int index)
   {
      return collection.get(index);
   }

   public int indexOf(Object o)
   {
      return collection.indexOf(o);
   }

   public int lastIndexOf(Object o)
   {
      return lastIndexOf(o);
   }

   public ListIterator<MetaDataVisitorNode> listIterator()
   {
      return collection.listIterator();
   }

   public ListIterator<MetaDataVisitorNode> listIterator(int index)
   {
      return collection.listIterator(index);
   }

   public MetaDataVisitorNode remove(int index)
   {
      return collection.remove(index);
   }

   public MetaDataVisitorNode set(int index, MetaDataVisitorNode element)
   {
      return set(index, element);
   }

   public List<MetaDataVisitorNode> subList(int fromIndex, int toIndex)
   {
      return subList(fromIndex, toIndex);
   }

   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
   }
}