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
package org.jboss.test.kernel.annotations.test;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.test.kernel.annotations.support.AnnotationTester;
import org.jboss.test.kernel.annotations.support.FactoryMethodAnnotationTester;
import org.jboss.test.kernel.config.test.AbstractKernelConfigTest;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class FactoryMethodTestCase extends AbstractKernelConfigTest
{
   public FactoryMethodTestCase(String name) throws Throwable
   {
      super(name);
   }

   public FactoryMethodTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(FactoryMethodTestCase.class);
   }

   public void testFactoryMethod() throws Throwable
   {
      AnnotationTester tester = getTester();
      assertEquals("FromAnnotations", tester.getValue());
   }

   protected AnnotationTester getTester() throws Throwable
   {
      Object tester = instantiate(new AbstractBeanMetaData("Tester", FactoryMethodAnnotationTester.class.getName()));
      assertNotNull(tester);
      assertInstanceOf(tester, AnnotationTester.class);
      return (AnnotationTester)tester;
   }
}
