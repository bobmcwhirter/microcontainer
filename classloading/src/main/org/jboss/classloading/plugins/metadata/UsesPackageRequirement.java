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

import org.jboss.classloading.spi.version.VersionRange;

/**
 * UsesPackageRequirement.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class UsesPackageRequirement extends PackageRequirement
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -7552921085464308835L;

   /**
    * Create a new UsesPackageRequirement.
    */
   public UsesPackageRequirement()
   {
      init();
   }
   
   /**
    * Create a new UsesPackageRequirement with no version constraint
    * 
    * @param name the name
    * @throws IllegalArgumentException for a null name
    */
   public UsesPackageRequirement(String name)
   {
      super(name);
      init();
   }
   
   /**
    * Create a new UsesPackageRequirement.
    * 
    * @param name the name
    * @param versionRange the version range - pass null for all versions
    * @throws IllegalArgumentException for a null name
    */
   public UsesPackageRequirement(String name, VersionRange versionRange)
   {
      super(name, versionRange);
      init();
   }
   
   /**
    * Initialise the requirement
    */
   protected void init()
   {
      setOptional(true);
   }
}
