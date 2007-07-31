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

import java.util.Date;
import java.math.BigDecimal;
import java.math.BigInteger;

import junit.framework.Test;
import org.jboss.test.kernel.config.support.SimpleBean;
import org.jboss.test.kernel.config.support.XMLUtil;

/**
 * Configuration from object Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ConfigureFromObjectXMLTestCase extends ConfigureFromObjectTestCase
{
   public static Test suite()
   {
      return suite(ConfigureFromObjectXMLTestCase.class);
   }

   public ConfigureFromObjectXMLTestCase(String name)
   {
      super(name, true);
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
      XMLUtil util = bootstrapXML(true);
      return (SimpleBean) util.getBean("SimpleBean");
   }
}