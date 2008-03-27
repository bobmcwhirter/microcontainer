/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.kernel.deployment.support.container;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

import org.jboss.beans.metadata.spi.factory.BeanFactory;
import org.jboss.logging.Logger;

/**
 * 
 * @param <T> the type
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class BeanPool<T>
{
   private Logger log;
   /** The pooling policy */
   private ArrayBlockingQueue<T> pool;
   private boolean poolInitialized = false;
   private BeanFactory factory;

   public BeanPool()
   {
      this(3);
   }
   public BeanPool(int capacity)
   {
      pool = new ArrayBlockingQueue<T>(capacity);
   }

   public BeanFactory getFactory()
   {
      return factory;
   }

   public void setFactory(BeanFactory factory)
   {
      this.factory = factory;
   }

   public int size()
   {
      return pool.size();
   }
   public int remainingCapacity()
   {
      return pool.remainingCapacity();
   }

   @SuppressWarnings("unchecked")
   public synchronized T createBean()
      throws Throwable
   {
      if(poolInitialized == false)
      {
         T bean = (T) factory.createBean();
         pool.put(bean);
         log = Logger.getLogger("BeanPool<"+bean.getClass().getSimpleName()+">");
         log.debug("createBean, initializing pool, remainingCapacity: "+pool.remainingCapacity());
         int capacity = pool.remainingCapacity();
         // Fill the pool
         for(int n = 0; n < capacity; n ++)
         {
            bean = (T) factory.createBean();
            pool.put(bean);
         }
         poolInitialized = true;
      }
      T bean = pool.poll(1, TimeUnit.SECONDS);
      if(bean == null)
         throw new IllegalStateException(this+" is emtpy");
      log.debug("End createBean, size: "+pool.size()+", bean: "+bean);
      return bean;
   }
   public void destroyBean(T bean)
      throws Throwable
   {
      pool.put(bean);
   }
}
