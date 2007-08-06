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

import org.jboss.deployers.structure.spi.classloading.RequirePackage;
import org.jboss.deployers.structure.spi.classloading.VersionRange;

/**
 * RequirePackageImpl.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class RequirePackageImpl implements RequirePackage
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -7552921085464308835L;

   /** The package name required */
   private String name;
   
   /** The version range */
   private VersionRange versionRange;

   /**
    * Create a new RequirePackageImpl with no version constraint
    * 
    * @param name the name
    * @throws IllegalArgumentException for a null name
    */
   public RequirePackageImpl(String name)
   {
      this(name, null);
   }
   
   /**
    * Create a new RequirePackageImpl.
    * 
    * @param name the name
    * @param versionRange the version range - pass null for all versions
    * @throws IllegalArgumentException for a null name
    */
   public RequirePackageImpl(String name, VersionRange versionRange)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (versionRange == null)
         versionRange = VersionRange.ALL_VERSIONS;
      this.name = name;
      this.versionRange = versionRange;
   }
   
   
   public String getName()
   {
      return name;
   }

   public VersionRange getVersionRange()
   {
      return versionRange;
   }
   
   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof RequirePackage == false)
         return false;
      RequirePackage other = (RequirePackage) obj;
      if (getName().equals(other.getName()) == false)
         return false;
      return getVersionRange().equals(other.getVersionRange());
   }
   
   @Override
   public int hashCode()
   {
      return getName().hashCode();
   }
   
   @Override
   public String toString()
   {
      return "RequirePackage: " + getName() + ":" + getVersionRange();
   }
}
