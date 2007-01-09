/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.beans.metadata.plugins;

import java.io.Serializable;
import java.util.*;

import org.jboss.beans.metadata.spi.*;
import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Metadata for a bean.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractBeanMetaData extends AbstractFeatureMetaData
   implements BeanMetaData, BeanMetaDataFactory, Serializable
{
   private static final long serialVersionUID = 1L;

   /** The bean fully qualified class name */
   protected String bean;

   /** The name of this instance */
   protected String name;

   /** The mode */
   protected ControllerMode mode;

   /** The properties configuration Set<PropertyMetaData> */
   private Set<PropertyMetaData> properties;

   /** The bean ClassLoader */
   protected ClassLoaderMetaData classLoader;

   /** The constructor */
   protected ConstructorMetaData constructor;

   /** The create lifecycle */
   protected LifecycleMetaData create;

   /** The start lifecycle */
   protected LifecycleMetaData start;

   /** The stop lifecycle */
   protected LifecycleMetaData stop;

   /** The destroy lifecycle */
   protected LifecycleMetaData destroy;

   /** What the bean demands Set<DemandMetaData> */
   protected Set<DemandMetaData> demands;

   /** What the bean supplies Set<SupplyMetaData> */
   protected Set<SupplyMetaData> supplies;

   /** What the bean dependencies Set<DependencyMetaData> */
   protected Set<DependencyMetaData> depends;

   /** The install operations List<InstallMetaData> */
   protected List<InstallMetaData> installs;

   /** The uninstall operations List<InstallMetaData> */
   protected List<InstallMetaData> uninstalls;

   /** The kernel controller */
   protected KernelController controller;

   /**
    * Create a new bean meta data
    */
   public AbstractBeanMetaData()
   {
      super();
   }

   /**
    * Create a new bean meta data
    * 
    * @param bean the bean class name
    */
   public AbstractBeanMetaData(String bean)
   {
      this.bean = bean;
   }
   /**
    * Create a new bean meta data
    * 
    * @param name the name
    * @param bean the bean class name
    */
   public AbstractBeanMetaData(String name, String bean)
   {
      this.name = name;
      this.bean = bean;
   }

   public List<BeanMetaData> getBeans()
   {
      List<BeanMetaData> nestedBeans = findNestedBeans();
      if (nestedBeans.isEmpty())
      {
         return Collections.singletonList((BeanMetaData)this);
      }
      else
      {
         nestedBeans.add(this);
         return nestedBeans;
      }
   }

   protected List<BeanMetaData> findNestedBeans()
   {
      List<BeanMetaData> allBeans = new ArrayList<BeanMetaData>();
      addBeans(this, allBeans);
      return allBeans;
   }

   protected void addBeans(MetaDataVisitorNode current, List<BeanMetaData> list)
   {
      for(Iterator<? extends MetaDataVisitorNode> children = current.getChildren(); children != null && children.hasNext();)
      {
         MetaDataVisitorNode next = children.next();
         if (next instanceof BeanMetaDataFactory)
         {
            list.addAll(((BeanMetaDataFactory) next).getBeans());
         }
         else
         {
            addBeans(next, list);
            if (next instanceof BeanMetaData)
            {
               list.add((BeanMetaData) current);
            }
         }
      }
   }

   /**
    * Get the bean class name.
    * @return the fully qualified bean class name.
    */
   public String getBean()
   {
      return bean;
   }

   /**
    * Set the bean class name and flush the object cache.
    * 
    * @param bean The bean class name to set.
    */
   public void setBean(String bean)
   {
      this.bean = bean;
      flushJBossObjectCache();
   }

   /**
    * Get a property
    * 
    * @param name the name
    * @return the property name
    */
   public PropertyMetaData getProperty(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      if (properties != null && properties.size() > 0)
      {
         for (Iterator i = properties.iterator(); i.hasNext();)
         {
            AbstractPropertyMetaData prop = (AbstractPropertyMetaData) i.next();
            if (name.equals(prop.getName()))
               return prop;
         }
      }
      return null;
   }

   /**
    * Add a property
    * 
    * @param property the property
    */
   public void addProperty(PropertyMetaData property)
   {
      if (property == null)
         throw new IllegalArgumentException("Null property");
      if (properties == null)
         properties = new HashSet<PropertyMetaData>();
      properties.add(property);
      flushJBossObjectCache();
   }

   /**
    * Set the propertiess.
    * 
    * @param properties Set<PropertyMetaData>
    */
   public void setProperties(Set<PropertyMetaData> properties)
   {
      this.properties = properties;
      flushJBossObjectCache();
   }

   public ClassLoaderMetaData getClassLoader()
   {
      return classLoader;
   }

   public void setClassLoader(ClassLoaderMetaData classLoader)
   {
      this.classLoader = classLoader;
   }

   /**
    * Set the constructor
    * 
    * @param constructor the constructor metadata
    */
   public void setConstructor(ConstructorMetaData constructor)
   {
      this.constructor = constructor;
   }

   /**
    * Set what the bean demands.
    * 
    * @param demands Set<DemandMetaData>
    */
   public void setDemands(Set<DemandMetaData> demands)
   {
      this.demands = demands;
      flushJBossObjectCache();
   }

   /**
    * Set what the bean supplies.
    * 
    * @param supplies Set<SupplyMetaData>
    */
   public void setSupplies(Set<SupplyMetaData> supplies)
   {
      this.supplies = supplies;
      flushJBossObjectCache();
   }

   /**
    * Set what the bean depends.
    * 
    * @param depends Set<DependencyMetaData>
    */
   public void setDepends(Set<DependencyMetaData> depends)
   {
      this.depends = depends;
      flushJBossObjectCache();
   }

   public String getName()
   {
      return name;
   }

   /**
    * Set the name.
    * 
    * @param name The name to set.
    */
   public void setName(String name)
   {
      this.name = name;
      flushJBossObjectCache();
   }

   public ControllerMode getMode()
   {
      return mode;
   }

   public void setMode(ControllerMode mode)
   {
      this.mode = mode;
      flushJBossObjectCache();
   }

   public Set<PropertyMetaData> getProperties()
   {
      return properties;
   }

   public ConstructorMetaData getConstructor()
   {
      return constructor;
   }

   public LifecycleMetaData getCreate()
   {
      return create;
   }

   /**
    * Set the lifecycle metadata
    * 
    * @param lifecycle the lifecycle metadata
    */
   public void setCreate(LifecycleMetaData lifecycle)
   {
      lifecycle.setState(ControllerState.CREATE);
      this.create = lifecycle;
   }

   public LifecycleMetaData getStart()
   {
      return start;
   }

   /**
    * Set the start metadata
    * 
    * @param lifecycle the lifecycle metadata
    */
   public void setStart(LifecycleMetaData lifecycle)
   {
      lifecycle.setState(ControllerState.START);
      this.start = lifecycle;
   }

   public LifecycleMetaData getStop()
   {
      return stop;
   }

   /**
    * Set the stop metadata
    * 
    * @param lifecycle the lifecycle metadata
    */
   public void setStop(LifecycleMetaData lifecycle)
   {
      lifecycle.setState(ControllerState.START);
      this.stop = lifecycle;
   }

   public LifecycleMetaData getDestroy()
   {
      return destroy;
   }

   /**
    * Set the destroy metadata
    * 
    * @param lifecycle the lifecycle metadata
    */
   public void setDestroy(LifecycleMetaData lifecycle)
   {
      lifecycle.setState(ControllerState.CREATE);
      this.destroy = lifecycle;
   }

   public Set<DemandMetaData> getDemands()
   {
      return demands;
   }

   public Set<SupplyMetaData> getSupplies()
   {
      return supplies;
   }

   public Set<DependencyMetaData> getDepends()
   {
      return depends;
   }

   public List<InstallMetaData> getInstalls()
   {
      return installs;
   }

   /**
    * Set the installs
    * 
    * @param installs List<InstallMetaData>
    */
   public void setInstalls(List<InstallMetaData> installs)
   {
      this.installs = installs;
      flushJBossObjectCache();
   }

   public List<InstallMetaData> getUninstalls()
   {
      return uninstalls;
   }

   /**
    * Set the uninstalls
    * 
    * @param uninstalls List<InstallMetaData>
    */
   public void setUninstalls(List<InstallMetaData> uninstalls)
   {
      this.uninstalls = uninstalls;
      flushJBossObjectCache();
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      if (visitor.visitorNodeStack().isEmpty() == false || (classLoader != null && classLoader.getClassLoader() != this))
      {
         KernelControllerContext controllerContext = visitor.getControllerContext();
         controller = (KernelController) controllerContext.getController();
         Object name = controllerContext.getName();
         Object iDependOn = getUnderlyingValue();
         if (name.equals(iDependOn) == false)
         {
            ControllerState whenRequired = visitor.getContextState();
            DependencyItem di = new AbstractDependencyItem(name, iDependOn, whenRequired, ControllerState.INSTALLED);
            visitor.addDependency(di);
         }
      }
      super.initialVisit(visitor);
   }

   public void setController(KernelController controller)
   {
      this.controller = controller;
   }

   protected void addChildren(Set<MetaDataVisitorNode> children)
   {
      super.addChildren(children);
      if (classLoader != null && classLoader.getClassLoader() != this)
         children.add(classLoader);
      if (constructor != null)
         children.add(constructor);
      if (properties != null)
         children.addAll(properties);
      if (create != null)
         children.add(create);
      if (start != null)
         children.add(start);
      if (stop != null)
         children.add(stop);
      if (destroy != null)
         children.add(destroy);
      if (demands != null)
         children.addAll(demands);
      if (supplies != null)
         children.addAll(supplies);
      if (depends != null)
         children.addAll(depends);
      if (installs != null)
         children.addAll(installs);
      if (uninstalls != null)
         children.addAll(uninstalls);
   }

   public Class getType(MetaDataVisitor visitor, MetaDataVisitorNode previous) throws Throwable
   {
      throw new IllegalArgumentException("Cannot determine inject class type: " + this);
   }

   public Object getUnderlyingValue()
   {
      return name;
   }

   @SuppressWarnings("unchecked")
   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      ControllerContext context = controller.getInstalledContext(name);
      if (context == null || context.getTarget() == null)
      {
         // possible call for classloader
         if (info == null && classLoader != null && classLoader.getClassLoader() == this)
         {
            return cl;
         }
         throw new IllegalArgumentException("Bean not yet installed: " + name);
      }
      Object target = context.getTarget();
      // TODO - add progression here as well?
      if (info != null && info.getType().isAssignableFrom(target.getClass()) == false)
      {
         throw new ClassCastException(target + " is not a " + info);
      }
      return target;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("name=").append(name);
      buffer.append(" bean=").append(bean);
      buffer.append(" properties=");
      JBossObject.list(buffer, properties);
      if (classLoader != null && classLoader.getClassLoader() != this)
         buffer.append(" classLoader=").append(classLoader);
      buffer.append(" constructor=").append(constructor);
      if (create != null)
         buffer.append(" create=").append(create);
      if (start != null)
         buffer.append(" start=").append(start);
      if (stop != null)
         buffer.append(" stop=").append(stop);
      if (destroy != null)
         buffer.append(" destroy=").append(destroy);
      if (demands != null)
      {
         buffer.append(" demands=");
         JBossObject.list(buffer, demands);
      }
      super.toString(buffer);
      if (supplies != null)
      {
         buffer.append(" supplies=");
         JBossObject.list(buffer, supplies);
      }
      if (depends != null)
      {
         buffer.append(" depends=");
         JBossObject.list(buffer, depends);
      }
      if (installs != null)
      {
         buffer.append(" installs=");
         JBossObject.list(buffer, installs);
      }
      if (uninstalls != null)
      {
         buffer.append(" uninstalls=");
         JBossObject.list(buffer, uninstalls);
      }
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(bean);
      buffer.append('/');
      buffer.append(name);
   }
}
