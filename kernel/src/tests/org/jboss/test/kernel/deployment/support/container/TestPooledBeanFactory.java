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

import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.kernel.spi.config.KernelConfigurator;

/**
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class TestPooledBeanFactory extends GenericBeanFactory
{
   /** The pooling policy */
   private ArrayBlockingQueue<Object> pool;

   public TestPooledBeanFactory(KernelConfigurator configurator, int size)
   {
      super(configurator);
      pool = new ArrayBlockingQueue<Object>(size);
   }

   @Override
   public synchronized Object createBean()
      throws Throwable
   {
      if(pool.size() == 0)
      {
         // Fill the pool
         for(int n = 0; n < pool.size(); n ++)
            pool.put(super.createBean());
      }
      return pool.take();
   }
   public void destroyBean(Object bean)
      throws Exception
   {
      pool.put(bean);
   }
}
