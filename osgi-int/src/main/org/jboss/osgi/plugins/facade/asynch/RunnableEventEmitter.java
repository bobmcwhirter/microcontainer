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
package org.jboss.osgi.plugins.facade.asynch;

import org.jboss.kernel.plugins.event.AbstractEventEmitter;
import org.jboss.kernel.spi.event.KernelEvent;
import org.jboss.kernel.spi.event.KernelEventListener;

/**
 * Runnable wrapper event emitter.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class RunnableEventEmitter extends AbstractEventEmitter
{
   protected RunnableExecutor executor;
   protected long startTimeout;
   protected long completeTimeout;

   /**
    * Create new RunnableExecutor with value zero as defaults.
    *
    * @param executor the executor
    */
   public RunnableEventEmitter(RunnableExecutor executor)
   {
      this(executor, 0, 0);
   }

   /**
    * Create new RunnableExecutor with default limits.
    *
    * @param executor the runnable executor
    * @param startTimeout default start limit
    * @param completeTimeout default completion limit
    */
   public RunnableEventEmitter(RunnableExecutor executor, long startTimeout, long completeTimeout)
   {
      if (executor == null)
         throw new IllegalArgumentException("Null executor");
      this.executor = executor;
      this.startTimeout = startTimeout;
      this.completeTimeout = completeTimeout;
   }

   /**
    * Should we use runnable executor to execute listener call.
    *
    * @param listener the listener
    * @param event the event
    * @param handback the handback
    * @return true if we should use runnable, false to use default listener execution
    */
   protected boolean useRunnable(KernelEventListener listener, KernelEvent event, Object handback)
   {
      return true;
   }

   protected void fireKernelEvent(KernelEventListener listener, KernelEvent event, Object handback)
   {
      if (useRunnable(listener, event, handback))
      {
         executor.run(new RunnableWrapper(listener, event, handback), startTimeout, completeTimeout);
      }
      else
      {
         super.fireKernelEvent(listener, event, handback);
      }
   }

   private class RunnableWrapper implements Runnable
   {
      private KernelEventListener listener;
      private KernelEvent event;
      private Object handback;

      public RunnableWrapper(KernelEventListener listener, KernelEvent event, Object handback)
      {
         this.listener = listener;
         this.event = event;
         this.handback = handback;
      }

      public void run()
      {
         listener.onEvent(event, handback);
      }
   }
}
