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
package org.jboss.guice.plugins;

import com.google.inject.Binder;
import org.jboss.kernel.Kernel;

/**
 * Guice Objects.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface GuiceObject
{
   static final GuiceObject ALL = new AllGuiceObject();
   static final GuiceObject KERNEL = new KernelGuiceObject();
//   static final GuiceObject LISTENER = new ListenerGuiceObject();

   /**
    * Get the name.
    *
    * @return the name
    */
   String geName();

   /**
    * Create new MC-Guice object.
    *
    * @param kernel the kernel
    * @param binder the binder
    * @return new MC-Guice object instance
    */
   Object createObject(Kernel kernel, Binder binder);
}