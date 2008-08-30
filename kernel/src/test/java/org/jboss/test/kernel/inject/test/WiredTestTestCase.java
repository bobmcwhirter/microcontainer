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
package org.jboss.test.kernel.inject.test;

import junit.framework.Test;
import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.test.kernel.inject.support.StringValueObject;
import org.jboss.test.kernel.inject.support.TesterInterface;
import org.jboss.test.kernel.junit.MicrocontainerTest;

/**
 * Test wired test. :-)
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class WiredTestTestCase extends MicrocontainerTest
{
   private TesterInterface tester;
   private StringValueObject valueObject;
   private boolean lifecycleExecuted = false;

   public WiredTestTestCase(String name)
   {
      super(name, true);
   }

   public static Test suite()
   {
      return suite(WiredTestTestCase.class);
   }

   public void start()
   {
      lifecycleExecuted = true;
   }

   @Inject
   public void setTester(TesterInterface tester)
   {
      this.tester = tester;
   }

   @Inject
   public void setValueObject(StringValueObject valueObject)
   {
      this.valueObject = valueObject;
   }

   public void testWiredTest() throws Throwable
   {
      assertTrue(lifecycleExecuted);
      assertNotNull(tester);
      assertNotNull(valueObject);
   }
}
