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
package org.jboss.kernel.api.dependency;

/**
 * Match the class type.
 *
 * @param <T> the class to match
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class ClassMatcher<T> extends NonNullMatcher
{
   private Class<T> clazz;

   protected ClassMatcher(Class<T> clazz)
   {
      if (clazz == null)
         throw new IllegalArgumentException("Null class");
      this.clazz = clazz;
   }

   protected boolean internalMatch(Object other)
   {
      return clazz.isAssignableFrom(other.getClass()) != false && matchByType(clazz.cast(other));
   }

   /**
    * Match by type.
    *
    * @param other the T para to match
    * @return true if matched, false otherwise
    */
   protected abstract boolean matchByType(T other);
}
