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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.plugins.AbstractAliasMetaData;
import org.jboss.beans.metadata.plugins.AbstractAnnotationMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractClassLoaderMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractDemandMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyMetaData;
import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.plugins.AbstractLifecycleMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractSupplyMetaData;
import org.jboss.beans.metadata.plugins.InstallCallbackMetaData;
import org.jboss.beans.metadata.plugins.UninstallCallbackMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.beans.metadata.spi.AliasMetaData;
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
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.kernel.plugins.bootstrap.basic.KernelConstants;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.util.JBossObject;

/**
 * GenericBeanFactoryMetaData. Used to configure, or parse a <code>&lt;beanfactory&gt;</code>
 * element into, this {@link BeanMetaDataFactory} that can be used to obtain
 * the bean factories {@link BeanMetaData} so it can be deployed into the {@link KernelController}.
 * The resulting bean will be an instance of a {@link BeanFactory}. 
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
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

   /** The bean factory class name */
   protected String factoryClass;

   /** The access mode */
   protected BeanAccessMode accessMode;

   /** The controller mode */
   protected ControllerMode mode;

   /** The annotations */
   protected Set<AnnotationMetaData> annotations;
   
   /** The aliases */
   protected Set<AliasMetaData> aliases;

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

   public GenericBeanFactoryMetaData()
   {
   }

   public GenericBeanFactoryMetaData(String name, String bean)
   {
      this.name = name;
      this.bean = bean;
   }

   /**
    * Get the name of the bean factory
    * 
    * @see BeanMetaData#getName(String)
    * @return the name
    */
   public String getName()
   {
      return name;
   }

   /**
    * Set the bean factory name
    * 
    * @see BeanMetaData#setName(String)
    * @param name the name
    */
   @XmlAttribute
   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Get the bean class. This is the type of object that will be 
    * created by the {@link BeanFactory} once this metadata has been
    * converted into a {@link BeanFactory} following the call to
    * {@link #getBeans()} and installing it into the {@link KernelController}
    * 
    * @see BeanMetaData#getBean()
    * @return the bean class
    */
   public String getBean()
   {
      return bean;
   }

   /**
    * Set the bean class. This is the type of object that will be 
    * created by the {@link BeanFactory} once this metadata has been
    * converted into a {@link BeanFactory} following the call to
    * {@link #getBeans()} and installing it into the {@link KernelController} 
    * 
    * @see BeanMetaData#getBean()
    * @param bean the bean class
    */
   @XmlAttribute(name="class")
   public void setBean(String bean)
   {
      this.bean = bean;
   }

   /**
    * Get the factory class.
    *
    * @return the factory class
    */
   public String getFactoryClass()
   {
      return factoryClass;
   }

   /**
    * Set the factory class.
    * Note: this class param must either extend GenericBeanFactory
    * or have the same constructor and properties aka 'callbacks'.
    * If not set a default implementation will be used.
    *
    * @param factoryClass the factory class
    */
   @XmlAttribute(name="factoryClass")
   public void setFactoryClass(String factoryClass)
   {
      this.factoryClass = factoryClass;
   }

   /**
    * Get the aliases
    * 
    * @see BeanMetaData#getAliases()
    * @return the aliases
    */
   public Set<AliasMetaData> getAliases()
   {
      return aliases;
   }

   /**
    * Set the aliases
    * 
    * @see BeanMetaData#getAliases()
    * @param aliases the aliases
    */
   @XmlElement(name="alias", type=AbstractAliasMetaData.class)
   public void setAliases(Set<AliasMetaData> aliases)
   {
      this.aliases = aliases;
   }

   /**
    * Get the access mode.
    *
    * @see BeanMetaData#getAccessMode()
    * @return the access mode
    */
   public BeanAccessMode getAccessMode()
   {
      return accessMode;
   }

   /**
    * Set the access mode.
    *
    * @see BeanMetaData#getAccessMode()
    * @param accessMode the access mode
    */
   @XmlAttribute(name = "access-mode")
   public void setAccessMode(BeanAccessMode accessMode)
   {
      this.accessMode = accessMode;
   }

   /**
    * Get the mode
    * 
    * @see BeanMetaData#getMode()
    * @return the mode
    */
   public ControllerMode getMode()
   {
      return mode;
   }

   /**
    * Set the mode
    * 
    * @see BeanMetaData#getMode()
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
    * @see BeanMetaData#getAnnotations()
    * @return the annotations
    */
   public Set<AnnotationMetaData> getAnnotations()
   {
      return annotations;
   }

   /**
    * Set the annotations
    * 
    * @see BeanMetaData#getAnnotations()
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
    * @see BeanMetaData#getClassLoader()
    * @return the classloader
    */
   public ClassLoaderMetaData getClassLoader()
   {
      return classLoader;
   }

   /**
    * Set the classloader
    * 
    * @see BeanMetaData#getClassLoader()
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
    * @see BeanMetaData#getConstructor()
    * @return the constructor
    */
   public ConstructorMetaData getConstructor()
   {
      return constructor;
   }

   /**
    * Set the constructor
    * 
    * @see BeanMetaData#getConstructor()
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
    * @see BeanMetaData#getCreate()
    * @return the create
    */
   public LifecycleMetaData getCreate()
   {
      return create;
   }

   /**
    * Set the create
    * 
    * @see BeanMetaData#getCreate()
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
    * @see BeanMetaData#getProperties()
    * @return the properties
    */
   public Set<PropertyMetaData> getProperties()
   {
      return properties;
   }

   /**
    * Set the properties
    * 
    * @see BeanMetaData#getProperties()
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
    * @see BeanMetaData#getStart()
    * @return the start
    */
   public LifecycleMetaData getStart()
   {
      return start;
   }

   /**
    * Set the start
    * 
    * @see BeanMetaData#getStart()
    * @param start the start
    */
   @XmlElement(name="start", type=AbstractLifecycleMetaData.class)
   public void setStart(LifecycleMetaData start)
   {
      this.start = start;
   }

   /**
    * Get the demands
    * 
    * @see BeanMetaData#getDemands()
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
    * @see BeanMetaData#getDemands()
    * @param demands the demands
    */
   public void setDemands(Set<DemandMetaData> demands)
   {
      this.demands = demands;
   }

   /**
    * Get the dependencies
    * 
    * @see BeanMetaData#getDepends()
    * @return the dependencies
    */
   public Set<DependencyMetaData> getDepends()
   {
      return depends;
   }

   /**
    * Set the dependencies
    * 
    * @see BeanMetaData#getDepends()
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
    * @see BeanMetaData#getInstallCallbacks()
    * @return the install callbacks
    */
   public List<CallbackMetaData> getInstallCallbacks()
   {
      return installCallbacks;
   }

   /**
    * Set the install callbacks
    * 
    * @see BeanMetaData#getInstallCallbacks()
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
    * @see BeanMetaData#getInstalls()
    * @return the installs
    */
   public List<InstallMetaData> getInstalls()
   {
      return installs;
   }

   /**
    * Set the installs
    * 
    * @see BeanMetaData#getInstalls()
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
    * @see BeanMetaData#getSupplies()
    * @return the supplies
    */
   public Set<SupplyMetaData> getSupplies()
   {
      return supplies;
   }

   /**
    * Set the supplies
    * 
    * @see BeanMetaData#getSupplies()
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
    * @see BeanMetaData#getUninstallCallbacks()
    * @return the uninstall callbacks
    */
   public List<CallbackMetaData> getUninstallCallbacks()
   {
      return uninstallCallbacks;
   }

   /**
    * Set the uninstall callbacks
    * 
    * @see BeanMetaData#getUninstallCallbacks()
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
    * @see BeanMetaData#getUninstalls()
    * @return the uninstalls
    */
   public List<InstallMetaData> getUninstalls()
   {
      return uninstalls;
   }

   /**
    * Set the uninstalls
    * 
    * @see BeanMetaData#getUninstalls()
    * @param uninstalls the uninstalls
    */
   @XmlElement(name="uninstall", type=AbstractInstallMetaData.class)
   public void setUninstalls(List<InstallMetaData> uninstalls)
   {
      this.uninstalls = uninstalls;
   }

   @XmlTransient
   public List<BeanMetaData> getBeans()
   {
      return Collections.singletonList(getBeanMetaData());
   }

   /**
    * Get the bean metadata for the {@link BeanFactory} created by this bean factory metadata
    * 
    * @see BeanMetaData#getUninstalls()
    * @param uninstalls the uninstalls
    */
   @XmlTransient
   public BeanMetaData getBeanMetaData()
   {
      if (getBean() == null)
      {
         ConstructorMetaData constructor = getConstructor();
         if (constructor == null)
            throw new IllegalArgumentException("BeanFactory should have a class attribute or a constructor element.");
         if (constructor.getFactoryMethod() == null)
            throw new IllegalArgumentException("BeanFactory should have a class attribute or the constructor element should have a factoryMethod attribute.");
         if (constructor.getFactory() == null && constructor.getFactoryClass() == null)
            throw new IllegalArgumentException("BeanFactory should have a class attribute or the constructor element should have a either a factoryClass attribute or a factory element.");
      }

      if (getFactoryClass() == null)
         setFactoryClass(GenericBeanFactory.class.getName());

      AbstractBeanMetaData beanMetaData = new AbstractBeanMetaData(name, getFactoryClass());
      beanMetaData.setAnnotations(getAnnotations());
      beanMetaData.setDemands(getDemands());
      beanMetaData.setDepends(getDepends());
      beanMetaData.setSupplies(getSupplies());
      beanMetaData.setInstalls(getInstalls());
      beanMetaData.setUninstalls(getUninstalls());
      beanMetaData.setInstallCallbacks(getInstallCallbacks());
      beanMetaData.setUninstallCallbacks(getUninstallCallbacks());
      // builder util
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder(beanMetaData);
      if (aliases != null)
      {
         Set<Object> theAliases = new HashSet<Object>();
         for (AliasMetaData alias : aliases)
            theAliases.add(alias.getAliasValue());
         builder.setAliases(theAliases);
      }
      builder.setMode(mode);
      ValueMetaData injectKernelConfigurator = builder.createInject(KernelConstants.KERNEL_CONFIGURATOR_NAME);
      builder.addConstructorParameter(KernelConfigurator.class.getName(), injectKernelConfigurator);
      if (bean != null)
      {
         builder.addPropertyMetaData("bean", bean);
         // add bean as related class
         builder.addRelatedClass(bean);
      }
      if (classLoader != null)
      {
         builder.setClassLoader(classLoader);
         builder.addPropertyMetaData("classLoader", builder.createValue(classLoader));
      }
      if (accessMode != null)
         builder.addPropertyMetaData("accessMode", accessMode);
      if (constructor != null)
         builder.addPropertyMetaData("constructor", constructor);
      if (create != null)
         builder.addPropertyMetaData("create", create);
      if (start != null)
         builder.addPropertyMetaData("start", start);
      if (properties != null && properties.size() > 0)
      {
         PropertyMap propertyMap = new PropertyMap(); 
         for (PropertyMetaData property : properties)
         {
            propertyMap.put(property.getName(), property.getValue());
         }
         builder.addPropertyMetaData("properties", propertyMap);
      }
      return builder.getBeanMetaData();
   }
}