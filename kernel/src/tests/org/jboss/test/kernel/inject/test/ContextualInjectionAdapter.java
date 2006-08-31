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
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public abstract class ContextualInjectionAdapter extends MicrocontainerTest
{

   public ContextualInjectionAdapter(String name)
   {
      super(name);
   }

   protected abstract String getResource();

   protected abstract void checkInjection();

   protected void enableTrace()
   {
   }

   public void testInjection() throws Throwable
   {
      enableTrace();
      KernelDeployment deployment = deploy(getResource());
      try
      {
         validate();
         checkInjection();
      }
      finally
      {
         undeploy(deployment);
      }
   }
}
