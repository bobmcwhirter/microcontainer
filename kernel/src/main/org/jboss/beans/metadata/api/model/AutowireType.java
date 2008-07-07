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
package org.jboss.beans.metadata.api.model;

import org.jboss.util.JBossStringBuilder;
import org.jboss.xb.annotations.JBossXmlEnum;

/**
 * Autowire type:
 *  * ByClass - matching the class type of value (default)
 *  * ByName - matching the property name
 *  * Constructor - matching the constructor args
 *  * Auto - matching constructor or by type
 *  * None - do not autowire
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@JBossXmlEnum(ignoreCase=true)
public enum AutowireType
{
   NONE(MicrocontainerConstants.NONE),
   BY_CLASS(MicrocontainerConstants.BY_CLASS),
   BY_NAME(MicrocontainerConstants.BY_NAME),
   CONSTRUCTOR(MicrocontainerConstants.CONSTRUCTOR),
   AUTO(MicrocontainerConstants.AUTO);

   /** The type string */
   private final String typeString;

   /**
    * Create a new state
    *
    * @param typeString the string representation
    */
   private AutowireType(String typeString)
   {
      if (typeString == null)
         throw new IllegalArgumentException("Null type string");
      this.typeString = typeString;
   }

   /**
    * Return injection type.
    * Or NONE if no matching type.
    *
    * @param typeString type
    * @return AutowireType instance
    */
   public static AutowireType getInstance(String typeString)
   {
      if (typeString == null)
         throw new IllegalArgumentException("Null type string.");

      for(AutowireType type : values())
      {
         if (typeString.equalsIgnoreCase(type.getTypeString()))
            return type;
      }
      return NONE;
   }

   /**
    * Get the state string
    *
    * @return the state string
    */
   public String getTypeString()
   {
      return typeString;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append(typeString);
   }
}
