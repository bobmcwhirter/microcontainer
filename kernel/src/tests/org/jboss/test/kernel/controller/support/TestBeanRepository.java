/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.kernel.controller.support;

import java.util.HashSet;
import java.util.Set;

/**
 * TestBeanRepository.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class TestBeanRepository
{
   private Set<TestBean> beans = new HashSet<TestBean>();
   
   public Set<TestBean> getBeans()
   {
      return beans;
   }

   public void setBeans(Set<TestBean> beans)
   {
      this.beans = beans;
   }

   public void add(TestBean bean)
   {
      if (bean.isThrowError() == false)
         bean.setThrowError(true);
      else
         throw new Error();
      beans.add(bean);
   }

   public void remove(TestBean bean)
   {
      beans.remove(bean);
   }
}
