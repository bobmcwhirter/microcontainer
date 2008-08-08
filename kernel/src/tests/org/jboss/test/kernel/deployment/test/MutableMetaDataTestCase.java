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
package org.jboss.test.kernel.deployment.test;

import junit.framework.Test;

import org.jboss.annotation.factory.AnnotationCreator;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.metadata.plugins.loader.memory.MemoryMetaDataLoader;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.scope.CommonLevels;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.metadata.spi.signature.MethodSignature;
import org.jboss.metadata.spi.signature.Signature;
import org.jboss.test.kernel.deployment.support.TestAnnotation1;
import org.jboss.test.kernel.deployment.support.TestAnnotation2;
import org.jboss.test.kernel.deployment.support.TestAnnotation3;

/**
 * MutableMetaDataTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MutableMetaDataTestCase extends AbstractDeploymentTest
{
   public static Test suite()
   {
      return suite(MutableMetaDataTestCase.class);
   }

   public MutableMetaDataTestCase(String name) throws Throwable
   {
      super(name);
   }

   public void testMutableMetaData() throws Throwable
   {
      MutableMetaDataRepository repository = getMetaDataRepository().getMetaDataRepository();
      ScopeKey instanceScope = new ScopeKey(CommonLevels.INSTANCE, "TestBean");
      MemoryMetaDataLoader loader = new MemoryMetaDataLoader(instanceScope);
      TestAnnotation2 annotation = (TestAnnotation2) AnnotationCreator.createAnnotation("@org.jboss.test.kernel.deployment.support.TestAnnotation2", TestAnnotation2.class);
      loader.addAnnotation(annotation);
      repository.addMetaDataRetrieval(loader);
      ScopeKey setStringScope = new ScopeKey(CommonLevels.JOINPOINT, "setString");
      MemoryMetaDataLoader stringProperty = new MemoryMetaDataLoader(setStringScope);
      stringProperty.addAnnotation(annotation);
      Signature signature = new MethodSignature("setString", String.class);
      loader.addComponentMetaDataRetrieval(signature, stringProperty);
      
      KernelDeployment deployment = deploy("MutableMetaDataTestCase_NotAutomatic.xml");
      try
      {
         validate();
         
         ControllerContext ctx = getControllerContext("TestBean");
         MetaData metaData = ctx.getScopeInfo().getMetaData();
         assertNotNull(metaData);
      
         assertNotNull("TestAnnotation1 from xml", metaData.getAnnotation(TestAnnotation1.class));
         assertNotNull("TestAnnotation2 preconfigured", metaData.getAnnotation(TestAnnotation2.class));
         assertNotNull("TestAnnotation3 from class", metaData.getAnnotation(TestAnnotation3.class));
         
         MetaData setStringMetaData = metaData.getComponentMetaData(signature);
         assertNotNull("TestAnnotation1 from xml", setStringMetaData.getAnnotation(TestAnnotation1.class));
         assertNotNull("TestAnnotation2 preconfigured", setStringMetaData.getAnnotation(TestAnnotation2.class));
         assertNotNull("TestAnnotation3 from class", setStringMetaData.getAnnotation(TestAnnotation3.class));
      }
      finally
      {
         undeploy(deployment);
      }
      
      // Check the preconfigured stuff still exists
      MetaData metaData = repository.getMetaData(instanceScope);
      assertNotNull(metaData);
      assertNull("TestAnnotation1 from xml", metaData.getAnnotation(TestAnnotation1.class));
      assertNotNull("TestAnnotation2 preconfigured", metaData.getAnnotation(TestAnnotation2.class));
      assertNull("TestAnnotation3 from class", metaData.getAnnotation(TestAnnotation3.class));
      MetaData setStringMetaData = metaData.getComponentMetaData(signature);
      assertNull("TestAnnotation1 from xml", setStringMetaData.getAnnotation(TestAnnotation1.class));
      assertNotNull("TestAnnotation2 preconfigured", setStringMetaData.getAnnotation(TestAnnotation2.class));
      assertNull("TestAnnotation3 from class", setStringMetaData.getAnnotation(TestAnnotation3.class));
   }
}