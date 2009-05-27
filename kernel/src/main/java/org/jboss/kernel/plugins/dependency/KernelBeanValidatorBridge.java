/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.kernel.plugins.dependency;

import org.jboss.kernel.spi.validation.KernelBeanValidator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.reflect.spi.MethodInfo;

/**
 * Wrapper around kernel bean validator.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class KernelBeanValidatorBridge implements BeanValidatorBridge
{
   private KernelBeanValidator validator;

   public KernelBeanValidatorBridge(KernelBeanValidator validator)
   {
      if (validator == null)
         throw new IllegalArgumentException("Null validator");

      this.validator = validator;
   }

   public void validateConstructorValues(KernelControllerContext context, Joinpoint joinpoint) throws Throwable
   {
      validator.validateConstructorValues(context, joinpoint);
   }

   public void validateInstance(KernelControllerContext context, Object target) throws Throwable
   {
      validator.validateInstance(context, target);
   }

   public void validatePropertyValue(KernelControllerContext context, Object target, PropertyInfo propertyInfo, Object value) throws Throwable
   {
      validator.validatePropertyValue(context, target, propertyInfo, value);
   }

   public void validateMethodValues(KernelControllerContext context, Object target, MethodInfo methodInfo, Object[] parameters) throws Throwable
   {
      validator.validateMethodValues(context, target, methodInfo, parameters);
   }
}
