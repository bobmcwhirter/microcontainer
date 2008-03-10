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
package org.jboss.osgi.plugins.deployment.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.jboss.osgi.plugins.metadata.AbstractListenerMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * Listener handler.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ListenerHandler extends DefaultElementHandler
{
   public static final ListenerHandler HANDLER = new ListenerHandler();

   public Object startElement(Object object, QName qName, ElementBinding elementBinding)
   {
      return new AbstractListenerMetaData();
   }

   public void attributes(Object object, QName qName, ElementBinding eb, Attributes attrs, NamespaceContext ns)
   {
      AbstractListenerMetaData listenerMetaData = (AbstractListenerMetaData) object;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         String value = attrs.getValue(i);
         if ("ref".equals(localName))
            listenerMetaData.setRef(value);
         else if ("bind-method".equals(localName))
            listenerMetaData.setBindMethod(value);
         else if ("unbind-method".equals(localName))
            listenerMetaData.setUnbindMethod(value);
      }
   }

   public Object endElement(Object object, QName qName, ElementBinding elementBinding)
   {
      AbstractListenerMetaData lmd = (AbstractListenerMetaData) object;
      if (lmd.getRef() == null)
         throw new IllegalArgumentException("Ref attribute must be set: " + this);
      return lmd;
   }

}
