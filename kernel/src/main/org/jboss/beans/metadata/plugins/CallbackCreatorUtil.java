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
package org.jboss.beans.metadata.plugins;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;

import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.dependency.spi.CallbackItem;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.plugins.dependency.AttributeInfo;
import org.jboss.kernel.plugins.dependency.BasicCollectionCallbackItemFactory;
import org.jboss.kernel.plugins.dependency.ClassAttributeCallbackItem;
import org.jboss.kernel.plugins.dependency.ClassSingleCallbackItem;
import org.jboss.kernel.plugins.dependency.CollectionCallbackItem;
import org.jboss.kernel.plugins.dependency.CollectionCallbackItemFactory;
import org.jboss.kernel.plugins.dependency.MethodAttributeInfo;
import org.jboss.kernel.plugins.dependency.PropertyAttributeInfo;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.logging.Logger;
import org.jboss.reflect.plugins.introspection.ReflectionUtils;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * CallbackItem creator util.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class CallbackCreatorUtil
{
   protected static Logger log;
   protected static CollectionCallbackItemFactory factory;

   // layz log
   protected static Logger getLog()
   {
      if (log == null)
         log = Logger.getLogger(CallbackCreatorUtil.class);
      return log;
   }

   /**
    * Create collection callback item.
    *
    * @param info type info
    * @param context owner context
    * @param whenRequired when required state
    * @param attribute attribute
    * @param dependentState dependent state
    * @param cardinality cardinality
    * @return collection callback item
    */
   @SuppressWarnings("unchecked")
   private static CallbackItem<Class<?>> createCollectionCallback(
         TypeInfo info,
         KernelControllerContext context,
         AttributeInfo attribute,
         ControllerState whenRequired,
         ControllerState dependentState,
         Cardinality cardinality)
   {
      if (info instanceof ClassInfo)
      {
         ClassInfo ci = (ClassInfo)info;
         TypeInfo componentType = ci.getComponentType();
         if (componentType == null)
            throw new IllegalArgumentException("Null component type: " + info);
         Class<?> clazz = componentType.getType();
         if (Object.class.equals(clazz))
            throw new IllegalArgumentException("Component type too general - equals Object: " + info);
         Class<? extends Collection<Object>> collectionType = (Class) info.getType();
         CollectionCallbackItemFactory factory = getCollectionFactory();
         CollectionCallbackItem collectionCallback = factory.createCollectionCallbackItem(collectionType, clazz, whenRequired, dependentState, cardinality, context, attribute);
         return collectionCallback;
      }
      else
         throw new IllegalArgumentException("Unable to determine collection element class type: " + info);
   }

   /**
    * Create callback item from AttrbuteInfo.
    *
    * @param context owner context
    * @param ai attribute info
    * @param whenRequired when required state
    * @param dependentState dependent state
    * @param cardinality cardinality
    * @return callback item
    */
   @SuppressWarnings("unchecked")
   public static CallbackItem<Class<?>> createCallback(
         KernelControllerContext context,
         AttributeInfo ai,
         ControllerState whenRequired,
         ControllerState dependentState,
         Cardinality cardinality)
   {
      if (ai.isValid() == false)
         throw new IllegalArgumentException("Not a valid attribute info: " + ai);

      TypeInfo info = ai.getType();
      if (info.isCollection())
         return createCollectionCallback(info, context, ai, whenRequired, dependentState, cardinality);
      else if (ai.isProperty())
         return new ClassAttributeCallbackItem(info.getType(), whenRequired, dependentState, cardinality, context, ai.getName());
      else
         return new ClassSingleCallbackItem(info.getType(), whenRequired, dependentState, cardinality, context, ai.getName(), info.getName());
   }

   /**
    * Create callback item from PropertyInfo.
    *
    * @param context owner context
    * @param pi property info
    * @param whenRequired when required state
    * @param dependentState dependent state
    * @param cardinality cardinality
    * @return callback item
    */
   public static CallbackItem<Class<?>> createCallback(
         KernelControllerContext context,
         PropertyInfo pi,
         ControllerState whenRequired,
         ControllerState dependentState,
         Cardinality cardinality)
   {
      return createCallback(context, new PropertyAttributeInfo(pi), whenRequired, dependentState, cardinality);
   }

   /**
    * Create callback item from MethodInfo.
    *
    * @param context owner context
    * @param mi method info
    * @param whenRequired when required state
    * @param dependentState dependent state
    * @param cardinality cardinality
    * @return callback item
    */
   public static CallbackItem<Class<?>> createCallback(
         KernelControllerContext context,
         MethodInfo mi,
         ControllerState whenRequired,
         ControllerState dependentState,
         Cardinality cardinality)
   {
      return createCallback(context, new MethodAttributeInfo(mi), whenRequired, dependentState, cardinality);
   }

   /**
    * Get the collection callback item factory.
    * You can use org.jboss.dependency.collectionCallbackItemFactory name
    * in System properties to override default implementation class.
    *
    * @return get the underlying factory
    */
   public static CollectionCallbackItemFactory getCollectionFactory()
   {
      if (factory == null)
      {
            FactoryLookup lookup = new FactoryLookup();
            factory = AccessController.doPrivileged(lookup);
      }
      return factory;
   }

   // Privileged system property lookup + factory creation
   private static class FactoryLookup implements PrivilegedAction<CollectionCallbackItemFactory>
   {
      public CollectionCallbackItemFactory run()
      {
         try
         {
            String factoryClassName = System.getProperty("org.jboss.dependency.collectionCallbackItemFactory", BasicCollectionCallbackItemFactory.class.getName());
            Object result = ReflectionUtils.newInstance(factoryClassName);
            return CollectionCallbackItemFactory.class.cast(result);
         }
         catch (Throwable t)
         {
            getLog().warn("Exception while creating CollectionCallbackItemFactory, using basic one instead.", t);
            return new BasicCollectionCallbackItemFactory();
         }
      }
   }

}
