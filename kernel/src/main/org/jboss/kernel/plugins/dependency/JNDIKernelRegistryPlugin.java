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
package org.jboss.kernel.plugins.dependency;

import java.util.Hashtable;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.kernel.plugins.registry.AbstractKernelRegistryEntry;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.kernel.spi.registry.KernelRegistryPlugin;

/**
 * JNDI aware KernelRegistryPlugin.
 *
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class JNDIKernelRegistryPlugin implements KernelRegistryPlugin
{
   private Hashtable<?,?> properties;
   private Context context;

   public void setProperties(Hashtable<?,?> properties)
   {
      this.properties = properties;
   }

   public void create() throws NamingException
   {
      if (properties != null)
         context = new InitialContext(properties);
      else
         context = new InitialContext();
   }

   public void destroy() throws NamingException
   {
      if (context != null)
         context.close();
      context = null;
   }

   public KernelRegistryEntry getEntry(Object name)
   {
      try
      {
         Object target = context.lookup(name.toString());
         if (target != null)
            return new AbstractKernelRegistryEntry(name, target);
      }
      catch (NamingException e)
      {
      }
      return null;
   }
}
