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
import org.jboss.osgi.plugins.metadata.AbstractDependencyMetaData;
import org.jboss.osgi.plugins.metadata.AbstractServiceMetaData;
import org.jboss.osgi.spi.metadata.DependencyMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * Service handler.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ServiceHandler extends DefaultElementHandler
{
   public static final ServiceHandler HANDLER = new ServiceHandler();

   public Object startElement(Object object, QName qName, ElementBinding elementBinding)
   {
      return new AbstractServiceMetaData();
   }

   public void attributes(Object object, QName qName, ElementBinding eb, Attributes attrs, NamespaceContext ns)
   {
      AbstractServiceMetaData serviceMetaData = (AbstractServiceMetaData) object;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         String value = attrs.getValue(i);
         if ("id".equals(localName))
            serviceMetaData.setId(value);
         else if ("ref".equals(localName))
            serviceMetaData.setRef(value);
         else if ("interface".equals(localName))
            serviceMetaData.setInterface(value);
         else if ("lazy-init".equals(localName))
            serviceMetaData.setLazyInit(Boolean.valueOf(value));
         else if ("depends-on".equals(localName))
         {
            DependencyMetaData dmd = new AbstractDependencyMetaData(value);
            serviceMetaData.setDepends(Collections.singleton(dmd));
         }
         else if ("context-classloader".equals(localName))
         {
            ClassLoaderMetaData clmd = null; // todo
            serviceMetaData.setClassLoaderMetaData(clmd);
         }
      }
   }

   public Object endElement(Object object, QName qName, ElementBinding elementBinding)
   {
      AbstractServiceMetaData smd = (AbstractServiceMetaData) object;
      if (smd.getInterface() != null && smd.getInterfaces() != null && smd.getInterfaces().isEmpty() == false)
         throw new IllegalArgumentException("Cannot set both interface definitions: " + this);
      return smd;
   }

}
