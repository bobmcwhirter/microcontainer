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
package org.jboss.test.kernel.deployment.props.test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.plugins.deployment.props.PropertiesGraphFactory;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.BaseTestCase;

/**
 * Test graph from properties.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class GraphBuilderTestCase extends BaseTestCase
{
   public GraphBuilderTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(GraphBuilderTestCase.class);
   }

   public void testGraphFromProperties() throws Exception
   {
      Map<String, String> properties = new HashMap<String, String>();
      properties.put("mybean.(class)", "org.jboss.acme.MyBean");
      properties.put("mybean.somenumber", "123L");
      properties.put("mybean.somenumber.type", "java.lang.Long");
      properties.put("mybean.injectee", "(inject).injectee");
      properties.put("injectee.(class)", "org.jboss.acme.MyBean2");

      PropertiesGraphFactory propertiesGraph = new PropertiesGraphFactory(properties);
      KernelDeployment deployment = propertiesGraph.build();
      assertNotNull(deployment);
      List<BeanMetaData> beans = deployment.getBeans();
      assertNotNull(beans);
      assertFalse(beans.isEmpty());

      BeanMetaData bean1 = beans.get(0);
      assertInstanceOf(bean1, AbstractBeanMetaData.class);
      AbstractBeanMetaData abmd1 = (AbstractBeanMetaData)bean1;
      assertNotNull(bean1);
      assertEquals("mybean", bean1.getName());
      assertEquals("org.jboss.acme.MyBean", bean1.getBean());
      Set<PropertyMetaData> propertys = bean1.getProperties();
      assertNotNull(propertys);
      assertFalse(propertys.isEmpty());
      assertEquals(2, propertys.size());
      PropertyMetaData pmd1 = abmd1.getProperty("somenumber");
      assertNotNull(pmd1);
      ValueMetaData vmd1 = pmd1.getValue();
      assertNotNull(vmd1);
      assertInstanceOf(vmd1, StringValueMetaData.class);
      assertEquals("123L", vmd1.getUnderlyingValue());
      assertEquals("java.lang.Long", ((StringValueMetaData)vmd1).getType());
      PropertyMetaData pmd2 = abmd1.getProperty("injectee");
      assertNotNull(pmd2);
      ValueMetaData vmd2 = pmd2.getValue();
      assertNotNull(vmd2);
      assertInstanceOf(vmd2, AbstractDependencyValueMetaData.class);

      BeanMetaData bean2 = beans.get(1);
      assertNotNull(bean2);
      assertEquals("injectee", bean2.getName());
      assertEquals("org.jboss.acme.MyBean2", bean2.getBean());
   }
}
