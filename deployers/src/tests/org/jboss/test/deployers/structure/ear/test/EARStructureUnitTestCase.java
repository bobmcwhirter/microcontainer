/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free softeare; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Softeare Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This softeare is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied earranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this softeare; if not, write to the Free
* Softeare Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.test.deployers.structure.ear.test;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.structure.BasicStructuredDeployers;
import org.jboss.deployers.plugins.structure.vfs.file.FileStructure;
import org.jboss.deployers.plugins.structure.vfs.jar.JARStructure;
import org.jboss.deployers.plugins.structure.vfs.war.WARStructure;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.vfs.StructureDeployer;
import org.jboss.deployers.spi.structure.vfs.StructuredDeployers;
import org.jboss.test.deployers.BaseDeployersTest;
import org.jboss.test.deployers.structure.ear.support.MockEarStructureDeployer;
import org.jboss.virtual.VirtualFile;

/**
 * Mock ear structure deployer tests
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public class EARStructureUnitTestCase extends BaseDeployersTest
{
   /** The ear structure deployer */
   private static final MockEarStructureDeployer structure = new MockEarStructureDeployer();
   
   public static Test suite()
   {
      return new TestSuite(EARStructureUnitTestCase.class);
   }
   
   public EARStructureUnitTestCase(String name)
   {
      super(name);
   }

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      enableTrace("org.jboss.deployers");
   }

   @Override
   protected StructureDeployer getStrucutureDeployer()
   {
      return structure;
   }

   /**
    * We need more than just the ear deployer since ears are containers
    * for other deployments so we add them here.
    */
   @Override
   protected StructuredDeployers getStrucuturedDeployers()
   {
      BasicStructuredDeployers deployers = new BasicStructuredDeployers();
      FileStructure fs = new FileStructure();
      WARStructure ws = new WARStructure();
      JARStructure js = new JARStructure();
      HashSet<String> suffixes = new HashSet<String>(js.getSuffixes());
      suffixes.add(".ejb3");
      js.setSuffixes(suffixes);
      deployers.addDeployer(getStrucutureDeployer());
      deployers.addDeployer(fs);
      deployers.addDeployer(ws);
      deployers.addDeployer(js);
      return deployers;
   }

   protected DeploymentContext assertValidContext(String root, String path)
      throws Exception
   {
      DeploymentContext context = createDeploymentContext(root, path);
      boolean recognized = determineStructure(context, false);
      assertTrue("Structure should be valid: " + context.getName(), recognized);
      return context;
   }

   protected DeploymentContext assertNotValidContext(String root, String path)
      throws Exception
   {
      DeploymentContext context = createDeploymentContext(root, path);
      boolean recognized = determineStructure(context, false);
      assertFalse("Structure should not be valid: " + context.getName(), recognized);
      return context;
   }
   
   public void testNotAnEAR() throws Exception
   {
      assertNotValidContext("/structure/", "ear/notanear");
   }

   /**
    * Validate a basic ear with modules having no subdeployments 
    * @throws Exception
    */
   public void testSimpleWithAppXml() throws Exception
   {      
      DeploymentContext ear = assertValidContext("/structure/", "ear/simplewithappxml.ear");
      List<VirtualFile> classpath = ear.getClassPath();
      assertNotNull("classpath", classpath);
      assertEquals("classpath.size = 1", 1, classpath.size());
      VirtualFile earFile = ear.getRoot();
      VirtualFile j0 = earFile.findChild("lib/lib0.jar");
      assertTrue("lib/j0.jar in classpath", classpath.contains(j0));
      // Validate that the expected module contexts exist
      HashSet<String> expected = new HashSet<String>();
      URL rootURL = ear.getRoot().toURL();
      expected.add("module-service.xml");
      expected.add("module-bean1ejb.jar/");
      expected.add("module-bean2.ejb3/");
      expected.add("module-client1.jar/");
      expected.add("module-mbean1.sar/");
      expected.add("module-mcf1-ds.xml");
      expected.add("module-mcf1.rar/");
      expected.add("module-web1.war/");
      Set<DeploymentContext> children = ear.getChildren();
      Set<String> childPaths = super.createDeploymentPathSet(children, rootURL);
      assertEquals("ear child count", expected, childPaths);
   }

   /**
    * Validate a ear with modules having subdeployments 
    * @throws Exception
    */
   public void testComplexWithAppXml() throws Exception
   {      
      DeploymentContext ear = assertValidContext("/structure/", "ear/complexwithappxml.ear");
      List<VirtualFile> classpath = ear.getClassPath();
      assertNotNull("classpath", classpath);
      assertEquals("classpath.size = 1", 1, classpath.size());
      VirtualFile earFile = ear.getRoot();
      VirtualFile j0 = earFile.findChild("lib/lib0.jar");
      assertTrue("lib/j0.jar in classpath", classpath.contains(j0));
      // Validate that the expected module contexts exist
      HashSet<String> expected = new HashSet<String>();
      URL rootURL = ear.getRoot().toURL();
      expected.add("module-service.xml");
      expected.add("module-bean1ejb.jar/");
      expected.add("module-bean2.ejb3/");
      expected.add("module-client1.jar/");
      expected.add("module-mbean1.sar/");
      expected.add("module-mcf1-ds.xml");
      expected.add("module-mcf1.rar/");
      expected.add("module-web1.war/");
      Set<DeploymentContext> children = ear.getChildren();
      Set<String> childPaths = super.createDeploymentPathSet(children, rootURL);
      assertEquals("ear child count", expected, childPaths);

      // Validate that the expected module subdeployments are there
      Map<String, DeploymentContext> pathMap = createDeploymentPathMap(ear);
      DeploymentContext extensionsAop = pathMap.get("complexwithappxml.ear/module-mbean1.sar/extensions.aop");
      assertNotNull("complexwithappxml.ear/module-mbean1.sar/extensions.aop/", extensionsAop);
      DeploymentContext submbean1 = pathMap.get("complexwithappxml.ear/module-mbean1.sar/submbean.sar");
      assertNotNull("complexwithappxml.ear/submbean.sar", submbean1);
      DeploymentContext submbean2 = pathMap.get("complexwithappxml.ear/module-mbean1.sar/submbean2-service.xml");
      assertNotNull("complexwithappxml.ear/submbean2-service.xml", submbean2);
   }
   
}
