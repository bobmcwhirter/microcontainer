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

import org.jboss.beans.metadata.plugins.AbstractDemandMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * DemandHandler.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class DemandHandler extends DefaultElementHandler
{
   /** The handler */
   public static final DemandHandler HANDLER = new DemandHandler();

   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      return new AbstractDemandMetaData();
   }

   public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
   {
      AbstractDemandMetaData demand = (AbstractDemandMetaData) o;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("state".equals(localName))
            demand.setWhenRequired(new ControllerState(attrs.getValue(i)));
      }
   }
   
   public Object endElement(Object o, QName qName, ElementBinding element)
   {
      AbstractDemandMetaData x = (AbstractDemandMetaData) o;
      String name = (String) x.getDemand();
      if (name == null || name.trim().length() == 0)
         throw new IllegalArgumentException("Null or empty demand.");
      return o;
   }
}
