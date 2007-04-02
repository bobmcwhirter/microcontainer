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
package org.jboss.osgi.plugins.dependency;

import java.util.HashMap;
import java.util.Map;

import org.jboss.dependency.plugins.AbstractControllerContextActions;
import org.jboss.dependency.plugins.action.ControllerContextAction;
import org.jboss.dependency.spi.ControllerState;

/**
 * OSGi service controller context actions
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class OSGiServiceReferenceContextActions extends AbstractControllerContextActions
{
   /* The single instance */
   private static OSGiServiceReferenceContextActions instance;

   public OSGiServiceReferenceContextActions(Map<ControllerState, ControllerContextAction> actions)
   {
      super(actions);
   }

   public static OSGiServiceReferenceContextActions getInstance()
   {
      if (instance == null)
      {
         Map<ControllerState, ControllerContextAction> actions = new HashMap<ControllerState, ControllerContextAction>();
         actions.put(ControllerState.PRE_INSTALL, new EnableAction());        
         actions.put(ControllerState.CONFIGURED, new BindAction());
         actions.put(ControllerState.START, new ActivateAction());
         actions.put(ControllerState.INSTALLED, new InstallAction());
         instance = new OSGiServiceReferenceContextActions(actions);
      }
      return instance;
   }

}
