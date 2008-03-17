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
import java.util.Set;

import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.joinpoint.spi.MethodJoinpoint;
import org.jboss.joinpoint.spi.TargettedJoinpoint;
import org.jboss.kernel.spi.KernelObject;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * A configurator.<p>
 * 
 * The configurator is a utility class used by the controller
 * to create and configure beans. 
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface KernelConfigurator extends KernelObject
{
   /**
    * Get the BeanInfo
    * 
    * @param className the class name
    * @param cl the classloader
    * @return the bean info
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(String className, ClassLoader cl) throws Throwable;
   
   /**
    * Get the BeanInfo
    * 
    * @param clazz the class
    * @return the bean info
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(Class<?> clazz) throws Throwable;
   
   /**
    * Get the BeanInfo
    * 
    * @param type the type info
    * @return the bean info
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(TypeInfo type) throws Throwable;
   
   /**
    * Get the BeanInfo
    *
    * @param className the class name
    * @param cl the classloader
    * @param mode the access mode
    * @return the bean info
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(String className, ClassLoader cl, BeanAccessMode mode) throws Throwable;

   /**
    * Get the BeanInfo
    *
    * @param clazz the class
    * @return the bean info
    * @param mode the access mode
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(Class<?> clazz, BeanAccessMode mode) throws Throwable;

   /**
    * Get the BeanInfo
    *
    * @param type the type info
    * @param mode the access mode
    * @return the bean info
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(TypeInfo type, BeanAccessMode mode) throws Throwable;

   /**
    * Get the BeanInfo for some metadata
    * 
    * @param metaData the metadata
    * @return the bean info
    * @throws Throwable for any error
    */
   BeanInfo getBeanInfo(BeanMetaData metaData) throws Throwable;
   
   /**
    * Get the type info for a class
    * 
    * @param className the class name
    * @param cl the classloader
    * @return the type info
    * @throws Throwable for any error
    */
   TypeInfo getTypeInfo(String className, ClassLoader cl) throws Throwable;
   
   /**
    * Get the type info for a class
    * 
    * @param clazz the class
    * @return the type info
    * @throws Throwable for any error
    */
   TypeInfo getTypeInfo(Class<?> clazz) throws Throwable;
   
   /**
    * Get the class info for a class
    * 
    * @param className the class name
    * @param cl the classloader
    * @return the class info
    * @throws Throwable for any error
    */
   ClassInfo getClassInfo(String className, ClassLoader cl) throws Throwable;
   
   /**
    * Get the class info for a class
    * 
    * @param clazz the class
    * @return the class info
    * @throws Throwable for any error
    */
   ClassInfo getClassInfo(Class<?> clazz) throws Throwable;
   
   /**
    * Get a constructor join point
    * 
    * @param info the bean info
    * @return the join point
    * @throws Throwable for any error
    */
   Joinpoint getConstructorJoinPoint(BeanInfo info) throws Throwable;
   
   /**
    * Get a constructor join point
    * 
    * @param metaData the bean metadata
    * @return the join point
    * @throws Throwable for any error
    */
   Joinpoint getConstructorJoinPoint(BeanMetaData metaData) throws Throwable;
   
   /**
    * Get a constructor join point
    * 
    * @param info the bean info
    * @param metaData the constructor metadata
    * @param beanMetaData
    * @return the join point
    * @throws Throwable for any error
    */
   Joinpoint getConstructorJoinPoint(BeanInfo info, ConstructorMetaData metaData, BeanMetaData beanMetaData) throws Throwable;

   /**
    * Get property getter join point
    * 
    * @param info the bean info
    * @param property the property name
    * @return the join point
    * @throws Throwable for any error
    */
   TargettedJoinpoint getPropertyGetterJoinPoint(BeanInfo info, String property) throws Throwable;

   /**
    * Get property setter join points
    * 
    * @param info the bean info
    * @param metaData the bean metadata
    * @return the join points
    * @throws Throwable for any error
    */
   Set<TargettedJoinpoint> getPropertySetterJoinPoints(BeanInfo info, BeanMetaData metaData) throws Throwable;

   /**
    * Get property setter join point
    * 
    * @param info the bean info
    * @param cl the classloader
    * @param metaData the property metadata
    * @return the join point
    * @throws Throwable for any error
    */
   TargettedJoinpoint getPropertySetterJoinPoint(BeanInfo info, ClassLoader cl, PropertyMetaData metaData) throws Throwable;

   /**
    * Get property setter join point
    * 
    * @param info the bean info
    * @param property the property name
    * @param cl the classloader
    * @param vmd the value metadata
    * @return the join point
    * @throws Throwable for any error
    */
   TargettedJoinpoint getPropertySetterJoinPoint(BeanInfo info, String property, ClassLoader cl, ValueMetaData vmd) throws Throwable;
   
   /**
    * Get property setter join point
    * 
    * @param info the property info
    * @param cl the classloader
    * @param metaData the property metadata
    * @return the join point
    * @throws Throwable for any error
    */
   TargettedJoinpoint getPropertySetterJoinPoint(PropertyInfo info, ClassLoader cl, PropertyMetaData metaData) throws Throwable;

   /**
    * Get property nuller join points
    * 
    * @param info the bean info
    * @param metaData the bean metadata
    * @return the join points
    * @throws Throwable for any error
    */
   Set<TargettedJoinpoint> getPropertyNullerJoinPoints(BeanInfo info, BeanMetaData metaData) throws Throwable;

   /**
    * Get property nuller join point
    * 
    * @param info the bean info
    * @param metaData the property metadata
    * @return the join point
    * @throws Throwable for any error
    * @deprecated must use ClassLoader when determining PropertyInfo
    */
   TargettedJoinpoint getPropertyNullerJoinPoint(BeanInfo info, PropertyMetaData metaData) throws Throwable;

   /**
    * Get property nuller join point
    * 
    * @param info the property info
    * @param metaData the property metadata
    * @return the join point
    * @throws Throwable for any error
    */
   TargettedJoinpoint getPropertyNullerJoinPoint(PropertyInfo info, PropertyMetaData metaData) throws Throwable;
   
   /**
    * Get a method joinpoint
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
