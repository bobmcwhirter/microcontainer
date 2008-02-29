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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.aop.microcontainer.beans.LifecycleBinding;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.DependencyMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.dependency.spi.ControllerState;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class LifecycleBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
   implements BeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;

   private String classes;
   private String expr;
   private String installMethod;
   private String uninstallMethod;

   HashSet<PropertyMetaData> properties = new HashSet<PropertyMetaData>();

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
      BeanMetaDataBuilder lifecycleBuilder = BeanMetaDataBuilder.createBuilder(name, getBeanClass());
      for (PropertyMetaData pmd : properties)
      {
         lifecycleBuilder.addPropertyMetaData(pmd.getName(), pmd.getValue());   
      }
      Set<DependencyMetaData> depends = getDepends();
      if (depends != null)
      {
         for (DependencyMetaData depend : depends)
         {
            lifecycleBuilder.addDependency(depend.getDependency());
         }
      }
      
      result.add(lifecycleBuilder.getBeanMetaData());
      
      
      String aspectBindingName = name + "$AspectBinding";
      BeanMetaDataBuilder bindingBuilder = BeanMetaDataBuilder.createBuilder(aspectBindingName, LifecycleBinding.class.getName());
      bindingBuilder.addPropertyMetaData("callbackBean", name);
      util.setAspectManagerProperty(bindingBuilder, "manager");
      if (expr != null)
      {
         bindingBuilder.addPropertyMetaData("expr", expr);
      }
      else if (classes != null) 
      {
         bindingBuilder.addPropertyMetaData("classes", classes);         
      }
      bindingBuilder.addPropertyMetaData("state", getState());
      if (installMethod != null)
      {
         bindingBuilder.addPropertyMetaData("installMethod", installMethod);
      }
      if (uninstallMethod != null)
      {
         bindingBuilder.addPropertyMetaData("uninstallMethod", uninstallMethod);
      }
      result.add(bindingBuilder.getBeanMetaData());
      
      return result;
   }

   @Override
   public void addBeanProperty(PropertyMetaData property)
   {
      properties.add(property);
   }
}
