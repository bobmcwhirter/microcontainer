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
package org.jboss.classloading.spi.version;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Version comparator registry.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 */
public class VersionComparatorRegistry
{
   /** The singleton */
   private static VersionComparatorRegistry registry = new VersionComparatorRegistry();

   /** The comparators */
   private Map<Class<?>, Map<Class<?>, VersionComparator<?, ?>>> comparatorMap = new ConcurrentHashMap<Class<?>, Map<Class<?>, VersionComparator<?, ?>>>();

   /**
    * Create a new VersionComparatorRegistry.
    */
   public VersionComparatorRegistry()
   {
      // register all our known impls
      internalRegisterVersionComparator(Version.class, Version.class, new VersionToVersionComparator(), true);
      internalRegisterVersionComparator(Version.class, String.class, new VersionToStringComparator(), true);
      internalRegisterVersionComparator(String.class, String.class, new StringToStringComparator(), true);
   }

   /**
    * Get the singleton
    *  
    * @return the singleton
    */
   public static VersionComparatorRegistry getInstance()
   {
      return registry;
   }

   /**
    * Remove the version comparator.
    *
    * @param <T> the first version type
    * @param <U> the second version type
    * @param t first version impl
    * @param u second version impl
    */
   public <T, U> void removeVersionComparator(Class<T> t, Class<U> u)
   {
      registerVersionComparator(t, u, null);
   }

   /**
    * Remove the version comparator.
    *
    * @param <T> the version type
    * @param t version impl
    */
   public <T> void removeVersionComparator(Class<T> t)
   {
      registerVersionComparator(t, null);
   }

   /**
    * Register version comparator.
    * If comparator parameter is null, it's actually a removal.
    *
    * @param <T> the version type
    * @param t version impl
    * @param comparator the version comparator
    */
   public <T> void registerVersionComparator(Class<T> t, VersionComparator<T, T> comparator)
   {
      registerVersionComparator(t, t, comparator);
   }

   /**
    * Register version comparator.
    * If comparator parameter is null, it's actually a removal.
    *
    * @param <T> the first version type
    * @param <U> the second version type
    * @param t first version impl
    * @param u second version impl
    * @param comparator the version comparator
    */
   public <T, U> void registerVersionComparator(Class<T> t, Class<U> u, VersionComparator<T, U> comparator)
   {
      internalRegisterVersionComparator(t, u, comparator, false);
   }

   /**
    * Register version comparator.
    * If comparator parameter is null, it's actually a removal.
    *
    * @param <T> the first version type
    * @param <U> the second version type
    * @param t first version impl
    * @param u second version impl
    * @param comparator the version comparator
    * @param setup whether this is during setup
    */
   private <T, U> void internalRegisterVersionComparator(Class<T> t, Class<U> u, VersionComparator<T, U> comparator, boolean setup)
   {
      if (t == null || u == null)
         throw new IllegalArgumentException("Null version class");

      // Don't allow removal of the standard comparators
      if (setup == false)
      {
         if (t == Version.class && u == Version.class)
            throw new IllegalArgumentException("You can't remove or replace the built in Version->Version comparator");
         if (t == Version.class && u == String.class)
            throw new IllegalArgumentException("You can't remove or replace the built in Version->String comparator");
         if (t == String.class && u == Version.class)
            throw new IllegalArgumentException("You can't remove or replace the built in Version->String comparator");
         if (t == String.class && u == String.class)
            throw new IllegalArgumentException("You can't remove or replace the built in Version->String comparator");
      }
      
      if (comparator == null)
      {
         Map<Class<?>, VersionComparator<?, ?>> tKeyMap = comparatorMap.get(t);
         if (tKeyMap != null)
            tKeyMap.remove(u);

         // different impls
         if (t.equals(u) == false)
         {
            Map<Class<?>, VersionComparator<?, ?>> uKeyMap = comparatorMap.get(u);
            if (uKeyMap != null)
               uKeyMap.remove(t);
         }
      }
      else
      {
         Map<Class<?>, VersionComparator<?, ?>> tKeyMap = comparatorMap.get(t);
         if (tKeyMap == null)
         {
            tKeyMap = new ConcurrentHashMap<Class<?>, VersionComparator<?, ?>>();
            comparatorMap.put(t, tKeyMap);
         }
         tKeyMap.put(u, comparator);

         // different impls
         if (t.equals(u) == false)
         {
            Map<Class<?>, VersionComparator<?, ?>> uKeyMap = comparatorMap.get(u);
            if (uKeyMap == null)
            {
               uKeyMap = new ConcurrentHashMap<Class<?>, VersionComparator<?, ?>>();
               comparatorMap.put(u, uKeyMap);
            }
            uKeyMap.put(t, new SwitchVersionComparator<U, T>(comparator));
         }
      }
   }

