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

import org.jboss.kernel.spi.event.KernelEvent;
import org.osgi.framework.BundleListener;
import org.osgi.framework.SynchronousBundleListener;

/**
 * BundleListenerImpl implementation on top of KernelEventListener.
 * Handles both SynchronousBundleListener and simple BundleListener.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BundleListenerImpl extends AbstractDelegateListener<BundleListener>
{
   private boolean isSynchronous;

   public BundleListenerImpl(BundleListener delegate)
   {
      super(delegate);
      isSynchronous = (delegate instanceof SynchronousBundleListener);
   }

   /**
    * Is the listener synchronous.
    *
    * @return synchhronous
    */
   public boolean isSynchronous()
   {
      return isSynchronous;
   }

   protected void validate()
   {
      if (isSynchronous())
      {
         // todo - check appropriate AdminPermission[context bundle,LISTENER]
      }
   }

   // todo - synchrounous stuff
   public void onEvent(KernelEvent event, Object handback)
   {
      if (event instanceof BundleEventAdapter)
      {
         BundleEventAdapter adapter = (BundleEventAdapter)event;
         delegate.bundleChanged(adapter.getEvent());
      }
   }

}
