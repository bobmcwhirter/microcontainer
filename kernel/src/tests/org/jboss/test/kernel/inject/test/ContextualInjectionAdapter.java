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
package org.jboss.test.kernel.inject.test;

import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.kernel.junit.MicrocontainerTest;

/**
 * Test unit deployment contextual injection helper class.
 * 
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public abstract class ContextualInjectionAdapter extends MicrocontainerTest
{
   public ContextualInjectionAdapter(String name)
   {
      super(name);
   }

   protected void enableTrace()
   {
   }

   protected void execute(TestUnit testUnit) throws Throwable
   {
      enableTrace();
      KernelDeployment deployment = deploy(testUnit.getResource());
      try
      {
         validate();
         testUnit.executeTest();
      }
      finally
      {
         undeploy(deployment);
      }
   }

   protected interface TestUnit
   {
      String getResource();

      void executeTest() throws Throwable;
   }

}
