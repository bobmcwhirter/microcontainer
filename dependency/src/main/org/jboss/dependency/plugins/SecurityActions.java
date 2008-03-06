/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.dependency.plugins;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;
import org.jboss.logging.Logger;

/**
 * SecurityActions.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
class SecurityActions
{
   /** The logger */
   private static final Logger log = Logger.getLogger(SecurityActions.class); 
   
   private static ClassLoader setContextClassLoaderInternal(final ControllerContext context)
   {
      ClassLoader result = Thread.currentThread().getContextClassLoader();
      if (context instanceof InvokeDispatchContext)
      {
         ClassLoader cl = null;
         InvokeDispatchContext invokeContext = (InvokeDispatchContext) context;
         try
         {
            cl = invokeContext.getClassLoader();
         }
         catch (Throwable t)
         {
            if (log.isTraceEnabled())
               log.trace("Not setting classloader for " + context.getName() + " reason:" + context);
         }
         if (cl != null)
            Thread.currentThread().setContextClassLoader(cl);
      }
      return result;
   }
   
   static ClassLoader setContextClassLoader(final ControllerContext context)
   {
      if (System.getSecurityManager() == null)
      {
         return setContextClassLoaderInternal(context);
      }
      else
      {
         return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
         {
             public ClassLoader run()
             {
                return setContextClassLoaderInternal(context);
             }
         });
      }
   }

   static void resetContextClassLoader(final ClassLoader classLoader)
   {
      if (System.getSecurityManager() == null)
      {
         Thread.currentThread().setContextClassLoader(classLoader);
      }
      else
      {
         AccessController.doPrivileged(new PrivilegedAction<Object>()
         {
             public Object run()
             {
                Thread.currentThread().setContextClassLoader(classLoader);
                return null;
             }
         });
      }
   }
}
