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
package org.jboss.kernel.plugins.deployment.xml;

import java.io.InputStream;
import java.net.URL;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployer;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.logging.Logger;
import org.jboss.xb.binding.sunday.unmarshalling.SingletonSchemaResolverFactory;
import org.jboss.xb.binding.Unmarshaller;
import org.jboss.xb.binding.UnmarshallerFactory;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBindingResolver;

/**
 * An XML deployer.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="mailto:les.hazlewood@jboss.org">Les A. Hazlewood</a>
 * @author <a href="mailto:marc.fleury@jboss.com">Marc Fleury</a>
 * @version $Revision$
 */
public class BeanXMLDeployer extends AbstractKernelDeployer
{
   /** The log */
   private static final Logger log = Logger.getLogger(BeanXMLDeployer.class);

   /** Unmarshaller factory */
   private static final UnmarshallerFactory factory = UnmarshallerFactory.newInstance();
   
   /** The resolver */
   private static final SchemaBindingResolver resolver = SingletonSchemaResolverFactory.getInstance().getSchemaBindingResolver();

   /**
    * Create a new XML kernel deployer
    * 
    * @param kernel the kernel
    * @throws Throwable for any error
    */
   public BeanXMLDeployer(Kernel kernel) throws Throwable
   {
      super(kernel);
   }

   /**
    * Deploy a url
    * 
    * @param url the url to deploy
    * @throws Throwable for any error
    */
   public KernelDeployment deploy(final URL url) throws Throwable
   {
      final boolean trace = log.isTraceEnabled();

      if (url == null)
         throw new IllegalArgumentException("Null url");
      
      if (trace)
         log.trace("Parsing " + url);

      long start = System.currentTimeMillis();
      
      Unmarshaller unmarshaller = factory.newUnmarshaller();
      KernelDeployment deployment = (KernelDeployment) unmarshaller.unmarshal(url.toString(), resolver);
      if (deployment == null)
         throw new RuntimeException("The xml " + url + " is not well formed!");
      deployment.setName(url.toString());
      
      long now = System.currentTimeMillis();
      log.debug("Parsing " + url + " took " + (now -start) + " milliseconds");

      if (trace)
         log.trace("Deploying " + deployment);
      deploy(deployment);
      
      now = System.currentTimeMillis();
      log.debug("Deploying " + url + " took " + (now -start) + " milliseconds");
      
      if (trace)
         log.trace("Deployed " + deployment.getInstalledContexts());
      
      return deployment;
   }
   
   /**
    * Deploy a stream.  We may be deploying XML fragments.
    *
    * @param deploymentName the deployment name
    * @param stream the stream
    */
   public KernelDeployment deploy(String deploymentName, final InputStream stream) throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      if (trace)
         log.trace("Parsing " + deploymentName);
      Unmarshaller unmarshaller = factory.newUnmarshaller();
      KernelDeployment deployment = (KernelDeployment) unmarshaller.unmarshal(stream, resolver);
      if (deployment == null)
         throw new RuntimeException("The deployment " + deploymentName + " is not well formed!");
      deployment.setName(deploymentName);

      if (trace)
         log.trace("Deploying " + deployment);
      deploy(deployment);
      if (trace)
         log.trace("Deployed " + deployment.getInstalledContexts());

      return deployment;
   }
}
