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

import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.joinpoint.plugins.Config;
import org.jboss.joinpoint.spi.ConstructorJoinpoint;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.joinpoint.spi.JoinpointException;
import org.jboss.joinpoint.spi.JoinpointFactory;
import org.jboss.joinpoint.spi.MethodJoinpoint;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.ConstructorInfo;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.reflect.spi.TypeInfoFactory;

/**
 * Configuration utilities.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class Configurator extends Config
{
   /**
    * Instantiate and configure a bean
    *
    * @param config the confg
    * @param info the bean info
    * @param metaData the bean metadata
    * @return the instantiated and configured object
    * @throws Throwable for any error
    */
   public static Object instantiateAndConfigure(KernelConfig config, BeanInfo info, BeanMetaData metaData) throws Throwable
   {
      Object result = instantiate(config, info, metaData);
      if (metaData != null)
         configure(result, info, metaData);
      return result;
   }

   /**
    * Instantiate a bean
    *
    * @param config the kernel config
    * @param info the bean info
    * @param metaData the bean metadata
    * @return the instantiated object
    * @throws Throwable for any error
    */
   public static Object instantiate(KernelConfig config, BeanInfo info, BeanMetaData metaData) throws Throwable
   {
      boolean trace = log.isTraceEnabled();
      if (trace)
         log.trace("Instantiating info=" + info + " metaData=" + metaData);

      ConstructorMetaData constructor = null;
      if (metaData != null)
         constructor = metaData.getConstructor();
      Joinpoint joinPoint = getConstructorJoinPoint(config, info, constructor, metaData);
      return joinPoint.dispatch();
   }

   /**
    * Get a constructor joinpoint
    *
    * @param config the kernel config
    * @param info the bean info
    * @param metaData the constructor metadata
    * @param beanMetaData the bean metadata
    * @return the joinpoint
    * @throws Throwable for any error
    */
   public static Joinpoint getConstructorJoinPoint(KernelConfig config, BeanInfo info, ConstructorMetaData metaData, BeanMetaData beanMetaData)
      throws Throwable
   {
      return getConstructorJoinPoint(config, info, metaData, beanMetaData, null);
   }

   /**
    * Get a constructor joinpoint
    *
    * @param config the kernel config
    * @param info the bean info
    * @param metaData the constructor metadata
    * @param beanMetaData the bean metadata
    * @param object an opaque object
    * @return the joinpoint
    * @throws Throwable for any error
    */
   public static Joinpoint getConstructorJoinPoint(KernelConfig config, BeanInfo info, ConstructorMetaData metaData, BeanMetaData beanMetaData, Object object)
      throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      if (trace)
         log.trace("Get constructor joinpoint info=" + info + " constructor=" + metaData);

      if (config == null)
         throw new IllegalArgumentException("Null config");

      if (metaData != null)
      {
         ClassLoader cl = getClassLoader(beanMetaData);

         ValueMetaData vmd = metaData.getValue();
         if (vmd != null)
         {
            TypeInfo typeInfo = null;
            if (info != null)
               typeInfo = info.getClassInfo();
            return new ValueJoinpoint(vmd, typeInfo, cl);
         }

         vmd = metaData.getFactory();
         if (vmd != null)
         {
            // Get the factory
            Object factory = vmd.getValue(null, cl);

            // Get the parameters
            List<ParameterMetaData> parameters = metaData.getParameters();

            // Describe the factory
            BeanInfo factoryInfo = config.getBeanInfo(factory.getClass());

            // Find the method
            MethodJoinpoint joinPoint = findMethod(trace, factoryInfo, cl, metaData.getFactoryMethod(), parameters, false, true);
            joinPoint.setTarget(factory);
            MethodInfo minfo = joinPoint.getMethodInfo();

            // Set the parameters
            if (minfo != null)
            {
               TypeInfo[] pinfos = minfo.getParameterTypes();
               Object[] params = getParameters(trace, cl, pinfos, parameters);
               joinPoint.setArguments(params);
            }
            return joinPoint;
         }

         String factoryClassName = metaData.getFactoryClass();
         if (factoryClassName != null)
         {
            // Get the parameters
            List<ParameterMetaData> parameters = metaData.getParameters();

            BeanInfo factoryInfo = config.getBeanInfo(factoryClassName, cl);

            // Find the method
            MethodJoinpoint joinPoint = findMethod(trace, factoryInfo, cl, metaData.getFactoryMethod(), parameters, true, true);
            MethodInfo minfo = joinPoint.getMethodInfo();

            // Set the parameters
            if (minfo != null)
            {
               TypeInfo[] pinfos = minfo.getParameterTypes();
               Object[] params = getParameters(trace, cl, pinfos, parameters);
               joinPoint.setArguments(params);
            }
            return joinPoint;
         }

         // Find the constructor
         ConstructorJoinpoint joinPoint = findConstructor(trace, info, metaData, object);
         ConstructorInfo cinfo = joinPoint.getConstructorInfo();

         // Set the parameters
         if (cinfo != null)
         {
            TypeInfo[] pinfos = cinfo.getParameterTypes();
            Object[] params = getParameters(trace, cl, pinfos, metaData.getParameters());
            joinPoint.setArguments(params);
         }
         return joinPoint;
      }

      // Default constructor
      return findConstructor(trace, info, metaData, object);
   }

   /**
    * Find a constructor
    *
    * @param trace whether trace is enabled
    * @param info the bean info
    * @param metaData the bean metadata
    * @return the constructor join point
    * @throws Exception for any error
    */
   public static ConstructorJoinpoint findConstructor(boolean trace, BeanInfo info, BeanMetaData metaData) throws Exception
   {
      return findConstructor(trace, info, metaData.getConstructor());
   }

   /**
    * Find a constructor
    *
    * @param trace whether trace is enabled
    * @param info the bean info
    * @param metaData the constructor metadata
    * @return the constructor join point
    * @throws Exception for any error
    */
   public static ConstructorJoinpoint findConstructor(boolean trace, BeanInfo info, ConstructorMetaData metaData) throws Exception
   {
      return findConstructor(trace, info, metaData, null);
   }

   /**
    * Find a constructor
    *
    * @param trace whether trace is enabled
    * @param info the bean info
    * @param metaData the constructor metadata
    * @param object an opaque object
    * @return the constructor join point
    * @throws Exception for any error
    */
   public static ConstructorJoinpoint findConstructor(boolean trace, BeanInfo info, ConstructorMetaData metaData, Object object) throws Exception
   {
      ConstructorInfo cinfo = resolveConstructor(trace, info, metaData);
      JoinpointFactory jpf = info.getJoinpointFactory();
      if (object == null)
         return jpf.getConstructorJoinpoint(cinfo);
      else
         return jpf.getConstructorJoinpoint(cinfo, object);
   }

   /**
    * Resolve a constructor
    *
    * @param trace whether trace is enabled
    * @param info the bean info
    * @param metaData the constructor metadata
    * @return the constructor info
    */
   public static ConstructorInfo resolveConstructor(boolean trace, BeanInfo info, ConstructorMetaData metaData)
   {
      if (info == null)
         throw new IllegalArgumentException("Null bean info");

      List<ParameterMetaData> params = Collections.emptyList();
      if (metaData != null && metaData.getParameters() != null)
         params = metaData.getParameters();
      String[] paramTypes = new String[params.size()];
      if (params.isEmpty() == false)
      {
         int x = 0;
         for (Iterator<ParameterMetaData> i = params.iterator(); i.hasNext();)
         {
            ParameterMetaData pdata = i.next();
            paramTypes[x++] = pdata.getType();
         }
      }
      return findConstructorInfo(info.getClassInfo(), paramTypes);
   }

   /**
    * Configure a bean
    *
    * @param object the object to configure
    * @param info the bean info
    * @param metaData the bean metadata
    * @throws Throwable for any error
    */
   public static void configure(Object object, BeanInfo info, BeanMetaData metaData) throws Throwable
   {
      boolean trace = log.isTraceEnabled();

      if (object == null)
         throw new IllegalArgumentException("Null object");
      if (info == null)
         throw new IllegalArgumentException("Null bean info");
      if (metaData == null)
         throw new IllegalArgumentException("Null bean metadata");

      Set<PropertyMetaData> properties = metaData.getProperties();
      if (properties != null && properties.isEmpty() == false)
      {
         ClassLoader cl = getClassLoader(metaData);

         for (Iterator<PropertyMetaData> i = metaData.getProperties().iterator(); i.hasNext();)
         {
            PropertyMetaData property = i.next();
            configure(trace, object, info, cl, property);
         }
      }
   }

   /**
    * Configure a bean property
    *
    * @param object the object to configure
    * @param info the bean info
    * @param cl the classloader
    * @param metaData the property metadata
    * @throws Throwable for any error
    */
   public static void configure(Object object, BeanInfo info, ClassLoader cl, PropertyMetaData metaData) throws Throwable
   {
      boolean trace = log.isTraceEnabled();
      configure(trace, object, info, cl, metaData);
   }

   /**
    * Configure a bean property
    *
    * @param trace whether trace is enabled
    * @param object the object to configure
    * @param info the bean info
    * @param cl the classloader
    * @param metaData the property metadata
    * @throws Throwable for any error
    */
   public static void configure(boolean trace, Object object, BeanInfo info, ClassLoader cl, PropertyMetaData metaData) throws Throwable
   {
      PropertyInfo ainfo = resolveProperty(trace, info, cl, metaData.getName(), metaData.getType());
      configure(trace, object, ainfo, cl, metaData);
   }

   /**
    * Configure a bean property
    *
    * @param object the object to configure
    * @param info the property info
    * @param cl the classloader
    * @param metaData the property metadata
    * @throws Throwable for any error
    */
   public static void configure(Object object, PropertyInfo info, ClassLoader cl, PropertyMetaData metaData) throws Throwable
   {
      boolean trace = log.isTraceEnabled();
      configure(trace, object, info, cl, metaData);
   }

   /**
    * Configure a bean property
    *
    * @param trace whether trace is enabled
    * @param object the object to configure
    * @param info the property info
    * @param cl the classloader
    * @param metaData the property metadata
    * @throws Throwable for any error
    */
   public static void configure(boolean trace, Object object, PropertyInfo info, ClassLoader cl, PropertyMetaData metaData) throws Throwable
   {
      if (trace)
         log.trace("Configuring info=" + info + " metaData=" + metaData);

      if (trace)
         log.trace("Setting property " + info);
      info.set(object, metaData.getValue().getValue(info.getType(), cl));
   }

   /**
    * Unconfigure a bean
    *
    * @param object the object to unconfigure
    * @param info the bean info
    * @param metaData the bean metadata
    * @throws Throwable for any error
    */
   public static void unconfigure(Object object, BeanInfo info, BeanMetaData metaData) throws Throwable
   {
      if (object == null)
         throw new IllegalArgumentException("Null object");
      if (info == null)
         throw new IllegalArgumentException("Null bean info");
      if (metaData == null)
         throw new IllegalArgumentException("Null bean metadata");

      ClassLoader cl = getClassLoader(metaData);
      Set<PropertyMetaData> propertys = metaData.getProperties();
      if (propertys != null && propertys.isEmpty() == false)
      {
         for (Iterator<PropertyMetaData> i = metaData.getProperties().iterator(); i.hasNext();)
         {
            PropertyMetaData property = i.next();
            unconfigure(object, cl, info, property);
         }
      }
   }

   /**
    * Unconfigure a bean property
    *
    * @param object the object to unconfigure
    * @param cl the classloader
    * @param info the bean info
    * @param metaData the property metadata
    * @throws Throwable for any error
    */
   public static void unconfigure(Object object, ClassLoader cl, BeanInfo info, PropertyMetaData metaData) throws Throwable
   {
      boolean trace = log.isTraceEnabled();
      PropertyInfo ainfo = resolveProperty(trace, info, cl, metaData.getName(), metaData.getType());
      unconfigure(trace, object, ainfo, metaData);
   }

   /**
    * UnConfigure a bean property
    *
    * @param trace whether trace is enabled
    * @param object the object to configure
    * @param info the property info
    * @param metaData the property metadata
    * @throws Throwable for any error
    */
   public static void unconfigure(boolean trace, Object object, PropertyInfo info, PropertyMetaData metaData) throws Throwable
   {
      if (trace)
         log.trace("Unconfiguring info=" + info + " metaData=" + metaData);

      if (trace)
         log.trace("Unsetting property " + info);
      info.set(object, null);
   }

   /**
    * Get the property info
    *
    * @param trace whether trace is enabled
    * @param info the bean info
    * @param name the property name
    * @return the property info
    * @throws Throwable for any error
    */
   public static PropertyInfo resolveProperty(boolean trace, BeanInfo info, String name) throws Throwable
   {
      return resolveProperty(trace, info, null, name, null);
   }

   /**
    * Get the property info
    *
    * @param trace whether trace is enabled
    * @param info the bean info
    * @param cl the classloader
    * @param name the property name
    * @param type the property type
    * @return the property info
    * @throws Throwable for any error
    */
   @SuppressWarnings("deprecation")
   public static PropertyInfo resolveProperty(boolean trace, BeanInfo info, ClassLoader cl, String name, String type) throws Throwable
   {
      if (info == null)
         throw new IllegalArgumentException("Null bean info");
      if (name == null)
         throw new IllegalArgumentException("Null name");

      if (trace)
         log.trace("Resolving property on bean info=" + info + " name=" + name);

      if (cl == null)
         cl = info.getClassInfo().getType().getClassLoader();

      PropertyInfo ainfo = info.getProperty(name);
      ClassInfo classInfo = info.getClassInfo();
      TypeInfoFactory tif = classInfo.getTypeInfoFactory();
      if (tif == null)
         throw new IllegalArgumentException("TypeInfoFactory is null: " + classInfo);

      // check for possible progression
      String[] typeNames = {type};
      TypeInfo[] typeInfos = {ainfo.getType()};
      if (equals(typeNames, typeInfos) || isAssignable(tif, cl, typeNames, typeInfos))
      {
         return ainfo;
      }

      throw new JoinpointException("Property " + name + " not found for " + info);
   }

   /**
    * Find a method
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
   public static MethodJoinpoint findMethod(BeanInfo info, ClassLoader cl, String name, List<ParameterMetaData> parameters, boolean isStatic, boolean isPublic) throws Throwable
   {
      boolean trace = log.isTraceEnabled();
      return findMethod(trace, info, cl, name, parameters, isStatic, isPublic);
   }

   /**
    * Find a method
    *
    * @param trace whether trace is enabled
    * @param info the bean info
    * @param cl the classloader
    * @param name the method name
    * @param parameters the parameter metadata
    * @param isStatic whether the method is static
    * @param isPublic whether the method is public
    * @return the method join point
    * @throws Throwable for any error
    */
   public static MethodJoinpoint findMethod(boolean trace, BeanInfo info, ClassLoader cl, String name, List<ParameterMetaData> parameters, boolean isStatic, boolean isPublic) throws Throwable
   {
      if (info == null)
         throw new IllegalArgumentException("Null bean info");
      if (name == null)
         throw new IllegalArgumentException("Null name");

      String[] paramTypes = getParameterTypes(trace, parameters);
      MethodInfo minfo = findMethodInfo(info.getClassInfo(), name, paramTypes, isStatic, isPublic);
      JoinpointFactory jpf = info.getJoinpointFactory();
      MethodJoinpoint joinPoint = jpf.getMethodJoinpoint(minfo);

      // Set the parameters
      if (minfo != null)
      {
         TypeInfo[] pinfos = minfo.getParameterTypes();
         Object[] params = getParameters(trace, cl, pinfos, parameters);
         joinPoint.setArguments(params);
      }

      return joinPoint;
   }

   /**
    * Get the parameters types
    *
    * @param trace whether trace is enabled
    * @param parameters the parameter metadata
    * @return an array of parameter types
    * @throws Throwable for any error
    */
   public static String[] getParameterTypes(boolean trace, List<ParameterMetaData> parameters) throws Throwable
   {
      if (parameters == null)
         return null;

      String[] paramTypes = new String[parameters.size()];
      int x = 0;
      for (Iterator<ParameterMetaData> i = parameters.iterator(); i.hasNext();)
      {
         ParameterMetaData pmd = i.next();
         paramTypes[x++] = pmd.getType();
      }
      return paramTypes;
   }

   /**
    * Get the parameters types
    *
    * @param trace whether trace is enabled
    * @param parameters the parameter types
    * @return an array of parameter types
    * @throws Throwable for any error
    */
   public static String[] getParameterTypes(boolean trace, TypeInfo[] parameters) throws Throwable
   {
      if (parameters == null)
         return null;

      String[] paramTypes = new String[parameters.length];
      int x = 0;
      for (int i = 0; i < parameters.length; ++i)
         paramTypes[x++] = parameters[i].getName();
      return paramTypes;
   }

   /**
    * Get the parameters
    *
    * @param trace whether trace is enabled
    * @param cl the classloader
    * @param pinfos the parameter infos
    * @param parameters the parameter metadata
    * @return an array of parameters
    * @throws Throwable for any error
    */
   public static Object[] getParameters(boolean trace, ClassLoader cl, TypeInfo[] pinfos, List<ParameterMetaData> parameters) throws Throwable
   {
      if (parameters == null)
         return null;

      Object[] params = new Object[parameters.size()];
      int x = 0;
      for (Iterator<ParameterMetaData> i = parameters.iterator(); i.hasNext();)
      {
         ParameterMetaData pdata = i.next();
         ValueMetaData vmd = pdata.getValue();
         params[x] = vmd.getValue(pinfos[x], cl);
         x++;
      }
      return params;
   }

   /**
    * Get the classloader for some BeanMetaData
    *
    * @param metaData the metaData
    * @return the classloader
    * @throws Throwable for any error
    */
   public static ClassLoader getClassLoader(BeanMetaData metaData) throws Throwable
   {
      ClassLoaderMetaData clmd = null;
      if (metaData != null)
         clmd = metaData.getClassLoader();
      return getClassLoader(clmd);
   }

   /**
    * Get the classloader for some ClassLoaderMetaData
    *
    * @param metaData the metaData
    * @return the classloader
    * @throws Throwable for any error
    */
   public static ClassLoader getClassLoader(ClassLoaderMetaData metaData) throws Throwable
   {
      ClassLoader tcl = Thread.currentThread().getContextClassLoader();
      ClassLoader cl = null;
      if (metaData != null)
      {
            ValueMetaData clVMD = metaData.getClassLoader();
            if (clVMD != null)
            {
               Object object = clVMD.getValue(null, tcl);
               if (object != null && object instanceof ClassLoader == false)
                  throw new IllegalArgumentException("Configured object is not a classloader " + metaData);
               cl = (ClassLoader) object;
            }
      }
      if (cl == null)
         cl = tcl;
      return cl;
   }

   /**
    * Test whether type names can be assigned to type infos
    *
    * @param tif the type info factory
    * @param cl bean classloader
    * @param typeNames the type names
    * @param typeInfos the type infos
    * @return true when they can be assigned
    * @throws Throwable for any error
    */
   @SuppressWarnings("unchecked")
   public static boolean isAssignable(TypeInfoFactory tif, ClassLoader cl, String[] typeNames, TypeInfo[] typeInfos) throws Throwable
   {
      if (cl == null)
         return false;

      if (simpleCheck(typeNames, typeInfos) == false)
         return false;

      for (int i = 0; i < typeNames.length; ++i)
      {
         if (typeNames[i] != null)
         {
            if (typeInfos[i].isAssignableFrom(tif.getTypeInfo(typeNames[i], cl)) == false)
            {
               return false;
            }
         }
      }
      return true;
   }

   /**
    * ValueJoinpoint.
    */
   private static class ValueJoinpoint implements Joinpoint
   {
      /** The value metadata */
      private ValueMetaData vmd;

      /** The type info */
      private TypeInfo info;

      /** The classloader */
      private ClassLoader cl;

      /**
       * Create a new ValueJoinpoint.
       * 
       * @param vmd the value metadata
       * @param info the type info
       * @param cl the classloader
       */
      public ValueJoinpoint(ValueMetaData vmd, TypeInfo info, ClassLoader cl)
      {
         this.vmd = vmd;
         this.info = info;
         this.cl = cl;
      }

      public Object dispatch() throws Throwable
      {
         return vmd.getValue(info, cl);
      }

      public String toHumanReadableString()
      {
         return vmd.toShortString();
      }

      public Object clone()
      {
         try
         {
            return super.clone();
         }
         catch (CloneNotSupportedException e)
         {
            throw new Error(e);
         }
      }

   }
}
