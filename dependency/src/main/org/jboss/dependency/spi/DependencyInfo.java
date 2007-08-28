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
package org.jboss.dependency.spi;

import java.util.List;
import java.util.Set;

import org.jboss.util.JBossInterface;

/**
 * Information about dependencies.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface DependencyInfo extends JBossInterface
{
   /**
    * Return what we depend upon
    *
    * @param type the class of the dependency or null for all types 
    * @return our dependencies
    */
   Set<DependencyItem> getIDependOn(Class<?> type);

   /**
    * Return what depends upon me
    * 
    * @param type the class of the dependency or null for all types 
    * @return what depends upon this
    */
   Set<DependencyItem> getDependsOnMe(Class<?> type);

   /**
    * Add a dependency
    * 
    * @param dependency the dependency to add
    */
   void addIDependOn(DependencyItem dependency);

   /**
    * Remove a dependency
    * 
    * @param dependency the dependency to remove
    */
   void removeIDependOn(DependencyItem dependency);

   /**
    * Add a dependency reference
    * 
    * @param dependency the dependency to add
    */
   void addDependsOnMe(DependencyItem dependency);

   /**
    * Remove a dependency reference
    * 
    * @param dependency the dependency to remove
    */
   void removeDependsOnMe(DependencyItem dependency);

   /**
    * Try to resolve dependencies
    * 
    * @param controller the controller
    * @param state the state of dependency to resolve
    * @return true when all dependencies are resolved
    */
   boolean resolveDependencies(Controller controller, ControllerState state);
   
   /**
    * Return the unresolved dependencies
    * 
    * @return our unresolved dependencies
    */
   Set<DependencyItem> getUnresolvedDependencies();

   /**
    * Add a callback reference
    *
    * @param <T> the callback item type
    * @param callbackItem the callback to add
    */
   <T> void addInstallItem(CallbackItem<T> callbackItem);

   /**
    * Remove a callback reference
    *
    * @param <T> the callback item type
    * @param callbackItem the callback to remove
    */
   <T> void removeInstallItem(CallbackItem<T> callbackItem);

   /**
    * Return install callbacks.
    *
    * @return our install callbacks
    */
   Set<CallbackItem<?>> getInstallItems();

   /**
    * Add a callback reference
    *
    * @param <T> the callback item type
    * @param callbackItem the callback to add
    */
   <T> void addUninstallItem(CallbackItem<T> callbackItem);

   /**
    * Remove a callback reference
    *
    * @param <T> the callback item type
    * @param callbackItem the callback to remove
    */
   <T> void removeUninstallItem(CallbackItem<T> callbackItem);

   /**
    * Return uninstall callbacks.
    *
    * @return our uninstall callbacks
    */
   Set<CallbackItem<?>> getUninstallItems();
   
   /**
    * Add a lifecycle callback
    * 
    * @param lifecycleCallbackItem The lifecycle callback to add
    */
   void addLifecycleCallback(LifecycleCallbackItem lifecycleCallbackItem);
   
   /**
    * Get the lifecycle callbacks
    * 
    * @return our lifecycle callbacks
    */
   List<LifecycleCallbackItem> getLifecycleCallbacks();

   /**
    * Can we use this context for autowiring.
    *
    * @return true if context can be used for autowiring
    */
   boolean isAutowireCandidate();
   
   /**
    * Set whether this is an autowire candidate
    * 
    * @param candidate true if it is a candidate for autowiring
    */
   void setAutowireCandidate(boolean candidate);
}