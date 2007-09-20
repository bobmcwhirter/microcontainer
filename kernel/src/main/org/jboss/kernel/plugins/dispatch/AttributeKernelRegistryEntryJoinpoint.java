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
package org.jboss.kernel.plugins.dispatch;

import org.jboss.dependency.spi.dispatch.AttributeDispatchContext;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.registry.KernelRegistryEntryJoinpoint;
import org.jboss.util.JBossObject;

/**
 * Attribute kernel registry entry joinpoint.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AttributeKernelRegistryEntryJoinpoint extends JBossObject implements KernelRegistryEntryJoinpoint
{
   private String getterName;
   private AttributeDispatchContext context;

   public AttributeKernelRegistryEntryJoinpoint(String getterName)
   {
      this.getterName = getterName;
   }

   public boolean applyEntry(KernelRegistryEntry entry)
   {
      if (entry instanceof AttributeDispatchContext)
      {
         context = (AttributeDispatchContext)entry;
         return true;
      }
      return false;
   }

   public Object dispatch() throws Throwable
   {
      if (context == null)
         throw new IllegalArgumentException("Cannot dispatch null context.");
      return context.get(getterName);
   }

   public String toHumanReadableString()
   {
      return getterName + "," + context;
   }
}
