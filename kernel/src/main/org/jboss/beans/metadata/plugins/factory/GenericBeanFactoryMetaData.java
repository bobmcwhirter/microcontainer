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
package org.jboss.beans.metadata.plugins.factory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractMapMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.LifecycleMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.plugins.bootstrap.basic.KernelConstants;
import org.jboss.kernel.spi.config.KernelConfigurator;

/**
 * GenericBeanFactoryMetaData.
 * 
 * @deprecated see org.jboss.beans.metadata.spi.factory.GenericBeanFactoryMetaData
 * 
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@Deprecated
public class GenericBeanFactoryMetaData extends AbstractBeanMetaData
{
   private static final long serialVersionUID = 2L;

   /**
    * Create a new GenericBeanFactoryMetaData.
    */
   public GenericBeanFactoryMetaData()
   {
      setBean(GenericBeanFactory.class.getName());
      AbstractConstructorMetaData constructor = new AbstractConstructorMetaData();
      ArrayList<ParameterMetaData> parameters = new ArrayList<ParameterMetaData>();
      parameters.add(new AbstractParameterMetaData(KernelConfigurator.class.getName(), new AbstractDependencyValueMetaData(KernelConstants.KERNEL_CONFIGURATOR_NAME)));
      constructor.setParameters(parameters);
      setConstructor(constructor);
      setProperties(new HashSet<PropertyMetaData>());
   }
   
   /**
    * Create a new GenericBeanFactoryMetaData.
    * 
    * @param name the name
    */
   public GenericBeanFactoryMetaData(String name)
   {
      this();
      setName(name);
   }
   
   /**
    * Create a new GenericBeanFactoryMetaData.
    * 
    * @param name the name
    * @param bean the bean class name
    */
   public GenericBeanFactoryMetaData(String name, String bean)
   {
      this(name);
      setBeanClass(bean);
   }

   /**
    * Get the class of the bean created
    * 
    * @return the class
    */
   public String getBeanClass()
   {
      AbstractPropertyMetaData prop = (AbstractPropertyMetaData) getProperty("bean");
      if (prop != null)
      {
         AbstractValueMetaData value = (AbstractValueMetaData) prop.getValue();
         if (value != null)
            return (String) value.getUnderlyingValue();
      }
      return null;
   }

   /**
    * Set the class of the bean created
    * 
    * @param beanClass the class
    */
   public void setBeanClass(String beanClass)
   {
      Set<PropertyMetaData> properties = getProperties();
      properties.add(new AbstractPropertyMetaData("bean", new AbstractValueMetaData(beanClass)));
   }

   public void setClassLoader(ClassLoaderMetaData classLoader)
   {
      super.setClassLoader(classLoader);
      Set<PropertyMetaData> properties = getProperties();
      properties.add(new AbstractPropertyMetaData("classLoader", new AbstractValueMetaData(classLoader)));
   }

   public void setAccessMode(BeanAccessMode accessMode)
   {
      Set<PropertyMetaData> properties = getProperties();
      properties.add(new AbstractPropertyMetaData("accessMode", new AbstractValueMetaData(accessMode)));
   }

   /**
    * Set the bean constructor
    * 
    * @param constructor the constructor
    */
   public void setBeanConstructor(ConstructorMetaData constructor)
   {
      Set<PropertyMetaData> properties = getProperties();
      properties.add(new AbstractPropertyMetaData("constructor", new AbstractValueMetaData(constructor)));
   }
   
   /**
    * Get a bean property
    * 
    * @param name the name
    * @return the value metadata
    */
   public ValueMetaData getBeanProperty(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      PropertyMetaData properties = getProperty("properties");
      if (properties == null)
         return null;
      AbstractMapMetaData map = (AbstractMapMetaData) properties.getValue();
      for (Iterator<Map.Entry<MetaDataVisitorNode, MetaDataVisitorNode>> i = map.entrySet().iterator(); i.hasNext();)
      {
         Map.Entry<MetaDataVisitorNode, MetaDataVisitorNode> entry = i.next();
         ValueMetaData key = (ValueMetaData) entry.getKey();
         if (key.getUnderlyingValue().equals(name))
         {
            ValueMetaData vmd = (ValueMetaData) entry.getValue();
            return (ValueMetaData) vmd.getUnderlyingValue();
         }
      }
      return null;
   }
   
   /**
    * Add a bean property
    * 
    * @param property the property
    */
   public void addBeanProperty(PropertyMetaData property)
   {
      PropertyMetaData properties = getProperty("properties"); 

      AbstractMapMetaData map;
      if (properties == null)
      {
         map = new AbstractMapMetaData();
         properties = new AbstractPropertyMetaData("properties", map);
         addProperty(properties);
      }
      else
      {
         map = (AbstractMapMetaData) properties.getValue(); 
      }
      
      ValueMetaData valueMetaData = property.getValue();
      valueMetaData = new AbstractValueMetaData(valueMetaData);
      map.put(new AbstractValueMetaData(property.getName()), valueMetaData);
   }

   public void setBeanCreate(LifecycleMetaData lifecycle)
   {
      Set<PropertyMetaData> properties = getProperties();
      properties.add(new AbstractPropertyMetaData("create", new AbstractValueMetaData(lifecycle)));
   }

   public void setBeanStart(LifecycleMetaData lifecycle)
   {
      Set<PropertyMetaData> properties = getProperties();
      properties.add(new AbstractPropertyMetaData("start", new AbstractValueMetaData(lifecycle)));
   }

   public void setBean(String bean)
   {
      super.setBean(bean);
   }
}
