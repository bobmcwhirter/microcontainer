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
package org.jboss.test.deployers;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.deployers.plugins.structure.AbstractDeploymentContext;
import org.jboss.deployers.plugins.structure.BasicStructuredDeployers;
import org.jboss.deployers.plugins.structure.ContextInfoImpl;
import org.jboss.deployers.plugins.structure.DefaultStructureBuilderFactory;
import org.jboss.deployers.plugins.structure.StructureMetaDataImpl;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.StructureDetermined;
import org.jboss.deployers.spi.structure.vfs.StructureBuilder;
import org.jboss.deployers.spi.structure.vfs.StructureBuilderFactory;
import org.jboss.deployers.spi.structure.vfs.StructureDeployer;
import org.jboss.deployers.spi.structure.vfs.StructureMetaData;
import org.jboss.deployers.spi.structure.vfs.StructuredDeployers;
import org.jboss.test.BaseTestCase;
import org.jboss.util.NotImplementedException;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.plugins.context.jar.JarUtils;

/**
 * BaseDeployersTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class BaseDeployersTest extends BaseTestCase
{
   public BaseDeployersTest(String name)
   {
      super(name);
   }
   
   @Override
   public URL getResource(String path)
   {
      URL url = super.getResource(path);
      assertNotNull("Resource not found: " + path, url);
      return url;
   }
   
   /**
    * Get a virtual file
    * 
    * @param root the root
    * @param path the path
    * @return the file
    * @throws Exception for any error
    */
   protected VirtualFile getVirtualFile(String root, String path) throws Exception
   {
      URL url = getResource(root);
      return VFS.getVirtualFile(url, path);
   }
   
   /**
    * Get a url string from a path
    * 
    * @param path the path
    * @return the string
    * @throws Exception for any error
    */
   protected String getURL(String path) throws Exception
   {
      URL url = getResource(path);
      return url.toString();
   }
   
   /**
    * Get a jar url string from a path
    * 
    * @param path the path
    * @return the string
    * @throws Exception for any error
    */
   protected String getJarURL(String path) throws Exception
   {
      URL url = getResource(path);
      url = JarUtils.createJarURL(url);
      return url.toString();
   }

   /**
    * Get the structure deployer for this test
    * 
    * @return the deployer
    */
   protected StructureDeployer getStrucutureDeployer()
   {
      throw new NotImplementedException("Implemented in subclasses");
   }
   protected StructuredDeployers getStrucuturedDeployers()
   {
      BasicStructuredDeployers deployers = new BasicStructuredDeployers();
      deployers.addDeployer(getStrucutureDeployer());
      return deployers;
   }
   /**
    * Get the StructureVisitorFactory that translates the StructureMetaData into a DeploymentContext tree.
    * @return
    */
   protected StructureBuilderFactory getStructureBuilderFactory()
   {
      return new DefaultStructureBuilderFactory();     
   }

   /**
    * Determine the structure
    * 
    * @param context the context
    * @return the result
    */
   protected boolean determineStructure(DeploymentContext context, boolean addTopLevelInfo)
      throws Exception
   {
      StructureDeployer deployer = getStrucutureDeployer();
      StructuredDeployers deployers = getStrucuturedDeployers();
      return determineStructure(deployer, deployers, context, addTopLevelInfo);
   }

   /**
    * Determine the structure
    * 
    * @param structure the structural deployer
    * @param context the context
    * @return the result
    */
   protected boolean determineStructure(StructureDeployer deployer,
         DeploymentContext context)
      throws Exception
   {
      return determineStructure(deployer, getStrucuturedDeployers(), context, false);
   }
   /**
    * Determine the structure
    * 
    * @param structure the structural deployer
    * @param context the context
    * @return the result
    */
   protected boolean determineStructure(StructureDeployer deployer,
         DeploymentContext context, boolean addTopLevelInfo)
      throws Exception
   {
      return determineStructure(deployer, getStrucuturedDeployers(), context, addTopLevelInfo);
   }
   /**
    * 
    * @param deployer
    * @param deployers
    * @param context
    * @param addTopLevelInfo - should a ContextInfo be created for the
    *    context root
    * @return
    * @throws Exception
    */
   protected boolean determineStructure(StructureDeployer deployer,
         StructuredDeployers deployers, DeploymentContext context,
         boolean addTopLevelInfo)
      throws Exception
   {
      assertNotNull(deployer);
      assertNotNull(context);
      
      log.debug("Determining structure: " + context.getName());
      StructureMetaData metaData = new StructureMetaDataImpl();
      VirtualFile root = context.getRoot();
      String rootPath = root.getPathName();
      if( addTopLevelInfo && metaData.getContext(rootPath) == null )
      {
         /* For backward compatibility with the top-level context notion, we need
          to add an entry for the  
         */
         ContextInfoImpl rootContextInfo = new ContextInfoImpl(rootPath);
         metaData.addContext(rootContextInfo);
      }
      boolean result = deployer.determineStructure(root, metaData, deployers);
      if( result )
      {
         StructureBuilderFactory factory = getStructureBuilderFactory();
         StructureBuilder builder = factory.createBuilder(metaData);
         builder.populateContext(context, metaData);
      }
      return result;
   }

   /**
    * Assert non of the candidates are valid
    * 
    * @param context the context
    * @throws Exception for any error
    */
   protected void assertCandidatesNotValid(DeploymentContext context) throws Exception
   {
      assertCandidatesNotValid(getStrucutureDeployer(), context);
   }
   
   /**
    * Assert non of the candidates are valid
    * 
    * @param structure the structure deployer
    * @param context the context
    * @throws Exception for any error
    */
   protected void assertCandidatesNotValid(StructureDeployer structure, DeploymentContext context) throws Exception
   {
      assertNotNull(context);
      for (DeploymentContext child : context.getChildren())
      {
         boolean recognized = determineStructure(structure, child);
         assertFalse("Should not be a valid candidate: " + child.getName(), recognized);
      }
   }
   
   /**
    * Assert the candidates are valid
    * 
    * @param context the context
    * @throws Exception for any error
    */
   protected void assertCandidatesValid(DeploymentContext context) throws Exception
   {
      assertCandidatesValid(getStrucutureDeployer(), context);
   }
   
   /**
    * Assert the candidates are valid
    * 
    * @param structure the structure deployer
    * @param context the context
    * @throws Exception for any error
    */
   protected void assertCandidatesValid(StructureDeployer structure, DeploymentContext context) throws Exception
   {
      assertNotNull(structure);
      assertNotNull(context);

      for (DeploymentContext child : context.getChildren())
         assertTrue("Should be a valid candidate: " + child.getName(), determineStructure(structure, child));
   }

   /**
    * Assert the contexts match the expected urls
    * 
    * @param expected the expected
    * @param actual the actual
    * @throws Exception for any error
    */
   protected void assertCandidateContexts(Map<String, Boolean> expected, Set<DeploymentContext> actual) throws Exception
   {
      assertNotNull(expected);
      assertNotNull(actual);
      Set<String> contexts = new HashSet<String>(actual.size());
      for (DeploymentContext context : actual)
         contexts.add(context.getName());
      assertEquals(expected.keySet(), contexts);
   }

   /**
    * Assert the contexts match the expected urls
    * 
    * @param expected the expected
    * @param actual the actual
    * @throws Exception for any error
    */
   protected void assertActualContexts(Map<String, Boolean> expected, Set<DeploymentContext> actual) throws Exception
   {
      assertNotNull(expected);
      assertNotNull(actual);
      Set<String> contexts = new HashSet<String>(actual.size());
      for (DeploymentContext context : actual)
         contexts.add(context.getName());
      Set<String> expectedActualContexts = new HashSet<String> (expected.size());
      for (Map.Entry<String, Boolean> entry : expected.entrySet())
      {
         if (entry.getValue() == true)
            expectedActualContexts.add(entry.getKey());
      }
      assertEquals(expectedActualContexts, contexts);
   }
   
   /**
    * Create a deployment context
    * 
    * @param root the root
    * @param path the path
    * @return the context
    * @throws Exception for any error
    */
   protected DeploymentContext createDeploymentContext(String root, String path) throws Exception
   {
      VirtualFile file = getVirtualFile(root, path);
      return new AbstractDeploymentContext(file);
   }
   
   /**
    * Create a simple predetermined deployment context
    * 
    * @param name the context name
    * @return the context
    */
   protected DeploymentContext createSimpleDeployment(String name)
   {
      AbstractDeploymentContext context = new AbstractDeploymentContext(name);
      context.setStructureDetermined(StructureDetermined.PREDETERMINED);
      return context;
   }


   /**
    * Translate a set of DeploymentContexts string paths relative to the root
    * URL.
    * 
    * @param set
    * @return set of 
    * @throws URISyntaxException 
    * @throws IOException 
    */
   protected Set<String> createDeploymentPathSet(Set<DeploymentContext> set, URL root)
      throws IOException, URISyntaxException
   {
      HashSet<String> paths = new HashSet<String>();
      String rootPath = root.getPath();
      for(DeploymentContext ctx : set)
      {
         URL ctxURL = ctx.getRoot().toURL();
         String ctxPath = ctxURL.getPath().substring(rootPath.length());
         paths.add(ctxPath);
      }
      return paths;
   }
}
