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

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.registry.KernelRegistryEntry;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.scope.ScopeKey;
/**
 * Information about dependencies and state.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface KernelControllerContext extends KernelRegistryEntry
{
   /**
    * Get the kernel
    * 
    * @return the kernel
    */
   Kernel getKernel();
   
   /**
    * Get the BeanInfo
    * 
    * @return the bean info
    */
   BeanInfo getBeanInfo();
   
   /**
    * Set the bean info
    * 
    * @param info the bean info
    */
   void setBeanInfo(BeanInfo info);
   
   /**
    * Get the metadata
    * 
    * @return the bean metadata
    */
   BeanMetaData getBeanMetaData();
   
   /**
    * Set the target
    * 
    * @param target the target
    */
   void setTarget(Object target);
   
   /**
    * Get the metadata
    * 
    * @return the metadata
    */
   MetaData getMetaData();
   
   /**
    * Get the scope
    * 
    * @return the scope
    */
   ScopeKey getScope();
   
   /**
    * Set the scope
    * 
    * @param key the scope key
    */
   void setScope(ScopeKey key);
}
