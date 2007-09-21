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

import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.beans.metadata.api.annotations.Factory;

/**
 * A simple bean
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
@Factory(
      factoryClass = "org.jboss.test.kernel.config.support.SimpleBeanFactory",
      factoryMethod = "staticCreateSimpleBean"
)
public class FromStaticFactorySimpleBean extends SimpleBean
{
   public FromStaticFactorySimpleBean()
   {
   }

   public FromStaticFactorySimpleBean(String string)
   {
      super(string);
   }

   public FromStaticFactorySimpleBean(String string1, String string2)
   {
      super(string1, string2);
   }

   public FromStaticFactorySimpleBean(Integer integer)
   {
      super(integer);
   }

   public FromStaticFactorySimpleBean(Comparable comparable)
   {
      super(comparable);
   }

   public FromStaticFactorySimpleBean(Collection collection)
   {
      super(collection);
   }

   public FromStaticFactorySimpleBean(List list)
   {
      super(list);
   }

   public FromStaticFactorySimpleBean(Set set)
   {
      super(set);
   }

   public FromStaticFactorySimpleBean(Object[] array)
   {
      super(array);
   }

   public FromStaticFactorySimpleBean(Map map)
   {
      super(map);
   }

   public FromStaticFactorySimpleBean(Hashtable hashtable)
   {
      super(hashtable);
   }
}
