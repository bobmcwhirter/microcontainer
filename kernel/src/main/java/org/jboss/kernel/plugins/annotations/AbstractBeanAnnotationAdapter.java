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

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.metadata.spi.MetaData;
import org.jboss.reflect.spi.AnnotatedInfo;

/**
 * Abstract bean annotation handler.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractBeanAnnotationAdapter extends CommonAnnotationAdapter<AnnotationPlugin<?, ?>, MetaDataVisitor> implements BeanAnnotationAdapter
{
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
      BeanInfo info = context.getBeanInfo();

      handleAnnotations(info, retrieval, visitor, isApplyPhase);
   }

   @SuppressWarnings("unchecked")
   protected void applyPlugin(AnnotationPlugin plugin, AnnotatedInfo info, MetaData retrieval, MetaDataVisitor handle) throws Throwable
   {
      plugin.applyAnnotation(info, retrieval, handle);
   }

   @SuppressWarnings("unchecked")
   protected void cleanPlugin(AnnotationPlugin plugin, AnnotatedInfo info, MetaData retrieval, MetaDataVisitor handle) throws Throwable
   {
      plugin.cleanAnnotation(info, retrieval, handle);
   }

   protected Object getName(MetaDataVisitor handle)
   {
      return handle.getControllerContext().getName();
   }
}
