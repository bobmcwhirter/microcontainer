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
import java.lang.reflect.Array;

import javax.xml.bind.annotation.XmlType;

import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.xb.annotations.JBossXmlNoElements;
import org.jboss.xb.annotations.JBossXmlChildren;
import org.jboss.xb.annotations.JBossXmlChild;
import org.jboss.xb.annotations.JBossXmlChildWildcard;

/**
 * Array metadata.
 * 
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@XmlType
@JBossXmlNoElements
@JBossXmlChildren
({
   @JBossXmlChild(name="array", type=AbstractArrayMetaData.class),
   @JBossXmlChild(name="collection", type=AbstractCollectionMetaData.class),
   @JBossXmlChild(name="inject", type=AbstractInjectionValueMetaData.class),
   @JBossXmlChild(name="list", type=AbstractListMetaData.class),
   @JBossXmlChild(name="map", type=AbstractMapMetaData.class),
   @JBossXmlChild(name="null", type=AbstractValueMetaData.class),
   @JBossXmlChild(name="set", type=AbstractSetMetaData.class),
   @JBossXmlChild(name="this", type=ThisValueMetaData.class),
   @JBossXmlChild(name="value", type=StringValueMetaData.class),
   @JBossXmlChild(name="value-factory", type=AbstractValueFactoryMetaData.class)
})
@JBossXmlChildWildcard(wrapper=AbstractValueMetaData.class, property="value")
public class AbstractArrayMetaData extends AbstractListMetaData
{
   private static final long serialVersionUID = 2L;

   /**
    * Create a new array value
    */
   public AbstractArrayMetaData()
   {
   }

   @SuppressWarnings("unchecked")
   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      Collection result = (Collection)super.getValue(info, cl);

      TypeInfo typeInfo = getClassInfo(cl);
      
      if (typeInfo != null && typeInfo instanceof ClassInfo == false)
         throw new IllegalArgumentException(typeInfo.getName() + " is not a class");

      if (typeInfo == null)
      {
         // No type specified
         if (info == null)
         {
            info = getElementClassInfo(cl, null);
            if (info == null)
               return null;
            info = info.getArrayType();
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

      Object array = typeInfo.newArrayInstance(result.size());
      int i = 0;
      for(Object element : result)
         Array.set(array, i++, element);
      return array;
   }

   protected <T> T getTypeInstance(TypeInfo info, ClassLoader cl, Class<T> expected) throws Throwable
   {
      Collection<Object> result = new ArrayList<Object>();
      Object preinstantiatedObject = preinstantiatedLookup(cl, Object[].class);
      if (preinstantiatedObject != null)
      {
         Object[] preinstantiatedArray = (Object[]) preinstantiatedObject;
         for(Object previous : preinstantiatedArray)
         {
            result.add(previous);
         }
      }
      return expected.cast(result);
   }

   protected <T> T checkResult(Object result, Class<T> expected)
   {
      if (result != null)
      {
         if (result.getClass().isArray() == false)
            throw new ClassCastException("Preinstantiated property is not an array: " + propertyName);
      }
      return expected.cast(result);
   }
}