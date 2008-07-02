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
package org.jboss.dependency.plugins;

import java.util.Map;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContextActions;
import org.jboss.dependency.spi.ScopeInfo;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.plugins.action.ControllerContextAction;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * Scoped alias controller context.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ScopedAliasControllerContext extends AbstractAliasControllerContext
{
   private ScopedAliasControllerContext(Object alias, Object original, ScopeKey scopeKey, ControllerContextActions actions)
   {
      super(alias, scopeKey.toString(), original, actions);
      ScopeInfo scopeInfo = getScopeInfo();
      scopeInfo.setInstallScope(scopeKey);
   }

   /**
    * Add scoped alias.
    *
    * @param alias the alias
    * @param original the original
    * @param scopeKey the scope key
    * @param controller the controller
    * @throws Throwable for any error
    */
   public static void addAlias(Object alias, Object original, ScopeKey scopeKey, Controller controller) throws Throwable
   {
      if (controller instanceof AbstractController == false)
         throw new IllegalArgumentException("Can only handle AbstractController.");

      AbstractController ac = (AbstractController)controller;
      Map<ControllerState, ControllerContextAction> map = ac.createAliasActions();
      ControllerContextActions actions = new AbstractControllerContextActions(map);
      controller.install(new ScopedAliasControllerContext(alias, original, scopeKey, actions));
   }

   /**
    * Remove alias.
    *
    * @param alias the alias
    * @param scopeKey the scope key
    * @param controller the controller
    */
   public static void removeAlias(Object alias, ScopeKey scopeKey, Controller controller)
   {
      controller.uninstall(alias + "_Alias_" + scopeKey);      
   }
}