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
package org.jboss.test.deployers.structure.main.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.deployment.MainDeployerImpl;
import org.jboss.deployers.plugins.structure.vfs.file.FileStructure;
import org.jboss.deployers.plugins.structure.vfs.jar.JARStructure;
import org.jboss.deployers.plugins.structure.vfs.war.WARStructure;
import org.jboss.deployers.spi.deployment.MainDeployer;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.DeploymentState;
import org.jboss.test.deployers.structure.war.test.WARStructureUnitTestCase;

/**
 * MainDeployerStructureUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MainDeployerWarStructureUnitTestCase extends WARStructureUnitTestCase
{
   public static Test suite()
   {
      return new TestSuite(MainDeployerWarStructureUnitTestCase.class);
   }
   
   public MainDeployerWarStructureUnitTestCase(String name)
   {
      super(name);
   }

   protected DeploymentContext assertValidContext(String root, String path) throws Exception
   {
      DeploymentContext context = createDeploymentContext(root, path);
      getMainDeployer().addDeploymentContext(context);
      assertFalse("Structure should be valid: " + context.getName(), context.getState() == DeploymentState.ERROR);
      assertEmpty(context.getChildren());
      return context;
   }

   protected DeploymentContext assertNotValidContext(String root, String path,
         boolean addTopLevelInfo, boolean isValidJar) throws Exception
   {
      // It might not be a valid war but it is a valid jar
      if (isValidJar)
         return assertValidContext(root, path, addTopLevelInfo);

      DeploymentContext context = createDeploymentContext(root, path);
      getMainDeployer().addDeploymentContext(context);
      assertTrue("Structure should not be valid: " + context.getName(), context.getState() == DeploymentState.ERROR);
      assertEmpty(context.getChildren());
      return context;
   }

   protected static MainDeployer getMainDeployer()
   {
      MainDeployerImpl main = new MainDeployerImpl();
      main.addStructureDeployer(new FileStructure());
      main.addStructureDeployer(new JARStructure());
      main.addStructureDeployer(new WARStructure());
      return main;
   }
}
