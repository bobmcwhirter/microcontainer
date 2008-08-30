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
import org.jboss.test.kernel.config.support.FromObjectsCollectionSimpleBean;
import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.UnmodifiableGetterBean;
import org.jboss.test.kernel.config.support.FromStringsCollectionSimpleBean;
import org.jboss.test.kernel.config.support.FromCustomCollectionSimpleBean;
import org.jboss.test.kernel.config.support.FromCustomSignatureCollectionSimpleBean;
import org.jboss.test.kernel.config.support.FromPreinstCollectionSimpleBean;
import org.jboss.test.kernel.config.support.FromStringsCollectionUnmodifiableObject;
import org.jboss.test.kernel.config.support.TypeOverrideCollectionSimpleBean;
import org.jboss.test.kernel.config.support.OnObjectCollectionSimpleBean;
import org.jboss.test.kernel.config.support.NotACollectionSimpleBean;
import org.jboss.test.kernel.config.support.InterfaceCollectionSimpleBean;
import org.jboss.dependency.spi.ControllerState;

/**
 * Collection Test Case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class CollectionAnnotationTestCase extends CollectionTestCase
{
   public static Test suite()
   {
      return suite(CollectionAnnotationTestCase.class);
   }

   public CollectionAnnotationTestCase(String name)
   {
      super(name);
   }

   public SimpleBean simpleCollectionFromObjects() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromObjectsCollectionSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean simpleCollectionFromStrings() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromStringsCollectionSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customCollectionExplicit() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromCustomCollectionSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customCollectionFromSignature() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromCustomSignatureCollectionSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customCollectionPreInstantiated() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromPreinstCollectionSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected UnmodifiableGetterBean unmodifiableCollectionPreInstantiated() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromStringsCollectionUnmodifiableObject.class.getName());
      return (UnmodifiableGetterBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean collectionWithValueTypeOverride() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", TypeOverrideCollectionSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean collectionInjectOnObject() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", OnObjectCollectionSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   public void testCollectionNotACollection() throws Throwable
   {
      collectionNotACollection();
   }

   protected SimpleBean collectionNotACollection() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", NotACollectionSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData(), ControllerState.ERROR);
   }

   public void testCollectionIsInterface() throws Throwable
   {
      collectionIsInterface();
   }

   protected SimpleBean collectionIsInterface() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", InterfaceCollectionSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData(), ControllerState.ERROR);
   }
}
