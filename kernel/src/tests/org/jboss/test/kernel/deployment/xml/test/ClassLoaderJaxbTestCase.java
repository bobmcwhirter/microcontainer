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
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;

/**
 * ClassLoaderJaxbTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 56471 $
 */
public class ClassLoaderJaxbTestCase extends AbstractMCTest
{
   protected ClassLoaderMetaData getClassLoader() throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment();
      ClassLoaderMetaData classLoader = deployment.getClassLoader();
      assertNotNull(classLoader);
      return classLoader;
   }

   public void testClassLoaderWithBean() throws Exception
   {
      ClassLoaderMetaData classLoader = getClassLoader();
      assertNotNull(classLoader.getClassLoader());
      assertTrue(classLoader.getClassLoader() instanceof BeanMetaData);
   }

   public void testClassLoaderWithInject() throws Exception
   {
      ClassLoaderMetaData classLoader = getClassLoader();
      assertInjection(classLoader.getClassLoader());
   }

   public void testClassLoaderWithNull() throws Exception
   {
      ClassLoaderMetaData classLoader = getClassLoader();
      assertNullValue(classLoader.getClassLoader());
   }

   public void testClassLoaderWithWildcard() throws Exception
   {
      ClassLoaderMetaData classLoader = getClassLoader();
      assertWildcard(classLoader.getClassLoader());
   }

   /* TODO
   public void testClassLoaderBadNoValue() throws Exception
   {
      try
      {
         unmarshalDeployment("ClassLoaderBadNoValue.xml");
         fail("Should not be here");
      }
      catch (Exception expected)
      {
         checkJBossXBException(IllegalArgumentException.class, expected);
      }
   }
   */

   public static Test suite()
   {
      return ClassLoaderJaxbTestCase.suite(ClassLoaderJaxbTestCase.class);
   }

   public ClassLoaderJaxbTestCase(String name)
   {
      super(name);
   }
}
