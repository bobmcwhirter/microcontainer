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

import org.jboss.deployers.structure.spi.classloading.Version;

/**
 * VersionImpl.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class VersionImpl implements Version
{
   /** The delegate */
   private org.osgi.framework.Version delegate;
   
   /**
    * Create a new VersionImpl.
    * 
    * @param version the version - pass null for the default version
    * @throws IllegalArgumentException if the string does not conform to an OSGi version
    */
   public VersionImpl(String version)
   {
      delegate = org.osgi.framework.Version.parseVersion(version);
   }
   
   public int compareTo(Version o)
   {
      if (o == this)
         return 0;
      if (o == null || o instanceof VersionImpl == false)
         throw new IllegalArgumentException("Not a version impl: " + o);
      org.osgi.framework.Version other = (org.osgi.framework.Version) o;
      return delegate.compareTo(other);
   }
   
   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof VersionImpl == false)
         return false;
      org.osgi.framework.Version other = (org.osgi.framework.Version) obj;
      return delegate.equals(other);
   }
   
   @Override
   public int hashCode()
   {
      return delegate.hashCode();
   }
   
   @Override
   public String toString()
   {
      return delegate.toString();
   }
}
