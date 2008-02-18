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
package org.jboss.classloading.plugins.metadata;

import org.jboss.classloading.spi.dependency.Module;
import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.Requirement;
import org.jboss.classloading.spi.metadata.helpers.AbstractCapability;

/**
 * ModuleCapability.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ModuleCapability extends AbstractCapability implements Capability
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -5444212755644141118L;

   /**
    * Create a new ModuleCapability.
    */
   public ModuleCapability()
   {
   }
   
   /**
    * Create a new ModuleCapability with the default version
    * 
    * @param name the name
    * @throws IllegalArgumentException for a null name
    */
   public ModuleCapability(String name)
   {
      super(name);
   }
   
   /**
    * Create a new ModuleCapability.
    * 
    * @param name the name
    * @param version the version - pass null for default version
    * @throws IllegalArgumentException for a null name
    */
   public ModuleCapability(String name, Object version)
   {
      super(name, version);
   }

   public boolean resolves(Module module, Requirement requirement)
   {
      if (requirement instanceof ModuleRequirement == false)
         return false;
      ModuleRequirement moduleRequirement = (ModuleRequirement) requirement;
      if (getName().equals(moduleRequirement.getName()) == false)
         return false;
      return moduleRequirement.getVersionRange().isInRange(getVersion());
   }
   
   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof ModuleCapability == false)
         return false;
      return super.equals(obj);
   }
}
