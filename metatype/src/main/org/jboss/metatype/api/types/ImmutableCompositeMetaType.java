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
package org.jboss.metatype.api.types;

import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.plugins.types.AbstractCompositeMetaType;

/**
 * ImmutableCompositeMetaType.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ImmutableCompositeMetaType extends AbstractCompositeMetaType
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1133171306971861455L;

   /** Cached hash code */
   private transient int cachedHashCode = Integer.MIN_VALUE;

   /** Cached string representation */
   private transient String cachedToString = null;

   /**
    * Construct a composite meta type. The parameters are checked for validity.<p>
    *
    * The three arrays are internally copied. Future changes to these
    * arrays do not alter the composite type.<p>
    *
    * getClassName() returns {@link CompositeValue}
    *
    * @param typeName the name of the composite type, cannot be null or  empty
    * @param description the human readable description of the composite type, cannot be null or empty
    * @param itemNames the names of the items described by this type. Cannot
    *        be null, must contain at least one element, the elements cannot
    *        be null or empty. The order of the items is unimportant when
    *        determining equality.
    * @param itemDescriptions the human readable descriptions of the items
    *        in the same order as the itemNames, cannot be null must have the
    *        same number of elements as the itemNames. The elements cannot
    *        be null or empty.
    * @param itemTypes the MetaTypes of the items in the same order as the
    *        item names, cannot be null must have the
    *        same number of elements as the itemNames. The elements cannot
    *        be null.
    * @exception IllegalArgumentException when a parameter does not match
    *            what is described above or when itemNames contains a duplicate name.
    *            The names are case sensitive, leading and trailing whitespace
    *            is ignored.
    */
   public ImmutableCompositeMetaType(String typeName, String description, String[] itemNames, String[] itemDescriptions, MetaType[] itemTypes)
   {
      super(typeName, description, itemNames, itemDescriptions, itemTypes, false);
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
