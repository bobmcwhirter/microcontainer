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

import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.jboss.test.kernel.junit.MicrocontainerTestDelegate;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.dependency.spi.ControllerMode;

/**
 * Abstract Deployment Test Case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractOnDemandDeploymentTest extends MicrocontainerTest
{
   public AbstractOnDemandDeploymentTest(String name)
   {
      super(name);
   }

   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      MicrocontainerTestDelegate delegate = (MicrocontainerTestDelegate) MicrocontainerTest.getDelegate(clazz);
      delegate.setDefaultMode(ControllerMode.ON_DEMAND);
      delegate.enableSecurity = true;
      return delegate;
   }

   protected void configureLogging()
   {
      //enableTrace("org.jboss.dependency");
      //enableTrace("org.jboss.kernel.plugins.dependency");
      //enableTrace("org.jboss.kernel.plugins.deployment");
   }
}
