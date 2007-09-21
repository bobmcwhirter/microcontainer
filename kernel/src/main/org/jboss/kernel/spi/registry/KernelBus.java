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
package org.jboss.kernel.spi.registry;

import org.jboss.kernel.spi.KernelObject;

/**
 * A bus.<p>
 * 
 * The bus allows detatched invocations on named components in
 * the registry. 
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface KernelBus extends KernelObject
{
   /**
    * Getter property / attribute
    *
    * @param name entry name
    * @param getter property / attribute name
    * @return target's property / attribute instance
    * @throws Throwable for any error
    */
   Object get(Object name, String getter) throws Throwable;

   /**
    * Setter property / attribute
    *
    * @param name entry name
    * @param setter property / attribute name
    * @param value set target's property / attribute instance
    * @throws Throwable for any error
    */
   void set(Object name, String setter, Object value) throws Throwable;

   /**
    * Invoke method / operation
    *
    * @param name entry name
    * @param methodName method name
    * @param parameters parameter values
    * @param signature method's parameter types / signatures
    * @return inovocation's return object
    * @throws Throwable for any error
    */
   Object invoke(Object name, String methodName, Object[] parameters, String[] signature) throws Throwable;
}
