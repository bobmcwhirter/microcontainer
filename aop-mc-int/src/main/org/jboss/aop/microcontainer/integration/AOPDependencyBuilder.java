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
package org.jboss.aop.microcontainer.integration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.jboss.aop.Advisor;
import org.jboss.aop.AspectManager;
import org.jboss.aop.ReflectiveAspectBinder;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.microcontainer.beans.AspectManagerFactory;
import org.jboss.aop.microcontainer.beans.ManagedAspectDefinition;
import org.jboss.aop.microcontainer.lifecycle.LifecycleCallbackDefinition;
import org.jboss.aop.proxy.container.ContainerCache;
import org.jboss.aop.util.Advisable;
import org.jboss.aop.util.ClassInfoMethodHashing;
import org.jboss.classadapter.plugins.dependency.AbstractDependencyBuilder;
import org.jboss.classadapter.spi.ClassAdapter;
import org.jboss.classadapter.spi.Dependency;
import org.jboss.classadapter.spi.DependencyBuilderListItem;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.signature.MethodSignature;
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

/**
 * Finds all managed aspects that apply
 * to the bean and includes their dependencies as dependencies of the bean
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AOPDependencyBuilder extends AbstractDependencyBuilder
{
   private static final String DEPENDENCY_CLASS_NAME = Dependency.class.getName();
   private static final String DEPENDENCY_NAME_ATTRIBUTE = "name";
   private static final IntrospectionTypeInfoFactoryImpl typeInfoFactory = new IntrospectionTypeInfoFactoryImpl();

   public List<DependencyBuilderListItem> getDependencies(ClassAdapter classAdapter, MetaData metaData)
   {
      AspectManager manager = AspectManagerFactory.getAspectManager(metaData);
      try
      {
         ClassInfo classInfo = classAdapter.getClassInfo();
         String className = classInfo.getName();
         if (className != null)
         {
            ClassLoader loader = classAdapter.getClassLoader();
            if (loader == null)
            {
               loader = Thread.currentThread().getContextClassLoader();
            }
            Class clazz = loader.loadClass(className);

            Advisor advisor;
            synchronized (ContainerCache.mapLock)
            {
               ContainerCache cache = ContainerCache.initialise(manager, clazz, metaData, true);
               advisor = cache.getAdvisor();
            }
            ReflectiveAspectBinder binder = new ReflectiveAspectBinder(clazz, advisor);
            Set aspects = binder.getAspects();

            ArrayList<DependencyBuilderListItem> depends = new ArrayList<DependencyBuilderListItem>();
            if (aspects != null && aspects.size() > 0)
            {
               Iterator it = aspects.iterator();
               while (it.hasNext())
               {
                  AspectDefinition def = (AspectDefinition) it.next();
                  if (def instanceof ManagedAspectDefinition)
                  {
                     depends.add(new AspectDependencyBuilderListItem(def.getName()));
                  }
               }
            }

            Map<Object, Set<LifecycleCallbackDefinition>> lifecycleCallbacks = binder.getLifecycleCallbacks();
            if (lifecycleCallbacks != null && lifecycleCallbacks.size() > 0)
            {
               for (Entry<Object, Set<LifecycleCallbackDefinition>> states : lifecycleCallbacks.entrySet())
               {
                  for (LifecycleCallbackDefinition callback : states.getValue())
                  {
                     depends.add(new LifecycleAspectDependencyBuilderListItem(
                           callback.getBean(), (ControllerState)states.getKey(), callback.getInstallMethod(), callback.getUninstallMethod()));
                  }
               }
            }

            HashSet<String> annotationDependencies = getAnnotationDependencies(classInfo, metaData);
            for (String dependency : annotationDependencies)
            {
               depends.add(new AnnotationDependencyBuilderListItem(dependency));
            }
            return depends;
         }
         return null;

      }
      catch (ClassNotFoundException e)
      {
         throw new RuntimeException(e);
      }
   }

   private HashSet<String> getAnnotationDependencies(ClassInfo classInfo, MetaData metaData)
   {
      try
      {
         HashSet<String> dependencies = new HashSet<String>();
         getClassAnnotationDependencies(classInfo, metaData, dependencies);
         getMethodAnnotationDependencies(classInfo, metaData, dependencies);
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

   private void getClassAnnotationDependencies(ClassInfo classInfo, MetaData metaData, HashSet<String> dependencies) throws Exception
   {
      HashMap<String, ArrayList<String>> realMap = new HashMap<String, ArrayList<String>>();
      getRealClassAnnotationDependencies(classInfo, realMap);
      HashMap<String, ArrayList<String>> metaMap = new HashMap<String, ArrayList<String>>();
      getMetaDataClassAnnotationDependencies(metaData, metaMap);
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

   private void getMetaDataClassAnnotationDependencies(MetaData metaData, HashMap<String, ArrayList<String>> dependencies) throws Exception
   {
      if (metaData != null)
      {
         for (Object annotation : metaData.getAnnotations())
         {
            getDependenciesForMetaDataAnnotation(annotation, dependencies);
         }
      }
   }

   private void getMethodAnnotationDependencies(ClassInfo classInfo, MetaData metaData, HashSet<String> dependencies) throws Exception
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
               getMetaDataMethodAnnotationDependencies(method, metaData, overrideMap);
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

   private void getMetaDataMethodAnnotationDependencies(MethodInfo method, MetaData metaData, HashMap<String, ArrayList<String>> dependencies) throws Exception
   {
      if (metaData != null)
      {
         TypeInfo[] typeInfos = method.getParameterTypes();
         Class[] params = new Class[typeInfos.length];
         for (int i = 0; i < typeInfos.length; ++i)
            params[i] = typeInfos[i].getType();
         MetaData methodMetaData = metaData.getComponentMetaData(new MethodSignature(method.getName(), params));
         if (methodMetaData != null)
         {
            for (Object annotation : methodMetaData.getAnnotations())
            {
               getDependenciesForMetaDataAnnotation(annotation, dependencies);
            }
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

   private void addAllDependenciesToSet(HashSet<String> dependencies, HashMap<String, ArrayList<String>> classMap, HashMap<String, ArrayList<String>> overrideMap)
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
