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

import org.jboss.beans.metadata.plugins.AbstractClassLoaderMetaData;
import org.jboss.beans.metadata.plugins.AbstractCollectionMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultWildcardHandler;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;

/**
 * ValueWildcardHandler.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ValueWildcardHandler extends DefaultWildcardHandler
{
   /** The value wildcard handler */
   public static final ValueWildcardHandler WILDCARD = new ValueWildcardHandler();
   
   public void setParent(Object parent, Object o, QName elementName, ElementBinding element, ElementBinding parentElement)
   {
      ValueMetaData value;
      if (o instanceof ValueMetaData)
         value = (ValueMetaData) o;
      else
         value = new AbstractValueMetaData(o);

      if (parent instanceof AbstractCollectionMetaData)
      {
         AbstractCollectionMetaData collection = (AbstractCollectionMetaData) parent;
         collection.add(value);
      }
      else if (parent instanceof AbstractParameterMetaData)
      {
         AbstractParameterMetaData valueMetaData = (AbstractParameterMetaData) parent;
         valueMetaData.setValue(value);
      }
      else if (parent instanceof AbstractPropertyMetaData)
      {
         AbstractPropertyMetaData valueMetaData = (AbstractPropertyMetaData) parent;
         valueMetaData.setValue(value);
      }
      else if (parent instanceof AbstractClassLoaderMetaData)
      {
         AbstractClassLoaderMetaData valueMetaData = (AbstractClassLoaderMetaData) parent;
         valueMetaData.setClassLoader(value);
      }
      else if (parent instanceof AbstractConstructorMetaData)
      {
         AbstractConstructorMetaData valueMetaData = (AbstractConstructorMetaData) parent;
         valueMetaData.setValue(value);
      }
      else
      {
         AbstractValueMetaData valueMetaData = (AbstractValueMetaData) parent;
         valueMetaData.setValue(value);
      }
   }
}