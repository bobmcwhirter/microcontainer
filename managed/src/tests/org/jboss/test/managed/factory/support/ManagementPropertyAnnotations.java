/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.managed.factory.support;

import java.io.Serializable;

import org.jboss.managed.api.annotation.ManagementObject;
import org.jboss.managed.api.annotation.ManagementObjectID;
import org.jboss.managed.api.annotation.ManagementProperty;
import org.jboss.managed.api.annotation.ViewUse;

/**
 * Test property annotations
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
@ManagementObject
public class ManagementPropertyAnnotations implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -1L;

   /**
    * Get property 
    * 
    * @return null
    */
   @ManagementProperty(description="Property with extra annotations",
         use={ViewUse.RUNTIME})
   @ManagementObjectID(name="SomeObject", type="SomeType")
   public String getRuntime() 
   {
      return null;
   }

   @ManagementProperty(description="Property with CONFIGURATION use",
         use={ViewUse.CONFIGURATION})
   public String getConfiguration() 
   {
      return null;
   }
   public void setConfiguration(String config)
   {
   }

   @ManagementProperty(description="Property with STATISTIC use",
         use={ViewUse.STATISTIC})
   public String getStat() 
   {
      return null;
   }
}
