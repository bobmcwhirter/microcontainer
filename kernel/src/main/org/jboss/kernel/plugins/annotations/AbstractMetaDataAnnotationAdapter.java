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
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.metadata.spi.MetaData;
import org.jboss.reflect.spi.AnnotatedInfo;
import org.jboss.kernel.spi.annotations.BeanMetaDataAnnotationAdapter;

/**
 * Abstract metadata annotation handler.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractMetaDataAnnotationAdapter extends CommonAnnotationAdapter<MetaDataAnnotationPlugin, BeanMetaData> implements BeanMetaDataAnnotationAdapter
{
   public void applyAnnotations(BeanInfo beanInfo, MetaData metaData, BeanMetaData beanMetaData) throws Throwable
   {
      handleAnnotations(beanInfo, metaData, beanMetaData, true);
   }

   @SuppressWarnings("unchecked")
   protected void applyPlugin(MetaDataAnnotationPlugin plugin, AnnotatedInfo info, MetaData retrieval, BeanMetaData handle) throws Throwable
   {
      plugin.applyAnnotation(info, retrieval, handle);
   }

   protected void cleanPlugin(MetaDataAnnotationPlugin plugin, AnnotatedInfo info, MetaData retrieval, BeanMetaData handle) throws Throwable
   {
      throw new UnsupportedOperationException("Cleanup is not supported on metadata annotation adapter.");
   }

   protected Object getName(BeanMetaData handle)
   {
      return handle.getName();
   }
}