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
package org.jboss.reliance.drools.core;

import java.util.Map;
import java.util.Collections;

import org.drools.spi.GlobalResolver;
import org.jboss.dependency.spi.Controller;

/**
 * Map alias names for rules globals --> microcontainer names.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class NamingKernelGlobalResolver extends KernelGlobalResolver
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 647237007123208771L
   ;
   private Map<String, String> names = Collections.emptyMap();

   public NamingKernelGlobalResolver(GlobalResolver delegate, Controller controller)
   {
      super(delegate, controller);
   }

   /**
    * Set the names map.
    *
    * @param names the names map
    */
   public void setNames(Map<String, String> names)
   {
      this.names = names;
   }

   /**
    * Add additional names.
    *
    * @param additionalNames additional names
    */
   public void addNames(Map<String, String> additionalNames)
   {
      names.putAll(additionalNames);
   }

   protected Object transformName(String name)
   {
      String mappedName = names.get(name);
      return mappedName != null ? mappedName : super.transformName(name);
   }
}
