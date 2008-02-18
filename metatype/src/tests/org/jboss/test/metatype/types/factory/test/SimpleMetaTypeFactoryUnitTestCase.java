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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import junit.framework.Test;

import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.SimpleMetaType;

/**
 * SimpleMetaTypeFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class SimpleMetaTypeFactoryUnitTestCase extends AbstractMetaTypeFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(SimpleMetaTypeFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new SimpleMetaTypeFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public SimpleMetaTypeFactoryUnitTestCase(String name)
   {
      super(name);
   }

   SimpleMetaType<?>[] expected = new SimpleMetaType[]
   {
      SimpleMetaType.BIGDECIMAL,
      SimpleMetaType.BIGINTEGER,
      SimpleMetaType.BOOLEAN,
      SimpleMetaType.BOOLEAN,
      SimpleMetaType.BYTE,
      SimpleMetaType.BYTE,
      SimpleMetaType.CHARACTER,
      SimpleMetaType.CHARACTER,
      SimpleMetaType.DATE,
      SimpleMetaType.DOUBLE,
      SimpleMetaType.DOUBLE,
      SimpleMetaType.FLOAT,
      SimpleMetaType.FLOAT,
      SimpleMetaType.INTEGER,
      SimpleMetaType.INTEGER,
      SimpleMetaType.LONG,
      SimpleMetaType.LONG,
      SimpleMetaType.SHORT,
      SimpleMetaType.SHORT,
      SimpleMetaType.STRING,
      SimpleMetaType.VOID
   };

   Class<?>[] classes = new Class<?>[]
   {
      BigDecimal.class,
      BigInteger.class,
      Boolean.class,
      Boolean.TYPE,
      Byte.class,
      Byte.TYPE,
      Character.class,
      Character.TYPE,
      Date.class,
      Double.class,
      Double.TYPE,
      Float.class,
      Float.TYPE,
      Integer.class,
      Integer.TYPE,
      Long.class,
      Long.TYPE,
      Short.class,
      Short.TYPE,
      String.class,
      Void.class
   };

   /**
    * Test the simple meta types are generated correctly
    * 
    * @throws Exception for any problem
    */
   public void testSimpleTypes() throws Exception
   {
      for (int i = 0; i < expected.length; ++i)
      {
         String className = classes[i].getName();
         MetaType<?> actual = resolve(classes[i]);
         getLog().debug("SimpleMetaType: " + className + " className=" + actual.getClassName() + " typeName=" + actual.getTypeName() + " description=" + actual.getDescription());
         assertEquals(expected[i], actual);
      }
   }
}
