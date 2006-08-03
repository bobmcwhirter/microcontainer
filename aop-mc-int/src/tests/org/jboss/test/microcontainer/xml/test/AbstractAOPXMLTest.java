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
package org.jboss.test.microcontainer.xml.test;

import java.net.URL;
import java.util.List;

import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.xb.binding.JBossXBException;

public class AbstractAOPXMLTest extends AbstractTestCaseWithSetup
{
   protected String rootName = getRootName();
   
   /**
    * Create a new AbstractXMLTest.
    * 
    * @param name the name of the test
    */
   public AbstractAOPXMLTest(String name)
   {
      super(name);
   }

   protected Object assertType(List beans, int index, Class clazz)
   {
      Object object = beans.get(index);
      assertNotNull("Got null, expected bean " + index + " to be a " + clazz.getName(), object);
      assertTrue("Got " + object.getClass() + ", expected bean " + index + " to be a " + clazz.getName(), clazz.isInstance(object));
      return object;
   }
   
   /**
    * Unmarshal an aspect
    * 
    * @param name the name
    * @param size the number of beans expected
    * @return the unmarshalled beans
    * @throws Exception for any error
    */
   protected List unmarshalBeans(String name, int size) throws Exception
   {
      BeanMetaDataFactory factory = (BeanMetaDataFactory) unmarshal(name, BeanMetaDataFactory.class);
      List beans = factory.getBeans();
      assertNotNull(beans);
      assertEquals(size, beans.size());
      return beans;
   }
   
   /**
    * Unmarshal some xml
    * 
    * @param name the name
    * @param expected the expected class
    * @return the unmarshalled object
    * @throws Exception for any error
    */
   protected Object unmarshal(String name, Class<?> expected) throws Exception
   {
      String url = findXML(name);
      Object object = getJBossXBDelegate().unmarshal(url);
      if (object == null)
         fail("No object from " + name);
      assertTrue("Object '" + object + "' cannot be assigned to " + expected.getName(), expected.isAssignableFrom(object.getClass()));
      return object;
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
   
   protected void checkJBossXBException(Class expected, Throwable throwable)
   {
      checkThrowable(JBossXBException.class, throwable);
      JBossXBException e = (JBossXBException) throwable;
      checkThrowable(expected, e.getCause());
   }

   /**
    * Setup the test delegate
    * 
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class clazz) throws Exception
   {
      return new XMLTestDelegate(clazz);
   }

   protected XMLTestDelegate getJBossXBDelegate()
   {
      return (XMLTestDelegate) getDelegate();
   }
   
   protected void setUp() throws Exception
   {
      super.setUp();
      configureLogging();
   }
   
   /**
    * Get the package root name
    * 
    * @return the root name
    */
   protected String getRootName()
   {
      String longName = getClass().getName();
      int dot = longName.lastIndexOf('.');
      if (dot != -1)
         return longName.substring(dot + 1);
      return longName;
   }
   
   protected void configureLogging()
   {
      enableTrace("org.jboss.xb");
   }
}
