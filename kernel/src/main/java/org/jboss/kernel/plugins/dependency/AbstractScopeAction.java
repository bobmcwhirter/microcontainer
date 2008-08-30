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

import org.jboss.dependency.plugins.action.SimpleControllerContextAction;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ScopeInfo;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * Abstract scope action.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractScopeAction extends SimpleControllerContextAction<ControllerContext>
{
   protected ControllerContext contextCast(ControllerContext context)
   {
      return context;
   }

   protected boolean validateContext(ControllerContext context)
   {
      return true;
   }

   /**
    * Get install scope key.
    *
    * @param context the context
    * @return install scope key
    */
   protected ScopeKey getScopeKey(ControllerContext context)
   {
      ScopeInfo scopeInfo = context.getScopeInfo();
      if (scopeInfo != null)
         return scopeInfo.getInstallScope();

      return null;
   }
}