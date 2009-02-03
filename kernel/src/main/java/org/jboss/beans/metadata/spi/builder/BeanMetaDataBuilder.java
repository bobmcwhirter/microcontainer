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

import java.lang.annotation.Annotation;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.api.model.AutowireType;
import org.jboss.beans.metadata.api.model.InjectOption;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.beans.metadata.spi.RelatedClassMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.ErrorHandlingMode;
import org.jboss.dependency.spi.graph.SearchInfo;

/**
 * BeanMetaDataBuilder contract.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 */
public abstract class BeanMetaDataBuilder
{
   /**
    * Create builder from bean.
    *
    * @param bean bean class name
    * @return new Builder
    */
   public static BeanMetaDataBuilder createBuilder(String bean)
   {
      return BeanMetaDataBuilderFactory.createBuilder(bean);
   }

   /**
    * Create builder from name and bean.
    *
    * @param name bean name
    * @param bean bean class name
    * @return new Builder
    */
   public static BeanMetaDataBuilder createBuilder(String name, String bean)
   {
      return BeanMetaDataBuilderFactory.createBuilder(name, bean);
   }
   
   /**
    * Create builder from BeanMetaData
    * 
    * @param beanMetaData the bean metadata
    * @return new Builder()
    */
   public static BeanMetaDataBuilder createBuilder(BeanMetaData beanMetaData)
   {
      if (beanMetaData instanceof AbstractBeanMetaData)
      {
         return BeanMetaDataBuilderFactory.createBuilder((AbstractBeanMetaData)beanMetaData);
      }
      else
         throw new IllegalArgumentException("Invalid type of bean metadata");
   }

   /**
    * Get the constructed bean metadata 
    * 
    * @return the bean metadata
    */
   public abstract BeanMetaData getBeanMetaData();

   /**
    * Get bean factory from underlying bean meta data.
    *
    * Note: this one includes all nested beans from
    * underlying bean metadata.
    *
     * @return bean meta data factory
    */
   public abstract BeanMetaDataFactory getBeanMetaDataFactory();

   /**
    * Get underlying bean as BeanMetaDataFactory.
    *
    * Note: this method doesn't include nested beans from
    * underlying bean metadata.
    *
     * @return bean meta data factory
    */
   public BeanMetaDataFactory asBeanMetaDataFactory()
   {
      return new BeanMetaDataFactory()
      {
         public List<BeanMetaData> getBeans()
         {
            return Collections.singletonList(getBeanMetaData());
         }
      };
   }

   /**
    * Set the bean name.
    *
    * @param name the name
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setName(String name);

   /**
    * Set the bean.
    *
    * @param bean the bean class name
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setBean(String bean);

   /**
    * Set the aliases
    * 
    * @param aliases the aliases
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setAliases(Set<Object> aliases);

   /**
    * Add related class.
    *
    * @param className the related class name
    * @param enabled the enabled
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addRelatedClass(String className, Object... enabled);

   /**
    * Add related class.
    *
    * @param related the related class
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addRelatedClass(RelatedClassMetaData related);

   /**
    * Set the related
    *
    * @param related the related
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setRelated(Set<RelatedClassMetaData> related);

   /**
    * Add alias.
    *
    * @param alias the alias
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addAlias(Object alias);

   /**
    * Set the annotations
    *
    * @param annotations the annotations
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setAnnotations(Set<String> annotations);

   /**
    * Add annotation.
    *
    * @param annotation the annotation
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addAnnotation(String annotation);

   /**
    * Add annotation.
    *
    * @param annotation the annotation
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addAnnotation(Annotation annotation);

   /**
    * Add annotation.
    *
    * @param annotation the annotation
    * @param replace the replace flag
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addAnnotation(String annotation, boolean replace);

   /**
    * Set the mode
    * 
    * @param modeString the mode
    * @return the builder
    */
   public BeanMetaDataBuilder setMode(String modeString)
   {
      return setMode(ControllerMode.getInstance(modeString));
   }

   /**
    * Set the mode
    * 
    * @param mode the mode
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setMode(ControllerMode mode);

   /**
    * Set the access mode
    *
    * @param mode the access mode
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setAccessMode(BeanAccessMode mode);

   /**
    * Set the error handling mode
    *
    * @param mode the error handling mode
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setErrorHandlingMode(ErrorHandlingMode mode);

   /**
    * Set the autowire type
    *
    * @param type the autowire type
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setAutowireType(AutowireType type);

   /**
    * Is bean autowire candidate.
    *
    * @param candidate the candidate flag
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setAutowireCandidate(boolean candidate);

   /**
    * Set that we don't want to use the deployment classloader
    * 
    * @return the builder
    */

