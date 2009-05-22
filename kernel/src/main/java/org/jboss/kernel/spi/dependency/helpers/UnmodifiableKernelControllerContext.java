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
package org.jboss.kernel.spi.dependency.helpers;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.helpers.UnmodifiableBeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.helpers.UnmodifiableControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * Unmodifiable instance.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class UnmodifiableKernelControllerContext extends UnmodifiableControllerContext implements KernelControllerContext
{
   public UnmodifiableKernelControllerContext(KernelControllerContext delegate)
   {
      super(delegate);
   }

   protected KernelControllerContext getDelegate()
   {
      return KernelControllerContext.class.cast(super.getDelegate());
   }

   public Kernel getKernel()
   {
      return getDelegate().getKernel();
   }

   public BeanInfo getBeanInfo()
   {
      BeanInfo beanInfo = getDelegate().getBeanInfo();
      return beanInfo != null ? new UnmodifiableBeanInfo(beanInfo) : null;
   }

   public void setBeanInfo(BeanInfo info)
   {
      throw new UnsupportedOperationException("Cannot execute set on unmodifiable wrapper.");
   }

   public BeanMetaData getBeanMetaData()
   {
      return getDelegate().getBeanMetaData();
   }

   public void setTarget(Object target)
   {
      throw new UnsupportedOperationException("Cannot execute set on unmodifiable wrapper.");
   }

   public void setName(Object name)
   {
      throw new UnsupportedOperationException("Cannot execute set on unmodifiable wrapper.");
   }

   public Object invoke(String name, Object parameters[], String[] signature) throws Throwable
   {
      throw new UnsupportedOperationException("Cannot execute invoke on unmodifiable wrapper.");
   }

   public ClassLoader getClassLoader() throws Throwable
   {
      return getDelegate().getClassLoader();
   }

   public Object get(String name) throws Throwable
   {
      return getDelegate().get(name);
   }

   public void set(String name, Object value) throws Throwable
   {
      throw new UnsupportedOperationException("Cannot execute set on unmodifiable wrapper.");
   }

   public ControllerState lifecycleInvocation(String name, Object[] parameters, String[] signature) throws Throwable
   {
      return null;
   }
}
