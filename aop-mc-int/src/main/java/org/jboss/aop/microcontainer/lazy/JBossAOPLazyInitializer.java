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
package org.jboss.aop.microcontainer.lazy;

import java.util.Set;

import org.jboss.aop.proxy.container.AOPProxyFactoryParameters;
import org.jboss.aop.proxy.container.GeneratedAOPProxyFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.plugins.lazy.AbstractLazyInitializer;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.MetaData;

/**
 * JBossAOP lazy initializer.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class JBossAOPLazyInitializer extends AbstractLazyInitializer
{
   @SuppressWarnings("deprecation")
   public Object initializeProxy(Kernel kernel, String bean, boolean exposeClass, Set<String> interfaces) throws Throwable
   {
      return initializeProxy(kernel, bean, exposeClass, interfaces, null);
   }
   
   public Object initializeProxy(Kernel kernel, String bean, boolean exposeClass, Set<String> interfaces, MetaData metaData) throws Throwable
   {
      KernelControllerContext context = getKernelControllerContext(kernel, bean);
      BeanMetaData bmd = context.getBeanMetaData();
      KernelConfigurator configurator = kernel.getConfigurator();
      ClassLoader cl = Configurator.getClassLoader(bmd);

      // TODO - add lazy handler
      AOPProxyFactoryParameters params = new AOPProxyFactoryParameters();
      params.setMetaData(metaData);
      if (exposeClass)
      {
         Class<?> beanClass = getBeanClass(context, configurator, cl);
         params.setProxiedClass(beanClass);
      }
      if (interfaces != null && interfaces.isEmpty() == false)
      {
         params.setInterfaces(getClasses(configurator, interfaces, cl));
      }

      GeneratedAOPProxyFactory factory = new GeneratedAOPProxyFactory();
      return factory.createAdvisedProxy(params);
   }
}
