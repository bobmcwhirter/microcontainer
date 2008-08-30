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
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.deployment.support.SimpleBean;
import org.jboss.test.kernel.deployment.support.SimpleObjectWithBean;

/**
 * Scoping tests.
 * Diff scopes.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class IllegalScopingTestCase extends ScopingDeploymentTest
{
   public IllegalScopingTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(IllegalScopingTestCase.class);
   }

   // ---- tests

   public void testIllegalScoping() throws Throwable
   {
      ClassLoader cl = (ClassLoader) getBean("cl");
      assertNotNull(cl);

      Throwable expected = null;
      try
      {
         getBean("deploy1");
      }
      catch(Throwable t)
      {
         expected = t;
      }
      assertNotNull(expected);

      SimpleObjectWithBean deploy = (SimpleObjectWithBean) getBean("deploy1", ControllerState.INSTANTIATED);
      assertNotNull(deploy);
      SimpleBean simple1 = deploy.getSimpleBean();
      assertNull(simple1);

      SimpleBean simple = (SimpleBean)getBean("simple");
      assertNotNull(simple);
      assertEquals("deployment2", simple.getConstructorString());
   }

}
