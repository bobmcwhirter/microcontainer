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
package org.jboss.beans.metadata.injection;

import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
public class InjectionMode extends JBossObject
{
   /** ByType */
   public static final InjectionMode BY_TYPE = new InjectionMode("ByType");

   /** ByName */
   public static final InjectionMode BY_NAME = new InjectionMode("ByName");

   /** The state string */
   protected final String modeString;

   /**
    * Create a new state
    *
    * @param modeString the string representation
    */
   public InjectionMode(String modeString)
   {
      if (modeString == null)
         throw new IllegalArgumentException("Null mode string");
      this.modeString = modeString;
   }

   /**
    * Get the state string
    *
    * @return the state string
    */
   public String getModeString()
   {
      return modeString;
   }

   public boolean equals(Object object)
   {
      if (object == null || object instanceof InjectionMode == false)
         return false;
      InjectionMode other = (InjectionMode) object;
      return modeString.equals(other.getModeString());
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append(modeString);
   }

   protected int getHashCode()
   {
      return modeString.hashCode();
   }

}
