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
package org.jboss.kernel.spi.annotations;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.config.plugins.property.PropertyConfiguration;
import org.jboss.config.spi.Configuration;
import org.jboss.kernel.plugins.annotations.BasicBeanMetaDataAnnotationAdapter;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.retrieval.MetaDataRetrievalToMetaDataBridge;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.plugins.loader.reflection.AnnotatedElementMetaDataLoader;

/**
 * Annotation to bean metadata factory.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AnnotationToBeanMetaDataFactory
{
   /** The configuration */
   private static Configuration configuration;

   static
   {
      // get Configuration instance
      configuration = AccessController.doPrivileged(new PrivilegedAction<Configuration>()
      {
         public Configuration run()
         {
            return new PropertyConfiguration();
         }
      });
   }

   /**
    * Create bean metadata for class.
    *
    * @param beanClass the bean class
    * @return bean metadata
    * @throws Throwable for any error
    */
   public static BeanMetaData createBeanMetaData(Class<?> beanClass) throws Throwable
   {
      return fillBeanMetaData(beanClass, null);
   }

   /**
    * Create bean metadata for class.
    *
    * @param beanClass the bean class
    * @param mode the bean access mode
    * @return bean metadata
    * @throws Throwable for any error
    */
   public static BeanMetaData createBeanMetaData(Class<?> beanClass, BeanAccessMode mode) throws Throwable
   {
      return fillBeanMetaData(beanClass, mode, null);
   }

   /**
    * Create bean metadata for class.
    *
    * @param beanClass the bean class
    * @param metaData predefined metadata
    * @return bean metadata
    * @throws Throwable for any error
    */
   public static BeanMetaData createBeanMetaData(Class<?> beanClass, MetaData metaData) throws Throwable
   {
      return fillBeanMetaData(beanClass, metaData, null, null);
   }

   /**
    * Create bean metadata for class.
    *
    * @param beanClass the bean class
    * @param adapter bean metadata adapter
    * @return bean metadata
    * @throws Throwable for any error
    */
   public static BeanMetaData createBeanMetaData(Class<?> beanClass, BeanMetaDataAnnotationAdapter adapter) throws Throwable
   {
      return fillBeanMetaData(beanClass, null, adapter);
   }

   /**
    * Create bean metadata for class.
    *
    * @param beanClass the bean class
    * @param mode the bean access mode
    * @param adapter bean metadata adapter
    * @return bean metadata
    * @throws Throwable for any error
    */
   public static BeanMetaData createBeanMetaData(Class<?> beanClass, BeanAccessMode mode, BeanMetaDataAnnotationAdapter adapter) throws Throwable
   {
      return fillBeanMetaData(beanClass, mode, null, null, adapter);
   }

   /**
    * Create bean metadata for class.
    *
    * @param beanClass the bean class
    * @param existingMetaData the existing bean metadata
    * @return bean metadata
    * @throws Throwable for any error
    */
   public static BeanMetaData fillBeanMetaData(Class<?> beanClass, BeanMetaData existingMetaData) throws Throwable
   {
      return fillBeanMetaData(beanClass, existingMetaData, null);
   }

   /**
    * Create bean metadata for class.
    *
    * @param beanClass the bean class
    * @param mode the bean access mode
    * @param existingMetaData the existing bean metadata
    * @return bean metadata
    * @throws Throwable for any error
    */
   public static BeanMetaData fillBeanMetaData(Class<?> beanClass, BeanAccessMode mode, BeanMetaData existingMetaData) throws Throwable
   {
      return fillBeanMetaData(beanClass, mode, null, existingMetaData, null);
   }

   /**
    * Create bean metadata for class.
    *
    * @param beanClass the bean class
    * @param existingMetaData the existing bean metadata
    * @param adapter bean metadata adapter
    * @return bean metadata
    * @throws Throwable for any error
    */
   public static BeanMetaData fillBeanMetaData(Class<?> beanClass, BeanMetaData existingMetaData, BeanMetaDataAnnotationAdapter adapter) throws Throwable
   {
      return fillBeanMetaData(beanClass, null, null, existingMetaData, adapter);
   }

   /**
    * Create bean metadata for class.
    *
    * @param beanClass the bean class
    * @param mode the bean access mode
    * @param existingMetaData the existing bean metadata
    * @param adapter bean metadata adapter
    * @return bean metadata
    * @throws Throwable for any error
    */
   public static BeanMetaData fillBeanMetaData(Class<?> beanClass, BeanAccessMode mode, BeanMetaData existingMetaData, BeanMetaDataAnnotationAdapter adapter) throws Throwable
   {
      return fillBeanMetaData(beanClass, mode, null, existingMetaData, adapter);
   }

   /**
    * Create bean metadata for class.
    *
    * @param beanClass the bean class
    * @param metaData predefined metadata
    * @param existingMetaData the existing bean metadata
    * @param adapter bean metadata adapter
    * @return bean metadata
    * @throws Throwable for any error
    */
   public static BeanMetaData fillBeanMetaData(Class<?> beanClass, MetaData metaData, BeanMetaData existingMetaData, BeanMetaDataAnnotationAdapter adapter) throws Throwable
   {
      return fillBeanMetaData(beanClass, null, metaData, existingMetaData, adapter);
   }

   /**
    * Create bean metadata for class.
    *
    * @param beanClass the bean class
    * @param mode the bean access mode
    * @param metaData predefined metadata
    * @param existingMetaData the existing bean metadata
    * @param adapter bean metadata adapter
    * @return bean metadata
    * @throws Throwable for any error
    */
   public static BeanMetaData fillBeanMetaData(Class<?> beanClass, BeanAccessMode mode, MetaData metaData, BeanMetaData existingMetaData, BeanMetaDataAnnotationAdapter adapter) throws Throwable
   {
      if (beanClass == null)
         throw new IllegalArgumentException("Null bean class");

      if (mode == null)
         mode = BeanAccessMode.STANDARD;

      if (metaData == null)
      {
         MetaDataRetrieval retrieval = new AnnotatedElementMetaDataLoader(beanClass);
         metaData = new MetaDataRetrievalToMetaDataBridge(retrieval);
      }

      if (existingMetaData == null)
      {
         AbstractBeanMetaData abmd = new AbstractBeanMetaData();
         abmd.setBean(beanClass.getName());
         abmd.setAccessMode(mode);
         existingMetaData = abmd;
      }
      else
      {
         BeanAccessMode bam = existingMetaData.getAccessMode();
         if (bam == null)
         {
            if (existingMetaData instanceof AbstractBeanMetaData == false)
               throw new IllegalArgumentException("Can only apply BeanAccessMode to AbstractBeanMetaData: " + existingMetaData);

            AbstractBeanMetaData abmd = AbstractBeanMetaData.class.cast(existingMetaData);
            abmd.setAccessMode(mode);
         }
         else if (bam != mode)
            throw new IllegalArgumentException("Different bean access modes: " + bam + " != " + mode);
      }

      if (adapter == null)
         adapter = BasicBeanMetaDataAnnotationAdapter.INSTANCE;

      BeanInfo beanInfo = configuration.getBeanInfo(beanClass, mode);
      adapter.applyAnnotations(beanInfo, metaData, existingMetaData);

      return existingMetaData;
   }
}
