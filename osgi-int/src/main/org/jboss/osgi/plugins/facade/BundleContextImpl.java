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
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Dictionary;
import java.util.EventListener;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderImpl;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.deployers.spi.structure.DeploymentContext;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.event.KernelEvent;
import org.jboss.kernel.spi.event.KernelEventEmitter;
import org.jboss.kernel.spi.event.KernelEventFilter;
import org.jboss.kernel.spi.event.KernelEventListener;
import org.jboss.logging.Logger;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.util.id.GUID;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.BundleException;
import org.osgi.framework.BundleListener;
import org.osgi.framework.Constants;
import org.osgi.framework.Filter;
import org.osgi.framework.FrameworkListener;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceFactory;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

/**
 * Bundle context implementation on top of existing DeploymentContext.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BundleContextImpl implements BundleContext, KernelEventEmitter
{
   /** The log */
   private static final Logger log = Logger.getLogger(BundleContextImpl.class);
   /** The service reference comparator */
   private static Comparator<ServiceReference> serviceRefenceComparator = new ServiceReferenceComparator();

   protected DeploymentContext context;
   protected Bundle bundle;
   protected KernelController controller; // todo - get it
   protected KernelEventEmitter emitterDelegate; // todo - get it
   protected KernelConfigurator configurator; // todo - get it

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

   protected ClassLoader getClassLoader()
   {
      return null;
   }

   public void registerListener(KernelEventListener listener, KernelEventFilter filter, Object handback) throws Throwable
   {
      emitterDelegate.registerListener(listener, filter, handback);
   }

   public void unregisterListener(KernelEventListener listener, KernelEventFilter filter, Object handback) throws Throwable
   {
      emitterDelegate.unregisterListener(listener, filter, handback);
   }

   public void fireKernelEvent(KernelEvent event)
   {
      emitterDelegate.fireKernelEvent(event);
   }

   // --------------- OSGi framework -----------------------

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
         registerListener(listener, eventFilter, this);
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
      validateBundle();
      AbstractDelegateListener listener = listeners.get(serviceListener);
      if (listener != null)
      {
         listener.removeValidate();
         KernelEventFilter filter = filters.get(serviceListener);
         try
         {
            unregisterListener(listener, filter, this);
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
            registerListener(listener, null, this);
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
            unregisterListener(listener, null, this);
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
            registerListener(listener, null, this);
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
            unregisterListener(listener, null, this);
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

   public ServiceRegistration registerService(String[] clazzes, Object service, Dictionary properties)
   {
      validateBundle();
      // todo - permissions
      if (service == null)
         throw new IllegalArgumentException("service is null!");
      if (clazzes == null)
         throw new Error("null clazzes parameter!"); // todo - what else?

      ClassInfo serviceInfo;
      ClassInfo[] infos = new ClassInfo[clazzes.length];
      Class[] interfaces = new Class[clazzes.length];
      try
      {
         serviceInfo = configurator.getClassInfo(service.getClass());
         for(int i=0; i < clazzes.length; i++)
         {
            infos[i] = configurator.getClassInfo(clazzes[i], getClassLoader());
            interfaces[i] = infos[i].getType();
         }
      }
      catch (Throwable t)
      {
         throw new InternalOSGiFacadeException(t);
      }
      // check types
      boolean isServiceFactory = (service instanceof ServiceFactory);
      if (isServiceFactory == false)
      {
         for (ClassInfo info : infos)
         {
            if (info.isAssignableFrom(serviceInfo) == false)
               throw new IllegalArgumentException("service is not a ServiceFactory object and is not an instance of all the named classes in clazzes: " + info);
         }
      }
      // handle properties
      Map<String, Object> serviceMap = OSGiUtils.toMap(properties);
      Long serviceId = NumberUtil.nextLong();
      serviceMap.put(Constants.SERVICE_ID, serviceId);
      serviceMap.put(Constants.OBJECTCLASS, clazzes);
      if (isServiceFactory)
      {
         service = ServiceFactoryProxy.createProxy(service, interfaces);
      }
      BeanMetaDataBuilder builder = new BeanMetaDataBuilderImpl(GUID.asString(), serviceInfo.getName());
      BeanMetaData metaData = builder.getBeanMetaData();
      KernelControllerContext context;
      try
      {
         context = controller.install(metaData, service);
      }
      catch (Throwable t)
      {
         throw new InternalOSGiFacadeException(t);
      }
      // todo - get underlying bundle --> ServiceReference
      return new ServiceRegistrationImpl(this, context, serviceId, serviceMap);
   }

   public ServiceRegistration registerService(String clazz, Object service, Dictionary properties)
   {
      return registerService(new String[]{clazz}, service, properties);
   }

   public ServiceReference[] getAllServiceReferences(String clazz, String filter) throws InvalidSyntaxException
   {
      validateBundle();
      // todo - see spec API
      return null;
   }

   public ServiceReference[] getServiceReferences(String clazz, String filter) throws InvalidSyntaxException
   {
      ServiceReference[] references = getAllServiceReferences(clazz, filter);
      if (references != null && references.length > 0)
      {
         List<ServiceReference> list = new ArrayList<ServiceReference>();
         for(ServiceReference ref : references)
         {
            String[] clazzes = (String[])ref.getProperty(Constants.OBJECTCLASS);
            for (String refClass : clazzes)
            {
               if (ref.isAssignableTo(getBundle(), refClass))
               {
                  list.add(ref);
               }
            }
         }
         Collections.sort(list, serviceRefenceComparator);
         return list.toArray(new ServiceReference[list.size()]);
      }
      else
      {
         return null;
      }
   }

   public ServiceReference getServiceReference(String clazz)
   {
      validateBundle();
      try
      {
         ServiceReference[] references = getServiceReferences(clazz, null);
         if (references != null && references.length > 0)
         {
            // watch for the order in getServiceReferences
            return references[0];
         }
         return null;
      }
      catch (InvalidSyntaxException e)
      {
         throw new InternalOSGiFacadeException("Should not be here, since filter was null!");
      }
   }

   public Object getService(ServiceReference reference)
   {
      validateBundle();
      return null;
   }

   public boolean ungetService(ServiceReference reference)
   {
      validateBundle();
      return false;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public File getDataFile(String filename)
   {
      return null;  //To change body of implemented methods use File | Settings | File Templates.
   }

   public Filter createFilter(String filter) throws InvalidSyntaxException
   {
      validateBundle();
      // todo - create our own impl?
      return FrameworkUtil.createFilter(filter);
   }

   /**
    * @see org.osgi.framework.BundleContext#getServiceReferences(String, String)
    */
   private static class ServiceReferenceComparator implements Comparator<ServiceReference>
   {
      public int compare(ServiceReference sr1, ServiceReference sr2)
      {
         int rank1 = OSGiUtils.getServiceRanking(sr1);
         int rank2 = OSGiUtils.getServiceRanking(sr2);
         if (rank1 == rank2)
         {
            long id1 = OSGiUtils.getServiceId(sr1);
            long id2 = OSGiUtils.getServiceId(sr2);
            return (int)(id1 - id2);
         }
         return rank2 - rank1;
      }
   }

}
