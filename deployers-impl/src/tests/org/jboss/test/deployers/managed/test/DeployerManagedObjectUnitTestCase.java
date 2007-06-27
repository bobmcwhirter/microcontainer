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
package org.jboss.test.deployers.managed.test;

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.spi.attachments.MutableAttachments;
import org.jboss.managed.api.ManagedObject;
import org.jboss.test.deployers.AbstractDeployerTest;
import org.jboss.test.deployers.managed.support.TestAttachment;
import org.jboss.test.deployers.managed.support.TestManagedObjectDeployer;
import org.jboss.util.graph.Graph;

/**
 * DeployerManagedObjectUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DeployerManagedObjectUnitTestCase extends AbstractDeployerTest
{
   private TestManagedObjectDeployer deployer = new TestManagedObjectDeployer();
   
   public static Test suite()
   {
      return new TestSuite(DeployerManagedObjectUnitTestCase.class);
   }
   
   public DeployerManagedObjectUnitTestCase(String name)
   {
      super(name);
   }

   public void testManagedObject() throws Exception
   {
      DeployerClient main = getMainDeployer();
      
      // Deploy a context
      Deployment context = createSimpleDeployment("deploy");
      main.addDeployment(context);
      main.process();
      
      // Check the default settings
      assertNotNull(deployer.lastAttachment);
      assertEquals("initialString1", deployer.lastAttachment.getProperty("string1"));
      assertEquals("initialString2", deployer.lastAttachment.getProperty("string2"));
      
      // Get the managed object
      Map<String, ManagedObject> mos = main.getManagedObjects(context.getName());
      assertNotNull(mos);
      ManagedObject mo = mos.get(TestAttachment.class.getName());
      assertNotNull(mo);
      //
      Graph<Map<String, ManagedObject>> mosg = main.getDeepManagedObjects(context.getName());
      assertEquals("MO Graph", mosg.size(), 1);
      mos = mosg.getRootVertex().getData();
      assertNotNull(mos);
      mo = mos.get(TestAttachment.class.getName());
      assertNotNull(mo);

      // TODO the attachment should NOT be the top level managed object
      //      that should be describing the structure and deployment state
      //      with the attachments as sub managed objects
      
      // Check the managed object has the default settings
      assertEquals("initialString1", mo.getProperty("string1").getValue());
      assertEquals("initialString2", mo.getProperty("string2").getValue());
      
      // Change a value
      mo.getProperty("string1").setValue("changedString1");
      
      // Get the changed attachment
      TestAttachment attachment = (TestAttachment) mo.getAttachment();

      // Redeploy with our changed attachment
      MutableAttachments attachments = (MutableAttachments) context.getPredeterminedManagedObjects();
      attachments.addAttachment(TestAttachment.class, attachment);
      main.addDeployment(context);
      main.process();

      // Check the changed settings as seen by the deployer
      assertNotNull(deployer.lastAttachment);
      assertEquals("changedString1", deployer.lastAttachment.getProperty("string1"));
      assertEquals("initialString2", deployer.lastAttachment.getProperty("string2"));
      
      // TODO shouldn't have to reget the managed object handles across redeploys?
      mos = main.getManagedObjects(context.getName());
      assertNotNull(mos);
      mo = mos.get(TestAttachment.class.getName());
      assertNotNull(mo);

      // Check the changed settings as described by the managed object
      assertEquals("changedString1", mo.getProperty("string1").getValue());
      assertEquals("initialString2", mo.getProperty("string2").getValue());
   }
   
   protected DeployerClient getMainDeployer()
   {
      return createMainDeployer(deployer);
   }
}
