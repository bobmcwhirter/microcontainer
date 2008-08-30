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
import org.w3c.dom.Element;

/**
 * Test org.w3c.dom.Element usage in MC xml.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ElementTestCase extends AbstractKernelConfigTest
{
   public ElementTestCase(String name)
   {
      super(name);
   }

   public ElementTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(ElementTestCase.class);
   }

   public void testElement() throws Throwable
   {
      Element element = instantiateElement();
      assertNotNull(element);
      assertEquals("someelement", element.getTagName());
      assertTrue(element.hasAttribute("attrib"));
      assertEquals("someattribute", element.getAttribute("attrib"));
   }

   protected Element instantiateElement() throws Throwable
   {
      PropertyMetaData pmd = new AbstractPropertyMetaData(
            "element",
            "<someelement attrib=\"someattribute\"/>",
            Element.class.getName()
      );
      StringValueMetaData svmd = assertInstanceOf(pmd.getValue(), StringValueMetaData.class, false);
      svmd.setConfigurator(bootstrap().getConfigurator());
      return (Element)svmd.getValue(null, Thread.currentThread().getContextClassLoader());
   }

}
