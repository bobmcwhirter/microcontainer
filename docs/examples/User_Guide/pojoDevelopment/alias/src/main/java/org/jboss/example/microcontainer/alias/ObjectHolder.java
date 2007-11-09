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
package org.jboss.example.microcontainer.alias;

import java.util.Set;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ObjectHolder
{
   private Nameable nameable;
   private Set<Nameable> names;

   public ObjectHolder(Nameable nameable)
   {
      this.nameable = nameable;
   }

   public void setNameable(Nameable nameable)
   {
      this.nameable = nameable;
   }

   public void setNames(Set<Nameable> names)
   {
      this.names = names;
   }

   public void validate()
   {
      System.out.println("I'm nameable: " + nameable);
      System.out.println("Names size: " + names.size());
      System.out.println("Names: " + names);     
   }
}
