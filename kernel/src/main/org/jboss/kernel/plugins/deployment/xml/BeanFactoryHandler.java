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

import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * BeanFactoryHandler.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class BeanFactoryHandler extends DefaultElementHandler
{
   /** The handler */
   public static final BeanFactoryHandler HANDLER = new BeanFactoryHandler();
   
   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      return new GenericBeanFactoryMetaData();
   }

   public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
   {
      GenericBeanFactoryMetaData bean = (GenericBeanFactoryMetaData) o;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("name".equals(localName))
            bean.setName(attrs.getValue(i));
         else if ("class".equals(localName))
            bean.setBeanClass(attrs.getValue(i));
         else if ("mode".equals(localName))
            bean.setMode(new ControllerMode(attrs.getValue(i)));
      }
   }

   public Object endElement(Object o, QName qName, ElementBinding element)
   {
      GenericBeanFactoryMetaData bean = (GenericBeanFactoryMetaData) o;
      if (bean.getBeanClass() == null)
      {
         PropertyMetaData property = bean.getProperty("constructor");
         if (property == null)
            throw new IllegalArgumentException("BeanFactory should have a class attribute or a constructor element.");
         ValueMetaData value = property.getValue();
         if (value == null)
            throw new IllegalArgumentException("BeanFactory should have a class attribute or a constructor element.");
         ConstructorMetaData constructor = (ConstructorMetaData) value.getUnderlyingValue();
         if (constructor == null)
            throw new IllegalArgumentException("BeanFactory should have a class attribute or a constructor element.");
         if (constructor.getFactoryMethod() == null)
            throw new IllegalArgumentException("BeanFactory should have a class attribute or the constructor element should have a factoryMethod attribute.");
         if (constructor.getFactory() == null && constructor.getFactoryClass() == null)
            throw new IllegalArgumentException("BeanFactory should have a class attribute or the constructor element should have a either a factoryClass attribute or a factory element.");
      }
      return bean;
   }
}
