/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashSet;

import junit.framework.Test;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.SimpleBean.Alphabet;

/**
 * Configuration from object Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ConfigureFromObjectTestCase extends AbstractKernelConfigTest
{
   public static Test suite()
   {
      return suite(ConfigureFromObjectTestCase.class);
   }

   public ConfigureFromObjectTestCase(String name)
   {
      super(name);
   }

   public void testConfigure() throws Throwable
   {
      String stringValue =  new String("StringValue");
      Byte byteValue = new Byte("12");
      Boolean booleanValue = Boolean.TRUE;
      Character characterValue = new Character('a');
      Short shortValue = new Short("123");
      Integer integerValue = new Integer("1234");
      Long longValue = new Long("12345");
      Float floatValue = new Float("3.14");
      Double doubleValue = new Double("3.14e12");
      Date dateValue = createDate(2001, 1, 1);
      BigDecimal bigDecimalValue = new BigDecimal("12e4");
      BigInteger bigIntegerValue = new BigInteger("123456");
      Alphabet enumValue = Alphabet.Z;

      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      BeanInfo info = configurator.getBeanInfo(SimpleBean.class);
      SimpleBean bean = (SimpleBean) instantiate(configurator, info);

      AbstractBeanMetaData metaData = new AbstractBeanMetaData();
      HashSet<PropertyMetaData> attributes = new HashSet<PropertyMetaData>();
      attributes.add(new AbstractPropertyMetaData("AString", stringValue));
      attributes.add(new AbstractPropertyMetaData("AByte", byteValue));
      attributes.add(new AbstractPropertyMetaData("ABoolean", booleanValue));
      attributes.add(new AbstractPropertyMetaData("ACharacter", characterValue));
      attributes.add(new AbstractPropertyMetaData("AShort", shortValue));
      attributes.add(new AbstractPropertyMetaData("anInt", integerValue));
      attributes.add(new AbstractPropertyMetaData("ALong", longValue));
      attributes.add(new AbstractPropertyMetaData("AFloat", floatValue));
      attributes.add(new AbstractPropertyMetaData("ADouble", doubleValue));
      attributes.add(new AbstractPropertyMetaData("ADate", dateValue));
      attributes.add(new AbstractPropertyMetaData("ABigDecimal", bigDecimalValue));
      attributes.add(new AbstractPropertyMetaData("ABigInteger", bigIntegerValue));
      attributes.add(new AbstractPropertyMetaData("abyte", byteValue));
      attributes.add(new AbstractPropertyMetaData("aboolean", booleanValue));
      attributes.add(new AbstractPropertyMetaData("achar", characterValue));
      attributes.add(new AbstractPropertyMetaData("ashort", shortValue));
      attributes.add(new AbstractPropertyMetaData("anint", integerValue));
      attributes.add(new AbstractPropertyMetaData("along", longValue));
      attributes.add(new AbstractPropertyMetaData("afloat", floatValue));
      attributes.add(new AbstractPropertyMetaData("adouble", doubleValue));
      attributes.add(new AbstractPropertyMetaData("ANumber", longValue));
      attributes.add(new AbstractPropertyMetaData("overloadedProperty", stringValue));
      attributes.add(new AbstractPropertyMetaData("enumProperty", enumValue));
      metaData.setProperties(attributes);
      
      configure(configurator, bean, info, metaData);
      
      assertEquals(stringValue, bean.getAString());
      assertEquals(byteValue, bean.getAByte());
      assertEquals(booleanValue, bean.getABoolean());
      assertEquals(characterValue, bean.getACharacter());
      assertEquals(shortValue, bean.getAShort());
      assertEquals(integerValue, bean.getAnInt());
      assertEquals(longValue, bean.getALong());
      assertEquals(floatValue, bean.getAFloat());
      assertEquals(doubleValue, bean.getADouble());
      assertEquals(dateValue, bean.getADate());
      assertEquals(bigDecimalValue, bean.getABigDecimal());
      assertEquals(bigIntegerValue, bean.getABigInteger());
      assertEquals(byteValue.byteValue(), bean.getAbyte());
      assertEquals(booleanValue.booleanValue(), bean.isAboolean());
      assertEquals(characterValue.charValue(), bean.getAchar());
      assertEquals(shortValue.shortValue(), bean.getAshort());
      assertEquals(integerValue.intValue(), bean.getAnint());
      assertEquals(longValue.longValue(), bean.getAlong());
      assertEquals(floatValue.floatValue(), bean.getAfloat());
      assertEquals(doubleValue.doubleValue(), bean.getAdouble());
      Number number = bean.getANumber();
      assertEquals(Long.class, number.getClass());
      assertEquals(longValue, number);
      assertEquals(stringValue, bean.getOverloadedProperty());
      assertEquals(enumValue, bean.getEnumProperty());
   }
}