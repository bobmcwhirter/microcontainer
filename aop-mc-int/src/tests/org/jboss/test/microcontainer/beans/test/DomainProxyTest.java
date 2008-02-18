/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.test.microcontainer.beans.test;

import org.jboss.aop.AspectManager;
import org.jboss.aop.DomainDefinition;
import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.aop.proxy.container.AOPProxyFactoryParameters;
import org.jboss.aop.proxy.container.ContainerCache;
import org.jboss.aop.proxy.container.GeneratedAOPProxyFactory;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class DomainProxyTest extends AOPMicrocontainerTest
{
   GeneratedAOPProxyFactory proxyFactory = new GeneratedAOPProxyFactory();

   public DomainProxyTest(String name)
   {
      super(name);
   }
   
   protected AspectManager getMainAspectManager()
   {
      AspectManager manager = (AspectManager)getBean("AspectManager");
      assertNotNull(manager);
      return manager;
   }
   
   protected AspectManager getDomain(String name)
   {
      AspectManager manager = getMainAspectManager();
      DomainDefinition def = manager.getContainer(name);
      assertNotNull(def);
      AspectManager domain = def.getManager();
      assertNotNull(domain);
      return domain;
   }
   
   protected Object createProxy(Object target)
   {
      AspectManager manager = getMainAspectManager();
      return createProxy(manager, target);
   }

   protected Object createProxy(String domainName, Object target)
   {
      AspectManager domain = getDomain(domainName);
      return createProxy(domain, target);
   }
   
   private Object createProxy(AspectManager manager, Object target)
   {
      ContainerCache containerCache = ContainerCache.initialise(manager, target.getClass(), null, false);
      AOPProxyFactoryParameters params = new AOPProxyFactoryParameters();
      params.setProxiedClass(target.getClass());
      params.setTarget(target);
      params.setContainerCache(containerCache);
      return proxyFactory.createAdvisedProxy(params);
   }
}