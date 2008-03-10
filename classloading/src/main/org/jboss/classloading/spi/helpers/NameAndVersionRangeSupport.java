/*
* JBoss, Home of Professional Open Source
* Copyright 2008, JBoss Inc., and individual contributors as indicated
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
package org.jboss.classloading.spi.helpers;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.jboss.classloading.spi.version.Version;
import org.jboss.classloading.spi.version.VersionRange;
import org.jboss.metatype.api.annotations.CompositeKey;
import org.jboss.metatype.api.annotations.CompositeValue;

/**
 * NameAndVersionRangeSupport.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class NameAndVersionRangeSupport implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 3318997559330427708L;

   /** The name  */
   private String name = "<unknown>";
   
   /** The version range */
   private VersionRange versionRange = VersionRange.ALL_VERSIONS;
   
   /**
    * Create a new NameAndVersionRangeSupport
    */
   public NameAndVersionRangeSupport()
   {
   }

   /**
    * Create a new NameAndVersionRangeSupport with no version constraint
    * 
    * @param name the name
    * @throws IllegalArgumentException for a null name
    */
   public NameAndVersionRangeSupport(String name)
   {
      this(name, null);
   }
   
   /**
    * Create a new NameAndVersionRangeSupport.
    * 
    * @param name the name
    * @param versionRange the version range - pass null for all versions
    * @throws IllegalArgumentException for a null name
    */
   public NameAndVersionRangeSupport(String name, VersionRange versionRange)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (versionRange == null)
         versionRange = VersionRange.ALL_VERSIONS;
      
      this.name = name;
      this.versionRange = versionRange;
   }
   
   /**
    * Get the name.
    * 
    * @return the name.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Set the name.
    * 
    * @param name the name.
    */
   @CompositeKey
   @XmlAttribute
   public void setName(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      this.name = name;
   }

   /**
    * Get the versionRange.
    * 
    * @return the versionRange.
    */
   public VersionRange getVersionRange()
   {
      return versionRange;
   }

   /**
    * Set the versionRange.
    * 
    * @param versionRange the versionRange.
    */
   @CompositeValue(ignore=true)
   @XmlTransient
   public void setVersionRange(VersionRange versionRange)
   {
      if (versionRange == null)
         versionRange = VersionRange.ALL_VERSIONS;
      this.versionRange = versionRange;
   }
   
   /**
    * Get the fromVersion.
    * 
    * @return the fromVersion.
    */
   public Object getFrom()
   {
      return versionRange.getLow();
   }

   /**
    * Set the fromVersion.
    * 
    * @param fromVersion the fromVersion.
    */
   @CompositeValue(ignore=true)
   @XmlTransient
   public void setFrom(Object fromVersion)
   {
      versionRange = new VersionRange(fromVersion, versionRange.isLowInclusive(), versionRange.getHigh(), versionRange.isHighInclusive());
   }
   
   /**
    * Get the fromVersion.
    * 
    * @return the fromVersion.
    */
   public Version getFromVersion()
   {
      Object from = getFrom();
      if (from == null)
         return null;
      if (from instanceof Version)
         return (Version) from;
      if (from instanceof String)
         return Version.parseVersion((String) from);
      throw new IllegalStateException(from + " is not an instance of version");
   }

   /**
    * Set the fromVersion.
    * 
    * @param fromVersion the fromVersion.
    */
   @CompositeKey
   @CompositeValue(name="from")
   @XmlAttribute(name="from")
   public void setFromVersion(Version fromVersion)
   {
      setFrom(fromVersion);
   }

   /**
    * Get the fromVersionInclusive.
    * 
    * @return the fromVersionInclusive.
    */
   public boolean isFromVersionInclusive()
   {
      return versionRange.isLowInclusive();
   }

   /**
    * Set the fromVersionInclusive.
    * 
    * @param fromVersionInclusive the fromVersionInclusive.
    */
   @CompositeKey
   @XmlAttribute(name="from-inclusive")
   public void setFromVersionInclusive(boolean fromVersionInclusive)
   {
      versionRange = new VersionRange(versionRange.getLow(), fromVersionInclusive, versionRange.getHigh(), versionRange.isHighInclusive());
   }

   /**
    * Get the toVersion.
    * 
    * @return the toVersion.
    */
   public Object getTo()
   {
      return versionRange.getHigh();
   }

   /**
    * Set the toVersion.
    * 
    * @param toVersion the toVersion.
    */
   @CompositeValue(ignore=true)
   @XmlTransient
   public void setTo(Object toVersion)
   {
      versionRange = new VersionRange(versionRange.getLow(), versionRange.isLowInclusive(), toVersion, versionRange.isHighInclusive());
   }

   /**
    * Get the toVersion.
    * 
    * @return the toVersion.
    */
   public Version getToVersion()
   {
      Object to = getTo();
      if (to == null)
         return null;
      if (to instanceof Version)
         return (Version) to;
      if (to instanceof String)
         return Version.parseVersion((String) to);
      throw new IllegalStateException(to + " is not an instance of version");
   }

   /**
    * Set the toVersion.
    * 
    * @param toVersion the toVersion.
    */
   @CompositeKey
   @CompositeValue(name="to")
   @XmlAttribute(name="to")
   public void setToVersion(Version toVersion)
   {
      setTo(toVersion);
   }

   /**
    * Get the toVersionInclusive.
    * 
    * @return the toVersionInclusive.
    */
   public boolean isToVersionInclusive()
   {
      return versionRange.isHighInclusive();
   }

   /**
    * Set the toVersionInclusive.
    * 
    * @param toVersionInclusive the toVersionInclusive.
    */
   @CompositeKey
   @XmlAttribute(name="to-inclusive")
   public void setToVersionInclusive(boolean toVersionInclusive)
   {
      versionRange = new VersionRange(versionRange.getLow(), versionRange.isLowInclusive(), versionRange.getHigh(), toVersionInclusive);
   }
   
   /**
    * Set a single version as the version range
    * 
    * @param version the version
    */
   @CompositeValue(ignore=true)
   @XmlAttribute(name="version")
   public void setVersion(Version version)
   {
      versionRange = new VersionRange(version, true, version, true);
   }
   
   @Override
   public boolean equals(Object obj)
   {
      if (obj == this)
         return true;
      if (obj == null || obj instanceof NameAndVersionRangeSupport == false)
         return false;
      NameAndVersionRangeSupport other = (NameAndVersionRangeSupport) obj;
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
      return getClass().getSimpleName() + " " + getName() + ":" + getVersionRange();
   }
}
