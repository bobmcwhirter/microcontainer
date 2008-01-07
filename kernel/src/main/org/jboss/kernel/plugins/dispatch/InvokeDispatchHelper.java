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
package org.jboss.kernel.plugins.dispatch;

import java.util.List;

import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.logging.Logger;

/**
 * Helper - reducing duplicated code.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class InvokeDispatchHelper
{
   private static final Logger log = Logger.getLogger(InvokeDispatchHelper.class);

   /**
    * Invoke context with params from ParameterMetaData list.
    *
    * @param configurator the configurator
    * @param target the target
    * @param context the invoke dispatch
    * @param methodName the method name
    * @param params the parameters
    * @return  invocation's result
    * @throws Throwable for any exception
    */
   public static Object invoke(
         KernelConfigurator configurator,
         Object target,
         InvokeDispatchContext context,
         String methodName,
         List<ParameterMetaData> params) throws Throwable
   {
      String[] signature;
      Object[] parameters;
      if (params == null || params.isEmpty())
      {
         signature = new String[0];
         parameters = new Object[0];
      }
      else
      {
         int size = params.size();
         signature = Configurator.getParameterTypes(log.isTraceEnabled(), params);
         // TODO - is this ok for non-POJO targets?
         ClassLoader classLoader = SecurityActions.getClassLoader(context);
         if (target != null)
         {
            MethodInfo methodInfo = Configurator.findMethodInfo(configurator.getClassInfo(target.getClass()), methodName, signature);
            parameters = Configurator.getParameters(log.isTraceEnabled(), classLoader, methodInfo.getParameterTypes(), params);
            // add some more info, if not yet set
            for(int i = 0; i < size; i++)
            {
               if (signature[i] == null)
               {
                  signature[i] = methodInfo.getParameterTypes()[i].getName();
               }
            }
         }
         else
         {
            parameters = new Object[size];
            for (int i = 0; i < size; i++)
            {
               ParameterMetaData pmd = params.get(i);
               TypeInfo typeInfo = null;
               if (signature[i] != null)
               {
                  typeInfo = configurator.getClassInfo(signature[i], classLoader);
               }
               // typeInfo might be null, but we can still get value in some cases
               parameters[i] = pmd.getValue().getValue(typeInfo, classLoader);
            }

         }
      }
      return context.invoke(methodName, parameters, signature);
   }
}