   public BeanMetaDataBuilder setNoClassLoader()
   {
      return setClassLoader(createNull());
   }

   /**
    * Set the classloader
    * 
    * @param classLoader the classloader
    * @return the builder
    */
   public BeanMetaDataBuilder setClassLoader(Object classLoader)
   {
      return setClassLoader(createValue(classLoader));
   }

   /**
    * Set the classloader
    * 
    * @param classLoader the classloader
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setClassLoader(ValueMetaData classLoader);

   /**
    * Set the classloader
    * 
    * @param classLoader the classloader
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setClassLoader(ClassLoaderMetaData classLoader);

   /**
    * Set the factory
    * 
    * @param factory the factory
    * @return the builder
    */
   public BeanMetaDataBuilder setFactory(Object factory)
   {
      return setFactory(createValue(factory));
   }

   /**
    * Set the factory
    * 
    * @param bean the bean name
    * @return the builder
    */
   public BeanMetaDataBuilder setFactory(String bean)
   {
      return setFactory(bean, null);
   }

   /**
    * Set the factory
    * 
    * @param bean the bean name
    * @param property the property name for the factory
    * @return the builder
    */
   public BeanMetaDataBuilder setFactory(String bean, String property)
   {
      return setFactory(createInject(bean, property));
   }

   /**
    * Set the factory
    * 
    * @param factory the factory
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setFactory(ValueMetaData factory);

   /**
    * Set the factory class
    * 
    * @param factoryClass the factory class
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setFactoryClass(String factoryClass);

   /**
    * Set the factory method
    * 
    * @param factoryMethod the factory method
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setFactoryMethod(String factoryMethod);

   /**
    * Set the constructor value
    * 
    * @param value the object "constructed"
    * @return the builder
    */
   
   public BeanMetaDataBuilder setConstructorValue(Object value)
   {
      return setConstructorValue(createValue(value));
   }

   /**
    * Set the constructor value
    * 
    * @param type the type
    * @param value the object "constructed"
    * @return the builder
    */
   
   public BeanMetaDataBuilder setConstructorValue(String type, String value)
   {
      return setConstructorValue(createString(type, value));
   }

   /**
    * Set the constructor value
    * 
    * @param value the object "constructed"
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setConstructorValue(ValueMetaData value);

   /**
    * Add a constructor parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addConstructorParameter(String type, Object value);

   /**
    * Add a constructor parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addConstructorParameter(String type, String value);

   /**
    * Add a constructor parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addConstructorParameter(String type, ValueMetaData value);

   /**
    * Add a property, replace it if it already exists
    * 
    * @param name the property name
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addPropertyMetaData(String name, Object value);

   /**
    * Add a property, replace it if it already exists
    * 
    * @param name the property name
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addPropertyMetaData(String name, String value);

   /**
    * Add a property, replace it if it already exists
    * 
    * @param name the property name
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addPropertyMetaData(String name, ValueMetaData value);

   /**
    * Add a property, replace it if it already exists
    * 
    * @param name the property name
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addPropertyMetaData(String name, Collection<ValueMetaData> value);

   /**
    * Add a property annotation.
    *
    * @param name the property name
    * @param annotation the annotation
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addPropertyAnnotation(String name, String annotation);

   /**
    * Add a property annotation.
    *
    * @param name the property name
    * @param annotation the annotation
    * @param replace the replace flag
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addPropertyAnnotation(String name, String annotation, boolean replace);

   /**
    * Add a property annotation.
    *
    * @param name the property name
    * @param annotation the annotation
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addPropertyAnnotation(String name, Annotation annotation);

   /**
    * Add a property, replace it if it already exists
    * 
    * @param name the property name
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addPropertyMetaData(String name, Map<ValueMetaData, ValueMetaData> value);
   
   /**
    * Should we ignore default create invocation.
    *
    * @return the builder
    */
   public abstract BeanMetaDataBuilder ignoreCreate();

