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
package org.jboss.test.dependency.controller.test;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;

import junit.framework.Test;

/**
 * AliasUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AliasUnitTestCase extends AbstractDependencyTest
{
   public static Test suite()
   {
      return suite(AliasUnitTestCase.class);
   }
   
   public AliasUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testSimpleAlias() throws Throwable
   {
      ControllerContext original = assertCreateInstall("Name1", "Alias1");
      try
      {
          ControllerContext ctx = assertContext("Alias1", ControllerState.INSTALLED);
          assertEquals("Name1", ctx.getName());
      }
      finally
      {
         try
         {
            assertUninstall(original);
         }
         finally
         {
            assertNoContext("Alias1");
         }
      }
   }
   
   public void testAliasAlreadyInstalled() throws Throwable
   {
      ControllerContext alreadyDone = assertCreateInstall("AlreadyDone");
      try
      {
         ControllerContext broken = createControllerContext("Name1", "AlreadyDone");
         try
         {
            install(broken);
         }
         catch (Throwable t)
         {
            checkThrowable(IllegalStateException.class, t);
         }
         assertNoContext("Name1");
         
         ControllerContext notAlias = assertContext("AlreadyDone", ControllerState.INSTALLED);
         assertEquals("AlreadyDone", notAlias.getName());
      }
      finally
      {
         assertUninstall(alreadyDone);
      }
      assertCreateInstall("Name1", "AlreadyDone");
   }
}