   /**
    * Get the comparator.
    *
    * @param <T> the first version type
    * @param <U> the second version type
    * @param t first version impl
    * @param u second version impl 
    * @return the matching comparator
    */
   @SuppressWarnings("unchecked")
   protected <T, U> VersionComparator<T, U> getComparator(T t, U u)
   {
      if (t == null || u == null)
         return null;
      Class<T> classT = (Class) t.getClass();
      Class<U> classU = (Class) u.getClass();
      return getComparator(classT, classU);
   }

   /**
    * Get the comparator.
    *
    * @param <T> the first version type
    * @param <U> the second version type
    * @param t first version impl class
    * @param u second version impl class
    * @return the matching comparator
    */
   @SuppressWarnings("unchecked")
   public <T, U> VersionComparator<T, U> getComparator(Class<T> t, Class<U> u)
   {
      Map<Class<?>, VersionComparator<?, ?>> map = comparatorMap.get(t);
      if (map == null)
         return null;
      else
         return (VersionComparator) map.get(u);
   }

   /**
    * Compare two version impls.
    *
    * @param <T> the first version type
    * @param <U> the second version type
    * @param t the first version
    * @param u the second version
    * @return the compare result
    */
   public <T, U> int compare(T t, U u)
   {
      VersionComparator<T, U> comparator = getComparator(t, u);
      if (comparator == null)
      {
         String classT = null;
         if (t != null)
            classT = t.getClass().getName();
         String classU = null;
         if (u != null)
            classU = u.getClass().getName();
         throw new IllegalArgumentException("Missing version comparator for Version pair: (" + classT + "," + classU + ")");
      }

      return comparator.compare(t, u);
   }

   /**
    * Test whether two version impls are request.
    *
    * @param <T> the first version type
    * @param <U> the second version type
    * @param t T version impl
    * @param u U version impl
    * @return the compare result
    */
   public <T, U> boolean same(T t, U u)
   {
      VersionComparator<T, U> comparator = getComparator(t, u);
      if (comparator == null)
         return false;

      return comparator.compare(t, u) == 0;
   }

   /**
    * Switch the compare value.
    *
    * @param <T> the first version type
    * @param <U> the second version type
    */
   private class SwitchVersionComparator<T, U> implements VersionComparator<T, U>
   {
      /** The delegate */
      private VersionComparator<U, T> delegate;

      /**
       * Create a new SwitchVersionComparator.
       * 
       * @param delegate the delegate comparator
       */
      public SwitchVersionComparator(VersionComparator<U, T> delegate)
      {
         if (delegate == null)
            throw new IllegalArgumentException("Null delegate");
         this.delegate = delegate;
      }

      public int compare(T t, U u)
      {
         return (-1) * delegate.compare(u, t); 
      }
   }

   /**
    * VersionToVersionComparator.
    */
   private class VersionToVersionComparator implements VersionComparator<Version, Version>
   {
      public int compare(Version t, Version u)
      {
         return t.compareTo(u);
      }
   }

   /**
    * VersionToStringComparator.
    */
   private class VersionToStringComparator implements VersionComparator<Version, String>
   {
      public int compare(Version t, String u)
      {
         return t.compareTo(Version.parseVersion(u));
      }
   }

   /**
    * StringToStringComparator.
    */
   private class StringToStringComparator implements VersionComparator<String, String>
   {
      public int compare(String t, String u)
      {
         return Version.parseVersion(t).compareTo(Version.parseVersion(u));
      }
   }
}
