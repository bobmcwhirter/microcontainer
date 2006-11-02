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
package org.jboss.kernel.plugins.util;

import java.security.BasicPermission;

/** 
 * This permission represents "trust" in a signer or codebase.
 * 
 * It contains a target name but no actions list. The targets are
 * kernel - access the kernel 
 * * - all allowed access
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision: 57306 $
 */
public class KernelLocatorPermission extends BasicPermission
{
   /** For serialization */
   private static final long serialVersionUID = 1L;

   /** 
    * Create a new Permission
    * 
    * @param name the target
    * @throws IllegalArgumentException for invalid name 
    * @throws NullPointerException for null name
    */ 
   public KernelLocatorPermission(String name)
   {
      this(name, null);
   }

   /** 
    * Create a new Permission
    * 
    * @param name the target
    * @param actions the actions
    * @throws IllegalArgumentException for an invalid name or target 
    * @throws NullPointerException for null name
    */ 
   public KernelLocatorPermission(String name, String actions)
   {
      super(name, actions);
   }

   /**
    * @return human readable string.
    */
   public String toString()
   {
      StringBuilder buffer = new StringBuilder(64);
      buffer.append(getClass().getName()).append(":");
      buffer.append(" name=").append(getName());
      buffer.append(" actions=").append(getActions());
      return buffer.toString();
   }

}
