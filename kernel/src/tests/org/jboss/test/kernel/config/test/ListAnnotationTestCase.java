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
import org.jboss.test.kernel.config.support.FromObjectsListSimpleBean;
import org.jboss.test.kernel.config.support.FromStringsListSimpleBean;
import org.jboss.test.kernel.config.support.FromCustomListSimpleBean;
import org.jboss.test.kernel.config.support.FromCustomSignatureListSimpleBean;
import org.jboss.test.kernel.config.support.FromPreinstListSimpleBean;
import org.jboss.test.kernel.config.support.UnmodifiableGetterBean;
import org.jboss.test.kernel.config.support.FromStringsListUnmodifiableObject;
import org.jboss.test.kernel.config.support.TypeOverrideListSimpleBean;
import org.jboss.test.kernel.config.support.OnObjectListSimpleBean;
import org.jboss.test.kernel.config.support.NotAListSimpleBean;
import org.jboss.test.kernel.config.support.InterfaceListSimpleBean;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.dependency.spi.ControllerState;

/**
 * List Test Case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class ListAnnotationTestCase extends ListTestCase
{
   public static Test suite()
   {
      return suite(ListAnnotationTestCase.class);
   }

   public ListAnnotationTestCase(String name)
   {
      super(name);
   }

   public SimpleBean simpleListFromObjects() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromObjectsListSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean simpleListFromStrings() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromStringsListSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customListExplicit() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromCustomListSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customListFromSignature() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromCustomSignatureListSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customListPreInstantiated() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromPreinstListSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected UnmodifiableGetterBean unmodifiableListPreInstantiated() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromStringsListUnmodifiableObject.class.getName());
      return (UnmodifiableGetterBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean listWithValueTypeOverride() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", TypeOverrideListSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean listInjectOnObject() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", OnObjectListSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   public void testListNotAList() throws Throwable
   {
      listNotAList();
   }

   protected SimpleBean listNotAList() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", NotAListSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData(), ControllerState.ERROR);
   }

   public void testListIsInterface() throws Throwable
   {
      listIsInterface();
   }

   protected SimpleBean listIsInterface() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", InterfaceListSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData(), ControllerState.ERROR);
   }
}
