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
package org.jboss.osgi.plugins.facade.helpers;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jboss.logging.Logger;
import org.jboss.osgi.plugins.facade.BundleImpl;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.VirtualFileVisitor;
import org.jboss.virtual.VisitorAttributes;

/**
 * A BundleEntryVisitor - VirtualFile visitor used to find Bundle entries matching a file pattern
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public class BundleEntryVisitor implements VirtualFileVisitor
{
   private final boolean recurse;

   private final Pattern filePattern;

   private List<URL> entries = new ArrayList<URL>();

   /** The log */
   private static final Logger log = Logger.getLogger(BundleImpl.class);

   /**
    * Create a new BundleEntryVisitor.
    * 
    * @param filePattern The file pattern
    * @param recurse Should recurse
    */
   public BundleEntryVisitor(String filePattern, boolean recurse)
   {
      this.filePattern = convertToPattern(filePattern);
      this.recurse = recurse;
   }

   /**
    * Get attributes used to define visitor behavior 
    * 
    * @return visitor attributes
    */
   public VisitorAttributes getAttributes()
   {
      if (recurse)
      {
         return VisitorAttributes.RECURSE_LEAVES_ONLY;
      }
      else
      {
         return VisitorAttributes.LEAVES_ONLY;
      }
   }

   /**
    * Visit an individual file and match against pattern
    * 
    * @param file File to compare to pattern
    */
   public void visit(VirtualFile file)
   {
      Matcher matcher = filePattern.matcher(file.getName());
      if (matcher.find())
      {
         try
         {
            entries.add(file.toURL());
         }
         catch (Exception e)
         {
            // TODO Handle Exception
            log.error("Failed to get URL for VirtualFile " + file.getName(), e);
         }
      }
   }

   /**
    * Get entries matching the file pattern  
    * 
    * @return Entry List
    */
   public List<URL> getEntries()
   {
      return entries;
   }

   /**
    * Convert file pattern (RFC 1960-based Filter) into a RegEx pattern
    * 
    * @param filePattern
    * @return Regular expressions pattern
    */
   private Pattern convertToPattern(String filePattern)
   {
      return Pattern.compile(new StringBuilder().append("^").append(filePattern.replace("*", ".*")).append("$")
            .toString());
   }

}
