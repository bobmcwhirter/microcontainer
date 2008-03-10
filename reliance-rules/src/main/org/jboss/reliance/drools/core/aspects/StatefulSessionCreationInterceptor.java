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
package org.jboss.reliance.drools.core.aspects;

import org.drools.StatefulSession;
import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.microcontainer.aspects.util.ProxyUtils;

/**
 * Statefull session wrapper interceptor.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class StatefulSessionCreationInterceptor implements Interceptor
{
   public String getName()
   {
      return "StatefulSessionInterceptor";
   }

   /**
    * Wrap stateful session.
    *
    * @param invocation the invocation
    * @return invocation's result
    * @throws Throwable for any exception
    */
   public Object invoke(Invocation invocation) throws Throwable
   {
      Object result = invocation.invokeNext();
      if (result instanceof StatefulSession == false)
         throw new IllegalArgumentException("Interceptor should only used on methods the return StatefulSession instance!");

      return ProxyUtils.createProxy((StatefulSession)result, StatefulSession.class);
   }
}
