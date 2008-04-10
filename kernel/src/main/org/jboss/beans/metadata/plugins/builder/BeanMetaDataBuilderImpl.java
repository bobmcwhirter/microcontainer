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
package org.jboss.beans.metadata.plugins.builder;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.plugins.AbstractArrayMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractCallbackMetaData;
import org.jboss.beans.metadata.plugins.AbstractClassLoaderMetaData;
import org.jboss.beans.metadata.plugins.AbstractCollectionMetaData;
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.beans.metadata.plugins.AbstractDemandMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.plugins.AbstractListMetaData;
import org.jboss.beans.metadata.plugins.AbstractMapMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.AbstractSetMetaData;
import org.jboss.beans.metadata.plugins.AbstractSupplyMetaData;
import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.plugins.ThisValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractAnnotationMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.beans.metadata.spi.DependencyMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.AnnotationMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.beans.metadata.spi.builder.ParameterMetaDataBuilder;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;

/**
 * Helper class.
 * Similar to StringBuffer, methods return current instance of BeanMetaDataBuilder.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 */
class BeanMetaDataBuilderImpl extends BeanMetaDataBuilder
{
   /** The bean metadata */
   private AbstractBeanMetaData beanMetaData;

   /** The constructor builder */
   private ParameterMetaDataBuilderImpl<AbstractConstructorMetaData> constructorBuilder;
   
   /** The create lifecycle builder */
   private LifecycleMetaDataBuilder createBuilder;
   
   /** The start lifecycle builder */
   private LifecycleMetaDataBuilder startBuilder;
   
   /** The stop lifecycle builder */
   private LifecycleMetaDataBuilder stopBuilder;
   
   /** The destroy lifecycle builder */
   private LifecycleMetaDataBuilder destroyBuilder;

   /** The install builder */
   private StateActionBuilder<AbstractInstallMetaData> installBuilder;
   
   /** The uninstall builder */
   private StateActionBuilder<AbstractInstallMetaData> uninstallBuilder;

   /** The incallback builder */
   private StateActionBuilder<AbstractCallbackMetaData> propIncallbackBuilder;

   /** The uncallback builder */
   private StateActionBuilder<AbstractCallbackMetaData> propUncallbackBuilder;

   /** The incallback builder */
   private StateActionBuilder<AbstractCallbackMetaData> incallbackBuilder;

   /** The uncallback builder */
   private StateActionBuilder<AbstractCallbackMetaData> uncallbackBuilder;

   /**
    * Create a new BeanMetaDataBuilderImpl.
    * 
    * @param bean the bean
    */
   public BeanMetaDataBuilderImpl(String bean)
   {
      this(new AbstractBeanMetaData(bean));
   }

   /**
    * Create a new BeanMetaDataBuilderImpl.
    * 
    * @param name the bean name
    * @param bean the bean
    */
   public BeanMetaDataBuilderImpl(String name, String bean)
   {
      this(new AbstractBeanMetaData(name, bean));
   }

   /**
    * Create a new BeanMetaDataBuilderImpl.
    * 
    * @param beanMetaData the bean metadata
    */
   public BeanMetaDataBuilderImpl(AbstractBeanMetaData beanMetaData)
   {
      this.beanMetaData = beanMetaData;
      // lifecycle builders
      createBuilder = new CreateLifecycleMetaDataBuilder(beanMetaData);
      startBuilder = new StartLifecycleMetaDataBuilder(beanMetaData);
      stopBuilder = new StopLifecycleMetaDataBuilder(beanMetaData);
      destroyBuilder = new DestroyLifecycleMetaDataBuilder(beanMetaData);
      // install
      installBuilder = new InstallMetaDataBuilder(beanMetaData);
      uninstallBuilder = new UninstallMetaDataBuilder(beanMetaData);
      // callback
      propIncallbackBuilder = new PropertyInstallCallbackMetaDataBuilder(beanMetaData);
      propUncallbackBuilder = new PropertyUninstallCallbackMetaDataBuilder(beanMetaData);
      incallbackBuilder = new InstallCallbackMetaDataBuilder(beanMetaData);
      uncallbackBuilder = new UninstallCallbackMetaDataBuilder(beanMetaData);
   }

   public BeanMetaData getBeanMetaData()
   {
      return beanMetaData;
   }

   public BeanMetaDataBuilder setName(String name)
   {
      beanMetaData.setName(name);
      return this;
   }

   public BeanMetaDataBuilder setBean(String bean)
   {
      beanMetaData.setBean(bean);
      return this;
   }

   public BeanMetaDataBuilder setAliases(Set<Object> aliases)
   {
      beanMetaData.setAliases(aliases);
      return this;
   }

