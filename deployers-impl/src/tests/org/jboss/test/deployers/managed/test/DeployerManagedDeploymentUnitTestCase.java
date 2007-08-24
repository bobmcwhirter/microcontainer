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
import org.jboss.managed.api.ManagedDeployment;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.api.factory.ManagedObjectFactory;
import org.jboss.metatype.api.values.ArrayValue;
import org.jboss.metatype.api.values.GenericValue;
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

      Map<String, ManagedProperty> props = mo.getProperties();
      assertEquals(2, props.size());
      
   }

   public void testManagedDeployment()
      throws Exception
   {
      DeployerClient main = getMainDeployer();
      MockProfileService ps = new MockProfileService(main);
      
      // Deploy a datasource with local and xa factories
      Deployment ctx1 = createSimpleDeployment("deployment1");
      DSMetaData dsmd = new DSMetaData();
      LocalDataSourceMetaData ds = new LocalDataSourceMetaData();
      ds.setJndiName("java:DefaultDS1");
      ds.setMaxSize(100);
      ds.setMinSize(10);
      ds.setPassword("password1".toCharArray());
      ds.setUsername("username1");
      SecMetaData smd1 = new SecMetaData();
      smd1.setDomain("java:/jaas/domain1");
      ds.setSecurityMetaData(smd1);

      XADataSourceMetaData xads = new XADataSourceMetaData();
      xads.setJndiName("java:DefaultXADS2");
      ds.setMaxSize(100);
      ds.setMinSize(10);
      ds.setPassword("password2".toCharArray());
      ds.setUsername("username2");
      xads.setXaDataSourceClass("org.jboss.xa.SomeXADS");
      xads.setXaResourceTimeout(300);
      SecMetaData smd2 = new SecMetaData();
      smd2.setDomain("java:/jaas/domain2");
      xads.setSecurityMetaData(smd2);

      ArrayList<ConnMetaData> deployments = new ArrayList<ConnMetaData>();
      deployments.add(ds);
      deployments.add(xads);
      dsmd.setDeployments(deployments);

      MutableAttachments a1 = (MutableAttachments) ctx1.getPredeterminedManagedObjects();
      a1.addAttachment(DSMetaData.class, dsmd);
      ps.addDeployment(ctx1);

      // Deploy security domain1
      Deployment secCtx1 = createSimpleDeployment("sec-domain1");
      MutableAttachments sda1 = (MutableAttachments) secCtx1.getPredeterminedManagedObjects();
      SecurityDeployment sd1 = new SecurityDeployment();
      sd1.setDomainName("java:/jaas/domain1");
      sda1.addAttachment(SecurityDeployment.class, sd1);
      ps.addDeployment(secCtx1);
      // Deploy security domain2
      Deployment secCtx2 = createSimpleDeployment("sec-domain2");
      MutableAttachments sda2 = (MutableAttachments) secCtx2.getPredeterminedManagedObjects();
      SecurityDeployment sd2 = new SecurityDeployment();
      sd2.setDomainName("java:/jaas/domain2");
      sda2.addAttachment(SecurityDeployment.class, sd2);
      ps.addDeployment(secCtx2);

      // Process the deployments to build the ManagedObjects
      ps.process();

      // Validate the deployment1 ManagedObjects
      ManagedDeployment mo1 = ps.getManagedDeployment("deployment1");
      assertNotNull("deployment1 ManagedDeployment", mo1);
      ManagedProperty deploymentsProp = mo1.getProperty("deployments");
      assertNotNull("deployments prop", deploymentsProp);
      // Get the deployments property MOs
      Object deploymentsValue = deploymentsProp.getValue();
      assertTrue("deploymentsValue instanceof ArrayValue", deploymentsValue instanceof ArrayValue);
      ArrayValue deploymentsArray = (ArrayValue) deploymentsValue;
      ManagedObject localDataMO = null;
      ManagedObject xaDataMO = null;
      for(int n = 0; n < deploymentsArray.getLength(); n ++)
      {
         GenericValue gv = (GenericValue) deploymentsArray.getValue(n);
         ManagedObject propMO = (ManagedObject) gv.getValue();
         if (propMO.getAttachmentName().equals(LocalDataSourceMetaData.class.getName()))
            localDataMO = propMO;
         else if (propMO.getAttachmentName().equals(XADataSourceMetaData.class.getName()))
            xaDataMO = propMO;
      }
      // Get the LocalDataSourceMetaData/SecMetaData/domain ManagedProperty
      assertNotNull("LocalDataSourceMetaData MO", localDataMO);
      log.debug("LocalDataSourceMetaData MO.props: "+localDataMO.getProperties());
      ManagedProperty localSecDomainProp = localDataMO.getProperty("security-domain");
      assertNotNull("localSecDomainProp", localSecDomainProp);
      GenericValue localSecDomainPropGV = (GenericValue) localSecDomainProp.getValue();
      ManagedObject localSecDomainPropMO = (ManagedObject) localSecDomainPropGV.getValue();
      ManagedProperty localSecDomainRefProp = localSecDomainPropMO.getProperty("domain-name");
      assertNotNull("localSecDomainRefProp", localSecDomainRefProp);
      // Get the XADataSourceMetaData/SecMetaData/domain ManagedProperty
      log.debug("XADataSourceMetaData MO: "+xaDataMO);
      assertNotNull("XADataSourceMetaData MO.props", xaDataMO.getProperties());
      ManagedProperty xaSecDomainProp = localDataMO.getProperty("security-domain");
      assertNotNull("xaSecDomainProp", xaSecDomainProp);
      GenericValue xaSecDomainPropGV = (GenericValue) xaSecDomainProp.getValue();
      ManagedObject xaSecDomainPropMO = (ManagedObject) xaSecDomainPropGV.getValue();
      ManagedProperty xaSecDomainRefProp = xaSecDomainPropMO.getProperty("domain-name");
      assertNotNull("xaSecDomainRefProp", xaSecDomainRefProp);

      // Should be 0 unresolved ManagementObjectRef for the sec-domain1/sec-domain2
      Map<String, Set<ManagedProperty>> unresolvedRefs = ps.getUnresolvedRefs();
      log.info("unresolvedRefs: "+unresolvedRefs);
      assertEquals("Should be 0 ManagementObjectRef", 0, unresolvedRefs.size());

      Map<String, ManagedObject> sd1MDs = main.getManagedObjects("sec-domain1");
      Map<String, ManagedObject> sd2MDs = main.getManagedObjects("sec-domain2");

      // Validate that the sec-domain1 ManagedObject is the target of the localSecDomainRefProp
      ManagedObject sd1MO = sd1MDs.get("org.jboss.test.deployers.deployer.support.SecurityDeployment");
      assertNotNull("org.jboss.test.deployers.deployer.support.SecurityDeployment MO1", sd1MO);
      ManagedObject localSecDomainPropTarget = localSecDomainRefProp.getTargetManagedObject();
      assertEquals(sd1MO, localSecDomainPropTarget);

      // Validate that the sec-domain2 ManagedObject is the target of the xaSecDomainRefProp
      ManagedObject sd2MO = sd2MDs.get("org.jboss.test.deployers.deployer.support.SecurityDeployment");
      assertNotNull("org.jboss.test.deployers.deployer.support.SecurityDeployment MO2", sd2MO);
      ManagedObject xaSecDomainPropTarget = xaSecDomainRefProp.getTargetManagedObject();
      assertEquals(sd2MO, xaSecDomainPropTarget);
   }

   protected DeployerClient getMainDeployer()
   {
      return createMainDeployer(deployer);
   }
}
