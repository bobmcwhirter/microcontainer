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
package org.jboss.example.microcontainer.annotations;

import java.util.Map;
import java.util.Set;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.metadata.spi.retrieval.AnnotationItem;
import org.jboss.metadata.spi.retrieval.AnnotationsItem;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AnnotationChecker
{
   private Kernel kernel;
   private Map<String, Set<String>> checker;

   public void setKernel(Kernel kernel)
   {
      this.kernel = kernel;
   }

   public void setCheckerMap(Map<String, Set<String>> checker)
   {
      this.checker = checker;
   }

   public void check()
   {
      KernelController controller = kernel.getController();
      for(String key : checker.keySet())
      {
         KernelControllerContext context = (KernelControllerContext)controller.getInstalledContext(key);
         Set<String> annotations = checker.get(key);
         for(String annotation : annotations)
         {
            System.out.println(
                  "Annotation " + annotation + (check(context, annotation) ?
                  " exists " : " doesn't exist ") + " on " + context.getTarget().getClass().getSuperclass()
            );
         }
      }
   }

   public boolean check(KernelControllerContext context, String annotation)
   {
      KernelMetaDataRepository repository = kernel.getMetaDataRepository();
      MetaDataRetrieval retrieval = repository.getMetaDataRetrieval(context);
      if (retrieval != null)
      {
         AnnotationsItem annotations = retrieval.retrieveAnnotations();
         if (annotations != null)
         {
            AnnotationItem[] annotationItems = annotations.getAnnotations();
            if (annotationItems != null && annotationItems.length > 0)
            {
               for (AnnotationItem annItem : annotationItems)
               {
                  if (annItem.getAnnotation().annotationType().getName().equals(annotation))
                  {
                     return true;
                  }
               }
            }
         }
      }
      return false;
   }

}
