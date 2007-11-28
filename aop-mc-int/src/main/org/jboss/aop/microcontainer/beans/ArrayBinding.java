/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors.
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
package org.jboss.aop.microcontainer.beans;

import java.util.ArrayList;
import java.util.List;

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.InterceptorFactory;
import org.jboss.aop.array.Type;
import org.jboss.logging.Logger;
import org.jboss.util.id.GUID;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ArrayBinding implements Binding
{
   private static final Logger log = Logger.getLogger(ArrayBinding.class);
   AspectManager manager;
   String name = GUID.asString();
   String type;
   List<BindingEntry> advices;

   public AspectManager getManager()
   {
      return manager;
   }

   public void setManager(AspectManager manager)
   {
      this.manager = manager;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
      this.type = type;
   }

   public List<BindingEntry> getAdvices()
   {
      return advices;
   }

   public void setAdvices(List<BindingEntry> advices)
   {
      this.advices = advices;
   }

   public void start() throws Exception
   {
      if (manager == null)
      {
         throw new IllegalArgumentException("Null manager");
      }
      if (type == null)
      {
         throw new IllegalArgumentException("Null type");
      }
      Type theType = Type.valueOf(type);

      ArrayList<InterceptorFactory> interceptors = null;
      if (advices != null)
      {
         interceptors = new ArrayList<InterceptorFactory>();
         int i = 0;
         for (BindingEntry entry : advices)
         {
            entry.start();
            InterceptorFactory[] factories = entry.getInterceptorFactories();
            for (InterceptorFactory ifac : factories)
            {
               interceptors.add(ifac);
            }
         }
      }
      InterceptorFactory[] facs = interceptors != null ? interceptors.toArray(new InterceptorFactory[interceptors.size()]) : new InterceptorFactory[0];
      org.jboss.aop.array.ArrayBinding binding = new org.jboss.aop.array.ArrayBinding(name, facs, theType);
      manager.addArrayBinding(binding);
      log.debug("Bound array binding " + name);
   }

   public void stop() throws Exception
   {
      manager.removeArrayBinding(name);
      if (advices != null)
      {
         for (BindingEntry entry : advices)
         {
            entry.stop();
         }
      }
   }

   public void uninstall() throws Exception
   {
      stop();
   }

   public void rebind() throws Exception
   {
      stop();
      start();
   }
}
