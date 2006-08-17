/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.beans.metadata.plugins;

import org.jboss.logging.Logger;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;

/**
 * String value.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class StringValueMetaData extends AbstractTypeMetaData
{
   /** The log */
   private static final Logger log = Logger.getLogger(StringValueMetaData.class);

   /**
    * Create a new string value
    */
   public StringValueMetaData()
   {
   }

   /**
    * Create a new string value
    * 
    * @param value the value
    */
   public StringValueMetaData(String value)
   {
      super(value);
   }

   /**
    * Set the value
    * 
    * @param value the value
    */
   public void setValue(String value)
   {
      super.setValue(value);
   }

   public void setValue(Object value)
   {
      if (value != null && value instanceof String == false)
         throw new ClassCastException("value is not a String: " + value);
      super.setValue(value);
   }

   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      boolean trace = log.isTraceEnabled();
      if (trace)
         log.trace("getValue value=" + value + " type=" + type + " info=" + info);

      TypeInfo typeInfo = getClassInfo(cl);
      if (typeInfo == null)
         typeInfo = info;
      if (typeInfo == null)
         throw new IllegalArgumentException("Unable to determine type for value: " + value);

      return typeInfo.convertValue(value);
   }

   public Class getType(MetaDataVisitor visitor, MetaDataVisitorNode previous) throws Throwable
   {
      if (getType() != null)
      {
         return getClass(visitor, getType());
      }
      return super.getType(visitor, previous);
   }

}
