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

import org.jboss.managed.api.DeploymentTemplateInfo;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.plugins.factory.DeploymentTemplateInfoFactory;
import org.jboss.test.managed.factory.AbstractManagedObjectFactoryTest;
import org.jboss.test.managed.factory.support.template.ManagementObjectExplicit;


/**
 * DeploymentTemplateInfo factory tests.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class DeploymentTemplateInfoUnitTestCase extends AbstractManagedObjectFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(DeploymentTemplateInfoUnitTestCase.class);
   }

   /**
    * Create a new DeploymentTemplateInfoUnitTestCase.
    * 
    * @param name the test name
    */
   public DeploymentTemplateInfoUnitTestCase(String name)
   {
      super(name);
   }
   
   /**
    * Test Explicit properties included
    */
   public void testExplicit()
   {
      ManagedObject managedObject = createManagedObject(ManagementObjectExplicit.class);
      checkManagedObjectDefaults(ManagementObjectExplicit.class, managedObject);
      checkManagedProperties(managedObject, "property1");
      DeploymentTemplateInfoFactory factory = new DeploymentTemplateInfoFactory();
      DeploymentTemplateInfo info = factory.createTemplateInfo(managedObject, "testExplicit", "testExplicit");
      log.info(info);
      assertEquals("testExplicit", info.getName());
      assertEquals("testExplicit", info.getDescription());
      assertTrue("property1 is in template info", info.getProperties().containsKey("property1"));
   }
   

}