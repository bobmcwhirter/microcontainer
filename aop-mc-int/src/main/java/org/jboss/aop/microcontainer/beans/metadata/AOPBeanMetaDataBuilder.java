/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.aop.microcontainer.beans.metadata;

import org.jboss.aop.microcontainer.annotations.DisableAOP;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;

/**
 * Wrapper factory class to obtain a {@link BeanMetaDataBuilder} which adds the @{@link DisableAOP} annotation
 * to the underlying bean being built 
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class AOPBeanMetaDataBuilder
{
   /**
    * Create builder from bean name and add the @{@link DisableAOP} annotation 
    * to the underlying bean being built.
    *
    * @param bean bean class name
    * @return new Builder
    */
   public static BeanMetaDataBuilder createBuilder(String bean)
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder(bean);
      addDisableAopAnnotation(builder);
      return builder;
   }

   /**
    * Create builder from name and bean and add the @{@link DisableAOP} annotation 
    * to the underlying bean being built.
    * @param name bean name
    * @param bean bean class name
    * @return new Builder
    */
   public static BeanMetaDataBuilder createBuilder(String name, String bean)
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder(name, bean);
      addDisableAopAnnotation(builder);
      return builder;
   }
   
   /**
    * Create builder from BeanMetaData and add the @{@link DisableAOP} annotation 
    * to the underlying bean being built.
    * 
    * @param beanMetaData the bean metadata
    * @return new Builder()
    */
   public static BeanMetaDataBuilder createBuilder(BeanMetaData beanMetaData)
   {
      if (beanMetaData instanceof AbstractBeanMetaData)
      {
         BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder((AbstractBeanMetaData)beanMetaData);
         addDisableAopAnnotation(builder);
         return builder;
      }
      else
      {
         throw new IllegalArgumentException("Invalid type of bean metadata: " + beanMetaData);
      }
   }
   
   private static void addDisableAopAnnotation(BeanMetaDataBuilder builder)
   {
      builder.addAnnotation("@" + DisableAOP.class.getName());
   }
}
