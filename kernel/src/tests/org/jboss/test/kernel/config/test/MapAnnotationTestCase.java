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
import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.FromObjectsMapSimpleBean;
import org.jboss.test.kernel.config.support.FromStringsMapSimpleBean;
import org.jboss.test.kernel.config.support.FromCustomMapSimpleBean;
import org.jboss.test.kernel.config.support.FromCustomSignatureMapSimpleBean;
import org.jboss.test.kernel.config.support.FromPreinstMapSimpleBean;
import org.jboss.test.kernel.config.support.UnmodifiableGetterBean;
import org.jboss.test.kernel.config.support.FromStringsMapUnmodifiableObject;
import org.jboss.test.kernel.config.support.ValueTypeOverrideMapSimpleBean;
import org.jboss.test.kernel.config.support.OnObjectMapSimpleBean;
import org.jboss.test.kernel.config.support.NotAMapSimpleBean;
import org.jboss.test.kernel.config.support.InterfaceMapSimpleBean;
import org.jboss.test.kernel.config.support.KeyTypeOverrideMapSimpleBean;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.dependency.spi.ControllerState;

/**
 * Map Test Case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class MapAnnotationTestCase extends MapTestCase
{
   public static Test suite()
   {
      return suite(MapAnnotationTestCase.class);
   }

   public MapAnnotationTestCase(String name)
   {
      super(name);
   }

   public SimpleBean simpleMapFromObjects() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromObjectsMapSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean simpleMapFromStrings() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromStringsMapSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customMapExplicit() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromCustomMapSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customMapFromSignature() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromCustomSignatureMapSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customMapPreInstantiated() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromPreinstMapSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected UnmodifiableGetterBean unmodifiableMapPreInstantiated() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromStringsMapUnmodifiableObject.class.getName());
      return (UnmodifiableGetterBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean mapWithKeyTypeOverride() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", KeyTypeOverrideMapSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean mapWithValueTypeOverride() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", ValueTypeOverrideMapSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean mapInjectOnObject() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", OnObjectMapSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   public void testMapNotAMap() throws Throwable
   {
      mapNotAMap();
   }

   protected SimpleBean mapNotAMap() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", NotAMapSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData(), ControllerState.ERROR);
   }

   public void testMapIsInterface() throws Throwable
   {
      mapIsInterface();
   }

   protected SimpleBean mapIsInterface() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", InterfaceMapSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData(), ControllerState.ERROR);
   }
}
