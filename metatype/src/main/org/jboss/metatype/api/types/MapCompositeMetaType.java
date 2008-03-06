/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.metatype.api.types;

import org.jboss.metatype.plugins.types.AbstractCompositeMetaType;

/**
 * A CompositeMetaType for Map<String, MetaValue>
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class MapCompositeMetaType extends AbstractCompositeMetaType
{
   private static final long serialVersionUID = 1;
   private MetaType valueType;
   /** Cached hash code */
   private transient int cachedHashCode = Integer.MIN_VALUE;

   /** Cached string representation */
   private transient String cachedToString = null;

   /**
    * Create a MapCompositeMetaType with the given value metatype.
    * @param valueType the value meta type
    */
   public MapCompositeMetaType(MetaType valueType)
   {
      super("java.util.Map", "Map<String,MetaValue>");
      this.valueType = valueType;
   }

   /**
    * Access the map value type.
    * @return the map value type.
    */
   public MetaType getValueType()
   {
      return valueType;
   }

   /**
    * 
    * @param itemName
    */
   public void addItem(String itemName)
   {
      super.addItem(itemName, itemName, valueType);
   }

   @Override
   public boolean equals(Object obj)
   {
      return equalsImpl(obj);
   }

   @Override
   public int hashCode()
   {
      if (cachedHashCode != Integer.MIN_VALUE)
         return cachedHashCode;
      cachedHashCode = hashCodeImpl();
      return cachedHashCode;
   }

   @Override
   public String toString()
   {
      if (cachedToString == null)
         cachedToString = super.toString();
      return cachedToString;
   }
}
