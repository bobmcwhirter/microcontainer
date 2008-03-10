/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.test.classloading.metadata.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;

import org.jboss.classloading.spi.metadata.CapabilitiesMetaData;
import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.test.classloading.AbstractClassLoadingTestWithSecurity;

/**
 * CapabilitiesMetaDataUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class CapabilitiesMetaDataUnitTestCase extends AbstractClassLoadingTestWithSecurity
{
   public static Test suite()
   {
      return suite(CapabilitiesMetaDataUnitTestCase.class);
   }

   public CapabilitiesMetaDataUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testConstructor() throws Exception
   {
      CapabilitiesMetaData metadata = new CapabilitiesMetaData();
      assertNull(metadata.getCapabilities());
   }
   
   public void testSetCapabilities() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();

      CapabilitiesMetaData metadata = new CapabilitiesMetaData();
      assertNull(metadata.getCapabilities());
      
      List<Capability> list = new ArrayList<Capability>();
      list.add(factory.createModule("module", "1.0.0"));
      list.add(factory.createPackage("package", "1.0.0"));
      metadata.setCapabilities(list);
      
      assertEquals(list, metadata.getCapabilities());
   }
   
   public void testAddCapabilities() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();

      CapabilitiesMetaData metadata = new CapabilitiesMetaData();
      assertNull(metadata.getCapabilities());
      
      List<Capability> list = new ArrayList<Capability>();
      Capability capability = factory.createModule("module", "1.0.0");
      metadata.addCapability(capability);
      list.add(capability);
      assertEquals(list, metadata.getCapabilities());

      capability = factory.createPackage("module", "1.0.0");
      metadata.addCapability(capability);
      list.add(capability);
      assertEquals(list, metadata.getCapabilities());
   }
   
   public void testRemoveCapabilities() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();

      CapabilitiesMetaData metadata = new CapabilitiesMetaData();
      assertNull(metadata.getCapabilities());

      Capability module = factory.createModule("module", "1.0.0");
      Capability pkge = factory.createPackage("module", "1.0.0");

      List<Capability> list = new ArrayList<Capability>();
      list.add(module);
      list.add(pkge);
      metadata.setCapabilities(list);
      
      metadata.removeCapability(module);
      list.remove(module);
      assertEquals(list, metadata.getCapabilities());
      
      metadata.removeCapability(pkge);
      list.remove(pkge);
      assertEquals(list, metadata.getCapabilities());
   }
   
   public void testSerialization() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();

      CapabilitiesMetaData test = new CapabilitiesMetaData();
      test.addCapability(factory.createModule("module", "1.0.0"));
      test.addCapability(factory.createPackage("package", "1.0.0"));
      CapabilitiesMetaData other = serializeDeserialize(test, CapabilitiesMetaData.class);
      assertEquals(test.getCapabilities(), other.getCapabilities());
   }
}
