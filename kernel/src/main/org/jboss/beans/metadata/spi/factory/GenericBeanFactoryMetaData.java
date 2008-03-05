/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.beans.metadata.spi.factory;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.jboss.beans.metadata.plugins.AbstractAnnotationMetaData;
import org.jboss.beans.metadata.plugins.AbstractClassLoaderMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractDemandMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyMetaData;
import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.plugins.AbstractLifecycleMetaData;
import org.jboss.beans.metadata.plugins.AbstractMapMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractSupplyMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.InstallCallbackMetaData;
import org.jboss.beans.metadata.plugins.UninstallCallbackMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.beans.metadata.spi.DependencyMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.beans.metadata.spi.LifecycleMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.ParameterizedMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.beans.metadata.spi.builder.ParameterMetaDataBuilder;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.kernel.plugins.bootstrap.basic.KernelConstants;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.util.JBossObject;

/**
 * GenericBeanFactoryMetaData.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 59429 $
 */
@XmlRootElement(name="beanfactory")
@XmlType(name="beanfactoryType", propOrder={"aliases", "annotations", "classLoader", "constructor", "properties", "create", "start", "depends", "demands", "supplies", "installs", "uninstalls", "installCallbacks", "uninstallCallbacks"})
public class GenericBeanFactoryMetaData extends JBossObject implements BeanMetaDataFactory, Serializable
{
   private static final long serialVersionUID = 1L;
   
   /** The name */
   protected String name;
   
   /** The bean class name */
   protected String bean;

   /** The controller mode */
   protected ControllerMode mode;

   /** The annotations */
   protected Set<AnnotationMetaData> annotations;
   
   /** The aliases */
   protected Set<Object> aliases;

   /** The classloader */
   protected ClassLoaderMetaData classLoader;
   
   /** The constructor metadata */
   protected ConstructorMetaData constructor;
   
   /** The properties Map<propertyName, ValueMetaData> */
   protected Set<PropertyMetaData> properties;

   /** The create lifecycle method */
   protected LifecycleMetaData create;

   /** The start lifecycle method */
   protected LifecycleMetaData start;

   /** The depends */
   protected Set<DependencyMetaData> depends;

   /** The demands */
   protected Set<DemandMetaData> demands;
   
   /** The supplies */
   protected Set<SupplyMetaData> supplies;
   
   /** The installs */
   protected List<InstallMetaData> installs;
   
   /** The uninstalls */
   protected List<InstallMetaData> uninstalls;
   
   /** The install callbacks */
   protected List<CallbackMetaData> installCallbacks;
   
   /** The uninstall callbacks */
   protected List<CallbackMetaData> uninstallCallbacks;

   /**
    * Get the name
    * 
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * Set the bean name
    * 
    * @param name the name
    */
   @XmlAttribute
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Get the bean class
    * 
    * @return the bean class
    */
   public String getBean()
   {
      return bean;
   }

   /**
    * Set the bean class
    * 
    * @param bean the bean class
    */
   @XmlAttribute(name="class")
   public void setBean(String bean)
   {
      this.bean = bean;
   }

   /**
    * Get the aliases
    * 
    * @return the aliases
    */
   public Set<Object> getAliases()
   {
      return aliases;
   }

   /**
    * Set the aliases
    * 
    * @param aliases the aliases
    */
   @XmlElement(name="alias", type=String.class)
   public void setAliases(Set<Object> aliases)
   {
      this.aliases = aliases;
   }

   /**
    * Get the mode
    * 
    * @return the mode
    */
   public ControllerMode getMode()
   {
      return mode;
   }

   /**
    * Set the mode
    * 
    * @param mode the mode
    */
   @XmlAttribute
   public void setMode(ControllerMode mode)
   {
      this.mode = mode;
   }

   /**
    * Get the annotations
    * 
    * @return the annotations
    */
   public Set<AnnotationMetaData> getAnnotations()
   {
      return annotations;
   }

   /**
    * Set the annotations
    * 
    * @param annotations the annotations
    */
   @XmlElement(name="annotation", type=AbstractAnnotationMetaData.class)
   public void setAnnotations(Set<AnnotationMetaData> annotations)
   {
      this.annotations = annotations;
   }

   /**
    * Get the classloader
    * 
    * @return the classloader
    */
   public ClassLoaderMetaData getClassLoader()
   {
      return classLoader;
   }

   /**
    * Set the classloader
    * 
    * @param classLoader the classloader
    */
   @XmlElement(name="classloader", type=AbstractClassLoaderMetaData.class)
   public void setClassLoader(ClassLoaderMetaData classLoader)
   {
      this.classLoader = classLoader;
   }

