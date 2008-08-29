/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.kernel.plugins.util;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;

/**
 * A simple singleton that provides access to the Kernel the KernelLocator
 * is associated with.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class KernelLocator implements KernelControllerContextAware
{
   private static Kernel kernel;
   private static final KernelLocatorPermission GET_KERNEL = new KernelLocatorPermission("kernel");

   public void setKernelControllerContext(KernelControllerContext context)
      throws Exception
   {
      KernelLocator.kernel = context.getKernel();
   }

   public void unsetKernelControllerContext(KernelControllerContext context)
      throws Exception
   {
      KernelLocator.kernel = null;
   }

   public static Kernel getKernel()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkPermission(GET_KERNEL);

      return kernel;
   }

}
