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
package org.jboss.kernel.plugins.annotations.wb;

import java.lang.annotation.Annotation;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.List;
import java.util.ArrayList;

import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.logging.Logger;
import org.jboss.metadata.spi.MetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.api.annotations.Inject;

/**
 * Web beans injection resolver.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class WBInjectionResolver
{
   /** The log */
   private static final Logger log = Logger.getLogger(WBInjectionResolver.class);

   /** The cache */
   private static final Map<CacheKey, Map<KernelControllerContext, Boolean>> cache = new WeakHashMap<CacheKey, Map<KernelControllerContext, Boolean>>();

   /** The excluded annotation */
   private static final Set<Class<? extends Annotation>> excludedAnnotations = new HashSet<Class<? extends Annotation>>();

   static
   {
      addExcludedAnnotation(Inject.class);
      // exclude jdk annotations
      addExcludedAnnotation(SuppressWarnings.class);
      addExcludedAnnotation(Deprecated.class);
      addExcludedAnnotation(Override.class);
   }

   /**
    * Create wb injection value.
    *
    * @param type the matching type
    * @param underlyingAnnotations underlying annotations
    * @return injection value metadata
    */
   static ValueMetaData createValueMetaData(Class<?> type, Annotation[] underlyingAnnotations)
   {
      List<Annotation> annotations = new ArrayList<Annotation>();
      for (Annotation annotation : underlyingAnnotations)
      {
         if (excludedAnnotations.contains(annotation.annotationType()) == false)
            annotations.add(annotation);
      }
      return new WBInjectionValueMetaData(type, annotations.toArray(new Annotation[annotations.size()]));
   }

   /**
    * Add excluded annotations.
    *
    * @param annotationClass the excluded annotation's class.
    */
   public static void addExcludedAnnotation(Class<? extends Annotation> annotationClass)
   {
      excludedAnnotations.add(annotationClass);
   }

   /**
    * Find matching controller context.
    *
    * @param controller the kernel controller
    * @param type the matching type
    * @param annotations the filter annotations
    * @return single matching context or null if none or multiple
    */
   public static KernelControllerContext resolve(KernelController controller, Class<?> type, Annotation[] annotations)
   {
      if (controller == null)
         throw new IllegalArgumentException("Null controller");
      if (type == null)
         throw new IllegalArgumentException("Null type");
      if (annotations == null)
         annotations = new Annotation[]{};

      Set<KernelControllerContext> contexts = controller.getContexts(type, ControllerState.INSTALLED);
      if (contexts != null && contexts.isEmpty() == false)
      {
         CacheKey key = new CacheKey(type, annotations);
         Map<KernelControllerContext, Boolean> cachedResults = cache.get(key);
         if (cachedResults == null)
         {
            cachedResults = new WeakHashMap<KernelControllerContext, Boolean>();
            cache.put(key, cachedResults);
         }

         Set<KernelControllerContext> matchingContexts = new HashSet<KernelControllerContext>();
         for(KernelControllerContext context : contexts)
         {
            Boolean match = cachedResults.get(context);
            if (match == null)
            {
               match = true;
               MetaData metaData = context.getScopeInfo().getMetaData();
               for(Annotation annotation : annotations)
               {
                  Annotation mdAnnotation = metaData.getAnnotation(annotation.annotationType());
                  if (mdAnnotation == null || annotation.equals(mdAnnotation) == false)
                  {
                     match = false;
                     break;
                  }
               }
            }

            if (match)
               matchingContexts.add(context);

            cachedResults.put(context, match);
         }
         int size = matchingContexts.size();
         if (size == 0)
            return null;
         else if (size > 1)
         {
            if (log.isTraceEnabled())
               log.trace("Too many matching contexts: " + matchingContexts);

            return null;
         }
         else
            return matchingContexts.iterator().next();
      }
      else
      {
         return null;
      }
   }
}