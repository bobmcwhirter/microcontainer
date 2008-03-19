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
package org.jboss.test.kernel.dependency.test;

import java.security.AccessControlException;

import junit.framework.Test;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;

/**
 * Field access tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class FieldAccessTestCase extends AbstractKernelDependencyTest
{
   public FieldAccessTestCase(String name) throws Throwable
   {
      super(name, true);
   }

   public static Test suite()
   {
      return suite(FieldAccessTestCase.class);
   }

   public void testFieldAccess() throws Throwable
   {
      ControllerContext privateContext = install(0, "private");
      assertNotNull(privateContext);
      assertNotNull(privateContext.getError());
      assertInstanceOf(privateContext.getError(), AccessControlException.class, false);

      ControllerContext protectedContext = install(1, "protected");
      assertNotNull(protectedContext);
      assertNotNull(protectedContext.getError());
      assertInstanceOf(protectedContext.getError(), AccessControlException.class, false);

      ControllerContext publicContext = install(2, "public");
      assertNotNull(publicContext);
      assertEquals(ControllerState.INSTALLED, publicContext.getState());
      assertNull(publicContext.getError());
   }
}
