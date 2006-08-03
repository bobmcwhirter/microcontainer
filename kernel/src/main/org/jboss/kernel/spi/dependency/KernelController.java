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

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.Controller;
import org.jboss.kernel.spi.KernelObject;

/**
 * A controller.<p>
 * 
 * The controller is the core component for keeping track
 * of beans to make sure the configuration and lifecycle are
 * done in the correct order including dependencies and
 * classloading considerations. 
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface KernelController extends KernelObject, Controller
{
   /**
    * Install a context
    * 
    * @param metaData the metaData
    * @return the context
    * @throws Throwable for any error
    */
   KernelControllerContext install(BeanMetaData metaData) throws Throwable;

   /**
    * Install a context
    * 
    * @param metaData the metaData
    * @param target the target object
    * @return the context
    * @throws Throwable for any error
    */
   KernelControllerContext install(BeanMetaData metaData, Object target) throws Throwable;

   /**
    * Add supplies
    * 
    * @param context the context
    */
   void addSupplies(KernelControllerContext context);

   /**
    * Remove supplies
    * 
    * @param context the context
    */
   void removeSupplies(KernelControllerContext context);
}
