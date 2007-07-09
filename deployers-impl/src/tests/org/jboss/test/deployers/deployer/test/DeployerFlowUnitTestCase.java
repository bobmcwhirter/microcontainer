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

import junit.framework.Test;
import junit.framework.TestSuite;
import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.client.spi.Deployment;
import org.jboss.test.deployers.AbstractDeployerTest;
import org.jboss.test.deployers.deployer.support.TestFlowDeployer;

/**
 * DeployerOrderingUnitTestCase.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DeployerFlowUnitTestCase extends AbstractDeployerTest
{
   public static Test suite()
   {
      return new TestSuite(DeployerFlowUnitTestCase.class);
   }

   public DeployerFlowUnitTestCase(String name)
   {
      super(name);
   }

   protected void setUp() throws Exception
   {
      super.setUp();
      TestFlowDeployer.reset();
   }

   public void testSimpleInputOutputCorrectOrder() throws Exception
   {
      DeployerClient main = createMainDeployer();
      TestFlowDeployer deployer1 = new TestFlowDeployer("1");
      deployer1.setOutputs("test");
      addDeployer(main, deployer1);
      TestFlowDeployer deployer2 = new TestFlowDeployer("2");
      deployer2.setInputs("test");
      addDeployer(main, deployer2);

      Deployment deployment = createSimpleDeployment("correctOrder");
      main.addDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());

      main.removeDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(3, deployer2.getUndeployOrder());

      main.addDeployment(deployment);
      main.process();

      assertEquals(5, deployer1.getDeployOrder());
      assertEquals(6, deployer2.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(3, deployer2.getUndeployOrder());
   }

   public void testSimpleInputOutputWrongOrder() throws Exception
   {
      DeployerClient main = createMainDeployer();
      TestFlowDeployer deployer2 = new TestFlowDeployer("2");
      deployer2.setInputs("test");
      addDeployer(main, deployer2);
      TestFlowDeployer deployer1 = new TestFlowDeployer("1");
      deployer1.setOutputs("test");
      addDeployer(main, deployer1);

      Deployment deployment = createSimpleDeployment("wrongOrder");
      main.addDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());

      main.removeDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(3, deployer2.getUndeployOrder());

      main.addDeployment(deployment);
      main.process();

      assertEquals(5, deployer1.getDeployOrder());
      assertEquals(6, deployer2.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(3, deployer2.getUndeployOrder());
   }

   public void testInputOutputLoop() throws Exception
   {
      DeployerClient main = createMainDeployer();
      TestFlowDeployer deployer1 = new TestFlowDeployer("1");
      deployer1.setInputs("input1");
      deployer1.setOutputs("output1");
      addDeployer(main, deployer1);
      TestFlowDeployer deployer2 = new TestFlowDeployer("2");
      deployer2.setInputs("output1");
      deployer2.setOutputs("output2");
      addDeployer(main, deployer2);
      TestFlowDeployer deployer3 = new TestFlowDeployer("3");
      deployer3.setInputs("output2");
      deployer3.setOutputs("input1");
      try
      {
         addDeployer(main, deployer3);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalStateException.class, t);
      }
   }

   public void testInputOutputTransient() throws Exception
   {
      DeployerClient main = createMainDeployer();
      TestFlowDeployer deployer1 = new TestFlowDeployer("1");
      deployer1.setOutputs("test");
      addDeployer(main, deployer1);
      TestFlowDeployer deployer3 = new TestFlowDeployer("3");
      deployer3.setInputs("test");
      addDeployer(main, deployer3);
      TestFlowDeployer deployer2 = new TestFlowDeployer("2");
      deployer2.setInputs("test");
      deployer2.setOutputs("test");
      addDeployer(main, deployer2);

      Deployment deployment = createSimpleDeployment("transient");
      main.addDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());
      assertEquals(-1, deployer3.getUndeployOrder());

      main.removeDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(6, deployer1.getUndeployOrder());
      assertEquals(5, deployer2.getUndeployOrder());
      assertEquals(4, deployer3.getUndeployOrder());

      main.addDeployment(deployment);
      main.process();

      assertEquals(7, deployer1.getDeployOrder());
      assertEquals(8, deployer2.getDeployOrder());
      assertEquals(9, deployer3.getDeployOrder());
      assertEquals(6, deployer1.getUndeployOrder());
      assertEquals(5, deployer2.getUndeployOrder());
      assertEquals(4, deployer3.getUndeployOrder());
   }

   public void testInputOutputTransient2() throws Exception
   {
      DeployerClient main = createMainDeployer();
      TestFlowDeployer deployer1 = new TestFlowDeployer("1");
      deployer1.setInputs("test");
      deployer1.setOutputs("test");
      addDeployer(main, deployer1);
      TestFlowDeployer deployer2 = new TestFlowDeployer("2");
      deployer2.setInputs("test");
      addDeployer(main, deployer2);

      Deployment deployment = createSimpleDeployment("transient2");
      main.addDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());

      main.removeDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(3, deployer2.getUndeployOrder());

      main.addDeployment(deployment);
      main.process();

      assertEquals(5, deployer1.getDeployOrder());
      assertEquals(6, deployer2.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(3, deployer2.getUndeployOrder());
   }

   public void testInputOutputMultipleTransient() throws Exception
   {
      DeployerClient main = createMainDeployer();
      TestFlowDeployer deployer4 = new TestFlowDeployer("4");
      deployer4.setInputs("test");
      addDeployer(main, deployer4);
      TestFlowDeployer deployer2 = new TestFlowDeployer("2");
      deployer2.setInputs("test");
      deployer2.setOutputs("test");
      addDeployer(main, deployer2);
      TestFlowDeployer deployer3 = new TestFlowDeployer("3");
      deployer3.setInputs("test");
      deployer3.setOutputs("test");
      addDeployer(main, deployer3);
      TestFlowDeployer deployer1 = new TestFlowDeployer("1");
      deployer1.setOutputs("test");
      addDeployer(main, deployer1);

      Deployment deployment = createSimpleDeployment("transient");
      main.addDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(4, deployer4.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());
      assertEquals(-1, deployer3.getUndeployOrder());
      assertEquals(-1, deployer4.getUndeployOrder());

      main.removeDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(4, deployer4.getDeployOrder());
      assertEquals(8, deployer1.getUndeployOrder());
      assertEquals(7, deployer2.getUndeployOrder());
      assertEquals(6, deployer3.getUndeployOrder());
      assertEquals(5, deployer4.getUndeployOrder());

      main.addDeployment(deployment);
      main.process();

      assertEquals(9, deployer1.getDeployOrder());
      assertEquals(10, deployer2.getDeployOrder());
      assertEquals(11, deployer3.getDeployOrder());
      assertEquals(12, deployer4.getDeployOrder());
      assertEquals(8, deployer1.getUndeployOrder());
      assertEquals(7, deployer2.getUndeployOrder());
      assertEquals(6, deployer3.getUndeployOrder());
      assertEquals(5, deployer4.getUndeployOrder());
   }

   public void testMultipleOutput() throws Exception
   {
      DeployerClient main = createMainDeployer();
      TestFlowDeployer deployer1 = new TestFlowDeployer("1");
      deployer1.setOutputs("test1", "test2");
      addDeployer(main, deployer1);
      TestFlowDeployer deployer2 = new TestFlowDeployer("2");
      deployer2.setInputs("test1");
      addDeployer(main, deployer2);
      TestFlowDeployer deployer3 = new TestFlowDeployer("3");
      deployer3.setInputs("test2");
      addDeployer(main, deployer3);

      Deployment deployment = createSimpleDeployment("MultipleOutput");
      main.addDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());
      assertEquals(-1, deployer3.getUndeployOrder());

      main.removeDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(6, deployer1.getUndeployOrder());
      assertEquals(5, deployer2.getUndeployOrder());
      assertEquals(4, deployer3.getUndeployOrder());

      main.addDeployment(deployment);
      main.process();

      assertEquals(7, deployer1.getDeployOrder());
      assertEquals(8, deployer2.getDeployOrder());
      assertEquals(9, deployer3.getDeployOrder());
      assertEquals(6, deployer1.getUndeployOrder());
      assertEquals(5, deployer2.getUndeployOrder());
      assertEquals(4, deployer3.getUndeployOrder());
   }

   public void testMultipleInput() throws Exception
   {
      DeployerClient main = createMainDeployer();
      TestFlowDeployer deployer3 = new TestFlowDeployer("3");
      deployer3.setInputs("test1", "test2");
      addDeployer(main, deployer3);
      TestFlowDeployer deployer1 = new TestFlowDeployer("1");
      deployer1.setOutputs("test1");
      addDeployer(main, deployer1);
      TestFlowDeployer deployer2 = new TestFlowDeployer("2");
      deployer2.setOutputs("test2");
      addDeployer(main, deployer2);

      Deployment deployment = createSimpleDeployment("MultipleInput");
      main.addDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());
      assertEquals(-1, deployer3.getUndeployOrder());

      main.removeDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(6, deployer1.getUndeployOrder());
      assertEquals(5, deployer2.getUndeployOrder());
      assertEquals(4, deployer3.getUndeployOrder());

      main.addDeployment(deployment);
      main.process();

      assertEquals(7, deployer1.getDeployOrder());
      assertEquals(8, deployer2.getDeployOrder());
      assertEquals(9, deployer3.getDeployOrder());
      assertEquals(6, deployer1.getUndeployOrder());
      assertEquals(5, deployer2.getUndeployOrder());
      assertEquals(4, deployer3.getUndeployOrder());
   }

   public void testChain() throws Exception
   {
      DeployerClient main = createMainDeployer();
      TestFlowDeployer deployer3 = new TestFlowDeployer("3");
      deployer3.setInputs("test2");
      addDeployer(main, deployer3);
      TestFlowDeployer deployer2 = new TestFlowDeployer("2");
      deployer2.setInputs("test1");
      deployer2.setOutputs("test2");
      addDeployer(main, deployer2);
      TestFlowDeployer deployer1 = new TestFlowDeployer("1");
      deployer1.setOutputs("test1");
      addDeployer(main, deployer1);

      Deployment deployment = createSimpleDeployment("Chain");
      main.addDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());
      assertEquals(-1, deployer3.getUndeployOrder());

      main.removeDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(6, deployer1.getUndeployOrder());
      assertEquals(5, deployer2.getUndeployOrder());
      assertEquals(4, deployer3.getUndeployOrder());

      main.addDeployment(deployment);
      main.process();

      assertEquals(7, deployer1.getDeployOrder());
      assertEquals(8, deployer2.getDeployOrder());
      assertEquals(9, deployer3.getDeployOrder());
      assertEquals(6, deployer1.getUndeployOrder());
      assertEquals(5, deployer2.getUndeployOrder());
      assertEquals(4, deployer3.getUndeployOrder());
   }

   public void testComplicated() throws Exception
   {
      DeployerClient main = createMainDeployer();
      TestFlowDeployer deployer6 = new TestFlowDeployer("6");
      deployer6.setInputs("test1", "test3");
      addDeployer(main, deployer6);
      TestFlowDeployer deployer5 = new TestFlowDeployer("5");
      deployer5.setInputs("test3");
      deployer5.setOutputs("test3");
      addDeployer(main, deployer5);
      TestFlowDeployer deployer4 = new TestFlowDeployer("4");
      deployer4.setInputs("test2");
      deployer4.setOutputs("test3");
      addDeployer(main, deployer4);
      TestFlowDeployer deployer3 = new TestFlowDeployer("3");
      deployer3.setOutputs("test2");
      addDeployer(main, deployer3);
      TestFlowDeployer deployer2 = new TestFlowDeployer("2");
      deployer2.setOutputs("test1");
      addDeployer(main, deployer2);
      TestFlowDeployer deployer1 = new TestFlowDeployer("1");
      deployer1.setOutputs("test1");
      addDeployer(main, deployer1);

      Deployment deployment = createSimpleDeployment("Complicated");
      main.addDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(4, deployer4.getDeployOrder());
      assertEquals(5, deployer5.getDeployOrder());
      assertEquals(6, deployer6.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());
      assertEquals(-1, deployer3.getUndeployOrder());
      assertEquals(-1, deployer4.getUndeployOrder());
      assertEquals(-1, deployer5.getUndeployOrder());
      assertEquals(-1, deployer6.getUndeployOrder());

      main.removeDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(4, deployer4.getDeployOrder());
      assertEquals(5, deployer5.getDeployOrder());
      assertEquals(6, deployer6.getDeployOrder());
      assertEquals(12, deployer1.getUndeployOrder());
      assertEquals(11, deployer2.getUndeployOrder());
      assertEquals(10, deployer3.getUndeployOrder());
      assertEquals(9, deployer4.getUndeployOrder());
      assertEquals(8, deployer5.getUndeployOrder());
      assertEquals(7, deployer6.getUndeployOrder());

      main.addDeployment(deployment);
      main.process();

      assertEquals(13, deployer1.getDeployOrder());
      assertEquals(14, deployer2.getDeployOrder());
      assertEquals(15, deployer3.getDeployOrder());
      assertEquals(16, deployer4.getDeployOrder());
      assertEquals(17, deployer5.getDeployOrder());
      assertEquals(18, deployer6.getDeployOrder());
      assertEquals(12, deployer1.getUndeployOrder());
      assertEquals(11, deployer2.getUndeployOrder());
      assertEquals(10, deployer3.getUndeployOrder());
      assertEquals(9, deployer4.getUndeployOrder());
      assertEquals(8, deployer5.getUndeployOrder());
      assertEquals(7, deployer6.getUndeployOrder());
   }

   public void testIntermediateIsRelativelySorted() throws Exception
   {
      DeployerClient main = createMainDeployer();
      TestFlowDeployer deployer2 = new TestFlowDeployer("A");
      deployer2.setInputs("test1");
      addDeployer(main, deployer2);
      TestFlowDeployer deployer3 = new TestFlowDeployer("B");
      addDeployer(main, deployer3);
      TestFlowDeployer deployer1 = new TestFlowDeployer("C");
      deployer1.setOutputs("test1");
      addDeployer(main, deployer1);

      Deployment deployment = createSimpleDeployment("IntermediateIsRelativelySorted");
      main.addDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());
      assertEquals(-1, deployer3.getUndeployOrder());

      main.removeDeployment(deployment);
      main.process();

      assertEquals(1, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(3, deployer3.getDeployOrder());
      assertEquals(6, deployer1.getUndeployOrder());
      assertEquals(5, deployer2.getUndeployOrder());
      assertEquals(4, deployer3.getUndeployOrder());

      main.addDeployment(deployment);
      main.process();

      assertEquals(7, deployer1.getDeployOrder());
      assertEquals(8, deployer2.getDeployOrder());
      assertEquals(9, deployer3.getDeployOrder());
      assertEquals(6, deployer1.getUndeployOrder());
      assertEquals(5, deployer2.getUndeployOrder());
      assertEquals(4, deployer3.getUndeployOrder());
   }

   public void testTransitionOrdering() throws Exception
   {
      DeployerClient main = createMainDeployer();
      TestFlowDeployer deployer1 = new TestFlowDeployer("A");
      deployer1.setInputs("3");
      deployer1.setOutputs("4");
      addDeployer(main, deployer1);

      TestFlowDeployer deployer2 = new TestFlowDeployer("B");
      deployer2.setInputs("1");
      deployer2.setOutputs("2");
      addDeployer(main, deployer2);

      TestFlowDeployer deployer3 = new TestFlowDeployer("C");
      deployer3.setInputs("2");
      deployer3.setOutputs("3");
      addDeployer(main, deployer3);

      Deployment deployment = createSimpleDeployment("TransitionOrdering");
      main.addDeployment(deployment);
      main.process();

      assertEquals(3, deployer1.getDeployOrder());
      assertEquals(1, deployer2.getDeployOrder());
      assertEquals(2, deployer3.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());
      assertEquals(-1, deployer3.getUndeployOrder());

      main.removeDeployment(deployment);
      main.process();

      assertEquals(3, deployer1.getDeployOrder());
      assertEquals(1, deployer2.getDeployOrder());
      assertEquals(2, deployer3.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(6, deployer2.getUndeployOrder());
      assertEquals(5, deployer3.getUndeployOrder());

      main.addDeployment(deployment);
      main.process();

      assertEquals(9, deployer1.getDeployOrder());
      assertEquals(7, deployer2.getDeployOrder());
      assertEquals(8, deployer3.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(6, deployer2.getUndeployOrder());
      assertEquals(5, deployer3.getUndeployOrder());
   }

   public void testSymetricDots() throws Exception
   {
      DeployerClient main = createMainDeployer();
      TestFlowDeployer deployer1 = new TestFlowDeployer("1");
      deployer1.setInputs("X");
      deployer1.setOutputs("B");
      addDeployer(main, deployer1);

      TestFlowDeployer deployer2 = new TestFlowDeployer("2");
      deployer2.setInputs("X");
      deployer2.setOutputs("X");
      addDeployer(main, deployer2);

      TestFlowDeployer deployer3 = new TestFlowDeployer("3");
      deployer3.setInputs("A");
      deployer3.setOutputs("X");
      addDeployer(main, deployer3);

      Deployment deployment = createSimpleDeployment("SymetricDots");
      main.addDeployment(deployment);
      main.process();

      assertEquals(3, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(1, deployer3.getDeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());
      assertEquals(-1, deployer3.getUndeployOrder());

      main.removeDeployment(deployment);
      main.process();

      assertEquals(3, deployer1.getDeployOrder());
      assertEquals(2, deployer2.getDeployOrder());
      assertEquals(1, deployer3.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(5, deployer2.getUndeployOrder());
      assertEquals(6, deployer3.getUndeployOrder());

      main.addDeployment(deployment);
      main.process();

      assertEquals(9, deployer1.getDeployOrder());
      assertEquals(8, deployer2.getDeployOrder());
      assertEquals(7, deployer3.getDeployOrder());
      assertEquals(4, deployer1.getUndeployOrder());
      assertEquals(5, deployer2.getUndeployOrder());
      assertEquals(6, deployer3.getUndeployOrder());
   }

   public void testDoubleCycle() throws Exception
   {
      DeployerClient main = createMainDeployer();

      TestFlowDeployer deployer2 = new TestFlowDeployer("A");
      deployer2.setInputs("test2");
      addDeployer(main, deployer2);

      TestFlowDeployer deployer3 = new TestFlowDeployer("B");
      addDeployer(main, deployer3);

      TestFlowDeployer deployer6 = new TestFlowDeployer("C");
      deployer6.setInputs("2ndcycle");
      addDeployer(main, deployer6);

      TestFlowDeployer deployer1 = new TestFlowDeployer("D");
      deployer1.setOutputs("test1");
      addDeployer(main, deployer1);

      TestFlowDeployer deployer4 = new TestFlowDeployer("E");
      addDeployer(main, deployer4);

      TestFlowDeployer deployer5 = new TestFlowDeployer("F");
      deployer5.setInputs("test1");
      deployer5.setOutputs("test2");
      addDeployer(main, deployer5);

      TestFlowDeployer deployer7 = new TestFlowDeployer("G");
      addDeployer(main, deployer7);

      TestFlowDeployer deployer8 = new TestFlowDeployer("H");
      deployer8.setOutputs("2ndcycle");
      addDeployer(main, deployer8);

      Deployment deployment = createSimpleDeployment("DoubleCycle");
      main.addDeployment(deployment);
      main.process();

      assertEquals(1, deployer3.getDeployOrder());
      assertEquals(2, deployer8.getDeployOrder());
      assertEquals(3, deployer6.getDeployOrder());
      assertEquals(4, deployer1.getDeployOrder());
      assertEquals(5, deployer4.getDeployOrder());
      assertEquals(6, deployer5.getDeployOrder());
      assertEquals(7, deployer2.getDeployOrder());
      assertEquals(8, deployer7.getDeployOrder());
      assertEquals(-1, deployer3.getUndeployOrder());
      assertEquals(-1, deployer8.getUndeployOrder());
      assertEquals(-1, deployer6.getUndeployOrder());
      assertEquals(-1, deployer1.getUndeployOrder());
      assertEquals(-1, deployer4.getUndeployOrder());
      assertEquals(-1, deployer5.getUndeployOrder());
      assertEquals(-1, deployer2.getUndeployOrder());
      assertEquals(-1, deployer7.getUndeployOrder());

      main.removeDeployment(deployment);
      main.process();

      assertEquals(1, deployer3.getDeployOrder());
      assertEquals(2, deployer8.getDeployOrder());
      assertEquals(3, deployer6.getDeployOrder());
      assertEquals(4, deployer1.getDeployOrder());
      assertEquals(5, deployer4.getDeployOrder());
      assertEquals(6, deployer5.getDeployOrder());
      assertEquals(7, deployer2.getDeployOrder());
      assertEquals(8, deployer7.getDeployOrder());
      assertEquals(16, deployer3.getUndeployOrder());
      assertEquals(15, deployer8.getUndeployOrder());
      assertEquals(14, deployer6.getUndeployOrder());
      assertEquals(13, deployer1.getUndeployOrder());
      assertEquals(12, deployer4.getUndeployOrder());
      assertEquals(11, deployer5.getUndeployOrder());
      assertEquals(10, deployer2.getUndeployOrder());
      assertEquals(9, deployer7.getUndeployOrder());

      main.addDeployment(deployment);
      main.process();

      assertEquals(17, deployer3.getDeployOrder());
      assertEquals(18, deployer8.getDeployOrder());
      assertEquals(19, deployer6.getDeployOrder());
      assertEquals(20, deployer1.getDeployOrder());
      assertEquals(21, deployer4.getDeployOrder());
      assertEquals(22, deployer5.getDeployOrder());
      assertEquals(23, deployer2.getDeployOrder());
      assertEquals(24, deployer7.getDeployOrder());
      assertEquals(16, deployer3.getUndeployOrder());
      assertEquals(15, deployer8.getUndeployOrder());
      assertEquals(14, deployer6.getUndeployOrder());
      assertEquals(13, deployer1.getUndeployOrder());
      assertEquals(12, deployer4.getUndeployOrder());
      assertEquals(11, deployer5.getUndeployOrder());
      assertEquals(10, deployer2.getUndeployOrder());
      assertEquals(9, deployer7.getUndeployOrder());
   }
}
