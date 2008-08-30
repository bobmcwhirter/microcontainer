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

import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.plugins.ValueMetaDataAware;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.CharactersHandler;
import org.jboss.xb.binding.sunday.unmarshalling.TypeBinding;

/**
 * Abstract StringValueMetaData helper.
 * Looks for previous value on the ValueMetaDataAware.
 * If the previous value is StringValueMD it applies its
 * property values to the new StringValueMetaData instance.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class StringValueCharactersHandler extends CharactersHandler
{
   public Object unmarshal(QName qName, TypeBinding typeBinding, NamespaceContext nsCtx, org.jboss.xb.binding.metadata.ValueMetaData valueMetaData, String value)
   {
      return new StringValueMetaData(value);
   }

   protected void setStringValue(ValueMetaDataAware vmda, StringValueMetaData svmd)
   {
      ValueMetaData vmd = vmda.getValue();
      if (vmd != null && vmd instanceof StringValueMetaData)
      {
         StringValueMetaData previous = (StringValueMetaData) vmd;
         svmd.setReplace(previous.isReplace());
         svmd.setTrim(previous.isTrim());
         String type = previous.getType();
         if (type != null)
            svmd.setType(type);
      }
      vmda.setValue(svmd);
   }
}
