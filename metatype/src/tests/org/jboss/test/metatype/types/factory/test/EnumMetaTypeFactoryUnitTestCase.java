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
package org.jboss.test.metatype.types.factory.test;

import java.util.ArrayList;

import junit.framework.Test;

import org.jboss.metatype.api.types.EnumMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.test.metatype.types.factory.support.TestEnum;

/**
 * EnumMetaTypeFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class EnumMetaTypeFactoryUnitTestCase extends AbstractMetaTypeFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(EnumMetaTypeFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new EnumMetaTypeFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public EnumMetaTypeFactoryUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the correct metatype is generated for an enum
    * 
    * @throws Exception for any problem
    */
   public void testEnumType() throws Exception
   {
      MetaType result = resolve(TestEnum.class);
      EnumMetaType actual = assertInstanceOf(result, EnumMetaType.class);
      ArrayList<String> expectedValues = new ArrayList<String>(3);
      expectedValues.add("ONE");
      expectedValues.add("TWO");
      expectedValues.add("THREE");
      EnumMetaType expected = new EnumMetaType(TestEnum.class.getName(), expectedValues);
      getLog().debug("Enum MetaType: className=" + actual.getClassName() + " typeName=" + actual.getTypeName() + " description=" + actual.getDescription() + " values=" + actual.getValidValues());
      assertEquals(expected, actual);
   }
}
