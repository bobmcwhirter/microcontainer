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

import java.util.Set;
import java.util.HashSet;
import java.lang.annotation.Annotation;
import java.lang.annotation.Target;
import java.lang.annotation.Retention;
import java.lang.annotation.ElementType;

import org.jboss.logging.Logger;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.signature.Signature;
import org.jboss.metadata.spi.signature.ConstructorSignature;
import org.jboss.metadata.spi.signature.MethodSignature;
import org.jboss.metadata.spi.signature.FieldSignature;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.ConstructorInfo;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.FieldInfo;

/**
 * Abstract bean annotation handler.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@SuppressWarnings("unchecked")
public abstract class AbstractBeanAnnotationAdapter implements BeanAnnotationAdapter
{
   protected Logger log = Logger.getLogger(AbstractBeanAnnotationAdapter.class);

   protected Set<AnnotationPlugin> classAnnotationPlugins = new HashSet<AnnotationPlugin>();
   protected Set<AnnotationPlugin> constructorAnnotationPlugins = new HashSet<AnnotationPlugin>();
   protected Set<AnnotationPlugin> propertyAnnotationPlugins = new HashSet<AnnotationPlugin>();
   protected Set<AnnotationPlugin> methodAnnotationPlugins = new HashSet<AnnotationPlugin>();
   protected Set<AnnotationPlugin> fieldAnnotationPlugins = new HashSet<AnnotationPlugin>();

   /**
    * Add the annotation plugin.
    * Breaks down the plugin usage into
    * different ElementType support collections.
    *
    * @param plugin the annotation plugin
    */
   protected void addAnnotationPlugin(AnnotationPlugin plugin)
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

      Set supported = plugin.getSupportedTypes();
      if (supported == null || supported.isEmpty())
         throw new IllegalArgumentException("Null or empty support types: " + plugin);

      if (supported.contains(ElementType.TYPE))
      {
         classAnnotationPlugins.add(plugin);
      }
      if (supported.contains(ElementType.CONSTRUCTOR))
      {
         constructorAnnotationPlugins.add(plugin);
      }
      if (supported.contains(ElementType.METHOD))
      {
         if (plugin instanceof PropertyAware)
            propertyAnnotationPlugins.add(plugin);
         else
            methodAnnotationPlugins.add(plugin);
      }
      if (supported.contains(ElementType.FIELD))
      {
         fieldAnnotationPlugins.add(plugin);
      }
   }

   public void applyAnnotations(MetaDataVisitor visitor) throws Throwable
   {
      handleAnnotations(visitor, true);
   }

   public void cleanAnnotations(MetaDataVisitor visitor) throws Throwable
   {
      handleAnnotations(visitor, false);
   }

   /**
    * Handle apply or cleanup of annotations.
    *
    * @param visitor the metadata visitor
    * @param isApplyPhase is this apply phase
    * @throws Throwable for any error
    */
   protected void handleAnnotations(MetaDataVisitor visitor, boolean isApplyPhase) throws Throwable
   {
      if (visitor == null)
         throw new IllegalArgumentException("Null meta data visitor.");

      KernelControllerContext context = visitor.getControllerContext();
      Kernel kernel = context.getKernel();
      KernelMetaDataRepository repository = kernel.getMetaDataRepository();
      MetaData retrieval = repository.getMetaData(context);

      boolean trace = log.isTraceEnabled();
      BeanInfo info = context.getBeanInfo();
      Object name = context.getName();
      if (trace)
         log.trace(name + " apply annotations");

      // class
      ClassInfo classInfo = info.getClassInfo();
      for(AnnotationPlugin plugin : classAnnotationPlugins)
      {
         if (isApplyPhase)
            plugin.applyAnnotation(classInfo, retrieval, visitor);
         else
            plugin.cleanAnnotation(classInfo, retrieval, visitor);
      }

      // constructors
      Set<ConstructorInfo> constructors = info.getConstructors();
      if (constructors != null && constructors.isEmpty() == false)
      {
         for(ConstructorInfo ci : constructors)
         {
            Signature cis = new ConstructorSignature(Configurator.getParameterTypes(trace, ci.getParameterTypes()));
            MetaData cmdr = retrieval.getComponentMetaData(cis);
            if (cmdr != null)
            {
               for(AnnotationPlugin plugin : constructorAnnotationPlugins)
               {
                  if (isApplyPhase)
                     plugin.applyAnnotation(ci, cmdr, visitor);
                  else
                     plugin.cleanAnnotation(ci, cmdr, visitor);
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
            MethodInfo setter = pi.getSetter();
            if (setter != null)
            {
               visitedMethods.add(setter);
               Signature sis = new MethodSignature(setter);
               MetaData cmdr = retrieval.getComponentMetaData(sis);
               if (cmdr != null)
               {
                  for(AnnotationPlugin plugin : propertyAnnotationPlugins)
                  {
                     if (isApplyPhase)
                        plugin.applyAnnotation(pi, cmdr, visitor);
                     else
                        plugin.cleanAnnotation(pi, cmdr, visitor);
                  }
               }
               else if (trace)
                  log.trace("No annotations for property " + pi.getName());
            }
         }
      }
      else if (trace)
         log.trace("No properties");

      // methods
      Set<MethodInfo> methods = info.getMethods();
      if (methods != null && methods.isEmpty() == false)
      {
         for(MethodInfo mi : methods)
         {
            if (visitedMethods.contains(mi) == false)
            {
               Signature mis = new MethodSignature(mi);
               MetaData cmdr = retrieval.getComponentMetaData(mis);
               if (cmdr != null)
               {
                  for(AnnotationPlugin plugin : methodAnnotationPlugins)
                  {
                     if (isApplyPhase)
                        plugin.applyAnnotation(mi, cmdr, visitor);
                     else
                        plugin.cleanAnnotation(mi, cmdr, visitor);
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
      MethodInfo[] staticMethods = classInfo.getDeclaredMethods();
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
                  for(AnnotationPlugin plugin : methodAnnotationPlugins)
                  {
                     if (isApplyPhase)
                        plugin.applyAnnotation(smi, cmdr, visitor);
                     else
                        plugin.cleanAnnotation(smi, cmdr, visitor);
                  }
               }
               else if (trace)
                  log.trace("No annotations for " + smi);
            }
         }
      }
      else if (trace)
         log.trace("No static methods");

      // fields
      FieldInfo[] fields = classInfo.getDeclaredFields();
      if (fields != null && fields.length > 0)
      {
         for(FieldInfo fi : fields)
         {
            Signature fis = new FieldSignature(fi.getName());
            MetaData cmdr = retrieval.getComponentMetaData(fis);
            if (cmdr != null)
            {
               for(AnnotationPlugin plugin : fieldAnnotationPlugins)
               {
                  if (isApplyPhase)
                     plugin.applyAnnotation(fi, cmdr, visitor);
                  else
                     plugin.cleanAnnotation(fi, cmdr, visitor);
               }
            }
            else if (trace)
               log.trace("No annotations for field " + fi.getName());
         }
      }
      else if (trace)
         log.trace("No fields");
   }
}
