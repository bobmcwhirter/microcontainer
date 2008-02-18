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

import org.jboss.kernel.plugins.deployment.xml.BeanPropertyInterceptor;
import org.jboss.kernel.plugins.deployment.xml.DeploymentAliasInterceptor;
import org.jboss.kernel.plugins.deployment.xml.DeploymentBeanInterceptor;
import org.jboss.kernel.plugins.deployment.xml.DeploymentWildcardHandler;
import org.jboss.kernel.plugins.deployment.xml.EntryHandler;
import org.jboss.kernel.plugins.deployment.xml.EntryKeyInterceptor;
import org.jboss.kernel.plugins.deployment.xml.EntryValueInterceptor;
import org.jboss.kernel.plugins.deployment.xml.MapEntryInterceptor;
import org.jboss.kernel.plugins.deployment.xml.NamedAliasHandler;
import org.jboss.kernel.plugins.deployment.xml.NullValueElementInterceptor;
import org.jboss.kernel.plugins.deployment.xml.PlainValueCharactersHandler;
import org.jboss.kernel.plugins.deployment.xml.PropHandler;
import org.jboss.kernel.plugins.deployment.xml.PropertiesHandler;
import org.jboss.kernel.plugins.deployment.xml.PropertyCharactersHandler;
import org.jboss.kernel.plugins.deployment.xml.PropertyHandler;
import org.jboss.kernel.plugins.deployment.xml.ValueMetaDataElementInterceptor;
import org.jboss.kernel.plugins.deployment.xml.ValueWildcardHandler;
import org.jboss.xb.binding.sunday.unmarshalling.TypeBinding;
import org.jboss.xb.binding.sunday.unmarshalling.WildcardBinding;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SpringSchemaBindingHelper
{

   public static void initBeansHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(SpringBeansHandler.HANDLER);
      // handle import
      typeBinding.pushInterceptor(SpringSchemaBinding.importQName, ImportInterceptor.INTERCEPTOR);
      // handle aliases
      typeBinding.pushInterceptor(SpringSchemaBinding.aliasQName, DeploymentAliasInterceptor.INTERCEPTOR);
      // handle beans
      typeBinding.pushInterceptor(SpringSchemaBinding.beanQName, DeploymentBeanInterceptor.INTERCEPTOR);
      // Deployment can take wildcards
      typeBinding.getWildcard().setWildcardHandler(DeploymentWildcardHandler.HANDLER);
   }

   public static void initAliasHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(NamedAliasHandler.NAMED_ALIAS_HANDLER);
   }

   public static void initImportHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(ImportHandler.HANDLER);
   }

   public static void initBeanHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(SpringBeanHandler.HANDLER);
      // handle constructor-arg
      typeBinding.pushInterceptor(SpringSchemaBinding.constructorQName, ConstructorArgInterceptor.INTERCEPTOR);
      // handle properties
      typeBinding.pushInterceptor(SpringSchemaBinding.propertyQName, BeanPropertyInterceptor.INTERCEPTOR);
      // todo lookup-method
      // todo replaced method
   }

   public static void initRefHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(RefHandler.HANDLER);
   }

   public static void initConstructorArgHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(ConstructorArgHandler.HANDLER);
      // configure
      configureValueBindings(typeBinding);
   }

   public static void initPropertyHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(PropertyHandler.HANDLER);
      // property can take characters
      typeBinding.setSimpleType(PropertyCharactersHandler.HANDLER);
      // configure
      configureValueBindings(typeBinding);
   }

   // here value only takes simple type
   public static void initValueHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(SpringPlainValueHandler.SPRING_PLAIN_VALUE_HANDLER);
      // value can take characters
      typeBinding.setSimpleType(PlainValueCharactersHandler.HANDLER);
   }

   public static void initCollectionHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(SpringCollectionHandler.HANDLER);
      // configure
      configureValueBindings(typeBinding);
   }

   public static void initMapHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(SpringMapHandler.HANDLER);
      // entry has an entry
      typeBinding.pushInterceptor(SpringSchemaBinding.entryQName, MapEntryInterceptor.INTERCEPTOR);
   }

   public static void initPropsHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(PropertiesHandler.HANDLER);
      // props
      typeBinding.pushInterceptor(SpringSchemaBinding.propQName, MapEntryInterceptor.INTERCEPTOR);
   }

   public static void initEntryHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(EntryHandler.HANDLER);

      // entry has a key
      typeBinding.pushInterceptor(SpringSchemaBinding.keyQName, EntryKeyInterceptor.INTERCEPTOR);

      // entry has bean
      typeBinding.pushInterceptor(SpringSchemaBinding.beanQName, EntryValueInterceptor.INTERCEPTOR);

      // entry has ref
      typeBinding.pushInterceptor(SpringSchemaBinding.refQName, EntryValueInterceptor.INTERCEPTOR);

      // entry has value
      typeBinding.pushInterceptor(SpringSchemaBinding.valueQName, StringEntryValueInterceptor.STRING_ENTRY_VALUE_INTERCEPTOR);

      // entry can take a list
      typeBinding.pushInterceptor(SpringSchemaBinding.listQName, EntryValueInterceptor.INTERCEPTOR);

      // entry can take a set
      typeBinding.pushInterceptor(SpringSchemaBinding.setQName, EntryValueInterceptor.INTERCEPTOR);

      // entry can take a map
      typeBinding.pushInterceptor(SpringSchemaBinding.mapQName, EntryValueInterceptor.INTERCEPTOR);

      // entry has a null
      typeBinding.pushInterceptor(SpringSchemaBinding.nullQName, EntryValueInterceptor.INTERCEPTOR);
   }

   public static void initPropHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(PropHandler.HANDLER);
      // property takes characters
      typeBinding.setSimpleType(PropertyCharactersHandler.HANDLER);
   }

   public static void initKeyHandler(TypeBinding typeBinding)
   {
      typeBinding.setHandler(SpringMapKeyHandler.HANDLER);
      // key has a value
      configureValueBindings(typeBinding);
   }

   public static void configureValueBindings(TypeBinding typeBinding)
   {
      // type has beans
      typeBinding.pushInterceptor(SpringSchemaBinding.beanQName, ValueMetaDataElementInterceptor.VALUES);

      // type has refs
      typeBinding.pushInterceptor(SpringSchemaBinding.refQName, ValueMetaDataElementInterceptor.VALUES);

      // type has values
      typeBinding.pushInterceptor(SpringSchemaBinding.valueQName, ValueMetaDataElementInterceptor.VALUES);

      // type can take a list
      typeBinding.pushInterceptor(SpringSchemaBinding.listQName, ValueMetaDataElementInterceptor.VALUES);

      // type can take a set
      typeBinding.pushInterceptor(SpringSchemaBinding.setQName, ValueMetaDataElementInterceptor.VALUES);

      // type can take a map
      typeBinding.pushInterceptor(SpringSchemaBinding.mapQName, ValueMetaDataElementInterceptor.VALUES);

      // type has a null
      typeBinding.pushInterceptor(SpringSchemaBinding.nullQName, NullValueElementInterceptor.NULLVALUES);

      // type has wildcard
      WildcardBinding wcb = typeBinding.getWildcard();
      if (wcb == null)
         throw new IllegalStateException("Missing wildcard binding for type: " + typeBinding.getQName());
      wcb.setWildcardHandler(ValueWildcardHandler.WILDCARD);
   }

}
