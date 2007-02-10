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
package org.jboss.aop.microcontainer.beans.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.jboss.aop.microcontainer.beans.AspectBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.ConfigureLifecycleBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.CreateLifecycleBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.DescribeLifecycleBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.InstallLifecycleBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.InstantiateLifecycleBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.LifecycleBeanMetaDataFactory;
import org.jboss.aop.microcontainer.beans.StartLifecycleBeanMetaDataFactory;
import org.jboss.kernel.plugins.deployment.xml.BeanFactoryHandler;
import org.jboss.kernel.plugins.deployment.xml.BeanSchemaBinding20;
import org.jboss.kernel.plugins.deployment.xml.BeanSchemaBindingHelper;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBindingInitializer;
import org.jboss.xb.binding.sunday.unmarshalling.TypeBinding;
import org.xml.sax.Attributes;

/**
 * AOPBeansSchemaInitializer.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AOPBeansSchemaInitializer implements SchemaBindingInitializer
{
   /** The namespace */
   private static final String AOP_BEANS_NS = "urn:jboss:aop-beans:1.0";

   /** The aspect binding */
   private static final QName aspectTypeQName = new QName(AOP_BEANS_NS, "aspectType");
   
   /** The lifecycle configure aspect binding */
   private static final QName lifecycleConfigureTypeQName = new QName(AOP_BEANS_NS, "lifecycleConfigureType");
   
   /** The lifecycle create aspect binding */
   private static final QName lifecycleCreateTypeQName = new QName(AOP_BEANS_NS, "lifecycleCreateType");
   
   /** The lifecycle describe aspect binding */
   private static final QName lifecycleDescribeTypeQName = new QName(AOP_BEANS_NS, "lifecycleDescribeType");
   
   /** The lifecycle aspect binding */
   private static final QName lifecycleInstallTypeQName = new QName(AOP_BEANS_NS, "lifecycleInstallType");
   
   /** The lifecycle aspect binding */
   private static final QName lifecycleInstantiateTypeQName = new QName(AOP_BEANS_NS, "lifecycleInstantiateType");
   
   /** The lifecycle start aspect binding */
   private static final QName lifecycleStartTypeQName = new QName(AOP_BEANS_NS, "lifecycleStartType");

   
   public SchemaBinding init(SchemaBinding schema)
   {
      // ignore XB property replacement
      schema.setReplacePropertyRefs(false);

      // aspect binding
      TypeBinding aspectType = schema.getType(aspectTypeQName);
      BeanSchemaBindingHelper.initBeanFactoryHandlers(aspectType);
      aspectType.setHandler(new AspectBeanFactoryHandler());

      //Configure binding
      TypeBinding lifecycleConfigureTypeQ = schema.getType(lifecycleConfigureTypeQName);
      BeanSchemaBindingHelper.initBeanFactoryHandlers(lifecycleConfigureTypeQ);
      lifecycleConfigureTypeQ.setHandler(new ConfigureLifecycleBeanFactoryHandler());

      //Create binding
      TypeBinding lifecycleCreateType = schema.getType(lifecycleCreateTypeQName);
      BeanSchemaBindingHelper.initBeanFactoryHandlers(lifecycleCreateType);
      lifecycleCreateType.setHandler(new CreateLifecycleBeanFactoryHandler());

      //Describe binding
      TypeBinding lifecycleDescribeType = schema.getType(lifecycleDescribeTypeQName);
      BeanSchemaBindingHelper.initBeanFactoryHandlers(lifecycleDescribeType);
      lifecycleDescribeType.setHandler(new DescribeLifecycleBeanFactoryHandler());

      //Install binding
      TypeBinding lifecycleInstallType = schema.getType(lifecycleInstallTypeQName);
      BeanSchemaBindingHelper.initBeanFactoryHandlers(lifecycleInstallType);
      lifecycleInstallType.setHandler(new InstallLifecycleBeanFactoryHandler());
      
      //Instantiate binding
      TypeBinding lifecycleInstantiateType = schema.getType(lifecycleInstantiateTypeQName);
      BeanSchemaBindingHelper.initBeanFactoryHandlers(lifecycleInstantiateType);
      lifecycleInstantiateType.setHandler(new InstantiateLifecycleBeanFactoryHandler());
      
      //Start binding
      TypeBinding lifecycleStartType = schema.getType(lifecycleStartTypeQName);
      BeanSchemaBindingHelper.initBeanFactoryHandlers(lifecycleStartType);
      lifecycleStartType.setHandler(new StartLifecycleBeanFactoryHandler());
      
      // TODO FIXME???
      BeanSchemaBinding20.initArtifacts(schema);
      
      return schema;
   }
   
   private static class AspectBeanFactoryHandler extends BeanFactoryHandler
   {
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new AspectBeanMetaDataFactory();
      }

      public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
      {
         super.attributes(o, elementName, element, attrs, nsCtx);

         AspectBeanMetaDataFactory factory = (AspectBeanMetaDataFactory) o;
         for (int i = 0; i < attrs.getLength(); ++i)
         {
            String localName = attrs.getLocalName(i);
            setAttribute(factory, localName, attrs.getValue(i));
         }
      }
      
      protected void setAttribute(AspectBeanMetaDataFactory factory, String localName, String attr)
      {
         if ("pointcut".equals(localName))
         {
            factory.setPointcut(attr);
         }
         else if ("manager-bean".equals(localName))
         {
            factory.setManagerBean(attr);
         }
         else if ("manager-property".equals(localName))
         {
            factory.setManagerProperty(attr);
         }
         else if ("method".equals(localName))
         {
            factory.setAdviceMethod(attr);
         }
      }
   }

   private static class LifecycleBeanFactoryHandler extends AspectBeanFactoryHandler
   {
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         throw new RuntimeException("Do not use <lifecycle> directly");
      }

      protected void setAttribute(AspectBeanMetaDataFactory factory, String localName, String attr)
      {
         super.setAttribute(factory, localName, attr);
         if ("classes".equals(localName))
         {
            ((LifecycleBeanMetaDataFactory)factory).setClasses(attr);
         }
      }
   }
   
   private static class ConfigureLifecycleBeanFactoryHandler extends LifecycleBeanFactoryHandler
   {
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new ConfigureLifecycleBeanMetaDataFactory();
      }
   }
   
   private static class CreateLifecycleBeanFactoryHandler extends LifecycleBeanFactoryHandler
   {
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new CreateLifecycleBeanMetaDataFactory();
      }
   }
   
   private static class DescribeLifecycleBeanFactoryHandler extends LifecycleBeanFactoryHandler
   {
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new DescribeLifecycleBeanMetaDataFactory();
      }
   }
   
   private static class InstallLifecycleBeanFactoryHandler extends LifecycleBeanFactoryHandler
   {
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new InstallLifecycleBeanMetaDataFactory();
      }
   }
   
   private static class InstantiateLifecycleBeanFactoryHandler extends LifecycleBeanFactoryHandler
   {
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new InstantiateLifecycleBeanMetaDataFactory();
      }
   }
   
   private static class StartLifecycleBeanFactoryHandler extends LifecycleBeanFactoryHandler
   {
      public Object startElement(Object parent, QName name, ElementBinding element)
      {
         return new StartLifecycleBeanMetaDataFactory();
      }
   }
}
