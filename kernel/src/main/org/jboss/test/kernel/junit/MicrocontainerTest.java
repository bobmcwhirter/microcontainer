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

import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;

/**
 * A MicrocontainerTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class MicrocontainerTest extends AbstractTestCaseWithSetup
{
   /**
    * Get the test delegate
    * 
    * @param clazz the test class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class clazz) throws Exception
   {
      return new MicrocontainerTestDelegate(clazz);
   }
   
   /**
    * Create a new Microcontainer test
    * 
    * @param name the test name
    */
   public MicrocontainerTest(String name)
   {
      super(name);
   }
   
   protected void setUp() throws Exception
   {
      super.setUp();
      configureLogging();
      // Validate everything deployed 
      getMCDelegate().validate();
   }

   /**
    * Get a bean
    * 
    * @param name the bean name
    * @return the bean
    * @throws IllegalStateException when the bean does not exist
    */
   protected Object getBean(Object name)
   {
      return getBean(name, ControllerState.INSTALLED);
   }
   
   /**
    * Get a bean
    * 
    * @param name the name of the bean
    * @param state the state of the bean
    * @return the bean
    * @throws IllegalStateException when the bean does not exist at that state
    */
   protected Object getBean(Object name, ControllerState state)
   {
      return getMCDelegate().getBean(name, state);
   }

   /**
    * Get a context
    * 
    * @param name the bean name
    * @return the context
    * @throws IllegalStateException when the context does not exist 
    */
   protected KernelControllerContext getControllerContext(Object name)
   {
      return getControllerContext(name, ControllerState.INSTALLED);
   }

   /**
    * Get a context
    * 
    * @param name the name of the bean
    * @param state the state of the bean
    * @return the context
    * @throws IllegalStateException when the context does not exist at that state
    */
   protected KernelControllerContext getControllerContext(Object name, ControllerState state)
   {
      return getMCDelegate().getControllerContext(name, state);
   }

   /**
    * Change the context to the given state
    * 
    * @param context the context
    * @param required the required state
    * @return the actual state
    * @throws Throwable for any error
    */
   public ControllerState change(KernelControllerContext context, ControllerState required) throws Throwable
   {
      return getMCDelegate().change(context, required);
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
      return getMCDelegate().deploy(url);
   }
   
   /**
    * Deploy a resource
    *
    * @param resource the deployment resource
    * @return the deployment
    * @throws Exception for any error  
    */
   protected KernelDeployment deploy(String resource) throws Exception
   {
      URL url = getClass().getResource(resource);
      if (url == null)
         throw new IllegalArgumentException("Resource not found: " + resource);
      return getMCDelegate().deploy(url);
   }
   
   /**
    * Undeploy a deployment
    *
    * @param deployment the deployment
    */
   protected void undeploy(KernelDeployment deployment)
   {
      getMCDelegate().undeploy(deployment);
   }
   
   /**
    * Undeploy a deployment
    *
    * @param resource the url
    */
   protected void undeploy(String resource)
   {
      URL url = getClass().getResource(resource);
      if (url == null)
         throw new IllegalArgumentException("Resource not found: " + resource);
      getMCDelegate().undeploy(url);
   }

   /**
    * Validate
    * 
    * @throws Exception for any error
    */
   protected void validate() throws Exception
   {
      getMCDelegate().validate();
   }
   
   /**
    * Get the metadata repository
    * 
    * @return the metadata repository
    * @throws IllegalStateException when the bean does not exist at that state
    */
   protected KernelMetaDataRepository getMetaDataRepository()
   {
      return getMCDelegate().getMetaDataRepository();
   }

   /**
    * Get the delegate
    * 
    * @return the delegate
    */
   protected MicrocontainerTestDelegate getMCDelegate()
   {
      return (MicrocontainerTestDelegate) getDelegate();
   }
}
