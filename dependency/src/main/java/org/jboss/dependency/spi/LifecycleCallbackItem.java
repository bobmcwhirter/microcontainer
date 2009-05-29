/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.dependency.spi;

/**
 * This represents a lifecycle callback applied via  aop the
 * <code>
 *   &lt;lifecycle-xxx&gt;
 * </code> bindings.
 * 
 * This represents a dependency on a {@link ControllerContext}. These are
 * held within a {@link ControllerContext}'s {@link DependencyInfo}.
 * <p>
 * When the owning {@link ControllerContext} enters the state in 
 * {@link #getWhenRequired()}, if the {@link ControllerContext} of the lifecycle callback
 * we have a dependency on has not reached the state in {@link #getDependentState()} the owning
 * {@link ControllerContext} cannot proceed to the state in {@link #getWhenRequired()}.
 * <p>
 * When the owning {@link ControllerContext} enters the {@link #getWhenRequired()} 
 * state on install the  {@link #install(ControllerContext)} method is called by the controller. 
 * This will delegate to the
 * <code>
 * public void install(ControllerContext context)
 * </code> method of the lifecycle callback bean implementation.
 * <p>
 * On uninstalling the owning {@link ControllerContext} from the {@link #getWhenRequired()}
 * state, the {@link #uninstall(ControllerContext)} method is called by the controller. 
 * This will delegate to the
 * <code>
 * public void uninstall(ControllerContext context)
 * </code> method of the lifecycle callback bean implementation.
 * 
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public interface LifecycleCallbackItem
{
   /**
    * Gets the name of the lifecycle callback {@link ControllerContext}. This
    * holds the bean implementing the lifecycle callback.
    * 
    * @return the name of the lifecycle callback
    */
   Object getBean();
   
   /**
    * Get the target state of the bean this callback applies to indicating when this callback should trigger
    * @return the state 
    */
   ControllerState getWhenRequired();
   
   /**
    * The required state of the lifecycle callback bean
    * @return the dependant state
    */
   ControllerState getDependentState();
   
   /**
    * Call when the target bean is installed
    * @param ctx the context of the target bean
    * @throws Exception for any error
    */
   void install(ControllerContext ctx) throws Exception;
   
   /**
    * Call when the target bean is uninstalled
    * @param ctx the context of the target bean
    */
   void uninstall(ControllerContext ctx);
}
