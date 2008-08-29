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

/**
 * A KernelControllerContextAware.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface KernelControllerContextAware
{
   /**
    * Set the controller context.<p> 
    * 
    * This is invoked after construction with the controller context.<p>
    * 
    * WARNING: This protocol is subject to change with the classadapter implementation.
    * 
    * @todo integrate better with aop and instance classadapter such that
    *       describe can process this without the instance actually existing.
    *       Use case: creating jmx/jsr77 hollow proxy for on-demand beans before they are instantiated.
    * @param context the context
    * @throws Exception for any error
    */
   void setKernelControllerContext(KernelControllerContext context) throws Exception;

   /**
    * Unset the controller context.<p> 
    * 
    * This is before uninstallation with null.<p>
    * 
    * WARNING: This protocol is subject to change with the classadapter implementation.
    * 
    * @todo integrate better with aop and instance classadapter such that
    *       describe can process this without the instance actually existing.
    *       Use case: creating jmx/jsr77 hollow proxy for on-demand beans before they are instantiated.
    * @param context the context
    * @throws Exception for any error
    */
   void unsetKernelControllerContext(KernelControllerContext context) throws Exception;
}
