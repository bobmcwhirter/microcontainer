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

import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.CharactersHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.jboss.xb.binding.sunday.unmarshalling.TypeBinding;

/**
 * PropertyCharactersHandler.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class PropertyCharactersHandler extends CharactersHandler
{
   /** The interceptor */
   public static final PropertyCharactersHandler HANDLER = new PropertyCharactersHandler();

   public Object unmarshal(QName qName, TypeBinding typeBinding, NamespaceContext nsCtx, org.jboss.xb.binding.metadata.ValueMetaData valueMetaData, String value)
   {
      return new StringValueMetaData(value);
   }

   public void setValue(QName qName, ElementBinding element, Object owner, Object value)
   {
      AbstractPropertyMetaData property = (AbstractPropertyMetaData) owner;
      StringValueMetaData svmd = (StringValueMetaData) value;
      ValueMetaData vmd = property.getValue();
      if (vmd != null && vmd instanceof StringValueMetaData)
      {
         StringValueMetaData previous = (StringValueMetaData) vmd;
         svmd.setReplace(previous.isReplace());
         String type = previous.getType();
         if (type != null)
            svmd.setType(type);
      }
      property.setValue(svmd);
   }
}
