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

import java.util.List;
import java.util.ArrayList;
import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.jboss.beans.metadata.plugins.AbstractValueFactoryMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * ValueFactoryHandler.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class ValueFactoryHandler extends DefaultElementHandler
{
   /** The handler */
   public static final ValueFactoryHandler HANDLER = new ValueFactoryHandler();

   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      return new AbstractValueFactoryMetaData();
   }

   public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
   {
      AbstractValueFactoryMetaData valueFactory = (AbstractValueFactoryMetaData) o;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("bean".equals(localName))
            valueFactory.setValue(attrs.getValue(i));
         else if ("method".equals(localName))
            valueFactory.setMethod(attrs.getValue(i));
         else if ("state".equals(localName))
            valueFactory.setDependentState(new ControllerState(attrs.getValue(i)));
         else if ("whenRequired".equals(localName))
            valueFactory.setWhenRequiredState(new ControllerState(attrs.getValue(i)));
         else if ("parameter".equals(localName))
            valueFactory.setParameter(attrs.getValue(i));
         else if ("default".equals(localName))
            valueFactory.setDefaultValue(attrs.getValue(i));
      }
   }

   public Object endElement(Object o, QName qName, ElementBinding element)
   {
      AbstractValueFactoryMetaData vf = (AbstractValueFactoryMetaData)o;
      if (vf.getUnderlyingValue() == null || vf.getMethod() == null)
         throw new IllegalArgumentException("Bean or method cannot null: " + vf);
      if (vf.getParameter() != null && vf.getParameters() != null)
         throw new IllegalArgumentException("Both parameter and parameters cannot be set: " + vf);
      if (vf.getParameter() != null)
      {
         List<ParameterMetaData> parameters = new ArrayList<ParameterMetaData>();
         parameters.add(new AbstractParameterMetaData(String.class.getName(), vf.getParameter()));
         vf.setParameters(parameters);
      }
      return vf;
   }
}
