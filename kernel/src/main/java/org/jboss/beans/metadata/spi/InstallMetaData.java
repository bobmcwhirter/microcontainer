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
package org.jboss.beans.metadata.spi;

import org.jboss.dependency.spi.ControllerState;

/**
 * Metadata about an (un)installation method.
 * The method will be called when the bean is installed (if this metadata
 * is in the list returned by {@link BeanMetaData#getInstalls()}, or when the bean
 * is uninstalled if it is in the list returned by {@link BeanMetaData#getUninstalls()}.
 * Install methods can either exist on the bean owning this {@link InstallMetaData} or
 * on another bean, in which case we will get a dependency on the other bean.<p>
 * 
 * Normally (un)installation methods will be called when invoked upon installing to/uninstalling from
 * the {@link ControllerState#INSTALLED} state, but this can be overridden by setting {@link #setState(ControllerState)} 
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface InstallMetaData extends LifecycleMetaData
{
   /**
    * Get the bean name on which we should invoke the (un)installation method.
    *
    * @return the bean name.
    */
   String getBean();
   
   /**
    * Get the required state of bean containing the (un)installation method
    * 
    * @return the required state or null if it must be in the registry
    */
   ControllerState getDependentState();
}
