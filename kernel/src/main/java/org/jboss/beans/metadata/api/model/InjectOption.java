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
package org.jboss.beans.metadata.api.model;

import org.jboss.xb.annotations.JBossXmlEnum;

/**
 * Injection option defines how dependencies are injected.
 * Unless specified when creating the injection, the default is 
 * {@link #STRICT}
 *
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
@JBossXmlEnum(ignoreCase=true)
public enum InjectOption
{
   /**
    * If the injected dependency is not available
    * the target bean will wait until the dependency 
    * becomes available.
    */
   STRICT(MicrocontainerConstants.STRICT),
   /**
    * If the injected dependency is not available
    * the target bean will not wait until the dependency 
    * becomes available, but move through the controller
    * lifecycle. If the injected dependency becomes available
    * at a later stage, it is injected then. 
    */
   CALLBACK(MicrocontainerConstants.CALLBACK),
   /**
    * If the injected dependency is not available
    * the target bean will not wait until the dependency 
    * becomes available, but move through the controller
    * lifecycle. If the injected dependency becomes available
    * at a later stage, it is ignored.
    */
   OPTIONAL(MicrocontainerConstants.OPTIONAL);

   private String optionString;

   InjectOption(String optionString)
   {
      this.optionString = optionString;
   }

   public String toString()
   {
      return optionString;
   }
}
