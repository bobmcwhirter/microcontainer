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

import java.util.Set;

import org.jboss.beans.metadata.spi.RelatedClassMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import junit.framework.Test;

/**
 * RelatedClassJaxbTestCase.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class RelatedClassJaxbTestCase extends AbstractMCTest
{
   protected RelatedClassMetaData getRelated() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean();
      Set<RelatedClassMetaData> related = bean.getRelated();
      assertNotNull(related);
      assertEquals(1, related.size());
      RelatedClassMetaData rcmd = related.iterator().next();
      assertNotNull(rcmd);
      return rcmd;
   }

   public void testRelatedWithName() throws Exception
   {
      RelatedClassMetaData related = getRelated();
      assertEquals("java.lang.Object", related.getClassName());
   }

   public void testRelatedWithEnabled() throws Exception
   {
      RelatedClassMetaData related = getRelated();
      assertEquals("java.lang.Object", related.getClassName());
      Set<Object> enabled = related.getEnabled();
      assertNotNull(enabled);
      assertEquals(1, enabled.size());
      assertEquals("aop", enabled.iterator().next());
   }

   public void testRelatedWithEnableds() throws Exception
   {
      RelatedClassMetaData related = getRelated();
      assertEquals("java.lang.Object", related.getClassName());
      Set<Object> enabled = related.getEnabled();
      assertNotNull(enabled);
      assertEquals(2, enabled.size());
   }

   public static Test suite()
   {
      return suite(RelatedClassJaxbTestCase.class);
   }

   public RelatedClassJaxbTestCase(String name)
   {
      super(name);
   }
}