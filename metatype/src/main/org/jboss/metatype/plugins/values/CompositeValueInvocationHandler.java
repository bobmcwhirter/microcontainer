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
package org.jboss.metatype.plugins.values;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Collections;
import java.util.WeakHashMap;

import org.jboss.util.UnreachableStatementException;
import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.MetaValueFactory;
import org.jboss.metatype.api.values.MetaValue;

/**
 * CompositeValueInvocationHandler.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 */
public class CompositeValueInvocationHandler implements InvocationHandler
{
   /** The MetaValueFactory */
   private static MetaValueFactory factory = MetaValueFactory.getInstance();

   /** A cache of methods to keys */
   private static final Map<Method, String> compositeDataKeyCache = Collections.synchronizedMap(new WeakHashMap<Method, String>());

   /** The composite value */
   private CompositeValue compositeValue;

   /**
    * Create a new CompositeValueInvocationHandler.
    *
    * @param compositeValue the composite value
    */
   public CompositeValueInvocationHandler(CompositeValue compositeValue)
   {
      if (compositeValue == null)
         throw new IllegalArgumentException("Null compositeValue");
      this.compositeValue = compositeValue;
   }

   /**
    * Get the compositeData.
    *
    * @return the compositeData.
    */
   public CompositeValue getCompositeValue()
   {
      return compositeValue;
   }

   public Object invoke(Object proxy, Method method, Object[] args) throws Throwable
   {
      if (Object.class.equals(method.getDeclaringClass()))
         return handleObjectInvocation(method.getName(), args);

      MetaValue value = compositeValue.get(getCompositeDataKey(method));
      Type returnType = method.getGenericReturnType();
      return factory.unwrap(value, returnType);
   }

   private Object handleObjectInvocation(String name, Object[] args) throws Throwable
   {
      if ("equals".equals(name))
      {
         Object object = args[0];
         if (object == null || object instanceof Proxy == false)
            return false;
         InvocationHandler handler = Proxy.getInvocationHandler(object);
         if (handler == this)
            return true;
         if (handler == null || handler instanceof CompositeValueInvocationHandler == false)
            return false;

         CompositeValueInvocationHandler other = (CompositeValueInvocationHandler) handler;
         return getCompositeValue().equals(other.getCompositeValue());
      }
      else if ("hashCode".equals(name))
         return getCompositeValue().hashCode();
      else if ("toString".equals(name))
         return getCompositeValue().toString();
      throw new UnreachableStatementException();
   }

   /**
    * Get the key for a composite data getter method
    *
    * @param method the method
    * @return the key
    */
   public static String getCompositeDataKey(Method method)
   {
      String key = compositeDataKeyCache.get(method);
      if (key != null)
         return key;

      StringBuilder fieldName = null;

      Class<?> returnType = method.getReturnType();
      Class<?>[] paramTypes = method.getParameterTypes();
      if (Void.TYPE.equals(returnType) == false && paramTypes.length == 0)
      {
         String name = method.getName();
         if (name.startsWith("is") && name.length() > 2)
         {
            if (Boolean.TYPE.equals(returnType))
            {
               fieldName = new StringBuilder();
               fieldName.append(Character.toLowerCase(name.charAt(2)));
               if (name.length() > 3)
                  fieldName.append(name.substring(3));
            }
         }
         else if (name.startsWith("get") && name.length() > 3)
         {
            fieldName = new StringBuilder();
            fieldName.append(Character.toLowerCase(name.charAt(3)));
            if (name.length() > 4)
               fieldName.append(name.substring(4));
         }
      }

      if (fieldName == null)
         return null;

      String result = fieldName.toString();
      compositeDataKeyCache.put(method, result);
      return result;
   }
}