   /**
    * Set the create method
    * 
    * @param methodName the method name
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setCreate(String methodName);

   /**
    * Add a create parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addCreateParameter(String type, Object value);

   /**
    * Add a create parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addCreateParameter(String type, String value);

   /**
    * Add a create parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addCreateParameter(String type, ValueMetaData value);

   /**
    * Should we ignore default start invocation.
    *
    * @return the builder
    */
   public abstract BeanMetaDataBuilder ignoreStart();

   /**
    * Set the start method
    * 
    * @param methodName the method name
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setStart(String methodName);

   /**
    * Add a start parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addStartParameter(String type, Object value);

   /**
    * Add a start parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addStartParameter(String type, String value);

   /**
    * Add a start parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addStartParameter(String type, ValueMetaData value);

   /**
    * Should we ignore default stop invocation.
    *
    * @return the builder
    */
   public abstract BeanMetaDataBuilder ignoreStop();

   /**
    * Set the stop method
    * 
    * @param methodName the method name
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setStop(String methodName);

   /**
    * Add a stop parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addStopParameter(String type, Object value);

   /**
    * Add a stop parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addStopParameter(String type, String value);

   /**
    * Add a stop parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addStopParameter(String type, ValueMetaData value);

   /**
    * Should we ignore default destroy invocation.
    *
    * @return the builder
    */
   public abstract BeanMetaDataBuilder ignoreDestroy();

   /**
    * Set the destroy method
    * 
    * @param methodName the method name
    * @return the builder
    */
   public abstract BeanMetaDataBuilder setDestroy(String methodName);

   /**
    * Add a destroy parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addDestroyParameter(String type, Object value);

   /**
    * Add a destroy parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addDestroyParameter(String type, String value);

   /**
    * Add a destroy parameter
    * 
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addDestroyParameter(String type, ValueMetaData value);

   /**
    * Add a supply
    * 
    * @param supply the supply
    * @return the builder
    */
   public BeanMetaDataBuilder addSupply(Object supply)
   {
      return addSupply(supply, null);
   }

   /**
    * Add a supply
    * 
    * @param supply the supply
    * @param type the supply type
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addSupply(Object supply, String type);

   /**
    * Add a demand
    * 
    * @param demand the demand
    * @return the builder
    */
   public BeanMetaDataBuilder addDemand(Object demand)
   {
      return addDemand(demand, (ControllerState) null, null);
   }

   /**
    * Add a demand
    * 
    * @param demand the demand
    * @param whenRequired when the demand is required
    * @param transformer the transformer
    * @return the builder
    */
   public BeanMetaDataBuilder addDemand(Object demand, String whenRequired, String transformer)
   {
      ControllerState whenRequiredState = null;
      if (whenRequired != null)
         whenRequiredState = new ControllerState(whenRequired);
      return addDemand(demand, whenRequiredState, transformer);
   }

   /**
    * Add a demand
    * 
    * @param demand the demand
    * @param whenRequired when the demand is required
    * @param transformer the transformer
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addDemand(Object demand, ControllerState whenRequired, String transformer);

   /**
    * Add a dependency
    * 
    * @param dependency the dependency
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addDependency(Object dependency);

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName)
   {
      return addInstall(methodName, null);
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String type, Object value)
   {
      return addInstall(methodName, null, type, value);
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String type, String value)
   {
      return addInstall(methodName, null, type, value);
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String type, ValueMetaData value)
   {
      return addInstall(methodName, null, type, value);
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param types the parameter types
    * @param values the values
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String[] types, Object[] values)
   {
      return addInstall(methodName, null, types, values);
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param types the parameter types
    * @param values the values
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String[] types, String[] values)
   {
      return addInstall(methodName, null, types, values);
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param types the parameter types
    * @param values the values
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String[] types, ValueMetaData[] values)
   {
      return addInstall(methodName, null, types, values);
   }

   /**
    * Add an install with a this parameter
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @return the builder
    */
   public BeanMetaDataBuilder addInstallWithThis(String methodName, String bean)
   {
      return addInstallWithThis(methodName, bean, null);
   }

   /**
    * Add an install with a this parameter
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param state the state of the bean
    * @return the builder
    */
   public BeanMetaDataBuilder addInstallWithThis(String methodName, String bean, ControllerState state)
   {
      return addInstallWithThis(methodName, bean, state, null);
   }

