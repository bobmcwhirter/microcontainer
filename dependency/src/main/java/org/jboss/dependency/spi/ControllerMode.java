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

import java.util.Arrays;

import javax.xml.bind.annotation.XmlEnumValue;

import org.jboss.util.JBossStringBuilder;
import org.jboss.xb.annotations.JBossXmlEnum;

/**
 * Enumeration used to specify the mode a {@link ControllerContext} should be installed,
 * which has impact on what the {@link Controller} does when told to install a {@link ControllerContext}.
 * The comments on the enum constants relates to running the Microcontainer in its
 * default configuration.<p>
 * 
 * For a description of the {@link ControllerState}s mentioned, see {@link ControllerContextActions}.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @version $Revision$
 */
@JBossXmlEnum(ignoreCase=true)
public enum ControllerMode
{
   /** 
    * The {@link Controller} will install the {@link ControllerContext} all the way to the 
    *  {@link ControllerState#INSTALLED} state.  
    */ 
   AUTOMATIC("Automatic", ControllerState.INSTALLED),
   
   /**
    * The {@link Controller} will install the {@link ControllerContext} to the {@link ControllerState#DESCRIBED}
    * state and keep it there until another {@link ControllerContext} actually wants to use it for injection. 
    */
   @XmlEnumValue("On Demand") ON_DEMAND("On Demand", ControllerState.DESCRIBED),
   MANUAL("Manual"),
   /**
    * The {@link Controller} will not install the {@link ControllerContext} 
    */
   DISABLED("Disabled"),
   /**
    * The {@link Controller} will install the {@link ControllerContext} in another thread, allowing parallel deployments.
    * The {@link ControllerContext} will be installed all the way to {@link ControllerState#INSTALLED}
    */
   ASYNCHRONOUS("Asynchronous", ControllerState.INSTALLED);

   /** The mode string */
   private final String modeString;

   /** The required state */
   private ControllerState requiredState;

   /**
    * Create a new mode
    * 
    * @param modeString the mode representation
    */
   private ControllerMode(String modeString)
   {
      this(modeString, null);
   }

   private ControllerMode(String modeString, ControllerState requiredState)
   {
      if (modeString == null)
         throw new IllegalArgumentException("Null mode string");
      if (requiredState == null)
         requiredState = ControllerState.NOT_INSTALLED;

      this.modeString = modeString;
      this.requiredState = requiredState;
   }

   /**
    * Get the right enum instance from mode string.
    *
    * @param modeString the mode string param
    * @return matching enum or exception if no match
    */
   public static ControllerMode getInstance(String modeString)
   {
      if (modeString == null)
         throw new IllegalArgumentException("Null mode string.");

      for(ControllerMode cm : values())
      {
         if (modeString.equalsIgnoreCase(cm.getModeString()))
            return cm;
      }
      throw new IllegalArgumentException("No such controller mode: " + modeString + ", available: " + Arrays.toString(values()));
   }

   /**
    * Get the mode string
    * 
    * @return the mode string
    */
   public String getModeString()
   {
      return modeString;
   }

   /**
    * The required state.
    *
    * @return the required state
    */
   public ControllerState getRequiredState()
   {
      return requiredState;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append(modeString);
   }
}
