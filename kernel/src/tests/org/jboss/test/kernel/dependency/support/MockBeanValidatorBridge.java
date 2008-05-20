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
package org.jboss.test.kernel.dependency.support;

import org.jboss.kernel.plugins.dependency.BeanValidatorBridge;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.reflect.spi.MethodInfo;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class MockBeanValidatorBridge implements BeanValidatorBridge
{
   private Joinpoint joinpoint;
   private Object target;
   private String property;
   private String method;

   public Joinpoint getJoinpoint()
   {
      return joinpoint;
   }

   public Object getTarget()
   {
      return target;
   }

   public String getProperty()
   {
      return property;
   }

   public String getMethod()
   {
      return method;
   }

   public void validateConstructorValues(KernelControllerContext context, Joinpoint joinpoint) throws Throwable
   {
      this.joinpoint = joinpoint;
   }

   public void validateInstance(KernelControllerContext context, Object target) throws Throwable
   {
      this.target = target;
   }

   public void validatePropertyValue(KernelControllerContext context, Object target, PropertyInfo propertyInfo, Object value) throws Throwable
   {
      property = propertyInfo.getName();
   }

   public void validateMethodValues(KernelControllerContext context, Object target, MethodInfo methodInfo, Object[] parameters) throws Throwable
   {
      method = methodInfo.getName();
   }
}
