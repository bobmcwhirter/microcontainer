/*
  * JBoss, Home of Professional Open Source
  * Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.deployers.plugins.structure.vfs.war;

import java.io.IOException;

import org.jboss.logging.Logger;
import org.jboss.virtual.VirtualFile;
import org.jboss.virtual.VirtualFileFilterWithAttributes;
import org.jboss.virtual.VisitorAttributes;
import org.jboss.virtual.plugins.context.jar.JarUtils;

/**
 * Filters web-inf/lib for archives
 * 
 * @author Scott.Stark@jboss.org
 * @author adrian@jboss.org
 * @version $Revision: 44223 $
 */
public class WebInfLibFilter implements VirtualFileFilterWithAttributes
{
   /** The log */
   private static final Logger log = Logger.getLogger(WebInfLibFilter.class);
   
   /** The instance */
   public static final WebInfLibFilter INSTANCE = new WebInfLibFilter();
   
   /**
    * Singleton
    */
   private WebInfLibFilter()
   {
   }
   
   public VisitorAttributes getAttributes()
   {
      return VisitorAttributes.DEFAULT;
   }

   public boolean accepts(VirtualFile file)
   {
      try
      {
         // We want archives
         if (file.isArchive())
            return true;
         
         // Or directories pretending to be archives
         if (file.isDirectory() && JarUtils.isArchive(file.getName()))
            return true;
      }
      catch (IOException e)
      {
         log.warn("Ignoring " + file + " reason=" + e);
      }
      
      // We ignore everything else
      return false;
   }
}
