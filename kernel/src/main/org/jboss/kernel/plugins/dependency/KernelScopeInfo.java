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

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.dependency.plugins.AbstractScopeInfo;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.logging.Logger;
import org.jboss.metadata.spi.ComponentMutableMetaData;
import org.jboss.metadata.spi.loader.MutableMetaDataLoader;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.CommonLevels;
import org.jboss.metadata.spi.scope.Scope;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.metadata.spi.signature.FieldSignature;
import org.jboss.metadata.spi.signature.MethodSignature;
import org.jboss.metadata.spi.signature.Signature;
import org.jboss.reflect.spi.FieldInfo;
import org.jboss.reflect.spi.MethodInfo;

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
   public void updateMetaData(MutableMetaDataRepository repository, ControllerContext context, MutableMetaDataLoader mutable, boolean add)
   {
      if (context instanceof KernelControllerContext == false)
         return;
      KernelControllerContext theContext = (KernelControllerContext) context;
      updateClassAnnotations(repository, mutable, theContext, add);
      if (mutable instanceof ComponentMutableMetaData)
          updatePropertyAnnotations(repository, (ComponentMutableMetaData) mutable, theContext, add);
      else if (add == true)
         log.warn("Unable to add properties to mutable metadata that does not support components: " + mutable + " for " + context.toShortString());
   }
   
   /**
    * Update class annotations
    * 
    * @param repository the repository
    * @param mutable the mutable metadata
    * @param context the context
    * @param add true for add, false for remove
    */
   private void updateClassAnnotations(MutableMetaDataRepository repository, MutableMetaDataLoader mutable, KernelControllerContext context, boolean add)
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
         updateAnnotations(repository, cl, mutable, context, beanMetaData.getAnnotations(), add);
      }
   }

   /**
    * Update property annotations
    * 
    * @param repository the repository
    * @param mutable the mutable
    * @param context the kernel controller contex
    * @param add true for add, false for remove
    */
   private void updatePropertyAnnotations(MutableMetaDataRepository repository, ComponentMutableMetaData mutable, KernelControllerContext context, boolean add)
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
         updatePropertyAnnotations(repository, cl, mutable, context, property, beanInfo, add);
   }

   /**
    * Update property annotations
    * 
    * @param repository the repository
    * @param classloader the classloader
    * @param mutable the mutable
    * @param context the context
    * @param propertyMetaData the property
    * @param beanInfo the bean info
    * @param add true for add, false for remove
    */
   private void updatePropertyAnnotations(MutableMetaDataRepository repository, ClassLoader classloader, ComponentMutableMetaData mutable, KernelControllerContext context, PropertyMetaData propertyMetaData, BeanInfo beanInfo, boolean add)
   {
      Set<AnnotationMetaData> propertyAnnotations = propertyMetaData.getAnnotations();
      if (propertyAnnotations == null || propertyAnnotations.size() == 0)
         return;

      PropertyInfo propertyInfo = beanInfo.getProperty(propertyMetaData.getName());
      // method annotations
      MethodInfo methodInfo = propertyInfo.getGetter();
      if (methodInfo != null)
         updateAnnotations(repository, classloader, mutable, context, methodInfo, propertyAnnotations, add);
      methodInfo = propertyInfo.getSetter();
      if (methodInfo != null)
         updateAnnotations(repository, classloader, mutable, context, methodInfo, propertyAnnotations, add);
      // field annotations
      FieldInfo fieldInfo = propertyInfo.getFieldInfo();
      if (fieldInfo != null)
         updateAnnotations(repository, classloader, mutable, context, fieldInfo, propertyAnnotations, add);
   }
   
   /**
    * Update component annotations
    *
    * @param repository the repository
    * @param classloader the classloader
    * @param component the component metadata
    * @param context the context
    * @param signature the signature
    * @param scope the scope
    * @param annotations the annotations
    * @param add true for add, false for remove
    */
   private void updateAnnotations(MutableMetaDataRepository repository, ClassLoader classloader, ComponentMutableMetaData component, KernelControllerContext context, Signature signature, ScopeKey scope, Set<AnnotationMetaData> annotations, boolean add)
   {
      MetaDataRetrieval retrieval = ((MetaDataRetrieval) component).getComponentMetaDataRetrieval(signature);
      MutableMetaDataLoader mutable = null;
      if (retrieval != null)
      {
         mutable = getMutableMetaDataLoader(retrieval);
         if (mutable == null)
         {
            if (add)
               log.warn("MetaData is not mutable with signature: " + signature + " for " + context.toShortString());
            return;
         }
      }
      else if (add)
      {
         mutable = initMutableMetaDataRetrieval(repository, context, scope);
         component.addComponentMetaDataRetrieval(signature, mutable);
      }
      else
      {
         return;
      }
      updateAnnotations(repository, classloader, mutable, context, annotations, add);
   }
   
   /**
    * Update annotations for a method
    *
    * @param repository the repository
    * @param classloader the classloader
    * @param component the mutable metadata
    * @param context the context
    * @param methodInfo the method info
    * @param annotations the annotations
    * @param add true for add, false for remove
    */
   private void updateAnnotations(MutableMetaDataRepository repository, ClassLoader classloader, ComponentMutableMetaData component, KernelControllerContext context, MethodInfo methodInfo, Set<AnnotationMetaData> annotations, boolean add)
   {
      if (annotations == null || annotations.isEmpty())
         return;
      Signature signature = new MethodSignature(methodInfo);
      ScopeKey scope = new ScopeKey(CommonLevels.JOINPOINT_OVERRIDE, methodInfo.getName());
      updateAnnotations(repository, classloader, component, context, signature, scope, annotations, add);
   }
   
   /**
    * Add annotations for a field
    *
    * @param repository the repository
    * @param classloader the classloader
    * @param component the mutable metadata
    * @param context the context
    * @param fieldInfo the field info
    * @param annotations the annotations
    * @param add true for add, false for remove
    */
   private void updateAnnotations(MutableMetaDataRepository repository, ClassLoader classloader, ComponentMutableMetaData component, KernelControllerContext context, FieldInfo fieldInfo, Set<AnnotationMetaData> annotations, boolean add)
   {
      if (annotations == null || annotations.isEmpty())
         return;
      Signature signature = new FieldSignature(fieldInfo);
      ScopeKey scope = new ScopeKey(CommonLevels.JOINPOINT_OVERRIDE, fieldInfo.getName());
      updateAnnotations(repository, classloader, component, context, signature, scope, annotations, add);
   }

   /**
    * Add annotations to a mutable metadata
    *
    * @param repository the repository
    * @param classloader the classloader
    * @param mutable the mutable metadata
    * @param context the context
    * @param annotations the annotations
    * @param add true for add, false for remove
    */
   private void updateAnnotations(MutableMetaDataRepository repository, ClassLoader classloader, MutableMetaDataLoader mutable, KernelControllerContext context, Set<AnnotationMetaData> annotations, boolean add)
   {
      if (annotations == null || annotations.size() == 0)
         return;
      for (AnnotationMetaData annotation : annotations)
      {
         
         if (add)
         {
            Annotation annotationInstance = annotation.getAnnotationInstance(classloader); 
            mutable.addAnnotation(annotationInstance);
         }
         else
         {
            Annotation annotationInstance = annotation.getAnnotationInstance();
            // Null means we never constructed it in the first place 
            if (annotationInstance != null)
               mutable.removeAnnotation(annotationInstance.annotationType());
         }
      }
   }

   @Override
   protected MetaDataRetrieval createMetaDataRetrieval(ControllerContext context, List<MetaDataRetrieval> retrievals)
   {
      Controller controller = context.getController();
      if (controller instanceof KernelController)
      {
         KernelController kernelController = (KernelController) controller;
         MetaDataRetrieval result = kernelController.getKernel().getMetaDataRepository().createMetaDataRetrieval(context, retrievals);
         if (result != null)
            return result;
      }
      return super.createMetaDataRetrieval(context, retrievals);
   }
}
