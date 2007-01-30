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
package org.jboss.kernel.plugins.metadata.basic;

import java.util.ArrayList;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.kernel.plugins.metadata.AbstractKernelMetaDataRepository;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.plugins.context.AbstractMetaDataContext;
import org.jboss.metadata.plugins.loader.memory.MemoryMetaDataLoader;
import org.jboss.metadata.plugins.loader.reflection.AnnotatedElementMetaDataLoader;
import org.jboss.metadata.plugins.repository.basic.BasicMetaDataRepository;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.CommonLevels;
import org.jboss.metadata.spi.scope.Scope;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.metadata.spi.signature.MethodSignature;
import org.jboss.metadata.spi.stack.MetaDataStack;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * BasicKernelMetaDataRepository.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class BasicKernelMetaDataRepository extends AbstractKernelMetaDataRepository
{
   /**
    * Create a new BasicKernelMetaDataRepository.
    */
   public BasicKernelMetaDataRepository()
   {
      super(new BasicMetaDataRepository());
   }

   public MetaData getMetaData(KernelControllerContext context)
   {
      MutableMetaDataRepository repository = getMetaDataRepository();
      ScopeKey scope = context.getScope();
      MetaData metaData = repository.getMetaData(scope);
      if (metaData == null)
      {
         initMetaDataRetrieval(context);
         metaData = repository.getMetaData(scope);
      }
      return metaData;
   }

   public MetaDataRetrieval getMetaDataRetrieval(KernelControllerContext context)
   {
      MutableMetaDataRepository repository = getMetaDataRepository();
      ScopeKey scope = context.getScope();
      MetaDataRetrieval metaDataRetrieval = repository.getMetaDataRetrieval(scope);
      if (metaDataRetrieval == null)
         metaDataRetrieval = initMetaDataRetrieval(context);
      return metaDataRetrieval;
   }

   public void popMetaData(KernelControllerContext context)
   {
      MetaDataStack.pop();
   }

   public void addMetaData(KernelControllerContext context)
   {
      MutableMetaDataRepository repository = getMetaDataRepository();
      ScopeKey scope = getMutableScope(context);
      MemoryMetaDataLoader mutable = new MemoryMetaDataLoader(scope);
      repository.addMetaDataRetrieval(mutable);
      addClassAnnotations(mutable, context);
      addPropertyAnnotations(mutable, context);
   }

   public void removeMetaData(KernelControllerContext context)
   {
      MutableMetaDataRepository repository = getMetaDataRepository();
      // Remove the read only/full scope
      ScopeKey scope = context.getScope();
      repository.removeMetaDataRetrieval(scope);
      // Remove the mutable scope
      scope = getMutableScope(context);
      repository.removeMetaDataRetrieval(scope);
   }

   public ScopeKey getFullScope(KernelControllerContext context)
   {
      ScopeKey scope = ScopeKey.DEFAULT_SCOPE.clone();
      scope.addScope(CommonLevels.INSTANCE, context.getName().toString());
      BeanMetaData beanMetaData = context.getBeanMetaData();
      if (beanMetaData != null)
      {
         String bean = beanMetaData.getBean();
         if (bean != null)
            scope.addScope(CommonLevels.CLASS, bean);
      }
      return scope;
   }

   public ScopeKey getMutableScope(KernelControllerContext context)
   {
      return new ScopeKey(CommonLevels.INSTANCE, context.getName().toString());
   }

   /**
    * Initialise metadata retrieval
    * 
    * TODO lots more work
    * @param context the context
    * @return the retrieval
    */
   protected MetaDataRetrieval initMetaDataRetrieval(KernelControllerContext context)
   {
      MutableMetaDataRepository repository = getMetaDataRepository();
      ScopeKey scopeKey = context.getScope();
      ArrayList<MetaDataRetrieval> retrievals = new ArrayList<MetaDataRetrieval>();
      for (Scope scope : scopeKey.getScopes())
      {
         ScopeKey thisScope = new ScopeKey(scope);
         MetaDataRetrieval retrieval = repository.getMetaDataRetrieval(thisScope);
         if (retrieval == null)
         {
            if (scope.getScopeLevel() == CommonLevels.CLASS)
            {
               ClassLoader cl = Thread.currentThread().getContextClassLoader();
               try
               {
                  Class clazz = cl.loadClass(scope.getQualifier());
                  retrieval = new AnnotatedElementMetaDataLoader(clazz);
               }
               catch (ClassNotFoundException e)
               {
                  throw new RuntimeException("Unable to load class: " + scope.getQualifier(), e);
               }
            }
            else
            {
               retrieval = new MemoryMetaDataLoader(thisScope);
               repository.addMetaDataRetrieval(retrieval);
            }
         }
         retrievals.add(0, retrieval);
      }
      AbstractMetaDataContext metaDataContext = new AbstractMetaDataContext(null, retrievals);
      repository.addMetaDataRetrieval(metaDataContext);
      return metaDataContext;
   }
   
   /**
    * Add class annotations
    * 
    * @param mutable the mutable metadata
    * @param context the context
    */
   private void addClassAnnotations(MemoryMetaDataLoader mutable, KernelControllerContext context)
   {
      BeanMetaData beanMetaData = context.getBeanMetaData();
      if (beanMetaData != null)
         addAnnotations(mutable, beanMetaData.getAnnotations());
   }

   /**
    * Add property annotations
    * 
    * @param mutable the mutable
    * @param context the kernel controller contex
    */
   private void addPropertyAnnotations(MemoryMetaDataLoader mutable, KernelControllerContext context)
   {
      BeanMetaData beanMetaData = context.getBeanMetaData();
      if (beanMetaData == null)
         return;

      Set<PropertyMetaData> properties = beanMetaData.getProperties();

      if (properties == null || properties.size() == 0)
         return;

      BeanInfo beanInfo = context.getBeanInfo();
      if (beanInfo == null)
         return;
      
      for (PropertyMetaData property : properties)
         addPropertyAnnotations(mutable, property, beanInfo);
   }

   /**
    * Add property annotations
    * 
    * @param mutable the mutable
    * @param propertyMetaData the property
    * @param beanInfo the bean info
    */
   private void addPropertyAnnotations(MemoryMetaDataLoader mutable, PropertyMetaData propertyMetaData, BeanInfo beanInfo)
   {
      Set<AnnotationMetaData> propertyAnnotations = propertyMetaData.getAnnotations();
      if (propertyAnnotations == null || propertyAnnotations.size() == 0)
         return;

      Set<PropertyInfo> propertyInfos = beanInfo.getProperties();
      if (propertyInfos != null && propertyInfos.size() > 0)
      {
         for (PropertyInfo propertyInfo : propertyInfos)
         {
            if (propertyInfo.getName().equals(propertyMetaData.getName()))
            {
               MethodInfo methodInfo = propertyInfo.getGetter();
               if (methodInfo != null)
                  addAnnotations(mutable, methodInfo, propertyAnnotations);
               methodInfo = propertyInfo.getSetter();
               if (methodInfo != null)
                  addAnnotations(mutable, methodInfo, propertyAnnotations);
            }
         }
      }
   }
   
   /**
    * Add annotations for a method
    * 
    * @param mutable the mutable metadata
    * @param methodInfo the method info
    * @param annotations the annotations
    */
   private void addAnnotations(MemoryMetaDataLoader mutable, MethodInfo methodInfo, Set<AnnotationMetaData> annotations)
   {
      TypeInfo[] typeInfos = methodInfo.getParameterTypes();
      String[] paramTypes = new String[typeInfos.length];
      for (int i = 0; i < typeInfos.length; ++i)
         paramTypes[i] = typeInfos[i].getName();

      ScopeKey scope = new ScopeKey(CommonLevels.JOINPOINT_OVERRIDE, methodInfo.getName());
      MemoryMetaDataLoader loader = new MemoryMetaDataLoader(scope);
      addAnnotations(loader, annotations);
      mutable.addComponentMetaDataRetrieval(new MethodSignature(methodInfo.getName(), paramTypes), loader);
   }
   
   /**
    * Add annotations to a mutable metadata
    * 
    * @param mutable the mutable metadata
    * @param annotations the annotations
    */
   private void addAnnotations(MemoryMetaDataLoader mutable, Set<AnnotationMetaData> annotations)
   {
      if (annotations == null || annotations.size() == 0)
         return;

      for (AnnotationMetaData annotation : annotations)
         mutable.addAnnotation(annotation.getAnnotationInstance());
   }
}
