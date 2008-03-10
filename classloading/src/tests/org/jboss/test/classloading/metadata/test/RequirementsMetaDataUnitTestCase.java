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

import org.jboss.classloading.spi.metadata.RequirementsMetaData;
import org.jboss.classloading.spi.metadata.Requirement;
import org.jboss.classloading.spi.metadata.ClassLoadingMetaDataFactory;
import org.jboss.classloading.spi.version.VersionRange;
import org.jboss.test.classloading.AbstractClassLoadingTestWithSecurity;

/**
 * RequirementsMetaDataUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class RequirementsMetaDataUnitTestCase extends AbstractClassLoadingTestWithSecurity
{
   public static Test suite()
   {
      return suite(RequirementsMetaDataUnitTestCase.class);
   }

   public RequirementsMetaDataUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testConstructor() throws Exception
   {
      RequirementsMetaData metadata = new RequirementsMetaData();
      assertNull(metadata.getRequirements());
   }
   
   public void testSetRequirements() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();

      RequirementsMetaData metadata = new RequirementsMetaData();
      assertNull(metadata.getRequirements());
      
      List<Requirement> list = new ArrayList<Requirement>();
      list.add(factory.createRequireModule("module", new VersionRange("1.0.0")));
      list.add(factory.createRequirePackage("package", new VersionRange("1.0.0")));
      metadata.setRequirements(list);
      
      assertEquals(list, metadata.getRequirements());
   }
   
   public void testAddRequirements() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();

      RequirementsMetaData metadata = new RequirementsMetaData();
      assertNull(metadata.getRequirements());
      
      List<Requirement> list = new ArrayList<Requirement>();
      Requirement Requirement = factory.createRequireModule("module", new VersionRange("1.0.0"));
      metadata.addRequirement(Requirement);
      list.add(Requirement);
      assertEquals(list, metadata.getRequirements());

      Requirement = factory.createRequirePackage("module", new VersionRange("1.0.0"));
      metadata.addRequirement(Requirement);
      list.add(Requirement);
      assertEquals(list, metadata.getRequirements());
   }
   
   public void testRemoveRequirements() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();

      RequirementsMetaData metadata = new RequirementsMetaData();
      assertNull(metadata.getRequirements());

      Requirement module = factory.createRequireModule("module", new VersionRange("1.0.0"));
      Requirement pkge = factory.createRequirePackage("module", new VersionRange("1.0.0"));

      List<Requirement> list = new ArrayList<Requirement>();
      list.add(module);
      list.add(pkge);
      metadata.setRequirements(list);
      
      metadata.removeRequirement(module);
      list.remove(module);
      assertEquals(list, metadata.getRequirements());
      
      metadata.removeRequirement(pkge);
      list.remove(pkge);
      assertEquals(list, metadata.getRequirements());
   }
   
   public void testSerialization() throws Exception
   {
      ClassLoadingMetaDataFactory factory = ClassLoadingMetaDataFactory.getInstance();

      RequirementsMetaData test = new RequirementsMetaData();
      test.addRequirement(factory.createRequireModule("module", new VersionRange("1.0.0")));
      test.addRequirement(factory.createRequirePackage("package", new VersionRange("1.0.0")));
      RequirementsMetaData other = serializeDeserialize(test, RequirementsMetaData.class);
      assertEquals(test.getRequirements(), other.getRequirements());
   }
}
