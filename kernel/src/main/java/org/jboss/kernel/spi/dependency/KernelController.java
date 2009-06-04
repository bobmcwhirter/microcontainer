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
package org.jboss.kernel.spi.dependency;

import java.util.Set;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.KernelObject;

/**
 * A controller.<p>
 * 
 * The controller is the core component for keeping track
 * of beans to make sure the configuration and lifecycle are
 * done in the correct order including dependencies and
 * classloading considerations. The {@link Kernel} uses this class as its
 * controller, rather than the normal {@link Controller}, and it contains a few
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface KernelController extends KernelObject, Controller
{
   /**
    * Install a context from a {@link BeanMetaData}.
    * 
    * @see Controller#install(org.jboss.dependency.spi.ControllerContext)
    * @param metaData the bean metadata
    * @return the context that is created from the bean meta data. 
    * @throws Throwable for any error
    */
   KernelControllerContext install(BeanMetaData metaData) throws Throwable;

   /**
    * Install a context from a {@link BeanMetaData} in the same way as
    * {@link #install(BeanMetaData)}, but use the passed in target as the target
    * for this context instead of instantiating one in the {@link ControllerState#INSTANTIATED}
    * state.
    * 
    * @see #install(BeanMetaData)
    * @param metaData the metaData
    * @param target the target object
    * @return the context that is cre
    * @throws Throwable for any error
    */
   KernelControllerContext install(BeanMetaData metaData, Object target) throws Throwable;

   /**
    * Add supplies from the context to the my list of suppliers.
    * 
    * @param context the context we want to check for supplies
    */
   void addSupplies(KernelControllerContext context);

   /**
    * Remove supplies from the context from my list of suppliers.
    * 
    * @param context the context we want to check for supplies
    */
   void removeSupplies(KernelControllerContext context);

   /**
    * Get all instantiated contexts of a given type
    * 
    * @param clazz the type
    * @return the contexts
    */
   Set<KernelControllerContext> getInstantiatedContexts(Class<?> clazz);

   /**
    * Get all contexts of a type which are in the given state or above
    *
    * @param clazz the type
    * @param state the required state
    * @return the contexts
    */
   Set<KernelControllerContext> getContexts(Class<?> clazz, ControllerState state);

   /**
    * Get an instantiated context that is of the type passed in.
    * If zero or multiple instances match class clazz
    * a warning is issued, but no throwable is thrown
    *
    * @param clazz the type
    * @return context whose target is instance of this class clazz param or null if zero or multiple such instances
    */
   KernelControllerContext getContextByClass(Class<?> clazz);

   /**
    * Add instantiated context into the map used by {@link #getContextByClass(Class)}.
    * Look at all the context's target's superclasses and interfaces.
    * 
    * @param context the context
    */
   void addInstantiatedContext(KernelControllerContext context);

   /**
    * Remove instantiated context from the map used by {@link #getContextByClass(Class)}.
    * Look at all target's superclasses and interfaces.
    * 
    * @param context the context
    */
   void removeInstantiatedContext(KernelControllerContext context);
}
