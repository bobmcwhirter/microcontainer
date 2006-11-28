/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.kernel.plugins.registry;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;
import org.jboss.util.NotImplementedException;

/**
 * Abstract Kernel registry entry.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractKernelRegistryEntry extends JBossObject implements KernelRegistryEntry
{
   /** The name */
   protected Object name;

   /** The target */
   protected Object target;

   /**
    * Create an abstract kernel registry entry
    * 
    * @param target any target object
    */
   public AbstractKernelRegistryEntry(Object target)
   {
      this.target = target;
   }

   /**
    * Create an abstract kernel registry entry
    *
    * @param name the name 
    * @param target any target object
    */
   public AbstractKernelRegistryEntry(Object name, Object target)
   {
      this.name = name;
      this.target = target;
   }

   public Object getName()
   {
      return name;
   }
   
   public void setName(Object name)
   {
      this.name = name;
      flushJBossObjectCache();
   }

   public Object getTarget()
   {
      return target;
   }

   // @todo SORT THIS OUT, i.e. dependency that doesn't go through controller
   
   public Controller getController()
   {
      throw new NotImplementedException("getController");
   }

   public DependencyInfo getDependencyInfo()
   {
      return null;
   }

   public Throwable getError()
   {
      throw new NotImplementedException("getError");
   }

   public ControllerState getState()
   {
      return ControllerState.ERROR;
   }
   
   public void setState(ControllerState state)
   {
      throw new org.jboss.util.NotImplementedException("setState");
   }

   public ControllerState getRequiredState()
   {
      throw new NotImplementedException("getRequiredState");
   }

   public void setRequiredState(ControllerState state)
   {
      throw new NotImplementedException("setRequiredState");
   }
   
   public ControllerMode getMode()
   {
      return ControllerMode.MANUAL;
   }
   
   public void setMode(ControllerMode mode)
   {
      throw new NotImplementedException("setMode");
   }

   public void install(ControllerState fromState, ControllerState toState) throws Throwable
   {
      throw new NotImplementedException("install");
      
   }

   public void setController(Controller controller)
   {
      throw new NotImplementedException("setController");
      
   }

   public void setError(Throwable error)
   {
      throw new NotImplementedException("NYI setError");
      
   }

   public void uninstall(ControllerState fromState, ControllerState toState)
   {
      throw new NotImplementedException("uninstall");
      
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("target=").append(target);
   }
   
   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(target);
   }
}