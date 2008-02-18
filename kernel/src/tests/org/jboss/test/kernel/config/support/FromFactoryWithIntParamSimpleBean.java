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
      factory = @Value(javabean = @JavaBeanValue("org.jboss.test.kernel.config.support.SimpleBeanFactory")),
      factoryMethod = "createSimpleBean",
      parameters = {@Value(type = "java.lang.Integer", string = @StringValue("4"))}
)
public class FromFactoryWithIntParamSimpleBean extends SimpleBean
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;

   public FromFactoryWithIntParamSimpleBean()
   {
   }

   public FromFactoryWithIntParamSimpleBean(String string)
   {
      super(string);
   }

   public FromFactoryWithIntParamSimpleBean(String string1, String string2)
   {
      super(string1, string2);
   }

   public FromFactoryWithIntParamSimpleBean(Integer integer)
   {
      super(integer);
   }

   public FromFactoryWithIntParamSimpleBean(Comparable<?> comparable)
   {
      super(comparable);
   }

   public FromFactoryWithIntParamSimpleBean(Collection<?> collection)
   {
      super(collection);
   }

   public FromFactoryWithIntParamSimpleBean(List<?> list)
   {
      super(list);
   }

   public FromFactoryWithIntParamSimpleBean(Set<?> set)
   {
      super(set);
   }

   public FromFactoryWithIntParamSimpleBean(Object[] array)
   {
      super(array);
   }

   public FromFactoryWithIntParamSimpleBean(Map<?,?> map)
   {
      super(map);
   }

   public FromFactoryWithIntParamSimpleBean(Hashtable<?,?> hashtable)
   {
      super(hashtable);
   }
}
