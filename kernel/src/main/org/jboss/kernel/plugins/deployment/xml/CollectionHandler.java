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

import org.jboss.beans.metadata.plugins.AbstractArrayMetaData;
import org.jboss.beans.metadata.plugins.AbstractCollectionMetaData;
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.plugins.AbstractSetMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * CollectionHandler.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class CollectionHandler extends DefaultElementHandler
{
   /** The handler */
   public static final CollectionHandler HANDLER = new CollectionHandler();

   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      if (BeanSchemaBinding20.collectionQName.equals(name))
         return new AbstractCollectionMetaData();
      else if (BeanSchemaBinding20.listQName.equals(name))
         return new AbstractListMetaData();
      else if (BeanSchemaBinding20.setQName.equals(name))
         return new AbstractSetMetaData();
      else if (BeanSchemaBinding20.arrayQName.equals(name))
         return new AbstractArrayMetaData();
      else
         throw new IllegalArgumentException("Unknown collection qname=" + name);
   }

   public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
   {
      AbstractCollectionMetaData collection = (AbstractCollectionMetaData) o;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("class".equals(localName))
            collection.setType(attrs.getValue(i));
         else if ("elementClass".equals(localName))
            collection.setElementType(attrs.getValue(i));
      }
   }
}
