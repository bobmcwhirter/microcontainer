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
package org.jboss.test.metatype.values.factory.test;

import junit.framework.Test;

import org.jboss.metatype.api.types.EnumMetaType;
import org.jboss.metatype.api.values.EnumValue;
import org.jboss.metatype.api.values.EnumValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.test.metatype.values.factory.support.TestEnum;

/**
 * EnumValueFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class EnumValueFactoryUnitTestCase extends AbstractMetaValueFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(EnumValueFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new EnumValueFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public EnumValueFactoryUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the correct value is generate for an enum
    * 
    * @throws Exception for any problem
    */
   public void testEnumFromString() throws Exception
   {
      EnumMetaType enumType = assertInstanceOf(resolve(TestEnum.class), EnumMetaType.class);
      EnumValue expected = new EnumValueSupport(enumType, TestEnum.ONE.name());

      MetaValue result = createMetaValue(TestEnum.ONE);
      EnumValue actual = assertInstanceOf(result, EnumValue.class);
      
      getLog().debug("Enum Value: " + actual);
      assertEquals(expected, actual);
   }
   public void testEnum() throws Exception
   {
      EnumMetaType enumType = assertInstanceOf(resolve(TestEnum.class), EnumMetaType.class);
      EnumValue expected = new EnumValueSupport(enumType, TestEnum.ONE);

      MetaValue result = createMetaValue(TestEnum.ONE);
      EnumValue actual = assertInstanceOf(result, EnumValue.class);
      
      getLog().debug("Enum Value: " + actual);
      assertEquals(expected, actual);
   }
}
