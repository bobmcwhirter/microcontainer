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

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.deployment.MainDeployerImpl;
import org.jboss.deployers.plugins.structure.vfs.jar.JARStructure;
import org.jboss.deployers.plugins.structure.vfs.war.WARStructure;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.DeploymentState;
import org.jboss.deployers.spi.structure.vfs.StructureDeployer;
import org.jboss.test.deployers.BaseDeployersTest;

/**
 * MainDeployerStructureUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MainDeployerStructureUnitTestCase extends BaseDeployersTest
{
   public static Test suite()
   {
      return new TestSuite(MainDeployerStructureUnitTestCase.class);
   }
   
   public MainDeployerStructureUnitTestCase(String name)
   {
      super(name);
   }

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      enableTrace("org.jboss.deployers");
   }
   
   public void testAddNullStructuralDeployer() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      try
      {
         main.addStructureDeployer(null);
         fail("Should not be here!");
      }
      catch (AssertionFailedError e)
      {
         throw e;
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   public void testAddStructuralDeployers() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      assertEmpty(main.getStructureDeployers());
      
      StructureDeployer deployer = new JARStructure();
      HashSet<StructureDeployer> expected = new HashSet<StructureDeployer>();
      expected.add(deployer);

      main.addStructureDeployer(deployer);
      assertEquals(expected, main.getStructureDeployers());
      
      deployer = new WARStructure();
      expected.add(deployer);

      main.addStructureDeployer(deployer);
      assertEquals(expected, main.getStructureDeployers());

      // Duplicate
      main.addStructureDeployer(deployer);
      assertEquals(expected, main.getStructureDeployers());
   }
   
   public void testRemoveNullStructuralDeployer() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      try
      {
         main.removeStructureDeployer(null);
         fail("Should not be here!");
      }
      catch (AssertionFailedError e)
      {
         throw e;
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   public void testRemoveStructuralDeployers() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      assertEmpty(main.getStructureDeployers());
      
      StructureDeployer jarDeployer = new JARStructure();
      StructureDeployer warDeployer = new WARStructure();
      HashSet<StructureDeployer> expected = new HashSet<StructureDeployer>();
      expected.add(jarDeployer);
      expected.add(warDeployer);
      main.addStructureDeployer(jarDeployer);
      main.addStructureDeployer(warDeployer);
      assertEquals(expected, main.getStructureDeployers());
      
      StructureDeployer notPresent = new JARStructure();
      main.removeStructureDeployer(notPresent);
      assertEquals(expected, main.getStructureDeployers());

      main.removeStructureDeployer(jarDeployer);
      expected.remove(jarDeployer);
      assertEquals(expected, main.getStructureDeployers());

      main.removeStructureDeployer(warDeployer);
      expected.remove(warDeployer);
      assertEquals(expected, main.getStructureDeployers());
   }
   
   public void testSetNullStructuralDeployers() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      try
      {
         main.setStructureDeployers(null);
         fail("Should not be here!");
      }
      catch (AssertionFailedError e)
      {
         throw e;
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   public void testSetStructuralDeployers() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      assertEmpty(main.getStructureDeployers());
      
      StructureDeployer jarDeployer = new JARStructure();
      StructureDeployer warDeployer = new WARStructure();
      Set<StructureDeployer> expected = new HashSet<StructureDeployer>();
      expected.add(jarDeployer);
      expected.add(warDeployer);
      main.setStructureDeployers(expected);
      assertEquals(expected, main.getStructureDeployers());
      
      expected = new HashSet<StructureDeployer>();
      expected.add(jarDeployer);
      main.setStructureDeployers(expected);
      assertEquals(expected, main.getStructureDeployers());
      
      expected = new HashSet<StructureDeployer>();
      expected.add(warDeployer);
      main.setStructureDeployers(expected);
      assertEquals(expected, main.getStructureDeployers());
      
      expected = Collections.emptySet();
      main.setStructureDeployers(expected);
      assertEquals(expected, main.getStructureDeployers());
   }
   
   public void testNoStructuralDeployers() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      DeploymentContext context = createDeploymentContext("/structure/", "jar/simple");
      main.addDeploymentContext(context);
      assertEquals(DeploymentState.ERROR, context.getState());
      checkThrowable(IllegalStateException.class, context.getProblem());
   }
}