   public BeanMetaDataBuilder addAlias(Object alias)
   {
      Set<Object> aliases = beanMetaData.getAliases();
      if (aliases == null)
      {
         aliases = new HashSet<Object>();
         beanMetaData.setAliases(aliases);
      }
      aliases.add(alias);
      return this;
   }

   public BeanMetaDataBuilder setAnnotations(Set<String> annotations)
   {
      if (annotations != null && annotations.isEmpty() == false)
      {
         Set<AnnotationMetaData> amds = new HashSet<AnnotationMetaData>();
         for (String annotation : annotations)
         {
            AbstractAnnotationMetaData amd = new AbstractAnnotationMetaData();
            amd.setAnnotation(annotation);
         }
         beanMetaData.setAnnotations(amds);
      }
      return this;
   }

   /**
    * Get the annotations.
    *
    * @return the annotations
    */
   protected Set<AnnotationMetaData> getAnnotations()
   {
      Set<AnnotationMetaData> annotations = beanMetaData.getAnnotations();
      if (annotations == null)
      {
         annotations = new HashSet<AnnotationMetaData>();
         beanMetaData.setAnnotations(annotations);
      }
      return annotations;
   }

   public BeanMetaDataBuilder addAnnotation(String annotation)
   {
      Set<AnnotationMetaData> annotations = getAnnotations();
      AbstractAnnotationMetaData amd = new AbstractAnnotationMetaData();
      amd.setAnnotation(annotation);
      annotations.add(amd);
      return this;
   }

   public BeanMetaDataBuilder addAnnotation(String annotation, boolean replace)
   {
      Set<AnnotationMetaData> annotations = getAnnotations();
      AbstractAnnotationMetaData amd = new AbstractAnnotationMetaData();
      amd.setAnnotation(annotation);
      amd.setReplace(replace);
      annotations.add(amd);
      return this;
   }

   public BeanMetaDataBuilder setMode(ControllerMode mode)
   {
      beanMetaData.setMode(mode);
      return this;
   }

   public BeanMetaDataBuilder setAccessMode(BeanAccessMode mode)
   {
      beanMetaData.setAccessMode(mode);
      return this;
   }

   public BeanMetaDataBuilder setClassLoader(ValueMetaData classLoader)
   {
      beanMetaData.setClassLoader(new AbstractClassLoaderMetaData(classLoader));
      return this;
   }

   public BeanMetaDataBuilder setClassLoader(ClassLoaderMetaData classLoader)
   {
      beanMetaData.setClassLoader(classLoader);
      return this;
   }

