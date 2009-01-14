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
package org.jboss.beans.metadata.plugins;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * SecurityActions.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @version $Revision: 1.1 $
 */
class SecurityActions
{
   /**
    * Set context classloader.
    *
    * @param cl the classloader
    * @return previous context classloader
    * @throws Throwable for any error
    */
   static ClassLoader setContextClassLoader(final ClassLoader cl) throws Throwable
   {
      if (System.getSecurityManager() == null)
      {
         ClassLoader result = Thread.currentThread().getContextClassLoader();
         if (cl != null)
            Thread.currentThread().setContextClassLoader(cl);
         return result;
      }
      else
      {
         try
         {
            return AccessController.doPrivileged(new PrivilegedExceptionAction<ClassLoader>()
            {
               public ClassLoader run() throws Exception
               {
                  try
                  {
                     ClassLoader result = Thread.currentThread().getContextClassLoader();
                     if (cl != null)
                        Thread.currentThread().setContextClassLoader(cl);
                     return result;
                  }
                  catch (Exception e)
                  {
                     throw e;
                  }
                  catch (Error e)
                  {
                     throw e;
                  }
                  catch (Throwable e)
                  {
                     throw new RuntimeException("Error setting context classloader", e);
                  }
               }
            });
         }
         catch (PrivilegedActionException e)
         {
            throw e.getCause();
         }
      }
   }

   /**
    * Reset context classloader.
    *
    * @param classLoader the classloader
    */
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

   /**
    * Get classloader from class.
    *
    * @param clazz the class
    * @return class's classloader
    */
   static ClassLoader getClassLoader(final Class<?> clazz)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm == null)
      {
         return clazz.getClassLoader();
      }
      else
      {
         return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>()
         {
            public ClassLoader run()
            {
               return clazz.getClassLoader();
            }
         });
      }
   }
}
