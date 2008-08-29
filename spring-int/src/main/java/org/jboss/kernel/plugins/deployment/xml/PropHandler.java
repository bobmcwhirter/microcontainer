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

import javax.xml.namespace.QName;
import javax.xml.namespace.NamespaceContext;

import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * PropertyHandler.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class PropHandler extends DefaultElementHandler
{
   /** The handler */
   public static final PropHandler HANDLER = new PropHandler();

   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      return new MapEntry();
   }

   public void attributes(Object object, QName qName, ElementBinding eb, Attributes attrs, NamespaceContext nc)
   {
      MapEntry entry = (MapEntry) object;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("key".equals(localName))
            entry.key = attrs.getValue(i);
      }
   }

   public Object endElement(Object object, QName qName, ElementBinding elementBinding)
   {
      MapEntry entry = (MapEntry) object;
      if (entry.key == null)
         throw new IllegalArgumentException("Missing key attribute: " + elementBinding);
      return entry;
   }

}
