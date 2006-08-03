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

import javax.xml.namespace.QName;

import org.jboss.beans.metadata.plugins.AbstractAnnotationMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;

/**
 * AnnotationHandler.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AnnotationHandler extends DefaultElementHandler
{
   /** The handler */
   public static final AnnotationHandler HANDLER = new AnnotationHandler();

   public Object startElement(Object parent, QName name, ElementBinding element)
   {
      return new AbstractAnnotationMetaData();
   }

   public Object endElement(Object o, QName qName, ElementBinding element)
   {
      AbstractAnnotationMetaData annotation = (AbstractAnnotationMetaData) o;
      if (annotation.getAnnotation() == null || annotation.getAnnotation().length() == 0)
      {
         throw new IllegalArgumentException("Empty <annotation/> content");
      }
      if (!annotation.getAnnotation().startsWith("@"))
      {
         throw new IllegalArgumentException("<annotation/> content must be a fully qualified annotation type name prefixed with '@'");
      }
      
      return annotation;
   }
}
