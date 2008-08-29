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
package org.jboss.test.kernel.config.test;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.test.kernel.config.support.ParamArrayConstructorAnnBean;
import org.jboss.test.kernel.config.support.ParamCollectionConstructorAnnBean;
import org.jboss.test.kernel.config.support.ParamCompConstructorAnnBean;
import org.jboss.test.kernel.config.support.ParamConstructorAnnBean;
import org.jboss.test.kernel.config.support.ParamIntConstructorAnnBean;
import org.jboss.test.kernel.config.support.ParamListConstructorAnnBean;
import org.jboss.test.kernel.config.support.ParamMapConstructorAnnBean;
import org.jboss.test.kernel.config.support.ParamNullConstructorAnnBean;
import org.jboss.test.kernel.config.support.ParamPropertiesConstructorAnnBean;
import org.jboss.test.kernel.config.support.ParamSetConstructorAnnBean;
import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.SimpleConstructorAnnBean;

/**
 * Instantiation Annotation Test Case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class InstantiateAnnotationTestCase extends InstantiateTestCase
{
   public static Test suite()
   {
      return suite(InstantiateAnnotationTestCase.class);
   }

   public InstantiateAnnotationTestCase(String name)
   {
      super(name);
   }

   public void testSimpleInstantiateFromBeanInfo() throws Throwable
   {
      // No Annotation equivalent
   }

   protected SimpleBean simpleInstantiateFromBeanMetaData() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("simpleBean", SimpleConstructorAnnBean.class.getName());
      return (SimpleBean)instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean parameterInstantiateFromBeanMetaData() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("simpleBean", ParamConstructorAnnBean.class.getName());
      return (SimpleBean)instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean parameterInstantiateFromNull() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("simpleBean", ParamNullConstructorAnnBean.class.getName());
      return (SimpleBean)instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean parameterInstantiateWithTypeOverride() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("simpleBean", ParamIntConstructorAnnBean.class.getName());
      return (SimpleBean)instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean parameterInstantiateViaInterfaceWithTypeOverride() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("simpleBean", ParamCompConstructorAnnBean.class.getName());
      return (SimpleBean)instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean parameterInstantiateWithCollection() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("simpleBean", ParamCollectionConstructorAnnBean.class.getName());
      return (SimpleBean)instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean parameterInstantiateWithList() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("simpleBean", ParamListConstructorAnnBean.class.getName());
      return (SimpleBean)instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean parameterInstantiateWithSet() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("simpleBean", ParamSetConstructorAnnBean.class.getName());
      return (SimpleBean)instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean parameterInstantiateWithArray() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("simpleBean", ParamArrayConstructorAnnBean.class.getName());
      return (SimpleBean)instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean parameterInstantiateWithMap() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("simpleBean", ParamMapConstructorAnnBean.class.getName());
      return (SimpleBean)instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean parameterInstantiateWithProperties() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("simpleBean", ParamPropertiesConstructorAnnBean.class.getName());
      return (SimpleBean)instantiate(builder.getBeanMetaData());
   }

   public void testValueInstantiateFromValue() throws Throwable
   {
      // Not really meaningful for Annotations
   }

   public void testValueInstantiateFromCollection() throws Throwable
   {
      // Not really meaningful for Annotations
   }

   public void testValueInstantiateFromList() throws Throwable
   {
      // Not really meaningful for Annotations
   }

   public void testValueInstantiateFromSet() throws Throwable
   {
      // Not really meaningful for Annotations
   }

   public void testValueInstantiateFromArray() throws Throwable
   {
      // Not really meaningful for Annotations
   }

   public void testValueInstantiateFromObject() throws Throwable
   {
      // Not really meaningful for Annotations
   }

   public void testConstructorDoesNotExist() throws Throwable
   {
      // No Annotation equivalent
   }
}
