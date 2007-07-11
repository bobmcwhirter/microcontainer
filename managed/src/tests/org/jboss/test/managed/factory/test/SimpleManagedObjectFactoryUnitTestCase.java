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
package org.jboss.test.managed.factory.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import junit.framework.Test;

import org.jboss.managed.api.ManagedObject;
import org.jboss.test.managed.factory.AbstractManagedObjectFactoryTest;
import org.jboss.test.managed.factory.support.Simple;

/**
 * SimpleManagedObjectFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class SimpleManagedObjectFactoryUnitTestCase extends AbstractManagedObjectFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(SimpleManagedObjectFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new SimpleManagedObjectFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public SimpleManagedObjectFactoryUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the correct managed object is generated for a simple type
    * 
    * @throws Exception for any problem
    */
   public void testSimpleSkeleton() throws Exception
   {
      ManagedObject managedObject = createManagedObject(Simple.class);
      checkManagedObjectDefaults(Simple.class, managedObject);
      checkDefaultManagedProperties(managedObject, Simple.class);
      checkPropertyDefaults(managedObject, "bigDecimalValue", BigDecimal.class);
      checkPropertyDefaults(managedObject, "bigIntegerValue", BigInteger.class);
      checkPropertyDefaults(managedObject, "booleanvalue", Boolean.TYPE, Boolean.FALSE);
      checkPropertyDefaults(managedObject, "booleanValue", Boolean.class);
      checkPropertyDefaults(managedObject, "bytevalue", Byte.TYPE, new Byte((byte) 0));
      checkPropertyDefaults(managedObject, "byteValue", Byte.class);
      checkPropertyDefaults(managedObject, "charactervalue", Character.TYPE, new Character('\0'));
      checkPropertyDefaults(managedObject, "characterValue", Character.class);
      checkPropertyDefaults(managedObject, "dateValue", Date.class);
      checkPropertyDefaults(managedObject, "doublevalue", Double.TYPE, new Double(0));
      checkPropertyDefaults(managedObject, "doubleValue", Double.class);
      checkPropertyDefaults(managedObject, "floatvalue", Float.TYPE, new Float(0));
      checkPropertyDefaults(managedObject, "floatValue", Float.class);
      checkPropertyDefaults(managedObject, "integervalue", Integer.TYPE, new Integer(0));
      checkPropertyDefaults(managedObject, "integerValue", Integer.class);
      checkPropertyDefaults(managedObject, "longvalue", Long.TYPE, new Long(0));
      checkPropertyDefaults(managedObject, "longValue", Long.class);
      checkPropertyDefaults(managedObject, "shortvalue", Short.TYPE, new Short((short) 0));
      checkPropertyDefaults(managedObject, "shortValue", Short.class);
      checkPropertyDefaults(managedObject, "stringValue", String.class);
   }

   /**
    * Test the correct managed object is generated for a simple value
    * 
    * @throws Exception for any problem
    */
   public void testSimple() throws Exception
   {
      BigDecimal bigDecimal = new BigDecimal(10);
      BigInteger bigInteger = BigInteger.ONE;
      Date date = new Date(System.currentTimeMillis());
      
      Simple simple = new Simple();
      simple.setBigDecimalValue(bigDecimal);
      simple.setBigIntegerValue(bigInteger);
      simple.setBooleanvalue(true);
      simple.setBooleanValue(Boolean.FALSE);
      simple.setBytevalue((byte) 1);
      simple.setByteValue(new Byte((byte) 2));
      simple.setCharactervalue('a');
      simple.setCharacterValue(new Character('b'));
      simple.setDateValue(date);
      simple.setDoublevalue(1);
      simple.setDoubleValue(new Double(2));
      simple.setFloatvalue(1);
      simple.setFloatValue(new Float(2));
      simple.setIntegervalue(1);
      simple.setIntegerValue(new Integer(2));
      simple.setLongvalue(1);
      simple.setLongValue(new Long(2));
      simple.setShortvalue((short) 1);
      simple.setShortValue(new Short((short) 2));
      simple.setStringValue("Hello");

      ManagedObject managedObject = initManagedObject(simple);
      checkManagedObjectDefaults(Simple.class, managedObject, simple);
      checkDefaultManagedProperties(managedObject, Simple.class);
      checkPropertyDefaults(managedObject, "bigDecimalValue", BigDecimal.class, bigDecimal);
      checkPropertyDefaults(managedObject, "bigIntegerValue", BigInteger.class, bigInteger);
      checkPropertyDefaults(managedObject, "booleanvalue", Boolean.TYPE, Boolean.TRUE);
      checkPropertyDefaults(managedObject, "booleanValue", Boolean.class, Boolean.FALSE);
      checkPropertyDefaults(managedObject, "bytevalue", Byte.TYPE, new Byte((byte) 1));
      checkPropertyDefaults(managedObject, "byteValue", Byte.class, new Byte((byte) 2));
      checkPropertyDefaults(managedObject, "charactervalue", Character.TYPE, new Character('a'));
      checkPropertyDefaults(managedObject, "characterValue", Character.class, new Character('b'));
      checkPropertyDefaults(managedObject, "dateValue", Date.class, date);
      checkPropertyDefaults(managedObject, "doublevalue", Double.TYPE, new Double(1));
      checkPropertyDefaults(managedObject, "doubleValue", Double.class, new Double(2));
      checkPropertyDefaults(managedObject, "floatvalue", Float.TYPE, new Float(1));
      checkPropertyDefaults(managedObject, "floatValue", Float.class, new Float(2));
      checkPropertyDefaults(managedObject, "integervalue", Integer.TYPE, new Integer(1));
      checkPropertyDefaults(managedObject, "integerValue", Integer.class, new Integer(2));
      checkPropertyDefaults(managedObject, "longvalue", Long.TYPE, new Long(1));
      checkPropertyDefaults(managedObject, "longValue", Long.class, new Long(2));
      checkPropertyDefaults(managedObject, "shortvalue", Short.TYPE, new Short((short) 1));
      checkPropertyDefaults(managedObject, "shortValue", Short.class, new Short((short) 2));
      checkPropertyDefaults(managedObject, "stringValue", String.class, "Hello");
   }
}
