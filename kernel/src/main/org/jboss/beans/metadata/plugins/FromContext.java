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

import java.io.Serializable;
import java.lang.reflect.Method;

import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;
import org.jboss.reflect.plugins.introspection.ReflectionUtils;
import org.jboss.dependency.spi.ControllerContext;

/**
 * Inject from controller context:
 *  * name - controller context name
 *  * metadata - inject MetaData
 *  * scope - ScopeKey
 *  * id - identifier
 *  * ...
 *
 * @param <T> exact controller context type
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
public abstract class FromContext<T extends ControllerContext> extends JBossObject
      implements Serializable
{
   private static final long serialVersionUID = 1L;

   /** name */
   public static final FromContext NAME = new NameFromContext("name");

   /** metadata */
   public static final FromContext METADATA = new MetaDataFromContext("metadata");

   /** scope */
   public static final FromContext SCOPE = new ScopeFromContext("scope");

   /** id */
   public static final FromContext ID = new IdFromContext("id");

   /** The type string */
   protected final String fromString;

   /**
    * Create a new state
    *
    * @param fromString the string representation
    */
   protected FromContext(String fromString)
   {
      if (fromString == null)
         throw new IllegalArgumentException("Null from string");
      this.fromString = fromString;
   }

   /**
    * Return from type.
    *
    * @param optionString type
    * @return InjectionOption instance
    */
   public static FromContext getInstance(String optionString)
   {
      if (NAME.getFromString().equalsIgnoreCase(optionString))
         return NAME;
      else if (METADATA.getFromString().equalsIgnoreCase(optionString))
         return METADATA;
      else if (SCOPE.getFromString().equalsIgnoreCase(optionString))
         return SCOPE;
      else if (ID.getFromString().equalsIgnoreCase(optionString))
         return ID;
      else
         return new DynamicFromContext(optionString);
   }

   /**
    * Execute injection on context.
    *
    * @param context the target context
    * @return lookup value
    * @throws Throwable for any error
    */
   public abstract Object executeLookup(T context) throws Throwable;

   /**
    * Get the from string
    *
    * @return the state string
    */
   public String getFromString()
   {
      return fromString;
   }

   public boolean equals(Object object)
   {
      if (object == null || object instanceof FromContext == false)
         return false;
      FromContext other = (FromContext) object;
      return fromString.equals(other.getFromString());
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append(fromString);
   }

   protected int getHashCode()
   {
      return fromString.hashCode();
   }

   private static class NameFromContext extends FromContext
   {
      private static final long serialVersionUID = 1L;

      public NameFromContext(String fromString)
      {
         super(fromString);
      }

      public Object executeLookup(ControllerContext context)
      {
         return context.getName();
      }
   }

   private static class MetaDataFromContext extends FromContext<KernelControllerContext>
   {
      private static final long serialVersionUID = 1L;

      public MetaDataFromContext(String fromString)
      {
         super(fromString);
      }

      public MetaData executeLookup(KernelControllerContext context)
      {
         return context.getMetaData();
      }
   }

   private static class ScopeFromContext extends FromContext<KernelControllerContext>
   {
      private static final long serialVersionUID = 1L;

      public ScopeFromContext(String fromString)
      {
         super(fromString);
      }

      public ScopeKey executeLookup(KernelControllerContext context)
      {
         return context.getScope();
      }
   }

   private static class IdFromContext extends FromContext
   {
      private static final long serialVersionUID = 1L;

      public IdFromContext(String fromString)
      {
         super(fromString);
      }

      public Object executeLookup(ControllerContext context)
      {
         // todo - change to actual id when impl
         return context.getName();
      }
   }

   private static class DynamicFromContext extends FromContext
   {
      private static final long serialVersionUID = 1L;

      public DynamicFromContext(String fromString)
      {
         super(fromString);
      }

      protected Method findMethod(Class clazz)
      {
         if (clazz == null || clazz == Object.class)
            return null;

         Method[] methods = clazz.getDeclaredMethods();
         for(Method m : methods)
         {
            if (m.getName().equals(getFromString()) && m.getParameterTypes().length == 0)
            {
               return m;
            }
         }

         Method method = findMethod(clazz.getSuperclass());
         if (method != null)
            return method;

         for(Class infc : clazz.getInterfaces())
         {
            Method m = findMethod(infc);
            if (m != null)
               return m;
         }
         return null;
      }

      public Object executeLookup(ControllerContext context) throws Throwable
      {
         Method method = findMethod(context.getClass());
         if (method == null)
            throw new IllegalArgumentException("No such getter on context class: " + getFromString());
         return ReflectionUtils.invoke(method, context, new Object[]{});
      }
   }

}
