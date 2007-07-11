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
package org.jboss.test.managed.mock;

import java.io.Serializable;

import org.jboss.managed.api.Fields;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * DOMFields.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockDOMFields implements Fields
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;

   /** The datasource element */
   private Element element; 
   
   /** The element name */
   private String elementName;
   
   /**
    * Create a new DOMFields.
    *
    * @param element the data source element
    * @param elementName the element name
    */
   public MockDOMFields(Element element, String elementName)
   {
      this.element = element;
      this.elementName = elementName;
   }
   
   public Serializable getField(String name)
   {
      if (NAME.equals(name))
         return elementName;
      if (VALUE.equals(name))
      {
         NodeList nodes = element.getElementsByTagName(elementName);
         if (nodes.getLength() == 0)
            return null;
         else
         {
            Element element = (Element) nodes.item(0);
            return element.getTextContent();
         }
      }
      return null;
   }

   public void setField(String name, Serializable value)
   {
      if (VALUE.equals(name))
      {
         String string = (String) value;
         NodeList nodes = element.getElementsByTagName(elementName);
         Element childElement = null;
         if (nodes.getLength() == 0)
         {
            if (string == null || string.length() == 0)
               return;
            childElement = element.getOwnerDocument().createElement(elementName);
            element.appendChild(childElement);
         }
         else
         {
            childElement = (Element) nodes.item(0);
            if (string == null || string.length() == 0)
            {
               element.removeChild(childElement);
               return;
            }
         }
         childElement.setTextContent(string);
         return;
      }
      throw new UnsupportedOperationException("setField " + name);
   }
}
