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

import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.Invocation;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class MetaDataContextInterceptor implements Interceptor
{
   public static Object classAnnotation;
   public static Object joinpointAnnotation;
   
   private Class annotationType;
   
   public static void reset()
   {
      classAnnotation = null;
      joinpointAnnotation = null;
   }
   
   public void setAnnotation(String annotationType)
   {
      try
      {
         this.annotationType = Thread.currentThread().getContextClassLoader().loadClass(annotationType);
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }
   
   public String getName()
   {
      return getClass().getName();
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      classAnnotation = invocation.resolveClassAnnotation(annotationType);
      joinpointAnnotation = invocation.resolveAnnotation(annotationType);
      return invocation.invokeNext();
   }
}
