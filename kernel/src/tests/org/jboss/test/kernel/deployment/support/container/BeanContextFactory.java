/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.kernel.deployment.support.container;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.builder.ParameterMetaDataBuilderImpl;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.kernel.plugins.bootstrap.basic.KernelConstants;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;
import org.jboss.logging.Logger;

/**
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class BeanContextFactory<T> implements BeanMetaDataFactory, KernelControllerContextAware
{
   private static final Logger log = Logger.getLogger(BeanContextFactory.class);
   private String beanClass;
   private BeanContainer<T> container;
   private Set<TestInjectionMetaData> beanInjectionMD;
   private KernelConfigurator configurator;
   private Map<String, Set<TestInjectionMetaData>> interceptorInjectionMD =
      new HashMap<String, Set<TestInjectionMetaData>>();
   private List<String> interceptorNames;

   public String getBeanClass()
   {
      return beanClass;
   }
   public void setBeanClass(String beanClass)
   {
      this.beanClass = beanClass;
   }
   public BeanContainer<T> getContainer()
   {
      return container;
   }
   public void setContainer(BeanContainer<T> container)
   {
      this.container = container;
   }
   public Set<TestInjectionMetaData> getBeanInjectionMD()
   {
      return beanInjectionMD;
   }
   public void setBeanInjectionMD(Set<TestInjectionMetaData> beanInjectionMD)
   {
      this.beanInjectionMD = beanInjectionMD;
   }
   public Map<String, Set<TestInjectionMetaData>> getInterceptorInjectionMD()
   {
      return interceptorInjectionMD;
   }
   public void setInterceptorInjectionMD(
         Map<String, Set<TestInjectionMetaData>> interceptorInjectionMD)
   {
      this.interceptorInjectionMD = interceptorInjectionMD;
   }
   public List<String> getInterceptorNames()
   {
      return interceptorNames;
   }
   public void setInterceptorNames(List<String> interceptorNames)
   {
      this.interceptorNames = interceptorNames;
   }

   public void setKernelControllerContext(KernelControllerContext context)
         throws Exception
   {
      if(context != null)
      {
         configurator = context.getKernel().getConfigurator();
      }
   }
   public void unsetKernelControllerContext(KernelControllerContext context)
         throws Exception
   {
      configurator = null;
   }

   /**
    * Create the beans that make up the bean component
    */
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> beans = new ArrayList<BeanMetaData>();
      try
      {         
         // Create the BeanContext factory
         String contextName = "ContextFactory";
         BeanMetaDataBuilder contextBuilder = BeanMetaDataBuilder.createBuilder(contextName, BaseContext.class.getName());
         contextBuilder.setAccessMode(BeanAccessMode.ALL);
         // The BaseContext ctor
         contextBuilder.addConstructorParameter(BeanContainer.class.getName(), container);
         // BaseContext properties
         // BaseContext.instance
         String beanName = "BeanInstance";
         ValueMetaData beanInstance = contextBuilder.createInject(beanName);
         contextBuilder.addPropertyMetaData("instance", beanInstance);

         BeanMetaData beanContext = contextBuilder.getBeanMetaData();
         beans.add(beanContext);
   
         // Create the instance bean
         BeanMetaDataBuilder beanBuilder = BeanMetaDataBuilder.createBuilder(beanName, beanClass);
         beanBuilder.setAccessMode(BeanAccessMode.ALL);
         // For every injection target get the associated property
         if(beanInjectionMD != null)
         {
            addDependencyInjection(beanClass, beanInjectionMD, beanBuilder);
         }
         beans.add(beanBuilder.getBeanMetaData());
   
         // Create the interceptors
         int count = interceptorNames != null ? interceptorNames.size() : 0;
         for(int n = 0; n < count; n ++)
         {
            String name = "Interceptor:"+n;
            String iclass = interceptorNames.get(n);
            BeanMetaDataBuilder ibuilder = BeanMetaDataBuilder.createBuilder(name, iclass);
            ibuilder.addInstall("addInterceptor", contextName);
            ibuilder.addUninstall("removeInterceptor", contextName);
            Set<TestInjectionMetaData> injectMDs = interceptorInjectionMD.get(iclass);
            if(injectMDs != null)
               addDependencyInjection(beanClass, injectMDs, beanBuilder);
            BeanMetaData interceptor = ibuilder.getBeanMetaData();
            beans.add(interceptor);
         }
      }
      catch(Throwable t)
      {
         log.error("Failed to create component beans", t);
         throw new RuntimeException(t);
      }
      log.info("getBeans returning: "+beans);
      return beans;
   }

   private void addDependencyInjection(String clazzName,
         Set<TestInjectionMetaData> injectMDs,
         BeanMetaDataBuilder beanBuilder)
         throws Throwable
   {
      ClassLoader loader = getClass().getClassLoader();
      BeanInfo beanInfo = configurator.getBeanInfo(clazzName, loader);
      for(TestInjectionMetaData injectMD : injectMDs)
      {
         if(injectMD.getInjectionTargets() != null)
         {
            for(TestInjectionTargetMetaData targetMD : injectMD.getInjectionTargets())
            {
               ValueMetaData injectValue = beanBuilder.createInject(injectMD.getResolvedJndiName());
               String targetName = targetMD.getInjectionTargetName();
               PropertyInfo pinfo = beanInfo.getProperty(targetName);
               if(pinfo != null)
               {
                  beanBuilder.addPropertyMetaData(targetName, injectValue);
               }
               else
               {
                  log.warn("No property found for injection target:"+targetName+", on bean: "+beanClass);
               }
            }
         }
      }
   }
   private class PropertyMap extends HashMap<String, ValueMetaData> implements MetaDataVisitorNode
   {
      /** The serialVersionUID */
      private static final long serialVersionUID = -4295725682462294630L;

      public void initialVisit(MetaDataVisitor visitor)
      {
         visitor.initialVisit(this);
      }

      public void describeVisit(MetaDataVisitor vistor)
      {
         vistor.describeVisit(this);
      }

      public Iterator<? extends MetaDataVisitorNode> getChildren()
      {
         return values().iterator();
      }
   }
}
