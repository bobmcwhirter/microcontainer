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
package org.jboss.kernel.plugins.dependency;

import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapter;
import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapterFactory;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * Handles IoC annotations.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AnnotationsAction extends InstallsAwareAction
{
   /**
    * Handle IoC annotations.
    *
    * @param context the kernel controller context
    * @throws Throwable for any error
    */
   protected void applyAnnotations(KernelControllerContext context) throws Throwable
   {
      AnnotationMetaDataVisitor annotationsVisitor = new AnnotationMetaDataVisitor(context);
      annotationsVisitor.before();
      try
         {
            getBeanAnnotationAdapter().applyAnnotations(annotationsVisitor);
         }
         finally
         {
            annotationsVisitor.after();
         }
   }

   /**
    * Clean IoC annotations.
    *
    * @param context the kernel controller context
    */
   protected void cleanAnnotations(KernelControllerContext context)
   {
      AnnotationMetaDataVisitor annotationsVisitor = new AnnotationMetaDataVisitor(context);
      annotationsVisitor.before();
      try
      {
         getBeanAnnotationAdapter().cleanAnnotations(annotationsVisitor);
      }
      catch(Throwable t)
      {
         log.debug("Error while cleaning the annotations: " + t);
      }
      finally
      {
         annotationsVisitor.after();
      }
   }

   /**
    * Get bean annotation adapter.
    *
    * @return bean annotation adapter
    */
   protected BeanAnnotationAdapter getBeanAnnotationAdapter()
   {
      BeanAnnotationAdapterFactory factory = BeanAnnotationAdapterFactory.getInstance();
      return factory.getBeanAnnotationAdapter();
   }
}