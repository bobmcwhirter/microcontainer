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
package org.jboss.test.kernel.inject.support;

import java.util.Collection;
import java.util.Set;
import java.util.List;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class GenericsTestObject
{
   private Collection<TesterInterface> collection;
   private Set<TesterInterface> set;
   private List<TesterInterface> list;
   private SomeGenericObject<String> generic;

   public Collection<TesterInterface> getCollection()
   {
      return collection;
   }

   public void setCollection(Collection<TesterInterface> collection)
   {
      this.collection = collection;
   }

   public Set<TesterInterface> getSet()
   {
      return set;
   }

   public void setSet(Set<TesterInterface> set)
   {
      this.set = set;
   }

   public List<TesterInterface> getList()
   {
      return list;
   }

   public void setList(List<TesterInterface> list)
   {
      this.list = list;
   }

   public SomeGenericObject<String> getGeneric()
   {
      return generic;
   }

   public void setGeneric(SomeGenericObject<String> generic)
   {
      this.generic = generic;
   }
}
