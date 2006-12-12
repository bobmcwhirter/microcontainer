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
package org.jboss.spring.deployment.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractLifecycleMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.xml.sax.Attributes;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SpringBeanHandler extends DefaultElementHandler
{
   /**
    * The beans handler
    */
   public static final SpringBeanHandler HANDLER = new SpringBeanHandler();

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
         if ("id".equals(localName))
            bean.setName(attrs.getValue(i));
         else if ("class".equals(localName))
            bean.setBean(attrs.getValue(i));
         else if ("init-method".equals(localName))
            bean.setCreate(new AbstractLifecycleMetaData(attrs.getValue(i)));
         else if ("destroy-method".equals(localName))
            bean.setDestroy(new AbstractLifecycleMetaData(attrs.getValue(i)));
         else if ("factory-method".equals(localName) || "factory-bean".equals(localName))
         {
            AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) bean.getConstructor();
            if (constructor == null)
            {
               constructor = new AbstractConstructorMetaData();
               bean.setConstructor(constructor);
            }
            if ("factory-method".equals(localName))
               constructor.setFactoryMethod(attrs.getValue(i));
            if ("factory-bean".equals(localName))
               constructor.setFactory(new AbstractDependencyValueMetaData(attrs.getValue(i)));
         }
      }
   }

}
