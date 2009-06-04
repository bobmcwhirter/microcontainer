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
package org.jboss.kernel.spi.lazy;

import java.util.Set;

import org.jboss.kernel.Kernel;
import org.jboss.metadata.spi.MetaData;

/**
 * Lazy initializer used by LazyProxyFactory
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface LazyInitializer
{
   /**
    * Initialize lazy proxy with no metadata.
    *
    * @param kernel the kernel
    * @param bean the bean to wrap
    * @param exposeClass do we expose full class
    * @param interfaces interfaces to expose
    * @return the proxy
    * @throws Throwable for any error
    */
   Object initializeProxy(Kernel kernel, String bean, boolean exposeClass, Set<String> interfaces) throws Throwable;

   /**
    * Initialize lazy proxy.
    *
    * @param kernel the kernel
    * @param bean the bean to wrap
    * @param exposeClass do we expose full class
    * @param interfaces interfaces to expose
    * @param metaData the metaData
    * @return the proxy
    * @throws Throwable for any error
    */
   Object initializeProxy(Kernel kernel, String bean, boolean exposeClass, Set<String> interfaces, MetaData metaData) throws Throwable;
}
