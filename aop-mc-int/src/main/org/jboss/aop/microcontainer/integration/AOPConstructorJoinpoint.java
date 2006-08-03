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
package org.jboss.aop.microcontainer.integration;

import java.lang.reflect.Constructor;

import org.jboss.aop.Advisor;
import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.ConstructorInvocation;
import org.jboss.aop.proxy.container.AOPProxyFactory;
import org.jboss.aop.proxy.container.AOPProxyFactoryParameters;
import org.jboss.aop.proxy.container.ContainerCache;
import org.jboss.aop.proxy.container.GeneratedAOPProxyFactory;
import org.jboss.joinpoint.plugins.BasicConstructorJoinPoint;
import org.jboss.reflect.spi.ConstructorInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.repository.spi.MetaDataContext;

/**
 * An AOPConstructorJoinpoint.
 *
 * @TODO This is not correct if the target is already advised
 *       there is no need for the proxy advisor.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AOPConstructorJoinpoint extends BasicConstructorJoinPoint
{
   AOPProxyFactory proxyFactory = new GeneratedAOPProxyFactory();

   MetaDataContext metaDataContext;
   /**
    * Create a new AOPConstructorJoinpoint.
    *
    * @param constructorInfo the constructor info
    */
   public AOPConstructorJoinpoint(ConstructorInfo constructorInfo, MetaDataContext metaDataContext)
   {
      super(constructorInfo);
      this.metaDataContext = metaDataContext;
   }

   public Object dispatch() throws Throwable
   {
      Class clazz = constructorInfo.getDeclaringClass().getType();
      ContainerCache cache = ContainerCache.initialise(AspectManager.instance(), clazz, metaDataContext);
      Object target = createTarget(cache);

      AOPProxyFactoryParameters params = new AOPProxyFactoryParameters();

      params.setProxiedClass(target.getClass());
      params.setMetaDataContext(metaDataContext);
      params.setTarget(target);
      params.setContainerCache(cache);

      Object proxy = proxyFactory.createAdvisedProxy(params);

      return proxy;
   }

   private Object createTarget(ContainerCache cache) throws Throwable
   {
      Advisor advisor = cache.getAdvisor();
      if (advisor != null)
      {
         org.jboss.aop.ConstructorInfo aopinfo = findAopConstructorInfo(advisor);
         Interceptor[] interceptors = (aopinfo != null) ? aopinfo.getInterceptors() : null;

         if (interceptors != null)
         {
            ConstructorInvocation inv = new ConstructorInvocation(aopinfo, aopinfo.getInterceptors());
            inv.setArguments(getArguments());
            return inv.invokeNext();
         }
      }

      return super.dispatch();
   }

   private org.jboss.aop.ConstructorInfo findAopConstructorInfo(Advisor advisor)
   {
      TypeInfo[] params = constructorInfo.getParameterTypes();
      org.jboss.aop.ConstructorInfo[] infos = advisor.getConstructorInfos();
      for (int i = 0 ; i < infos.length ; i++)
      {
         Constructor ctor = infos[i].getConstructor();
         Class[] ctorParams = ctor.getParameterTypes();
         if (params.length == ctorParams.length)
         {
            boolean match = true;
            for (int p = 0 ; p < params.length ; p++)
            {
               if (!params[p].getName().equals(ctorParams[p].getName()))
               {
                  match = false;
                  break;
               }
            }

            if (match)
            {
               return infos[i];
            }
         }
      }
      return null;
   }

}
