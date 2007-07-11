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

import junit.framework.Test;

import org.jboss.managed.api.ManagedObject;
import org.jboss.test.managed.factory.AbstractManagedObjectFactoryTest;
import org.jboss.test.managed.factory.support.ManagementObjectAll;
import org.jboss.test.managed.factory.support.ManagementObjectExplicit;
import org.jboss.test.managed.factory.support.ManagementObjectIgnored;
import org.jboss.test.managed.factory.support.ManagementObjectNone;

/**
 * ManagementObjectPropertiesUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ManagementObjectPropertiesUnitTestCase extends AbstractManagedObjectFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(ManagementObjectPropertiesUnitTestCase.class);
   }

   /**
    * Create a new ManagementObjectPropertiesUnitTestCase.
    * 
    * @param name the test name
    */
   public ManagementObjectPropertiesUnitTestCase(String name)
   {
      super(name);
   }
   
   /**
    * Test all properties included
    */
   public void testAll()
   {
      ManagedObject managedObject = createManagedObject(ManagementObjectAll.class);
      checkManagedObjectDefaults(ManagementObjectAll.class, managedObject);
      checkManagedProperties(managedObject, "property1", "property2");
   }
   
   /**
    * Test some properties ignored
    */
   public void testIgnored()
   {
      ManagedObject managedObject = createManagedObject(ManagementObjectIgnored.class);
      checkManagedObjectDefaults(ManagementObjectIgnored.class, managedObject);
      checkManagedProperties(managedObject, "property2");
   }
   
   /**
    * Test no properties included
    */
   public void testNone()
   {
      ManagedObject managedObject = createManagedObject(ManagementObjectNone.class);
      checkManagedObjectDefaults(ManagementObjectNone.class, managedObject);
      checkManagedProperties(managedObject, new String[0]);
   }
   
   /**
    * Test explicit properties included
    */
   public void testExplicit()
   {
      ManagedObject managedObject = createManagedObject(ManagementObjectExplicit.class);
      checkManagedObjectDefaults(ManagementObjectExplicit.class, managedObject);
      checkManagedProperties(managedObject, "property1");
   }
}