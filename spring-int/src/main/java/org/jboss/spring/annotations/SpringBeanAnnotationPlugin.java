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
package org.jboss.spring.annotations;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.api.model.FromContext;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.kernel.plugins.annotations.ClassAnnotationPlugin;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.InterfaceInfo;

/**
 * Marker annotation plugin handler.
 *
 * @author John Bailey
 * @author Davide Panelli
 */
public class SpringBeanAnnotationPlugin extends ClassAnnotationPlugin<SpringBean>
{
   private static final String BNAI = "org.springframework.beans.factory.BeanNameAware";
   private static final String BNP = "beanName";

   public SpringBeanAnnotationPlugin()
   {
      super(SpringBean.class);
   }

   @Override
   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(ClassInfo info, SpringBean annotation, BeanMetaData metaData) throws Throwable
   {
      PropertyMetaData property = findBeanNameAwareInterface(info, metaData);
      return property != null ? Collections.singletonList(property) : null;
   }

   /**
    * Find the BeanNameAware interface.
    *
    * @param info the type info
    * @param metaData the metadata
    * @return bean property metadata or null if not found
    */
   protected PropertyMetaData findBeanNameAwareInterface(ClassInfo info, BeanMetaData metaData)
   {
      InterfaceInfo[] interfaces = info.getInterfaces();
      if (interfaces != null && interfaces.length > 0)
      {
         for (InterfaceInfo anInterface : interfaces)
         {
            String name = anInterface.getName();
            if (BNAI.equals(name))
            {
               AbstractInjectionValueMetaData injectionMetaData = new AbstractInjectionValueMetaData();
               injectionMetaData.setFromContext(FromContext.NAME);
               AbstractPropertyMetaData beanNameProperty = new AbstractPropertyMetaData(BNP, injectionMetaData);
               Set<PropertyMetaData> properties = metaData.getProperties();
               if (properties == null)
               {
                  properties = new HashSet<PropertyMetaData>();
                  AbstractBeanMetaData abmd = checkIfNotAbstractBeanMetaDataSpecific(metaData);
                  abmd.setProperties(properties);
               }
               properties.add(beanNameProperty);
               return beanNameProperty;
            }
            // Search on super interfaces
            PropertyMetaData property = findBeanNameAwareInterface(anInterface, metaData);
            if (property != null)
               return property;
         }
      }
      // Search on super class
      ClassInfo superInfo = info.getSuperclass();
      if (superInfo != null)
         return findBeanNameAwareInterface(superInfo, metaData);

      return null;
   }

   @Override
   protected boolean isMetaDataAlreadyPresent(ClassInfo info, SpringBean annotation, BeanMetaData beanMetaData)
   {
      Set<PropertyMetaData> properties = beanMetaData.getProperties();
      if (properties != null && properties.isEmpty() == false)
      {
         for (PropertyMetaData property : properties)
         {
            if (BNP.equals(property.getName()))
            {
               return true;
            }
         }
      }
      return false;
   }
}
