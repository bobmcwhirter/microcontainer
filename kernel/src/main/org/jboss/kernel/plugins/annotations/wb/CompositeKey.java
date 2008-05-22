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
package org.jboss.kernel.plugins.annotations.wb;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 * Composite map key with weak part notion.
 *
 * @param <T> exact weak part type
 * @param <U> rest of the composite key
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="mailto:bstansberry@jboss.com">Brian Stansberry</a>
 */
public abstract class CompositeKey<T, U>
{
   private KeyRef<T, U> weakPart;
   private U[] rest;

   protected CompositeKey(T weakPart, U[] rest)
   {
      if (weakPart == null)
         throw new IllegalArgumentException("Null weak part.");
      if (rest == null)
         throw new IllegalArgumentException("Null rest");

      this.weakPart = new KeyRef<T, U>(this, weakPart, getReferenceQueue());
      this.rest = rest;
   }

   /**
    * Get the reference queue holding gced references.
    *
    * @return the reference queue
    */
   protected abstract ReferenceQueue<T> getReferenceQueue();

   /**
    * Get hash code from weak ref.
    * If already null return zero.
    *
    * @param wr the weak reference
    * @return wr's value hash code
    */
   protected static int safeWeakHaskHode(WeakReference wr)
   {
      Object weak = wr.get();
      return weak != null ? weak.hashCode() : 0;
   }

   /**
    * Get weak part hash code.
    *
    * @return the weak part hash code
    */
   protected int getWeakPartHashCode()
   {
      return safeWeakHaskHode(weakPart);
   }

   /**
    * Get rest hash code.
    *
    * @return the rest hash code
    */
   protected int getRestHashCode()
   {
      int hash = 0;
      for (U u : rest)
         hash += (3 * u.hashCode());
      return hash;
   }

   public int hashCode()
   {
      return getWeakPartHashCode() + getRestHashCode();
   }

   @SuppressWarnings("unchecked")
   public boolean equals(Object obj)
   {
      if (obj instanceof CompositeKey == false)
         return false;

      CompositeKey<T,U> ck = (CompositeKey<T,U>)obj;
      T wp = weakPart.get();
      T otherWP = ck.weakPart.get();

      if (wp == null || otherWP == null)
      {
         cleanKeyRefs();
         return false;
      }
      else if (wp.equals(otherWP) == false)
      {
         return false;
      }

      if (rest.length != ck.rest.length)
         return false;

      for (U u : rest)
      {
         boolean match = false;
         for (U ou : ck.rest)
         {
            
            if (u.equals(ou))
            {
               match = true;
               break;
            }
         }
         if (match == false)
            return false;
      }

      return true;
   }

   /**
    * Allows optimization; i.e. change to only poll the queue every X invocations.
    *
    * @return true if we should clean refs
    */
   protected boolean shouldCleanKeyRefs()
   {
      return true;
   }

   /**
    * Poll the reference queue and clear the strong ref to Key from all
    * reference objects.  This allows the Key to get removed from
    * WeakHashMap.
    */
   private void cleanKeyRefs()
   {
      ReferenceQueue<T> queue = getReferenceQueue();
      Reference ref;
      while ((ref = queue.poll()) != null)
      {
         ((KeyRef)ref).nullifyKey();
      }
   }

   /**
    * KeyRef strong holder.
    */
   private static class KeyRef<T, U> extends WeakReference<T>
   {
      private CompositeKey<T, U> key;

      private KeyRef(CompositeKey<T, U> key, T referent, ReferenceQueue<? super T> q)
      {
         super(referent, q);
         this.key = key;
      }

      public CompositeKey<T, U> getKey()
      {
         return key;
      }

      public void nullifyKey()
      {
         this.key = null;
      }
   }
}