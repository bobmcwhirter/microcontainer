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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.test.kernel.config.support.FromObjectSimpleBean;
import org.jboss.test.kernel.config.support.SimpleBean;

/**
 * Configuration from object Test Case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class ConfigureFromObjectAnnotationTestCase extends ConfigureFromObjectTestCase
{
   public static Test suite()
   {
      return suite(ConfigureFromObjectAnnotationTestCase.class);
   }

   public ConfigureFromObjectAnnotationTestCase(String name)
   {
      super(name);
   }

   protected SimpleBean createSimpleBean(
         String stringValue,
         Byte byteValue,
         Boolean booleanValue,
         Character characterValue,
         Short shortValue,
         Integer integerValue,
         Long longValue,
         Float floatValue,
         Double doubleValue,
         Date dateValue,
         BigDecimal bigDecimalValue,
         BigInteger bigIntegerValue,
         SimpleBean.Alphabet enumValue
   )
         throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder("SimpleBean", FromObjectSimpleBean.class.getName());
      return (SimpleBean)instantiate(builder.getBeanMetaData());
   }
}
