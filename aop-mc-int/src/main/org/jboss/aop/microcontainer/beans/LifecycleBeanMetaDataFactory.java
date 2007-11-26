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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.jboss.aop.microcontainer.beans.beanmetadatafactory.BeanMetaDataUtil;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.dependency.spi.ControllerState;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class LifecycleBeanMetaDataFactory extends GenericBeanFactoryMetaData
implements BeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;

   private String classes;
   private String expr;
   private String installMethod;
   private String uninstallMethod;

   BeanMetaDataUtil util = new BeanMetaDataUtil();
   HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();

   public void setManagerBean(String managerBean)
   {
      util.setManagerBean(managerBean);
   }

   public void setManagerProperty(String aspectManagerProperty)
   {
      util.setManagerProperty(aspectManagerProperty);
   }

   public void setClasses(String classes)
   {
      this.classes = classes;
   }
   
   public void setExpr(String classes)
   {
      this.expr = classes;
   }
   
   public void setInstallMethod(String installMethod)
   {
      this.installMethod = installMethod;
   }

   public void setUninstallMethod(String uninstallMethod)
   {
      this.uninstallMethod = uninstallMethod;
   }

   protected abstract ControllerState getState();

   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> result = new ArrayList<BeanMetaData>();

      //Do not include the bean factory here, just install the bean directly and the binding 
      AbstractBeanMetaData lifecycle = new AbstractBeanMetaData();
      lifecycle.setName(name);
      lifecycle.setBean(getBeanClass());
      for (PropertyMetaData pmd : properties)
      {
         lifecycle.addProperty(pmd);   
      }
      lifecycle.setDepends(getDepends());
      result.add(lifecycle);
      
      
      String aspectBindingName = name + "$AspectBinding";
      AbstractBeanMetaData aspectBinding = new AbstractBeanMetaData();
      aspectBinding.setName(aspectBindingName);
      aspectBinding.setBean(LifecycleBinding.class.getName());

      BeanMetaDataUtil.setSimpleProperty(aspectBinding, "callbackBean", name);
      util.setAspectManagerProperty(aspectBinding, "manager");
      if (expr != null)
      {
         BeanMetaDataUtil.setSimpleProperty(aspectBinding, "expr", expr);
      }
      else if (classes != null) 
      {
         BeanMetaDataUtil.setSimpleProperty(aspectBinding, "classes", classes);         
      }
      BeanMetaDataUtil.setSimpleProperty(aspectBinding, "state", getState());
      if (installMethod != null)
      {
         BeanMetaDataUtil.setSimpleProperty(aspectBinding, "installMethod", installMethod);
      }
      if (uninstallMethod != null)
      {
         BeanMetaDataUtil.setSimpleProperty(aspectBinding, "uninstallMethod", uninstallMethod);
      }
      result.add(aspectBinding);
      
      return result;
   }


   protected boolean hasInjectedBeans()
   {
      ArrayList<AbstractDependencyValueMetaData> dependencies = new ArrayList<AbstractDependencyValueMetaData>();
      getDependencies(dependencies, this);
      
      for (AbstractDependencyValueMetaData dep : dependencies)
      {
         if(!((String)dep.getValue()).startsWith("jboss.kernel:service="))
         {
            return true;
         }
      }
      return false;
   }

   private void getDependencies(ArrayList<AbstractDependencyValueMetaData> dependencies, MetaDataVisitorNode node)
   {
      Iterator<? extends MetaDataVisitorNode> children = node.getChildren();
      
      if (children != null)
      {
         while (children.hasNext())
         {
            MetaDataVisitorNode child = children.next();
            if (child instanceof AbstractDependencyValueMetaData)
            {
               dependencies.add((AbstractDependencyValueMetaData)child);
            }
            getDependencies(dependencies, child);
         }
      }
   }

   @Override
   public void addBeanProperty(PropertyMetaData property)
   {
      properties.add(property);
   }

}
