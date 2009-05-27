/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.kernel.plugins.validation.basic;

import java.security.PrivilegedAction;
import java.security.AccessController;

import org.jboss.kernel.plugins.validation.AbstractKernelBeanValidator;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.validation.KernelBeanValidator;

/**
 * Basic impl of kernel bean validator.
 * By default it's disabled.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class BasicKernelBeanValidator extends AbstractKernelBeanValidator
{
   public static final String DISABLED_PROPERTY_KEY = KernelBeanValidator.class.getSimpleName() + ".disabled";

   private boolean disabled;

   @Override
   public void setKernel(Kernel kernel) throws Throwable
   {
      super.setKernel(kernel);
      // read the disabled flag
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         disabled = AccessController.doPrivileged(new DisabledFlagReader());
      else
         disabled = readDisabledFlag();
   }

   public boolean isDisabled()
   {
      return disabled;
   }

   /**
    * Set the disabled flag.
    *
    * @param disabled the disabled flag
    */
   public void setDisabled(boolean disabled)
   {
      this.disabled = disabled;
   }

   /**
    * Read the disabled flag.
    *
    * @return the system property flag read
    */
   protected static boolean readDisabledFlag()
   {
      return Boolean.parseBoolean(System.getProperty(DISABLED_PROPERTY_KEY, "true"));
   }

   private class DisabledFlagReader implements PrivilegedAction<Boolean>
   {
      public Boolean run()
      {
         return readDisabledFlag();
      }
   }
}
