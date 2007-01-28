/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.microcontainer.support.deployers;

import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.logging.Logger;

/**
 * IMainDeployer aspects.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class MainDeployerAspects
{
   private static Logger log = Logger.getLogger(MainDeployerAspects.class);
   private static boolean processCalled;

   public static boolean isProcessCalled()
   {
      return processCalled;
   }

   public static void setProcessCalled(boolean processCalled)
   {
      MainDeployerAspects.processCalled = processCalled;
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      log.debug(invocation);
      MethodInvocation mi = (MethodInvocation) invocation;
      String methodName = mi.getActualMethod().getName();
      Object value = null;
      if( methodName.equals("process") )
         value = process(mi);
      else
         value = invocation.invokeNext();
      return value;

   }

   public Object process(MethodInvocation invocation)
      throws Throwable
   {
      Object target = invocation.getTargetObject();
      Object[] args = invocation.getArguments();
      log.debug("process, target="+target);
      processCalled = true;
      return invocation.invokeNext();
   }

}
