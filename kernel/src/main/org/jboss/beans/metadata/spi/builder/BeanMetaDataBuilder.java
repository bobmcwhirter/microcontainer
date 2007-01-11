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
package org.jboss.beans.metadata.spi.builder;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractClassLoaderMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractDemandMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyMetaData;
import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractSupplyMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.builder.CreateLifecycleMetaDataBuilder;
import org.jboss.beans.metadata.plugins.builder.DestroyLifecycleMetaDataBuilder;
import org.jboss.beans.metadata.plugins.builder.LifecycleMetaDataBuilder;
import org.jboss.beans.metadata.plugins.builder.ParameterMetaDataBuilder;
import org.jboss.beans.metadata.plugins.builder.StartLifecycleMetaDataBuilder;
import org.jboss.beans.metadata.plugins.builder.StopLifecycleMetaDataBuilder;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.beans.metadata.spi.DependencyMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.dependency.spi.ControllerMode;

/**
 * Helper class.
 * Similar to StringBuffer, methods return current instance of BeanMetaDataBuilder.
 *
 * TODO - add on demand, when building OSGi, Spring, ...
 * TODO - install/uninstall parametrers
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BeanMetaDataBuilder
{
   private AbstractBeanMetaData beanMetaData;
   // parameter builders
   private ParameterMetaDataBuilder<AbstractConstructorMetaData> constructorBuilder;
   private LifecycleMetaDataBuilder createBuilder;
   private LifecycleMetaDataBuilder startBuilder;
   private LifecycleMetaDataBuilder stopBuilder;
   private LifecycleMetaDataBuilder destroyBuilder;

   public BeanMetaDataBuilder(String bean)
   {
      this(new AbstractBeanMetaData(bean));
   }

   public BeanMetaDataBuilder(String name, String bean)
   {
      this(new AbstractBeanMetaData(name, bean));
   }

   public BeanMetaDataBuilder(AbstractBeanMetaData beanMetaData)
   {
      this.beanMetaData = beanMetaData;
      // lifecycle builders
      createBuilder = new CreateLifecycleMetaDataBuilder(beanMetaData);
      startBuilder = new StartLifecycleMetaDataBuilder(beanMetaData);
      stopBuilder = new StopLifecycleMetaDataBuilder(beanMetaData);
      destroyBuilder = new DestroyLifecycleMetaDataBuilder(beanMetaData);
   }

   public BeanMetaData getBeanMetaData()
   {
      return beanMetaData;
   }

   public BeanMetaDataBuilder setMode(String modeString)
   {
      return setMode(new ControllerMode(modeString));
   }

   public BeanMetaDataBuilder setMode(ControllerMode mode)
   {
      beanMetaData.setMode(mode);
      return this;
   }

   public BeanMetaDataBuilder setClassLoader(Object classLoader)
   {
      beanMetaData.setClassLoader(new AbstractClassLoaderMetaData(new AbstractValueMetaData(classLoader)));
      return this;
   }

   public BeanMetaDataBuilder addConstructorParameter(String type, Object value)
   {
      AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) beanMetaData.getConstructor();
      if (constructor == null)
      {
         constructor = new AbstractConstructorMetaData();
         beanMetaData.setConstructor(constructor);
         constructorBuilder = new ParameterMetaDataBuilder<AbstractConstructorMetaData>(constructor);
      }
      constructorBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addPropertyMetaData(String name, Object value)
   {
      Set<PropertyMetaData> properties = beanMetaData.getProperties();
      if (properties == null)
      {
         properties = new HashSet<PropertyMetaData>();
         beanMetaData.setProperties(properties);
      }
      properties.add(new AbstractPropertyMetaData(name, value));
      return this;
   }

   public BeanMetaDataBuilder setCreate(String methodName)
   {
      createBuilder.createLifecycleMetaData(methodName);
      return this;
   }

   public BeanMetaDataBuilder addCreateParameter(String type, Object value)
   {
      createBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder setStart(String methodName)
   {
      startBuilder.createLifecycleMetaData(methodName);
      return this;
   }

   public BeanMetaDataBuilder addStartParameter(String type, Object value)
   {
      startBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder setStop(String methodName)
   {
      stopBuilder.createLifecycleMetaData(methodName);
      return this;
   }

   public BeanMetaDataBuilder addStopParameter(String type, Object value)
   {
      stopBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder setDestroy(String methodName)
   {
      destroyBuilder.createLifecycleMetaData(methodName);
      return this;
   }

   public BeanMetaDataBuilder addDestroyParameter(String type, Object value)
   {
      destroyBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addSupply(Object supply)
   {
      Set<SupplyMetaData> supplies = beanMetaData.getSupplies();
      if (supplies == null)
      {
         supplies = new HashSet<SupplyMetaData>();
         beanMetaData.setSupplies(supplies);
      }
      supplies.add(new AbstractSupplyMetaData(supply));
      return this;
   }

   public BeanMetaDataBuilder addDemand(Object demand)
   {
      Set<DemandMetaData> demands = beanMetaData.getDemands();
      if (demands == null)
      {
         demands = new HashSet<DemandMetaData>();
         beanMetaData.setDemands(demands);
      }
      demands.add(new AbstractDemandMetaData(demand));
      return this;
   }

   public BeanMetaDataBuilder addDependency(Object dependency)
   {
      Set<DependencyMetaData> dependencies = beanMetaData.getDepends();
      if (dependencies == null)
      {
         dependencies = new HashSet<DependencyMetaData>();
         beanMetaData.setDepends(dependencies);
      }
      dependencies.add(new AbstractDependencyMetaData(dependency));
      return this;
   }

   public BeanMetaDataBuilder addInstall(String methodName)
   {
      List<InstallMetaData> installs = beanMetaData.getInstalls();
      if (installs == null)
      {
         installs = new ArrayList<InstallMetaData>();
         beanMetaData.setInstalls(installs);
      }
      AbstractInstallMetaData installMetaData = new AbstractInstallMetaData();
      installMetaData.setMethodName(methodName);
      installs.add(installMetaData);
      return this;
   }

   public BeanMetaDataBuilder addUninstall(String methodName)
   {
      List<InstallMetaData> uninstalls = beanMetaData.getUninstalls();
      if (uninstalls == null)
      {
         uninstalls = new ArrayList<InstallMetaData>();
         beanMetaData.setUninstalls(uninstalls);
      }
      AbstractInstallMetaData uninstallMetaData = new AbstractInstallMetaData();
      uninstallMetaData.setMethodName(methodName);
      uninstalls.add(uninstallMetaData);
      return this;
   }

}
