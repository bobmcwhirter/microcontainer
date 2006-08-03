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

import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * ConstructorHandler.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ConstructorHandler extends DefaultElementHandler
{
   /** The handler */
   public static final ConstructorHandler HANDLER = new ConstructorHandler();

   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      return new AbstractConstructorMetaData();
   }

   public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
   {
      AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) o;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("factoryClass".equals(localName))
            constructor.setFactoryClass(attrs.getValue(i));
         else if ("factoryMethod".equals(localName))
            constructor.setFactoryMethod(attrs.getValue(i));
      }
   }

   public Object endElement(Object o, QName qName, ElementBinding element)
   {
      AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) o;
      if (constructor.getFactory() != null || constructor.getFactoryClass() != null)
         if (constructor.getFactoryMethod() == null)
            throw new IllegalArgumentException("Constructor needs a factoryMethod attribute when there is a factoryClass attribute or factory element.");
      return constructor;
   }
}
