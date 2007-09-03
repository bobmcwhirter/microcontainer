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
package org.jboss.reliance.drools.core;

import java.io.Reader;
import java.io.StringReader;
import java.util.UUID;

import org.drools.compiler.PackageBuilder;
import org.drools.compiler.PackageBuilderConfiguration;
import org.jboss.beans.metadata.plugins.annotations.Constructor;
import org.jboss.beans.metadata.plugins.annotations.Inject;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.kernel.plugins.bootstrap.basic.KernelConstants;
import org.jboss.kernel.spi.dependency.KernelController;
import org.w3c.dom.Element;

/**
 * Package generator.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class PackageGenerator
{
   private KernelController controller;

   private String name;
   private String fromDRL;
   private Element fromXML;
   private String dslTemplate;

   private PackageBuilderConfiguration configuration;
   private String uuidName;

   @Constructor
   public PackageGenerator(@Inject(bean= KernelConstants.KERNEL_CONTROLLER_NAME) KernelController controller)
   {
      this.controller = controller;
   }

   /**
    * Start package creation.
    *
    * @throws Throwable for any error
    */
   public void start() throws Throwable
   {
      PackageBuilder builder = new PackageBuilder(configuration);
      Reader dslReader = null;
      if (dslTemplate != null)
         dslReader = new StringReader(dslTemplate);
      if (fromDRL != null)
      {
         Reader drlReader = new StringReader(fromDRL);
         if (dslReader != null)
            builder.addPackageFromDrl(drlReader, dslReader);
         else
            builder.addPackageFromDrl(drlReader);
      }
      if (fromXML != null)
      {
         Reader xmlReader = new StringReader(fromXML.toString());
         builder.addPackageFromXml(xmlReader);
      }
      uuidName = UUID.randomUUID().toString();
      BeanMetaDataBuilder beanBuilder = BeanMetaDataBuilderFactory.createBuilder(
            uuidName,
            PackageWrapper.class.getName()
      );
      beanBuilder
            .addConstructorParameter(String.class.getName(), name)
            .addConstructorParameter(org.drools.rule.Package.class.getName(), builder.getPackage());
      controller.install(beanBuilder.getBeanMetaData());
   }

   /**
    * Stop package creation,
    * uninstall package from Controller 
    */
   public void stop()
   {
      controller.uninstall(uuidName);
      uuidName = null;
   }

   public void setConfiguration(PackageBuilderConfiguration configuration)
   {
      this.configuration = configuration;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public void setFromDRL(String fromDRL)
   {
      this.fromDRL = fromDRL;
   }

   public void setFromXML(Element fromXML)
   {
      this.fromXML = fromXML;
   }

   public void setDslTemplate(String dslTemplate)
   {
      this.dslTemplate = dslTemplate;
   }
}
