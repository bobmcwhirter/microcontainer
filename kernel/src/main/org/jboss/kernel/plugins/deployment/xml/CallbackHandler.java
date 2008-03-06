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

import org.jboss.beans.metadata.plugins.AbstractCallbackMetaData;
import org.jboss.beans.metadata.plugins.InstallCallbackMetaData;
import org.jboss.beans.metadata.plugins.UninstallCallbackMetaData;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * CallbackHandler.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class CallbackHandler extends DefaultElementHandler
{
   /** The handler */
   public static final CallbackHandler HANDLER = new CallbackHandler();

   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      QName qname = element.getQName();
      boolean isInstall = qname.getLocalPart().startsWith("in");
      return isInstall ? new InstallCallbackMetaData() : new UninstallCallbackMetaData();
   }

   public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
   {
      AbstractCallbackMetaData callbackMetaData = (AbstractCallbackMetaData) o;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("property".equals(localName))
            callbackMetaData.setProperty(attrs.getValue(i));
         else if ("method".equals(localName))
            callbackMetaData.setMethodName(attrs.getValue(i));
         else if ("state".equals(localName))
            callbackMetaData.setDependentState(new ControllerState(attrs.getValue(i)));
         else if ("whenRequired".equals(localName))
            callbackMetaData.setState(new ControllerState(attrs.getValue(i)));
         else if ("signature".equals(localName))
            callbackMetaData.setSignature(attrs.getValue(i));
         else if ("cardinality".equals(localName))
            callbackMetaData.setCardinality(Cardinality.toCardinality(attrs.getValue(i)));
      }
   }

   public Object endElement(Object o, QName qName, ElementBinding element)
   {
      AbstractCallbackMetaData callbackMetaData = (AbstractCallbackMetaData) o;
      if (callbackMetaData.getMethodName() == null && callbackMetaData.getProperty() == null)
         throw new IllegalArgumentException("Install/uninstall callback should have method or property attribute.");
      if (callbackMetaData.getMethodName() != null && callbackMetaData.getProperty() != null)
         throw new IllegalArgumentException("Install/uninstall callback should have exactly one of method or property attribute.");
      return callbackMetaData;
   }
}
