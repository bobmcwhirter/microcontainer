/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.kernel.plugins.event;

import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.kernel.spi.event.KernelEvent;
import org.jboss.kernel.spi.event.KernelEventEmitter;
import org.jboss.kernel.spi.event.KernelEventFilter;
import org.jboss.kernel.spi.event.KernelEventListener;
import org.jboss.logging.Logger;

/**
 * Abstract Event emitter.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractEventEmitter implements KernelEventEmitter
{
   /** The log */
   private static final Logger log = Logger.getLogger(AbstractEventEmitter.class);

   /** Used to represent a null parameter */
   protected static final Object NULL = new Object();

   /** Used to represent a null filter */
   protected static final KernelEventFilter NULL_FILTER = new KernelEventFilter()
   {
      public boolean wantEvent(KernelEvent event, Object handback)
      {
         return false;
      }
   };

   /** The registry Map<filter, Map<handback, List<listener>>>*/
   protected Map<KernelEventFilter, Map<Object, List<KernelEventListener>>> eventListenerRegistry = new ConcurrentHashMap<KernelEventFilter, Map<Object,List<KernelEventListener>>>();

   /** The sequence number of this emitter */
   private long emitterSequence = 0;

   /**
    * Do we have listeners
    * 
    * @return true when there are listeners
    */
   public boolean hasListeners()
   {
      return eventListenerRegistry.isEmpty() == false;
   }

   /**
    * Make a new event
    * 
    * @param type the event type
    * @param context the context
    * @return the event
    */
   public KernelEvent createEvent(String type, Object context)
   {
      return new AbstractEvent(this, type, nextEmitterSequence(), System.currentTimeMillis(), context);
   }

   public void registerListener(KernelEventListener listener, KernelEventFilter filter, Object handback) throws Throwable
   {
      KernelEventFilter filterObject = filter == null ? NULL_FILTER : filter;
      Object handbackObject = handback == null ? NULL : handback;

      synchronized (eventListenerRegistry)
      {
         Map<Object, List<KernelEventListener>> handbacks = eventListenerRegistry.get(filterObject);
         if (handbacks == null)
         {
            handbacks = new ConcurrentHashMap<Object, List<KernelEventListener>>();
            eventListenerRegistry.put(filterObject, handbacks);
         }
         List<KernelEventListener> listeners = handbacks.get(handbackObject);
         if (listeners == null)
         {
            listeners = new CopyOnWriteArrayList<KernelEventListener>();
            handbacks.put(handbackObject, listeners);
         }
         listeners.add(listener);
         if (log.isTraceEnabled())
            log.trace("Registered listener: " + listener + " with filter=" + filter + " handback=" + handback + " on object " + this);

      }
   }

   public void unregisterListener(KernelEventListener listener, KernelEventFilter filter, Object handback) throws Throwable
   {
      KernelEventFilter filterObject = filter == null ? NULL_FILTER : filter;
      Object handbackObject = handback == null ? NULL : handback;

      synchronized (eventListenerRegistry)
      {
         Map<Object, List<KernelEventListener>> handbacks = eventListenerRegistry.get(filterObject);
         if (handbacks != null)
         {
            List<KernelEventListener> listeners = handbacks.get(handbackObject);
            if (listeners != null && listeners.remove(listener))
            {
               if (log.isTraceEnabled())
                  log.trace("Unregistered listener: " + listener + " with filter=" + filter + " handback=" + handback + " on object " + this);
               return;
            }
         }
      }
      throw new IllegalStateException("Listener not registered.");
   }

   public void fireKernelEvent(KernelEvent event)
   {
      if (log.isTraceEnabled())
         log.trace("Firing event: " + event + " on object " + this);
      if (eventListenerRegistry.isEmpty() == false)
      {
         for (Iterator i = eventListenerRegistry.entrySet().iterator(); i.hasNext();)
         {
            Map.Entry registryEntry = (Map.Entry) i.next();

            Map handbacks = (Map) registryEntry.getValue();
            if (handbacks != null)
            {
               KernelEventFilter filter = null;
               Object filterObject = registryEntry.getKey();
               if (filterObject != NULL_FILTER)
                  filter = (KernelEventFilter) filterObject;

               for (Iterator j = handbacks.entrySet().iterator(); j.hasNext();)
               {
                  Map.Entry handbackEntry = (Map.Entry) j.next();
                  List listeners = (List) handbackEntry.getValue();
                  if (listeners != null)
                  {
                     Object handback = handbackEntry.getKey();
                     if (handback == NULL)
                        handback = null;

                     for (ListIterator k = listeners.listIterator(); k.hasNext();)
                     {
                        KernelEventListener listener = (KernelEventListener) k.next();
                        try
                        {
                           if (filter == null || filter.wantEvent(event, handback))
                              fireKernelEvent(listener, event, handback);
                        }
                        catch (Throwable t)
                        {
                           log.debug("Ignored unhandled throwable: ", t);
                        }
                     }
                  }
               }
            }
         }
      }
   }

   /**
    * Fire a kernel event to a single listener
    * 
    * @param listener the listener
    * @param event the event
    * @param handback the handback object
    */
   protected void fireKernelEvent(KernelEventListener listener, KernelEvent event, Object handback)
   {
      listener.onEvent(event, handback);
   }

   /**
    * Get the next emitter sequence
    * 
    * @return the next emitter sequence
    */
   protected long nextEmitterSequence()
   {
      synchronized (eventListenerRegistry)
      {
         return emitterSequence++;
      }
   }
}
