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
 * The API similar to the DynamicMBean API
 * where there are methods to get/set Properties/Attributes and invoke on
 * methods/operations with each context knowing how to do this, e.g.
 * KernelControllerContext -> Configurator
 * ServiceControllerContext -> MBeanServer
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface DispatchContext
{
   /**
    * Getter property / attribute
    *
    * @param name
    * @return target's property / attribute instance
    * @throws Throwable
    */
   Object get(String name) throws Throwable;

   /**
    * Setter property / attribute
    *
    * @param name
    * @param value set target's property / attribute instance
    * @throws Throwable
    */
   void set(String name, Object value) throws Throwable;

   /**
    * Invoke method / operation
    *
    * @param name
    * @param parameters
    * @param signature
    * @return inovocation's return object
    * @throws Throwable
    */
   Object invoke(String name, Object parameters[], String[] signature) throws Throwable;

   /**
    * Get context's classloader.
    * Used when determining type info for parameter and
    * getting the parameter actual value.
    *
    * @return context's classloader
    * @throws Throwable
    */
   ClassLoader getClassLoader() throws Throwable;

   /**
    * Get context's target, if available.
    * @return target
    */
   Object getTarget();

}
