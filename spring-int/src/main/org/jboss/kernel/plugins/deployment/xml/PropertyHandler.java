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
package org.jboss.kernel.plugins.deployment.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * PropertyHandler.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 65585 $
 */
public class PropertyHandler extends DefaultElementHandler
{
   /** The handler */
   public static final PropertyHandler HANDLER = new PropertyHandler();

   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      return new AbstractPropertyMetaData();
   }

   public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
   {
      AbstractPropertyMetaData property = (AbstractPropertyMetaData) o;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("name".equals(localName))
            property.setName(attrs.getValue(i));
         else if ("preinstantiate".equals(localName))
            property.setPreInstantiate(Boolean.parseBoolean(attrs.getValue(i)));
         else if ("class".equals(localName) || "replace".equals(localName) || "trim".equals(localName))
         {
            StringValueMetaData svmd;
            ValueMetaData vmd = property.getValue();
            if (vmd == null || vmd instanceof StringValueMetaData == false)
            {
               svmd = new StringValueMetaData();
               property.setValue(svmd);
            }
            else
            {
               svmd = (StringValueMetaData)vmd;
            }
            if ("class".equals(localName))
               svmd.setType(attrs.getValue(i));
            else if ("replace".equals(localName))
               svmd.setReplace(Boolean.parseBoolean(attrs.getValue(i)));
            else if ("trim".equals(localName))
               svmd.setTrim(Boolean.parseBoolean(attrs.getValue(i)));
         }
      }
   }
   
   public Object endElement(Object o, QName qName, ElementBinding element)
   {
      AbstractPropertyMetaData x = (AbstractPropertyMetaData) o;
      String name = x.getName();
      if (name == null || name.trim().length() == 0)
         throw new IllegalArgumentException("Null or empty property name.");
      return o;
   }
}
