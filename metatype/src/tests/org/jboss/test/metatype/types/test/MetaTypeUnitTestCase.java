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
package org.jboss.test.metatype.types.test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import junit.framework.Test;

import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.SimpleValue;
import org.jboss.metatype.api.values.TableValue;
import org.jboss.test.metatype.AbstractMetaTypeTest;
import org.jboss.test.metatype.types.support.MockMetaType;

/**
 * MetaTypeUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MetaTypeUnitTestCase extends AbstractMetaTypeTest
{
   public static Test suite()
   {
      return suite(MetaTypeUnitTestCase.class);
   }
   
   public MetaTypeUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testAllowedClasses() throws Exception
   {
      String[] allowedClassNames = MetaType.ALLOWED_CLASSNAMES;
      assertEquals(16, allowedClassNames.length);
      List<String> names = Arrays.asList(allowedClassNames);
      checkMetaType(names, Void.class);
      checkMetaType(names, Boolean.class);
      checkMetaType(names, Character.class);
      checkMetaType(names, Byte.class);
      checkMetaType(names, Short.class);
      checkMetaType(names, Integer.class);
      checkMetaType(names, Long.class);
      checkMetaType(names, Float.class);
      checkMetaType(names, Double.class);
      checkMetaType(names, String.class);
      checkMetaType(names, Date.class);
      checkMetaType(names, BigDecimal.class);
      checkMetaType(names, BigInteger.class);
      checkMetaType(names, SimpleValue.class);
      checkMetaType(names, CompositeValue.class);
      checkMetaType(names, TableValue.class);
   }

   public void testConstructorSimple() throws Exception
   {
      MetaType test = new MockMetaType("java.lang.Void", "type", "description");
      assertEquals("java.lang.Void", test.getClassName());
      assertEquals("type", test.getTypeName());
      assertEquals("description", test.getDescription());
      assertEquals(false, test.isArray());
   }

   public void testConstructorArray() throws Exception
   {
      MetaType test = new MockMetaType("[[Ljava.lang.Void;", "type", "description");
      assertEquals("[[Ljava.lang.Void;", test.getClassName());
      assertEquals("type", test.getTypeName());
      assertEquals("description", test.getDescription());
      assertEquals(true, test.isArray());
   }

   public void testSerializationSimple() throws Exception
   {
      testSerialization("java.lang.Void", "type", "description");
   }
 
   public void testSerializationArray() throws Exception
   {
      testSerialization("[[Ljava.lang.Void;", "type", "description");
   }

   public void testErrors() throws Exception
   {
      try
      {
         new MockMetaType(null, "dummy", "dummy");
         fail("className cannot be null");
      }
      catch (Throwable e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }

      try
      {
         new MockMetaType("", "dummy", "dummy");
         fail("className cannot be empty");
      }
      catch (Throwable e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }

      try
      {
         new MockMetaType("java.lang.Void", null, "dummy");
         fail("typeName cannot be null");
      }
      catch (Throwable e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }

      try
      {
         new MockMetaType("java.lang.Void", null, "dummy");
         fail("typeName cannot be empty");
      }
      catch (Throwable e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }

      try
      {
         new MockMetaType("java.lang.Void", "dummy", null);
         fail("description cannot be null");
      }
      catch (Throwable e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }

      try
      {
         new MockMetaType("java.lang.Void", "dummy", "");
         fail("description cannot be empty");
      }
      catch (Throwable e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }

      try
      {
         new MockMetaType("java.lang.Class", "dummy", "dummy");
         fail("className must be a MetaType");
      }
      catch (Throwable e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }

      try
      {
         new MockMetaType("[Ljava.lang.Void", "dummy", "dummy");
         fail("[Ljava.lang.Void is not a valid array");
      }
      catch (Throwable e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }

      try
      {
         new MockMetaType("[L", "dummy", "dummy");
         fail("[L is not a valid array");
      }
      catch (Throwable e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }

      try
      {
         new MockMetaType("[Xjava.lang.Void;", "dummy", "dummy");
         fail("FAILS IN RI: [Xjava.lang.Void; is not a valid array");
      }
      catch (Throwable e)
      {
         checkThrowable(IllegalArgumentException.class, e);
      }
   }
   
   private void testSerialization(String className, String type, String description) throws Exception
   {
      MetaType original = new MockMetaType(className, type, description);
      byte[] bytes = serialize(original);
      MetaType result = (MetaType) deserialize(bytes);
      assertEquals(original.getClassName(), result.getClassName());
      assertEquals(original.getTypeName(), result.getTypeName());
      assertEquals(original.getDescription(), result.getDescription());
      assertEquals(original.isArray(), result.isArray());
   }

   private void checkMetaType(List names, Class clazz) throws Exception
   {
      String name = clazz.getName();
      assertTrue(name + " is a MetaType", names.contains(name));

      new MockMetaType(name, "dummy", "dummy");

      new MockMetaType("[L"+name+";", "dummy", "dummy");

      new MockMetaType("[[[[[L"+name+";", "dummy", "dummy");
   }
}
