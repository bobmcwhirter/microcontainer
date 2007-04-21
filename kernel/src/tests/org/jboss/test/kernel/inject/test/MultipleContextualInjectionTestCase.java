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
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.inject.support.PropertyInjectTestObject;

/**
 * Multiple injection - should 'fail'.
 * 
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class MultipleContextualInjectionTestCase extends SingleContextualInjectionAdapter
{
   public MultipleContextualInjectionTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(MultipleContextualInjectionTestCase.class);
   }

   protected String getResource()
   {
      return "MultipleContextualInjection.xml";
   }

   protected void checkInjection()
   {
      PropertyInjectTestObject test = (PropertyInjectTestObject) getBean("testObject", ControllerState.DESCRIBED);
      assertNull(test);
   }

   protected void validate() throws Exception
   {
      try
      {
         super.validate();
         fail("Should not be here!");
      }
      catch (IllegalStateException e)
      {
         if (e.getMessage().startsWith("Incompletely"))
            getLog().debug("Got expected Incomplete deployment exception: " + e);
         else
            throw e;
      }
   }
}
