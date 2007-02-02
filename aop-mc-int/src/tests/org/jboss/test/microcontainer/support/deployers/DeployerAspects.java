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

import java.util.HashSet;

import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.logging.Logger;
import org.jboss.test.microcontainer.support.deployers.IDeployer.IDeployerMethod;

/**
 * IDeployer aspects
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class DeployerAspects
{
   private static Logger log = Logger.getLogger(DeployerAspects.class);
   private static HashSet<IDeployerMethod> called = new HashSet<IDeployerMethod>();
   private static DeployerAspects instance;

   public static HashSet<IDeployerMethod> getCalled()
   {
      return called;
   }
   public static synchronized DeployerAspects getInstance()
   {
      if( instance == null )
         instance = new DeployerAspects();
      return instance;
   }

   public DeployerAspects()
   {
      log.debug("ctor");
      instance = this;
   }

   public Object invoke(Invocation invocation) throws Throwable
   {
      log.debug(invocation);
      MethodInvocation mi = (MethodInvocation) invocation;
      String methodName = mi.getMethod().getName();
      Object value = null;
      if( methodName.equals("prepareDeploy") )
         value = prepareDeploy(mi);
      else if( methodName.equals("commitDeploy") )
         value = commitDeploy(mi);
      else if( methodName.equals("prepareUndeploy") )
         value = prepareUndeploy(mi);
      else if( methodName.equals("commitUndeploy") )
         value = commitUndeploy(mi);
      else
      {
         log.debug("Method " + methodName + " is not advised");
         value = invocation.invokeNext();
      }
      return value;
   }

   public Object prepareDeploy(MethodInvocation invocation)
      throws Throwable
   {
      Object target = invocation.getTargetObject();
      log.debug("prepareDeploy, target="+target);
      called.add(IDeployerMethod.prepareDeploy);
      return invocation.invokeNext();
   }
   public Object commitDeploy(MethodInvocation invocation)
      throws Throwable
   {
      Object target = invocation.getTargetObject();
      Object value = invocation.invokeNext();
      log.debug("commitDeploy, target="+target);
      called.add(IDeployerMethod.commitDeploy);
      return value;
   }

   public Object prepareUndeploy(MethodInvocation invocation)
      throws Throwable
   {
      Object target = invocation.getTargetObject();
      log.debug("prepareUndeploy, target="+target);
      called.add(IDeployerMethod.prepareUndeploy);
      return invocation.invokeNext();
   }
   public Object commitUndeploy(MethodInvocation invocation)
      throws Throwable
   {
      Object target = invocation.getTargetObject();
      Object value = invocation.invokeNext();
      log.debug("commitUndeploy, target="+target);
      called.add(IDeployerMethod.commitUndeploy);
      return value;
   }
}
