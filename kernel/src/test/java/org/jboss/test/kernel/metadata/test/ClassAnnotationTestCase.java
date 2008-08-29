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
package org.jboss.test.kernel.metadata.test;

import junit.framework.Test;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.metadata.spi.signature.MethodSignature;
import org.jboss.test.kernel.metadata.support.TestAnnotationA;
import org.jboss.test.kernel.metadata.support.TestAnnotationB;
import org.jboss.test.kernel.metadata.support.TestAnnotationC;

/**
 * ClassAnnotationTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassAnnotationTestCase extends AbstractMetaDataTest
{
   public static Test suite()
   {
      return suite(ClassAnnotationTestCase.class);
   }

   public ClassAnnotationTestCase(String name) throws Throwable
   {
      super(name);
   }

   protected MetaData assertMetaData()
   {
      return assertMetaData("Name1");
   }

   public void testClassAnnotationNoOverride() throws Throwable
   {
      ScopeKey scope = null;
      KernelDeployment deployment = deploy("ClassAnnotationTestCase_NoOverride.xml");
      try
      {
         scope = assertRetrievals("Name1");
         MetaData metaData = assertMetaData();
         TestAnnotationA a = assertAnnotation(metaData, TestAnnotationA.class);
         assertEquals("NotOverridden", a.value());
         TestAnnotationB b = assertAnnotation(metaData, TestAnnotationB.class);
         assertEquals("NotOverridden", b.value());
         assertNoAnnotation(metaData, TestAnnotationC.class);
      }
      finally
      {
         undeploy(deployment);
         if (scope != null)
            assertNoRetrievals("Name1", scope);
      }
   }

   public void testClassAnnotationOverride() throws Throwable
   {
      ScopeKey scope = null;
      KernelDeployment deployment = deploy("ClassAnnotationTestCase_Override.xml");
      try
      {
         scope = assertRetrievals("Name1");
         MetaData metaData = assertMetaData();
         TestAnnotationA a = assertAnnotation(metaData, TestAnnotationA.class);
         assertEquals("Overridden", a.value());
         TestAnnotationB b = assertAnnotation(metaData, TestAnnotationB.class);
         assertEquals("NotOverridden", b.value());
         assertNoAnnotation(metaData, TestAnnotationC.class);
      }
      finally
      {
         undeploy(deployment);
         if (scope != null)
            assertNoRetrievals("Name1", scope);
      }
   }

   public void testClassAnnotationNew() throws Throwable
   {
      ScopeKey scope = null;
      KernelDeployment deployment = deploy("ClassAnnotationTestCase_New.xml");
      try
      {
         scope = assertRetrievals("Name1");
         MetaData metaData = assertMetaData();
         TestAnnotationA a = assertAnnotation(metaData, TestAnnotationA.class);
         assertEquals("NotOverridden", a.value());
         TestAnnotationB b = assertAnnotation(metaData, TestAnnotationB.class);
         assertEquals("NotOverridden", b.value());
         assertAnnotation(metaData, TestAnnotationC.class);
      }
      finally
      {
         undeploy(deployment);
         if (scope != null)
            assertNoRetrievals("Name1", scope);
      }
   }

   public void testPropertyAnnotationNoOverride() throws Throwable
   {
      ScopeKey scope = null;
      KernelDeployment deployment = deploy("ClassAnnotationTestCase_NoOverrideProperty.xml");
      try
      {
         scope = assertRetrievals("Name1");
         MetaData classMetaData = assertMetaData();
         MetaData metaData = classMetaData.getComponentMetaData(new MethodSignature("getSomething"));
         TestAnnotationA a = assertAnnotation(metaData, TestAnnotationA.class);
         assertEquals("NotOverridden", a.value());
         TestAnnotationB b = assertAnnotation(metaData, TestAnnotationB.class);
         assertEquals("NotOverridden", b.value());
         assertNoAnnotation(metaData, TestAnnotationC.class);
         metaData = classMetaData.getComponentMetaData(new MethodSignature("setSomething", String.class));
         a = assertAnnotation(metaData, TestAnnotationA.class);
         assertEquals("NotOverridden", a.value());
         b = assertAnnotation(metaData, TestAnnotationB.class);
         assertEquals("NotOverridden", b.value());
         assertNoAnnotation(metaData, TestAnnotationC.class);
      }
      finally
      {
         undeploy(deployment);
         if (scope != null)
            assertNoRetrievals("Name1", scope);
      }
   }

   public void testPropertyAnnotationOverride() throws Throwable
   {
      ScopeKey scope = null;
      KernelDeployment deployment = deploy("ClassAnnotationTestCase_OverrideProperty.xml");
      try
      {
         scope = assertRetrievals("Name1");
         MetaData classMetaData = assertMetaData();
         MetaData metaData = classMetaData.getComponentMetaData(new MethodSignature("getSomething"));
         TestAnnotationA a = assertAnnotation(metaData, TestAnnotationA.class);
         assertEquals("Overridden", a.value());
         TestAnnotationB b = assertAnnotation(metaData, TestAnnotationB.class);
         assertEquals("NotOverridden", b.value());
         assertNoAnnotation(metaData, TestAnnotationC.class);
         metaData = classMetaData.getComponentMetaData(new MethodSignature("setSomething", String.class));
         a = assertAnnotation(metaData, TestAnnotationA.class);
         assertEquals("Overridden", a.value());
         b = assertAnnotation(metaData, TestAnnotationB.class);
         assertEquals("NotOverridden", b.value());
         assertNoAnnotation(metaData, TestAnnotationC.class);
      }
      finally
      {
         undeploy(deployment);
         if (scope != null)
            assertNoRetrievals("Name1", scope);
      }
   }

   public void testPropertyAnnotationNew() throws Throwable
   {
      ScopeKey scope = null;
      KernelDeployment deployment = deploy("ClassAnnotationTestCase_NewProperty.xml");
      try
      {
         scope = assertRetrievals("Name1");
         MetaData classMetaData = assertMetaData();
         MetaData metaData = classMetaData.getComponentMetaData(new MethodSignature("getSomething"));
         TestAnnotationA a = assertAnnotation(metaData, TestAnnotationA.class);
         assertEquals("NotOverridden", a.value());
         TestAnnotationB b = assertAnnotation(metaData, TestAnnotationB.class);
         assertEquals("NotOverridden", b.value());
         assertAnnotation(metaData, TestAnnotationC.class);
         metaData = classMetaData.getComponentMetaData(new MethodSignature("setSomething", String.class));
         a = assertAnnotation(metaData, TestAnnotationA.class);
         assertEquals("NotOverridden", a.value());
         b = assertAnnotation(metaData, TestAnnotationB.class);
         assertEquals("NotOverridden", b.value());
         assertAnnotation(metaData, TestAnnotationC.class);
      }
      finally
      {
         undeploy(deployment);
         if (scope != null)
            assertNoRetrievals("Name1", scope);
      }
   }
}