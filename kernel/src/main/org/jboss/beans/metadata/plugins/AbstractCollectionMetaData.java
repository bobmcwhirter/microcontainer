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

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Collection metadata.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractCollectionMetaData extends AbstractTypeMetaData
   implements Collection<MetaDataVisitorNode>, Serializable
{
   private static final long serialVersionUID = 1L;

   /** The collection */
   protected ArrayList<MetaDataVisitorNode> collection = new ArrayList<MetaDataVisitorNode>();

   /** The element type */
   protected String elementType;

   /**
    * Create a new collection value
    */
   public AbstractCollectionMetaData()
   {
   }

   /**
    * Get the element type
    * 
    * @return the element type
    */
   public String getElementType()
   {
      return elementType;
   }

   /**
    * Set the element type
    * 
    * @param elementType the element type
    */
   public void setElementType(String elementType)
   {
      this.elementType = elementType;
   }

   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      Collection<Object> result = getCollectionInstance(info, cl, Collection.class);
      if (result == null)
         result = getDefaultCollectionInstance();

      TypeInfo elementTypeInfo = getElementClassInfo(cl);

      for (int i = 0; i < collection.size(); ++i)
      {
         ValueMetaData vmd = (ValueMetaData) collection.get(i);
         result.add(vmd.getValue(elementTypeInfo, cl));
      }
      return result;
   }

   public Class getType(MetaDataVisitor visitor, MetaDataVisitorNode previous) throws Throwable
   {
      if (elementType != null)
      {
         return getClass(visitor, elementType);
      }
      return super.getType(visitor, previous);
   }

   public boolean add(MetaDataVisitorNode o)
   {
      return collection.add(o);
   }

   public boolean addAll(Collection<? extends MetaDataVisitorNode> c)
   {
      return collection.addAll(c);
   }

   public void clear()
   {
      collection.clear();
   }

   public boolean contains(Object o)
   {
      return collection.contains(o);
   }

   public boolean containsAll(Collection c)
   {
      return collection.containsAll(c);
   }

   public boolean isEmpty()
   {
      return collection.isEmpty();
   }

   public Iterator<MetaDataVisitorNode> iterator()
   {
      return collection.iterator();
   }

   public boolean remove(Object o)
   {
      return collection.remove(o);
   }

   public boolean removeAll(Collection c)
   {
      return collection.removeAll(c);
   }

   public boolean retainAll(Collection c)
   {
      return collection.retainAll(c);
   }

   public int size()
   {
      return collection.size();
   }

   public Object[] toArray()
   {
      return collection.toArray();
   }

   public <T> T[] toArray(T[] a)
   {
      return collection.toArray(a);
   }

   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      return collection.iterator();
   }

   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      buffer.append(" collection=");
      JBossObject.list(buffer, collection);
   }

   /**
    * Create the default collection instance
    * 
    * @return the class instance
    * @throws Throwable for any error
    */
   protected Collection<Object> getDefaultCollectionInstance() throws Throwable
   {
      return new ArrayList<Object>();
   }

   /**
    * Create the collection instance
    * 
    * @param info the request type
    * @param cl the classloader
    * @param expected the expected class
    * @return the class instance
    * @throws Throwable for any error
    */
   @SuppressWarnings("unchecked")
   protected Collection<Object> getCollectionInstance(TypeInfo info, ClassLoader cl, Class<?> expected) throws Throwable
   {
      Object result = preinstantiatedLookup(cl, expected);
      if (result == null)
      {
         TypeInfo typeInfo = getClassInfo(cl);

         if (typeInfo != null && typeInfo instanceof ClassInfo == false)
            throw new IllegalArgumentException(typeInfo.getName() + " is not a class");

         if (typeInfo != null && ((ClassInfo) typeInfo).isInterface())
            throw new IllegalArgumentException(typeInfo.getName() + " is an interface");

         if (typeInfo == null)
         {
            // No type specified
            if (info == null)
               return null;
            // Not a class
            if (info instanceof ClassInfo == false)
               return null;
            // Is an interface
            if (((ClassInfo) info).isInterface())
               return null;
            // Type is too general
            if (Object.class.getName().equals(info.getName()))
               return null;
            // Try to use the passed type
            typeInfo = info;
         }

         BeanInfo beanInfo = configurator.getBeanInfo(typeInfo);
         Joinpoint constructor = configurator.getConstructorJoinPoint(beanInfo);
         result = constructor.dispatch();
         if (expected.isAssignableFrom(result.getClass()) == false)
            throw new ClassCastException(result.getClass() + " is not a " + expected.getName());
      }
      return (Collection<Object>) result;
   }

   /**
    * Get the class info for the element type
    *
    * @param cl the classloader
    * @return the class info
    * @throws Throwable for any error
    */
   protected ClassInfo getElementClassInfo(ClassLoader cl) throws Throwable
   {
      if (elementType == null)
         return null;

      return configurator.getClassInfo(elementType, cl);
   }
}