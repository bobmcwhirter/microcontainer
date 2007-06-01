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

import java.io.Serializable;

import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Injection type:
 *  * ByClass - matching the class type of value (default)
 *  * ByName - matching the property name
 *  * Constructor - matching the constructor args
 *  * Auto - matching constructor or by type
 *  * None - do not autowire
 *
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
public class InjectionType extends JBossObject
   implements Serializable
{
   private static final long serialVersionUID = 2L;

   /** None */
   public static final InjectionType NONE = new InjectionType("None");

   /** Strict */
   public static final InjectionType BY_CLASS = new InjectionType("ByClass");

   /** Loose */
   public static final InjectionType BY_NAME = new InjectionType("ByName");

   /** Constructor */
   public static final InjectionType CONSTRUCTOR = new InjectionType("Constructor");

   /** Auto */
   public static final InjectionType AUTO = new InjectionType("Auto");

   /** Array */
   public static final InjectionType[] TYPES = new InjectionType[]{
         NONE,
         BY_CLASS,
         BY_NAME,
         CONSTRUCTOR,
         AUTO,
   };

   /** The type string */
   protected final String typeString;

   /**
    * Create a new state
    *
    * @param typeString the string representation
    */
   private InjectionType(String typeString)
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
    * @return InjectionType instance
    */
   public static InjectionType getInstance(String typeString)
   {
      for(InjectionType type : TYPES)
      {
         if (type.getTypeString().equalsIgnoreCase(typeString))
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

   public boolean equals(Object object)
   {
      if (object == null || object instanceof InjectionType == false)
         return false;
      InjectionType other = (InjectionType) object;
      return typeString.equals(other.getTypeString());
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append(typeString);
   }

   protected int getHashCode()
   {
      return typeString.hashCode();
   }

}
