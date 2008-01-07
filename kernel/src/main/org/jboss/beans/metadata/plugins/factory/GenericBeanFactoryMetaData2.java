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
package org.jboss.beans.metadata.plugins.factory;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlNsForm;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

import org.jboss.beans.metadata.plugins.AbstractAnnotationMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
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
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.util.JBossObject;
import org.jboss.xb.annotations.JBossXmlSchema;

/**
 * GenericBeanFactoryMetaData.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 59429 $
 */
@JBossXmlSchema(namespace="urn:jboss:bean-deployer:2.0", elementFormDefault=XmlNsForm.QUALIFIED)
@XmlRootElement(name="beanfactory")
@XmlType(propOrder={"aliases", "annotations", "classLoader", "constructor", "properties", "create", "start", "depends", "demands", "supplies", "installs", "uninstalls", "installCallbacks", "uninstallCallbacks"})
public class GenericBeanFactoryMetaData2 extends JBossObject implements BeanMetaDataFactory, Serializable
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

   protected Set<DependencyMetaData> depends;

   protected Set<DemandMetaData> demands;
   
   protected Set<SupplyMetaData> supplies;
   
   protected List<InstallMetaData> installs;
   
   protected List<InstallMetaData> uninstalls;
   
   protected List<CallbackMetaData> installCallbacks;
   
   protected List<CallbackMetaData> uninstallCallbacks;
   
   public String getName()
   {
      return name;
   }

   @XmlAttribute
   public void setName(String name)
   {
      this.name = name;
   }

   public String getBean()
   {
      return bean;
   }

   @XmlAttribute(name="class")
   public void setBean(String bean)
   {
      this.bean = bean;
   }

   public Set<Object> getAliases()
   {
      return aliases;
   }

   @XmlElement(name="alias", type=String.class)
   public void setAliases(Set<Object> aliases)
   {
      this.aliases = aliases;
   }

   public ControllerMode getMode()
   {
      return mode;
   }

   @XmlAttribute
   public void setMode(ControllerMode mode)
   {
      this.mode = mode;
   }

   public Set<AnnotationMetaData> getAnnotations()
   {
      return annotations;
   }

   @XmlElement(name="annotation", type=AbstractAnnotationMetaData.class)
   public void setAnnotations(Set<AnnotationMetaData> annotations)
   {
      this.annotations = annotations;
   }

   public ClassLoaderMetaData getClassLoader()
   {
      return classLoader;
   }

   @XmlElement(name="classloader", type=AbstractClassLoaderMetaData.class)
   public void setClassLoader(ClassLoaderMetaData classLoader)
   {
      this.classLoader = classLoader;
   }

   public ConstructorMetaData getConstructor()
   {
      return constructor;
   }

   @XmlElement(name="constructor", type=AbstractConstructorMetaData.class)
   public void setConstructor(ConstructorMetaData constructor)
   {
      this.constructor = constructor;
   }

   public LifecycleMetaData getCreate()
   {
      return create;
   }

   @XmlElement(name="create", type=AbstractLifecycleMetaData.class)
   public void setCreate(LifecycleMetaData create)
   {
      this.create = create;
   }

   public Set<PropertyMetaData> getProperties()
   {
      return properties;
   }

   @XmlElement(name="property", type=AbstractPropertyMetaData.class)
   public void setProperties(Set<PropertyMetaData> properties)
   {
      this.properties = properties;
   }

   public LifecycleMetaData getStart()
   {
      return start;
   }

   @XmlElement(name="start", type=AbstractLifecycleMetaData.class)
   public void setStart(LifecycleMetaData start)
   {
      this.start = start;
   }

   @XmlElement(name="demand", type=AbstractDemandMetaData.class)
   public Set<DemandMetaData> getDemands()
   {
      return demands;
   }

   public void setDemands(Set<DemandMetaData> demands)
   {
      this.demands = demands;
   }

   public Set<DependencyMetaData> getDepends()
   {
      return depends;
   }

   @XmlElement(name="depends", type=AbstractDependencyMetaData.class)
   public void setDepends(Set<DependencyMetaData> depends)
   {
      this.depends = depends;
   }

   public List<CallbackMetaData> getInstallCallbacks()
   {
      return installCallbacks;
   }

   @XmlElement(name="incallback", type=InstallCallbackMetaData.class)
   public void setInstallCallbacks(List<CallbackMetaData> installCallbacks)
   {
      this.installCallbacks = installCallbacks;
   }

   public List<InstallMetaData> getInstalls()
   {
      return installs;
   }

   @XmlElement(name="install", type=AbstractInstallMetaData.class)
   public void setInstalls(List<InstallMetaData> installs)
   {
      this.installs = installs;
   }

   public Set<SupplyMetaData> getSupplies()
   {
      return supplies;
   }

   @XmlElement(name="supply", type=AbstractSupplyMetaData.class)
   public void setSupplies(Set<SupplyMetaData> supplies)
   {
      this.supplies = supplies;
   }

   public List<CallbackMetaData> getUninstallCallbacks()
   {
      return uninstallCallbacks;
   }

   @XmlElement(name="uncallback", type=UninstallCallbackMetaData.class)
   public void setUninstallCallbacks(List<CallbackMetaData> uninstallCallbacks)
   {
      this.uninstallCallbacks = uninstallCallbacks;
   }

   public List<InstallMetaData> getUninstalls()
   {
      return uninstalls;
   }

   @XmlElement(name="uninstall", type=AbstractInstallMetaData.class)
   public void setUninstalls(List<InstallMetaData> uninstalls)
   {
      this.uninstalls = uninstalls;
   }

   public List<BeanMetaData> getBeans()
   {
      AbstractBeanMetaData gbf = new AbstractBeanMetaData();
      gbf.setName(name);
      gbf.setAliases(aliases);
      gbf.setBean(GenericBeanFactory.class.getName());
      gbf.setMode(mode);
      Set<PropertyMetaData> properties = new HashSet<PropertyMetaData>();
      gbf.setProperties(properties);
      properties.add(createProperty("bean", bean));
      properties.add(createProperty("classLoader", classLoader));
      properties.add(createProperty("constructor", constructor));
      properties.add(createMapProperty("properties", properties));
      properties.add(createProperty("start", start));
      properties.add(createProperty("create", create));
      gbf.setDemands(demands);
      gbf.setDepends(depends);
      gbf.setSupplies(supplies);
      gbf.setInstalls(installs);
      gbf.setUninstalls(uninstalls);
      gbf.setInstallCallbacks(installCallbacks);
      gbf.setUninstallCallbacks(uninstallCallbacks);
      return Collections.singletonList((BeanMetaData) gbf);
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