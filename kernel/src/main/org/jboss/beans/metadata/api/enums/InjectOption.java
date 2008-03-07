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
package org.jboss.beans.metadata.api.enums;

/**
 * Injection option - strict or optional / callback.
 *
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public enum InjectOption
{
   STRICT(MicrocontainerConstants.STRICT),
   CALLBACK(MicrocontainerConstants.CALLBACK);

   private String optionString;

   InjectOption(String optionString)
   {
      this.optionString = optionString;
   }

   public String toString()
   {
      return optionString;
   }

   /**
    * Get the option enum.
    *
    * @param optionString the option string
    * @return the inject option enum instance
    */
   // TODO - remove this once JBMICROCONT-219 is done
   public static InjectOption getInstance(String optionString)
   {
      if (optionString == null)
         throw new IllegalArgumentException("Null option string.");

      for (InjectOption io : values())
      {
         if (optionString.equalsIgnoreCase(io.optionString))
            return io;
      }
      throw new IllegalArgumentException("No such option string: " + optionString);
   }
}
