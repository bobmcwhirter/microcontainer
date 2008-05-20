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

import java.lang.annotation.Annotation;

/**
 * Contexts cache key.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
// TODO - fix this key to have weak notion
class CacheKey
{
   private Class<?> clazz;
   private Annotation[] annotations;

   CacheKey(Class<?> clazz, Annotation[] annotations)
   {
      this.clazz = clazz;
      this.annotations = annotations;
   }

   public int hashCode()
   {
      int hash = clazz.hashCode();
      for (Annotation annotation : annotations)
         hash += (3 * annotation.hashCode());
      return hash;
   }

   public boolean equals(Object obj)
   {
      if (obj instanceof CacheKey == false)
         return false;

      CacheKey ck = (CacheKey)obj;
      if (clazz.equals(ck.clazz) == false)
         return false;
      if (annotations.length != ck.annotations.length)
         return false;

      for (Annotation annotation : annotations)
      {
         boolean match = false;
         for (Annotation ckAnnotation : ck.annotations)
         {
            if (annotation.equals(ckAnnotation))
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
}