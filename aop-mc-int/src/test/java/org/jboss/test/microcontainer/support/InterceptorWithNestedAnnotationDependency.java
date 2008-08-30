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
package org.jboss.test.microcontainer.support;

import org.jboss.aop.joinpoint.Invocation;
import org.jboss.dependency.spi.Controller;

/**
 * A Interceptor with a dependency from an annotation
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class InterceptorWithNestedAnnotationDependency extends AbstractInterceptor
{
   public static final String SRC_CLASS = "class";
   public static final String SRC_METHOD = "method";

   public static SimpleBean intercepted1 = null;
   public static SimpleBean intercepted2 = null;
   public static SimpleBean intercepted3 = null;
   
   private static Controller controller;
   
   public void setController(Controller controller)
   {
      InterceptorWithNestedAnnotationDependency.controller = controller;
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      log.debug("InterceptorWithNestedAnnotationDependency: " + invocation);
      Containing annotation = (Containing) invocation.resolveClassAnnotation(Containing.class);
      if (annotation == null)
      {
         annotation = (Containing) invocation.resolveAnnotation(Containing.class);
      }

      if (annotation != null)
      {
         intercepted1 = (SimpleBean) controller.getInstalledContext(annotation.dependency().data()).getTarget();
         TestAnnotationDependency[] dependencies = annotation.contained().dependencies();
         intercepted2 = (SimpleBean) controller.getInstalledContext(dependencies[0].data()).getTarget();
         intercepted3 = (SimpleBean) controller.getInstalledContext(dependencies[1].data()).getTarget();
      }      
      return invocation.invokeNext();
   }
}
