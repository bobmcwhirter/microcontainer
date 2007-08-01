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
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.config.support.FromCustomArraySimpleBean;
import org.jboss.test.kernel.config.support.FromCustomSignatureArraySimpleBean;
import org.jboss.test.kernel.config.support.FromObjectsArraySimpleBean;
import org.jboss.test.kernel.config.support.FromStringsArraySimpleBean;
import org.jboss.test.kernel.config.support.InterfaceArraySimpleBean;
import org.jboss.test.kernel.config.support.NotAArraySimpleBean;
import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.TypeOverrideArraySimpleBean;

/**
 * Array Test Case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class ArrayAnnotationTestCase extends ArrayTestCase
{
   public static Test suite()
   {
      return suite(ArrayAnnotationTestCase.class);
   }

   public ArrayAnnotationTestCase(String name)
   {
      super(name);
   }

   public SimpleBean simpleArrayFromObjects() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromObjectsArraySimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean simpleArrayFromStrings() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromStringsArraySimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customArrayExplicit() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromCustomArraySimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customArrayFromSignature() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromCustomSignatureArraySimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

/*
   protected SimpleBean customArrayPreInstantiated() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromPreinstArraySimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected UnmodifiableGetterBean unmodifiableArrayPreInstantiated() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromStringsArrayUnmodifiableObject.class.getName());
      return (UnmodifiableGetterBean) instantiate(builder.getBeanMetaData());
   }
*/

   protected SimpleBean arrayWithValueTypeOverride() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", TypeOverrideArraySimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

/*
   protected SimpleBean arrayInjectOnObject() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", OnObjectArraySimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }
*/

   public void testArrayNotAArray() throws Throwable
   {
      arrayNotAArray();
   }

   protected SimpleBean arrayNotAArray() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", NotAArraySimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData(), ControllerState.ERROR);
   }

   public void testArrayIsInterface() throws Throwable
   {
      arrayIsInterface();
   }

   protected SimpleBean arrayIsInterface() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", InterfaceArraySimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData(), ControllerState.ERROR);
   }
}
