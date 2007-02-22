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
import org.jboss.test.kernel.deployment.support.ObjectWithBeanAware;
import org.jboss.test.kernel.deployment.support.SimpleBean;

/**
 * Scoping tests.
 * With scope annotation at class level.
 *
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
public class AnnotatedClassesScopingTestCase extends ScopingDeploymentTest
{
   public AnnotatedClassesScopingTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(AnnotatedClassesScopingTestCase.class);
   }

   // ---- tests

   public void testAnnotatedDeploymentScoping() throws Throwable
   {
      ObjectWithBeanAware appScopeObject = (ObjectWithBeanAware) getBean("appScopeObject");
      assertNotNull(appScopeObject);

      ObjectWithBeanAware deploy1 = (ObjectWithBeanAware) getBean("deploy1");
      assertNotNull(deploy1);
      SimpleBean simple1 = deploy1.getSimpleBean();
      assertNotNull(simple1);
      assertEquals("deployment1", simple1.getConstructorString());

      ObjectWithBeanAware deploy2 = (ObjectWithBeanAware) getBean("deploy2");
      assertNotNull(deploy2);
      SimpleBean simple2 = deploy2.getSimpleBean();
      assertNotNull(simple2);
      assertEquals("deployment2", simple2.getConstructorString());

      ObjectWithBeanAware deploy3 = (ObjectWithBeanAware) getBean("deploy3");
      assertNotNull(deploy3);
      SimpleBean simple3 = deploy3.getSimpleBean();
      assertNotNull(simple3);
      assertEquals("fromBoot", simple3.getConstructorString());

      ObjectWithBeanAware deploy4 = (ObjectWithBeanAware) getBean("deploy4");
      assertNotNull(deploy4);
      SimpleBean simple4 = deploy4.getSimpleBean();
      assertNotNull(simple4);
      assertEquals("fromApp", simple4.getConstructorString());
   }

}
