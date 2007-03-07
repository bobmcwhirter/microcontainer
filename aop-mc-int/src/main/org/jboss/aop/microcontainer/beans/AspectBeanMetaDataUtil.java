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
package org.jboss.aop.microcontainer.beans;


import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class AspectBeanMetaDataUtil
{
   private static final long serialVersionUID = 1L;

   /** Unless specified use the bean with this name as the aspect manager */
   protected final static String DEFAULT_ASPECT_MANAGER = "AspectManager";
   
   /** The bean name of the aspect manager to use */
   protected String managerBean = DEFAULT_ASPECT_MANAGER;

   /** The property of the aspect manager bean, if any, containing the aspect manager */
   protected String managerProperty;


   public String getManager()
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

   protected void setSimpleProperty(AbstractBeanMetaData bean, String propertyName, Object value)
   {
      bean.addProperty(new AbstractPropertyMetaData(propertyName, value));
   }

   protected void setDependencyProperty(AbstractBeanMetaData bean, String propertyName, Object dependencyBean)
   {
      setDependencyProperty(bean, propertyName, dependencyBean, null);
   }

   protected void setDependencyProperty(AbstractBeanMetaData bean, String propertyName, Object dependencyBean, String dependencyProperty)
   {
      bean.addProperty(new AbstractPropertyMetaData(propertyName, new AbstractDependencyValueMetaData(dependencyBean, dependencyProperty)));
   }

   protected void setAspectManagerProperty(AbstractBeanMetaData bean, String propertyName)
   {
      setDependencyProperty(bean, propertyName, managerBean, managerProperty);
   }

}
