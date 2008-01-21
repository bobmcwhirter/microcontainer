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

import java.util.NoSuchElementException;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.jboss.deployers.structure.spi.classloading.Version;

/**
 * VersionImpl.
 * OSGi kind of version impl.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class VersionImpl extends Version
{
   private static final String SEPARATOR = ".";
   private static Pattern QUALIFIER_PATTERN = Pattern.compile("[a-zA-Z0-9_-]*");

   /**
    * The empty version.
    */
   private static final VersionImpl emptyVersion = new VersionImpl(0, 0, 0);

   private int major;
   private int minor;
   private int micro;
   private String qualifier;

   public VersionImpl(int major, int minor, int micro)
   {
      this(major, minor, micro, null);
   }

   public VersionImpl(int major, int minor, int micro, String qualifier)
   {
      this.major = major;
      this.minor = minor;
      this.micro = micro;
      if (qualifier == null)
         qualifier = "";
      this.qualifier = qualifier;
      validate();
   }

   public VersionImpl(String version)
   {
      if (version == null)
         throw new IllegalArgumentException("Null version");
      
      int major = 0;
      int minor = 0;
      int micro = 0;
      String qualifier = "";

      try
      {
         StringTokenizer st = new StringTokenizer(version, SEPARATOR, true);
         major = Integer.parseInt(st.nextToken());

         if (st.hasMoreTokens())
         {
            st.nextToken();
            minor = Integer.parseInt(st.nextToken());

            if (st.hasMoreTokens())
            {
               st.nextToken();
               micro = Integer.parseInt(st.nextToken());

               if (st.hasMoreTokens())
               {
                  st.nextToken();
                  qualifier = st.nextToken();

                  if (st.hasMoreTokens())
                  {
                     throw new IllegalArgumentException("invalid format");
                  }
               }
            }
         }
      }
      catch (NoSuchElementException e)
      {
         throw new IllegalArgumentException("invalid format");
      }

      this.major = major;
      this.minor = minor;
      this.micro = micro;
      this.qualifier = qualifier;
      validate();
   }

   /**
    * Validate arguments.
    */
   protected void validate()
   {
      if (major < 0)
         throw new IllegalArgumentException("negative major: " + major);
      if (minor < 0)
         throw new IllegalArgumentException("negative minor: " + minor);
      if (micro < 0)
         throw new IllegalArgumentException("negative micro: " + micro);
      if (QUALIFIER_PATTERN.matcher(qualifier).matches() == false)
         throw new IllegalArgumentException("invalid qualifier: " + qualifier);
   }

   /**
    * Parses a version identifier from the specified string.
    * See <code>Version(String)</code> for the format of the version string.
    *
    * @param version String representation of the version identifier. Leading
    *                and trailing whitespace will be ignored.
    * @return A <code>Version</code> object representing the version
    *         identifier. If <code>version</code> is <code>null</code> or
    *         the empty string then <code>emptyVersion</code> will be
    *         returned.
    * @throws IllegalArgumentException If <code>version</code> is improperly
    *                                  formatted.
    */
   public static VersionImpl parseVersion(String version)
   {
      if (version == null)
         return emptyVersion;

      version = version.trim();
      if (version.length() == 0)
         return emptyVersion;

      return new VersionImpl(version);
   }

   /**
    * Returns the major component of this version identifier.
    *
    * @return The major component.
    */
   public int getMajor()
   {
      return major;
   }

   /**
    * Returns the minor component of this version identifier.
    *
    * @return The minor component.
    */
   public int getMinor()
   {
      return minor;
   }

   /**
    * Returns the micro component of this version identifier.
    *
    * @return The micro component.
    */
   public int getMicro()
   {
      return micro;
   }

   /**
    * Returns the qualifier component of this version identifier.
    *
    * @return The qualifier component.
    */
   public String getQualifier()
   {
      return qualifier;
   }

   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(major).append(SEPARATOR).append(minor).append(SEPARATOR).append(micro);
      if (qualifier.length() > 0)
         builder.append(SEPARATOR).append(qualifier);
      return builder.toString();
   }

   public int hashCode()
   {
      return (major << 24) + (minor << 16) + (micro << 8) + qualifier.hashCode();
   }

   public boolean equals(Object object)
   {
      if (object == this)
         return true;

      if (object instanceof VersionImpl == false)
         return false;

      VersionImpl other = (VersionImpl)object;
      return (major == other.major) && (minor == other.minor) && (micro == other.micro) && qualifier.equals(other.qualifier);
   }

   /**
    * Compare two VersionImpls.
    *
    * @param version the other version impl
    * @return compare result
    */
   int compareTo(VersionImpl version)
   {
      if (version == this)
         return 0;

      int result = major - version.major;
      if (result != 0)
         return result;

      result = minor - version.minor;
      if (result != 0)
         return result;

      result = micro - version.micro;
      if (result != 0)
         return result;

      return qualifier.compareTo(version.qualifier);
   }
}
