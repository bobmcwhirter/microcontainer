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
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;

import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;
import org.jboss.xb.annotations.JBossXmlChild;
import org.jboss.xb.annotations.JBossXmlChildWildcard;
import org.jboss.xb.annotations.JBossXmlChildren;
import org.jboss.xb.annotations.JBossXmlNoElements;

/**
 * Collection metadata.
 * 
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@XmlType(name="collectionType")
@JBossXmlNoElements
@JBossXmlChildren
({
   @JBossXmlChild(name="bean", type=AbstractBeanMetaData.class),
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
public class AbstractCollectionMetaData extends AbstractTypeMetaData
   implements Collection<MetaDataVisitorNode>, Serializable
{
   private static final long serialVersionUID = 2L;

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
   @XmlAttribute(name="elementClass")
   public void setElementType(String elementType)
   {
      this.elementType = elementType;
   }

   @SuppressWarnings("unchecked")
   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      Collection result = getTypeInstance(info, cl, Collection.class);

      TypeInfo elementTypeInfo = getElementClassInfo(cl, info);
      for (int i = 0; i < collection.size(); ++i)
      {
         ValueMetaData vmd = (ValueMetaData) collection.get(i);
         result.add(vmd.getValue(elementTypeInfo, cl));
      }
      return result;
   }

   public TypeInfo getType(MetaDataVisitor visitor, MetaDataVisitorNode previous) throws Throwable
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

   public boolean containsAll(Collection<?> c)
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

   public boolean removeAll(Collection<?> c)
   {
      return collection.removeAll(c);
   }

   public boolean retainAll(Collection<?> c)
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
    */
   protected Object getDefaultInstance()
   {
      return new ArrayList<Object>();
   }

   /**
    * Get the class info for the element type
    *
    * @param cl the classloader
    * @param info the type info
    * @return the class info
    * @throws Throwable for any error
    */
   protected TypeInfo getElementClassInfo(ClassLoader cl, TypeInfo info) throws Throwable
   {
      if (elementType != null)
         return configurator.getClassInfo(elementType, cl);

      // null is excluded
      if (info instanceof ClassInfo)
      {
         ClassInfo classInfo = (ClassInfo)info;
         return classInfo.getComponentType();
      }

      return null;
   }
}