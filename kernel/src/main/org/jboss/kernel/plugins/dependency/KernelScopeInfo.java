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
import org.jboss.logging.Logger;
import org.jboss.metadata.plugins.loader.memory.MemoryMetaDataLoader;
import org.jboss.metadata.spi.ComponentMutableMetaData;
import org.jboss.metadata.spi.loader.MutableMetaDataLoader;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.scope.CommonLevels;
import org.jboss.metadata.spi.scope.Scope;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.metadata.spi.signature.MethodSignature;
import org.jboss.metadata.spi.signature.FieldSignature;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.FieldInfo;

/**
 * KernelScopeInfo.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class KernelScopeInfo extends AbstractScopeInfo
{
   /** The log */
   private static final Logger log = Logger.getLogger(KernelScopeInfo.class);
   
   /** The bean metadata */
   private BeanMetaData beanMetaData;
   
   /**
    * Create a new KernelScopeInfo.
    * 
    * @param name the name
    * @param className the class name
    * @param beanMetaData the bean metadata
    */
   public KernelScopeInfo(Object name, String className, BeanMetaData beanMetaData)
   {
      super(name, className);
      this.beanMetaData = beanMetaData;
   }

   @Override
   public ScopeKey getScope()
   {
      // THIS IS A HACK - the scope originally gets initialise with a class name, we fix it to have the class
      ScopeKey key = super.getScope();
      Scope scope = key.getScope(CommonLevels.CLASS);
      if (scope == null)
         return key;
      Object qualifier = scope.getQualifier();
      if (qualifier instanceof Class)
         return key;

      String className = (String) qualifier;
      ClassLoader cl;
      try
      {
         cl = Configurator.getClassLoader(beanMetaData);
      }
      catch (Throwable t)
      {
         throw new RuntimeException("Error getting classloader for " + key, t);
      }
      Class<?> clazz;
      try
      {
         clazz = Class.forName(className, false, cl);
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException("Unable to load class: " + className + " for " + key, e);
      }
      key.addScope(new Scope(CommonLevels.CLASS, clazz));
      return key;
   }

   @Override
   public void addMetaData(MutableMetaDataRepository repository, ControllerContext context, MutableMetaDataLoader mutable)
   {
      if (context instanceof KernelControllerContext == false)
         return;
      KernelControllerContext theContext = (KernelControllerContext) context;
      addClassAnnotations(mutable, theContext);
      if (mutable instanceof ComponentMutableMetaData)
          addPropertyAnnotations((ComponentMutableMetaData) mutable, theContext);
      else
         log.warn("Unable to add properties to mutable metadata that does not support components: " + mutable + " for " + context.toShortString());
   }
   
   /**
    * Add class annotations
    * 
    * @param mutable the mutable metadata
    * @param context the context
    */
   private void addClassAnnotations(MutableMetaDataLoader mutable, KernelControllerContext context)
   {
      BeanMetaData beanMetaData = context.getBeanMetaData();
      if (beanMetaData != null)
      {
         ClassLoader cl;
         try
         {
            cl = Configurator.getClassLoader(beanMetaData);
         }
         catch(Throwable t)
         {
            throw new RuntimeException("Error getting classloader for " + beanMetaData.getName(), t);
         }
         addAnnotations(cl, mutable, beanMetaData.getAnnotations());
      }
   }

   /**
    * Add property annotations
    * 
    * @param mutable the mutable
    * @param context the kernel controller contex
    */
   private void addPropertyAnnotations(ComponentMutableMetaData mutable, KernelControllerContext context)
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

      ClassLoader cl;
      try
      {
         cl = Configurator.getClassLoader(beanMetaData);
      }
      catch(Throwable t)
      {
         throw new RuntimeException("Error getting classloader for metadata");
      }
      for (PropertyMetaData property : properties)
         addPropertyAnnotations(cl, mutable, property, beanInfo);
   }

   /**
    * Add property annotations
    * 
    * @param classloader the classloader
    * @param mutable the mutable
    * @param propertyMetaData the property
    * @param beanInfo the bean info
    */
   private void addPropertyAnnotations(ClassLoader classloader, ComponentMutableMetaData mutable, PropertyMetaData propertyMetaData, BeanInfo beanInfo)
   {
      Set<AnnotationMetaData> propertyAnnotations = propertyMetaData.getAnnotations();
      if (propertyAnnotations == null || propertyAnnotations.size() == 0)
         return;

      PropertyInfo propertyInfo = beanInfo.getProperty(propertyMetaData.getName());
      // method annotations
      MethodInfo methodInfo = propertyInfo.getGetter();
      if (methodInfo != null)
         addAnnotations(classloader, mutable, methodInfo, propertyAnnotations);
      methodInfo = propertyInfo.getSetter();
      if (methodInfo != null)
         addAnnotations(classloader, mutable, methodInfo, propertyAnnotations);
      // field annotations
      FieldInfo fieldInfo = propertyInfo.getFieldInfo();
      if (fieldInfo != null)
         addAnnotations(classloader, mutable, fieldInfo, propertyAnnotations);
   }
   
   /**
    * Add annotations for a method
    *
    * @param classloader the classloader
    * @param mutable the mutable metadata
    * @param methodInfo the method info
    * @param annotations the annotations
    */
   private void addAnnotations(ClassLoader classloader, ComponentMutableMetaData mutable, MethodInfo methodInfo, Set<AnnotationMetaData> annotations)
   {
      ScopeKey scope = new ScopeKey(CommonLevels.JOINPOINT_OVERRIDE, methodInfo.getName());
      MemoryMetaDataLoader loader = new MemoryMetaDataLoader(scope);
      addAnnotations(classloader, loader, annotations);
      mutable.addComponentMetaDataRetrieval(new MethodSignature(methodInfo), loader);
   }
   
   /**
    * Add annotations for a field
    *
    * @param classloader the classloader
    * @param mutable the mutable metadata
    * @param fieldInfo the field info
    * @param annotations the annotations
    */
   private void addAnnotations(ClassLoader classloader, ComponentMutableMetaData mutable, FieldInfo fieldInfo, Set<AnnotationMetaData> annotations)
   {
      ScopeKey scope = new ScopeKey(CommonLevels.JOINPOINT_OVERRIDE, fieldInfo.getName());
      MemoryMetaDataLoader loader = new MemoryMetaDataLoader(scope);
      addAnnotations(classloader, loader, annotations);
      mutable.addComponentMetaDataRetrieval(new FieldSignature(fieldInfo), loader);
   }

   /**
    * Add annotations to a mutable metadata
    *
    * @param classloader the classloader
    * @param mutable the mutable metadata
    * @param annotations the annotations
    */
   private void addAnnotations(ClassLoader classloader, MutableMetaDataLoader mutable, Set<AnnotationMetaData> annotations)
   {
      if (annotations == null || annotations.size() == 0)
         return;

      for (AnnotationMetaData annotation : annotations)
         mutable.addAnnotation(annotation.getAnnotationInstance(classloader));
   }
}
