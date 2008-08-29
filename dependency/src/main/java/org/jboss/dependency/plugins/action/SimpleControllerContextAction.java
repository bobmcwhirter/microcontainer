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

import org.jboss.dependency.spi.ControllerContext;

/**
 * Simple and full context impls are the same.
 *
 * @param <T> ControllerContext impl
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class SimpleControllerContextAction<T extends ControllerContext> extends AccessControllerContextAction<T, T>
{
   /**
    * Cast the context to the actual implementation.
    *
    * @param context the context
    * @return exact context type
    */
   protected abstract T contextCast(ControllerContext context);

   protected T simpleContextCast(ControllerContext context)
   {
      return contextCast(context);
   }

   protected T fullContextCast(ControllerContext context)
   {
      return contextCast(context);
   }

   /**
    * The install.
    *
    * @param context the context
    * @throws Throwable for any error
    */
   protected abstract void installAction(T context) throws Throwable;

   protected void simpleInstallAction(T context) throws Throwable
   {
      installAction(context);
   }

   protected void secureInstallAction(T context) throws Throwable
   {
      installAction(context);
   }

   /**
    * The uninstall.
    *
    * @param context the context
    */
   protected abstract void uninstallAction(T context);

   protected void simpleUninstallAction(T context)
   {
      uninstallAction(context);
   }

   protected void secureUninstallAction(T context)
   {
      uninstallAction(context);
   }

}
