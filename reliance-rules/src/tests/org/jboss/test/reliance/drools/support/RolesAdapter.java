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
package org.jboss.test.reliance.drools.support;

import java.util.Set;
import java.util.Collections;

import org.jboss.reliance.identity.Identity;
import org.jboss.beans.metadata.plugins.annotations.Constructor;
import org.jboss.beans.metadata.plugins.annotations.Inject;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class RolesAdapter
{
   private Identity identity;
   private String username;
   private String password;
   private Set<String> roles = Collections.emptySet();

   @Constructor
   public RolesAdapter(@Inject Identity identity)
   {
      this.identity = identity;
   }

   public void create()
   {
      identity.setUsername(username);
      identity.setPassword(password);
      identity.login();
   }

   public void start()
   {
      for(String role : roles)
         addRole(role);
   }

   public void addRole(String role)
   {
      identity.addRole(role);
   }

   public void removeRole(String role)
   {
      identity.removeRole(role);
   }

   public void stop()
   {
      for(String role : roles)
         removeRole(role);
   }

   public void destroy()
   {
      identity.logout();
      identity = null;
   }

   public void setUsername(String username)
   {
      this.username = username;
   }

   public void setPassword(String password)
   {
      this.password = password;
   }

   public void setRoles(Set<String> roles)
   {
      this.roles = roles;
   }
}
