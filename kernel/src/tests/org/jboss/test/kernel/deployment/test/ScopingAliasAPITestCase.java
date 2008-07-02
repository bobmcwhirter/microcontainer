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
package org.jboss.test.kernel.deployment.test;

import junit.framework.Test;
import org.jboss.dependency.plugins.ScopedAliasControllerContext;
import org.jboss.dependency.spi.Controller;
import org.jboss.metadata.spi.scope.CommonLevels;
import org.jboss.metadata.spi.scope.Scope;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.test.kernel.deployment.support.SimpleBean;
import org.jboss.test.kernel.deployment.support.SimpleObjectWithBean;
import org.jboss.test.AbstractTestDelegate;

/**
 * Scoping alias API tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ScopingAliasAPITestCase extends ScopingDeploymentTest
{
   public ScopingAliasAPITestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(ScopingAliasAPITestCase.class);
   }

   /**
    * Default setup with security manager enabled
    *
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      AbstractTestDelegate delegate = new ScopingTestAPIDelegate(clazz);
      delegate.enableSecurity = true;
      return delegate;
   }

   protected void afterSetUp() throws Exception
   {
      try
      {
         Controller controller = ((ScopingTestAPIDelegate)getDelegate()).getController();

         ScopeKey sk1 = new ScopeKey(new Scope[]{new Scope(CommonLevels.DEPLOYMENT, "deployment1"), new Scope(CommonLevels.APPLICATION, "testApp")});
         ScopedAliasControllerContext.addAlias("simple", "simple1", sk1, controller);

         ScopeKey sk3 = new ScopeKey(new Scope[]{new Scope(CommonLevels.DEPLOYMENT, "deployment3"), new Scope(CommonLevels.APPLICATION, "testApp")});
         ScopedAliasControllerContext.addAlias("simple", "simple3", sk3, controller);

         controller.addAlias("simple", "simple-main");
      }
      catch (Throwable t)
      {
         throw new Exception(t);
      }

      super.afterSetUp();
   }

   @Override
   protected void tearDown() throws Exception
   {
      Controller controller = ((ScopingTestAPIDelegate)getDelegate()).getController();

      controller.removeAlias("simple");

      ScopeKey sk1 = new ScopeKey(new Scope[]{new Scope(CommonLevels.DEPLOYMENT, "deployment1"), new Scope(CommonLevels.APPLICATION, "testApp")});
      ScopedAliasControllerContext.removeAlias("simple", sk1, controller);

      ScopeKey sk3 = new ScopeKey(new Scope[]{new Scope(CommonLevels.DEPLOYMENT, "deployment3"), new Scope(CommonLevels.APPLICATION, "testApp")});
      ScopedAliasControllerContext.removeAlias("simple", sk3, controller);

      super.tearDown();
   }

   public void testAliasInMainController() throws Throwable
   {
      checkBean("main", "main");
   }

   public void testAliasInOwnScope() throws Throwable
   {
      checkBean("deploy1", "deployment1");
   }

   public void testAliasInParentScope() throws Throwable
   {
      checkBean("deploy2", "main");
   }

   public void testAliasInOwnScope2() throws Throwable
   {
      checkBean("deploy3", "deployment3");
   }

   private void checkBean(String bean, String ctor)
   {
      SimpleObjectWithBean deploy = (SimpleObjectWithBean) getBean(bean);
      assertNotNull(deploy);
      SimpleBean simple = deploy.getSimpleBean();
      assertNotNull(simple);
      assertEquals(ctor, simple.getConstructorString());
   }
}