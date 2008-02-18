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
package org.jboss.test.classloading.metadata.xml;

import java.net.URL;

import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.util.UnreachableStatementException;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.builder.JBossXBBuilder;

/**
 * AbstractBuilderTest.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AbstractJBossXBTest extends AbstractTestCaseWithSetup
{
   public AbstractJBossXBTest(String name)
   {
      super(name);
   }

   /**
    * Setup the test delegate
    *
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      return new JBossXBTestDelegate(clazz);
   }

   protected JBossXBTestDelegate getJBossXBDelegate()
   {
      return (JBossXBTestDelegate) getDelegate();
   }

   @Override
   protected void setUp() throws Exception
   {
      super.setUp();
      configureLogging();
   }

   protected void tearDown() throws Exception
   {
      super.tearDown();
   }

   protected <T, U> T unmarshalObject(Class<T> expected, Class<U> reference, Class<?>... others) throws Exception
   {
      TestSchemaResolver resolver = new TestSchemaResolver();

      SchemaBinding schemaBinding = JBossXBBuilder.build(reference);
      resolver.addSchemaBinding(schemaBinding);
      if (others != null)
      {
         for (Class<?> other : others)
         {
            SchemaBinding otherBinding = JBossXBBuilder.build(other);
            resolver.addSchemaBinding(otherBinding);
         }
      }

      String testXml = findTestXml();
      Object o = unmarshal(testXml, schemaBinding);
      assertNotNull(o);
      getLog().debug("Unmarshalled " + o + " of type " + o.getClass().getName());
      try
      {
         return expected.cast(o);
      }
      catch (ClassCastException e)
      {
         fail("Expected " + expected.getName() + " got " + o.getClass().getName());
         throw new UnreachableStatementException();
      }
   }

   /**
    * Unmarshal some xml
    *
    * @param name the name
    * @param schema the schema
    * @return the unmarshalled object
    * @throws Exception for any error
    */
   protected Object unmarshal(String name, SchemaBinding schema) throws Exception
   {
      String url = findXML(name);
      return getJBossXBDelegate().unmarshal(url, schema);
   }

   protected <T, U> T unmarshalObject(Class<T> expected, Class<U> reference) throws Exception
   {
      return unmarshalObject(expected, reference, null);
   }

   protected <T> T unmarshalObject(Class<T> expected) throws Exception
   {
      return unmarshalObject(expected, expected, null);
   }

   protected String findTestXml()
   {
      return getName().substring(4) + ".xml";
   }

   /**
    * Find the xml
    *
    * @param name the name
    * @return the url of the xml
    */
   protected String findXML(String name)
   {
      URL url = getResource(name);
      if (url == null)
         fail(name + " not found");
      return url.toString();
   }

   @Override
   public void configureLogging()
   {
      //enableTrace("org.jboss.xb");
   }
}
