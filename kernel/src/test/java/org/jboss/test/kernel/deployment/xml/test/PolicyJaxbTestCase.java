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
import org.jboss.beans.metadata.spi.policy.PolicyMetaData;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class PolicyJaxbTestCase extends AbstractPolicyTest
{
   public PolicyJaxbTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(PolicyJaxbTestCase.class);
   }

   public void testPolicy() throws Throwable
   {
      PolicyMetaData policy = unmarshalPolicy();
      assertNull(policy.getName());
      assertNull(policy.getExtends());
      assertNull(policy.getScope());
      assertNull(policy.getAnnotations());
      assertNull(policy.getBindings());
   }

   public void testPolicyWithName() throws Throwable
   {
      PolicyMetaData policy = unmarshalPolicy();
      assertEquals("NamedPolicy", policy.getName());
      assertNull(policy.getExtends());
      assertNull(policy.getScope());
      assertNull(policy.getAnnotations());
      assertNull(policy.getBindings());
   }

   public void testPolicyWithExtends() throws Throwable
   {
      PolicyMetaData policy = unmarshalPolicy();
      assertNull(policy.getName());
      assertEquals("ExtendablePolicy", policy.getExtends());
      assertNull(policy.getScope());
      assertNull(policy.getAnnotations());
      assertNull(policy.getBindings());
   }

   public void testPolicyWithScope() throws Throwable
   {
      PolicyMetaData policy = unmarshalPolicy();
      assertNull(policy.getName());
      assertNull(policy.getExtends());
      assertNotNull(policy.getScope());
      assertEquals("DefaultLevel", policy.getScope().getLevel());
      assertNull(policy.getAnnotations());
      assertNull(policy.getBindings());
   }

   public void testPolicyWithAnnotations() throws Throwable
   {
/*
      PolicyMetaData policy = unmarshal("PolicyWithAnnotations.xml", AbstractPolicyMetaData.class);
      assertNull(policy.getName());
      assertNull(policy.getExtends());
      assertNull(policy.getScope());
      assertNotNull(policy.getAnnotations());
      assertTrue(policy.getAnnotations().size() > 0);
      assertNull(policy.getBindings());
*/
   }

   public void testPolicyWithBindings() throws Throwable
   {
      PolicyMetaData policy = unmarshalPolicy();
      assertNull(policy.getName());
      assertNull(policy.getExtends());
      assertNull(policy.getScope());
      assertNull(policy.getAnnotations());
      assertNotNull(policy.getBindings());
      assertTrue(policy.getBindings().size() > 0);
   }

}
