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
package org.jboss.dependency.plugins;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerContextActions;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * A ControllerContext.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractControllerContext extends JBossObject implements ControllerContext
{
   /** The name */
   private Object name;

   /** The target */
   private Object target;

   /** The controller */
   private Controller controller;

   /** The state */
   private ControllerState state = ControllerState.ERROR;

   /** The required state */
   private ControllerState requiredState = ControllerState.NOT_INSTALLED;

   /** The mdoe */
   private ControllerMode mode = ControllerMode.AUTOMATIC;
   
   /** The actions */
   private ControllerContextActions actions;
   
   /** The dependencies */
   private DependencyInfo dependencies;

   /** Any error */
   private Throwable error;

   /**
    * Create a new AbstractControllerContext.
    * 
    * @param name the name
    * @param actions the actions
    */
   public AbstractControllerContext(Object name, ControllerContextActions actions)
   {
      this(name, actions, null, null);
   }

   /**
    * Create a new AbstractControllerContext.
    * 
    * @param name the name
    * @param actions the actions
    * @param dependencies the dependencies
    */
   public AbstractControllerContext(Object name, ControllerContextActions actions, DependencyInfo dependencies)
   {
      this(name, actions, dependencies, null);
   }

   /**
    * Create a new AbstractControllerContext.
    * 
    * @param name the name
    * @param actions the actions
    * @param dependencies the dependencies
    * @param target the target
    */
   public AbstractControllerContext(Object name, ControllerContextActions actions, DependencyInfo dependencies, Object target)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (actions == null)
         throw new IllegalArgumentException("Null actions");

      this.name = name;
      this.actions = actions;
      if (dependencies == null)
         this.dependencies = new AbstractDependencyInfo();
      else
        this.dependencies = dependencies;
      this.target = target;
   }

   /**
    * Create a new AbstractControllerContext.
    * 
    * @param name the name
    * @param target the target
    */
   public AbstractControllerContext(Object name, Object target)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");

      this.name = name;
      this.target = target;
   }
   
   public Object getName()
   {
      return name;
   }

   /**
    * Set the name
    * 
    * @param name the name
    */
   public void setName(Object name)
   {
      this.name = name;
   }
   
   public ControllerState getState()
   {
      return state;
   }
   
   public ControllerState getRequiredState()
   {
      return requiredState;
   }
   
   public void setRequiredState(ControllerState state)
   {
      this.requiredState = state;
   }
   
   public ControllerMode getMode()
   {
      return mode;
   }
   
   public void setMode(ControllerMode mode)
   {
      this.mode = mode;
      flushJBossObjectCache();
   }

   /**
    * Get the controller
    * 
    * @return the controller
    */
   public Controller getController()
   {
      return controller;
   }
   
   public void setController(Controller controller)
   {
      this.controller = controller;
      flushJBossObjectCache();
   }

   public DependencyInfo getDependencyInfo()
   {
      return dependencies;
   }

   public Object getTarget()
   {
      return target;
   }

   /**
    * Set the target
    *
    * @param target the target
    */
   public void setTarget(Object target)
   {
      this.target = target;
      flushJBossObjectCache();
   }
   
   public Throwable getError()
   {
      return error;
   }

   public void setError(Throwable error)
   {
      this.error = error;
      state = ControllerState.ERROR;
      flushJBossObjectCache();
   }

   public void install(ControllerState fromState, ControllerState toState) throws Throwable
   {
      this.error = null;
      actions.install(this, fromState, toState);
      this.state = toState;
      flushJBossObjectCache();
   }

   public void uninstall(ControllerState fromState, ControllerState toState)
   {
      this.error = null;
      this.state = toState;
      flushJBossObjectCache();
      actions.uninstall(this, fromState, toState);
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("name=").append(name);
      buffer.append(" target=").append(target);
      if (error != null || state.equals(ControllerState.ERROR) == false)
         buffer.append(" state=").append(state.getStateString());
      if (ControllerMode.AUTOMATIC.equals(mode) == false)
      {
         buffer.append(" mode=").append(mode.getModeString());
         buffer.append(" requiredState=").append(requiredState.getStateString());
      }
      if (dependencies != null)
         buffer.append(" depends=").append(dependencies);
      if (error != null)
      {
         StringWriter stringWriter = new StringWriter();
         PrintWriter writer = new PrintWriter(stringWriter);
         error.printStackTrace(writer);
         writer.flush();
         buffer.append(" error=").append(stringWriter.getBuffer());
      }
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append("name=").append(name);
      if (error != null || state.equals(ControllerState.ERROR) == false)
         buffer.append(" state=").append(state.getStateString());
      if (ControllerMode.AUTOMATIC.equals(mode) == false)
      {
         buffer.append(" mode=").append(mode.getModeString());
         buffer.append(" requiredState=").append(requiredState.getStateString());
      }
      if (error != null)
         buffer.append(" error=").append(error.getClass().getName()).append(": ").append(error.getMessage());
   }
}
