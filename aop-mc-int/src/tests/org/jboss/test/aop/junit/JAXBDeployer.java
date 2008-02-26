/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.test.aop.junit;

import java.net.URL;

import org.jboss.aop.microcontainer.beans.beanmetadatafactory.AOPDeployment;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.javabean.plugins.jaxb.JavaBean;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.kernel.plugins.deployment.xml.BasicXMLDeployer;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.logging.Logger;
import org.jboss.test.xb.builder.JBossXBTestDelegate;
import org.jboss.test.xb.builder.TestSchemaResolver;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.builder.JBossXBBuilder;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class JAXBDeployer extends BasicXMLDeployer
{
   /** The log */
   private static final Logger log = Logger.getLogger(BasicXMLDeployer.class);
   
   JBossXBTestDelegate jbossXbTestDelegate;

   public JAXBDeployer(Kernel kernel, ControllerMode mode, Class<?> clazz)
   {
      super(kernel, mode);
      setupJaxbDelegate(clazz);
   }

   public JAXBDeployer(Kernel kernel, Class<?> clazz)
   {
      super(kernel);
      setupJaxbDelegate(clazz);
   }

   private void setupJaxbDelegate(Class<?> clazz)
   {
      try
      {
         jbossXbTestDelegate = new JBossXBTestDelegate(clazz);
         jbossXbTestDelegate.setUp();
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }
   
   public KernelDeployment unmarshal(final URL url) throws Exception
   {
      TestSchemaResolver resolver = setupResolver();
      
      KernelDeployment deployment = (KernelDeployment)jbossXbTestDelegate.unmarshal(url.toString(), resolver);
      
      deployment.setName(url.toString());

      return deployment;
   }
   
   private TestSchemaResolver setupResolver()
   {
      TestSchemaResolver resolver = new TestSchemaResolver();
      
      addSchemaBinding(resolver, AbstractKernelDeployment.class);
      addSchemaBinding(resolver, AOPDeployment.class);
      addSchemaBinding(resolver, JavaBean.class);
      
      return resolver;
   }

   private SchemaBinding addSchemaBinding(TestSchemaResolver resolver, Class<?> clazz)
   {
      SchemaBinding binding = JBossXBBuilder.build(clazz);
      resolver.addSchemaBinding(binding);
      return binding;
   }
   /**
    * Deploy a url
    * 
    * @param url the url to deploy
    * @return the kernel deployment
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

      KernelDeployment deployment = unmarshal(url);
      
      if (trace)
      {
         long now = System.currentTimeMillis();
         log.trace("Parsing " + url + " took " + (now-start) + " milliseconds");
      }

      deploy(deployment);

      if (trace)
      {
         long now = System.currentTimeMillis();
         log.trace("Deploying " + url + " took " + (now-start) + " milliseconds");
      }

      return deployment;
   }

}
