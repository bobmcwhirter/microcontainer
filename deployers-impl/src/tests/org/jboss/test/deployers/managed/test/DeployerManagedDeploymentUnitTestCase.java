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
package org.jboss.test.deployers.managed.test;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.plugins.attachments.AttachmentsImpl;
import org.jboss.deployers.spi.attachments.MutableAttachments;
import org.jboss.deployers.spi.attachments.PredeterminedManagedObjectAttachments;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.api.factory.ManagedObjectFactory;
import org.jboss.reflect.plugins.ClassInfoImpl;
import org.jboss.reflect.plugins.introspection.IntrospectionTypeInfoFactoryImpl;
import org.jboss.reflect.spi.AnnotationValue;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.test.deployers.AbstractDeployerTest;
import org.jboss.test.deployers.deployer.support.ConnMetaData;
import org.jboss.test.deployers.deployer.support.DSMetaData;
import org.jboss.test.deployers.deployer.support.LocalDataSourceMetaData;
import org.jboss.test.deployers.deployer.support.MCFDeployer;
import org.jboss.test.deployers.deployer.support.SecMetaData;
import org.jboss.test.deployers.deployer.support.SecurityDeployment;
import org.jboss.test.deployers.deployer.support.XADataSourceMetaData;
import org.jboss.test.deployers.managed.support.MockProfileService;
import org.jboss.test.deployers.managed.support.TestManagedObjectDeployer;

/**
 * ManagedDeployment unit tests.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class DeployerManagedDeploymentUnitTestCase extends AbstractDeployerTest
{
   private MCFDeployer deployer = new MCFDeployer();
   
   public static Test suite()
   {
      return new TestSuite(DeployerManagedObjectUnitTestCase.class);
   }

   public DeployerManagedDeploymentUnitTestCase(String name)
   {
      super(name);
   }

   public void testAnnotationScan()
      throws Exception
   {
      ManagedObjectFactory mof = ManagedObjectFactory.getInstance();
      ManagedObject mo = mof.createManagedObject(DSMetaData.class);

      Set<ManagedProperty> props = mo.getProperties();
      assertEquals(2, props.size());
      
   }

   public void testManagedDeployment()
      throws Exception
   {
      MockProfileService ps = new MockProfileService();
      DeployerClient main = getMainDeployer();
      
      // Deploy a datasource
      Deployment ctx1 = createSimpleDeployment("deployment1");
      DSMetaData dsmd = new DSMetaData();
      LocalDataSourceMetaData ds = new LocalDataSourceMetaData();
      ds.setJndiName("java:DefaultDS1");
      ds.setMaxSize(100);
      ds.setMinSize(10);
      ds.setPassword("password1".toCharArray());
      ds.setUsername("username1");
      SecMetaData smd = new SecMetaData();
      smd.setDomain("java:/jaas/domain1");
      ds.setSecurityMetaData(smd);

      XADataSourceMetaData xads = new XADataSourceMetaData();
      xads.setJndiName("java:DefaultXADS2");
      ds.setMaxSize(100);
      ds.setMinSize(10);
      ds.setPassword("password2".toCharArray());
      ds.setUsername("username2");
      xads.setXaDataSourceClass("org.jboss.xa.SomeXADS");
      xads.setXaResourceTimeout(300);
      xads.setSecurityMetaData(smd);

      ArrayList<ConnMetaData> deployments = new ArrayList<ConnMetaData>();
      deployments.add(ds);
      deployments.add(xads);
      dsmd.setDeployments(deployments);

      MutableAttachments a1 = (MutableAttachments) ctx1.getPredeterminedManagedObjects();
      a1.addAttachment(DSMetaData.class, dsmd);
      main.addDeployment(ctx1);

      // Deploy a security domain
      Deployment ctx2 = createSimpleDeployment("deployment2");
      MutableAttachments a2 = (MutableAttachments) ctx2.getPredeterminedManagedObjects();
      SecurityDeployment sd = new SecurityDeployment();
      a2.addAttachment(SecurityDeployment.class, sd);
      main.addDeployment(ctx2);
      main.process();

      // 
      Map<String, ManagedObject> mds1 = main.getManagedObjects("deployment1");
      log.info("deployment1 ManagedObjects: " + mds1);
      for(ManagedObject mo : mds1.values())
         ps.processManagedObject(mo);
      Map<String, List<ManagedProperty>> unresolvedRefs = ps.getUnresolvedRefs();
      log.info("unresolvedRefs: "+unresolvedRefs);

      Map<String, ManagedObject> mds2 = main.getManagedObjects("deployment2");
      log.info("deployment2 ManagedObjects: " +mds2);
      for(ManagedObject mo : mds2.values())
         ps.processManagedObject(mo);

      // 
   }
   
   protected DeployerClient getMainDeployer()
   {
      return createMainDeployer(deployer);
   }
}
