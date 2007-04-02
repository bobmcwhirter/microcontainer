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
package org.jboss.dependency.plugins.action;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import org.jboss.dependency.spi.ControllerContext;

/**
 * Access controller context.
 * It runs action in simple mode if there is no security manager present.
 * While running simple action, you cast the actual context into less specific type.  
 *
 * @param <S> simple ControllerContext impl
 * @param <T> full ControllerContext impl
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AccessControllerContextAction<S extends  ControllerContext, T extends ControllerContext> implements ControllerContextAction
{
   /**
    * Validate context
    *
    * @param context context instance to validate
    * @return false if not able to use privileged action
    */
   protected abstract boolean validateContext(ControllerContext context);

   /**
    * Cast context instance.
    * We can throw class cast exception if unable to cast it it S.
    *
    * @param context context to cast
    * @return casted context
    */
   protected abstract S simpleContextCast(ControllerContext context);

   /**
    * Cast context instance.
    * We can throw class cast exception if unable to cast it it T.
    *
    * @param context context to cast
    * @return casted context
    */
   protected abstract T fullContextCast(ControllerContext context);

   public void install(final ControllerContext context) throws Throwable
   {
      if (System.getSecurityManager() == null || validateContext(context) == false)
         simpleInstallAction(simpleContextCast(context));
      else
      {
         PrivilegedExceptionAction<Object> action = new PrivilegedExceptionAction<Object>()
         {
            public Object run() throws Exception
            {
               try
               {
                  secureInstallAction(fullContextCast(context));
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

   /**
    * Unsecure call to install action.
    *
    * @param context the context
    * @throws Throwable for any error
    */
   protected abstract void simpleInstallAction(S context) throws Throwable;

   /**
    * Secure call to install action.
    *
    * @param context the context
    * @throws Throwable for any error
    */
   protected abstract void secureInstallAction(T context) throws Throwable;

   public void uninstall(final ControllerContext context)
   {
      if (System.getSecurityManager() == null || validateContext(context) == false)
         simpleUninstallAction(simpleContextCast(context));
      else
      {
         PrivilegedAction<Object> action = new PrivilegedAction<Object>()
         {
            public Object run()
            {
               secureUninstallAction(fullContextCast(context));
               return null;
            }
         };
         AccessController.doPrivileged(action);
      }
   }

   /**
    * Unsecure call to uninstall action.
    *
    * @param context the context
    */
   protected abstract void simpleUninstallAction(S context);

   /**
    * Secure call to uninstall action.
    *
    * @param context the context
    */
   protected abstract void secureUninstallAction(T context);

}
