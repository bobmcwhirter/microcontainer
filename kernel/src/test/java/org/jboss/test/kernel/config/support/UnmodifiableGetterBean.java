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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Collection;

/**
 * An unmodifiable getter bean.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
@SuppressWarnings("unchecked")
public class UnmodifiableGetterBean implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;
   
   private Collection collection = new CustomCollection(true);
   private List list = new CustomList(true);
   private Set set = new CustomSet(true);
   private Map map = new CustomMap(true);
   private String[] array = new String[]{"aa", "ab", "ac"};

   public Collection getCollection()
   {
      return Collections.unmodifiableCollection(collection);
   }

   public void setCollection(Collection<?> collection)
   {
      this.collection = collection;
   }

   public List getList()
   {
      return Collections.unmodifiableList(list);
   }

   public void setList(List<?> list)
   {
      this.list = list;
   }

   public Set getSet()
   {
      return Collections.unmodifiableSet(set);
   }

   public void setSet(Set<?> set)
   {
      this.set = set;
   }

   public Map getMap()
   {
      return Collections.unmodifiableMap(map);
   }

   public void setMap(Map<?,?> map)
   {
      this.map = map;
   }

   public String[] getArray()
   {
      return array;
   }

   public void setArray(String[] array)
   {
      this.array = array;
   }
}
