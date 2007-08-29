/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.kernel.plugins.dependency;

import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.dependency.plugins.AbstractScopeInfo;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.plugins.loader.memory.MemoryMetaDataLoader;
import org.jboss.metadata.plugins.loader.reflection.AnnotatedElementMetaDataLoader;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.CommonLevels;
import org.jboss.metadata.spi.scope.Scope;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.metadata.spi.signature.MethodSignature;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * KernelScopeInfo.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class KernelScopeInfo extends AbstractScopeInfo
{
   /**
    * Create a new KernelScopeInfo.
    * 
    * @param name the name
    * @param className the class name
    */
   public KernelScopeInfo(Object name, String className)
   {
      super(name, className);
   }

   /**
    * Create a new KernelScopeInfo.
    * 
    * @param name the name
    */
   public KernelScopeInfo(Object name)
   {
      super(name);
   }

   /**
    * Create a new KernelScopeInfo.
    * 
    * @param key the scope
    * @param mutable the mutable scope
    */
   public KernelScopeInfo(ScopeKey key, ScopeKey mutable)
   {
      super(key, mutable);
   }

   @Override
   public MetaDataRetrieval initMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context, Scope scope)
   {
      if (scope.getScopeLevel() == CommonLevels.CLASS)
      {
         if (context instanceof KernelControllerContext == false)
            return null;
         KernelControllerContext theContext = (KernelControllerContext) context;
         BeanMetaData metaData = theContext.getBeanMetaData();
         ClassLoader cl = null;
         try
         {
            cl = Configurator.getClassLoader(metaData);
         }
         catch (Throwable t)
         {
            throw new RuntimeException("Error getting classloader for " + context.getName(), t);
         }
         try
         {
            Class<?> clazz = cl.loadClass(scope.getQualifier());
            return new AnnotatedElementMetaDataLoader(clazz);
         }
         catch (ClassNotFoundException e)
         {
            throw new RuntimeException("Unable to load class: " + scope.getQualifier(), e);
         }
      }
      return null;
   }

   @Override
   public void addMetaData(MutableMetaDataRepository repository, ControllerContext context, MemoryMetaDataLoader mutable)
   {
      if (context instanceof KernelControllerContext == false)
         return;
      KernelControllerContext theContext = (KernelControllerContext) context;
      addClassAnnotations(mutable, theContext);
      addPropertyAnnotations(mutable, theContext);
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
      {
         try
         {
            ClassLoader cl = Configurator.getClassLoader(beanMetaData);
            addAnnotations(cl, mutable, beanMetaData.getAnnotations());
         }
         catch(Throwable t)
         {
            throw new RuntimeException("Error getting classloader for metadata", t);
         }
      }
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
      
      try
      {
         ClassLoader cl = Configurator.getClassLoader(beanMetaData);
         for (PropertyMetaData property : properties)
            addPropertyAnnotations(cl, mutable, property, beanInfo);
      }
      catch(Throwable t)
      {
         throw new RuntimeException("Error getting classloader for metadata");
      }
   }

   /**
    * Add property annotations
    * 
    * @param classloader the classloader
    * @param mutable the mutable
    * @param propertyMetaData the property
    * @param beanInfo the bean info
    */
   private void addPropertyAnnotations(ClassLoader classloader, MemoryMetaDataLoader mutable, PropertyMetaData propertyMetaData, BeanInfo beanInfo)
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
                  addAnnotations(classloader, mutable, methodInfo, propertyAnnotations);
               methodInfo = propertyInfo.getSetter();
               if (methodInfo != null)
                  addAnnotations(classloader, mutable, methodInfo, propertyAnnotations);
            }
         }
      }
   }
   
   /**
    * Add annotations for a method
    *
    * @param classloader the classloader
    * @param mutable the mutable metadata
    * @param methodInfo the method info
    * @param annotations the annotations
    */
   private void addAnnotations(ClassLoader classloader, MemoryMetaDataLoader mutable, MethodInfo methodInfo, Set<AnnotationMetaData> annotations)
   {
      TypeInfo[] typeInfos = methodInfo.getParameterTypes();
      String[] paramTypes = new String[typeInfos.length];
      for (int i = 0; i < typeInfos.length; ++i)
         paramTypes[i] = typeInfos[i].getName();

      ScopeKey scope = new ScopeKey(CommonLevels.JOINPOINT_OVERRIDE, methodInfo.getName());
      MemoryMetaDataLoader loader = new MemoryMetaDataLoader(scope);
      addAnnotations(classloader, loader, annotations);
      mutable.addComponentMetaDataRetrieval(new MethodSignature(methodInfo.getName(), paramTypes), loader);
   }
   
   /**
    * Add annotations to a mutable metadata
    *
    * @param classloader the classloader
    * @param mutable the mutable metadata
    * @param annotations the annotations
    */
   private void addAnnotations(ClassLoader classloader, MemoryMetaDataLoader mutable, Set<AnnotationMetaData> annotations)
   {
      if (annotations == null || annotations.size() == 0)
         return;

      for (AnnotationMetaData annotation : annotations)
         mutable.addAnnotation(annotation.getAnnotationInstance(classloader));
   }
}
