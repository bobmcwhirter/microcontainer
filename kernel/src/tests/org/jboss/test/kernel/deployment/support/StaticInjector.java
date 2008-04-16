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
package org.jboss.test.kernel.deployment.support;

import java.lang.reflect.Method;

import org.jboss.beans.metadata.api.annotations.Constructor;
import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.joinpoint.plugins.Config;
import org.jboss.kernel.plugins.bootstrap.basic.KernelConstants;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.reflect.plugins.introspection.ReflectMethodInfoImpl;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.FieldInfo;
import org.jboss.reflect.spi.MethodInfo;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class StaticInjector
{
   private KernelConfigurator configurator;

   @Constructor
   public StaticInjector(@Inject(bean = KernelConstants.KERNEL_CONFIGURATOR_NAME) KernelConfigurator configurator)
   {
      if (configurator == null)
         throw new IllegalArgumentException("Null configurator");

      this.configurator = configurator;
   }

   public void injectToMethod(Class<?> clazz, String method, Object value, Class<?> signature) throws Throwable
   {
      injectToMethod(clazz, method, value, signature, true);
   }

   public void injectToNonPublicMethod(Class<?> clazz, String method, Object value, Class<?> signature) throws Throwable
   {
      injectToMethod(clazz, method, value, signature, false);
   }

   private void injectToMethod(Class<?> clazz, String method, Object value, Class<?> signature, boolean isPublic) throws Throwable
   {
      ClassInfo classInfo = configurator.getClassInfo(clazz);
      MethodInfo mi = Config.findMethodInfo(classInfo, method, new String[]{signature.getName()}, true, isPublic);
      if (isPublic == false)
      {
         // TODO - move this into Reflection?
         if (mi instanceof ReflectMethodInfoImpl)
         {
            ReflectMethodInfoImpl rmi = (ReflectMethodInfoImpl)mi;
            Method  m = rmi.getMethod();
            m.setAccessible(true);
         }
         else
            throw new IllegalArgumentException("Cannot set accessible on method info: " + mi);
      }
      mi.invoke(null, new Object[]{value});
   }

   public void injectToField(Class<?> clazz, String name, Object value) throws Throwable
   {
      ClassInfo classInfo = configurator.getClassInfo(clazz);
      FieldInfo fi = Config.findFieldInfo(classInfo, name);
      fi.set(null, value);
   }
}