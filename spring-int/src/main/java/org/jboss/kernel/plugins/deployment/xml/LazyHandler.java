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

import org.jboss.beans.metadata.plugins.AbstractLazyMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * LazyHandler.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class LazyHandler extends DefaultElementHandler
{
   /** The handler */
   public static final LazyHandler HANDLER = new LazyHandler();

   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      return new AbstractLazyMetaData();
   }

   public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
   {
      AbstractLazyMetaData lazy = (AbstractLazyMetaData) o;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("name".equals(localName))
            lazy.setName(attrs.getValue(i));
         else if ("bean".equals(localName))
            lazy.setBeanName(attrs.getValue(i));
         else if ("exposeClass".equals(localName))
            lazy.setExposeClass(Boolean.parseBoolean(attrs.getValue(i)));
      }
   }

   public Object endElement(Object o, QName qName, ElementBinding element)
   {
      AbstractLazyMetaData lazy = (AbstractLazyMetaData) o;
      if (lazy.getBeanName() == null)
         throw new IllegalArgumentException("Bean name must be set!");
      return lazy;
   }
}
