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
package org.jboss.kernel.plugins.annotations;

import java.lang.annotation.Annotation;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.api.annotations.MCAnnotations;
import org.jboss.logging.Logger;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.signature.ConstructorSignature;
import org.jboss.metadata.spi.signature.FieldSignature;
import org.jboss.metadata.spi.signature.MethodSignature;
import org.jboss.metadata.spi.signature.Signature;
import org.jboss.reflect.spi.AnnotatedInfo;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.ConstructorInfo;
import org.jboss.reflect.spi.FieldInfo;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.TypeInfoFactory;
import org.jboss.reflect.spi.TypeInfo;

/**
 * Common bean annotation handler.
 *
 * @param <T> exact annotation plugin type
 * @param <U> exact handle type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class CommonAnnotationAdapter<T extends MetaDataAnnotationPlugin<?, ?>, U>
{
   /** The log */
   protected Logger log = Logger.getLogger(getClass());

   /** The annotation plugins */
   private final Map<ElementType, Set<T>> pluginsMap = new HashMap<ElementType, Set<T>>();

   /** The property annotation plugin filter */
   private static final AnnotationPluginFilter PROPERTY_FILTER = new PropertyAnnotationPluginFilter();

   /** The method annotation plugin filter */
   private static final AnnotationPluginFilter METHOD_FILTER = new MethodAnnotationPluginFilter();



   /**
    * Add the annotation plugin.
    * Breaks down the plugin usage into
    * different ElementType support collections.
    *
    * @param plugin the annotation plugin
    */
   public void addAnnotationPlugin(T plugin)
   {
      if (plugin == null)
         throw new IllegalArgumentException("Null plugin.");

      Class<? extends Annotation> annotation = plugin.getAnnotation();
      if (annotation == null)
         throw new IllegalArgumentException("Null annotation class: " + plugin);

      if (annotation.getAnnotation(Target.class) == null)
         log.warn("Annotation " + annotation + " missing @Target annotation!");
      if (annotation.getAnnotation(Retention.class) == null)
         log.warn("Annotation " + annotation + " missing @Retention annotation!");

      Set<ElementType> supported = plugin.getSupportedTypes();
      if (supported == null || supported.isEmpty())
         throw new IllegalArgumentException("Null or empty support types: " + plugin);

      synchronized (pluginsMap)
      {
         for (ElementType type : supported)
         {
            Set<T> plugins = pluginsMap.get(type);
            if (plugins == null)
            {
               plugins = new HashSet<T>();
               pluginsMap.put(type, plugins);
            }
            plugins.add(plugin);
         }
      }
   }

   /**
    * Remove the plugin.
    *
    * @param plugin the annotation plugin
    */
   public void removeAnnotationPlugin(T plugin)
   {
      if (plugin == null)
         return;

      Set<ElementType> supported = plugin.getSupportedTypes();
      if (supported == null || supported.isEmpty())
         throw new IllegalArgumentException("Null or empty support types: " + plugin);

      synchronized (pluginsMap)
      {
         for (ElementType type : supported)
         {
            Set<T> plugins = pluginsMap.get(type);
            if (plugins != null)
            {
               plugins.remove(plugin);

               if (plugins.isEmpty())
                  pluginsMap.remove(type);
            }
         }
      }
   }

   /**
    * Apply plugin.
    *
    * @param plugin the plugin
    * @param info the bean info
    * @param retrieval the metadata
    * @param handle the handle
    * @throws Throwable for any error
    */
   protected abstract void applyPlugin(T plugin, AnnotatedInfo info, MetaData retrieval, U handle) throws Throwable;

   /**
    * Clean plugin.
    *
    * @param plugin the plugin
    * @param info the bean info
    * @param retrieval the metadata
    * @param handle the handle
    * @throws Throwable for any error
    */
   protected abstract void cleanPlugin(T plugin, AnnotatedInfo info, MetaData retrieval, U handle) throws Throwable;

   /**
    * Get the name from handle.
    *
    * @param handle the handle
    * @return handle's name
    */
   protected abstract Object getName(U handle);

   /**
    * Get plugins.
    *
    * @param type the element type to match
    * @param filter possible plugins filter
    * @param annotationClasses possible annotations
    * @return iterable matching plugins
    */
   protected Iterable<T> getPlugins(ElementType type, AnnotationPluginFilter filter, Collection<Class<? extends Annotation>> annotationClasses)
   {
      Set<T> plugins;
      synchronized (pluginsMap)
      {
         plugins = pluginsMap.get(type);
      }
      
      if (plugins == null || plugins.isEmpty())
         return Collections.emptySet();

      if (filter != null)
      {
         List<T> result = new ArrayList<T>();
         for (T plugin : plugins)
         {
            if (filter.accept(plugin) && (annotationClasses == null || annotationClasses.contains(plugin.getAnnotation())))
            {
               result.add(plugin);
            }
         }
         return result;
      }
      else if (annotationClasses != null)
      {
         List<T> result = new ArrayList<T>();
         for (T plugin : plugins)
         {
            if (annotationClasses.contains(plugin.getAnnotation()))
            {
               result.add(plugin);
            }
         }
         return result;
      }
      else
      {
         return plugins;
      }
   }

   /**
    * Handle apply or cleanup of annotations.
    *
    * @param info the bean info
    * @param retrieval the metadata
    * @param handle the handle to use in a plugin
    * @param isApplyPhase is this apply phase
    * @throws Throwable for any error
    */
   protected void handleAnnotations(BeanInfo info, MetaData retrieval, U handle, boolean isApplyPhase) throws Throwable
   {
      if (info == null)
         throw new IllegalArgumentException("Null bean info.");
      if (retrieval == null)
         throw new IllegalArgumentException("Null metadata.");
      if (handle == null)
         throw new IllegalArgumentException("Null handle.");

      boolean trace = log.isTraceEnabled();
      if (trace)
         log.trace(getName(handle) + " apply annotations");

      // limit the annotations
      MCAnnotations annotations = retrieval.getAnnotation(MCAnnotations.class);
      Collection<Class<? extends Annotation>> annotationClasses = (annotations != null ? Arrays.asList(annotations.value()) : null);

      // class
      ClassInfo classInfo = info.getClassInfo();
      for(T plugin : getPlugins(ElementType.TYPE, null, annotationClasses))
      {
         if (isApplyPhase)
            applyPlugin(plugin, classInfo, retrieval, handle);
         else
            cleanPlugin(plugin, classInfo, retrieval, handle);
      }

      // constructors
      Set<ConstructorInfo> constructors = info.getConstructors();
      if (constructors != null && constructors.isEmpty() == false)
      {
         for(ConstructorInfo ci : constructors)
         {
            Signature cis = new ConstructorSignature(ci);
            MetaData cmdr = retrieval.getComponentMetaData(cis);
            if (cmdr != null)
            {
               for(T plugin : getPlugins(ElementType.CONSTRUCTOR, null, annotationClasses))
               {
                  if (isApplyPhase)
                     applyPlugin(plugin, ci, cmdr, handle);
                  else
                     cleanPlugin(plugin, ci, cmdr, handle);
               }
            }
            else if (trace)
               log.trace("No annotations for " + ci);
         }
      }
      else if (trace)
         log.trace("No constructors");

      // properties
      Set<MethodInfo> visitedMethods = new HashSet<MethodInfo>();
      Set<PropertyInfo> properties = info.getProperties();
      if (properties != null && properties.isEmpty() == false)
      {
         for(PropertyInfo pi : properties)
         {
            FieldInfo field = pi.getFieldInfo();
            if (field != null)
            {
               Signature sis = new FieldSignature(field);
               MetaData cmdr = retrieval.getComponentMetaData(sis);
               if (cmdr != null)
               {
                  for(T plugin : getPlugins(ElementType.FIELD, null, annotationClasses))
                  {
                     if (isApplyPhase)
                        applyPlugin(plugin, field, cmdr, handle);
                     else
                        cleanPlugin(plugin, field, cmdr, handle);
                  }
               }
               else if (trace)
                  log.trace("No annotations for field " + field.getName());
            }
            // apply setter and getter as well - if they exist
            handleMethod(retrieval, handle, isApplyPhase, trace, visitedMethods, pi, pi.getSetter(), "setter", annotationClasses);
            handleMethod(retrieval, handle, isApplyPhase, trace, visitedMethods, pi, pi.getGetter(), "getter", annotationClasses);
         }
      }
      else if (trace)
         log.trace("No properties");

      // get Object's class info - it's cached so it shouldn't take much
      TypeInfoFactory tif = classInfo.getTypeInfoFactory();
      TypeInfo objectTI = tif.getTypeInfo(Object.class);

      // method plugins
      Iterable<T> methodPlugins = null;
      // methods
      Set<MethodInfo> methods = info.getMethods();
      if (methods != null && methods.isEmpty() == false)
      {
         for(MethodInfo mi : methods)
         {
            ClassInfo declaringCI = mi.getDeclaringClass();
            // direct == check is OK
            if (declaringCI != objectTI && visitedMethods.contains(mi) == false)
            {
               Signature mis = new MethodSignature(mi);
               MetaData cmdr = retrieval.getComponentMetaData(mis);
               if (cmdr != null)
               {
                  methodPlugins = getPlugins(ElementType.METHOD, METHOD_FILTER, annotationClasses);
                  for(T plugin : methodPlugins)
                  {
                     if (isApplyPhase)
                        applyPlugin(plugin, mi, cmdr, handle);
                     else
                        cleanPlugin(plugin, mi, cmdr, handle);
                  }
               }
               else if (trace)
                  log.trace("No annotations for " + mi);
            }
         }
      }
      else if (trace)
         log.trace("No methods");

      // static methods
      MethodInfo[] staticMethods = getStaticMethods(classInfo);
      if (staticMethods != null && staticMethods.length != 0)
      {
         for(MethodInfo smi : staticMethods)
         {
            if (smi.isStatic() && smi.isPublic())
            {
               Signature mis = new MethodSignature(smi);
               MetaData cmdr = retrieval.getComponentMetaData(mis);
               if (cmdr != null)
               {
                  if (methodPlugins == null)
                     methodPlugins = getPlugins(ElementType.METHOD, METHOD_FILTER, annotationClasses);
                  
                  for(T plugin : methodPlugins)
                  {
                     if (isApplyPhase)
                        applyPlugin(plugin, smi, cmdr, handle);
                     else
                        cleanPlugin(plugin, smi, cmdr, handle);
                  }
               }
               else if (trace)
                  log.trace("No annotations for " + smi);
            }
         }
      }
      else if (trace)
         log.trace("No static methods");

      // fields - if accessible - are already handled with propertys
   }

   /**
    * Handle setter or getter on property.
    *
    * @param retrieval the metadata
    * @param handle the handle
    * @param isApplyPhase is apply phase
    * @param trace is trace enabled
    * @param visitedMethods visited methods
    * @param pi the property info
    * @param method the method info
    * @param type method type
    * @param annotationClasses the possible annotation classes
    * @throws Throwable for any error
    */
   protected void handleMethod(
         MetaData retrieval,
         U handle,
         boolean isApplyPhase,
         boolean trace,
         Set<MethodInfo> visitedMethods,
         PropertyInfo pi,
         MethodInfo method,
         String type,
         Collection<Class<? extends Annotation>> annotationClasses)
         throws Throwable
   {
      if (method == null)
         return;
      
      visitedMethods.add(method);
      Signature sis = new MethodSignature(method);
      MetaData cmdr = retrieval.getComponentMetaData(sis);
      if (cmdr != null)
      {
         for(T plugin : getPlugins(ElementType.METHOD, PROPERTY_FILTER, annotationClasses))
         {
            if (isApplyPhase)
               applyPlugin(plugin, pi, cmdr, handle);
            else
               cleanPlugin(plugin, pi, cmdr, handle);
         }
      }
      else if (trace)
         log.trace("No annotations for " + type + ": " + pi.getName());
   }

   /**
    * Get the static methods of class info.
    *
    * @param classInfo the class info
    * @return the static methods
    */
   protected MethodInfo[] getStaticMethods(ClassInfo classInfo)
   {
      return classInfo.getDeclaredMethods();
   }
}