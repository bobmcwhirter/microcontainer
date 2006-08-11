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
package org.jboss.kernel.plugins.dependency;

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.plugins.spi.action.ControllerContextAction;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.logging.Logger;

/**
 * KernelControllerContextAction.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class KernelControllerContextAction implements ControllerContextAction
{
   protected Logger log = Logger.getLogger(getClass());

   /** 
    * Dispatch a joinpoint
    * 
    * @param context the context
    * @param joinpoint the joinpoint
    * @return the result
    * @throws Throwable for any error
    */
   protected static Object dispatchJoinPoint(final KernelControllerContext context, final Joinpoint joinpoint) throws Throwable
   {
      BeanMetaData metaData = context.getBeanMetaData();
      ClassLoader cl = Configurator.getClassLoader(metaData);
      AccessControlContext access = null;
      if (context instanceof AbstractKernelControllerContext)
      {
         AbstractKernelControllerContext theContext = (AbstractKernelControllerContext) context;
         access = theContext.getAccessControlContext();
      }

      // Dispatch with the bean class loader if it exists
      ClassLoader tcl = Thread.currentThread().getContextClassLoader();
      try
      {
         if( cl != null && access == null )
            Thread.currentThread().setContextClassLoader(cl);
         if (access == null)
         {
            return joinpoint.dispatch();
         }
         else
         {
            DispatchJoinPoint action = new DispatchJoinPoint(joinpoint);
            try
            {
               return AccessController.doPrivileged(action, access);
            }
            catch (PrivilegedActionException e)
            {
               throw e.getCause();
            }
         }
      }
      finally
      {
         if( cl != null && access == null )
            Thread.currentThread().setContextClassLoader(tcl);
      }
   }

   public void install(final ControllerContext context) throws Throwable
   {
      if (System.getSecurityManager() == null || context instanceof AbstractKernelControllerContext == false)
         installAction((KernelControllerContext) context);
      else
      {
         PrivilegedExceptionAction<Object> action = new PrivilegedExceptionAction<Object>()
         {
            public Object run() throws Exception
            {
               try
               {
                  installAction((KernelControllerContext) context);
                  return null;
               }
               catch (RuntimeException e)
               {
                  throw e;
               }
               catch (Exception e)
               {
                  throw e;
               }
               catch (Error e)
               {
                  throw e;
               }
               catch (Throwable t)
               {
                  throw new RuntimeException(t);
               }
            }
         };
         try
         {
            AccessController.doPrivileged(action);
         }
         catch (PrivilegedActionException e)
         {
            throw e.getCause();
         }
      }
   }

   public void uninstall(final ControllerContext context)
   {
      if (System.getSecurityManager() == null || context instanceof AbstractKernelControllerContext == false)
         uninstallAction((KernelControllerContext) context);
      else
      {
         PrivilegedAction<Object> action = new PrivilegedAction<Object>()
         {
            public Object run()
            {
               uninstallAction((KernelControllerContext) context);
               return null;
            }
         };
         AccessController.doPrivileged(action);
      }
   }
   
   public void installAction(KernelControllerContext context) throws Throwable
   {
   }

   public void uninstallAction(KernelControllerContext context)
   {
   }
}