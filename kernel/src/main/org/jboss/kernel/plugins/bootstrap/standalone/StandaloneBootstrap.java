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
package org.jboss.kernel.plugins.bootstrap.standalone;

import java.net.URL;
import java.util.Enumeration;

import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.plugins.deployment.xml.BasicXMLDeployer;

/**
 * Standalone Bootstrap of the kernel.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="mailto:les.hazlewood@jboss.org">Les A. Hazlewood</a>
 * @version $Revision$
 */
public class StandaloneBootstrap extends BasicBootstrap
{
   /** The deployer */
   protected BasicXMLDeployer deployer;
   
   /** The arguments */
   protected String[] args;
   
   /**
    * Bootstrap the kernel from the command line
    * 
    * @param args the command line arguments
    * @throws Exception for any error
    */
   public static void main(String[] args) throws Exception
   {
      StandaloneBootstrap bootstrap = new StandaloneBootstrap(args);
      bootstrap.run();
   }

   /**
    * Create a new bootstrap
    * 
    * @param args the arguments
    * @throws Exception for any error
    */
   public StandaloneBootstrap(String[] args) throws Exception
   {
      super();
      this.args = args;
   }
   
   public void bootstrap() throws Throwable
   {
      super.bootstrap();
      
      deployer = new BasicXMLDeployer(getKernel());
      
      Runtime.getRuntime().addShutdownHook(new Shutdown());
      
      ClassLoader cl = Thread.currentThread().getContextClassLoader();
      for (Enumeration<URL> e = cl.getResources(StandaloneKernelConstants.DEPLOYMENT_XML_NAME); e.hasMoreElements(); )
      {
         URL url = e.nextElement();
         deploy(url);
      }
      for (Enumeration<URL> e = cl.getResources("META-INF/" + StandaloneKernelConstants.DEPLOYMENT_XML_NAME); e.hasMoreElements(); )
      {
         URL url = e.nextElement();
         deploy(url);
      }
      
      // Validate that everything is ok
      deployer.validate();
   }
   
   /**
    * Deploy a url
    *
    * @param url the deployment url
    * @throws Throwable for any error  
    */
   protected void deploy(URL url) throws Throwable
   {
      deployer.deploy(url);
   }
   
   /**
    * Undeploy a url
    * 
    * @param url the deployment url
    */
   protected void undeploy(URL url)
   {
      try
      {
         deployer.undeploy(url);
      }
      catch (Throwable t)
      {
         log.warn("Error during undeployment: " + url, t);
      }
   }
   
   protected class Shutdown extends Thread
   {
      public void run()
      {
         log.info("Shutting down");
         deployer.shutdown();
      }
   }
}
