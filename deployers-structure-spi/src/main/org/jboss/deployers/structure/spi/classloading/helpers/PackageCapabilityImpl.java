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
package org.jboss.deployers.structure.spi.classloading.helpers;

import org.jboss.deployers.structure.spi.DeploymentUnit;
import org.jboss.deployers.structure.spi.classloading.PackageCapability;
import org.jboss.deployers.structure.spi.classloading.RequirePackage;
import org.jboss.deployers.structure.spi.classloading.Requirement;
import org.jboss.deployers.structure.spi.classloading.Version;

/**
 * PackageCapabilityImpl.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class PackageCapabilityImpl implements PackageCapability
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -1586959469453286396L;

   /** The package name  */
   private String name;
   
   /** The version */
   private Version version;

   /**
    * Create a new PackageCapabilityImpl with the default version
    * 
    * @param name the name
    * @throws IllegalArgumentException for a null name
    */
   public PackageCapabilityImpl(String name)
   {
      this(name, null);
   }
   
   /**
    * Create a new PackageCapabilityImpl.
    * 
    * @param name the name
    * @param version the version - pass null for default version
    * @throws IllegalArgumentException for a null name
    */
   public PackageCapabilityImpl(String name, Version version)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (version == null)
         version = Version.DEFAULT_VERSION;
      this.name = name;
      this.version = version;
   }
   
   
   public String getName()
   {
      return name;
   }

   public Version getVersion()
   {
      return version;
   }

   public boolean resolves(DeploymentUnit unit, Requirement requirement)
   {
      if (requirement instanceof RequirePackage == false)
         return false;
      RequirePackage requirePackage = (RequirePackage) requirement;
      if (getName().equals(requirePackage.getName()) == false)
         return false;
      return requirePackage.getVersionRange().isInRange(getVersion());
   }
   
   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof PackageCapability == false)
         return false;
      PackageCapability other = (PackageCapability) obj;
      if (getName().equals(other.getName()) == false)
         return false;
      return getVersion().equals(other.getVersion());
   }
   
   @Override
   public int hashCode()
   {
      return getName().hashCode();
   }
   
   @Override
   public String toString()
   {
      return "Package: " + getName() + ":" + getVersion();
   }
}