   /**
    * Add an install with a this parameter
    *
    * @param methodName the method name
    * @param bean the bean name
    * @param state the state of the bean
    * @param whenRequired the state when to install
    * @return the builder
    */
   public BeanMetaDataBuilder addInstallWithThis(String methodName, String bean, ControllerState state, ControllerState whenRequired)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean, state, whenRequired);
      parameters.addParameterMetaData(null, createThis());
      return this;
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean)
   {
      addInstallWithParameters(methodName, bean);
      return this;
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean, String type, Object value)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean);
      parameters.addParameterMetaData(type, value);
      return this;
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean, String type, String value)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean);
      parameters.addParameterMetaData(type, value);
      return this;
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean, String type, ValueMetaData value)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean);
      parameters.addParameterMetaData(type, value);
      return this;
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param types the parameter types
    * @param values the values
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean, String[] types, Object[] values)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean);
      for (int i = 0; i < types.length; i++)
         parameters.addParameterMetaData(types[i], values[i]);
      return this;
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param types the parameter types
    * @param values the values
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean, String[] types, String[] values)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean);
      for (int i = 0; i < types.length; i++)
         parameters.addParameterMetaData(types[i], values[i]);
      return this;
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param types the parameter types
    * @param values the values
    * @return the builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean, String[] types, ValueMetaData[] values)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean);
      for (int i = 0; i < types.length; i++)
         parameters.addParameterMetaData(types[i], values[i]);
      return this;
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @return the builder
    */
   public ParameterMetaDataBuilder addInstallWithParameters(String methodName)
   {
      return addInstallWithParameters(methodName, null);
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @return the builder
    */
   public ParameterMetaDataBuilder addInstallWithParameters(String methodName, String bean)
   {
      return addInstallWithParameters(methodName, bean, null);
   }

   /**
    * Add an install
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param state the state of the bean
    * @return the parameter builder
    */
   public ParameterMetaDataBuilder addInstallWithParameters(String methodName, String bean, ControllerState state)
   {
      return addInstallWithParameters(methodName, bean, state, null);
   }

   /**
    * Add an install
    *
    * @param methodName the method name
    * @param bean the bean name
    * @param state the state of the bean
    * @param whenRequired the state when to install
    * @return the parameter builder
    */
   public abstract ParameterMetaDataBuilder addInstallWithParameters(String methodName, String bean, ControllerState state, ControllerState whenRequired);

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName)
   {
      return addUninstall(methodName, null);
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String type, Object value)
   {
      return addUninstall(methodName, null, type, value);
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String type, String value)
   {
      return addUninstall(methodName, null, type, value);
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String type, ValueMetaData value)
   {
      return addUninstall(methodName, null, type, value);
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param types the parameter types
    * @param values the values
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String[] types, Object[] values)
   {
      return addUninstall(methodName, null, types, values);
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param types the parameter types
    * @param values the values
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String[] types, String[] values)
   {
      return addUninstall(methodName, null, types, values);
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param types the parameter types
    * @param values the values
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String[] types, ValueMetaData[] values)
   {
      return addUninstall(methodName, null, types, values);
   }

   /**
    * Add an uninstall with a this parameter
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstallWithThis(String methodName, String bean)
   {
      return addUninstallWithThis(methodName, bean, null);
   }

   /**
    * Add an uninstall with a this parameter
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param state the state when to uninstall
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstallWithThis(String methodName, String bean, ControllerState state)
   {
      return addUninstallWithThis(methodName, bean, state, null);
   }

   /**
    * Add an uninstall with a this parameter
    *
    * @param methodName the method name
    * @param bean the bean name
    * @param state the state of the bean
    * @param whenRequired the state when to uninstall
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstallWithThis(String methodName, String bean, ControllerState state, ControllerState whenRequired)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean, state, whenRequired);
      parameters.addParameterMetaData(null, createThis());
      return this;
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean)
   {
      addUninstallWithParameters(methodName, bean);
      return this;
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean, String type, Object value)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean);
      parameters.addParameterMetaData(type, value);
      return this;
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean, String type, String value)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean);
      parameters.addParameterMetaData(type, value);
      return this;
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param type the parameter type
    * @param value the value
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean, String type, ValueMetaData value)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean);
      parameters.addParameterMetaData(type, value);
      return this;
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param types the parameter types
    * @param values the values
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean, String[] types, Object[] values)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean);
      for (int i = 0; i < types.length; ++i)
         parameters.addParameterMetaData(types[i], values[i]);
      return this;
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param types the parameter types
    * @param values the values
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean, String[] types, String[] values)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean);
      for (int i = 0; i < types.length; ++i)
         parameters.addParameterMetaData(types[i], values[i]);
      return this;
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param types the parameter types
    * @param values the values
    * @return the builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean, String[] types, ValueMetaData[] values)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean);
      for (int i = 0; i < types.length; ++i)
         parameters.addParameterMetaData(types[i], values[i]);
      return this;
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @return the builder
    */
   public ParameterMetaDataBuilder addUninstallWithParameters(String methodName)
   {
      return addUninstallWithParameters(methodName, null);
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @return the builder
    */
   public ParameterMetaDataBuilder addUninstallWithParameters(String methodName, String bean)
   {
      return addUninstallWithParameters(methodName, bean, null);
   }

   /**
    * Add an uninstall
    * 
    * @param methodName the method name
    * @param bean the bean name
    * @param state the state of the bean
    * @return the parameter builder
    */
   public ParameterMetaDataBuilder addUninstallWithParameters(String methodName, String bean, ControllerState state)
   {
      return addUninstallWithParameters(methodName, bean, state, null);
   }
   
   /**
    * Add an uninstall
    *
    * @param methodName the method name
    * @param bean the bean name
    * @param state the state of the bean
    * @param whenRequired the state when to uninstall
    * @return the parameter builder
    */
   public abstract ParameterMetaDataBuilder addUninstallWithParameters(String methodName, String bean, ControllerState state, ControllerState whenRequired);

   /**
    * Add property install callback.
    *
    * @param property the property name
    * @return the builder
    */
   public BeanMetaDataBuilder addPropertyInstallCallback(String property)
   {
      return addPropertyInstallCallback(property, null, null);
   }

   /**
    * Add property install callback.
    *
    * @param property the property name
    * @param whenRequired the when required state
    * @return the builder
    */
   public BeanMetaDataBuilder addPropertyInstallCallback(String property, ControllerState whenRequired)
   {
      return addPropertyInstallCallback(property, whenRequired, null);
   }
         
   /**
    * Add property install callback.
    *
    * @param property the property name
    * @param cardinality the cardinality
    * @return the builder
    */
   public BeanMetaDataBuilder addPropertyInstallCallback(String property, Cardinality cardinality)
   {
      return addPropertyInstallCallback(property, null, cardinality);
   }

   /**
    * Add property install callback.
    *
    * @param property the property name
    * @param whenRequired the when required state
    * @param cardinality the cardinality
    * @return the builder
    */
   public BeanMetaDataBuilder addPropertyInstallCallback(String property, ControllerState whenRequired, Cardinality cardinality)
   {
      return addPropertyInstallCallback(property, null, whenRequired, null, cardinality);
   }

   /**
    * Add property install callback.
    *
    * @param property the property name
    * @param signature the property signature
    * @param whenRequired the when required state
    * @param dependentState the dependant state
    * @param cardinality the cardinality
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addPropertyInstallCallback(
         String property,
         String signature,
         ControllerState whenRequired,
         ControllerState dependentState,
         Cardinality cardinality);

   /**
    * Add property uninstall callback.
    *
    * @param property the property name
    * @return the builder
    */
   public BeanMetaDataBuilder addPropertyUninstallCallback(String property)
   {
      return addPropertyUninstallCallback(property, null, null);
   }

   /**
    * Add property uninstall callback.
    *
    * @param property the property name
    * @param whenRequired the when required state
    * @return the builder
    */
   public BeanMetaDataBuilder addPropertyUninstallCallback(String property, ControllerState whenRequired)
   {
      return addPropertyUninstallCallback(property, whenRequired, null);
   }

   /**
    * Add property uninstall callback.
    *
    * @param property the property name
    * @param cardinality the cardinality
    * @return the builder
    */
   public BeanMetaDataBuilder addPropertyUninstallCallback(String property, Cardinality cardinality)
   {
      return addPropertyUninstallCallback(property, null, cardinality);
   }

   /**
    * Add property uninstall callback.
    *
    * @param property the property name
    * @param whenRequired the when required state
    * @param cardinality the cardinality
    * @return the builder
    */
   public BeanMetaDataBuilder addPropertyUninstallCallback(String property, ControllerState whenRequired, Cardinality cardinality)
   {
      return addPropertyUninstallCallback(property, null, whenRequired, null, cardinality);
   }

   /**
    * Add property uninstall callback.
    *
    * @param property the property name
    * @param signature the property signature
    * @param whenRequired the when required state
    * @param dependentState the dependant state
    * @param cardinality the cardinality
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addPropertyUninstallCallback(
         String property,
         String signature,
         ControllerState whenRequired,
         ControllerState dependentState,
         Cardinality cardinality);

   /**
    * Add method install callback.
    *
    * @param method the method name
    * @return the builder
    */
   public BeanMetaDataBuilder addMethodInstallCallback(String method)
   {
      return addMethodInstallCallback(method, null, null);
   }

   /**
    * Add method install callback.
    *
    * @param method the method name
    * @param whenRequired the when required state
    * @return the builder
    */
   public BeanMetaDataBuilder addMethodInstallCallback(String method, ControllerState whenRequired)
   {
      return addMethodInstallCallback(method, whenRequired, null);
   }

   /**
    * Add method install callback.
    *
    * @param method the method name
    * @param cardinality the cardinality
    * @return the builder
    */
   public BeanMetaDataBuilder addMethodInstallCallback(String method, Cardinality cardinality)
   {
      return addMethodInstallCallback(method, null, cardinality);
   }

   /**
    * Add method install callback.
    *
    * @param method the method name
    * @param whenRequired the when required state
    * @param cardinality the cardinality
    * @return the builder
    */
   public BeanMetaDataBuilder addMethodInstallCallback(String method, ControllerState whenRequired, Cardinality cardinality)
   {
      return addMethodInstallCallback(method, null, whenRequired, null, cardinality);
   }

   /**
    * Add method install callback.
    *
    * @param method the method name
    * @param signature the method signature
    * @param whenRequired the when required state
    * @param dependentState the dependant state
    * @param cardinality the cardinality
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addMethodInstallCallback(
         String method,
         String signature,
         ControllerState whenRequired,
         ControllerState dependentState,
         Cardinality cardinality);

   /**
    * Add method uninstall callback.
    *
    * @param method the method name
    * @return the builder
    */
   public BeanMetaDataBuilder addMethodUninstallCallback(String method)
   {
      return addMethodUninstallCallback(method, null, null);
   }

   /**
    * Add method uninstall callback.
    *
    * @param method the method name
    * @param whenRequired the when required state
    * @return the builder
    */
   public BeanMetaDataBuilder addMethodUninstallCallback(String method, ControllerState whenRequired)
   {
      return addMethodUninstallCallback(method, whenRequired, null);
   }

   /**
    * Add method uninstall callback.
    *
    * @param method the method name
    * @param cardinality the cardinality
    * @return the builder
    */
   public BeanMetaDataBuilder addMethodUninstallCallback(String method, Cardinality cardinality)
   {
      return addMethodUninstallCallback(method, null, cardinality);
   }

   /**
    * Add method uninstall callback.
    *
    * @param method the method name
    * @param whenRequired the when required state
    * @param cardinality the cardinality
    * @return the builder
    */
   public BeanMetaDataBuilder addMethodUninstallCallback(String method, ControllerState whenRequired, Cardinality cardinality)
   {
      return addMethodUninstallCallback(method, null, whenRequired, null, cardinality);
   }

   /**
    * Add method uninstall callback.
    *
    * @param method the method name
    * @param signature the method signature
    * @param whenRequired the when required state
    * @param dependentState the dependant state
    * @param cardinality the cardinality
    * @return the builder
    */
   public abstract BeanMetaDataBuilder addMethodUninstallCallback(
         String method,
         String signature,
         ControllerState whenRequired,
         ControllerState dependentState,
         Cardinality cardinality);

   /**
    * Create related class name.
    *
    * @param className the related class name
    * @param enabled the enabled
    * @return new related class meta data
    */
   public abstract RelatedClassMetaData createRelated(String className, Object... enabled);

   /**
    * Create a null value
    * 
    * @return the null value
    */
   public abstract ValueMetaData createNull();
   
   /**
    * Create a this value
    * 
    * @return the this value
    */
   public abstract ValueMetaData createThis();
   
   /**
    * Create a value
    * 
    * @param value the already constructed value
    * @return the value
    */
   public abstract ValueMetaData createValue(Object value);
   
   /**
    * Create a string value
    * 
    * @param type the type to be converted into
    * @param value the value 
    * @return the string value
    */
   public abstract ValueMetaData createString(String type, String value);
   
   /**
    * Create an injection
    * 
    * @param bean the bean to inject
    * @return the injection
    */
   public ValueMetaData createInject(Object bean)
   {
      return createInject(bean, null, null, null);
   }
   
   /**
    * Create an injection
    * 
    * @param bean the bean to inject
    * @param property the property of the bean
    * @return the injection
    */
   public ValueMetaData createInject(Object bean, String property)
   {
      return createInject(bean, property, null, null);
   }
   
   /**
    * Create an injection
    * 
    * @param bean the bean to inject
    * @param property the property of the bean
    * @param whenRequired when the injection is required
    * @param dependentState the state of the injected bean
    * @return the injection
    */
   public ValueMetaData createInject(Object bean, String property, ControllerState whenRequired, ControllerState dependentState)
   {
      return createInject(bean, property, whenRequired, dependentState, null);
   }

   /**
    * Create an injection
    *
    * @param bean the bean to inject
    * @param property the property of the bean
    * @param whenRequired when the injection is required
    * @param dependentState the state of the injected bean
    * @param search the search info
    * @return the injection
    */
   public abstract ValueMetaData createInject(Object bean, String property, ControllerState whenRequired, ControllerState dependentState, SearchInfo search);

   /**
    * Create contextual injection.
    *
    * @return the contextual injection
    */
   public ValueMetaData createContextualInject()
   {
      return createContextualInject(null, null);
   }

   /**
    * Create contextual injection.
    *
    * @param whenRequired when the injection is required
    * @param dependentState the state of the injected bean
    * @return the contextual injection
    */
   public ValueMetaData createContextualInject(ControllerState whenRequired, ControllerState dependentState)
   {
      return createContextualInject(whenRequired, dependentState, null, null);
   }

   /**
    * Create contextual injection.
    *
    * @param whenRequired when the injection is required
    * @param dependentState the state of the injected bean
    * @param autowire the autowire type
    * @param option the inject option
    * @return the contextual injection
    */
   public ValueMetaData createContextualInject(ControllerState whenRequired, ControllerState dependentState, AutowireType autowire, InjectOption option)
   {
      return createContextualInject(whenRequired, dependentState, autowire, option, null);
   }

   /**
    * Create contextual injection.
    *
    * @param whenRequired when the injection is required
    * @param dependentState the state of the injected bean
    * @param autowire the autowire type
    * @param option the inject option
    * @param search the search info
    * @return the contextual injection
    */
   public abstract ValueMetaData createContextualInject(ControllerState whenRequired, ControllerState dependentState, AutowireType autowire, InjectOption option, SearchInfo search);

   /**
    * Create a new collection
    * 
    * @return the collection
    */
   public Collection<ValueMetaData> createCollection()
   {
      return createCollection(null, null);
   }
   
   /**
    * Create a new collection
    * 
    * @param collectionType the collection type
    * @param elementType the element type
    * @return the collection
    */
   public abstract Collection<ValueMetaData> createCollection(String collectionType, String elementType);
   
   /**
    * Create a new list
    * 
    * @return the list
    */
   public List<ValueMetaData> createList()
   {
      return createList(null, null);
   }
   
   /**
    * Create a new list
    * 
    * @param listType the list type
    * @param elementType the element type
    * @return the list
    */
   public abstract List<ValueMetaData> createList(String listType, String elementType);
   
   /**
    * Create a new set
    * 
    * @return the set
    */
   public Set<ValueMetaData> createSet()
   {
      return createSet(null, null);
   }
   
   /**
    * Create a new set
    * 
    * @param setType the set type
    * @param elementType the element type
    * @return the set
    */
   public abstract Set<ValueMetaData> createSet(String setType, String elementType);
   
   /**
    * Create a new array
    * 
    * @return the array
    */
   public List<ValueMetaData> createArray()
   {
      return createArray(null, null);
   }
   
   /**
    * Create a new array
    * 
    * @param arrayType the array type
    * @param elementType the element type
    * @return the set
    */
   public abstract List<ValueMetaData> createArray(String arrayType, String elementType);
   
   /**
    * Create a new map
    * 
    * @return the map
    */
   public Map<ValueMetaData, ValueMetaData> createMap()
   {
      return createMap(null, null, null);
   }
   
   /**
    * Create a new map
    * 
    * @param mapType the map type
    * @param keyType the key type
    * @param valueType the value type
    * @return the map
    */
   public abstract Map<ValueMetaData, ValueMetaData> createMap(String mapType, String keyType, String valueType);
}
