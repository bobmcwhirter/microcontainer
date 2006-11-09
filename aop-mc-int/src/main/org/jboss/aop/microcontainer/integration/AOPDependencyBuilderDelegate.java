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
package org.jboss.aop.microcontainer.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.aop.Advisor;
import org.jboss.aop.AspectManager;
import org.jboss.aop.ReflectiveAspectBinder;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.microcontainer.beans.ManagedAspectDefinition;
import org.jboss.aop.proxy.container.ContainerCache;
import org.jboss.aop.util.Advisable;
import org.jboss.aop.util.ClassInfoMethodHashing;
import org.jboss.classadapter.plugins.dependency.AbstractDependencyBuilder;
import org.jboss.classadapter.spi.ClassAdapter;
import org.jboss.classadapter.spi.Dependency;
import org.jboss.reflect.plugins.AnnotationValueFactory;
import org.jboss.reflect.plugins.introspection.IntrospectionTypeInfoFactoryImpl;
import org.jboss.reflect.spi.AnnotationInfo;
import org.jboss.reflect.spi.AnnotationValue;
import org.jboss.reflect.spi.ArrayInfo;
import org.jboss.reflect.spi.ArrayValue;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.StringValue;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.reflect.spi.Value;
import org.jboss.repository.spi.MetaDataContext;

