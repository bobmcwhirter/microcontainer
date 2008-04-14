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

import junit.framework.Test;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.SimpleBean.Alphabet;

/**
 * Configuration from string Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ConfigureAttributeFromStringTestCase extends AbstractKernelConfigTest
{
   public static Test suite()
   {
      return suite(ConfigureAttributeFromStringTestCase.class);
   }

   public ConfigureAttributeFromStringTestCase(String name)
   {
      super(name);
   }

   public void testStringAttribute() throws Throwable
   {
      Object value = "StringValue";
      SimpleBean bean = configureSimpleBean("AString", value);
      assertEquals(value, bean.getAString());
   }

   public void testByteAttribute() throws Throwable
   {
      Object value = new Byte("12");
      SimpleBean bean = configureSimpleBean("AByte", value);
      assertEquals(value, bean.getAByte());
   }

   public void testBooleanAttribute() throws Throwable
   {
      Object value = Boolean.TRUE;
      SimpleBean bean = configureSimpleBean("ABoolean", value);
      assertEquals(value, bean.getABoolean());
   }

   /* TODO character
   public void testCharacterAttribute() throws Throwable
   {
      Object value = new Character('a');
      SimpleBean bean = configureSimpleBean("aCharacter", value);
      assertEquals(value, bean.getACharacter());
   } */

   public void testShortAttribute() throws Throwable
   {
      Object value = new Short("123");
      SimpleBean bean = configureSimpleBean("AShort", value);
      assertEquals(value, bean.getAShort());
   }

   public void testIntegerAttribute() throws Throwable
   {
      Object value = new Integer("1234");
      SimpleBean bean = configureSimpleBean("anInt", value);
      assertEquals(value, bean.getAnInt());
   }

   public void testLongAttribute() throws Throwable
   {
      Object value = new Long("12345");
      SimpleBean bean = configureSimpleBean("ALong", value);
      assertEquals(value, bean.getALong());
   }

   public void testFloatAttribute() throws Throwable
   {
      Object value = new Float("3.14");
      SimpleBean bean = configureSimpleBean("AFloat", value);
      assertEquals(value, bean.getAFloat());
   }

   public void testDoubleAttribute() throws Throwable
   {
      Object value = new Double("3.14e12");
      SimpleBean bean = configureSimpleBean("ADouble", value);
      assertEquals(value, bean.getADouble());
   }

   public void testDateAttribute() throws Throwable
   {
      Object value = createDate(2001, 1, 1);
      SimpleBean bean = configureSimpleBean("ADate", value);
      assertEquals(value, bean.getADate());
   }

   public void testBigDecimalAttribute() throws Throwable
   {
      Object value = new BigDecimal("12e4");
      SimpleBean bean = configureSimpleBean("ABigDecimal", value);
      assertEquals(value, bean.getABigDecimal());
   }

   public void testBigIntegerAttribute() throws Throwable
   {
      Object value = new BigInteger("123456");
      SimpleBean bean = configureSimpleBean("ABigInteger", value);
      assertEquals(value, bean.getABigInteger());
   }

   public void testPrimitiveByteAttribute() throws Throwable
   {
      Byte value = new Byte("12");
      SimpleBean bean = configureSimpleBean("abyte", value);
      assertEquals(value.byteValue(), bean.getAbyte());
   }

   public void testPrimitiveBooleanAttribute() throws Throwable
   {
      Boolean value = Boolean.TRUE;
      SimpleBean bean = configureSimpleBean("aboolean", value);
      assertEquals(value.booleanValue(), bean.isAboolean());
   }

   /* TODO character
   public void testPrimitiveCharacterAttribute() throws Throwable
   {
      Character value = new Character('a');
      SimpleBean bean = configureSimpleBean("achar", value);
      assertEquals(value.charValue(), bean.getAchar());
   } */

   public void testPrimitiveShortAttribute() throws Throwable
   {
      Short value = new Short("123");
      SimpleBean bean = configureSimpleBean("ashort", value);
      assertEquals(value.shortValue(), bean.getAshort());
   }

   public void testPrimitiveIntegerAttribute() throws Throwable
   {
      Integer value = new Integer("1234");
      SimpleBean bean = configureSimpleBean("anint", value);
      assertEquals(value.intValue(), bean.getAnint());
   }

   public void testPrimitiveLongAttribute() throws Throwable
   {
      Long value = new Long("12345");
      SimpleBean bean = configureSimpleBean("along", value);
      assertEquals(value.longValue(), bean.getAlong());
   }

   public void testPrimitiveFloatAttribute() throws Throwable
   {
      Float value = new Float("3.14");
      SimpleBean bean = configureSimpleBean("afloat", value);
      assertEquals(value.floatValue(), bean.getAfloat());
   }

   public void testPrimitiveDoubleAttribute() throws Throwable
   {
      Double value = new Double("3.14e12");
      SimpleBean bean = configureSimpleBean("adouble", value);
      assertEquals(value.doubleValue(), bean.getAdouble());
   }

   public void testNumber() throws Throwable
   {
      Long value = new Long("4");
      SimpleBean bean = configureSimpleBean("ANumber", value, "java.lang.Long");
      Object number = bean.getANumber();
      assertEquals(Long.class, number.getClass());
      assertEquals(value, number);
   }

   public void testEnum() throws Throwable
   {
      Alphabet value = Alphabet.Z;
      SimpleBean bean = configureSimpleBean("enumProperty", value, Alphabet.class.getName());
      Object evalue = bean.getEnumProperty();
      assertEquals(Alphabet.class, evalue.getClass());
      assertEquals(value, evalue);
   }

   protected SimpleBean configureSimpleBean(String name, Object value) throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      BeanInfo info = configurator.getBeanInfo(SimpleBean.class);
      SimpleBean bean = (SimpleBean) instantiate(configurator, info);

      AbstractPropertyMetaData metaData = new AbstractPropertyMetaData(name, value.toString());
      
      configure(bean, info, metaData);
      
      return bean;
   }
   
   protected SimpleBean configureSimpleBean(String name, Object value, String type) throws Throwable
   {
      Kernel kernel = bootstrap();
      KernelConfigurator configurator = kernel.getConfigurator();
      BeanInfo info = configurator.getBeanInfo(SimpleBean.class);
      SimpleBean bean = (SimpleBean) instantiate(configurator, info);

      AbstractPropertyMetaData metaData = new AbstractPropertyMetaData(name, value.toString(), type);
      ((StringValueMetaData) metaData.getValue()).setConfigurator(configurator);
      
      configure(bean, info, metaData);
      
      return bean;
   }
}