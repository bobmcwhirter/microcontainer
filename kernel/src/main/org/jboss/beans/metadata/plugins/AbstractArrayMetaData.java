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
package org.jboss.beans.metadata.plugins;

import java.util.ArrayList;
import java.util.Collection;

import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossStringBuilder;

/**
 * Array metadata.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractArrayMetaData extends AbstractListMetaData
{
   /**
    * Create a new array value
    */
   public AbstractArrayMetaData()
   {
   }

   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      Collection<? extends Object> result = (Collection<? extends Object>) super.getValue(info, cl);

      TypeInfo typeInfo = getClassInfo(cl);
      
      if (typeInfo != null && typeInfo instanceof ClassInfo == false)
         throw new IllegalArgumentException(typeInfo.getName() + " is not a class");

      if (typeInfo == null)
      {
         // No type specified
         if (info == null)
         {
            info = getElementClassInfo(cl);
            if (info == null)
               return null;
            info = info.getArrayType(0);
         }
         // Not a class 
         if (info instanceof ClassInfo == false)
            return null;
         // Not an interface
         if (((ClassInfo) info).isInterface())
            return null;
         // Type is too general
         if (Object.class.getName().equals(info.getName()))
            return null;
         // Try to use the passed type
         typeInfo = info;
      }
      
      Object[] array = new Object[result.size()];
      if (typeInfo != null)
         array = typeInfo.newArrayInstance(result.size());
      
      return result.toArray(array);
   }

   protected Collection<Object> getCollectionInstance(TypeInfo info, ClassLoader cl, Class expected) throws Throwable
   {
      return new ArrayList<Object>();
   }

   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
   }
}