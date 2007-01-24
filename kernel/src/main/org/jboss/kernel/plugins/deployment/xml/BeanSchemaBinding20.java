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

import javax.xml.namespace.QName;

import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.binding.sunday.unmarshalling.TypeBinding;

/**
 * The POJO schema binding.
 * 
 * @author <a href="mailto:alex@jboss.org">Alexey Loubyansky</a>
 * @author <a href="mailto:adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class BeanSchemaBinding20
{
   /** The namespace */
   public static final String BEAN_DEPLOYER_NS = "urn:jboss:bean-deployer:2.0";

   /** The deployment binding */
   public static final QName deploymentTypeQName = new QName(BEAN_DEPLOYER_NS, "deploymentType");

   /** The bean binding */
   public static final QName beanTypeQName = new QName(BEAN_DEPLOYER_NS, "beanType");

   /** The bean element name */
   public static final QName beanQName = new QName(BEAN_DEPLOYER_NS, "bean");

   /** The beanfactory binding */
   public static final QName beanFactoryTypeQName = new QName(BEAN_DEPLOYER_NS, "beanfactoryType");

   /** The beanfactory element name */
   public static final QName beanFactoryQName = new QName(BEAN_DEPLOYER_NS, "beanfactory");

   /** The annotation binding */
   public static final QName annotationTypeQName = new QName(BEAN_DEPLOYER_NS,  "annotationType");

   /** The annotation element name */
   public static final QName annotationQName = new QName(BEAN_DEPLOYER_NS, "annotation");

   /** The attribute element name */
   public static final QName attributeQName = new QName(BEAN_DEPLOYER_NS, "attribute");

   /** The classloader binding */
   public static final QName classloaderTypeQName = new QName(BEAN_DEPLOYER_NS, "classloaderType");

   /** The classloader element name */
   public static final QName classloaderQName = new QName(BEAN_DEPLOYER_NS, "classloader");

   /** The constructor binding */
   public static final QName constructorTypeQName = new QName(BEAN_DEPLOYER_NS, "constructorType");

   /** The constructor element name */
   public static final QName constructorQName = new QName(BEAN_DEPLOYER_NS, "constructor");

   /** The factory element name */
   public static final QName factoryQName = new QName(BEAN_DEPLOYER_NS, "factory");

   /** The parameter binding */
   public static final QName parameterTypeQName = new QName(BEAN_DEPLOYER_NS, "parameterType");

   /** The parameter element name */
   public static final QName parameterQName = new QName(BEAN_DEPLOYER_NS, "parameter");

   /** The lifecycle binding */
   public static final QName lifecycleTypeQName = new QName(BEAN_DEPLOYER_NS, "lifecycleType");

   /** The create element name */
   public static final QName createQName = new QName(BEAN_DEPLOYER_NS, "create");

   /** The start element name */
   public static final QName startQName = new QName(BEAN_DEPLOYER_NS, "start");

   /** The stop element name */
   public static final QName stopQName = new QName(BEAN_DEPLOYER_NS, "stop");

   /** The destroy element name */
   public static final QName destroyQName = new QName(BEAN_DEPLOYER_NS, "destroy");

   /** The install binding */
   public static final QName installTypeQName = new QName(BEAN_DEPLOYER_NS, "installType");

   /** The install element name */
   public static final QName installQName = new QName(BEAN_DEPLOYER_NS, "install");

   /** The start element name */
   public static final QName uninstallQName = new QName(BEAN_DEPLOYER_NS, "uninstall");

   /** The property binding */
   public static final QName propertyTypeQName = new QName(BEAN_DEPLOYER_NS, "propertyType");

   /** The property element name */
   public static final QName propertyQName = new QName(BEAN_DEPLOYER_NS, "property");

   /** The depends binding */
   public static final QName dependsTypeQName = new QName(BEAN_DEPLOYER_NS, "dependsType");

   /** The depends element name */
   public static final QName dependsQName = new QName(BEAN_DEPLOYER_NS, "depends");

   /** The demand binding */
   public static final QName demandTypeQName = new QName(BEAN_DEPLOYER_NS, "demandType");

   /** The demand element name */
   public static final QName demandQName = new QName(BEAN_DEPLOYER_NS, "demand");

   /** The supply binding */
   public static final QName supplyTypeQName = new QName(BEAN_DEPLOYER_NS, "supplyType");

   /** The supply element name */
   public static final QName supplyQName = new QName(BEAN_DEPLOYER_NS, "supply");

   /** The dependency binding */
   public static final QName dependencyTypeQName = new QName(BEAN_DEPLOYER_NS, "dependencyType");

   /** The dependency binding */
   public static final QName injectionTypeQName = new QName(BEAN_DEPLOYER_NS, "injectionType");

   /** The factory binding */
   public static final QName factoryTypeQName = new QName(BEAN_DEPLOYER_NS, "factoryType");

   /** The inject element name */
   public static final QName injectQName = new QName(BEAN_DEPLOYER_NS, "inject");

   /** The plain value binding */
   public static final QName plainValueTypeQName = new QName(BEAN_DEPLOYER_NS, "plainValueType");

   /** The value binding */
   public static final QName valueTypeQName = new QName(BEAN_DEPLOYER_NS, "valueType");

   /** The value element name */
   public static final QName valueQName = new QName(BEAN_DEPLOYER_NS, "value");

   /** The null element name */
   public static final QName nullQName = new QName(BEAN_DEPLOYER_NS, "null");

   /** The this element name */
   public static final QName thisQName = new QName(BEAN_DEPLOYER_NS, "this");

   /** The collection binding */
   public static final QName collectionTypeQName = new QName(BEAN_DEPLOYER_NS, "collectionType");

   /** The collection element name */
   public static final QName collectionQName = new QName(BEAN_DEPLOYER_NS, "collection");

   /** The list binding */
   public static final QName listTypeQName = new QName(BEAN_DEPLOYER_NS, "listType");

   /** The list element name */
   public static final QName listQName = new QName(BEAN_DEPLOYER_NS, "list");

   /** The set binding */
   public static final QName setTypeQName = new QName(BEAN_DEPLOYER_NS, "setType");

   /** The set element name */
   public static final QName setQName = new QName(BEAN_DEPLOYER_NS, "set");

   /** The array binding */
   public static final QName arrayTypeQName = new QName(BEAN_DEPLOYER_NS, "arrayType");

   /** The array element name */
   public static final QName arrayQName = new QName(BEAN_DEPLOYER_NS, "array");

   /** The map binding */
   public static final QName mapTypeQName = new QName(BEAN_DEPLOYER_NS, "mapType");

   /** The map element name */
   public static final QName mapQName = new QName(BEAN_DEPLOYER_NS, "map");

   /** The entry binding */
   public static final QName entryTypeQName = new QName(BEAN_DEPLOYER_NS, "entryType");

   /** The entry element name */
   public static final QName entryQName = new QName(BEAN_DEPLOYER_NS, "entry");

   /** The key element name */
   public static final QName keyQName = new QName(BEAN_DEPLOYER_NS, "key");

   /**
    * Initialize the schema binding
    * 
    * @param schemaBinding the schema binding
    */
   public static void init(SchemaBinding schemaBinding)
   {
      // ignore XB property replacement
      schemaBinding.setReplacePropertyRefs(false);
      // init
      initDeployment(schemaBinding);
      initBean(schemaBinding);
      initBeanFactory(schemaBinding);
      initArtifacts(schemaBinding);
   }

   /**
    * Initialize deployment part of the schema binding
    * 
    * @param schemaBinding the schema binding
    */
   public static void initDeployment(SchemaBinding schemaBinding)
   {
      // deployment binding
      TypeBinding deploymentType = schemaBinding.getType(deploymentTypeQName);
      BeanSchemaBindingHelper.initDeploymentHandlers(deploymentType);
   }

   /**
    * Initialize bean part of the schema binding
    * 
    * @param schemaBinding the schema binding
    */
   public static void initBean(SchemaBinding schemaBinding)
   {
      // bean binding
      TypeBinding beanType = schemaBinding.getType(beanTypeQName);
      BeanSchemaBindingHelper.initBeanHandlers(beanType);
   }

   /**
    * Initialize bean factory part of the schema binding
    * 
    * @param schemaBinding the schema binding
    */
   public static void initBeanFactory(SchemaBinding schemaBinding)
   {
      // beanfactory binding
      TypeBinding beanFactoryType = schemaBinding.getType(beanFactoryTypeQName);
      BeanSchemaBindingHelper.initBeanFactoryHandlers(beanFactoryType);
   }

   /**
    * Initialize other parts of the schema binding
    * 
    * @param schemaBinding the schema binding
    */
   public static void initArtifacts(SchemaBinding schemaBinding)
   {
      // classloader binding
      TypeBinding classloaderType = schemaBinding.getType(classloaderTypeQName);
      BeanSchemaBindingHelper.initClassLoaderHandlers(classloaderType);

      // constructor binding
      TypeBinding constructorType = schemaBinding.getType(constructorTypeQName);
      BeanSchemaBindingHelper.initConstructorHandlers(constructorType);

      // parameter binding
      TypeBinding parameterType = schemaBinding.getType(parameterTypeQName);
      BeanSchemaBindingHelper.initParameterHandlers(parameterType);

      // lifecycle binding
      TypeBinding lifecycleType = schemaBinding.getType(lifecycleTypeQName);
      BeanSchemaBindingHelper.initLifecycleHandlers(lifecycleType);

      // annotation binding
      TypeBinding annotationType = schemaBinding.getType(annotationTypeQName);
      BeanSchemaBindingHelper.initAnnotationHandlers(annotationType);

      // install binding
      TypeBinding installType = schemaBinding.getType(installTypeQName);
      BeanSchemaBindingHelper.initInstallHandlers(installType);

      // property binding
      TypeBinding propertyType = schemaBinding.getType(propertyTypeQName);
      BeanSchemaBindingHelper.initPropertyHandlers(propertyType);

      // dependency binding
      TypeBinding dependsType = schemaBinding.getType(dependsTypeQName);
      BeanSchemaBindingHelper.initDependsHandlers(dependsType);

      // demand binding
      TypeBinding demandType = schemaBinding.getType(demandTypeQName);
      BeanSchemaBindingHelper.initDemandHandlers(demandType);

      // supply binding
      TypeBinding supplyType = schemaBinding.getType(supplyTypeQName);
      BeanSchemaBindingHelper.initSupplyHandlers(supplyType);

      // dependency binding
      TypeBinding dependencyType = schemaBinding.getType(dependencyTypeQName);
      BeanSchemaBindingHelper.initDependencyHandlers(dependencyType);

      // injection binding
      TypeBinding injectionType = schemaBinding.getType(injectionTypeQName);
      BeanSchemaBindingHelper.initInjectionHandlers(injectionType);

      // factory binding
      TypeBinding factoryType = schemaBinding.getType(factoryTypeQName);
      BeanSchemaBindingHelper.initFactoryHandlers(factoryType);

      // value binding
      TypeBinding plainValueType = schemaBinding.getType(plainValueTypeQName);
      BeanSchemaBindingHelper.initPlainValueHandlers(plainValueType);

      // value binding
      TypeBinding valueType = schemaBinding.getType(valueTypeQName);
      BeanSchemaBindingHelper.initValueHandlers(valueType);

      // collection binding
      BeanSchemaBindingHelper.configureCollection(schemaBinding, collectionTypeQName);

      // list binding
      BeanSchemaBindingHelper.configureCollection(schemaBinding, listTypeQName);

      // set binding
      BeanSchemaBindingHelper.configureCollection(schemaBinding, setTypeQName);

      // array binding
      BeanSchemaBindingHelper.configureCollection(schemaBinding, arrayTypeQName);

      // map binding
      TypeBinding mapType = schemaBinding.getType(mapTypeQName);
      BeanSchemaBindingHelper.initMapHandlers(mapType);

      // entry binding
      TypeBinding entryType = schemaBinding.getType(entryTypeQName);
      BeanSchemaBindingHelper.initEntryHandlers(entryType);
   }
}
