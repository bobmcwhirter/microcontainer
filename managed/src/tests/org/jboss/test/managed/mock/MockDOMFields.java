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
import java.util.ArrayList;

import org.jboss.managed.api.Fields;
import org.jboss.metatype.api.types.CompositeMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.CompositeValueSupport;
import org.jboss.metatype.api.values.MetaValue;
import org.jboss.metatype.api.values.SimpleValue;
import org.jboss.metatype.api.values.SimpleValueSupport;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * DOMFields illustrates a Fields implementation backed by a DOM element.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
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
   private MetaType elementType;
   
   /**
    * Create a new DOMFields.
    *
    * @param element the data source element
    * @param elementName the element name
    */
   public MockDOMFields(Element element, String elementName)
   {
      this(element, elementName, SimpleMetaType.STRING);
   }
   public MockDOMFields(Element element, String elementName, MetaType elementType)
   {
      this.element = element;
      this.elementName = elementName;
      this.elementType = elementType;
   }
   
   public Serializable getField(String name)
   {
      Serializable field = null;
      if (NAME.equals(name))
         field = elementName;
      else if (VALUE.equals(name))
      {
         NodeList nodes = element.getElementsByTagName(elementName);
         if (nodes.getLength() == 0)
            return null;
         else if (elementType instanceof SimpleMetaType)
         {
            Element element = (Element) nodes.item(0);
            return element.getTextContent();
         }
         else if (elementType instanceof CompositeMetaType)
         {
            Element element = (Element) nodes.item(0);
            NodeList propNodes = element.getChildNodes();
            ArrayList<String> keys = new ArrayList<String>();
            ArrayList<MetaValue> values = new ArrayList<MetaValue>();
            for(int n = 0; n < propNodes.getLength(); n ++)
            {
               Node node = propNodes.item(n);
               if( node.getNodeType() != Node.ELEMENT_NODE )
                  continue;
               Element prop = (Element) node;
               keys.add(prop.getNodeName());
               values.add(SimpleValueSupport.wrap(prop.getTextContent()));
            }
            CompositeMetaType propsType = (CompositeMetaType) elementType;
            String[] propsKeys = new String[keys.size()];
            keys.toArray(propsKeys);
            MetaValue[] propsValues = new MetaValue[values.size()];
            values.toArray(propsValues);
            field = new CompositeValueSupport(propsType, propsKeys, propsValues);
         }
         else
         {
            throw new IllegalStateException("Unable to build value for type: "+elementType);
         }
      }
      else if (META_TYPE.equals(name))
         field = elementType;
      return field;
   }

   /**
    * Handle setting the VALUE field. 
    */
   public void setField(String name, Serializable value)
   {
      if (VALUE.equals(name))
      {
         NodeList nodes = element.getElementsByTagName(elementName);
         Element childElement = null;
         if (nodes.getLength() == 0)
         {
            if (value == null)
               return;
            childElement = element.getOwnerDocument().createElement(elementName);
            element.appendChild(childElement);
         }
         else
         {
            childElement = (Element) nodes.item(0);
            if (value == null)
            {
               element.removeChild(childElement);
               return;
            }
         }

         if( value instanceof CompositeValue )
            handleCompositeValue(childElement, (CompositeValue)value);
         else if( value instanceof SimpleValue )
            handleSimpleValue(childElement, (SimpleValue)value);
         else
         {
            String string = value.toString();
            if (value == null || string.length() == 0)
            {
               element.removeChild(childElement);
            }
            else
            {
               childElement.setTextContent(string);
            }
         }
         return;
      }
      throw new UnsupportedOperationException("setField " + name);
   }

   /**
    * Map a CompositeValue into a text element structure
    * @param childElement
    * @param cvalue
    */
   protected void handleCompositeValue(Element childElement, CompositeValue cvalue)
   {
      if (cvalue == null || cvalue.values().size() == 0)
      {
         element.removeChild(childElement);
         return;
      }

      
      for(String key : cvalue.getMetaType().keySet())
      {
         if( cvalue.containsKey(key) )
         {
            MetaValue value = cvalue.get(key);
            Element keyElement = null;
            NodeList nodes = childElement.getElementsByTagName(key);
            if (nodes.getLength() == 0)
            {
               keyElement = childElement.getOwnerDocument().createElement(key);
               childElement.appendChild(keyElement);
            }
            else
            {
               keyElement = (Element) nodes.item(0);
               if (value == null)
               {
                  childElement.removeChild(keyElement);
                  continue;
               }
            }
            // We only handle SimpleValue types
            if( !(value instanceof SimpleValue) )
               throw new IllegalArgumentException(key+" value type is not a SimpleValue, "+value);
            SimpleValue svalue = (SimpleValue) value;
            handleSimpleValue(keyElement, svalue);
         }
      }
   }

   /**
    * Map a SimpleValue to a text element
    * @param childElement
    * @param svalue
    */
   protected void handleSimpleValue(Element childElement, SimpleValue svalue)
   {
      Serializable value = svalue.getValue();
      if (value == null)
      {
         element.removeChild(childElement);
      }
      else
      {
         String string = value.toString();
         childElement.setTextContent(string);
      }
   }
}