   /**
    * Get the constructor
    * 
    * @return the constructor
    */
   public ConstructorMetaData getConstructor()
   {
      return constructor;
   }

   /**
    * Set the constructor
    * 
    * @param constructor the constructor
    */
   @XmlElement(name="constructor", type=AbstractConstructorMetaData.class)
   public void setConstructor(ConstructorMetaData constructor)
   {
      this.constructor = constructor;
   }

   /**
    * Get the create
    * 
    * @return the create
    */
   public LifecycleMetaData getCreate()
   {
      return create;
   }

   /**
    * Set the create
    * 
    * @param create the create
    */
   @XmlElement(name="create", type=AbstractLifecycleMetaData.class)
   public void setCreate(LifecycleMetaData create)
   {
      this.create = create;
   }

   /**
    * Get the properties
    * 
    * @return the properties
    */
   public Set<PropertyMetaData> getProperties()
   {
      return properties;
   }

   /**
    * Set the properties
    * 
    * @param properties the properties
    */
   @XmlElement(name="property", type=AbstractPropertyMetaData.class)
   public void setProperties(Set<PropertyMetaData> properties)
   {
      this.properties = properties;
   }

   /**
    * Get the start
    * 
    * @return the start
    */
   public LifecycleMetaData getStart()
   {
      return start;
   }

   /**
    * Set the start
    * 
    * @param start the start
    */
   @XmlElement(name="start", type=AbstractLifecycleMetaData.class)
   public void setStart(LifecycleMetaData start)
   {
      this.start = start;
   }

   /**
    * Get the demans
    * 
    * @return the demands
    */
   @XmlElement(name="demand", type=AbstractDemandMetaData.class)
   public Set<DemandMetaData> getDemands()
   {
      return demands;
   }

   /**
    * Set the demands
    * 
    * @param demands the demands
    */
   public void setDemands(Set<DemandMetaData> demands)
   {
      this.demands = demands;
   }

   /**
    * Get the dependencies
    * 
    * @return the dependencies
    */
   public Set<DependencyMetaData> getDepends()
   {
      return depends;
   }

   /**
    * Set the dependencies
    * 
    * @param depends the dependencies
    */
   @XmlElement(name="depends", type=AbstractDependencyMetaData.class)
   public void setDepends(Set<DependencyMetaData> depends)
   {
      this.depends = depends;
   }

   /**
    * Get the install callbacks
    * 
    * @return the install callbacks
    */
   public List<CallbackMetaData> getInstallCallbacks()
   {
      return installCallbacks;
   }

   /**
    * Set the install callbacks
    * 
    * @param installCallbacks the install callbacks
    */
   @XmlElement(name="incallback", type=InstallCallbackMetaData.class)
   public void setInstallCallbacks(List<CallbackMetaData> installCallbacks)
   {
      this.installCallbacks = installCallbacks;
   }

   /**
    * Get the installs
    * 
    * @return the installs
    */
   public List<InstallMetaData> getInstalls()
   {
      return installs;
   }

   /**
    * Set the installs
    * 
    * @param installs the installs
    */
   @XmlElement(name="install", type=AbstractInstallMetaData.class)
   public void setInstalls(List<InstallMetaData> installs)
   {
      this.installs = installs;
   }

   /**
    * Get the supples
    * 
    * @return the supplies
    */
   public Set<SupplyMetaData> getSupplies()
   {
      return supplies;
   }

   /**
    * Set the supplies
    * 
    * @param supplies the supplies
    */
   @XmlElement(name="supply", type=AbstractSupplyMetaData.class)
   public void setSupplies(Set<SupplyMetaData> supplies)
   {
      this.supplies = supplies;
   }

   /**
    * Get the uninstall callbacks
    * 
    * @return the uninstall callbacks
    */
   public List<CallbackMetaData> getUninstallCallbacks()
   {
      return uninstallCallbacks;
   }

   /**
    * Set the uninstall callbacks
    * 
    * @param uninstallCallbacks the uninstall callbacls
    */
   @XmlElement(name="uncallback", type=UninstallCallbackMetaData.class)
   public void setUninstallCallbacks(List<CallbackMetaData> uninstallCallbacks)
   {
      this.uninstallCallbacks = uninstallCallbacks;
   }

   /**
    * Get the uninstalls
    * 
    * @return the uninstalls
    */
   public List<InstallMetaData> getUninstalls()
   {
      return uninstalls;
   }

   /**
    * Set the uninstalls
    * 
    * @param uninstalls the uninstalls
    */
   @XmlElement(name="uninstall", type=AbstractInstallMetaData.class)
   public void setUninstalls(List<InstallMetaData> uninstalls)
   {
      this.uninstalls = uninstalls;
   }

