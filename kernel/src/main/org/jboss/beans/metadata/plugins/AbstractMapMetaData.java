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
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlTransient;

import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.xb.annotations.JBossXmlMapEntry;

/**
 * Map metadata.
 * 
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@XmlType(name="mapType")
@JBossXmlMapEntry(name = "entry", type = MapEntry.class)
public class AbstractMapMetaData extends AbstractTypeMetaData
   implements Map<MetaDataVisitorNode, MetaDataVisitorNode>, Serializable
{
   private static final long serialVersionUID = 2L;

   /** The map */
   private Map<MetaDataVisitorNode, MetaDataVisitorNode> map = new HashMap<MetaDataVisitorNode, MetaDataVisitorNode>();

   /** The key type */
   protected String keyType;

   /** The value type */
   protected String valueType;

   /**
    * Create a new map value
    */
   public AbstractMapMetaData()
   {
   }

   /**
    * Get the key type
    * 
    * @return the key type
    */
   public String getKeyType()
   {
      return keyType;
   }

   /**
    * Set the key type
    * 
    * @param keyType the key type
    */
   @XmlAttribute(name="keyClass")
   public void setKeyType(String keyType)
   {
      this.keyType = keyType;
   }

   /**
    * Get the value type
    * 
    * @return the value type
    */
   public String getValueType()
   {
      return valueType;
   }

   /**
    * Set the value type
    * 
    * @param valueType the value type
    */
   @XmlAttribute(name="valueClass")
   public void setValueType(String valueType)
   {
      this.valueType = valueType;
   }

   @SuppressWarnings("unchecked")
   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      Map result = getTypeInstance(info, cl, getExpectedClass());

      TypeInfo keyTypeInfo = getKeyClassInfo(cl);
      TypeInfo valueTypeInfo = getValueClassInfo(cl);

      if (map.size() > 0)
      {
         boolean first = true;
         for (Iterator i = map.entrySet().iterator(); i.hasNext();)
         {
            Map.Entry entry = (Map.Entry) i.next();
            ValueMetaData key = (ValueMetaData) entry.getKey();
            ValueMetaData value = (ValueMetaData) entry.getValue();
            Object keyValue = key.getValue(keyTypeInfo, cl);
            Object valueValue = value.getValue(valueTypeInfo, cl);
            try
            {
               result.put(keyValue, valueValue);
            }
            catch (UnsupportedOperationException e)
            {
               if (first == false)
                  throw e;
               result = getTypeInstance(info, cl, getExpectedClass(), false);
               result.put(keyValue, valueValue);
            }
            first = false;
         }
      }
      return result;
   }

   public void clear()
   {
      map.clear();
   }

   public boolean containsKey(Object key)
   {
      return map.containsKey(key);
   }

   public boolean containsValue(Object value)
   {
      return map.containsValue(value);
   }

   public Set<Entry<MetaDataVisitorNode, MetaDataVisitorNode>> entrySet()
   {
      return map.entrySet();
   }

   public MetaDataVisitorNode get(Object key)
   {
      return map.get(key);
   }

   public boolean isEmpty()
   {
      return map.isEmpty();
   }

   public Set<MetaDataVisitorNode> keySet()
   {
      return map.keySet();
   }

   public MetaDataVisitorNode put(MetaDataVisitorNode key, MetaDataVisitorNode value)
   {
      return map.put(key, value);
   }

   public void putAll(Map<? extends MetaDataVisitorNode, ? extends MetaDataVisitorNode> t)
   {
      map.putAll(t);
   }

   public MetaDataVisitorNode remove(Object key)
   {
      return map.remove(key);
   }

   public int size()
   {
      return map.size();
   }

   public Collection<MetaDataVisitorNode> values()
   {
      return map.values();
   }

   @XmlTransient
   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      ArrayList<MetaDataVisitorNode> children = new ArrayList<MetaDataVisitorNode>(keySet());
      children.addAll(values());
      return children.iterator();
   }

   public TypeInfo getType(MetaDataVisitor visitor, MetaDataVisitorNode previous) throws Throwable
   {
      if (keyType != null)
      {
         for(MetaDataVisitorNode key : keySet())
         {
            if (previous.equals(key))
            {
               return getClass(visitor, keyType);
            }
         }
      }
      if (valueType != null)
      {
         for(MetaDataVisitorNode v : values())
         {
            if (previous.equals(v))
            {
               return getClass(visitor, valueType);
            }
         }
      }
      return super.getType(visitor, previous);
   }

   /**
    * Create the default map instance
    * 
    * @return the class instance
    */
   @XmlTransient
   protected Object getDefaultInstance()
   {
      return new HashMap<Object, Object>();
   }

   @SuppressWarnings("unchecked")
   @XmlTransient
   protected Class<? extends Map> getExpectedClass()
   {
      return Map.class;
   }

   /**
    * Get the class info for the key type
    * 
    * @param cl the classloader
    * @return the class info
    * @throws Throwable for any error
    */
   protected ClassInfo getKeyClassInfo(ClassLoader cl) throws Throwable
   {
      if (keyType == null)
         return null;

      return configurator.getClassInfo(keyType, cl);
   }

   /**
    * Get the class info for the value type
    * 
    * @param cl the classloader
    * @return the class info
    * @throws Throwable for any error
    */
   protected ClassInfo getValueClassInfo(ClassLoader cl) throws Throwable
   {
      if (valueType == null)
         return null;

      return configurator.getClassInfo(valueType, cl);
   }

   public AbstractMapMetaData clone()
   {
      AbstractMapMetaData clone = (AbstractMapMetaData)super.clone();
      clone.map = CloneUtil.cloneMap(map, MetaDataVisitorNode.class, MetaDataVisitorNode.class);
      return clone;
   }
}