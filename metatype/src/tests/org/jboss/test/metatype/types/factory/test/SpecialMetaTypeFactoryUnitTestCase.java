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

import junit.framework.Test;

import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.GenericMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.plugins.types.ClassMetaTypeBuilder;
import org.jboss.metatype.plugins.types.MutableCompositeMetaType;
import org.jboss.test.metatype.types.factory.support.TestGeneric;
import org.jboss.test.metatype.types.factory.support.TestGenericComposite;
import org.jboss.test.metatype.types.factory.support.TestOverrideComposite;
import org.jboss.test.metatype.types.factory.support.TestOverrideCompositeBuilder;

/**
 * SpecialMetaTypeFactoryUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class SpecialMetaTypeFactoryUnitTestCase extends AbstractMetaTypeFactoryTest
{
   /**
    * Create a testsuite for this test
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(SpecialMetaTypeFactoryUnitTestCase.class);
   }
   
   /**
    * Create a new SpecialMetaTypeFactoryUnitTestCase.
    * 
    * @param name the test name
    */
   public SpecialMetaTypeFactoryUnitTestCase(String name)
   {
      super(name);
   }

   /**
    * Test the meta type is generated correctly for java.lang.Class
    * 
    * @throws Exception for any problem
    */
   public void testClass() throws Exception
   {
      MetaType<?> actual = resolve(Class.class);
      getLog().debug("Class MetaType: " + " className=" + actual.getClassName() + " typeName=" + actual.getTypeName() + " description=" + actual.getDescription());
      assertEquals(ClassMetaTypeBuilder.CLASS_META_TYPE, actual);
   }

   /**
    * Test the meta type is generated correctly for a generic type
    * 
    * @throws Exception for any problem
    */
   public void testGeneric() throws Exception
   {
      MetaType<?> actual = resolve(TestGeneric.class);
      getLog().debug("Generic MetaType: " + " className=" + actual.getClassName() + " typeName=" + actual.getTypeName() + " description=" + actual.getDescription());
      GenericMetaType expected = new GenericMetaType(TestGeneric.class.getName(), TestGeneric.class.getName());
      assertEquals(expected, actual);
   }

   /**
    * Test the meta type is generated correctly for a generic composite
    * 
    * @throws Exception for any problem
    */
   public void testGenericComposite() throws Exception
   {
      MetaType<?> actual = resolve(TestGenericComposite.class);
      printComposite("GenericComposite MetaType: ", assertInstanceOf(actual, CompositeMetaType.class));
      MutableCompositeMetaType expected = new MutableCompositeMetaType(TestGenericComposite.class.getName(), TestGenericComposite.class.getName());
      GenericMetaType generic = new GenericMetaType(TestGeneric.class.getName(), TestGeneric.class.getName());
      expected.addItem("generic", "generic", generic);
      expected.freeze();
      assertEquals(expected, actual);
   }
   
   /**
    * Test the meta type is generated correctly for a builder
    * 
    * @throws Exception for any problem
    */
   public void testBuilder() throws Exception
   {
      TestOverrideCompositeBuilder builder = new TestOverrideCompositeBuilder();
      setBuilder(TestOverrideComposite.class, builder);
      try
      {
         MetaType<?> expected = builder.buildMetaType();
         MetaType<?> actual = resolve(TestOverrideComposite.class);
         getLog().debug("Builder: " + actual);
         assertEquals(expected, actual);
      }
      finally
      {
         setBuilder(TestOverrideComposite.class, null);
      }
   }
}
