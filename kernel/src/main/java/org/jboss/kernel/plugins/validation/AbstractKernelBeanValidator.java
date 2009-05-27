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
package org.jboss.kernel.plugins.validation;

import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.AbstractKernelObject;
import org.jboss.kernel.plugins.dependency.BeanValidatorBridge;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.validation.KernelBeanValidator;
import org.jboss.reflect.spi.MethodInfo;

/**
 * Abstract kernel bean validator.
 * 
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public abstract class AbstractKernelBeanValidator extends AbstractKernelObject implements KernelBeanValidator
{
   protected KernelController controller;

   @Override
   public void setKernel(Kernel kernel) throws Throwable
   {
      super.setKernel(kernel);
      controller = kernel.getController();
   }

   /**
    * Get validator bridge delegate as a plain installed bean.
    *
    * @return the delegate if exists, or null
    */
   protected BeanValidatorBridge getDelegate()
   {
      KernelControllerContext context = controller.getContextByClass(BeanValidatorBridge.class);
      return context != null ? BeanValidatorBridge.class.cast(context.getTarget()) : null;
   }

   public void validateConstructorValues(KernelControllerContext context, Joinpoint joinpoint) throws Throwable
   {
      BeanValidatorBridge delegate = getDelegate();
      if (delegate != null)
         delegate.validateConstructorValues(context, joinpoint);
   }

   public void validateInstance(KernelControllerContext context, Object target) throws Throwable
   {
      BeanValidatorBridge delegate = getDelegate();
      if (delegate != null)
         delegate.validateInstance(context, target);
   }

   public void validatePropertyValue(KernelControllerContext context, Object target, PropertyInfo propertyInfo, Object value) throws Throwable
   {
      BeanValidatorBridge delegate = getDelegate();
      if (delegate != null)
         delegate.validatePropertyValue(context, target, propertyInfo, value);
   }

   public void validateMethodValues(KernelControllerContext context, Object target, MethodInfo methodInfo, Object[] parameters) throws Throwable
   {
      BeanValidatorBridge delegate = getDelegate();
      if (delegate != null)
         delegate.validateMethodValues(context, target, methodInfo, parameters);
   }
}
