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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueFactoryMetaData;
import org.jboss.beans.metadata.plugins.AbstractMapMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.Kernel;
import org.jboss.test.kernel.config.support.PropHolder;
import org.jboss.test.kernel.config.support.TrimTransformer;
import org.jboss.test.kernel.config.support.LDAPFactory;
import org.jboss.test.kernel.config.support.Transformer;

/**
 * Test org.w3c.dom.Element usage in MC xml.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ValueFactoryTestCase extends AbstractKernelConfigTest
{
   public ValueFactoryTestCase(String name)
   {
      super(name);
   }

   public ValueFactoryTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(ValueFactoryTestCase.class);
   }

   public void testValueFactory() throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelController controller = kernel.getController();
      PropHolder propHolder = instantiateHolder(controller);
      assertNotNull(propHolder);
      assertEquals("QWERT", propHolder.getConstructor());
      assertEquals("QWERT", propHolder.getValue());
      assertNotNull(propHolder.getList());
      for (String prop : propHolder.getList())
         assertEquals("QWERT", prop);
   }

   protected PropHolder instantiateHolder(KernelController controller) throws Throwable
   {
      // ldap
      BeanMetaDataBuilder ldap = BeanMetaDataBuilderFactory.createBuilder("ldap", LDAPFactory.class.getName());
      AbstractMapMetaData map = new AbstractMapMetaData();
      map.setKeyType(String.class.getName());
      map.setValueType(String.class.getName());
      map.put(new StringValueMetaData("foo.bar.key"), new StringValueMetaData("QWERT"));
      map.put(new StringValueMetaData("xyz.key"), new StringValueMetaData(" QWERT "));
      ldap.addConstructorParameter(Map.class.getName(), map);
      LDAPFactory lf = (LDAPFactory)instantiate(controller, ldap.getBeanMetaData());
      assertNotNull(lf);

      // simple one
      AbstractValueFactoryMetaData vfmd1 = new AbstractValueFactoryMetaData("ldap", "getValue");
      List<ParameterMetaData> parameters1 = new ArrayList<ParameterMetaData>();
      vfmd1.setParameters(parameters1);
      parameters1.add(new AbstractParameterMetaData(String.class.getName(), "foo.bar.key"));

      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("PropHolder", PropHolder.class.getName());
      builder.addConstructorParameter(String.class.getName(), vfmd1);
      builder.addPropertyMetaData("value", vfmd1);
      
      AbstractListMetaData listMD = new AbstractListMetaData();
      listMD.add(vfmd1);
      // default from 2nd param
      AbstractValueFactoryMetaData vfmd2 = new AbstractValueFactoryMetaData("ldap", "getValue");
      List<ParameterMetaData> parameters2 = new ArrayList<ParameterMetaData>();
      vfmd2.setParameters(parameters2);
      parameters2.add(new AbstractParameterMetaData(String.class.getName(), "foo.bar.key"));
      parameters2.add(new AbstractParameterMetaData(String.class.getName(), "qaz"));
      listMD.add(vfmd2);
      // with transformer
      AbstractValueFactoryMetaData vfmd3 = new AbstractValueFactoryMetaData("ldap", "getValue");
      List<ParameterMetaData> parameters3 = new ArrayList<ParameterMetaData>();
      vfmd3.setParameters(parameters3);
      parameters3.add(new AbstractParameterMetaData(String.class.getName(), "xyz.key"));
      parameters3.add(new AbstractParameterMetaData(String.class.getName(), "xyz"));
      // instantiate transformer
      BeanMetaDataBuilder tranformer = BeanMetaDataBuilderFactory.createBuilder("t", TrimTransformer.class.getName());
      assertNotNull(instantiate(controller, tranformer.getBeanMetaData()));
      parameters3.add(new AbstractParameterMetaData(Transformer.class.getName(), tranformer.getBeanMetaData()));
      listMD.add(vfmd3);
      // null, use default
      AbstractValueFactoryMetaData vfmd4 = new AbstractValueFactoryMetaData("ldap", "getValue", "QWERT");
      List<ParameterMetaData> parameters4 = new ArrayList<ParameterMetaData>();
      vfmd4.setParameters(parameters4);
      parameters4.add(new AbstractParameterMetaData(String.class.getName(), "no.such.key"));
      listMD.add(vfmd4);
      builder.addPropertyMetaData("list", listMD);
      return (PropHolder)instantiate(controller, builder.getBeanMetaData());
   }

}
