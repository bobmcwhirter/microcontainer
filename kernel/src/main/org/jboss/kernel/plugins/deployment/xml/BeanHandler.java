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

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.api.enums.AutowireType;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * BeanHandler.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class BeanHandler extends DefaultElementHandler
{
   /** The handler */
   public static final BeanHandler HANDLER = new BeanHandler();
   
   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      return new AbstractBeanMetaData();
   }

   public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
   {
      AbstractBeanMetaData bean = (AbstractBeanMetaData) o;
      for (int i = 0; i < attrs.getLength(); ++i)
      {
         String localName = attrs.getLocalName(i);
         if ("name".equals(localName))
            bean.setName(attrs.getValue(i));
         else if ("class".equals(localName))
            bean.setBean(attrs.getValue(i));
         else if ("mode".equals(localName))
            bean.setMode(ControllerMode.getInstance(attrs.getValue(i)));
         else if ("parent".equals(localName))
            bean.setParent(attrs.getValue(i));
         else if ("abstract".equals(localName))
            bean.setAbstract(Boolean.parseBoolean(attrs.getValue(i)));
         else if ("autowire-type".equals(localName))
            bean.setAutowireType(AutowireType.getInstance(attrs.getValue(i)));
         else if ("autowire-candidate".equals(localName))
            bean.setAutowireCandidate(Boolean.parseBoolean(attrs.getValue(i)));
      }
   }

   public Object endElement(Object o, QName qName, ElementBinding element)
   {
      AbstractBeanMetaData bean = (AbstractBeanMetaData) o;
      if (bean.getBean() == null && bean.isAbstract() == false && bean.getParent() == null)
      {
         ConstructorMetaData constructor = bean.getConstructor();
         if (constructor == null)
            throw new IllegalArgumentException("Bean should have a class attribute or a constructor element.");
         if (constructor.getFactoryMethod() == null)
         {
            if (constructor.getValue() == null)
               throw new IllegalArgumentException("Bean should have a class attribute or the constructor element should have either a factoryMethod attribute or embedded value.");
         }
         else if (constructor.getFactory() == null && constructor.getFactoryClass() == null)
            throw new IllegalArgumentException("Bean should have a class attribute or the constructor element should have one of a factoryClass attribute or a factory element, or embedded value.");
      }
      return bean;
   }
}
