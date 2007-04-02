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

import java.io.File;
import java.io.InputStream;
import java.util.Collections;
import java.util.Dictionary;
import java.util.EventListener;
import java.util.HashMap;
import java.util.Map;

import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.kernel.spi.event.KernelEventEmitter;
import org.jboss.kernel.spi.event.KernelEventFilter;
import org.jboss.logging.Logger;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * Bundle context implementation on top of existing DeploymentContext.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BundleContextImpl implements BundleContext
{
   /** The log */
   private static final Logger log = Logger.getLogger(BundleContextImpl.class);

   protected DeploymentContext context;
   protected Bundle bundle;
   protected KernelEventEmitter eventEmitter;

   protected Map<EventListener, AbstractDelegateListener> listeners = Collections.synchronizedMap(new HashMap<EventListener, AbstractDelegateListener>());
   protected Map<ServiceListener, KernelEventFilter> filters = Collections.synchronizedMap(new HashMap<ServiceListener, KernelEventFilter>());

   public BundleContextImpl(DeploymentContext context)
   {
      this.context = context;
   }

   protected boolean isBundleContextValid()
   {
      return true;
   }

   protected void validateBundle() throws IllegalStateException
   {
      if (isBundleContextValid() == false)
         throw new IllegalStateException("BundleContext is no longer valid.");
   }

   public String getProperty(String string)
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public Bundle getBundle()
   {
      if (bundle == null)
      {
         bundle = new BundleImpl(context);
      }
      return bundle;
   }

   public Bundle installBundle(String string) throws BundleException
   {
      throw new UnsupportedOperationException("not yet implemented");
   }

   public Bundle installBundle(String string, InputStream inputStream) throws BundleException
   {
      throw new UnsupportedOperationException("not yet implemented");
   }

   public Bundle getBundle(long id)
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public Bundle[] getBundles()
   {
      return new Bundle[0];  //To change body of implemented methods use File | Settings | File Templates.
   }

   public void addServiceListener(ServiceListener serviceListener, String filter) throws InvalidSyntaxException
   {
      // todo - conditional add
      validateBundle();
      // remove the existing first
      if (listeners.containsKey(serviceListener))
      {
         removeServiceListener(serviceListener);               
      }
      // create new one
      ServiceListenerImpl listener = new ServiceListenerImpl(serviceListener);
      listener.addValidate();
      OSGiKernelEventFilter eventFilter = null;
      if (filter != null)
      {
         eventFilter = new OSGiKernelEventFilter(filter);
         filters.put(serviceListener, eventFilter);
      }
      try
      {
         eventEmitter.registerListener(listener, eventFilter, this);
         listeners.put(serviceListener, listener);
      }
      catch (Throwable throwable)
      {
         filters.remove(serviceListener);
         // todo - throw what?
         throw new IllegalArgumentException(throwable);
      }
   }

   public void addServiceListener(ServiceListener serviceListener)
   {
      try
      {
         addServiceListener(serviceListener, null);
      }
      catch (InvalidSyntaxException ignored)
      {
      }
   }

   public void removeServiceListener(ServiceListener serviceListener)
   {
      AbstractDelegateListener listener = listeners.get(serviceListener);
      if (listener != null)
      {
         listener.removeValidate();
         KernelEventFilter filter = filters.get(serviceListener);
         try
         {
            eventEmitter.unregisterListener(listener, filter, this);
         }
         catch (Throwable t)
         {
            log.warn("Exception while unregistering Service listener.", t);
         }
         finally
         {
            listeners.remove(serviceListener);
            if (filter != null)
            {
               filters.remove(serviceListener);
            }
         }
      }
   }

   public void addBundleListener(BundleListener bundleListener)
   {
      validateBundle();
      if (listeners.containsKey(bundleListener) == false)
      {
         BundleListenerImpl listener = new BundleListenerImpl(bundleListener);
         listener.addValidate();
         try
         {
            eventEmitter.registerListener(listener, null, this);
            listeners.put(bundleListener, listener);
         }
         catch (Throwable t)
         {
            throw new IllegalArgumentException(t);
         }
      }
   }

   public void removeBundleListener(BundleListener bundleListener)
   {
      validateBundle();
      AbstractDelegateListener listener = listeners.get(bundleListener);
      if (listener != null)
      {
         listener.removeValidate();
         try
         {
            eventEmitter.unregisterListener(listener, null, this);
         }
         catch (Throwable t)
         {
            log.warn("Exception while unregistering Bundle listener.", t);
         }
         finally
         {
            listeners.remove(bundleListener);
         }
      }
   }

   public void addFrameworkListener(FrameworkListener frameworkListener)
   {
      validateBundle();
      if (listeners.containsKey(frameworkListener) == false)
      {
         FrameworkListenerImpl listener = new FrameworkListenerImpl(frameworkListener);
         listener.addValidate();
         try
         {
            eventEmitter.registerListener(listener, null, this);
            listeners.put(frameworkListener, listener);
         }
         catch (Throwable t)
         {
            throw new IllegalArgumentException(t);
         }
      }
   }

   public void removeFrameworkListener(FrameworkListener frameworkListener)
   {
      validateBundle();
      AbstractDelegateListener listener = listeners.get(frameworkListener);
      if (listener != null)
      {
         listener.removeValidate();
         try
         {
            eventEmitter.unregisterListener(listener, null, this);
         }
         catch (Throwable t)
         {
            log.warn("Exception while unregistering Framework listener.", t);
         }
         finally
         {
            listeners.remove(frameworkListener);
         }
      }
   }

   public ServiceRegistration registerService(String[] strings, Object object, Dictionary dictionary)
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public ServiceRegistration registerService(String string, Object object, Dictionary dictionary)
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public ServiceReference[] getServiceReferences(String string, String string1) throws InvalidSyntaxException
   {
      return new ServiceReference[0];  //To change body of implemented methods use File | Settings | File Templates.
   }

   public ServiceReference[] getAllServiceReferences(String string, String string1) throws InvalidSyntaxException
   {
      return new ServiceReference[0];  //To change body of implemented methods use File | Settings | File Templates.
   }

   public ServiceReference getServiceReference(String string)
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public Object getService(ServiceReference serviceReference)
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public boolean ungetService(ServiceReference serviceReference)
   {
      return false;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public File getDataFile(String string)
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public Filter createFilter(String filter) throws InvalidSyntaxException
   {
      validateBundle();
      return FrameworkUtil.createFilter(filter);
   }

}
