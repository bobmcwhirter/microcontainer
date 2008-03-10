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

import java.util.Collections;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.osgi.plugins.metadata.AbstractDependencyMetaData;
import org.jboss.osgi.plugins.metadata.AbstractReferenceMetaData;
import org.jboss.osgi.spi.metadata.DependencyMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * Reference handler.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ReferenceHandler extends DefaultElementHandler
{
   public static final ReferenceHandler HANDLER = new ReferenceHandler();

   public Object startElement(Object object, QName qName, ElementBinding elementBinding)
   {
      return new AbstractReferenceMetaData();
   }

   public void attributes(Object object, QName qName, ElementBinding eb, Attributes attrs, NamespaceContext ns)
   {
      AbstractReferenceMetaData referenceMetaData = (AbstractReferenceMetaData) object;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         String value = attrs.getValue(i);
         if ("id".equals(localName))
            referenceMetaData.setId(value);
         else if ("interface".equals(localName))
            referenceMetaData.setInterface(value);
         else if ("filter".equals(localName))
            referenceMetaData.setFilter(value);
         else if ("cardinality".equals(localName))
            referenceMetaData.setCardinality(Cardinality.toCardinality(value));
         else if ("timeout".equals(localName))
            referenceMetaData.setTimeout(Integer.valueOf(value));
         else if ("depends-on".equals(localName))
         {
            DependencyMetaData dmd = new AbstractDependencyMetaData(value);
            referenceMetaData.setDepends(Collections.singleton(dmd));
         }
         else if ("context-classloader".equals(localName))
         {
            ClassLoaderMetaData clmd = null; // todo
            referenceMetaData.setClassLoaderMetaData(clmd);
         }
      }
   }

   public Object endElement(Object object, QName qName, ElementBinding elementBinding)
   {
      AbstractReferenceMetaData rmd = (AbstractReferenceMetaData) object;
      if (rmd.getInterface() == null)
         throw new IllegalArgumentException("Interface attribute must be set: " + this);
      return rmd;
   }

}
