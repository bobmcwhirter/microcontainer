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

import org.jboss.test.kernel.deployment.support.SimpleBean;
import org.jboss.test.kernel.deployment.support.SimpleObjectWithBean;

/**
 *
 * @author <a href="mailto:kabir.khan@jboss.com">Kabir Khan</a>
 */
public class ScopingAliasTestCase extends ScopingDeploymentTest
{
   public ScopingAliasTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(ScopingAliasTestCase.class);
   }


   // ---- tests

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
