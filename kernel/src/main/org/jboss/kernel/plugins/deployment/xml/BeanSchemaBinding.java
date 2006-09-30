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
package org.jboss.kernel.plugins.deployment.xml;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;
import javax.xml.namespace.QName;

import org.jboss.beans.metadata.plugins.*;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.beans.metadata.spi.DependencyMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.plugins.InjectionType;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.logging.Logger;
import org.jboss.xb.binding.sunday.unmarshalling.CharactersHandler;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementHandler;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementInterceptor;
import org.jboss.xb.binding.sunday.unmarshalling.ElementBinding;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.binding.sunday.unmarshalling.TypeBinding;
import org.xml.sax.Attributes;

/**
 * The POJO schema binding.
 * 
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 * @author <a href="mailto:adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class BeanSchemaBinding
{
   /** The log */
   private static final Logger log = Logger.getLogger(BeanSchemaBinding.class);

   /** The namespace */
   private static final String BEAN_DEPLOYER_NS = "urn:jboss:bean-deployer";

   /** The deployment binding */
   private static final QName deploymentTypeQName = new QName(BEAN_DEPLOYER_NS, "deploymentType");

   /** The bean binding */
   private static final QName beanTypeQName = new QName(BEAN_DEPLOYER_NS, "beanType");

   /** The bean element name */
   private static final QName beanQName = new QName(BEAN_DEPLOYER_NS, "bean");

   /** The beanfactory binding */
   private static final QName beanFactoryTypeQName = new QName(BEAN_DEPLOYER_NS, "beanfactoryType");

   /** The beanfactory element name */
   private static final QName beanFactoryQName = new QName(BEAN_DEPLOYER_NS, "beanfactory");

   /** The classloader binding */
   private static final QName classloaderTypeQName = new QName(BEAN_DEPLOYER_NS, "classloaderType");

   /** The classloader element name */
   private static final QName classloaderQName = new QName(BEAN_DEPLOYER_NS, "classloader");

   /** The constructor binding */
   private static final QName constructorTypeQName = new QName(BEAN_DEPLOYER_NS, "constructorType");

   /** The constructor element name */
   private static final QName constructorQName = new QName(BEAN_DEPLOYER_NS, "constructor");

   /** The factory element name */
   private static final QName factoryQName = new QName(BEAN_DEPLOYER_NS, "factory");

   /** The parameter binding */
   private static final QName parameterTypeQName = new QName(BEAN_DEPLOYER_NS, "parameterType");

   /** The parameter element name */
   private static final QName parameterQName = new QName(BEAN_DEPLOYER_NS, "parameter");

   /** The lifecycle binding */
   private static final QName lifecycleTypeQName = new QName(BEAN_DEPLOYER_NS, "lifecycleType");

   /** The create element name */
   private static final QName createQName = new QName(BEAN_DEPLOYER_NS, "create");

   /** The start element name */
   private static final QName startQName = new QName(BEAN_DEPLOYER_NS, "start");

   /** The stop element name */
   private static final QName stopQName = new QName(BEAN_DEPLOYER_NS, "stop");

   /** The destroy element name */
   private static final QName destroyQName = new QName(BEAN_DEPLOYER_NS, "destroy");

   /** The property binding */
   private static final QName propertyTypeQName = new QName(BEAN_DEPLOYER_NS, "propertyType");

   /** The property element name */
   private static final QName propertyQName = new QName(BEAN_DEPLOYER_NS, "property");

   /** The depends binding */
   private static final QName dependsTypeQName = new QName(BEAN_DEPLOYER_NS, "dependsType");

   /** The depends element name */
   private static final QName dependsQName = new QName(BEAN_DEPLOYER_NS, "depends");

   /** The demand binding */
   private static final QName demandTypeQName = new QName(BEAN_DEPLOYER_NS, "demandType");

   /** The demand element name */
   private static final QName demandQName = new QName(BEAN_DEPLOYER_NS, "demand");

   /** The supply binding */
   private static final QName supplyTypeQName = new QName(BEAN_DEPLOYER_NS, "supplyType");

   /** The supply element name */
   private static final QName supplyQName = new QName(BEAN_DEPLOYER_NS, "supply");

   /** The dependency binding */
   private static final QName dependencyTypeQName = new QName(BEAN_DEPLOYER_NS, "dependencyType");

   /** The injection binding */
   private static final QName injectionTypeQName = new QName(BEAN_DEPLOYER_NS, "injectionType");

   /** The inject element name */
   private static final QName injectQName = new QName(BEAN_DEPLOYER_NS, "inject");

   /** The plain value binding */
   private static final QName plainValueTypeQName = new QName(BEAN_DEPLOYER_NS, "plainValueType");

   /** The value binding */
   private static final QName valueTypeQName = new QName(BEAN_DEPLOYER_NS, "valueType");

   /** The value element name */
   private static final QName valueQName = new QName(BEAN_DEPLOYER_NS, "value");

   /** The null element name */
   private static final QName nullQName = new QName(BEAN_DEPLOYER_NS, "null");

   /** The collection binding */
   private static final QName collectionTypeQName = new QName(BEAN_DEPLOYER_NS, "collectionType");

   /** The collection element name */
   private static final QName collectionQName = new QName(BEAN_DEPLOYER_NS, "collection");

   /** The list binding */
   private static final QName listTypeQName = new QName(BEAN_DEPLOYER_NS, "listType");

   /** The list element name */
   private static final QName listQName = new QName(BEAN_DEPLOYER_NS, "list");

   /** The set binding */
   private static final QName setTypeQName = new QName(BEAN_DEPLOYER_NS, "setType");

   /** The set element name */
   private static final QName setQName = new QName(BEAN_DEPLOYER_NS, "set");

   /** The array binding */
   private static final QName arrayTypeQName = new QName(BEAN_DEPLOYER_NS, "arrayType");

   /** The array element name */
   private static final QName arrayQName = new QName(BEAN_DEPLOYER_NS, "array");

   /** The map binding */
   private static final QName mapTypeQName = new QName(BEAN_DEPLOYER_NS, "mapType");

   /** The map element name */
   private static final QName mapQName = new QName(BEAN_DEPLOYER_NS, "map");

   /** The entry binding */
   private static final QName entryTypeQName = new QName(BEAN_DEPLOYER_NS, "entryType");

   /** The entry element name */
   private static final QName entryQName = new QName(BEAN_DEPLOYER_NS, "entry");

   /** The key element name */
   private static final QName keyQName = new QName(BEAN_DEPLOYER_NS, "key");

   /** The schema binding */
   private static SchemaBinding schemaBinding;
   
   /** The value handler */
   public static ValueMetaDataElementInterceptor VALUES = new ValueMetaDataElementInterceptor();
   
   /** The null handler */
   public static NullValueElementInterceptor NULLVALUES = new NullValueElementInterceptor();

   /**
    * Initialize the schema binding
    * 
    * @param schemaBinding the schema binding
    */
   protected synchronized static void init(SchemaBinding schemaBinding)
   {
      // Encourage users to upgrade to the more complete 2.0 version of the schema
      log.warn("You should use the 2.0 version of the Microcontainer xml. xmlns='urn:jboss:bean-deployer:2.0'");
      
      BeanSchemaBinding.schemaBinding = schemaBinding;

      // deployment binding
      TypeBinding deploymentType = schemaBinding.getType(deploymentTypeQName);
      deploymentType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AbstractKernelDeployment();
         }
      });

      // deployment has a classloader
      deploymentType.pushInterceptor(classloaderQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractKernelDeployment deployment = (AbstractKernelDeployment) parent;
            AbstractClassLoaderMetaData classloader = (AbstractClassLoaderMetaData) child;
            deployment.setClassLoader(classloader);
            // add classloaders as value beans
            List<BeanMetaDataFactory> beans = deployment.getBeanFactories();
            if (beans == null)
            {
               beans = new ArrayList<BeanMetaDataFactory>();
               deployment.setBeanFactories(beans);
            }
            beans.add(classloader);
         }
      });

      // deployment has a list beans
      deploymentType.pushInterceptor(beanQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractKernelDeployment deployment = (AbstractKernelDeployment) parent;
            AbstractBeanMetaData bean = (AbstractBeanMetaData) child;
            List<BeanMetaDataFactory> beans = deployment.getBeanFactories();
            if (beans == null)
            {
               beans = new ArrayList<BeanMetaDataFactory>();
               deployment.setBeanFactories(beans);
            }
            beans.add(bean);
         }
      });

      // deployment has a list beanfactorys
      deploymentType.pushInterceptor(beanFactoryQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractKernelDeployment deployment = (AbstractKernelDeployment) parent;
            AbstractBeanMetaData bean = (AbstractBeanMetaData) child;
            List<BeanMetaDataFactory> beans = deployment.getBeanFactories();
            if (beans == null)
            {
               beans = new ArrayList<BeanMetaDataFactory>();
               deployment.setBeanFactories(beans);
            }
            beans.add(bean);
         }
      });

      // bean binding
      TypeBinding beanType = schemaBinding.getType(beanTypeQName);

      /*
      ClassMetaData classMetaData = new ClassMetaData();
      classMetaData.setImpl(AbstractBeanMetaData.class.getName());
      beanType.setClassMetaData(classMetaData);
      
      QName CLASS = new QName("class");
      AttributeBinding attribute = beanType.getAttribute(CLASS);
      PropertyMetaData propertyMetaData = new PropertyMetaData();
      propertyMetaData.setName("bean");
      attribute.setPropertyMetaData(propertyMetaData);

      QName MODE = new QName("mode");
      attribute = beanType.getAttribute(MODE);
      ValueAdapter modeAdapter = new ValueAdapter()
      {
         public Object cast(Object o, Class c)
         {
            String string = (String) o;
            return new ControllerMode(string);
         }
      };
      attribute.setValueAdapter(modeAdapter);
      */
      beanType.setHandler(new DefaultElementHandler()
      {
         
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AbstractBeanMetaData();
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            AbstractBeanMetaData bean = (AbstractBeanMetaData) o;
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("name".equals(localName))
                  bean.setName(attrs.getValue(i));
               else if ("class".equals(localName))
                  bean.setBean(attrs.getValue(i));
               else if ("mode".equals(localName))
                  bean.setMode(new ControllerMode(attrs.getValue(i)));
            }
         }
      });

      // beanfactory binding
      TypeBinding beanFactoryType = schemaBinding.getType(beanFactoryTypeQName);
      beanFactoryType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new GenericBeanFactoryMetaData();
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            GenericBeanFactoryMetaData bean = (GenericBeanFactoryMetaData) o;
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("name".equals(localName))
                  bean.setName(attrs.getValue(i));
               else if ("class".equals(localName))
                  bean.setBeanClass(attrs.getValue(i));
               else if ("mode".equals(localName))
                  bean.setMode(new ControllerMode(attrs.getValue(i)));
            }
         }
      });

      // bean has a classloader
      beanType.pushInterceptor(classloaderQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
            AbstractClassLoaderMetaData classloader = (AbstractClassLoaderMetaData) child;
            bean.setClassLoader(classloader);
         }
      });

      // beanfactory has a classloader
      beanFactoryType.pushInterceptor(classloaderQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            GenericBeanFactoryMetaData bean = (GenericBeanFactoryMetaData) parent;
            AbstractClassLoaderMetaData classloader = (AbstractClassLoaderMetaData) child;
            bean.setClassLoader(classloader);
         }
      });

      // bean has a constructor
      beanType.pushInterceptor(constructorQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
            AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) child;
            bean.setConstructor(constructor);
         }
      });

      // beanfactory has a constructor
      beanFactoryType.pushInterceptor(constructorQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            GenericBeanFactoryMetaData bean = (GenericBeanFactoryMetaData) parent;
            AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) child;
            bean.setBeanConstructor(constructor);
         }
      });

      // classloader binding
      TypeBinding classloaderType = schemaBinding.getType(classloaderTypeQName);
      configureValueBindings(classloaderType);
      classloaderType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AbstractClassLoaderMetaData();
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
         }
      });

      // constructor binding
      TypeBinding constructorType = schemaBinding.getType(constructorTypeQName);
      constructorType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AbstractConstructorMetaData();
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) o;
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("factoryClass".equals(localName))
                  constructor.setFactoryClass(attrs.getValue(i));
               else if ("factoryMethod".equals(localName))
                  constructor.setFactoryMethod(attrs.getValue(i));
            }
         }
      });

      // constructor has a factory
      constructorType.pushInterceptor(factoryQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) parent;
            AbstractDependencyValueMetaData factory = (AbstractDependencyValueMetaData) child;
            constructor.setFactory(factory);
         }
      });

      // constructor has a list parameters
      constructorType.pushInterceptor(parameterQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) parent;
            AbstractParameterMetaData parameter = (AbstractParameterMetaData) child;
            List<ParameterMetaData> parameters = constructor.getParameters();
            if (parameters == null)
            {
               parameters = new ArrayList<ParameterMetaData>();
               constructor.setParameters(parameters);
            }
            parameter.setIndex(parameters.size());
            parameters.add(parameter);
         }
      });

      // parameter binding
      TypeBinding parameterType = schemaBinding.getType(parameterTypeQName);
      configureValueBindings(parameterType);
      parameterType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AbstractParameterMetaData();
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            AbstractParameterMetaData parameter = (AbstractParameterMetaData) o;
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("class".equals(localName))
                  parameter.setType(attrs.getValue(i));
            }
         }
      });
      
      // parameter can take a value
      parameterType.setSimpleType(new CharactersHandler()
      {
         public Object unmarshal(QName qName, TypeBinding typeBinding, NamespaceContext nsCtx, org.jboss.xb.binding.metadata.ValueMetaData valueMetaData, String value)
         {
            return new StringValueMetaData(value);
         }

         public void setValue(QName qName, ElementBinding element, Object owner, Object value)
         {
            AbstractParameterMetaData parameter = (AbstractParameterMetaData) owner;
            parameter.setValue((StringValueMetaData) value);
         }
      });

      // bean has a create
      beanType.pushInterceptor(createQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
            AbstractLifecycleMetaData lifecycle = (AbstractLifecycleMetaData) child;
            lifecycle.setType("create");
            bean.setCreate(lifecycle);
         }
      });

      // beanfactory has a create
      beanFactoryType.pushInterceptor(createQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            GenericBeanFactoryMetaData bean = (GenericBeanFactoryMetaData) parent;
            AbstractLifecycleMetaData lifecycle = (AbstractLifecycleMetaData) child;
            lifecycle.setType("create");
            bean.setBeanCreate(lifecycle);
         }
      });

      // bean has a start
      beanType.pushInterceptor(startQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
            AbstractLifecycleMetaData lifecycle = (AbstractLifecycleMetaData) child;
            lifecycle.setType("start");
            bean.setStart(lifecycle);
         }
      });

      // beanfactory has a start
      beanFactoryType.pushInterceptor(startQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            GenericBeanFactoryMetaData bean = (GenericBeanFactoryMetaData) parent;
            AbstractLifecycleMetaData lifecycle = (AbstractLifecycleMetaData) child;
            lifecycle.setType("start");
            bean.setBeanStart(lifecycle);
         }
      });

      // bean has a stop
      beanType.pushInterceptor(stopQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
            AbstractLifecycleMetaData lifecycle = (AbstractLifecycleMetaData) child;
            lifecycle.setType("stop");
            bean.setStop(lifecycle);
         }
      });

      // bean has a destroy
      beanType.pushInterceptor(destroyQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
            AbstractLifecycleMetaData lifecycle = (AbstractLifecycleMetaData) child;
            lifecycle.setType("destroy");
            bean.setDestroy(lifecycle);
         }
      });

      // lifecycle binding
      TypeBinding lifecycleType = schemaBinding.getType(lifecycleTypeQName);
      lifecycleType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AbstractLifecycleMetaData();
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            AbstractLifecycleMetaData lifecycle = (AbstractLifecycleMetaData) o;
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("method".equals(localName))
                  lifecycle.setMethodName(attrs.getValue(i));
            }
         }
      });

      // lifecycle has a list parameters
      lifecycleType.pushInterceptor(parameterQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractLifecycleMetaData lifecycle = (AbstractLifecycleMetaData) parent;
            AbstractParameterMetaData parameter = (AbstractParameterMetaData) child;
            List<ParameterMetaData> parameters = lifecycle.getParameters();
            if (parameters == null)
            {
               parameters = new ArrayList<ParameterMetaData>();
               lifecycle.setParameters(parameters);
            }
            parameter.setIndex(parameters.size());
            parameters.add(parameter);
         }
      });

      // bean has a set of properties
      beanType.pushInterceptor(propertyQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
            AbstractPropertyMetaData property = (AbstractPropertyMetaData) child;
            Set<PropertyMetaData> properties = bean.getProperties();
            if (properties == null)
            {
               properties = new HashSet<PropertyMetaData>();
               bean.setProperties(properties);
            }
            properties.add(property);
         }
      });

      // beanfactory has a set of properties
      beanFactoryType.pushInterceptor(propertyQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            GenericBeanFactoryMetaData bean = (GenericBeanFactoryMetaData) parent;
            AbstractPropertyMetaData property = (AbstractPropertyMetaData) child;
            bean.addBeanProperty(property);
         }
      });

      // bean has a set of depends
      beanType.pushInterceptor(dependsQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
            AbstractDependencyMetaData dependency = (AbstractDependencyMetaData) child;
            Set<DependencyMetaData> depends = bean.getDepends();
            if (depends == null)
            {
               depends = new HashSet<DependencyMetaData>();
               bean.setDepends(depends);
            }
            depends.add(dependency);
         }
      });

      // beanfactory has a set of depends
      beanFactoryType.pushInterceptor(dependsQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            GenericBeanFactoryMetaData bean = (GenericBeanFactoryMetaData) parent;
            AbstractDependencyMetaData dependency = (AbstractDependencyMetaData) child;
            Set<DependencyMetaData> depends = bean.getDepends();
            if (depends == null)
            {
               depends = new HashSet<DependencyMetaData>();
               bean.setDepends(depends);
            }
            depends.add(dependency);
         }
      });

      // bean has a set of demands
      beanType.pushInterceptor(demandQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
            AbstractDemandMetaData demand = (AbstractDemandMetaData) child;
            Set<DemandMetaData> demands = bean.getDemands();
            if (demands == null)
            {
               demands = new HashSet<DemandMetaData>();
               bean.setDemands(demands);
            }
            demands.add(demand);
         }
      });

      // beanfactory has a set of demands
      beanFactoryType.pushInterceptor(demandQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
            AbstractDemandMetaData demand = (AbstractDemandMetaData) child;
            Set<DemandMetaData> demands = bean.getDemands();
            if (demands == null)
            {
               demands = new HashSet<DemandMetaData>();
               bean.setDemands(demands);
            }
            demands.add(demand);
         }
      });

      // bean has a set of supplies
      beanType.pushInterceptor(supplyQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
            AbstractSupplyMetaData supply = (AbstractSupplyMetaData) child;
            Set<SupplyMetaData> supplies = bean.getSupplies();
            if (supplies == null)
            {
               supplies = new HashSet<SupplyMetaData>();
               bean.setSupplies(supplies);
            }
            supplies.add(supply);
         }
      });

      // beanfactory has a set of supplies
      beanFactoryType.pushInterceptor(supplyQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
            AbstractSupplyMetaData supply = (AbstractSupplyMetaData) child;
            Set<SupplyMetaData> supplies = bean.getSupplies();
            if (supplies == null)
            {
               supplies = new HashSet<SupplyMetaData>();
               bean.setSupplies(supplies);
            }
            supplies.add(supply);
         }
      });

      // property binding
      TypeBinding propertyType = schemaBinding.getType(propertyTypeQName);
      configureValueBindings(propertyType);
      propertyType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AbstractPropertyMetaData();
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            AbstractPropertyMetaData property = (AbstractPropertyMetaData) o;
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("name".equals(localName))
                  property.setName(attrs.getValue(i));
               else if ("class".equals(localName))
               {
                  StringValueMetaData svmd = new StringValueMetaData();
                  svmd.setType(attrs.getValue(i));
                  property.setValue(svmd);
               }
            }
         }
         
         public Object endElement(Object o, QName qName, ElementBinding element)
         {
            AbstractPropertyMetaData x = (AbstractPropertyMetaData) o;
            String name = x.getName();
            if (name == null || name.trim().length() == 0)
               throw new IllegalArgumentException("Null or empty property name.");
            return o;
         }
      });

      // property can take a value
      propertyType.setSimpleType(new CharactersHandler()
      {
         public Object unmarshal(QName qName, TypeBinding typeBinding, NamespaceContext nsCtx, org.jboss.xb.binding.metadata.ValueMetaData valueMetaData, String value)
         {
            return new StringValueMetaData(value);
         }

         public void setValue(QName qName, ElementBinding element, Object owner, Object value)
         {
            AbstractPropertyMetaData property = (AbstractPropertyMetaData) owner;
            StringValueMetaData svmd = (StringValueMetaData) value;
            ValueMetaData vmd = property.getValue();
            if (vmd != null && vmd instanceof StringValueMetaData)
            {
               StringValueMetaData previous = (StringValueMetaData) vmd;
               String type = previous.getType();
               if (type != null)
                  svmd.setType(type);
            }
            property.setValue(svmd);
         }
      });

      // dependency binding
      TypeBinding dependsType = schemaBinding.getType(dependsTypeQName);
      dependsType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AbstractDependencyMetaData();
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
         }
         
         public Object endElement(Object o, QName qName, ElementBinding element)
         {
            AbstractDependencyMetaData x = (AbstractDependencyMetaData) o;
            String name = (String) x.getDependency();
            if (name == null || name.trim().length() == 0)
               throw new IllegalArgumentException("Null or empty dependency.");
            return o;
         }
      });

      // depends can take a value
      dependsType.setSimpleType(new CharactersHandler()
      {
         public Object unmarshal(QName qName, TypeBinding typeBinding, NamespaceContext nsCtx, org.jboss.xb.binding.metadata.ValueMetaData valueMetaData, String value)
         {
            return value;
         }

         public void setValue(QName qname, ElementBinding element, Object owner, Object value)
         {
            AbstractDependencyMetaData depends = (AbstractDependencyMetaData) owner;
            depends.setDependency(value);
         }
      });

      // demand binding
      TypeBinding demandType = schemaBinding.getType(demandTypeQName);
      demandType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AbstractDemandMetaData();
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            AbstractDemandMetaData demand = (AbstractDemandMetaData) o;
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("state".equals(localName))
                  demand.setWhenRequired(new ControllerState(attrs.getValue(i)));
            }
         }
         
         public Object endElement(Object o, QName qName, ElementBinding element)
         {
            AbstractDemandMetaData x = (AbstractDemandMetaData) o;
            String name = (String) x.getDemand();
            if (name == null || name.trim().length() == 0)
               throw new IllegalArgumentException("Null or empty demand.");
            return o;
         }
      });

      // demand can take a value
      demandType.setSimpleType(new CharactersHandler()
      {
         public Object unmarshal(QName qName, TypeBinding typeBinding, NamespaceContext nsCtx, org.jboss.xb.binding.metadata.ValueMetaData valueMetaData, String value)
         {
            return value;
         }

         public void setValue(QName qname, ElementBinding element, Object owner, Object value)
         {
            AbstractDemandMetaData demand = (AbstractDemandMetaData) owner;
            demand.setDemand(value);
         }
      });

      // supply binding
      TypeBinding supplyType = schemaBinding.getType(supplyTypeQName);
      supplyType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AbstractSupplyMetaData();
         }
         
         public Object endElement(Object o, QName qName, ElementBinding element)
         {
            AbstractSupplyMetaData x = (AbstractSupplyMetaData) o;
            String name = (String) x.getSupply();
            if (name == null || name.trim().length() == 0)
               throw new IllegalArgumentException("Null or empty supply.");
            return o;
         }
      });

      // supply can take a value
      supplyType.setSimpleType(new CharactersHandler()
      {
         public Object unmarshal(QName qName, TypeBinding typeBinding, NamespaceContext nsCtx, org.jboss.xb.binding.metadata.ValueMetaData valueMetaData, String value)
         {
            return value;
         }

         public void setValue(QName qName, ElementBinding element, Object owner, Object value)
         {
            AbstractSupplyMetaData supply = (AbstractSupplyMetaData) owner;
            supply.setSupply(value);
         }
      });

      // dependency binding
      TypeBinding dependencyType = schemaBinding.getType(dependencyTypeQName);
      dependencyType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AbstractDependencyValueMetaData();
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            AbstractDependencyValueMetaData dependency = (AbstractDependencyValueMetaData) o;
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("bean".equals(localName))
                  dependency.setValue(attrs.getValue(i));
               else if ("property".equals(localName))
                  dependency.setProperty(attrs.getValue(i));
               else if ("state".equals(localName))
                  dependency.setDependentState(new ControllerState(attrs.getValue(i)));
               else if ("whenRequired".equals(localName))
                  dependency.setWhenRequiredState(new ControllerState(attrs.getValue(i)));
            }
         }
         
         public Object endElement(Object o, QName qName, ElementBinding element)
         {
            AbstractDependencyValueMetaData x = (AbstractDependencyValueMetaData) o;
            String name = (String) x.getUnderlyingValue();
            if (name == null || name.trim().length() == 0)
               throw new IllegalArgumentException("Null or empty bean in injection/factory.");
            return o;
         }
      });

      // injection binding
      TypeBinding injectionType = schemaBinding.getType(injectionTypeQName);
      injectionType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            AbstractInjectionValueMetaData vmd = new AbstractInjectionValueMetaData();
            if (parent instanceof AbstractPropertyMetaData)
            {
               AbstractPropertyMetaData x = (AbstractPropertyMetaData) parent;
               vmd.setPropertyMetaData(x);
            }
            return vmd;
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            AbstractInjectionValueMetaData injection = (AbstractInjectionValueMetaData) o;
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("bean".equals(localName))
                  injection.setValue(attrs.getValue(i));
               else if ("property".equals(localName))
                  injection.setProperty(attrs.getValue(i));
               else if ("state".equals(localName))
                  injection.setDependentState(new ControllerState(attrs.getValue(i)));
               else if ("whenRequired".equals(localName))
                  injection.setWhenRequiredState(new ControllerState(attrs.getValue(i)));
               else if ("type".equals(localName))
                  injection.setInjectionType(new InjectionType(attrs.getValue(i)));
            }
         }

      });

      // value binding
      TypeBinding plainValueType = schemaBinding.getType(plainValueTypeQName);
      plainValueType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new StringValueMetaData();
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            StringValueMetaData value = (StringValueMetaData) o;
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("class".equals(localName))
                  value.setType(attrs.getValue(i));
            }
         }
      });

      // value can take a value
      plainValueType.setSimpleType(new CharactersHandler()
      {
         public Object unmarshal(QName qName, TypeBinding typeBinding, NamespaceContext nsCtx, org.jboss.xb.binding.metadata.ValueMetaData valueMetaData, String value)
         {
            return value;
         }

         public void setValue(QName qName, ElementBinding element, Object owner, Object value)
         {
            StringValueMetaData valueMetaData = (StringValueMetaData) owner;
            valueMetaData.setValue(value);
         }
      });

      // value binding
      TypeBinding valueType = schemaBinding.getType(valueTypeQName);
      configureValueBindings(valueType);
      valueType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AbstractValueMetaData(new StringValueMetaData());
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            AbstractValueMetaData value = (AbstractValueMetaData) o;
            StringValueMetaData string = (StringValueMetaData) value.getValue();
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("class".equals(localName))
                  string.setType(attrs.getValue(i));
            }
         }
      });

      // value can take a value
      valueType.setSimpleType(new CharactersHandler()
      {
         public Object unmarshal(QName qName, TypeBinding typeBinding, NamespaceContext nsCtx, org.jboss.xb.binding.metadata.ValueMetaData valueMetaData, String value)
         {
            return value;
         }

         public void setValue(QName qName, ElementBinding element, Object owner, Object value)
         {
            AbstractValueMetaData valueMetaData = (AbstractValueMetaData) owner;
            StringValueMetaData string = (StringValueMetaData) valueMetaData.getValue();
            string.setValue(value);
         }
      });

      // collection binding
      configureCollection(collectionTypeQName);

      // list binding
      configureCollection(listTypeQName);

      // set binding
      configureCollection(setTypeQName);

      // array binding
      configureCollection(arrayTypeQName);

      // map binding
      TypeBinding mapType = schemaBinding.getType(mapTypeQName);
      mapType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new AbstractMapMetaData();
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            AbstractMapMetaData collection = (AbstractMapMetaData) o;
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("class".equals(localName))
                  collection.setType(attrs.getValue(i));
               else if ("keyClass".equals(localName))
                  collection.setKeyType(attrs.getValue(i));
               else if ("valueClass".equals(localName))
                  collection.setValueType(attrs.getValue(i));
            }
         }
      });

      // map has a map entries
      mapType.pushInterceptor(entryQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            AbstractMapMetaData map = (AbstractMapMetaData) parent;
            MapEntry entry = (MapEntry) child;
            AbstractValueMetaData entryKey = (AbstractValueMetaData) entry.key;
            if (entryKey == null)
               throw new IllegalArgumentException("No key in map entry");
            AbstractValueMetaData entryValue = (AbstractValueMetaData) entry.value; 
            if (entryValue == null)
               throw new IllegalArgumentException("No value in map entry");
            map.put((MetaDataVisitorNode) entryKey.getValue(), (MetaDataVisitorNode) entryValue.getValue());
         }
      });

      // entry binding
      TypeBinding entryType = schemaBinding.getType(entryTypeQName);
      entryType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            return new MapEntry();
         }
      });

      // entry has a key
      entryType.pushInterceptor(keyQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            MapEntry entry = (MapEntry) parent;
            entry.key = (ValueMetaData) child;
         }
      });

      // entry has a value
      entryType.pushInterceptor(valueQName, new DefaultElementInterceptor()
      {
         public void add(Object parent, Object child, QName name)
         {
            MapEntry entry = (MapEntry) parent;
            entry.value = (ValueMetaData) child;
         }
      });
   }
   
   /**
    * Configure a collection.
    */
   private static void configureCollection(QName qname)
   {
      TypeBinding collectionType = schemaBinding.getType(qname);
      collectionType.setHandler(new DefaultElementHandler()
      {
         public Object startElement(Object parent, QName name, ElementBinding element)
         {
            if (collectionQName.equals(name))
               return new AbstractCollectionMetaData();
            else if (listQName.equals(name))
               return new AbstractListMetaData();
            else if (setQName.equals(name))
               return new AbstractSetMetaData();
            else if (arrayQName.equals(name))
               return new AbstractArrayMetaData();
            else
               throw new IllegalArgumentException("Unknown collection qname=" + name);
         }

         public void attributes(Object o, QName elementName, ElementBinding element, Attributes attrs, NamespaceContext nsCtx)
         {
            AbstractCollectionMetaData collection = (AbstractCollectionMetaData) o;
            for (int i = 0; i < attrs.getLength(); ++i)
            {
               String localName = attrs.getLocalName(i);
               if ("class".equals(localName))
                  collection.setType(attrs.getValue(i));
               else if ("elementClass".equals(localName))
                  collection.setElementType(attrs.getValue(i));
            }
         }
      });

      configureValueBindings(collectionType);
   }
   
   private static void configureValueBindings(TypeBinding typeBinding)
   {
      // type has values
      typeBinding.pushInterceptor(valueQName, VALUES);

      // type has injections
      typeBinding.pushInterceptor(injectQName, VALUES);

      // type can take a collection
      typeBinding.pushInterceptor(collectionQName, VALUES);

      // type can take a list
      typeBinding.pushInterceptor(listQName, VALUES);

      // type can take a set
      typeBinding.pushInterceptor(setQName, VALUES);

      // type can take an array
      typeBinding.pushInterceptor(arrayQName, VALUES);

      // type can take a map
      typeBinding.pushInterceptor(mapQName, VALUES);

      // type has a null
      typeBinding.pushInterceptor(nullQName, NULLVALUES);
   }
   
   private static class NullValueElementInterceptor extends DefaultElementInterceptor
   {
      public void add(Object parent, Object child, QName name)
      {
         if (parent instanceof AbstractCollectionMetaData)
         {
            AbstractCollectionMetaData collection = (AbstractCollectionMetaData) parent;
            collection.add(new AbstractValueMetaData());
         }
         else if (parent instanceof AbstractParameterMetaData)
         {
            AbstractParameterMetaData valueMetaData = (AbstractParameterMetaData) parent;
            valueMetaData.setValue(new AbstractValueMetaData());
         }
         else if (parent instanceof AbstractPropertyMetaData)
         {
            AbstractPropertyMetaData valueMetaData = (AbstractPropertyMetaData) parent;
            valueMetaData.setValue(new AbstractValueMetaData());
         }
         else if (parent instanceof AbstractClassLoaderMetaData)
         {
            AbstractClassLoaderMetaData valueMetaData = (AbstractClassLoaderMetaData) parent;
            valueMetaData.setClassLoader(new AbstractValueMetaData());
         }
         else
         {
            AbstractValueMetaData valueMetaData = (AbstractValueMetaData) parent;
            valueMetaData.setValue(new AbstractValueMetaData());
         }
      }
   }
   
   private static class ValueMetaDataElementInterceptor extends DefaultElementInterceptor
   {
      public void add(Object parent, Object child, QName name)
      {
         if (parent instanceof AbstractCollectionMetaData)
         {
            AbstractCollectionMetaData collection = (AbstractCollectionMetaData) parent;
            ValueMetaData value = (ValueMetaData) child;
            collection.add(value);
         }
         else if (parent instanceof AbstractParameterMetaData)
         {
            AbstractParameterMetaData valueMetaData = (AbstractParameterMetaData) parent;
            ValueMetaData value = (ValueMetaData) child;
            valueMetaData.setValue(value);
         }
         else if (parent instanceof AbstractPropertyMetaData)
         {
            AbstractPropertyMetaData valueMetaData = (AbstractPropertyMetaData) parent;
            ValueMetaData value = (ValueMetaData) child;
            valueMetaData.setValue(value);
         }
         else if (parent instanceof AbstractClassLoaderMetaData)
         {
            AbstractClassLoaderMetaData valueMetaData = (AbstractClassLoaderMetaData) parent;
            ValueMetaData value = (ValueMetaData) child;
            valueMetaData.setClassLoader(value);
         }
         else
         {
            AbstractValueMetaData valueMetaData = (AbstractValueMetaData) parent;
            ValueMetaData value = (ValueMetaData) child;
            valueMetaData.setValue(value);
         }
      }
   }
   
   private static class MapEntry
   {
      public Object key;
      public Object value;
   }
}
