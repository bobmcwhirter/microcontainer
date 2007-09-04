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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * Abstract property annotation plugin.
 *
 * @param <C> annotation type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class PropertyAnnotationPlugin<C extends Annotation> extends AbstractAnnotationPlugin<PropertyInfo, C>
   implements PropertyAware, Annotation2ValueMetaDataAdapter<C>
{
   protected PropertyAnnotationPlugin(Class<C> annotation)
   {
      super(annotation);
   }

   protected boolean isElementTypeSupported(ElementType type)
   {
      return ElementType.METHOD == type || ElementType.PARAMETER == type;
   }

   protected boolean isMetaDataAlreadyPresent(PropertyInfo info, C annotation, BeanMetaData beanMetaData)
   {
      Set<PropertyMetaData> properties = beanMetaData.getProperties();
      if (properties != null && properties.isEmpty() == false)
      {
         for(PropertyMetaData pmd : properties)
         {
            if (pmd.getName().equals(info.getName()))
               return true;
         }
      }
      return false;
   }

   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(PropertyInfo info, C annotation, KernelControllerContext context)
   {
      Set<PropertyMetaData> properties = getProperties(context);
      PropertyMetaData property = getPropertyMetaData(info, annotation, context);
      properties.add(property);
      return Collections.singletonList(property);
   }

   /**
    * Get PropertyMetaData instance.
    *
    * @param info the info
    * @param annotation the annotation
    * @param context the context
    * @return get new PropertyMetaData instance
    */
   protected PropertyMetaData getPropertyMetaData(PropertyInfo info, C annotation, KernelControllerContext context)
   {
      return getPropertyMetaData(info, annotation, context.getBeanMetaData());
   }

   /**
    * Get PropertyMetaData instance.
    *
    * @param info the info
    * @param annotation the annotation
    * @param beanMetaData the bean metadata
    * @return get new PropertyMetaData instance
    */
   protected PropertyMetaData getPropertyMetaData(PropertyInfo info, C annotation, BeanMetaData beanMetaData)
   {
      return getPropertyMetaData(info, annotation);
   }

   /**
    * Get PropertyMetaData instance.
    *
    * @param info the info
    * @param annotation the annotation
    * @return get new PropertyMetaData instance
    */
   protected PropertyMetaData getPropertyMetaData(PropertyInfo info, C annotation)
   {
      ValueMetaData value = createValueMetaData(annotation);
      return new AbstractPropertyMetaData(info.getName(), value);
   }

   /**
    * Get the PropertyMetaData set.
    *
    * @param context the context
    * @return set of existing PropertyMetaData
    */
   protected Set<PropertyMetaData> getProperties(KernelControllerContext context)
   {
      BeanMetaData beanMetaData = context.getBeanMetaData();
      Set<PropertyMetaData> properties = beanMetaData.getProperties();
      if (properties == null)
      {
         properties = new HashSet<PropertyMetaData>();
         AbstractBeanMetaData bean = (AbstractBeanMetaData)beanMetaData;
         bean.setProperties(properties);
      }
      return properties;
   }
}
