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
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractTypeMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.test.AbstractTestDelegate;

import java.security.AccessController;
import java.security.PrivilegedAction;

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
      // set property to be replaced
      final String CONST = "PropertyReplaceTestCase";

      AbstractTestDelegate delegate = getDelegate();
      delegate.enableSecurity = false;
      AccessController.doPrivileged(new PrivilegedAction<Object>()
      {
         public Object run()
         {
            System.setProperty("test.property.value", CONST);
            return null;
         }
      });
      
      // get property
      Object value = instantiateReplacePropertyValue();
      assertNotNull(value);
      assertEquals(String.class, value.getClass());
      assertEquals(CONST, value);
   }

   protected Object instantiateReplacePropertyValue() throws Throwable
   {
      PropertyMetaData property = new AbstractPropertyMetaData("test", "${test.property.value}", String.class.getName());
      // TODO - update with jboss-test.jar
//      AbstractTypeMetaData atmd = assertInstanceOf(property.getValue(), AbstractTypeMetaData.class, false);
      assertTrue(property.getValue() instanceof AbstractTypeMetaData);
      AbstractTypeMetaData atmd = (AbstractTypeMetaData)property.getValue();
      atmd.setConfigurator(bootstrap().getConfigurator());
      return atmd.getValue(null, Thread.currentThread().getContextClassLoader());
   }

}