   protected void checkConstructorBuilder()
   {
      AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) beanMetaData.getConstructor();
      if (constructor == null)
      {
         constructor = new AbstractConstructorMetaData();
         beanMetaData.setConstructor(constructor);
         constructorBuilder = new ParameterMetaDataBuilderImpl<AbstractConstructorMetaData>(constructor);
      }
   }

   public BeanMetaDataBuilder setFactory(ValueMetaData factory)
   {
      checkConstructorBuilder();
      AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) beanMetaData.getConstructor();
      constructor.setFactory(factory);
      return this;
   }

   public BeanMetaDataBuilder setFactoryClass(String factoryClass)
   {
      checkConstructorBuilder();
      AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) beanMetaData.getConstructor();
      constructor.setFactoryClass(factoryClass);
      return this;
   }

   public BeanMetaDataBuilder setFactoryMethod(String factoryMethod)
   {
      checkConstructorBuilder();
      AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) beanMetaData.getConstructor();
      constructor.setFactoryMethod(factoryMethod);
      return this;
   }

   public BeanMetaDataBuilder setConstructorValue(ValueMetaData value)
   {
      checkConstructorBuilder();
      AbstractConstructorMetaData constructor = (AbstractConstructorMetaData) beanMetaData.getConstructor();
      constructor.setValue(value);
      return this;
   }

   public BeanMetaDataBuilder addConstructorParameter(String type, Object value)
   {
      checkConstructorBuilder();
      constructorBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addConstructorParameter(String type, String value)
   {
      checkConstructorBuilder();
      constructorBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addConstructorParameter(String type, ValueMetaData value)
   {
      checkConstructorBuilder();
      constructorBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addPropertyMetaData(String name, Object value)
   {
      Set<PropertyMetaData> properties = getProperties();
      removeProperty(properties, name);
      properties.add(new AbstractPropertyMetaData(name, value));
      return this;
   }

   public BeanMetaDataBuilder addPropertyMetaData(String name, String value)
   {
      Set<PropertyMetaData> properties = getProperties();
      removeProperty(properties, name);
      properties.add(new AbstractPropertyMetaData(name, value));
      return this;
   }

   public BeanMetaDataBuilder addPropertyMetaData(String name, ValueMetaData value)
   {
      Set<PropertyMetaData> properties = getProperties();
      removeProperty(properties, name);
      properties.add(new AbstractPropertyMetaData(name, value));
      return this;
   }

   public BeanMetaDataBuilder addPropertyMetaData(String name, Collection<ValueMetaData> value)
   {
      Set<PropertyMetaData> properties = getProperties();
      removeProperty(properties, name);
      
      if (value instanceof ValueMetaData)
      {
         properties.add(new AbstractPropertyMetaData(name, (ValueMetaData)value));
      }
      else
      {
         properties.add(new AbstractPropertyMetaData(name, value));
      }
      return this;
   }

   public BeanMetaDataBuilder addPropertyMetaData(String name, Map<ValueMetaData, ValueMetaData> value)
   {
      Set<PropertyMetaData> properties = getProperties();
      removeProperty(properties, name);

      if (value instanceof ValueMetaData)
      {
         properties.add(new AbstractPropertyMetaData(name, (ValueMetaData)value));
      }
      else
      {
         properties.add(new AbstractPropertyMetaData(name, value));
      }
      return this;
   }
   
   private Set<PropertyMetaData> removeProperty(Set<PropertyMetaData> properties, String name)
   {
      for (Iterator<PropertyMetaData> it = properties.iterator() ; it.hasNext() ; )
      {
         PropertyMetaData property = it.next();
         if (name.equals(property.getName()))
         {
            it.remove();
         }
      }
      return properties;
   }
   
   private Set<PropertyMetaData> getProperties()
   {
      Set<PropertyMetaData> properties = beanMetaData.getProperties();
      if (properties == null)
      {
         properties = new HashSet<PropertyMetaData>();
         beanMetaData.setProperties(properties);
      }
      return properties;
   }

   public BeanMetaDataBuilder setCreate(String methodName)
   {
      createBuilder.createStateActionMetaData(methodName);
      return this;
   }

   public BeanMetaDataBuilder addCreateParameter(String type, Object value)
   {
      createBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addCreateParameter(String type, String value)
   {
      createBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addCreateParameter(String type, ValueMetaData value)
   {
      createBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder setStart(String methodName)
   {
      startBuilder.createStateActionMetaData(methodName);
      return this;
   }

   public BeanMetaDataBuilder addStartParameter(String type, Object value)
   {
      startBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addStartParameter(String type, String value)
   {
      startBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addStartParameter(String type, ValueMetaData value)
   {
      startBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder setStop(String methodName)
   {
      stopBuilder.createStateActionMetaData(methodName);
      return this;
   }

   public BeanMetaDataBuilder addStopParameter(String type, Object value)
   {
      stopBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addStopParameter(String type, String value)
   {
      stopBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addStopParameter(String type, ValueMetaData value)
   {
      stopBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder setDestroy(String methodName)
   {
      destroyBuilder.createStateActionMetaData(methodName);
      return this;
   }

   public BeanMetaDataBuilder addDestroyParameter(String type, Object value)
   {
      destroyBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addDestroyParameter(String type, String value)
   {
      destroyBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addDestroyParameter(String type, ValueMetaData value)
   {
      destroyBuilder.addParameterMetaData(type, value);
      return this;
   }

   public BeanMetaDataBuilder addSupply(Object supply, String type)
   {
      Set<SupplyMetaData> supplies = beanMetaData.getSupplies();
      if (supplies == null)
      {
         supplies = new HashSet<SupplyMetaData>();
         beanMetaData.setSupplies(supplies);
      }
      AbstractSupplyMetaData asmd = new AbstractSupplyMetaData(supply);
      if (type != null)
         asmd.setType(type);
      supplies.add(asmd);
      return this;
   }

   public BeanMetaDataBuilder addDemand(Object demand, ControllerState whenRequired, String transformer)
   {
      Set<DemandMetaData> demands = beanMetaData.getDemands();
      if (demands == null)
      {
         demands = new HashSet<DemandMetaData>();
         beanMetaData.setDemands(demands);
      }
      AbstractDemandMetaData admd = new AbstractDemandMetaData(demand);
      if (whenRequired != null)
         admd.setWhenRequired(whenRequired);
      if (transformer != null)
         admd.setTransformer(transformer);
      demands.add(admd);
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

   public ParameterMetaDataBuilder addInstallWithParameters(String methodName, String bean, ControllerState state, ControllerState whenRequired)
   {
      AbstractInstallMetaData install = installBuilder.createStateActionMetaData(methodName);
      install.setBean(bean);
      if (state != null)
         install.setDependentState(state);
      if (whenRequired != null)
         install.setState(whenRequired);
      return new ParameterMetaDataBuilderImpl<AbstractInstallMetaData>(install);
   }

   public ParameterMetaDataBuilder addUninstallWithParameters(String methodName, String bean, ControllerState state, ControllerState whenRequired)
   {
      AbstractInstallMetaData uninstall = uninstallBuilder.createStateActionMetaData(methodName);
      uninstall.setBean(bean);
      if (state != null)
         uninstall.setDependentState(state);
      if (whenRequired != null)
         uninstall.setState(whenRequired);
      return new ParameterMetaDataBuilderImpl<AbstractInstallMetaData>(uninstall);
   }

   public BeanMetaDataBuilder addPropertyInstallCallback(String property, String signature, ControllerState whenRequired, ControllerState dependentState, Cardinality cardinality)
   {
      AbstractCallbackMetaData callback = propIncallbackBuilder.createStateActionMetaData(property);
      callback.setSignature(signature);
      callback.setState(whenRequired);
      if (dependentState != null)
         callback.setDependentState(dependentState);
      callback.setCardinality(cardinality);
      return this;
   }

   public BeanMetaDataBuilder addPropertyUninstallCallback(String property, String signature, ControllerState whenRequired, ControllerState dependentState, Cardinality cardinality)
   {
      AbstractCallbackMetaData callback = propUncallbackBuilder.createStateActionMetaData(property);
      callback.setSignature(signature);
      callback.setState(whenRequired);
      if (dependentState != null)
         callback.setDependentState(dependentState);
      callback.setCardinality(cardinality);
      return this;
   }

   public BeanMetaDataBuilder addMethodInstallCallback(String method, String signature, ControllerState whenRequired, ControllerState dependentState, Cardinality cardinality)
   {
      AbstractCallbackMetaData callback = incallbackBuilder.createStateActionMetaData(method);
      callback.setSignature(signature);
      callback.setState(whenRequired);
      if (dependentState != null)
         callback.setDependentState(dependentState);
      callback.setCardinality(cardinality);
      return this;
   }

   public BeanMetaDataBuilder addMethodUninstallCallback(String method, String signature, ControllerState whenRequired, ControllerState dependentState, Cardinality cardinality)
   {
      AbstractCallbackMetaData callback = uncallbackBuilder.createStateActionMetaData(method);
      callback.setSignature(signature);
      callback.setState(whenRequired);
      if (dependentState != null)
         callback.setDependentState(dependentState);
      callback.setCardinality(cardinality);
      return this;
   }

   public ValueMetaData createNull()
   {
      return new AbstractValueMetaData();
   }

   public ValueMetaData createThis()
   {
      return new ThisValueMetaData();
   }

   public ValueMetaData createValue(Object value)
   {
      return new AbstractValueMetaData(value);
   }

   public ValueMetaData createString(String type, String value)
   {
      StringValueMetaData result = new StringValueMetaData(value);
      result.setType(type);
      return result;
   }

   public ValueMetaData createInject(Object bean, String property, ControllerState whenRequired, ControllerState dependentState)
   {
      AbstractDependencyValueMetaData result = new AbstractDependencyValueMetaData(bean, property);
      if (whenRequired != null)
         result.setWhenRequiredState(whenRequired);
      if (dependentState != null)
         result.setDependentState(dependentState);
      return result;
   }

   @SuppressWarnings("unchecked")
   public Collection<ValueMetaData> createCollection(String collectionType, String elementType)
   {
      AbstractCollectionMetaData collection = new AbstractCollectionMetaData();
      if (collectionType != null)
         collection.setType(collectionType);
      if (elementType != null)
         collection.setElementType(elementType);
      return (Collection) collection;
   }

   @SuppressWarnings("unchecked")
   public List<ValueMetaData> createList(String listType, String elementType)
   {
      AbstractListMetaData collection = new AbstractListMetaData();
      if (listType != null)
         collection.setType(listType);
      if (elementType != null)
         collection.setElementType(elementType);
      return (List) collection;
   }

   @SuppressWarnings("unchecked")
   public Set<ValueMetaData> createSet(String setType, String elementType)
   {
      AbstractSetMetaData collection = new AbstractSetMetaData();
      if (setType != null)
         collection.setType(setType);
      if (elementType != null)
         collection.setElementType(elementType);
      return (Set) collection;
   }
   
   @SuppressWarnings("unchecked")
   public List<ValueMetaData> createArray(String arrayType, String elementType)
   {
      AbstractArrayMetaData collection = new AbstractArrayMetaData();
      if (arrayType != null)
         collection.setType(arrayType);
      if (elementType != null)
         collection.setElementType(elementType);
      return (List) collection;
   }
   
   @SuppressWarnings("unchecked")
   public Map<ValueMetaData, ValueMetaData> createMap(String mapType, String keyType, String valueType)
   {
      AbstractMapMetaData map = new AbstractMapMetaData();
      if (mapType != null)
         map.setType(mapType);
      if (keyType != null)
         map.setKeyType(keyType);
      if (valueType != null)
         map.setValue(valueType);
      return (Map) map;
   }
}
