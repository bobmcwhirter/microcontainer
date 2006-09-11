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
package org.jboss.test.deployers.deployer.test;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import junit.framework.AssertionFailedError;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.deployment.MainDeployerImpl;
import org.jboss.deployers.plugins.structure.AbstractDeploymentContext;
import org.jboss.deployers.plugins.structure.vfs.jar.JARStructure;
import org.jboss.deployers.spi.deployer.Deployer;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.deployers.spi.structure.StructureDetermined;
import org.jboss.test.deployers.BaseDeployersTest;
import org.jboss.test.deployers.deployer.support.TestSimpleDeployer;

/**
 * MainDeployerDeployerUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MainDeployerDeployerUnitTestCase extends BaseDeployersTest
{
   public static Test suite()
   {
      return new TestSuite(MainDeployerDeployerUnitTestCase.class);
   }
   
   public MainDeployerDeployerUnitTestCase(String name)
   {
      super(name);
   }

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      enableTrace("org.jboss.deployers");
   }
   
   public void testAddNullDeployer() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      try
      {
         main.addDeployer(null);
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
   
   public void testAddDeployers() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      assertEmpty(main.getDeployers());
      
      Deployer deployer = new TestSimpleDeployer();
      HashSet<Deployer> expected = new HashSet<Deployer>();
      expected.add(deployer);

      main.addDeployer(deployer);
      assertEquals(expected, main.getDeployers());
      
      deployer = new TestSimpleDeployer();
      expected.add(deployer);

      main.addDeployer(deployer);
      assertEquals(expected, main.getDeployers());

      // Duplicate
      main.addDeployer(deployer);
      assertEquals(expected, main.getDeployers());
   }
   
   public void testRemoveNullDeployer() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      try
      {
         main.removeDeployer(null);
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
   
   public void testRemoveDeployers() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      assertEmpty(main.getDeployers());
      
      Deployer deployer1 = new TestSimpleDeployer();
      Deployer deployer2 = new TestSimpleDeployer();
      HashSet<Deployer> expected = new HashSet<Deployer>();
      expected.add(deployer1);
      expected.add(deployer2);
      main.addDeployer(deployer1);
      main.addDeployer(deployer2);
      assertEquals(expected, main.getDeployers());
      
      Deployer notPresent = new TestSimpleDeployer();
      main.removeDeployer(notPresent);
      assertEquals(expected, main.getDeployers());

      main.removeDeployer(deployer1);
      expected.remove(deployer1);
      assertEquals(expected, main.getDeployers());

      main.removeDeployer(deployer2);
      expected.remove(deployer2);
      assertEquals(expected, main.getDeployers());
   }
   
   public void testSetNullDeployers() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      try
      {
         main.setDeployers(null);
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
   
   public void testSetDeployers() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      assertEmpty(main.getDeployers());
      
      Deployer deployer1 = new TestSimpleDeployer();
      Deployer deployer2 = new TestSimpleDeployer();
      Set<Deployer> expected = new HashSet<Deployer>();
      expected.add(deployer1);
      expected.add(deployer2);
      main.setDeployers(expected);
      assertEquals(expected, main.getDeployers());
      
      expected = new HashSet<Deployer>();
      expected.add(deployer1);
      main.setDeployers(expected);
      assertEquals(expected, main.getDeployers());
      
      expected = new HashSet<Deployer>();
      expected.add(deployer1);
      main.setDeployers(expected);
      assertEquals(expected, main.getDeployers());
      
      expected = Collections.emptySet();
      main.setDeployers(expected);
      assertEquals(expected, main.getDeployers());
   }
   
   public void testNoDeployers() throws Exception
   {
      MainDeployerImpl main = new MainDeployerImpl();
      main.addStructureDeployer(new JARStructure());

      DeploymentContext context = new AbstractDeploymentContext("predetermined");
      context.setStructureDetermined(StructureDetermined.PREDETERMINED);
      main.addDeploymentContext(context);
      
      try
      {
         main.process();
         fail("Should not be here!");
      }
      catch (AssertionFailedError e)
      {
         throw e;
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalStateException.class, t);
      }
   }
}
