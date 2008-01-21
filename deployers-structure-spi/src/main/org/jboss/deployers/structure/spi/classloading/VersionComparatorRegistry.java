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
package org.jboss.deployers.structure.spi.classloading;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.deployers.structure.spi.classloading.helpers.VersionImpl;
import org.jboss.deployers.structure.spi.classloading.helpers.VersionImplComparator;

/**
 * Version comparator registry.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class VersionComparatorRegistry
{
   private static VersionComparatorRegistry registry = new VersionComparatorRegistry();

   private Map<Class<? extends Version>, Map<Class<? extends Version>, VersionComparator>> comparatorMap = new ConcurrentHashMap<Class<? extends Version>, Map<Class<? extends Version>, VersionComparator>>();

   private VersionComparatorRegistry()
   {
      // register all our known impls
      registerVersionComparator(VersionImpl.class, new VersionImplComparator());
   }

   public static VersionComparatorRegistry getInstance()
   {
      return registry;
   }

   /**
    * Remove the version comparator.
    *
    * @param t first version impl
    * @param u second version impl
    */
   public <T extends Version, U extends Version> void removeVersionComparator(Class<T> t, Class<U> u)
   {
      registerVersionComparator(t, u, null);
   }

   /**
    * Remove the version comparator.
    *
    * @param t version impl
    */
   public <T extends Version> void removeVersionComparator(Class<T> t)
   {
      registerVersionComparator(t, null);
   }

   /**
    * Register version comparator.
    * If comparator parameter is null, it's actually a removal.
    *
    * @param t version impl
    * @param comparator the version comparator
    */
   public <T extends Version> void registerVersionComparator(Class<T> t, VersionComparator<T, T> comparator)
   {
      registerVersionComparator(t, t, comparator);
   }

   /**
    * Register version comparator.
    * If comparator parameter is null, it's actually a removal.
    *
    * @param t first version impl
    * @param u second version impl
    * @param comparator the version comparator
    */
   public <T extends Version, U extends Version> void registerVersionComparator(Class<T> t, Class<U> u, VersionComparator<T, U> comparator)
   {
      if (t == null || u == null)
         throw new IllegalArgumentException("Null version class");

      if (comparator == null)
      {
         Map<Class<? extends Version>, VersionComparator> tKeyMap = comparatorMap.get(t);
         if (tKeyMap != null)
            tKeyMap.remove(u);

         // different impls
         if (t.equals(u) == false)
         {
            Map<Class<? extends Version>, VersionComparator> uKeyMap = comparatorMap.get(u);
            if (uKeyMap != null)
               uKeyMap.remove(t);
         }
      }
      else
      {
         Map<Class<? extends Version>, VersionComparator> tKeyMap = comparatorMap.get(t);
         if (tKeyMap == null)
         {
            tKeyMap = new HashMap<Class<? extends Version>, VersionComparator>();
            comparatorMap.put(t, tKeyMap);
         }
         tKeyMap.put(u, comparator);

         // different impls
         if (t.equals(u) == false)
         {
            Map<Class<? extends Version>, VersionComparator> uKeyMap = comparatorMap.get(u);
            if (uKeyMap == null)
            {
               uKeyMap = new HashMap<Class<? extends Version>, VersionComparator>();
               comparatorMap.put(u, uKeyMap);
            }
            uKeyMap.put(t, new SwitchVersionComparator<U, T>(comparator));
         }
      }
   }

   /**
    * Get the comparator.
    *
    * @param t first version impl
    * @param u second version impl 
    * @return the matching comparator
    */
   @SuppressWarnings("unchecked")
   protected <T extends Version, U extends Version> VersionComparator<T, U> getComparator(T t, U u)
   {
      return getComparator(t.getClass(), u.getClass());
   }

   /**
    * Get the comparator.
    *
    * @param t first version impl class
    * @param u second version impl class
    * @return the matching comparator
    */
   @SuppressWarnings("unchecked")
   public VersionComparator getComparator(Class<? extends Version> t, Class<? extends Version> u)
   {
      Map<Class<? extends Version>, VersionComparator> map = comparatorMap.get(t);
      if (map == null)
         return null;
      else
         return map.get(u);
   }

   /**
    * Compare two version impls.
    *
    * @param t T version impl
    * @param u U version impl
    * @return the compare result
    */
   public <T extends Version, U extends Version> int compare(T t, U u)
   {
      VersionComparator<T, U> comparator = getComparator(t, u);
      if (comparator == null)
         throw new IllegalArgumentException("Missing version comparator for Version impl pair: (" + t.getClass().getName() + "," + u.getClass().getName() + ")");

      return comparator.compare(t, u);
   }

   /**
    * Switch the compare value.
    *
    * @param <T> exact version type
    * @param <U> exact version type
    */
   private class SwitchVersionComparator<T extends Version, U extends Version> implements VersionComparator<T, U>
   {
      private VersionComparator<U, T> delegate;

      public SwitchVersionComparator(VersionComparator<U, T> delegate)
      {
         if (delegate == null)
            throw new IllegalArgumentException("Null delegate");
         this.delegate = delegate;
      }

      public int compare(T t, U u)
      {
         return delegate.compare(u, t) * (-1); 
      }
   }
}
