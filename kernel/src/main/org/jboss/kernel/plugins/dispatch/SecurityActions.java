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
package org.jboss.kernel.plugins.dispatch;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;

/**
 * SecurityActions.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
class SecurityActions
{
   public static ClassLoader getClassLoader(final InvokeDispatchContext context) throws Throwable
   {
      if (System.getSecurityManager() == null)
      {
         return context.getClassLoader();
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
                      return context.getClassLoader();
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
                   catch (Throwable e)
                   {
                      throw new RuntimeException("Error retrieving classloader", e);
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
}
