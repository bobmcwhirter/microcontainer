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

import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.GenericMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.CompositeValueSupport;
import org.jboss.metatype.api.values.GenericValue;
import org.jboss.metatype.api.values.GenericValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.SimpleValueSupport;
import org.jboss.metatype.plugins.types.MutableCompositeMetaType;
import org.jboss.test.metatype.values.factory.support.TestGeneric;
import org.jboss.test.metatype.values.factory.support.TestGenericComposite;
import org.jboss.test.metatype.values.factory.support.TestOverrideComposite;
import org.jboss.test.metatype.values.factory.support.TestOverrideCompositeBuilder;

/**
 * SpecialValueFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class SpecialValueFactoryUnitTestCase extends AbstractMetaValueFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(SpecialValueFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new SpecialValueFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public SpecialValueFactoryUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the value generated for a class
    * 
    * @throws Exception for any problem
    */
   public void testClass() throws Exception
   {
      CompositeMetaType metaType = assertInstanceOf(resolve(Class.class), CompositeMetaType.class);
      String[] itemNames = { "name" };
      MetaValue[] itemValues = { SimpleValueSupport.wrap(Class.class.getName()) };
      CompositeValue expected = new CompositeValueSupport(metaType, itemNames, itemValues);
      
      MetaValue result = createMetaValue(Class.class);
      CompositeValue actual = assertInstanceOf(result, CompositeValue.class);
      getLog().debug("Class.class: " + actual);
      assertEquals(expected, actual);
   }

   /**
    * Test the value generated for an object
    * 
    * @throws Exception for any problem
    */
   public void testObject() throws Exception
   {
      CompositeMetaType metaType = assertInstanceOf(resolve(Object.class), CompositeMetaType.class);
      CompositeValue expected = new CompositeValueSupport(metaType, new String[0], new MetaValue[0]);
      
      MetaValue result = createMetaValue(new Object());
      CompositeValue actual = assertInstanceOf(result, CompositeValue.class);
      getLog().debug("new Object(): " + actual);
      assertEquals(expected, actual);
   }
   
   /**
    * Test the value generated for a generic
    * 
    * @throws Exception for any problem
    */
   public void testGeneric() throws Exception
   {
      TestGeneric test = new TestGeneric("Hello");
      GenericMetaType generic = new GenericMetaType(TestGeneric.class.getName(), TestGeneric.class.getName());
      GenericValue expected = new GenericValueSupport(generic, test);
      
      MetaValue result = createMetaValue(test);
      GenericValue actual = assertInstanceOf(result, GenericValue.class);
      getLog().debug("Generic: " + actual);
      assertEquals(expected, actual);
   }
   
   /**
    * Test the value generated for a generic composite
    * 
    * @throws Exception for any problem
    */
   public void testGenericComposite() throws Exception
   {
      TestGeneric test = new TestGeneric("Hello");
      TestGenericComposite composite = new TestGenericComposite();
      composite.setGeneric(test);
      GenericMetaType generic = new GenericMetaType(TestGeneric.class.getName(), TestGeneric.class.getName());
      GenericValue genericValue = new GenericValueSupport(generic, test);
      MutableCompositeMetaType compositeType = new MutableCompositeMetaType(TestGenericComposite.class.getName(), TestGenericComposite.class.getName());
      compositeType.addItem("generic", "generic", generic);
      compositeType.freeze();
      CompositeValueSupport expected = new CompositeValueSupport(compositeType);
      expected.set("generic", genericValue);
      
      MetaValue result = createMetaValue(composite);
      CompositeValue actual = assertInstanceOf(result, CompositeValue.class);
      getLog().debug("GenericComposite: " + actual);
      assertEquals(expected, actual);
   }
   
   /**
    * Test the value generated with a builder
    * 
    * @throws Exception for any problem
    */
   public void testBuilder() throws Exception
   {
      TestOverrideCompositeBuilder builder = new TestOverrideCompositeBuilder();
      setBuilder(TestOverrideComposite.class, builder);
      try
      {
         TestOverrideComposite value = new TestOverrideComposite("Hello");
         
         MetaType metaType = builder.buildMetaType();
         MetaValue expected = builder.buildMetaValue(metaType, value);

         MetaValue actual = createMetaValue(value);
         getLog().debug("Builder: " + actual);
         assertEquals(expected, actual);
      }
      finally
      {
         setBuilder(TestOverrideComposite.class, null);
      }
   }
}
