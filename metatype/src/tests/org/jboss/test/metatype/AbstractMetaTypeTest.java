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
package org.jboss.test.metatype;

import java.util.HashMap;
import java.util.Map;

import junit.framework.TestSuite;

import org.jboss.metatype.api.types.ArrayMetaType;
import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.ImmutableCompositeMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.types.TableMetaType;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.SimpleValue;
import org.jboss.metatype.api.values.SimpleValueSupport;
import org.jboss.test.BaseTestCase;

/**
 * AbstractMetaTypeTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractMetaTypeTest extends BaseTestCase
{
   /**
    * Create a new testsuite for the class
    * 
    * TODO move to BaseTestCase
    * @param clazz the class
    * @return the suite
    */
   public static TestSuite suite(Class<?> clazz)
   {
      return new TestSuite(clazz);
   }
   
   /**
    * Create a new AbstractMetaTypeTest.
    * 
    * @param name the test name
    */
   public AbstractMetaTypeTest(String name)
   {
      super(name);
   }

   /**
    * Initialise string value 1
    * 
    * @return the value
    */
   protected SimpleValue<String> initStringValue1()
   {
      return SimpleValueSupport.wrap("value1");
   }

   /**
    * Initialise string value 2
    * 
    * @return the value
    */
   protected SimpleValue<String> initStringValue2()
   {
      return SimpleValueSupport.wrap("value2");
   }

   /**
    * Initialise string name 1
    * 
    * @return the name
    */
   protected SimpleValue<String> initStringName1()
   {
      return SimpleValueSupport.wrap("name1");
   }

   /**
    * Initialise the empty string
    * 
    * @return the empty string
    */
   protected SimpleValue<String> initStringEmpty()
   {
      return SimpleValueSupport.wrap("");
   }

   /**
    * Initialise the null string
    * 
    * @return the null string
    */
   protected SimpleValue<String> initStringNull()
   {
      return new SimpleValueSupport<String>(SimpleMetaType.STRING, null);
   }

   /**
    * Initialise the wrong string
    * 
    * @return the wrong string
    */
   protected SimpleValue<String> initStringWrong()
   {
      return SimpleValueSupport.wrap("wrong");
   }

   /**
    * Initialise the integer 2
    * 
    * @return the value
    */
   protected SimpleValue<Integer> initInteger2()
   {
      return SimpleValueSupport.wrap(new Integer(2));
   }

   /**
    * Initialise the integer 3
    * 
    * @return the value
    */
   protected SimpleValue<Integer> initInteger3()
   {
      return SimpleValueSupport.wrap(new Integer(3));
   }

   /**
    * Initialise the integer 4
    * 
    * @return the value
    */
   protected SimpleValue<Integer> initInteger4()
   {
      return SimpleValueSupport.wrap(new Integer(4));
   }

   /**
    * Initialise the null integer
    * 
    * @return the value
    */
   protected SimpleValue<Integer> initIntegerNull()
   {
      return new SimpleValueSupport<Integer>(SimpleMetaType.INTEGER, null);
   }

   /**
    * Initialise map values
    * 
    * @return the value
    */
   protected Map<String, MetaValue> initMapValues()
   {
      Map<String, MetaValue> map = new HashMap<String, MetaValue>();
      map.put("name1", initStringValue1());
      map.put("name2", initInteger2());
      return map;
   }

   /**
    * Initialise map values 2
    * 
    * @return the value
    */
   protected Map<String, MetaValue> initMapValues2()
   {
      Map<String, MetaValue> map = new HashMap<String, MetaValue>();
      map.put("name1", initStringValue1());
      map.put("name2", initInteger3());
      return map;
   }

   /**
    * Initialise map values 3
    * 
    * @return the value
    */
   protected Map<String, MetaValue> initMapValues3()
   {
      Map<String, MetaValue> map = new HashMap<String, MetaValue>();
      map.put("name1", initStringValue2());
      map.put("name2", initInteger3());
      return map;
   }

   /**
    * Initialise map values 4
    * 
    * @return the value
    */
   protected Map<String, MetaValue> initMapValues4()
   {
      Map<String, MetaValue> map = new HashMap<String, MetaValue>();
      map.put("name1", initStringValue1());
      map.put("name2", initInteger4());
      return map;
   }
   
   /**
    * Initialise map keys
    * 
    * @return the keys
    */
   protected String[] initKeys()
   {
      return new String[] { "name1", "name2" };
   }

   /**
    * Initialise map values
    * 
    * @return the values
    */
   protected MetaValue[] initValues()
   {
      return new MetaValue[] { initStringValue1(), initInteger2() };
   }

   /**
    * Initialise map values 2
    * 
    * @return the values
    */
   protected MetaValue[] initValues2()
   {
      return new MetaValue[] { initStringValue1(), initInteger3() };
   }

   /**
    * Initialise map values 4
    * 
    * @return the values
    */
   protected MetaValue[] initValues4()
   {
      return new MetaValue[] { initStringValue1(), initInteger4() };
   }

   /**
    * Initialise a composite meta type
    * 
    * @return the type
    */
   protected CompositeMetaType initCompositeMetaType()
   {
      String[] itemNames = new String[] { "name1", "name2" };
      String[] itemDescriptions = new String[] { "desc1", "desc2" };
      MetaType[] itemTypes = new MetaType[] { SimpleMetaType.STRING, SimpleMetaType.INTEGER };
      CompositeMetaType compositeMetaType = new ImmutableCompositeMetaType("typeName", "description", itemNames, itemDescriptions, itemTypes);
      return compositeMetaType;
   }

   /**
    * Initialise a composite meta type 2
    * 
    * @return the type
    */
   protected CompositeMetaType initCompositeMetaType2()
   {
      String[] itemNames = new String[] { "name1", "name2" };
      String[] itemDescriptions = new String[] { "desc1", "desc2" };
      MetaType[] itemTypes = new MetaType[] { SimpleMetaType.STRING, SimpleMetaType.INTEGER };
      CompositeMetaType compositeMetaType = new ImmutableCompositeMetaType("typeName2", "description", itemNames, itemDescriptions, itemTypes);
      return compositeMetaType;
   }

   /**
    * Print a composite type
    * 
    * @param context the context
    * @param type the type
    */
   protected void printComposite(String context, CompositeMetaType type)
   {
      getLog().debug(context + " className=" + type.getClassName() + " typeName=" + type.getTypeName() + " description=" + type.getDescription() + " items=" + type.keySet());
   }

   /**
    * Test an array type
    * 
    * @param expected the expected
    * @param actual the actual
    * @throws Exception for any problem
    */
   protected void testArray(ArrayMetaType expected, ArrayMetaType actual) throws Exception
   {
      getLog().debug("Array MetaType: className=" + actual.getClassName() + " typeName=" + actual.getTypeName() + " description=" + actual.getDescription() + " dim=" + actual.getDimension());
      assertEquals(expected, actual);
   }

   /**
    * Test a table type
    * 
    * @param expected the expected
    * @param actual the actual
    * @throws Exception for any problem
    */
   protected void testTable(TableMetaType expected, TableMetaType actual) throws Exception
   {
      getLog().debug("Table MetaType: className=" + actual.getClassName() + " typeName=" + actual.getTypeName() + " description=" + actual.getDescription() + " index=" + actual.getIndexNames() + " row=" + actual.getRowType());
      assertEquals(expected, actual);
   }

   /**
    * Test a composite type
    * 
    * @param expected the expected
    * @param actual the actual
    * @throws Exception for any problem
    */
   protected void testComposite(CompositeMetaType expected, CompositeMetaType actual) throws Exception
   {
      printComposite("Composite MetaType", actual);
      assertEquals(expected, actual);
   }
}
