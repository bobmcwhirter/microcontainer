/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.microcontainer.annotatedaop;

import org.jboss.aop.Aspect;
import org.jboss.aop.Bind;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
@Aspect(scope=Scope.PER_INSTANCE)
public class TestAspect
{
   public static TestAspect last;
   public static int invoked;
   public static int advice;
   
   public static void reset()
   {
      last = null;
      invoked = 0;
      advice = 0;
   }
   
   @Bind(pointcut="execution(* org.jboss.test.microcontainer.annotatedaop.SimplePOJO->method())")
   public Object advice1(MethodInvocation inv) throws Throwable
   {
      last = this;
      invoked++;
      advice = 1;
      
      return inv.invokeNext();
   }
   
   @Bind(pointcut="execution(* org.jboss.test.microcontainer.annotatedaop.SimplePOJO->anotherMethod())")
   public Object advice2(Invocation inv) throws Throwable
   {
      last = this;
      invoked++;
      advice = 2;
      
      return inv.invokeNext();
   }
}
