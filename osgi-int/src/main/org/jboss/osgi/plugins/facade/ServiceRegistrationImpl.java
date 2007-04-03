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
package org.jboss.osgi.plugins.facade;

import java.util.Dictionary;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.event.KernelEvent;
import org.jboss.kernel.spi.event.KernelEventEmitter;
import org.jboss.kernel.spi.registry.KernelRegistry;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * ServiceRegistration implementation.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ServiceRegistrationImpl implements ServiceRegistration
{
   protected KernelEventEmitter emitter;
   protected KernelControllerContext context;
   protected Long serviceId;
   protected Map<String, Object> properties;

   protected boolean isRegistered;

   public ServiceRegistrationImpl(KernelEventEmitter emitter, KernelControllerContext context, Long serviceId, Map<String, Object> properties)
   {
      this.emitter = emitter;
      this.context = context;
      this.serviceId = serviceId;
      this.properties = properties;
      init();
   }
   protected void init()
   {
      KernelEvent event = new AbstractServiceEvent(this, KernelRegistry.KERNEL_REGISTRY_REGISTERED, serviceId, System.currentTimeMillis(), emitter);
      emitter.fireKernelEvent(event);
   }

   protected void validateServiceRegistration()
   {
      if (isServiceRegistered() == false)
         throw new IllegalStateException("ServiceRegistration object has already been unregistered.");
   }

   protected boolean isServiceRegistered()
   {
      return isRegistered;
   }

   public ServiceReference getReference()
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public void setProperties(Dictionary properties)
   {
      validateServiceRegistration();
      if (properties != null && properties.size() > 0)
      {
         Set<String> names = new TreeSet<String>(String.CASE_INSENSITIVE_ORDER);
         Enumeration keys = properties.keys();
         for(String key = (String)keys.nextElement(); keys.hasMoreElements();)
         {
            if (names.add(key) == false)
               throw new IllegalArgumentException("properties contains case variants of the same key name.");

            if (Constants.OBJECTCLASS.equals(key) == false && Constants.SERVICE_ID.equals(key) == false)
            {
               this.properties.put(key, properties.get(key));
            }
         }
         KernelEvent event = new AbstractServiceEvent(this, KernelRegistry.KERNEL_REGISTRY_MODIFIED, serviceId, System.currentTimeMillis(), emitter);
         emitter.fireKernelEvent(event);
      }
   }

   public void unregister()
   {
      validateServiceRegistration();
      isRegistered = false;
      KernelEvent event = new AbstractServiceEvent(this, KernelRegistry.KERNEL_REGISTRY_UNREGISTERED, serviceId, System.currentTimeMillis(), emitter);
      emitter.fireKernelEvent(event);
   }
}
