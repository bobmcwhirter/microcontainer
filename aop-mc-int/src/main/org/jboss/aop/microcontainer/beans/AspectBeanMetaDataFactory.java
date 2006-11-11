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
package org.jboss.aop.microcontainer.beans;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.ThisValueMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;

/**
 * AspectBeanMetaDataFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AspectBeanMetaDataFactory extends GenericBeanFactoryMetaData implements BeanMetaDataFactory
{
   /** Unless specified use the bean with this name as the aspect manager */
   final static String DEFAULT_ASPECT_MANAGER = "AspectManager";
   
   /** The pointcut */
   private String pointcut;
   
   /** The bean name of the aspect manager to use */
   private String managerBean = DEFAULT_ASPECT_MANAGER;
   
   /** The property of the aspect manager bean, if any, containing the aspect manager */
   private String managerProperty;
   
   /**
    * Get the pointcut.
    * 
    * @return the pointcut.
    */
   public String getPointcut()
   {
      return pointcut;
   }

   /**
    * Set the pointcut.
    * 
    * @param pointcut the pointcut.
    */
   public void setPointcut(String pointcut)
   {
      this.pointcut = pointcut;
   }


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
   
   public List<BeanMetaData> getBeans()
   {
      ArrayList<BeanMetaData> result = new ArrayList<BeanMetaData>();


      result.add(this);
      
      String aspectName = name + "$Aspect";
      AbstractBeanMetaData aspect = new AbstractBeanMetaData();
      aspect.setName(aspectName);
      aspect.setBean("org.jboss.aop.microcontainer.beans.Aspect");
      aspect.addProperty(getAspectManagerPropertyMetaData("manager"));
      result.add(aspect);
      
      String aspectBindingName = name + "$AspectBinding";
      AbstractBeanMetaData aspectBinding = new AbstractBeanMetaData();
      aspectBinding.setName(aspectBindingName);
      aspectBinding.setBean("org.jboss.aop.microcontainer.beans.AspectBinding");
      aspectBinding.addProperty(new AbstractPropertyMetaData("pointcut", pointcut));
      aspectBinding.addProperty(new AbstractPropertyMetaData("aspect", new AbstractDependencyValueMetaData(aspectName, "definition")));
      aspectBinding.addProperty(getAspectManagerPropertyMetaData("manager"));
      result.add(aspectBinding);
      
      if (hasInjectedBeans())
      {
         configureWithDependencies(aspect, aspectBinding);
      }
      else
      {
         configureNoDependencies(aspect, aspectBinding);
      }
      
      
      return result;
   }
   
   protected PropertyMetaData getAspectManagerPropertyMetaData(String name)
   {
      return new AbstractPropertyMetaData(name, new AbstractDependencyValueMetaData(managerBean, managerProperty));
   }
   
   private void configureWithDependencies(AbstractBeanMetaData aspect, AbstractBeanMetaData aspectBinding)
   {
      aspect.addProperty(new AbstractPropertyMetaData("adviceBean", name));
      
      AbstractInstallMetaData installAspect = new AbstractInstallMetaData();
      installAspect.setBean(aspect.getName());
      installAspect.setMethodName("install");
      ArrayList<ParameterMetaData> parameters = new ArrayList<ParameterMetaData>();
      parameters.add(new AbstractParameterMetaData(new ThisValueMetaData()));
      installAspect.setParameters(parameters);
      
      AbstractInstallMetaData installBinding = new AbstractInstallMetaData();
      installBinding.setBean(aspectBinding.getName());
      installBinding.setMethodName("rebind");
      parameters = new ArrayList<ParameterMetaData>();
      parameters.add(new AbstractParameterMetaData(new AbstractDependencyValueMetaData(aspect.getName(), "definition")));
      installBinding.setParameters(parameters);

      List<InstallMetaData> installs = getInstalls();
      if (installs == null)
         installs = new ArrayList<InstallMetaData>();
      installs.add(installAspect);
      installs.add(installBinding);
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
   
   private void configureNoDependencies(AbstractBeanMetaData aspect, AbstractBeanMetaData aspectBinding)
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
