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
package org.jboss.dependency.spi;

import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

import java.util.Map;
import java.util.HashMap;
import java.io.ObjectStreamException;
import java.io.Serializable;

/**
 * Mode of the context.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class ControllerMode extends JBossObject implements Serializable
{
   private static final long serialVersionUID = 1L;
   
   /** Automatic */
   public static final ControllerMode AUTOMATIC = new ControllerMode("Automatic");

   /** On demand */
   public static final ControllerMode ON_DEMAND = new ControllerMode("On Demand");

   /** Manual */
   public static final ControllerMode MANUAL = new ControllerMode("Manual");

   /** Disabled */
   public static final ControllerMode DISABLED = new ControllerMode("Disabled");

   /** The mode string */
   protected final String modeString;
   
   private static Map<String, ControllerMode> values = new HashMap<String, ControllerMode>();

   static
   {
      values.put(AUTOMATIC.getModeString(), AUTOMATIC);
      values.put(ON_DEMAND.getModeString(), ON_DEMAND);
      values.put(MANUAL.getModeString(), MANUAL);
      values.put(DISABLED.getModeString(), DISABLED);
   }

   /**
    * Create a new mode
    * 
    * @param modeString the mode representation
    */
   public ControllerMode(String modeString)
   {
      if (modeString == null)
         throw new IllegalArgumentException("Null mode string");
      this.modeString = modeString;
   }
   
   /**
    * Get the mode string
    * 
    * @return the mdoe string
    */
   public String getModeString()
   {
      return modeString;
   }
   
   public boolean equals(Object object)
   {
      if (object == null || object instanceof ControllerMode == false)
         return false;
      ControllerMode other = (ControllerMode) object;
      return modeString.equals(other.modeString);
   }
   
   public void toString(JBossStringBuilder buffer)
   {
      buffer.append(modeString);
   }

   protected int getHashCode()
   {
      return modeString.hashCode();
   }

   protected Object readResolve() throws ObjectStreamException
   {
      return values.get(modeString);   
   }

}
