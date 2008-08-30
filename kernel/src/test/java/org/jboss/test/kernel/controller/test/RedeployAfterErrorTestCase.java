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
package org.jboss.test.kernel.controller.test;

import junit.framework.Test;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;

/**
 * RedeployAfterErrorTestCase.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class RedeployAfterErrorTestCase extends AbstractControllerTest
{
   public static Test suite()
   {
      return suite(RedeployAfterErrorTestCase.class);
   }

   public RedeployAfterErrorTestCase(String name) throws Throwable
   {
      super(name);
   }

   public void testRedeployAfterError() throws Throwable
   {
      KernelDeployment deployment = deploy("RedeployAfterErrorTestCase_bad.xml");
      try
      {
         ControllerContext context = getControllerContext("Name1", null);
         assertEquals(ControllerState.ERROR, context.getState());
         checkThrowable(ClassNotFoundException.class, context.getError());
      }
      finally
      {
         undeploy(deployment);
      }

      validate();

      deployment = deploy("RedeployAfterErrorTestCase_good.xml");
      try
      {
         validate();
         assertNotNull(getBean("Name1"));
      }
      finally
      {
         undeploy(deployment);
      }
   }

   public void testChangeUsage() throws Throwable
   {
      KernelDeployment deployment = deploy("RedeployAfterErrorTestCase_bad.xml");
      try
      {
         KernelControllerContext context = getControllerContext("Name1", null);
         assertEquals(ControllerState.ERROR, context.getState());
         checkThrowable(ClassNotFoundException.class, context.getError());

         // a hacky fix, but the point is to get the error away :-)
         AbstractBeanMetaData bmd = (AbstractBeanMetaData)context.getBeanMetaData();
         bmd.setBean(Object.class.getName());

         // we suspect the error was resolved, let's try to install
         change(context, ControllerState.INSTALLED);

         // nope, still in error
         assertEquals(ControllerState.ERROR, context.getState());
      }
      finally
      {
         undeploy(deployment);
      }
   }
}