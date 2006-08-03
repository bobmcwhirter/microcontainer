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

/**
 * A Interceptor with a dependency.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class InterceptorWithDependency extends AbstractInterceptor
{
   public static SimpleBean intercepted = null;
   
   private SimpleBean bean;
   
   public SimpleBean getSimpleBean()
   {
      return bean;
   }
   
   public void setSimpleBean(SimpleBean bean)
   {
      log.debug("setBean " + bean);
      this.bean = bean;
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      log.debug("InterceptorWithDependency: " + invocation);
      System.out.println("-----> bean " + bean);
      intercepted = bean;
      System.out.println("-----> intercepted " + intercepted);
      return invocation.invokeNext();
   }
}
