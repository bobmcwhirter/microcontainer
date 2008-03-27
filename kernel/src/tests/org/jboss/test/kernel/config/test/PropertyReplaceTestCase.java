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
package org.jboss.test.kernel.config.test;

import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractAnnotationMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.test.kernel.config.support.SimpleAnnotation;

/**
 * Property replace test cases: ${x} - looking for System property named x.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class PropertyReplaceTestCase extends AbstractKernelConfigTest
{
   private static final String PROP_NAME = "test.property.value";
   private static final String BRACKET_PROP_NAME = "${" + PROP_NAME + "}";
   private static final String CONST = "PropertyReplaceTestCase";

   public PropertyReplaceTestCase(String name)
   {
      super(name);
   }

   public PropertyReplaceTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(PropertyReplaceTestCase.class);
   }

   private List<ObjectCreator> singlePropertyCreator(final boolean replace)
   {
      PropertyReplaceTestCase.ObjectCreator oc = new PropertyReplaceTestCase.ObjectCreator()
      {
         public Object createObject() throws Throwable
         {
            return instantiateReplacePropertyValue(replace);
         }
      };
      return Collections.singletonList(oc);
   }

   public void testPropertyWithPropertyValue() throws Throwable
   {
      doTestProperty(true, singlePropertyCreator(true));
   }

   public void testPropertyWithIgnoreReplace() throws Throwable
   {
      doTestProperty(false, createCreators());
   }

   public void testAnnotationPropertyReplace() throws Throwable
   {
      SecurityManager sm = suspendSecurity();
      try
      {
         replaceProperty();
         SimpleAnnotation replAnn = instantiateAnnotation(true);
         assertEquals("test." + CONST + ".Name", replAnn.name());

         SimpleAnnotation ignAnn = instantiateAnnotation(false);
         assertEquals("test." + BRACKET_PROP_NAME + ".Name", ignAnn.name());
      }
      finally
      {
         resumeSecurity(sm);
      }
   }

   protected SimpleAnnotation instantiateAnnotation(boolean replace) throws Throwable
   {
      AbstractAnnotationMetaData annotation = new AbstractAnnotationMetaData();
      annotation.setAnnotation("@org.jboss.test.kernel.config.support.SimpleAnnotation(name = \"test." + BRACKET_PROP_NAME + ".Name\")");
      annotation.setReplace(replace);
      return (SimpleAnnotation)annotation.getAnnotationInstance();
   }

   protected List<PropertyReplaceTestCase.ObjectCreator> createCreators()
   {
      return singlePropertyCreator(false);
   }

   private void replaceProperty()
   {
      // set property to be replaced
      System.setProperty(PROP_NAME, CONST);
      getLog().debug("Set "+PROP_NAME+" to: "+System.getProperty(PROP_NAME));      
   }
   private void doTestProperty(boolean replace, List<PropertyReplaceTestCase.ObjectCreator> ocs) throws Throwable
   {
      SecurityManager sm = suspendSecurity();
      try
      {
         replaceProperty();
         for(PropertyReplaceTestCase.ObjectCreator oc : ocs)
         {
            // get property
            Object value = oc.createObject();
            assertNotNull(value);
            assertEquals(String.class, value.getClass());
            String checkValue = replace ? CONST : BRACKET_PROP_NAME;
            assertEquals(checkValue, value);
         }
      }
      finally
      {
         resumeSecurity(sm);
      }
   }

   protected Object instantiateReplacePropertyValue(boolean replace) throws Throwable
   {
      PropertyMetaData property = new AbstractPropertyMetaData("key", BRACKET_PROP_NAME, String.class.getName());
      StringValueMetaData svmd = assertInstanceOf(property.getValue(), StringValueMetaData.class, false);
      svmd.setReplace(replace);
      svmd.setConfigurator(bootstrap().getConfigurator());
      return svmd.getValue(null, Thread.currentThread().getContextClassLoader());
   }

   protected interface ObjectCreator
   {
      Object createObject() throws Throwable;
   }

}
