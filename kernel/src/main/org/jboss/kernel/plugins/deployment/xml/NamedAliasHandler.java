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

import org.jboss.beans.metadata.plugins.AbstractNamedAliasMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * The named alias handler.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class NamedAliasHandler extends AliasHandler
{
   /**
    * The alias handler
    */
   public static final NamedAliasHandler NAMED_ALIAS_HANDLER = new NamedAliasHandler();

   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      return new AbstractNamedAliasMetaData();
   }

   public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
   {
      AbstractNamedAliasMetaData alias = (AbstractNamedAliasMetaData) o;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("name".equals(localName))
            alias.setName(attrs.getValue(i));
         else if ("alias".equals(localName))
            alias.setAliasValue(attrs.getValue(i));
      }
      super.attributes(o, elementName, element, attrs, nsCtx);
   }

   public Object endElement(Object object, QName qName, ElementBinding elementBinding)
   {
      AbstractNamedAliasMetaData alias = (AbstractNamedAliasMetaData)object;
      if (alias.getName() == null)
         throw new IllegalArgumentException("Missing name: " + alias);
      return super.endElement(object, qName, elementBinding);
   }

}
