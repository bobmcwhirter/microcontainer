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
package org.jboss.test.microcontainer.beans.test;

import java.util.HashSet;
import java.util.Set;

import junit.framework.Test;
import org.jboss.aop.proxy.container.AspectManaged;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.beans.POJO;


/**
 * AspectWithDependencyTurnedOffAopTestCase
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @version $Revision: 1.1 $
 */
public class AspectWithDependencyTurnedOffAopTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(AspectWithDependencyTurnedOffAopTestCase.class);
   }

   public AspectWithDependencyTurnedOffAopTestCase(String test)
   {
      super(test);
   }

   @Override
   protected void afterSetUp() throws Exception
   {
      // do nothing, so we ignore validate
   }

   public void testAllAopTurnedOff() throws Exception
   {
      assertMoreDependencies("Intercepted", "NotIntercepted");

      POJO pojoNotIntercepted = (POJO)getBean("NotIntercepted");
      assertFalse(pojoNotIntercepted instanceof AspectManaged);

      assertMoreDependencies("WithLifecycle", "WithLifecycleDisabled");
   }
   
   public void testPointcutsTurnedOff() throws Exception
   {
      assertMoreDependencies("Intercepted", "NotIntercepted");

      POJO pojoNotIntercepted = (POJO)getBean("NotIntercepted");
      assertFalse(pojoNotIntercepted instanceof AspectManaged);
      
      assertNotInstalledContext("WithLifecycle");
      assertNotInstalledContext("WithLifecycleDisabled");
   }
   
   public void testLifecycleTurnedOff() throws Exception
   {
      assertNotInstalledContext("Intercepted");
      assertNotInstalledContext("NotIntercepted");

      assertMoreDependencies("WithLifecycle", "WithLifecycleDisabled");
   }
   
   private void assertMoreDependencies(String aspectCtxName, String disabledCtxName)
   {
      ControllerContext aspectCtx = assertNotInstalledContext(aspectCtxName);
      ControllerContext disabledCtx = assertInstalledContext(disabledCtxName);
      
      Set<DependencyItem> interceptedDependencies = new HashSet<DependencyItem>(aspectCtx.getDependencyInfo().getIDependOn(null));
      Set<DependencyItem> notInterceptedDependencies = new HashSet<DependencyItem>(disabledCtx.getDependencyInfo().getIDependOn(null));
      assertTrue(interceptedDependencies.size() > notInterceptedDependencies.size());
   }
   
   private ControllerContext assertNotInstalledContext(String bean)
   {
      try
      {
         getControllerContext(bean);
         fail("'" + bean + "' should not have been installed");
      }
      catch (Exception expected)
      {
      }
      ControllerContext ctx = getControllerContext(bean, null);
      assertNotNull(ctx);
      assertNotSame(ControllerState.INSTALLED, ctx.getState());
      return ctx;
   }
   
   private ControllerContext assertInstalledContext(String bean)
   {
      ControllerContext ctx = getControllerContext(bean);
      assertNotNull(ctx);
      assertSame(ControllerState.INSTALLED, ctx.getState());
      return ctx;
   }
}
