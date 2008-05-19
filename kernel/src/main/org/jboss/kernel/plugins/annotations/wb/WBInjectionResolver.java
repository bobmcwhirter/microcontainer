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
import java.util.Set;

import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.MetaData;

/**
 * Web beans injection resolver.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class WBInjectionResolver
{
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
         Set<KernelControllerContext> matchingContexts = new HashSet<KernelControllerContext>();
         for(KernelControllerContext context : contexts)
         {
            boolean match = true;
            MetaData metaData = context.getScopeInfo().getMetaData();
            for(Annotation annotation : annotations)
            {
               Annotation mdAnnotation = metaData.getAnnotation(annotation.annotationType());
               if (mdAnnotation == null)
               {
                  match = false;
                  break;
               }
            }
            if (match)
               matchingContexts.add(context);
         }
         int size = matchingContexts.size();
         if (size != 1)
            return null;
         else
            return matchingContexts.iterator().next();
      }
      else
      {
         return null;
      }
   }
}