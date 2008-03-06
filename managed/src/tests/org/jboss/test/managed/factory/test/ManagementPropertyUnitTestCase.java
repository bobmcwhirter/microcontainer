/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.managed.factory.test;

import java.lang.annotation.Annotation;
import java.util.Map;

import junit.framework.Test;

import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.api.annotation.ManagementObjectID;
import org.jboss.managed.api.annotation.ManagementProperty;
import org.jboss.managed.api.annotation.ViewUse;
import org.jboss.test.managed.factory.AbstractManagedObjectFactoryTest;
import org.jboss.test.managed.factory.support.ManagementPropertyAnnotations;
import org.jboss.test.managed.factory.support.ManagementPropertyDescription;
import org.jboss.test.managed.factory.support.ManagementPropertyMandatory;
import org.jboss.test.managed.factory.support.ManagementPropertySimpleManaged;

/**
 * ManagementPropertyUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ManagementPropertyUnitTestCase extends AbstractManagedObjectFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(ManagementPropertyUnitTestCase.class);
   }

   /**
    * Create a new ManagementPropertyUnitTestCase.
    * 
    * @param name the test name
    */
   public ManagementPropertyUnitTestCase(String name)
   {
      super(name);
   }
   
   /**
    * Test the overridden description
    */
   public void testDescription()
   {
      ManagedObject managedObject = createAndCheckDefaultManagedObject(ManagementPropertyDescription.class);
      checkProperty(managedObject, "property", String.class, "changed", false, null);
   }
   
   /**
    * Test the overridden mandatory
    */
   public void testMandatory()
   {
      ManagedObject managedObject = createAndCheckDefaultManagedObject(ManagementPropertyMandatory.class);
      checkProperty(managedObject, "property", String.class, "property", true, null);
   }
   
   /**
    * Test the overidden property to managed object
    */
   public void testSimpleManaged()
   {
      ManagementPropertySimpleManaged test = new ManagementPropertySimpleManaged();
      ManagedObject managedObject = checkManagedObject(test);
      checkPropertyIsManagedObject(managedObject, "property", "property", false, test.getProperty());
   }

   /**
    * Test property annotations
    */
   public void testAnnotations()
   {
      ManagedObject managedObject = createAndCheckDefaultManagedObject(ManagementPropertyAnnotations.class);
      ManagedProperty managedProperty = managedObject.getProperty("runtime");
      assertNotNull(managedProperty);
      Map<String, Annotation> annotations = managedProperty.getAnnotations();
      assertEquals(2, annotations.size());
      assertTrue(annotations.containsKey(ManagementProperty.class.getName()));
      assertTrue(annotations.containsKey(ManagementObjectID.class.getName()));
   }

   /**
    * Test property ViewUse
    */
   public void testViewUse()
   {
      ManagedObject managedObject = createAndCheckDefaultManagedObject(ManagementPropertyAnnotations.class);
      ManagedProperty runtime = managedObject.getProperty("runtime");
      assertNotNull(runtime);
      assertTrue("has ViewUse.RUNTIME", runtime.hasViewUse(ViewUse.RUNTIME));
      assertTrue("!has ViewUse.CONFIGURATION", !runtime.hasViewUse(ViewUse.CONFIGURATION));
      assertTrue("!has ViewUse.STATISTIC", !runtime.hasViewUse(ViewUse.STATISTIC));

      ManagedProperty configuration = managedObject.getProperty("configuration");
      assertTrue("has ViewUse.CONFIGURATION", configuration.hasViewUse(ViewUse.CONFIGURATION));

      ManagedProperty stat = managedObject.getProperty("stat");
      assertTrue("has ViewUse.STATISTIC", stat.hasViewUse(ViewUse.STATISTIC));
   }
}