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
package org.jboss.kernel.plugins.config;

import java.util.List;

import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.joinpoint.spi.MethodJoinpoint;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.AbstractKernelObject;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * Abstract Kernel configurator.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractKernelConfigurator extends AbstractKernelObject implements KernelConfigurator
{
   /** The kernel config */
   protected KernelConfig config; 
   
   /**
    * Create an abstract kernel configurator
    * 
    * @throws Exception for any error
    */
   public AbstractKernelConfigurator() throws Exception
   {
   }

   public BeanInfo getBeanInfo(String className, ClassLoader cl) throws Throwable
   {
      return config.getBeanInfo(className, cl);
   }

   public BeanInfo getBeanInfo(Class<?> clazz) throws Throwable
   {
      return config.getBeanInfo(clazz);
   }

   public BeanInfo getBeanInfo(TypeInfo type) throws Throwable
   {
      return config.getBeanInfo(type);
   }

   public BeanInfo getBeanInfo(String className, ClassLoader cl, BeanAccessMode mode) throws Throwable
   {
      return config.getBeanInfo(className, cl, mode);
   }

   public BeanInfo getBeanInfo(Class<?> clazz, BeanAccessMode mode) throws Throwable
   {
      return config.getBeanInfo(clazz, mode);
   }

   public BeanInfo getBeanInfo(TypeInfo type, BeanAccessMode mode) throws Throwable
   {
      return config.getBeanInfo(type, mode);
   }

   public BeanInfo getBeanInfo(BeanMetaData metaData) throws Throwable
   {
      ClassLoader cl = Configurator.getClassLoader(metaData);
      String className = metaData.getBean();
      if (className == null)
         return null;
      return getBeanInfo(className, cl, metaData.getAccessMode());
   }

   public TypeInfo getTypeInfo(String className, ClassLoader cl) throws Throwable
   {
      return config.getTypeInfo(className, cl);
   }

   public TypeInfo getTypeInfo(Class<?> clazz) throws Throwable
   {
      return config.getTypeInfo(clazz);
   }

   public ClassInfo getClassInfo(String className, ClassLoader cl) throws Throwable
   {
      return config.getClassInfo(className, cl);
   }

   public ClassInfo getClassInfo(Class<?> clazz) throws Throwable
   {
      return config.getClassInfo(clazz);
   }
   
   public Joinpoint getConstructorJoinPoint(BeanInfo info) throws Throwable
   {
      return getConstructorJoinPoint(info, null, null);
   }

   public Joinpoint getConstructorJoinPoint(BeanMetaData metaData) throws Throwable
   {
      BeanInfo info = getBeanInfo(metaData);
      return getConstructorJoinPoint(info, metaData.getConstructor(), metaData);
   }

   public Joinpoint getConstructorJoinPoint(BeanInfo info, ConstructorMetaData metaData, BeanMetaData beanMetaData) throws Throwable
   {
      return Configurator.getConstructorJoinPoint(config, info, metaData, beanMetaData);
   }

   public Joinpoint getConstructorJoinPoint(BeanInfo info, ConstructorMetaData metaData, BeanMetaData beanMetaData, Object object) throws Throwable
   {
      return Configurator.getConstructorJoinPoint(config, info, metaData, beanMetaData, object);
   }

   public MethodJoinpoint getMethodJoinPoint(BeanInfo info, ClassLoader cl, String name, List<ParameterMetaData> parameters, boolean isStatic, boolean isPublic) throws Throwable
   {
      return Configurator.findMethod(info, cl, name, parameters, isStatic, isPublic);
   }
   
   public void setKernel(Kernel kernel) throws Throwable
   {
      super.setKernel(kernel);
      config = kernel.getConfig();
   }
}
