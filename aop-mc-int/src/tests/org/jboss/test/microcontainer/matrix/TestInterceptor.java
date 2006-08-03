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
package org.jboss.test.microcontainer.matrix;

import java.lang.reflect.Method;

import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.test.microcontainer.support.Test;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class TestInterceptor implements Interceptor
{
   public static int interceptions;
   public static Method invoked;
   public static Object classAnnotation;
   public static Object methodAnnotation;
   public static Object metadata;

   public static void reset()
   {
      interceptions = 0;
      invoked = null;
      classAnnotation = null;
      methodAnnotation = null;
      metadata = null;
   }
   
   public String getName()
   {
      return this.getClass().getName();
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      interceptions++;
      
      if (invocation instanceof MethodInvocation)
      {
         invoked = ((MethodInvocation)invocation).getMethod();
      }
      
      classAnnotation = invocation.resolveClassAnnotation(Test.class);
      methodAnnotation = invocation.resolveAnnotation(Test.class);
      metadata = invocation.getMetaData("Simple", "MetaData");
      
      return invocation.invokeNext();
   }

}
