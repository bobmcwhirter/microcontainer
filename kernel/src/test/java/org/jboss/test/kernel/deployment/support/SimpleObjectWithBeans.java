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
package org.jboss.test.kernel.deployment.support;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A simple object with all possible bean as meta data
 *
 * @author <a href="ales.justin@gmail.com">Ales Justin</a>
 */
public class SimpleObjectWithBeans implements Serializable
{
   // Constants -----------------------------------------------------

   private static final long serialVersionUID = 3258126972906387766L;

   // Attributes ----------------------------------------------------

   private SimpleBean simpleBean;
   private SimpleObjectWithBeans nested;
   private List<SimpleBean> list;
   private Set<SimpleBean> set;
   private Map<SimpleBean, SimpleBean> map;

   public SimpleObjectWithBeans()
   {
   }

   public SimpleObjectWithBeans(SimpleBean simpleBean)
   {
      this.simpleBean = simpleBean;
   }

   public SimpleBean getSimpleBean()
   {
      return simpleBean;
   }

   public void setSimpleBean(SimpleBean simpleBean)
   {
      this.simpleBean = simpleBean;
   }

   public SimpleObjectWithBeans getNested()
   {
      return nested;
   }

   public void setNested(SimpleObjectWithBeans nested)
   {
      this.nested = nested;
   }

   public List<SimpleBean> getList()
   {
      return list;
   }

   public void setList(List<SimpleBean> list)
   {
      this.list = list;
   }

   public Set<SimpleBean> getSet()
   {
      return set;
   }

   public void setSet(Set<SimpleBean> set)
   {
      this.set = set;
   }

   public Map<SimpleBean, SimpleBean> getMap()
   {
      return map;
   }

   public void setMap(Map<SimpleBean, SimpleBean> map)
   {
      this.map = map;
   }
}