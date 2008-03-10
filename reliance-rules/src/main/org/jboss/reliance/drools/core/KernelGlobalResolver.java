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

import org.drools.spi.GlobalResolver;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;

/**
 * Resolves Microcontainer context's as Drools globals.
 *  
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class KernelGlobalResolver implements GlobalResolver
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 8717911664770177777L
   ;
   private GlobalResolver delegate;
   private Controller controller;

   public KernelGlobalResolver(GlobalResolver delegate, Controller controller)
   {
      if (delegate == null)
         throw new IllegalArgumentException("Null global resolver delegate!");
      if (controller == null)
         throw new IllegalArgumentException("Null controller!");
      this.delegate = delegate;
      this.controller = controller;
   }

   /**
    * Transform Drools global name.
    *
    * @param name the name
    * @return transformed name
    */
   protected Object transformName(String name)
   {
      return name;
   }

   public Object resolveGlobal(String name)
   {
      ControllerContext context = controller.getInstalledContext(transformName(name));
      if (context != null)
         return context.getTarget();
      return delegate.resolveGlobal(name);
   }

   public void setGlobal(String name, Object value)
   {
      delegate.setGlobal(name, value);
   }
}
