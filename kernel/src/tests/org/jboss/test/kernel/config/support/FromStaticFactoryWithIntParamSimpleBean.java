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

import org.jboss.beans.metadata.plugins.annotations.Factory;
import org.jboss.beans.metadata.plugins.annotations.StringValue;
import org.jboss.beans.metadata.plugins.annotations.Value;

/**
 * A simple bean
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
@Factory(
      factoryClass = "org.jboss.test.kernel.config.support.SimpleBeanFactory",
      factoryMethod = "staticCreateSimpleBean",
      parameters = {@Value(type="java.lang.Integer", string = @StringValue("7"))}
)
public class FromStaticFactoryWithIntParamSimpleBean extends SimpleBean
{
   public FromStaticFactoryWithIntParamSimpleBean()
   {
   }

   public FromStaticFactoryWithIntParamSimpleBean(String string)
   {
      super(string);
   }

   public FromStaticFactoryWithIntParamSimpleBean(String string1, String string2)
   {
      super(string1, string2);
   }

   public FromStaticFactoryWithIntParamSimpleBean(Integer integer)
   {
      super(integer);
   }

   public FromStaticFactoryWithIntParamSimpleBean(Comparable comparable)
   {
      super(comparable);
   }

   public FromStaticFactoryWithIntParamSimpleBean(Collection collection)
   {
      super(collection);
   }

   public FromStaticFactoryWithIntParamSimpleBean(List list)
   {
      super(list);
   }

   public FromStaticFactoryWithIntParamSimpleBean(Set set)
   {
      super(set);
   }

   public FromStaticFactoryWithIntParamSimpleBean(Object[] array)
   {
      super(array);
   }

   public FromStaticFactoryWithIntParamSimpleBean(Map map)
   {
      super(map);
   }

   public FromStaticFactoryWithIntParamSimpleBean(Hashtable hashtable)
   {
      super(hashtable);
   }
}