/**
 * Used by the AOPDependencyBuilder once the AspectManager has been installed. Finds all managed aspects that apply 
 * to the bean and includes their dependencies as dependencies of the bean
 *  
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class AOPDependencyBuilderDelegate extends AbstractDependencyBuilder
{
   private static final String DEPENDENCY_CLASS_NAME = Dependency.class.getName();
   private static final String DEPENDENCY_NAME_ATTRIBUTE = "name";
   private static final IntrospectionTypeInfoFactoryImpl typeInfoFactory = new IntrospectionTypeInfoFactoryImpl();

   public List getDependencies(ClassAdapter classAdapter)
   {
      AspectManager manager = AspectManager.instance();
      try
      {
         ClassInfo classInfo = classAdapter.getClassInfo();
         String className = classInfo.getName();
         if (className != null)
         {
            if (manager.isNonAdvisableClassName(className))
            {
               return super.getDependencies(classAdapter);
            }
            
            MetaDataContext metaDataContext = classAdapter.getMetaDataContext();

            ClassLoader loader = classAdapter.getClassLoader();
            if (loader == null)
            {
               loader = Thread.currentThread().getContextClassLoader();
            }
            Class clazz = loader.loadClass(className);

            Advisor advisor;
            synchronized (ContainerCache.mapLock)
            {
               ContainerCache cache = ContainerCache.initialise(manager, clazz, metaDataContext);
               advisor = cache.getAdvisor();
            }
            
            ReflectiveAspectBinder binder = new ReflectiveAspectBinder(clazz, advisor);
            Set aspects = binder.getAspects();
            
            ArrayList<Object> depends = new ArrayList<Object>();
            if (aspects != null && aspects.size() > 0)
            {
               Iterator it = aspects.iterator();
               while (it.hasNext())
               {
                  AspectDefinition def = (AspectDefinition) it.next();
                  if (def instanceof ManagedAspectDefinition)
                  {
                     depends.add(def.getName());
                  }
               }
            }
            
            HashSet<Object> annotationDependencies = getAnnotationDependencies(classInfo, metaDataContext);
            depends.addAll(annotationDependencies);
            
            return depends;
         }
         return null;
         
      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }
   
   private HashSet<Object> getAnnotationDependencies(ClassInfo classInfo, MetaDataContext metaDataContext)
   {
      try
      {
         HashSet<Object> dependencies = new HashSet<Object>();
         getClassAnnotationDependencies(classInfo, metaDataContext, dependencies);
         getMethodAnnotationDependencies(classInfo, metaDataContext, dependencies);
         return dependencies;
      }
      catch (RuntimeException e)
      {
         throw e;
      }
      catch (Exception e)
      {
         throw new RuntimeException(e);
      }
   }

   private void getClassAnnotationDependencies(ClassInfo classInfo, MetaDataContext metaDataContext, HashSet<Object> dependencies) throws Exception
   {
      HashMap<String, ArrayList<String>> realMap = new HashMap<String, ArrayList<String>>();
      getRealClassAnnotationDependencies(classInfo, realMap);
      HashMap<String, ArrayList<String>> metaMap = new HashMap<String, ArrayList<String>>();
      getMetaDataContextClassAnnotationDependencies(metaDataContext, metaMap);
      addAllDependenciesToSet(dependencies, realMap, metaMap);
   }
   
   private void getRealClassAnnotationDependencies(ClassInfo classInfo, HashMap<String, ArrayList<String>> dependencies) throws Exception
   {
      AnnotationValue[] annotations = classInfo.getAnnotations();
      
      for (int i = 0 ; i < annotations.length ; i++)
      {
         getDependenciesForAnnotation(annotations[i].getType().getName(), annotations[i], dependencies);
      }
   }
   
   private void getMetaDataContextClassAnnotationDependencies(MetaDataContext metaDataContext, HashMap<String, ArrayList<String>> dependencies) throws Exception
   {
      if (metaDataContext != null)
      {
         for (Iterator it = metaDataContext.getAnnotations().iterator() ; it.hasNext() ; )
         {
            Object annotation = it.next();
            getDependenciesForMetaDataAnnotation(annotation, dependencies);
         }
      }
   }
   
   private void getMethodAnnotationDependencies(ClassInfo classInfo, MetaDataContext metaDataContext, HashSet<Object> dependencies) throws Exception
   {
      Map methodMap = ClassInfoMethodHashing.getMethodMap(classInfo);
      if (methodMap != null)
      {
         for (Iterator it = methodMap.values().iterator() ; it.hasNext() ; )
         {
            MethodInfo method = (MethodInfo)it.next();
            if (Advisable.isAdvisableMethod(method.getModifiers(), method.getName()))
            {
               HashMap<String, ArrayList<String>> classMap = new HashMap<String, ArrayList<String>>();
               getRealMethodAnnotationDependencies(method, classMap);
               HashMap<String, ArrayList<String>> overrideMap = new HashMap<String, ArrayList<String>>();
               getMetaDataContextMethodAnnotationDependencies(method, metaDataContext, overrideMap);
               addAllDependenciesToSet(dependencies, classMap, overrideMap);
            }
         }
      }
   }
   
   private void getRealMethodAnnotationDependencies(MethodInfo methodInfo, HashMap<String, ArrayList<String>> dependencies) throws Exception
   {
      AnnotationValue[] annotations = methodInfo.getAnnotations();
      if (annotations != null)
      {
         for (int i = 0 ; i < annotations.length ; i++)
         {
            getDependenciesForAnnotation(annotations[i].getType().getName(), annotations[i], dependencies);
         }
      }
   }
   
   private void getMetaDataContextMethodAnnotationDependencies(MethodInfo method, MetaDataContext metaDataContext, HashMap<String, ArrayList<String>> dependencies) throws Exception
   {
      if (metaDataContext != null)
      {
         long hash = ClassInfoMethodHashing.methodHash(method);
         List methodAnnotations = metaDataContext.getAnnotationsForMethod(hash);
         for (Iterator it = methodAnnotations.iterator() ; it.hasNext() ; )
         {
            Object annotation = it.next();
            getDependenciesForMetaDataAnnotation(annotation, dependencies);
         }
      }
   }
   
   private void getDependenciesForMetaDataAnnotation(Object annotation, HashMap<String, ArrayList<String>> dependencies) throws Exception
   {
      AnnotationInfo info;
      Class clazz = annotation.getClass().getInterfaces()[0];
      try
      {
         info = (AnnotationInfo)typeInfoFactory.getTypeInfo(clazz);
      }
      catch (RuntimeException e)
      {
         // AutoGenerated
         throw new RuntimeException("Error creating annotation for " + clazz.getName(), e);
      }
      AnnotationValue value = AnnotationValueFactory.createAnnotationValue(typeInfoFactory, typeInfoFactory, info, annotation);
      getDependenciesForAnnotation(info.getName(), value, dependencies);
   }
   
   private void getDependenciesForAnnotation(String topLevelAnnotationName, AnnotationValue annotation, HashMap<String, ArrayList<String>> dependencies)
   {
      if (annotation != null)
      {
         addAnnotationAttributeDependencies(topLevelAnnotationName, annotation, dependencies);
         
         AnnotationValue[] annotationAnnotations = annotation.getAnnotationType().getAnnotations();
         for (int i = 0 ; i < annotationAnnotations.length ; i++)
         {
            if (annotationAnnotations[i].getAnnotationType().getName().equals(DEPENDENCY_CLASS_NAME))
            {
               StringValue value = (StringValue)annotationAnnotations[i].getValue(DEPENDENCY_NAME_ATTRIBUTE);
               StringValue dependency = (StringValue)annotation.getValue(value.getValue());
               addDependency(topLevelAnnotationName, dependency, dependencies);
            }
         }
      }
   }
   
   private void addAnnotationAttributeDependencies(String topLevelAnnotationName, AnnotationValue annotation, HashMap<String, ArrayList<String>> dependencies)
   {
      MethodInfo[] attributes = annotation.getAnnotationType().getDeclaredMethods();
      if (attributes != null)
      {
         for (int i = 0 ; i < attributes.length ; i++)
         {
            Value value = annotation.getValue(attributes[i].getName());
            
            if (value instanceof AnnotationValue)
            {
               getDependenciesForAnnotation(topLevelAnnotationName, (AnnotationValue)value, dependencies);
            }
            else if (value instanceof ArrayValue)
            {
               ArrayValue arrVal = (ArrayValue)value;
               TypeInfo type = ((ArrayInfo)arrVal.getType()).getComponentType();
               if (type instanceof AnnotationInfo)
               {
                  Value[] values = arrVal.getValues();
                  for (int j = 0 ; j < values.length ; j++)
                  {
                     getDependenciesForAnnotation(topLevelAnnotationName, (AnnotationValue)values[j], dependencies);
                  }
               }
            }
         }
      }
   }
   
   private void addDependency(String topLevelAnnotationName, StringValue dependency, HashMap<String, ArrayList<String>> dependencies)
   {
      ArrayList<String> list = dependencies.get(topLevelAnnotationName);
      if (list == null)
      {
         list = new ArrayList<String>();
         dependencies.put(topLevelAnnotationName, list);
      }
      
      list.add(dependency.getValue());
   }

   private void addAllDependenciesToSet(HashSet<Object> dependencies, HashMap<String, ArrayList<String>> classMap, HashMap<String, ArrayList<String>> overrideMap)
   {
      HashMap<String, ArrayList<String>> dependencyMap = mergeClassAndOverrideMaps(classMap, overrideMap);
      if (dependencyMap.size() > 0)
      {
         for (ArrayList<String> deps : dependencyMap.values())
         {
            dependencies.addAll(deps);
         }
      }
   }
   
   private HashMap<String, ArrayList<String>> mergeClassAndOverrideMaps(HashMap<String, ArrayList<String>> classMap, HashMap<String, ArrayList<String>> overrideMap)
   {
      if (classMap.size() == 0 && overrideMap.size() == 0)
      {
         return classMap;
      }
      if (classMap.size() > 0 && overrideMap.size() == 0)
      {
         return classMap;
      }
      if (classMap.size() == 0 && overrideMap.size() > 0)
      {
         return overrideMap;
      }
      
      for (String key : overrideMap.keySet())
      {
         classMap.put(key, overrideMap.get(key));
      }
      return classMap;
   }
}
