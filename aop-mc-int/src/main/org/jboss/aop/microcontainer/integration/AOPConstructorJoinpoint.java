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
      AspectManager manager = AspectManager.instance();
      if (manager.isNonAdvisableClassName(clazz.getName()))
      {
         return super.dispatch();
      }
      ContainerCache cache = ContainerCache.initialise(manager, clazz, metaDataContext);
      AOPProxyFactoryParameters params = new AOPProxyFactoryParameters();
      Object target = createTarget(cache, params);

      params.setProxiedClass(target.getClass());
      params.setMetaDataContext(metaDataContext);
      params.setTarget(target);
      params.setContainerCache(cache);

      return proxyFactory.createAdvisedProxy(params);
   }

   private Object createTarget(ContainerCache cache, AOPProxyFactoryParameters params) throws Throwable
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
         
         if (getConstructorInfo().getParameterTypes().length > 0)
         {
            Constructor constructor = null;
            if (aopinfo == null)
            {
               //Fall back to using the class;
               Class clazz = advisor.getClazz();
               Constructor[] ctors = clazz.getConstructors();
               for (Constructor ctor : ctors)
               {
                  if (matchConstructor(ctor))
                  {
                     constructor = ctor;
                     break;
                  }
               }
            }
            else
            {
               constructor = aopinfo.getConstructor();
            }
            params.setCtor(constructor.getParameterTypes(), getArguments());
         }
      }

      return super.dispatch();
   }

   private org.jboss.aop.ConstructorInfo findAopConstructorInfo(Advisor advisor)
   {
      org.jboss.aop.ConstructorInfo[] infos = advisor.getConstructorInfos();
      for (int i = 0 ; i < infos.length ; i++)
      {
         if (matchConstructor(infos[i].getConstructor()))
         {
            return infos[i];
         }
      }
      return null;
   }
   
   private boolean matchConstructor(Constructor ctor)
   {
      TypeInfo[] params = constructorInfo.getParameterTypes();
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
            return true;
         }
      }
      return false;
   }

}
