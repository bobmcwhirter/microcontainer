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
package org.jboss.test.kernel.metadata.test;

import junit.framework.Test;
import org.jboss.dependency.spi.ScopeInfo;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.metadata.spi.MetaData;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.jboss.test.kernel.metadata.support.TestAnnotationA;
import org.jboss.test.kernel.metadata.support.TestAnnotationB;
import org.jboss.test.kernel.metadata.support.TestAnnotationC;
import org.jboss.test.kernel.metadata.support.TestCachingMetaDataRepository;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class CachingMetaDataRepositoryTestCase extends AbstractMetaDataTest
{
   public CachingMetaDataRepositoryTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(CachingMetaDataRepositoryTestCase.class);
   }

   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      System.setProperty(KernelMetaDataRepository.class.getName(), TestCachingMetaDataRepository.class.getName());
      System.setProperty("LRUPolicyCaching.min", "2");
      System.setProperty("LRUPolicyCaching.max", "10");
      return MicrocontainerTest.getDelegate(clazz);
   }

   protected MetaData assertMetaData()
   {
      return assertMetaData("Name1");
   }

   protected MetaData assertMetaData(String name)
   {
      KernelControllerContext context = getControllerContext(name);
      assertNotNull(context);
      ScopeInfo scopeInfo = context.getScopeInfo();
      assertNotNull(scopeInfo);
      MetaData metaData = scopeInfo.getMetaData();
      assertNotNull(metaData);
      return metaData;
   }

   public void testTouchCachingMetaDataRepository() throws Exception
   {
      TestCachingMetaDataRepository.touched = false;

      KernelDeployment deployment = deploy("ClassAnnotationTestCase_Override.xml");
      try
      {
         MetaData metaData = assertMetaData();
         TestAnnotationA a = assertAnnotation(metaData, TestAnnotationA.class);
         assertEquals("Overridden", a.value());
         TestAnnotationB b = assertAnnotation(metaData, TestAnnotationB.class);
         assertEquals("NotOverridden", b.value());
         assertNoAnnotation(metaData, TestAnnotationC.class);

         assertTrue(TestCachingMetaDataRepository.touched);
      }
      finally
      {
         undeploy(deployment);
      }
   }
}
