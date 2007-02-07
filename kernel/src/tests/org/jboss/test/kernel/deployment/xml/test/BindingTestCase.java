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
package org.jboss.test.kernel.deployment.xml.test;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.policy.AbstractPolicyMetaData;
import org.jboss.beans.metadata.spi.policy.BindingMetaData;
import org.jboss.beans.metadata.spi.policy.PolicyMetaData;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BindingTestCase extends AbstractXMLTest
{
   public BindingTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BindingTestCase.class);
   }

   public void testBindingWithNoName() throws Throwable
   {
      PolicyMetaData policy = unmarshal("BindingWithNoName.xml", AbstractPolicyMetaData.class);
      assertFalse(policy.getBindings().isEmpty());
      assertEquals(1, policy.getBindings().size());
      BindingMetaData binding = policy.getBindings().iterator().next();
//      assertEquals("", binding.getName()); // todo
      assertNotNull(binding.getValue());
   }

   public void testBindingWithPlainValue() throws Throwable
   {
      PolicyMetaData policy = unmarshal("BindingWithNoName.xml", AbstractPolicyMetaData.class);
      assertFalse(policy.getBindings().isEmpty());
      assertEquals(1, policy.getBindings().size());
      BindingMetaData binding = policy.getBindings().iterator().next();
      assertNull(binding.getName());
      assertNotNull(binding.getValue());
   }

   public void testBindingWithComplexValue() throws Throwable
   {
      PolicyMetaData policy = unmarshal("BindingWithNoName.xml", AbstractPolicyMetaData.class);
      assertFalse(policy.getBindings().isEmpty());
      assertEquals(1, policy.getBindings().size());
      BindingMetaData binding = policy.getBindings().iterator().next();
      assertNull(binding.getName());
      assertNotNull(binding.getValue());
   }

}
