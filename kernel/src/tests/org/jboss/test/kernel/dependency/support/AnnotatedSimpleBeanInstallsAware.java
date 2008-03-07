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
package org.jboss.test.kernel.dependency.support;

import org.jboss.beans.metadata.api.model.FromContext;
import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.beans.metadata.api.annotations.InstallMethod;
import org.jboss.beans.metadata.api.annotations.UninstallMethod;
import org.jboss.dependency.spi.ControllerState;

/**
 * SimpleBeanInstallAware
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 */
public class AnnotatedSimpleBeanInstallsAware extends SimpleBeanInstallsAware
{
   @InstallMethod(whenRequired = "Instantiated")
   public void addInstantiated(@Inject(fromContext=FromContext.STATE) ControllerState state)
   {
      states.add(state);
   }

   @InstallMethod(whenRequired = "Configured")
   public void addConfigured(@Inject(fromContext=FromContext.STATE) ControllerState state)
   {
      states.add(state);
   }

   @InstallMethod(whenRequired = "Create")
   public void addCreate(@Inject(fromContext= FromContext.STATE) ControllerState state)
   {
      states.add(state);
   }

   @InstallMethod(whenRequired = "Start")
   public void addStart(@Inject(fromContext=FromContext.STATE) ControllerState state)
   {
      states.add(state);
   }

   @UninstallMethod(whenRequired = "Instantiated")
   public void removeInstantiated(@Inject(fromContext=FromContext.STATE) ControllerState state)
   {
      states.remove(state);
   }

   @UninstallMethod(whenRequired = "Configured")
   public void removeConfigured(@Inject(fromContext=FromContext.STATE) ControllerState state)
   {
      states.remove(state);
   }

   @UninstallMethod(whenRequired = "Create")
   public void removeCreate(@Inject(fromContext= FromContext.STATE) ControllerState state)
   {
      states.remove(state);
   }

   @UninstallMethod(whenRequired = "Start")
   public void removeStart(@Inject(fromContext=FromContext.STATE) ControllerState state)
   {
      states.remove(state);
   }
}
