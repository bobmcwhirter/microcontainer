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
package org.jboss.test.kernel.junit;

import java.net.URL;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.bootstrap.AbstractBootstrap;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.plugins.deployment.xml.BasicXMLDeployer;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.test.AbstractTestDelegate;

/**
 * A MicrocontainerTestDelegate.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class MicrocontainerTestDelegate extends AbstractTestDelegate
{
   /** The kernel */
   protected Kernel kernel;

   /** The deployer */
   protected BasicXMLDeployer deployer;
   
   /**
    * Create a new MicrocontainerTestDelegate.
    * 
    * @param clazz the test class
    * @throws Exception for any error
    */
   public MicrocontainerTestDelegate(Class clazz) throws Exception
   {
      super(clazz);
   }

   public void setUp() throws Exception
   {
      super.setUp();
      
      try
      {
         // Bootstrap the kernel
         AbstractBootstrap bootstrap = getBootstrap();
         bootstrap.run();
         kernel = bootstrap.getKernel();
         
         // Create the deployer
         deployer = createDeployer();

         // Deploy
         deploy();
      }
      catch (RuntimeException e)
      {
         throw e;
      }
      catch (Exception e)
      {
         throw e;
      }
      catch (Error e)
      {
         throw e;
      }
      catch (Throwable e)
      {
         throw new RuntimeException(e);
      }
   }

   protected BasicXMLDeployer createDeployer()
   {
      return new BasicXMLDeployer(kernel);
   }

   public void tearDown() throws Exception
   {
      super.tearDown();
      undeploy();
   }
   
   /**
    * Get the kernel bootstrap
    * 
    * @return the bootstrap
    * @throws Exception for any error
    */
   protected AbstractBootstrap getBootstrap() throws Exception
   {
      return new BasicBootstrap();
   }
   
   /**
    * Get a bean
    *
    * @param name the name of the bean
    * @param state the state of the bean
    * @return the bean
    * @throws IllegalStateException when the bean does not exist at that state
    */
   protected Object getBean(final Object name, final ControllerState state)
   {
      KernelControllerContext context = getControllerContext(name, state);
      return context.getTarget();
   }
   
   /**
    * Get the metadata repository
    * @return the metadata repository
    * @throws IllegalStateException when the bean does not exist at that state
    */
   protected KernelMetaDataRepository getMetaDataRepository()
   {
      return kernel.getMetaDataRepository();
   }
   
   /**
    * Get a context
    *
    * @param name the name of the bean
    * @param state the state of the bean
    * @return the context
    * @throws IllegalStateException when the context does not exist at that state
    */
   protected KernelControllerContext getControllerContext(final Object name, final ControllerState state)
   {
      KernelController controller = kernel.getController();
      KernelControllerContext context = (KernelControllerContext) controller.getContext(name, state);
      if (context == null)
         throw new IllegalStateException("Bean not found " + name + " at state " + state);
      return context;
   }

   /**
    * Change the context to the given state
    * 
    * @param context the context
    * @param required the required state
    * @return the actual state
    * @throws Throwable for any error
    */
   protected ControllerState change(KernelControllerContext context, ControllerState required) throws Throwable
   {
      Controller controller = kernel.getController();
      controller.change(context, required);
      return context.getState();
   }

   /**
    * Validate
    * 
    * @throws Exception for any error
    */
   protected void validate() throws Exception
   {
      try
      {
         deployer.validate();
      }
      catch (RuntimeException e)
      {
         throw e;
      }
      catch (Exception e)
      {
         throw e;
      }
      catch (Error e)
      {
         throw e;
      }
      catch (Throwable t)
      {
         throw new RuntimeException(t);
      }
   }
   
   /**
    * Deploy a url
    *
    * @param url the deployment url
    * @return the deployment
    * @throws Exception for any error  
    */
   protected KernelDeployment deploy(URL url) throws Exception
   {
      try
      {
         log.debug("Deploying " + url);
         KernelDeployment deployment = deployer.deploy(url);
         log.trace("Deployed " + url);
         return deployment;
      }
      catch (RuntimeException e)
      {
         throw e;
      }
      catch (Exception e)
      {
         throw e;
      }
      catch (Error e)
      {
         throw e;
      }
      catch (Throwable t)
      {
         throw new RuntimeException(t);
      }
   }
   
   /**
    * Undeploy a deployment
    * 
    * @param deployment the deployment
    */
   protected void undeploy(KernelDeployment deployment)
   {
      log.debug("Undeploying " + deployment.getName());
      try
      {
         deployer.undeploy(deployment);
         log.trace("Undeployed " + deployment.getName());
      }
      catch (Throwable t)
      {
         log.warn("Error during undeployment: " + deployment.getName(), t);
      }
   }
   
   /**
    * Undeploy a deployment
    * 
    * @param url the url
    */
   protected void undeploy(URL url)
   {
      log.debug("Undeploying " + url);
      try
      {
         deployer.undeploy(url);
         log.trace("Undeployed " + url);
      }
      catch (Throwable t)
      {
         log.warn("Error during undeployment: " + url, t);
      }
   }
   
   /**
    * Deploy the beans
    * 
    * @throws Exception for any error
    */
   protected void deploy() throws Exception
   {
      String testName = getTestName();
      URL url = getTestResource(testName);
      if (url != null)
         deploy(url);
      else
         log.debug("No test specific deployment " + testName);
   }

   protected String getTestName()
   {
      String testName = clazz.getName();
      return testName.replace('.', '/') + ".xml";
   }

   protected URL getTestResource(String testName)
   {
      return clazz.getClassLoader().getResource(testName);
   }

   /**
    * Undeploy all
    */
   protected void undeploy()
   {
      log.debug("Undeploying " + deployer.getDeploymentNames());
      deployer.shutdown();
   }
}
