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
package org.jboss.aop.microcontainer.beans.beanmetadatafactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.aop.microcontainer.beans.Aspect;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.ThisValueMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ParameterMetaData;

/**
 * AspectBeanMetaDataFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 61194 $
 */
public class AspectBeanMetaDataFactory extends AspectManagerAwareBeanMetaDataFactory
   implements BeanMetaDataFactory
{
   private static final long serialVersionUID = 1L;

   private String scope = "PER_VM";

   private String factory;
   
   public void setScope(String scope)
   {
      this.scope = scope;
   }

   public void setFactory(String clazz)
   {
      this.factory = clazz;
      if (getBeanClass() != null)
      {
         throw new RuntimeException("Cannot set both factory and clazz");
      }
      //Meeded to satisfy validation in BeanFactoryHandler.endElement()
      super.setBeanClass(clazz);
   }
   
   @Override
   public void setBeanClass(String bean)
   {
      if (factory != null)
      {
         throw new RuntimeException("Cannot set both factory and clazz");
      }
      super.setBeanClass(bean);
   }
   
   @Override
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> result = new ArrayList<BeanMetaData>();
      
      if (this.name == null)
      {
         this.name = super.getBeanClass();
      }
      
      String aspectName = this.name;
      //Add the bean factory
      this.name = "Factory$" + name;
      result.add(this);
      //TODO dependencies and properties for aspect/interceptor
      
      //Add the Aspect
      AbstractBeanMetaData aspect = new AbstractBeanMetaData();
      aspect.setName(aspectName);
      aspect.setBean(Aspect.class.getName());
      BeanMetaDataUtil.setSimpleProperty(aspect, "scope", scope);
      util.setAspectManagerProperty(aspect, "manager");
      if (factory != null)
      {
         BeanMetaDataUtil.setSimpleProperty(aspect, "factory", Boolean.TRUE);
      }
      result.add(aspect);
      
      if (hasInjectedBeans())
      {
         configureWithDependencies(aspect);
      }
      else
      {
         configureNoDependencies(aspect);
      }

      return result;
   }

   private void configureWithDependencies(AbstractBeanMetaData aspect)
   {
      aspect.addProperty(new AbstractPropertyMetaData("adviceBean", name));
      
      AbstractInstallMetaData installAspect = new AbstractInstallMetaData();
      installAspect.setBean(aspect.getName());
      installAspect.setMethodName("install");
      ArrayList<ParameterMetaData> parameters = new ArrayList<ParameterMetaData>();
      parameters.add(new AbstractParameterMetaData(new ThisValueMetaData()));
      installAspect.setParameters(parameters);
      
      List<InstallMetaData> installs = getInstalls();
      if (installs == null)
         installs = new ArrayList<InstallMetaData>();
      installs.add(installAspect);
      setInstalls(installs);
      
      AbstractInstallMetaData uninstallAspect = new AbstractInstallMetaData();
      uninstallAspect.setBean(aspect.getName());
      uninstallAspect.setMethodName("uninstall");

      List<InstallMetaData> uninstalls = getUninstalls();
      if (uninstalls == null)
         uninstalls = new ArrayList<InstallMetaData>();
      uninstalls.add(uninstallAspect);
      setUninstalls(uninstalls);
      
   }
   
   private void configureNoDependencies(AbstractBeanMetaData aspect/*, AbstractBeanMetaData aspectBinding*/)
   {
      aspect.addProperty(new AbstractPropertyMetaData("advice", new AbstractDependencyValueMetaData(name)));
   }
   
   
   private boolean hasInjectedBeans()
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
      Iterator children = node.getChildren();
      
      if (children != null)
      {
         while (children.hasNext())
         {
            MetaDataVisitorNode child = (MetaDataVisitorNode)children.next();
            if (child instanceof AbstractDependencyValueMetaData)
            {
               dependencies.add((AbstractDependencyValueMetaData)child);
            }
            getDependencies(dependencies, child);
         }
      }
   }
}