   public List<BeanMetaData> getBeans()
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder(name, GenericBeanFactory.class.getName());
      builder.setAliases(aliases);
      builder.setMode(mode);
      ValueMetaData injectKernelConfigurator = builder.createInject(KernelConstants.KERNEL_CONFIGURATOR_NAME);
      builder.addConstructorParameter(KernelConfigurator.class.getName(), injectKernelConfigurator);
      builder.addPropertyMetaData("bean", bean);
      builder.addPropertyMetaData("constructor", constructor);
      builder.addPropertyMetaData("start", start);
      builder.addPropertyMetaData("create", create);
      if (demands != null && demands.size() > 0)
      {
         for (DemandMetaData demand : demands)
         {
            builder.addDemand(demand.getDemand());
         }
      }
      if (depends != null && depends.size() > 0)
      {
         for (DependencyMetaData dependency : depends)
         {
            builder.addDependency(dependency.getDependency());
         }
      }
      if (supplies != null && supplies.size() > 0)
      {
         for (SupplyMetaData supply : supplies)
         {
            builder.addSupply(supply.getSupply());
         }
      }
      if (installs != null && installs.size() > 0)
      {
         for (InstallMetaData install : installs)
         {
            ParameterMetaDataBuilder paramBuilder = builder.addInstallWithParameters(install.getMethodName(), install.getBean(), install.getState(), install.getDependentState());
            setParameters(paramBuilder, install);
         }
      }
      if (uninstalls != null && uninstalls.size() > 0)
      {
         for (InstallMetaData uninstall : uninstalls)
         {
            ParameterMetaDataBuilder paramBuilder = builder.addUninstallWithParameters(uninstall.getMethodName(), uninstall.getBean(), uninstall.getState(), uninstall.getDependentState());
            setParameters(paramBuilder, uninstall);
         }
      }
      if (properties != null && properties.size() > 0)
      {
         HashMap<String, ValueMetaData> propertyMap = new HashMap<String, ValueMetaData>(); 
         for (PropertyMetaData property : properties)
         {
            propertyMap.put(property.getName(), property.getValue());
         }
      }
      //TODO: installCallbacks and uninstallCallbacks
      
      return Collections.singletonList(builder.getBeanMetaData());
   }
   
//   public List<BeanMetaData> newGetBeans()
//   {
//      AbstractBeanMetaData gbf = new AbstractBeanMetaData();
//      gbf.setName(name);
//      gbf.setAliases(aliases);
//      gbf.setBean(GenericBeanFactory.class.getName());
//      gbf.setMode(mode);
//      Set<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
//      gbf.setProperties(properties);
//      if (this.properties != null)
//      {
//         properties.addAll(this.properties);
//      }
//      properties.add(createProperty("bean", bean));
//      properties.add(createProperty("classLoader", classLoader));
//      properties.add(createProperty("constructor", constructor));
//      properties.add(createMapProperty("properties", properties));
//      properties.add(createProperty("start", start));
//      properties.add(createProperty("create", create));
//      gbf.setDemands(demands);
//      gbf.setDepends(depends);
//      gbf.setSupplies(supplies);
//      gbf.setInstalls(installs);
//      gbf.setUninstalls(uninstalls);
//      gbf.setInstallCallbacks(installCallbacks);
//      gbf.setUninstallCallbacks(uninstallCallbacks);
//      return Collections.singletonList((BeanMetaData) gbf);
//   }

   /**
    * Add the parameters
    */
   private void setParameters(ParameterMetaDataBuilder builder, ParameterizedMetaData metadata)
   {
      List<ParameterMetaData> params = metadata.getParameters();
      if (params != null && params.size() > 0)
      {
         for (ParameterMetaData param : params)
         {
            builder.addParameterMetaData(param.getType(), param.getValue());
         }
      }
   }
   /**
    * Create property.
    *
    * @param name the property name
    * @param value the value
    * @return property meta data
    */
   protected PropertyMetaData createProperty(String name, Object value)
   {
      return new AbstractPropertyMetaData(name, value);
   }

   /**
    * Create property with map value.
    *
    * @param name the property name
    * @param properties the properties
    * @return property meta data
    */
   protected PropertyMetaData createMapProperty(String name, Set<PropertyMetaData> properties)
   {
      AbstractMapMetaData map = new AbstractMapMetaData();
      if (properties != null)
      {
         for (PropertyMetaData property : properties)
            map.put(new AbstractValueMetaData(property.getName()), new AbstractValueMetaData(property));
      }
      return new AbstractPropertyMetaData(name, map);
   }
}
