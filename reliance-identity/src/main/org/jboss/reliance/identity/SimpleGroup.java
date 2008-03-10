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
package org.jboss.reliance.identity;

import java.io.Serializable;
import java.security.Principal;
import java.security.acl.Group;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * Implementation of the Group interface, used for holding roles etc.
 *
 * @author Shane Bryzak
 */
public class SimpleGroup implements Group, Serializable
{
   private static final long serialVersionUID = 5766373925836425908L;

   /**
    * The name of the group
    */
   private String name;

   /**
    * The members of this group
    */
   private Set<Principal> members = new HashSet<Principal>();

   public SimpleGroup(String name)
   {
      this.name = name;
   }

   public boolean addMember(Principal user)
   {
      return members.add(user);
   }

   public boolean isMember(Principal member)
   {
      if (members.contains(member))
      {
         return true;
      }
      else
      {
         for (Principal m : members)
         {
            if (m instanceof Group && ((Group)m).isMember(member))
            {
               return true;
            }
         }
      }
      return false;
   }

   public Enumeration<? extends Principal> members()
   {
      return Collections.enumeration(members);
   }

   public boolean removeMember(Principal user)
   {
      return members.remove(user);
   }

   public String getName()
   {
      return name;
   }

   @Override
   public boolean equals(Object obj)
   {
      if (obj instanceof SimpleGroup)
      {
         SimpleGroup other = (SimpleGroup)obj;
         return other.name.equals(name);
      }
      else
      {
         return false;
      }
   }

   @Override
   public int hashCode()
   {
      return name.hashCode();
   }
}
