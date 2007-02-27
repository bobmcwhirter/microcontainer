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
package org.jboss.aop.microcontainer.aspects;

import java.lang.annotation.Annotation;

import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * Abstract helper class for [Aspect]Introduction.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractIntroduction implements Interceptor
{
   public String getName()
   {
      return getClass().getName();
   }

   protected abstract Class<? extends Annotation> getBindingAnnotation();

   protected boolean isBindingInfoPresent(Invocation invocation)
   {
      return invocation.resolveClassAnnotation(getBindingAnnotation()) != null;
   }

   protected void onRegistration(Invocation invocation, KernelControllerContext context)
   {
      onRegistration(context);
   }

   protected void onRegistration(KernelControllerContext context)
   {
   }

   protected void onUnregistration(Invocation invocation, KernelControllerContext context)
   {
      onUnregistration(context);
   }

   protected void onUnregistration(KernelControllerContext context)
   {
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      MethodInvocation mi = (MethodInvocation) invocation;
      KernelControllerContext context = (KernelControllerContext) mi.getArguments()[0];
      String methodName = mi.getMethod().getName();
      boolean infoPresent = isBindingInfoPresent(invocation);

      if ("setKernelControllerContext".equals(methodName) && infoPresent)
      {
         onRegistration(invocation, context);
      }
      else if (infoPresent)
      {
         onUnregistration(invocation, context);
      }

      return null;
   }
}
