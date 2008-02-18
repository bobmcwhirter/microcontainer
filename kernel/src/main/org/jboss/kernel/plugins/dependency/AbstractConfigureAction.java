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
package org.jboss.kernel.plugins.dependency;

import org.jboss.kernel.spi.dependency.ConfigureKernelControllerContextAware;
import org.jboss.kernel.spi.dependency.CreateKernelControllerContextAware;
import org.jboss.kernel.spi.dependency.DescribeKernelControllerContextAware;
import org.jboss.kernel.spi.dependency.InstallKernelControllerContextAware;
import org.jboss.kernel.spi.dependency.InstantiateKernelControllerContextAware;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;
import org.jboss.kernel.spi.dependency.StartKernelControllerContextAware;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * AbstractConfigureAction.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractConfigureAction extends KernelControllerContextAction
{
   protected Class<? extends KernelControllerContextAware> getActionAwareInterface()
   {
      return ConfigureKernelControllerContextAware.class;
   }

   //TODO remove this?
   //In case the class is EXACTLY KernelControllerContextAware, we call it from here,
   //required for KernelControllerContextAwareTestCase and KernelControllerContextAwareXMLTestCase

   /**
    * Execute KCCA install.
    * @param context the controller context
    * @throws Throwable for any erroor
    */
   protected void installKernelControllerContextAware(KernelControllerContext context) throws Throwable
   {
      Object target = context.getTarget();
      if (target != null && isExactlyKernelControllerContextAware(target))
      {
         ((KernelControllerContextAware)target).setKernelControllerContext(context);
      }
   }

   /**
    * Execute KCCA uninstall.
    *
    * @param context the controller context
    */
   protected void uninstallKernelControllerContextAware(KernelControllerContext context)
   {
      Object target = context.getTarget();
      if (target != null && isExactlyKernelControllerContextAware(target))
      {
         try
         {
            ((KernelControllerContextAware)target).unsetKernelControllerContext(context);
         }
         catch (Throwable t)
         {
            log.debug("Ignored error unsetting context ", t);
         }
      }
   }

   /**
    * Is exactly KCCA instance.
    * @param o the target to test
    * @return true if exact match
    */
   protected boolean isExactlyKernelControllerContextAware(Object o)
   {
      Class<?> clazz = o.getClass();
      return KernelControllerContextAware.class.isAssignableFrom(clazz) &&
               (!ConfigureKernelControllerContextAware.class.isAssignableFrom(clazz) &&
               !CreateKernelControllerContextAware.class.isAssignableFrom(clazz) &&
               !DescribeKernelControllerContextAware.class.isAssignableFrom(clazz) &&
               !InstallKernelControllerContextAware.class.isAssignableFrom(clazz) &&
               !InstantiateKernelControllerContextAware.class.isAssignableFrom(clazz) &&
               !StartKernelControllerContextAware.class.isAssignableFrom(clazz));
   }
}
