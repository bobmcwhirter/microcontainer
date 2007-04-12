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
import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.ImmutableCompositeMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.SimpleMetaType;
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
    * TODO added to BaseTestCase - remove once jboss-test is updated
    * @param clazz the class
    * @return the suite
    */
   public static TestSuite suite(Class<?> clazz)
   {
      return new TestSuite(clazz);
   }
   
   public AbstractMetaTypeTest(String name)
   {
      super(name);
   }

   protected SimpleValue<String> initStringValue1()
   {
      return SimpleValueSupport.wrap("value1");
   }

   protected SimpleValue<String> initStringValue2()
   {
      return SimpleValueSupport.wrap("value2");
   }

   protected SimpleValue<String> initStringName1()
   {
      return SimpleValueSupport.wrap("name1");
   }

   protected SimpleValue<String> initStringEmpty()
   {
      return SimpleValueSupport.wrap("");
   }

   protected SimpleValue<String> initStringNull()
   {
      return new SimpleValueSupport<String>(SimpleMetaType.STRING, null);
   }

   protected SimpleValue<String> initStringWrong()
   {
      return SimpleValueSupport.wrap("wrong");
   }

   protected SimpleValue<Integer> initInteger2()
   {
      return SimpleValueSupport.wrap(new Integer(2));
   }

   protected SimpleValue<Integer> initInteger3()
   {
      return SimpleValueSupport.wrap(new Integer(3));
   }

   protected SimpleValue<Integer> initInteger4()
   {
      return SimpleValueSupport.wrap(new Integer(4));
   }

   protected SimpleValue<Integer> initIntegerNull()
   {
      return new SimpleValueSupport<Integer>(SimpleMetaType.INTEGER, null);
   }

   protected Map<String, MetaValue> initMapValues()
   {
      Map<String, MetaValue> map = new HashMap<String, MetaValue>();
      map.put("name1", initStringValue1());
      map.put("name2", initInteger2());
      return map;
   }

   protected Map<String, MetaValue> initMapValues2()
   {
      Map<String, MetaValue> map = new HashMap<String, MetaValue>();
      map.put("name1", initStringValue1());
      map.put("name2", initInteger3());
      return map;
   }

   protected Map<String, MetaValue> initMapValues3()
   {
      Map<String, MetaValue> map = new HashMap<String, MetaValue>();
      map.put("name1", initStringValue2());
      map.put("name2", initInteger3());
      return map;
   }

   protected Map<String, MetaValue> initMapValues4()
   {
      Map<String, MetaValue> map = new HashMap<String, MetaValue>();
      map.put("name1", initStringValue1());
      map.put("name2", initInteger4());
      return map;
   }
   
   protected String[] initKeys()
   {
      return new String[] { "name1", "name2" };
   }

   protected MetaValue[] initValues()
   {
      return new MetaValue[] { initStringValue1(), initInteger2() };
   }

   protected MetaValue[] initValues2()
   {
      return new MetaValue[] { initStringValue1(), initInteger3() };
   }

   protected MetaValue[] initValues4()
   {
      return new MetaValue[] { initStringValue1(), initInteger4() };
   }

   protected CompositeMetaType initCompositeMetaType()
   {
      String[] itemNames = new String[] { "name1", "name2" };
      String[] itemDescriptions = new String[] { "desc1", "desc2" };
      MetaType[] itemTypes = new MetaType[] { SimpleMetaType.STRING, SimpleMetaType.INTEGER };
      CompositeMetaType compositeMetaType = new ImmutableCompositeMetaType("typeName", "description", itemNames, itemDescriptions, itemTypes);
      return compositeMetaType;
   }

   protected CompositeMetaType initCompositeMetaType2()
   {
      String[] itemNames = new String[] { "name1", "name2" };
      String[] itemDescriptions = new String[] { "desc1", "desc2" };
      MetaType[] itemTypes = new MetaType[] { SimpleMetaType.STRING, SimpleMetaType.INTEGER };
      CompositeMetaType compositeMetaType = new ImmutableCompositeMetaType("typeName2", "description", itemNames, itemDescriptions, itemTypes);
      return compositeMetaType;
   }
}
