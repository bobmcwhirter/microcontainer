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
import org.jboss.beans.metadata.api.annotations.JavaBeanValue;
import org.jboss.beans.metadata.api.annotations.StringValue;
import org.jboss.beans.metadata.api.annotations.Value;

/**
 * A simple bean
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
@Factory(
      factory = @Value(javabean = @JavaBeanValue(org.jboss.test.kernel.config.support.SimpleBeanFactory.class)),
      factoryMethod = "createSimpleBean",
      parameters = {@Value(string = @StringValue("Factory Value"), type = String.class)}
)
public class FromFactoryWithParamSimpleBean extends SimpleBean
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;

   public FromFactoryWithParamSimpleBean()
   {
   }

   public FromFactoryWithParamSimpleBean(String string)
   {
      super(string);
   }

   public FromFactoryWithParamSimpleBean(String string1, String string2)
   {
      super(string1, string2);
   }

   public FromFactoryWithParamSimpleBean(Integer integer)
   {
      super(integer);
   }

   public FromFactoryWithParamSimpleBean(Comparable<?> comparable)
   {
      super(comparable);
   }

   public FromFactoryWithParamSimpleBean(Collection<?> collection)
   {
      super(collection);
   }

   public FromFactoryWithParamSimpleBean(List<?> list)
   {
      super(list);
   }

   public FromFactoryWithParamSimpleBean(Set<?> set)
   {
      super(set);
   }

   public FromFactoryWithParamSimpleBean(Object[] array)
   {
      super(array);
   }

   public FromFactoryWithParamSimpleBean(Map<?, ?> map)
   {
      super(map);
   }

   public FromFactoryWithParamSimpleBean(Hashtable<?,?> hashtable)
   {
      super(hashtable);
   }
}
