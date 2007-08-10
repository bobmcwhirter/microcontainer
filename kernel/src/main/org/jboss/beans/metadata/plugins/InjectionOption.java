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
package org.jboss.beans.metadata.plugins;

import java.io.Serializable;

import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Injection option:
 *  * Strict - exactly one matching context (default)
 *  * Callback - issue callback once context with matching is present
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class InjectionOption extends JBossObject
      implements Serializable
{
   private static final long serialVersionUID = 1L;

   /** Strict */
   public static final InjectionOption STRICT = new InjectionOption("Strict");

   /** Loose */
   public static final InjectionOption CALLBACK = new InjectionOption("Callback");

   /** The type string */
   protected final String optionString;

   /**
    * Create a new state
    *
    * @param optionString the string representation
    */
   private InjectionOption(String optionString)
   {
      if (optionString == null)
         throw new IllegalArgumentException("Null option string");
      this.optionString = optionString;
   }

   /**
    * Return injection type.
    * Or exception if no matching type.
    *
    * @param optionString type
    * @return InjectionOption instance
    */
   public static InjectionOption getInstance(String optionString)
   {
      if (STRICT.getOptionString().equalsIgnoreCase(optionString))
         return STRICT;
      else if (CALLBACK.getOptionString().equalsIgnoreCase(optionString))
         return CALLBACK;
      else
         throw new IllegalArgumentException("No such option: " + optionString);
   }

   /**
    * Get the state string
    *
    * @return the state string
    */
   public String getOptionString()
   {
      return optionString;
   }

   public boolean equals(Object object)
   {
      if (object == null || object instanceof InjectionOption == false)
         return false;
      InjectionOption other = (InjectionOption) object;
      return optionString.equals(other.getOptionString());
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append(optionString);
   }

   protected int getHashCode()
   {
      return optionString.hashCode();
   }

}
