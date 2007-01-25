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

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class PropertyTestCase extends AbstractKernelConfigTest
{
   public PropertyTestCase(String name)
   {
      super(name);
   }

   public PropertyTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(PropertyTestCase.class);
   }

   public void testPropertyWithPropertyValue() throws Throwable
   {
      doTestProperty(true);
   }

   public void testPropertyWithIgnoreReplace() throws Throwable
   {
      doTestProperty(false);
   }

   private void doTestProperty(boolean replace) throws Throwable
   {
      SecurityManager sm = suspendSecurity();
      try
      {
         // set property to be replaced
         String PROP_NAME = "test.property.value";
         String CONST = "PropertyReplaceTestCase";
         System.setProperty(PROP_NAME, CONST);
         // get property
         Object value = instantiateReplacePropertyValue(replace);
         assertNotNull(value);
         assertEquals(String.class, value.getClass());
         String checkValue = replace ? CONST : "${" + PROP_NAME + "}"; 
         assertEquals(checkValue, value);
      }
      finally
      {
         resumeSecurity(sm);
      }
   }

   protected Object instantiateReplacePropertyValue(boolean replace) throws Throwable
   {
      PropertyMetaData property = new AbstractPropertyMetaData("test", "${test.property.value}", String.class.getName());
      StringValueMetaData svmd = assertInstanceOf(property.getValue(), StringValueMetaData.class, false);
      svmd.setReplace(replace);
      svmd.setConfigurator(bootstrap().getConfigurator());
      return svmd.getValue(null, Thread.currentThread().getContextClassLoader());
   }

}
