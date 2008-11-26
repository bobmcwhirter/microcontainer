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
package org.jboss.kernel.plugins.dependency;

import java.security.PrivilegedExceptionAction;
import java.util.Set;

import org.jboss.beans.info.plugins.BeanInfoUtil;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.logging.Logger;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.reflect.spi.PrimitiveInfo;

/**
 * PropertyDispatchWrapper.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
class PropertyDispatchWrapper extends ExecutionWrapper
{
   private static Logger log = Logger.getLogger(ConfigureAction.class);

   private KernelControllerContext context;
   private PropertyMetaData property;
   private boolean nullify;
   private BeanInfo beanInfo;
   private Object target;
   private ClassLoader cl;

   public PropertyDispatchWrapper(KernelControllerContext context, PropertyMetaData property, boolean nullify, BeanInfo beanInfo, Object target, ClassLoader cl)
   {
      if (context == null)
         throw new IllegalArgumentException("Null context");
      this.context = context;
      if (property == null)
         throw new IllegalArgumentException("Null property");
      this.property = property;
      this.nullify = nullify;
      if (beanInfo == null)
         throw new IllegalArgumentException("Null bean info");
      this.beanInfo = beanInfo;
      if (target == null)
         throw new IllegalArgumentException("Null target");
      this.target = target;
      this.cl = cl;
   }

   public Object execute() throws Throwable
   {
      String name = property.getName();
      PropertyInfo propertyInfo = BeanInfoUtil.getPropertyInfo(beanInfo, target, name);
      TypeInfo propertyTypeInfo = propertyInfo.getType();
      TypeInfo typeInfo = propertyTypeInfo;
      if (typeInfo == null)
         typeInfo = getTypeInfo();

      if (nullify)
      {
         if (typeInfo != null)
         {
            if (typeInfo.isPrimitive() == false)
            {
               try
               {
                  if (propertyTypeInfo != null)
                     beanInfo.setProperty(target, name, null);
                  else // e.g. NestedPropertyInfo
                     nullifyByMatchingType(propertyInfo.getUpperName(), typeInfo);
               }
               catch (Throwable t)
               {
                  if (log.isTraceEnabled())
                     log.trace("Ignored for " + target + "." + name, t);
               }
            }
         }
         else
         {
            log.warn("Cannot properly nullify property: " + property + ", beanInfo: " + beanInfo);
         }
      }
      else
      {
         ValueMetaData valueMetaData = property.getValue();
         Object value = valueMetaData.getValue(typeInfo, cl);
         validatePropertyValue(context, target, propertyInfo, value);
         beanInfo.setProperty(target, name, value);
      }
      return null;
   }

   /**
    * Try to get type info from property's class.
    *
    * @return type info
    * @throws Throwable for any error
    */
   protected TypeInfo getTypeInfo() throws Throwable
   {
      String className = property.getType();
      if (className == null)
         return null;

      Kernel kernel = context.getKernel();
      KernelConfigurator configurator = kernel.getConfigurator();
      return configurator.getTypeInfo(className, cl);
   }

   /**
    * Nullify by matching type on .
    *
    * @param name the property name
    * @param typeInfo the type info
    * @throws Throwable for any error
    */
   protected void nullifyByMatchingType(String name, TypeInfo typeInfo) throws Throwable
   {
      // TODO - remove this Character.toUpperCase once Reflect is updated
      String upperName = "set" + Character.toUpperCase(name.charAt(0));
      if (name.length() > 1)
         upperName += name.substring(1);

      Set<MethodInfo> methods = beanInfo.getMethods();
      for (MethodInfo minfo : methods)
      {
         String miName = minfo.getName();
         if (upperName.equals(miName))
         {
            TypeInfo returnType = minfo.getReturnType();
            TypeInfo[] parameters = minfo.getParameterTypes();
            if (parameters.length == 1 && PrimitiveInfo.VOID.equals(returnType) && typeInfo.equals(parameters[0]))
            {
               minfo.invoke(target, new Object[]{null});
               return;
            }
         }
      }
   }

   /**
    * Validate value injection.
    * Use jsr303 constraints.
    *
    * @param context the owner context
    * @param target the target
    * @param pi the property info
    * @param value the new value
    * @throws Throwable for any error
    */
   protected void validatePropertyValue(KernelControllerContext context, Object target, PropertyInfo pi, Object value) throws Throwable
   {
      BeanValidatorBridge bridge = KernelControllerContextAction.getBeanValidatorBridge(context);
      if (bridge != null)
         bridge.validatePropertyValue(context, target, pi, value);
   }

   protected PrivilegedExceptionAction<Object> getAction()
   {
      return new PrivilegedExceptionAction<Object>()
      {
         public Object run() throws Exception
         {
            try
            {
               return execute();
            }
            catch (RuntimeException e)
            {
               throw e;
            }
            catch (Exception e)
            {
               throw e;
            }
            catch (Error e)
            {
               throw e;
            }
            catch (Throwable t)
            {
               throw new RuntimeException(t);
            }
         }
      };
   }
}
