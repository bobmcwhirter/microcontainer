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

import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * DependencyHandler.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class DependencyHandler extends DefaultElementHandler
{
   /** The handler */
   public static final DependencyHandler HANDLER = new DependencyHandler();

   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      return new AbstractDependencyValueMetaData();
   }

   public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
   {
      AbstractDependencyValueMetaData dependency = (AbstractDependencyValueMetaData) o;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("bean".equals(localName))
            dependency.setValue(attrs.getValue(i));
         else if ("property".equals(localName))
            dependency.setProperty(attrs.getValue(i));
         else if ("state".equals(localName))
            dependency.setDependentState(new ControllerState(attrs.getValue(i)));
      }
   }
   
   public Object endElement(Object o, QName qName, ElementBinding element)
   {
      AbstractDependencyValueMetaData x = (AbstractDependencyValueMetaData) o;
      String name = (String) x.getUnderlyingValue();
      if (name == null || name.trim().length() == 0)
         throw new IllegalArgumentException("Null or empty bean in injection.");
      return o;
   }
}
