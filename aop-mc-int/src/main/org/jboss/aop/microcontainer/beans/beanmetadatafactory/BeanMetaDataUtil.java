/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.aop.microcontainer.beans.beanmetadatafactory;


import java.io.Serializable;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.dependency.spi.ControllerState;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class BeanMetaDataUtil implements Serializable
{
   private static final long serialVersionUID = 1L;

   /** Unless specified use the bean with this name as the aspect manager */
   protected final static String DEFAULT_ASPECT_MANAGER = "AspectManager";
   
   /** The bean name of the aspect manager to use */
   protected String managerBean = DEFAULT_ASPECT_MANAGER;

   /** The property of the aspect manager bean, if any, containing the aspect manager */
   protected String managerProperty;


   public String getManagerBean()
   {
      return managerBean;
   }

   public void setManagerBean(String managerBean)
   {
      this.managerBean = managerBean;
   }

   public String getManagerProperty()
   {
      return managerProperty;
   }

   public void setManagerProperty(String aspectManagerProperty)
   {
      this.managerProperty = aspectManagerProperty;
   }

   public static void setSimpleProperty(AbstractBeanMetaData bean, String propertyName, Object value)
   {
      bean.addProperty(new AbstractPropertyMetaData(propertyName, value));
   }

   public static void setSimpleProperty(AbstractBeanMetaData bean, String propertyName, ValueMetaData value)
   {
      bean.addProperty(new AbstractPropertyMetaData(propertyName, value));
   }
   
   public static void setDependencyProperty(DependencyBuilder builder)
   {
      AbstractDependencyValueMetaData advmd = new AbstractDependencyValueMetaData(builder.getDependencyBean(), builder.getDependencyProperty());
      if (builder.getState() != null)
      {
         advmd.setDependentState(new ControllerState(builder.getState()));
      }
      AbstractBeanMetaData bean = builder.getBean();
      AbstractPropertyMetaData existing = (AbstractPropertyMetaData)bean.getProperty(builder.getPropertyName());
      if (existing != null)
      {
         existing.setValue(advmd);
      }
      else
      {
         bean.addProperty(new AbstractPropertyMetaData(builder.getPropertyName(), advmd));
      }
   }

   public void setAspectManagerProperty(AbstractBeanMetaData bean, String propertyName)
   {
      DependencyBuilder builder = new DependencyBuilder(bean, propertyName, managerBean).setDependencyProperty(managerProperty);
      setDependencyProperty(builder);
   }

   public static class DependencyBuilder
   {
      AbstractBeanMetaData bean;
      String propertyName;
      String dependencyBean;
      String dependencyProperty;
      String state;
      
      public DependencyBuilder(AbstractBeanMetaData bean, String propertyName, String dependencyBean)
      {
         if (bean == null)
            throw new IllegalArgumentException("Null bean");
         if (propertyName == null)
            throw new IllegalArgumentException("Null property name");
         if (dependencyBean == null)
            throw new IllegalArgumentException("Null dependency bean");
         this.bean = bean;
         this.propertyName = propertyName;
         this.dependencyBean = dependencyBean;
      }
      
      DependencyBuilder setDependencyProperty(String dependencyProperty)
      {
         this.dependencyProperty = dependencyProperty;
         return this;
      }

      DependencyBuilder setState(String state)
      {
         this.state = state;
         return this;
      }

      public AbstractBeanMetaData getBean()
      {
         return bean;
      }

      public String getPropertyName()
      {
         return propertyName;
      }

      public String getDependencyBean()
      {
         return dependencyBean;
      }

      public String getDependencyProperty()
      {
         return dependencyProperty;
      }

      public String getState()
      {
         return state;
      }
   }
}
