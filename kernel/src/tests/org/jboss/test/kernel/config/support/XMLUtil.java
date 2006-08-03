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
package org.jboss.test.kernel.config.support;

import java.net.URL;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.deployment.xml.BasicXMLDeployer;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.kernel.spi.registry.KernelRegistry;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.util.Classes;

/**
 * A helper for tests using xml.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class XMLUtil
{
   /** The kernel */
   private Kernel kernel;
   
   /** The test */
   private AbstractKernelTest test;
   
   /** The deployer */
   private BasicXMLDeployer deployer;
   
   /** The deployment */
   private KernelDeployment deployment;
   
   /**
    * 
    * Create a new XMLKernel.
    * 
    * @param kernel the kernel
    * @param test the test
    * @param validate whether to validate the deployment
    * @throws Throwable for any error
    */
   public XMLUtil(Kernel kernel, AbstractKernelTest test, boolean validate) throws Throwable
   {
      this.kernel = kernel;
      this.test = test;
      deployer = new BasicXMLDeployer(kernel);
      String packageName = Classes.getPackageName(test.getClass());
      packageName = packageName.replace('.', '/');
      String name = "/xml-test/" + packageName + '/' + test.getName() + ".xml";
      test.getLog().debug("Using " + name);
      URL url = test.getResource(name);
      if (url == null)
         throw new RuntimeException("Resource not found: " + name);
      test.getLog().debug("url=" + url);
      deployment = deployer.deploy(url);
      if (validate)
         validate();
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
    * Get the deployer
    * 
    * @return the deployer
    */
   public BasicXMLDeployer getDeployer()
   {
      return deployer;
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
    * Get a bean from the registry
    * 
    * @param name the bean name
    * @return the bean
    * @throws Throwable for any error
    */
   public Object getBean(String name) throws Throwable
   {
      KernelRegistry registry = kernel.getRegistry();
      KernelRegistryEntry entry = registry.getEntry(name);
      return entry.getTarget();
   }
   
   /**
    * Validate the deployment
    * 
    * @throws Throwable for any error
    */
   public void validate() throws Throwable
   {
      deployer.validate();
   }
   
   /**
    * Undeploy the deployment
    */
   public void undeploy()
   {
      deployer.undeploy(deployment);
   }
}
