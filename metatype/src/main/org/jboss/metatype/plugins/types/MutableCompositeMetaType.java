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
package org.jboss.metatype.plugins.types;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.Set;

import org.jboss.metatype.api.types.MetaType;

/**
 * MutableCompositeMetaType.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MutableCompositeMetaType extends AbstractCompositeMetaType
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -8580367706705513814L;

   /** Whether the composite type is frozen */
   private transient boolean frozen = false;
   
   /** Cached hash code */
   private transient int cachedHashCode = Integer.MIN_VALUE;

   /** Cached string representation */
   private transient String cachedToString = null;

   /**
    * Construct a composite meta type with no items.
    *
    * @param typeName the name of the composite type, cannot be null or  empty
    * @param description the human readable description of the composite type, cannot be null or empty
    * @throws IllegalArgumentException when a parameter does not match what is described above.
    */
   public MutableCompositeMetaType(String typeName, String description)
   {
      super(typeName, description);
   }

   @Override
   public void addItem(String itemName, String itemDescription, MetaType<?> itemType)
   {
      if (frozen)
         throw new IllegalStateException("The type is frozen");
      super.addItem(itemName, itemDescription, itemType);
   }
   
   @Override
   public void setKeys(Set<String> keySet)
   {
      if (frozen)
         throw new IllegalStateException("The type is frozen");
      super.setKeys(keySet);
   }

   /**
    * Freeze the metatype
    */
   public void freeze()
   {
      frozen = true;
   }
   
   @Override
   public boolean equals(Object obj)
   {
      // If we aren't frozen yet, use identity
      if (frozen == false)
         return super.equals(obj);

      return equalsImpl(obj);
   }

   @Override
   public int hashCode()
   {
      // If we aren't frozen yet, use identity
      if (frozen == false)
         return super.hashCode();

      if (cachedHashCode != Integer.MIN_VALUE)
         return cachedHashCode;
      cachedHashCode = hashCodeImpl();
      return cachedHashCode;
   }

   @Override
   public String toString()
   {
      // If we aren't frozen yet, don't cache
      if (frozen == false)
         return super.toString();

      if (cachedToString == null)
         cachedToString = super.toString();
      return cachedToString;
   }

   private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException
   {
      in.defaultReadObject();
      freeze();
   }
}
