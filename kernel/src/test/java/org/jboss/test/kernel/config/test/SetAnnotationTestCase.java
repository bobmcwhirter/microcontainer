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
import org.jboss.test.kernel.config.support.FromObjectsSetSimpleBean;
import org.jboss.test.kernel.config.support.FromStringsSetSimpleBean;
import org.jboss.test.kernel.config.support.FromCustomSetSimpleBean;
import org.jboss.test.kernel.config.support.FromCustomSignatureSetSimpleBean;
import org.jboss.test.kernel.config.support.FromPreinstSetSimpleBean;
import org.jboss.test.kernel.config.support.UnmodifiableGetterBean;
import org.jboss.test.kernel.config.support.FromStringsSetUnmodifiableObject;
import org.jboss.test.kernel.config.support.TypeOverrideSetSimpleBean;
import org.jboss.test.kernel.config.support.OnObjectSetSimpleBean;
import org.jboss.test.kernel.config.support.NotASetSimpleBean;
import org.jboss.test.kernel.config.support.InterfaceSetSimpleBean;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.dependency.spi.ControllerState;

/**
 * Set Test Case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class SetAnnotationTestCase extends SetTestCase
{
   public static Test suite()
   {
      return suite(SetAnnotationTestCase.class);
   }

   public SetAnnotationTestCase(String name)
   {
      super(name);
   }

   public SimpleBean simpleSetFromObjects() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromObjectsSetSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean simpleSetFromStrings() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromStringsSetSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customSetExplicit() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromCustomSetSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customSetFromSignature() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromCustomSignatureSetSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean customSetPreInstantiated() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromPreinstSetSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected UnmodifiableGetterBean unmodifiableSetPreInstantiated() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromStringsSetUnmodifiableObject.class.getName());
      return (UnmodifiableGetterBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean setWithValueTypeOverride() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", TypeOverrideSetSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   protected SimpleBean setInjectOnObject() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", OnObjectSetSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData());
   }

   public void testSetNotASet() throws Throwable
   {
      setNotASet();
   }

   protected SimpleBean setNotASet() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", NotASetSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData(), ControllerState.ERROR);
   }

   public void testSetIsInterface() throws Throwable
   {
      setIsInterface();
   }

   protected SimpleBean setIsInterface() throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", InterfaceSetSimpleBean.class.getName());
      return (SimpleBean) instantiate(builder.getBeanMetaData(), ControllerState.ERROR);
   }
}
