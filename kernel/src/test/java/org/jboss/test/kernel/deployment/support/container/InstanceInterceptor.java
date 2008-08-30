/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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

import java.lang.reflect.Method;

import org.jboss.logging.Logger;

/**
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class InstanceInterceptor
{
   private static Logger log = Logger.getLogger(InstanceInterceptor.class);
   private BeanPool<BeanContext<?>> pool;

   public InstanceInterceptor()
   {
      log.info("ctor");
   }

   public Object invoke(Object...args)
      throws Throwable
   {
      BeanContext<?> ctx = pool.createBean();
      Object bean = ctx.getInstance();

      boolean discard = false;
      Object value = null;

      try
      {
         String name = (String) args[0];
         Class<?>[] paramTypes = {};
         Method m = bean.getClass().getMethod(name, paramTypes);
         Object[] empty = {};
         value = m.invoke(bean, empty);
         return value;
      }
      catch (Exception ex)
      {
         throw ex;
      }
      finally
      {
         if (discard)
            pool.destroyBean(ctx);
         else pool.releaseBean(ctx);
      }
   }
}
