/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.kernel.dependency.support;

import java.net.URL;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.deployment.xml.BasicXMLDeployer;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.kernel.AbstractKernelTest;

/**
 * A helper for tests.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class TestUtil
{
   /** The kernel */
   private Kernel kernel;

   /** The kernel controller */
   private KernelController controller;

   /** The xml deployer */
   private BasicXMLDeployer deployer;
   
   /** The test */
   private AbstractKernelTest test;
   
   /**
    * Create a new TestUtil.
    * 
    * @param kernel the kernel
    * @param test the test
    * @throws Throwable for any error
    */
   public TestUtil(Kernel kernel, AbstractKernelTest test) throws Throwable
   {
      this.kernel = kernel;
      this.controller = kernel.getController();
      this.deployer = new BasicXMLDeployer(kernel);
      this.test = test;
   }
   
   /**
    * Get the kernel
    * 
    * @return the kernel
    */
   public Kernel getKernel()
   {
      return kernel;
   }
   
   /**
    * Get the test
    * 
    * @return the test
    */
   public AbstractKernelTest getTest()
   {
      return test;
   }
   
   /**
    * Install a BeanMetaData
    * 
    * @param beanMetaData the bean meta data
    * @return the context
    * @throws Throwable for any error
    */
   public KernelControllerContext install(BeanMetaData beanMetaData) throws Throwable
   {
      return controller.install(beanMetaData);
   }
   
   /**
    * Uninstall
    * 
    * @param name the name
    * @throws Throwable for any error
    */
   public void uninstall(String name) throws Throwable
   {
      controller.uninstall(name);
   }
   
   /**
    * Get a context
    * 
    * @param name the name
    * @return the context
    * @throws Throwable for any error
    */
   public ControllerContext getContext(String name) throws Throwable
   {
      return controller.getContext(name, null);
   }
   
   /**
    * Get installed context
    * 
    * @param name the name
    * @return the context
    * @throws Throwable for any error
    */
   public ControllerContext getInstalledContext(String name) throws Throwable
   {
      return controller.getInstalledContext(name);
   }
   
   /**
    * Deploy a url
    * 
    * @param url the url
    * @return KernelDeployment the deployment
    * @throws Throwable for any error
    */
   public KernelDeployment deploy(URL url) throws Throwable
   {
      return deployer.deploy(url);
   }
}
