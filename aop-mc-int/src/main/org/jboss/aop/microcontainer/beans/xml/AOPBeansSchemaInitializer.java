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
package org.jboss.aop.microcontainer.beans.xml;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.jboss.aop.microcontainer.beans.AspectBeanMetaDataFactory;
import org.jboss.kernel.plugins.deployment.xml.BeanFactoryHandler;
import org.jboss.kernel.plugins.deployment.xml.BeanSchemaBinding20;
import org.jboss.kernel.plugins.deployment.xml.BeanSchemaBindingHelper;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBindingInitializer;
import org.jboss.xb.binding.sunday.unmarshalling.TypeBinding;
import org.xml.sax.Attributes;

/**
 * AOPBeansSchemaInitializer.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AOPBeansSchemaInitializer implements SchemaBindingInitializer
{
   /** The namespace */
   private static final String AOP_BEANS_NS = "urn:jboss:aop-beans:1.0";

   /** The aspect binding */
   private static final QName aspectTypeQName = new QName(AOP_BEANS_NS, "aspectType");
   
   public SchemaBinding init(SchemaBinding schema)
   {
      // aspect binding
      TypeBinding aspectType = schema.getType(aspectTypeQName);
      BeanSchemaBindingHelper.initBeanFactoryHandlers(aspectType);
      aspectType.setHandler(new BeanFactoryHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AspectBeanMetaDataFactory();
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            super.attributes(o, elementName, element, attrs, nsCtx);

            AspectBeanMetaDataFactory factory = (AspectBeanMetaDataFactory) o;
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("pointcut".equals(localName))
                  factory.setPointcut(attrs.getValue(i));
            }
         }
      });

      // TODO FIXME???
      BeanSchemaBinding20.initArtifacts(schema);
      
      return schema;
   }
}
