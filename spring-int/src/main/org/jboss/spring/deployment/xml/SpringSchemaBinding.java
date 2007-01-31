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
package org.jboss.spring.deployment.xml;

import javax.xml.namespace.QName;

import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.binding.sunday.unmarshalling.TypeBinding;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SpringSchemaBinding
{

   /**
    * The namespace
    */
   public static final String SPRING_DEPLOYER_NS = "urn:jboss:spring-beans:2.0";

   /**
    * The beans binding
    */
   public static final QName beansTypeQName = new QName(SPRING_DEPLOYER_NS, "beansType");

   /**
    * The beans element name
    */
   public static final QName beansQName = new QName(SPRING_DEPLOYER_NS, "beans");

   /**
    * The bean binding
    */
   public static final QName beanTypeQName = new QName(SPRING_DEPLOYER_NS, "beanType");

   /**
    * The bean element name
    */
   public static final QName beanQName = new QName(SPRING_DEPLOYER_NS, "bean");

   /**
    * The ref binding
    */
   public static final QName refTypeQName = new QName(SPRING_DEPLOYER_NS, "refType");

   /**
    * The ref element name
    */
   public static final QName refQName = new QName(SPRING_DEPLOYER_NS, "ref");

   /**
    * The constructor arg binding
    */
   public static final QName constructorTypeQName = new QName(SPRING_DEPLOYER_NS, "constructorArgType");

   /**
    * The constructor arg element name
    */
   public static final QName constructorQName = new QName(SPRING_DEPLOYER_NS, "constructor-arg");

   /**
    * The property binding
    */
   public static final QName propertyTypeQName = new QName(SPRING_DEPLOYER_NS, "propertyType");

   /**
    * The property element name
    */
   public static final QName propertyQName = new QName(SPRING_DEPLOYER_NS, "property");

   /**
    * The value binding
    */
   public static final QName valueTypeQName = new QName(SPRING_DEPLOYER_NS, "valueType");

   /**
    * The value element name
    */
   public static final QName valueQName = new QName(SPRING_DEPLOYER_NS, "value");

   /**
    * The null element name
    */
   public static final QName nullQName = new QName(SPRING_DEPLOYER_NS, "null");

   /**
    * The list binding
    */
   public static final QName listTypeQName = new QName(SPRING_DEPLOYER_NS, "listType");

   /**
    * The list element name
    */
   public static final QName listQName = new QName(SPRING_DEPLOYER_NS, "list");

   /**
    * The set binding
    */
   public static final QName setTypeQName = new QName(SPRING_DEPLOYER_NS, "setType");

   /**
    * The set element name
    */
   public static final QName setQName = new QName(SPRING_DEPLOYER_NS, "set");

   /**
    * The map binding
    */
   public static final QName mapTypeQName = new QName(SPRING_DEPLOYER_NS, "mapType");

   /**
    * The map element name
    */
   public static final QName mapQName = new QName(SPRING_DEPLOYER_NS, "map");

   /**
    * The props binding
    */
   public static final QName propsTypeQName = new QName(SPRING_DEPLOYER_NS, "propsType");

   /**
    * The props element name
    */
   public static final QName propsQName = new QName(SPRING_DEPLOYER_NS, "props");

   /**
    * The props binding
    */
   public static final QName propTypeQName = new QName(SPRING_DEPLOYER_NS, "propType");

   /**
    * The prop element name
    */
   public static final QName propQName = new QName(SPRING_DEPLOYER_NS, "prop");

   /**
    * The entry binding
    */
   public static final QName entryTypeQName = new QName(SPRING_DEPLOYER_NS, "entryType");

   /**
    * The entry element name
    */
   public static final QName entryQName = new QName(SPRING_DEPLOYER_NS, "entry");

   /**
    * The key binding
    */
   public static final QName keyTypeQName = new QName(SPRING_DEPLOYER_NS, "keyType");

   /**
    * The key element name
    */
   public static final QName keyQName = new QName(SPRING_DEPLOYER_NS, "key");

   public static void init(SchemaBinding schemaBinding)
   {
      // ignore XB property replacement
      schemaBinding.setReplacePropertyRefs(false);
      // init
      initDeployment(schemaBinding);
      initBean(schemaBinding);
      initArtifacts(schemaBinding);
   }

   /**
    * Initialize beans part of the schema binding
    *
    * @param schemaBinding the schema binding
    */
   public static void initDeployment(SchemaBinding schemaBinding)
   {
      // beans binding
      TypeBinding beansType = schemaBinding.getType(beansTypeQName);
      SpringSchemaBindingHelper.initBeansHandler(beansType);
   }

   /**
    * Initialize single bean part of the schema binding
    *
    * @param schemaBinding the schema binding
    */
   private static void initBean(SchemaBinding schemaBinding)
   {
      // bean binding
      TypeBinding beanType = schemaBinding.getType(beanTypeQName);
      SpringSchemaBindingHelper.initBeanHandler(beanType);
   }

   /**
    * Initialize other parts of the schema binding
    *
    * @param schemaBinding the schema binding
    */
   private static void initArtifacts(SchemaBinding schemaBinding)
   {
      // ref / injection
      TypeBinding refType = schemaBinding.getType(refTypeQName);
      SpringSchemaBindingHelper.initRefHandler(refType);

      // constructor arg
      TypeBinding constructorArgType = schemaBinding.getType(constructorTypeQName);
      SpringSchemaBindingHelper.initConstructorArgHandler(constructorArgType);

      // property binding
      TypeBinding propertyType = schemaBinding.getType(propertyTypeQName);
      SpringSchemaBindingHelper.initPropertyHandler(propertyType);

      // value binding
      TypeBinding valueType = schemaBinding.getType(valueTypeQName);
      SpringSchemaBindingHelper.initValueHandler(valueType);

      // list
      TypeBinding listType = schemaBinding.getType(listTypeQName);
      SpringSchemaBindingHelper.initCollectionHandler(listType);

      // set
      TypeBinding setType = schemaBinding.getType(setTypeQName);
      SpringSchemaBindingHelper.initCollectionHandler(setType);

      // map
      TypeBinding mapType = schemaBinding.getType(mapTypeQName);
      SpringSchemaBindingHelper.initMapHandler(mapType);

      // entry type
      TypeBinding entryType = schemaBinding.getType(entryTypeQName);
      SpringSchemaBindingHelper.initEntryHandler(entryType);

      // key type
      TypeBinding keyType = schemaBinding.getType(keyTypeQName);
      SpringSchemaBindingHelper.initKeyHandler(keyType);

      // props
      TypeBinding propsType = schemaBinding.getType(propsTypeQName);
      SpringSchemaBindingHelper.initPropsHandler(propsType);

      // prop
      TypeBinding propType = schemaBinding.getType(propTypeQName);
      SpringSchemaBindingHelper.initPropHandler(propType);
   }

}
