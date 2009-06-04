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
package org.jboss.kernel.spi.config;

import java.util.List;

import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.joinpoint.spi.MethodJoinpoint;
import org.jboss.kernel.spi.KernelObject;
import org.jboss.metadata.spi.MetaData;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * A configurator.<p>
 * 
 * The configurator is a utility class used by the controller
 * to create and configure beans. It will typically wrap a
 * {@link KernelConfig}.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface KernelConfigurator extends KernelObject
{
   /**
    * Get the BeanInfo for a class.
    * 
    * @param className the class name
    * @param cl the classloader
    * @return the bean info
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(String className, ClassLoader cl) throws Throwable;
   
   /**
    * Get the BeanInfo for a class.
    * 
    * @param clazz the class
    * @return the bean info
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(Class<?> clazz) throws Throwable;
   
   /**
    * Get the BeanInfo from a type info.
    * 
    * @param type the type info
    * @return the bean info
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(TypeInfo type) throws Throwable;
   
   /**
    * Get the BeanInfo for a class using the specified bean access mode.
    *
    * @param className the class name
    * @param cl the classloader
    * @param mode the access mode
    * @return the bean info
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(String className, ClassLoader cl, BeanAccessMode mode) throws Throwable;

   /**
    * Get the BeanInfo for a class using the specified bean access mode.
    *
    * @param clazz the class
    * @return the bean info
    * @param mode the access mode
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(Class<?> clazz, BeanAccessMode mode) throws Throwable;

   /**
    * Get the BeanInfo from a typ info using the specified bean access mode.
    *
    * @param type the type info
    * @param mode the access mode
    * @return the bean info
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(TypeInfo type, BeanAccessMode mode) throws Throwable;

   /**
    * Get the BeanInfo for some metadata. The BeanInfo will be for the bean metadata's
    * specified class.
    * 
    * @param metaData the metadata
    * @return the bean info
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(BeanMetaData metaData) throws Throwable;
   
   /**
    * Get the type info for a class.
    * 
    * @param className the class name
    * @param cl the classloader
    * @return the type info
    * @throws Throwable for any error
    */
   TypeInfo getTypeInfo(String className, ClassLoader cl) throws Throwable;
   
   /**
    * Get the type info for a class.
    * 
    * @param clazz the class
    * @return the type info
    * @throws Throwable for any error
    */
   TypeInfo getTypeInfo(Class<?> clazz) throws Throwable;
   
   /**
    * Get the class info for a class.
    * 
    * @param className the class name
    * @param cl the classloader
    * @return the class info
    * @throws Throwable for any error
    */
   ClassInfo getClassInfo(String className, ClassLoader cl) throws Throwable;
   
   /**
    * Get the class info for a class.
    * 
    * @param clazz the class
    * @return the class info
    * @throws Throwable for any error
    */
   ClassInfo getClassInfo(Class<?> clazz) throws Throwable;
   
   /**
    * Get a constructor join point for a bean info. This will be the default constructor.
    * 
    * @param info the bean info
    * @return the join point
    * @throws Throwable for any error
    */
   Joinpoint getConstructorJoinPoint(BeanInfo info) throws Throwable;
   
   /**
    * Get a constructor join point for a bean metadata. This will be the constructor 
    * that is the closest match to the constructor specified in the bean metadata.
    * 
    * @param metaData the bean metadata
    * @return the join point
    * @throws Throwable for any error
    */
   Joinpoint getConstructorJoinPoint(BeanMetaData metaData) throws Throwable;
   
   /**
    * Get a constructor join point for a bean metadata. This will be the constructor 
    * that is the closest match to the constructor specified by the passed in constructor
    * metadata.
    * 
    * @param info the bean info
    * @param metaData the constructor metadata
    * @param beanMetaData the bean metadata
    * @return the join point
    * @throws Throwable for any error
    */
   Joinpoint getConstructorJoinPoint(BeanInfo info, ConstructorMetaData metaData, BeanMetaData beanMetaData) throws Throwable;
   
   /**
    * Get a constructor join point a bean info. This will be the constructor 
    * that is the closest match to the constructor specified by the passed in constructor
    * metadata. 
    * 
    * @param info the bean info
    * @param metaData the constructor metadata
    * @param beanMetaData the bean metadata
    * @param object an opaque object. If used with AOP this should be the {@link MetaData} for the bean.
    * @return the join point
    * @throws Throwable for any error
    */
   Joinpoint getConstructorJoinPoint(BeanInfo info, ConstructorMetaData metaData, BeanMetaData beanMetaData, Object object) throws Throwable;

   /**
    * Get a method joinpoint for a bean.
    * 
    * @param info the bean info
    * @param cl the classloader
    * @param name the method name
    * @param parameters the parameter metadata
    * @param isStatic whether the method is static
    * @param isPublic whether the method is public
    * @return the method join point
    * @throws Throwable for any error
    */
   MethodJoinpoint getMethodJoinPoint(BeanInfo info, ClassLoader cl, String name, List<ParameterMetaData> parameters, boolean isStatic, boolean isPublic) throws Throwable;
}
