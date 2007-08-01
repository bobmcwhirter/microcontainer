/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.metatype.plugins.types;

import org.jboss.metatype.api.types.Name;

/**
 * A simple string based Name implementation.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class StringName implements Name
{
   private static final long serialVersionUID = 1;
   private String name;

   /**
    * 
    * @param name
    */
   public StringName(String name)
   {
      if( name == null )
         throw new IllegalArgumentException("Name cannot be null");
      this.name = name;
   }

   public int hashCode()
   {
      return name.hashCode();
   }
   public boolean equals(Object obj)
   {
      return name.equals(obj);
   }
   public String toString()
   {
      return name;
   }
}
