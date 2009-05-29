/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
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
package org.jboss.dependency.spi;

/**
 * This represents a callback. These are held within a {@link ControllerContext}'s 
 * {@link DependencyInfo}. The {@link ControllerContext} can have several callbacks associated 
 * with it. Callbacks can either be invoked when installing the owning {@link ControllerContext} or
 * when uninstalling the owning {@link ControllerContext}. The {@link ControllerContext}s
 * {@link DependencyInfo} maintains a collection of install callbacks and a collection of 
 * uninstall callbacks.
 * <p> 
 * An install callback is a listener for when other beans of a certain type are installed into
 * the {@link Controller}, and an uninstall callback is a listener for when other beans of a certain type 
 * are uninstalled from the {@link Controller}. Beans with callbacks must expose a method taking a single 
 * parameter, or a property/attribute which is a collection containing elements of a certain type. 
 * The type of the parameter, if using methods, or of the property/attribute collection type specifies 
 * the type of bean install/uninstalls this bean listens to.
 * <p>
 * When installing a bean implementing the given type, the associated install callback method on the 
 * bean owning this callback gets called, and when uninstalling a bean implementing the given type, 
 * the associated uninstall callback method on the bean owning this callback gets called. If a 
 * property/attribute is used installed beans get added to the collection of the bean owning this 
 * callback, and uninstalled beans get removed from the collection.
 *
 * @param <T> expected name type - Class, String, ...
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface CallbackItem<T>
{
   /**
    * The type of beans that my owning bean listens to being installed/uninstalled
    *
    * @return the type
    */
   T getIDependOn();

   /**
    * Get when the dependency is required. The callback must have been invoked 
    * before the bean owning this can enter this state. 
    * The default is {@link ControllerState#CONFIGURED}
    *
    * @return the state when required
    */
   ControllerState getWhenRequired();

   /**
    * Get the state the beans that we are listening for will be in when invoking
    * the listener method/attribute/property. The default is {@link ControllerState#INSTALLED}
    *
    * @return the state of the required of the dependent
    */
   ControllerState getDependentState();

   /**
    * Get the method/attribute/property name used for handling installs/uninstalls 
    * of beans we are listening for.
    *
    * @return the name
    */
   String getAttributeName();

   /**
    * Execute callback when item installed/uninstalled to controller. This will invoke the callback method
    * on the owning bean. If a property/attribute as used it will add to the collection if it is an install
    * callback, and remove from the collection if it is an uninstall callback. 
    *
    * @param controller the controller
    * @param isInstallPhase true if callback should be called on install, false if callback should be called on uninstall
    * @throws Throwable for any error
    */
   void ownerCallback(Controller controller, boolean isInstallPhase) throws Throwable;

   /**
    * Execute callback with current changed context.
    *
    * @param controller the controller
    * @param context the new context
    * @param isInstallPhase install or uninstall
    * @throws Throwable for any error
    */
   void changeCallback(Controller controller, ControllerContext context, boolean isInstallPhase) throws Throwable;
}
