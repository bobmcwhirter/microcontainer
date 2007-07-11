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

import java.util.Set;

/**
 * CompositeMetaType.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface CompositeMetaType extends MetaType
{
   /**
    * Determine whether this CompositeMetaType contains the itemName
    *
    * @param itemName the item name
    * @return true when it does, false otherwise
    */
   boolean containsItem(String itemName);

   /**
    * Retrieve the description for an item name
    *
    * @param itemName the item name
    * @return the description or null when there is no such item name
    */
   String getDescription(String itemName);

   /**
    * Retrieve the meta type for an item name
    *
    * @param itemName the item name
    * @return the open type or null when there is no such item name
    */
   MetaType getType(String itemName);

   /**
    * Retrieve an unmodifiable Set view of all the item names in ascending order.
    *
    * @return the Set
    */
   Set<String> itemSet();

   /**
    * Retrieve an unmodifiable Set view of all the item names in ascending order.
    *
    * @return the Set
    */
   Set<String> keySet();
}
