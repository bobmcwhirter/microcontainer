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
package org.jboss.aop.microcontainer.beans.metadata;

import javax.xml.bind.annotation.XmlAttribute;

import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.beans.metadata.spi.factory.GenericBeanFactoryMetaData;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class AspectManagerAwareBeanMetaDataFactory extends GenericBeanFactoryMetaData
{
   /** Unless specified use the bean with this name as the aspect manager */
   protected final static String DEFAULT_ASPECT_MANAGER = "AspectManager";
   
   /** The bean name of the aspect manager to use */
   protected String managerBean;
   
   /** Whether the managerBean was set using the setManagerBean accessor */
   protected boolean managerBeanSet;

   /** The property of the aspect manager bean, if any, containing the aspect manager */
   protected String managerProperty;

   /** Whether the managerProperty was set using the setManagerBean accessor */
   protected boolean managerPropertySet;

   @XmlAttribute(name="manager-bean")
   public void setManagerBean(String managerBean)
   {
      this.managerBean = managerBean;
      managerBeanSet = true;
   }

   public String getManagerBean()
   {
      return managerBean;
   }

   @XmlAttribute(name="manager-property")
   public void setManagerProperty(String aspectManagerProperty)
   {
      this.managerProperty = aspectManagerProperty;
      managerPropertySet = true;
   }
   
   public String getManagerProperty()
   {
      return managerProperty;
   }

   protected void setAspectManagerProperty(BeanMetaDataBuilder builder)
   {
      setAspectManagerProperty(builder, "manager");
   }

   protected void setAspectManagerProperty(BeanMetaDataBuilder builder, String propertyName)
   {
      String bean = managerBeanSet ? managerBean : DefaultAspectManager.getManagerBeanName();
      String property = managerPropertySet ? managerProperty : DefaultAspectManager.getManagerPropertyName();
      setAspectManagerProperty(builder, propertyName, bean, property);
   }

   protected void setAspectManagerProperty(BeanMetaDataBuilder builder, String managerBean, String managerProperty)
   {
      setAspectManagerProperty(builder, "manager", managerBean, managerProperty);
   }

   protected void setAspectManagerProperty(BeanMetaDataBuilder builder, String propertyName, String managerBean, String managerProperty)
   {
      ValueMetaData value = builder.createInject(managerBean, managerProperty);
      builder.addPropertyMetaData(propertyName, value);
   }
}
