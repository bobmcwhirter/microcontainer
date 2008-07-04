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

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

/**
 * Simple execution wrapper
 *
 * @author ales.justin@jboss.org
 */
abstract class ExecutionWrapper
{
   /**
    * Execute.
    *
    * @param access the access controller context
    * @return execution's value
    * @throws Throwable for any error
    */
   public Object execute(AccessControlContext access) throws Throwable
   {
      if (access == null)
      {
         return execute();
      }
      else
      {
         PrivilegedExceptionAction<Object> action = getAction();
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

   /**
    * Execute w/o access controller context.
    *
    * @return execution's value
    * @throws Throwable for any error
    */
   protected abstract Object execute() throws Throwable;

   /**
    * Get the privileged action.
    *
    * @return the privileged exception action
    */
   protected abstract PrivilegedExceptionAction<Object> getAction();
}

