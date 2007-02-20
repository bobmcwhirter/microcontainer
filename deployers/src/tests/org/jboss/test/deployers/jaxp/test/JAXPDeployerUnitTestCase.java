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
package org.jboss.test.deployers.jaxp.test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.deployment.MainDeployerImpl;
import org.jboss.deployers.plugins.structure.vfs.file.FileStructure;
import org.jboss.deployers.plugins.structure.vfs.jar.JARStructure;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.DeploymentState;
import org.jboss.test.deployers.BaseDeployersTest;
import org.jboss.test.deployers.jaxp.support.SomeBean;
import org.jboss.test.deployers.jaxp.support.TestXmlDeployer;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Tests of the JAXPDeployer.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class JAXPDeployerUnitTestCase extends BaseDeployersTest
{
   public static Test suite()
   {
      return new TestSuite(JAXPDeployerUnitTestCase.class);
   }

   private MainDeployerImpl main;
   private TestXmlDeployer deployer;

   public JAXPDeployerUnitTestCase(String name) throws Throwable
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      super.setUp();
      try
      {
         main = new MainDeployerImpl();
         main.addStructureDeployer(new JARStructure());
         main.addStructureDeployer(new FileStructure());
         
         deployer = new TestXmlDeployer();
         deployer.create();
         main.addDeployer(deployer);
      }
      catch (Throwable t)
      {
         throw new RuntimeException(t);
      }
   }

   public void testDocOverride() throws Exception
   {
      DeploymentContext context = createDeploymentContext("/jaxp", "somebean.jbean");
      assertDeploy(context, null, null);
      SomeBean bean = deployer.getLastBean();
      assertNotNull(bean);
      assertEquals("bean.name", bean.getName(), "bean1");
      assertEquals("bean.version", bean.getVersion(), "1.0");

      // Now deploy with an overriden Document
      DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
      DocumentBuilder builder = factory.newDocumentBuilder();
      DOMImplementation dom = builder.getDOMImplementation();
      Document doc = dom.createDocument(null, "somebean", null);
      Element root = doc.getDocumentElement();
      root.setAttribute("name", "bean1.1");
      root.setAttribute("version", "1.1");
      assertDeploy(context, Document.class, doc);

      bean = deployer.getLastBean();
      assertNotNull(bean);
      assertEquals("bean.name", bean.getName(), "bean1.1");
      assertEquals("bean.version", bean.getVersion(), "1.1");

      assertUndeploy(context);
   }

   protected <T> void assertDeploy(DeploymentContext context, Class<T> clazz, T mo)
      throws Exception
   {
      main.addDeploymentContext(context);
      if( mo != null )
         context.getTransientManagedObjects().addAttachment(clazz, mo);
      main.process();
      assertEquals("DeploymentState", DeploymentState.DEPLOYED, context.getState());
   }

   protected void assertUndeploy(DeploymentContext context)
      throws Exception
   {
      main.removeDeploymentContext(context.getName());
      main.process();
      assertEquals("DeploymentState", DeploymentState.UNDEPLOYED, context.getState());
   }
}
