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
import org.jboss.beans.metadata.api.model.FromContext;
import org.jboss.beans.metadata.api.model.InjectOption;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.LifecycleMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.RelatedClassMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.ErrorHandlingMode;
import org.jboss.dependency.spi.graph.SearchInfo;
import org.jboss.kernel.api.dependency.MatcherFactory;

/**
 * The BeanMetaDataBuilder is a class that allows you to construct a {@link BeanMetaData}
 * programatically.<p>
 * Users should first call one of the <code>createBuilder()</code> methods, do some work on
 * the returned builder and then call <code>getBeanMetaData()</code>. e.g.:
 * <pre>
 * BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("MyBean", "org.acme.Foo");
 * builder.addAnnotation("@org.acme.Marker").addPropertyMetaData("simple", "Simple");
 * ValueMetaData inject = aspectBuilder.createInject("OtherBean");
 * builder.addPropertyMetaData("injected", inject);
 * BeanMetaData bmd = builder.getBeanMetaData();
 * </pre> 
 * will result in a similar BeanMetaData to deploying the following xml
 * <pre>
 * &lt;bean name="MyBean" class="org.acme.Foo"&gt;
 *   &lt;annotation&gt;@org.acme.Marker&lt;/annotation&gt;
 *   &lt;property name="simple"&gt;Simple&lt;/property&gt
 *   &lt;property name="injected"&gt;&lt;inject name="OtherBean"/&gt;&lt;/property&gt
 * &lt;/bean&gt;
 * </pre> 
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="mailto:adrian@jboss.org">Adrian Brock</a>
 */
public abstract class BeanMetaDataBuilder
{
   /**
    * Create builder from a bean's class name.
    *
    * @param beanClassName bean class name
    * @return a new BeanMetaDataBuilder
    */
   public static BeanMetaDataBuilder createBuilder(String beanClassName)
   {
      return BeanMetaDataBuilderFactory.createBuilder(beanClassName);
   }

   /**
    * Create builder from name and bean's classname.
    *
    * @param beanInstanceName bean name
    * @param beanClassName bean class name
    * @return a new BeanMetaDataBuilder
    */
   public static BeanMetaDataBuilder createBuilder(String beanInstanceName, String beanClassName)
   {
      return BeanMetaDataBuilderFactory.createBuilder(beanInstanceName, beanClassName);
   }
   
   /**
    * Create builder from an existing BeanMetaData
    * 
    * @param beanMetaData the bean metadata
    * @return a new BeanMetaDataBuilder
    */
   public static BeanMetaDataBuilder createBuilder(BeanMetaData beanMetaData)
   {
      if (beanMetaData instanceof AbstractBeanMetaData)
      {
         return BeanMetaDataBuilderFactory.createBuilder((AbstractBeanMetaData)beanMetaData);
      }
      else
      {
         throw new IllegalArgumentException("Invalid type of bean metadata: " + beanMetaData);
      }
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
    * Set the bean name
    *
    * @see BeanMetaData#getName()
    * @param name the name
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setName(String name);

   /**
    * Set the bean class.
    *
    * @see BeanMetaData#getBean()
    * @param bean the bean class name
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setBean(String bean);

   /**
    * Set the aliases. This overwrites any existing aliases.
    * 
    * @see BeanMetaData#getAliases()
    * @param aliases the aliases
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setAliases(Set<Object> aliases);

   /**
    * Add related class to the set of related classes.
    *
    * @see BeanMetaData#getRelated()
    * @param className the related class name
    * @param enabled the enabled
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addRelatedClass(String className, Object... enabled);

   /**
    * Add related class to the set of related classes.
    *
    * @see BeanMetaData#getRelated()
    * @param related the related class
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addRelatedClass(RelatedClassMetaData related);

   /**
    * Set the related classes. This overwrites any existing related classes.
    *
    * @see BeanMetaData#getRelated()
    * @param related the set of related classes
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setRelated(Set<RelatedClassMetaData> related);

   /**
    * Add alias to the set of aliases.
    *
    * @see BeanMetaData#getAliases()
    * @param alias the alias
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addAlias(Object alias);

   /**
    * Set the annotations. This overwrites any exisisting annotations.
    *
    * @see BeanMetaData#getAnnotations()
    * @param annotations the annotations
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setAnnotations(Set<String> annotations);

   /**
    * Add annotation to the set of annotations, with system property replacement.
    *
    * @see #addAnnotation(String, boolean)
    * @see BeanMetaData#getAnnotations()
    * @param annotation string representation of the annotation, e.g. <code>@org.acme.Marker</code>
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addAnnotation(String annotation);

   /**
    * Add annotation to the set of annotations, with system property replacement.
    *
    * @see #addAnnotation(String, boolean)
    * @see BeanMetaData#getAnnotations()
    * @param annotation the annotation instance
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addAnnotation(Annotation annotation);

   /**
    * Add annotation to the set of annotations.
    *
    * @param annotation string representation of the annotation, e.g. <code>@org.acme.Marker</code>
    * @param replace whether system property replacement should happen on the values. e.g. if we have
    * <code>@org.acme.WithProperty("${test.property}")</code> and the value of 
    * <code>-Dtest.property=hello</code>, this becomes <code>@org.acme.WithProperty("hello")</code>.
    * 
    * @see BeanMetaData#getAnnotations()
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addAnnotation(String annotation, boolean replace);

   /**
    * Set the controller mode to use for the 
    * {@link org.jboss.kernel.spi.dependency.KernelControllerContext} constructed
    * from the {@link BeanMetaData} created from this builder.
    * 
    * @see BeanMetaData#getMode()
    * @param modeString the name of the {@link org.jboss.dependency.spi.ControllerMode}
    * @return this builder
    */
   public BeanMetaDataBuilder setMode(String modeString)
   {
      return setMode(ControllerMode.getInstance(modeString));
   }

   /**
    * Set the controller mode to use for the 
    * {@link org.jboss.kernel.spi.dependency.KernelControllerContext} constructed
    * from the {@link BeanMetaData} created from this builder.
    * 
    * @see BeanMetaData#getMode()
    * @param mode the mode
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setMode(ControllerMode mode);

   /**
    * Set the bean access mode to use for the bean.
    *
    * @see BeanMetaData#getAccessMode()
    * @param mode the access mode
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setAccessMode(BeanAccessMode mode);

   /**
    * Set the error handling mode to use for the 
    * {@link org.jboss.kernel.spi.dependency.KernelControllerContext} constructed
    * from the {@link BeanMetaData} created from this builder.
    *
    * @see BeanMetaData#getErrorHandlingMode()
    * @param mode the error handling mode
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setErrorHandlingMode(ErrorHandlingMode mode);

   /**
    * Set the autowire type to be used for the  
    * {@link org.jboss.kernel.spi.dependency.KernelControllerContext} constructed
    * from the {@link BeanMetaData} created from this builder.
    *
    * @see BeanMetaData#getAutowireType()
    * @param type the autowire type
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setAutowireType(AutowireType type);

   /**
    * Set whether the bean is an autowire candidate
    *
    * @see BeanMetaData#isAutowireCandidate()
    * @param candidate the candidate flag
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setAutowireCandidate(boolean candidate);

   /**
    * Set that we don't want to use the deployment classloader
    * 
    * @see BeanMetaData#getClassLoader()
    * @return this builder
    */
   public BeanMetaDataBuilder setNoClassLoader()
   {
      return setClassLoader(createNull());
   }

   /**
    * Set the classloader to be used to load the class for this bean
    * 
    * @see BeanMetaData#getClassLoader()
    * @param classLoader the classloader
    * @return this builder
    */
   public BeanMetaDataBuilder setClassLoader(Object classLoader)
   {
      return setClassLoader(createValue(classLoader));
   }

   /**
    * Set the classloader to be used to load the class for this bean
    * 
    * @see BeanMetaData#getClassLoader()
    * @param classLoader a value metadata containing the classloader
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setClassLoader(ValueMetaData classLoader);

   /**
    * Set the classloader to be used to load the class for this bean
    * 
    * @see BeanMetaData#getClassLoader()
    * @param classLoader the classloader
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setClassLoader(ClassLoaderMetaData classLoader);

   /**
    * Set the non-static factory bean to use constructing the bean
    * 
    * @see BeanMetaData#getConstructor()
    * @see ConstructorMetaData#getFactory()
    * @param factory the factory
    * @return this builder
    */
   public BeanMetaDataBuilder setFactory(Object factory)
   {
      return setFactory(createValue(factory));
   }

   /**
    * Set the non-static factory bean to use constructing the bean
    * 
    * @see BeanMetaData#getConstructor()
    * @see ConstructorMetaData#getFactory()
    * @param bean the bean name
    * @return this builder
    */
   public BeanMetaDataBuilder setFactory(String bean)
   {
      return setFactory(bean, null);
   }

   /**
    * Set the non-static factory bean to use constructing the bean
    * 
    * @see BeanMetaData#getConstructor()
    * @see ConstructorMetaData#getFactory()
    * @param bean the bean name
    * @param property the property name for the factory
    * @return this builder
    */
   public BeanMetaDataBuilder setFactory(String bean, String property)
   {
      return setFactory(createInject(bean, property));
   }

   /**
    * Set the non-static factory bean to use constructing the bean
    * 
    * @see BeanMetaData#getConstructor()
    * @see ConstructorMetaData#getFactory()
    * @param factory the factory
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setFactory(ValueMetaData factory);

   /**
    * Set the non-static factory class to use constructing the bean
    * 
    * @see BeanMetaData#getConstructor()
    * @see ConstructorMetaData#getFactoryClass()
    * @param factoryClass the factory class name
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setFactoryClass(String factoryClass);

   /**
    * Set the factory method to use constructing the bean. 
    * 
    * @see BeanMetaData#getConstructor()
    * @see ConstructorMetaData#getFactoryMethod()
    * @param factoryMethod the name of the factory method
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setFactoryMethod(String factoryMethod);

   /**
    * Set the constructor value to use constructing the bean
    * 
    * @see BeanMetaData#getConstructor()
    * @see ConstructorMetaData#getValue()
    * @param value the object "constructed"
    * @return this builder
    */
   public BeanMetaDataBuilder setConstructorValue(Object value)
   {
      return setConstructorValue(createValue(value));
   }

   /**
    * Set the constructor value to use constructing the bean
    * 
    * @see BeanMetaData#getConstructor()
    * @see ConstructorMetaData#getValue()
    * @param type the type of the object
    * @param value the object "constructed"
    * @return this builder
    */
   public BeanMetaDataBuilder setConstructorValue(String type, String value)
   {
      return setConstructorValue(createString(type, value));
   }

   /**
    * Set the constructor value to use constructing the bean
    * 
    * @see BeanMetaData#getConstructor()
    * @see ConstructorMetaData#getValue()
    * @param value the object "constructed"
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setConstructorValue(ValueMetaData value);

   /**
    * Add a constructor parameter to use when calling the constructor or a factory method
    * 
    * @see BeanMetaData#getConstructor()
    * @see ConstructorMetaData#getParameters()
    * @param type the parameter type
    * @param value the value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addConstructorParameter(String type, Object value);

   /**
    * Add a constructor parameter to use when calling the constructor or a factory method
    * 
    * @see BeanMetaData#getConstructor()
    * @see ConstructorMetaData#getParameters()
    * @param type the parameter type
    * @param value String representation of the value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addConstructorParameter(String type, String value);

   /**
    * Add a constructor parameter to use when calling the constructor or a factory method
    * 
    * @see BeanMetaData#getConstructor()
    * @see ConstructorMetaData#getParameters()
    * @param type the parameter type
    * @param value the value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addConstructorParameter(String type, ValueMetaData value);

   /**
    * Add a property to the beans set of properties, replace it if it already exists
    * 
    * @see BeanMetaData#getProperties()
    * @param name the property name
    * @param value the value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addPropertyMetaData(String name, Object value);

   /**
    * Add a property to the beans set of properties, replace it if it already exists
    * 
    * @see BeanMetaData#getProperties()
    * @param name the property name
    * @param value the value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addPropertyMetaData(String name, String value);

   /**
    * Add a property to the beans set of properties, replace it if it already exists
    * 
    * @see BeanMetaData#getProperties()
    * @param name the property name
    * @param value the value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addPropertyMetaData(String name, ValueMetaData value);

   /**
    * Add a property to the beans set of properties, replace it if it already exists
    * 
    * @see BeanMetaData#getProperties()
    * @param name the property name
    * @param value the value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addPropertyMetaData(String name, Collection<ValueMetaData> value);

   /**
    * Add an annotation to a property, with system property replacement
    *
    * @see #addPropertyAnnotation(String, String, boolean)
    * @see BeanMetaData#getProperties()
    * @see PropertyMetaData#getAnnotations()
    * @param name the property name
    * @param annotation the annotation in string format, e.g. <code>@org.acme.Marker</code>
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addPropertyAnnotation(String name, String annotation);

   /**
    * Add an annotation to a property, with system property replacement
    *
    * @see #addPropertyAnnotation(String, String, boolean)
    * @see BeanMetaData#getProperties()
    * @see PropertyMetaData#getAnnotations()
    * @param name the property name
    * @param annotation string representation of the annotation, e.g. <code>@org.acme.Marker</code>
    * @param replace whether system property replacement should happen on the values. e.g. if we have
    * <code>@org.acme.WithProperty("${test.property}")</code> and the value of 
    * <code>-Dtest.property=hello</code>, this becomes <code>@org.acme.WithProperty("hello")</code>.
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addPropertyAnnotation(String name, String annotation, boolean replace);

   /**
    * Add a property annotation.
    *
    * @see #addPropertyAnnotation(String, String, boolean)
    * @see BeanMetaData#getProperties()
    * @see PropertyMetaData#getAnnotations()
    * @param name the property name
    * @param annotation the annotation
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addPropertyAnnotation(String name, Annotation annotation);

   /**
    * Add a property to the beans set of properties, replace it if it already exists
    * 
    * @see BeanMetaData#getProperties()
    * @param name the property name
    * @param value the value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addPropertyMetaData(String name, Map<ValueMetaData, ValueMetaData> value);
   
   /**
    * Should we ignore default create invocation.
    *
    * @see BeanMetaData#getCreate()
    * @see LifecycleMetaData#isIgnored()
    * @return this builder
    */
   public abstract BeanMetaDataBuilder ignoreCreate();

   /**
    * Set the create method
    * 
    * @see BeanMetaData#getCreate()
    * @see LifecycleMetaData#getMethodName()
    * @param methodName the method name
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setCreate(String methodName);

   /**
    * Add a parameter to be passed in to the create method
    * 
    * @see BeanMetaData#getCreate()
    * @see LifecycleMetaData#getParameters()
    * @param type the parameter type
    * @param value the value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addCreateParameter(String type, Object value);

   /**
    * Add a parameter to be passed in to the create method
    * 
    * @see BeanMetaData#getCreate()
    * @see LifecycleMetaData#getParameters()
    * @param type the parameter type
    * @param value String representation of the parameter value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addCreateParameter(String type, String value);

   /**
    * Add a parameter to be passed in to the create method
    * 
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addCreateParameter(String type, ValueMetaData value);

   /**
    * Should we ignore default start invocation.
    *
    * @see BeanMetaData#getStart()
    * @see LifecycleMetaData#isIgnored()
    * @return this builder
    */
   public abstract BeanMetaDataBuilder ignoreStart();

   /**
    * Set the start method
    * 
    * @see BeanMetaData#getStart()
    * @see LifecycleMetaData#getMethodName()
    * @param methodName the method name
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setStart(String methodName);

   /**
    * Add a parameter to be passed in to the start method
    * 
    * @see BeanMetaData#getStart()
    * @see LifecycleMetaData#getParameters()
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addStartParameter(String type, Object value);

   /**
    * Add a parameter to be passed in to the start method
    * 
    * @see BeanMetaData#getStart()
    * @see LifecycleMetaData#getParameters()
    * @param type String representation of the parameter type
    * @param value the value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addStartParameter(String type, String value);

   /**
    * Add a parameter to be passed in to the start method
    * 
    * @see BeanMetaData#getStart()
    * @see LifecycleMetaData#getParameters()
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addStartParameter(String type, ValueMetaData value);

   /**
    * Should we ignore default stop invocation.
    *
    * @see BeanMetaData#getStop()
    * @see LifecycleMetaData#isIgnored()
    * @return this builder
    */
   public abstract BeanMetaDataBuilder ignoreStop();

   /**
    * Set the stop method
    * 
    * @see BeanMetaData#getStop()
    * @see LifecycleMetaData#getMethodName()
    * @param methodName the method name
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setStop(String methodName);

   /**
    * Add a parameter to be passed in to the stop method
    * 
    * @see BeanMetaData#getStop()
    * @see LifecycleMetaData#getParameters()
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addStopParameter(String type, Object value);

   /**
    * Add a parameter to be passed in to the stop method
    * 
    * @see BeanMetaData#getStop()
    * @see LifecycleMetaData#getParameters()
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addStopParameter(String type, String value);

   /**
    * Add a parameter to be passed in to the stop method
    * 
    * @see BeanMetaData#getStop()
    * @see LifecycleMetaData#getParameters()
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addStopParameter(String type, ValueMetaData value);

   /**
    * Should we ignore default destroy invocation.
    *
    * @See {@link BeanMetaData#getDestroy()}
    * @see LifecycleMetaData#isIgnored()
    * @return this builder
    */
   public abstract BeanMetaDataBuilder ignoreDestroy();

   /**
    * Set the destroy method
    * 
    * @see BeanMetaData#getDestroy()
    * @see LifecycleMetaData#getMethodName()
    * @param methodName the method name
    * @return this builder
    */
   public abstract BeanMetaDataBuilder setDestroy(String methodName);

   /**
    * Add a parameter to be passed in to the destroy method
    * 
    * @see BeanMetaData#getDestroy()
    * @see LifecycleMetaData#getParameters()
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addDestroyParameter(String type, Object value);

   /**
    * Add a parameter to be passed in to the destroy method
    * 
    * @see BeanMetaData#getDestroy()
    * @see LifecycleMetaData#getParameters()
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addDestroyParameter(String type, String value);

   /**
    * Add a parameter to be passed in to the destroy method
    * 
    * @see BeanMetaData#getDestroy()
    * @see LifecycleMetaData#getParameters()
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addDestroyParameter(String type, ValueMetaData value);

   /**
    * Add a supply
    * 
    * @param supply the supply
    * @return this builder
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
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addSupply(Object supply, String type);

   /**
    * Add a demand using the default value for when the supply is required by the demanding bean 
    * {@link ControllerState#DESCRIBED} and the default value for the state of the supply 
    * (link {@link ControllerState#INSTALLED} 
    * 
    * @see BeanMetaData#getDemands()
    * @param demand the demand
    * @return this builder
    */
   public BeanMetaDataBuilder addDemand(Object demand)
   {
      return addDemand(demand, (ControllerState) null, null);
   }

   /**
    * Add a demand using the default value for the state of the supply 
    * (link {@link ControllerState#INSTALLED}.
    * 
    * @see BeanMetaData#getDemands()
    * @see MatcherFactory
    * @param demand the demand
    * @param whenRequired string represenation of the when the demand is required
    * @param transformer the transformer name as described in {@link MatcherFactory}
    * @return this builder
    */
   public BeanMetaDataBuilder addDemand(Object demand, String whenRequired, String transformer)
   {
      ControllerState whenRequiredState = null;
      if (whenRequired != null)
         whenRequiredState = new ControllerState(whenRequired);
      return addDemand(demand, whenRequiredState, transformer);
   }

   /**
    * Add a demand using the default value for the state of the supply 
    * (link {@link ControllerState#INSTALLED} and the 
    * 
    * @see BeanMetaData#getDemands()
    * @see MatcherFactory
    * @param demand the demand
    * @param whenRequired when the demand is required
    * @param transformer the transformer name as described in {@link MatcherFactory}
    * @return this builder
    */
   public BeanMetaDataBuilder addDemand(Object demand, ControllerState whenRequired, String transformer)
   {
      return addDemand(demand, whenRequired, null, transformer);
   }

   /**
    * Add a demand 
    * 
    * @see BeanMetaData#getDemands()
    * @see MatcherFactory
    * @param demand the demand
    * @param whenRequired when the demand is required
    * @param targetState the target state of the supply
    * @param transformer the transformer name as described in {@link MatcherFactory}
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addDemand(Object demand, ControllerState whenRequired, ControllerState targetState, String transformer);

   /**
    * Add a dependency
    *
    * @see BeanMetaData#getDepends()
    * @param dependency the name of the dependency, i.e. bean name
    * @return this builder
    */
   public abstract BeanMetaDataBuilder addDependency(Object dependency);

   /**
    * Add an install where the install method resides in the bean being built here
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName)
   {
      return addInstall(methodName, null);
   }

   /**
    * Add an install where the install method resides in the bean being build here,
    * taking a single parameter.
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String type, Object value)
   {
      return addInstall(methodName, null, type, value);
   }

   /**
    * Add an install where the install method resides in the bean being build here,
    * taking a single parameter.
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param type the parameter type
    * @param value String representation of the parameter value
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String type, String value)
   {
      return addInstall(methodName, null, type, value);
   }

   /**
    * Add an install where the install method resides in the bean being build here,
    * taking a single parameter.
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String type, ValueMetaData value)
   {
      return addInstall(methodName, null, type, value);
   }

   /**
    * Add an install where the install method resides in the bean being build here,
    * taking several parameters. The <code>types</code> and <code>values</code>
    * arrays must have the same lengths, and <code>types[i]</code> is the type of
    * <code>values[i]</code>.
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param methodName the method name
    * @param types the parameter types
    * @param values the parameter values
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String[] types, Object[] values)
   {
      return addInstall(methodName, null, types, values);
   }

   /**
    * Add an install where the install method resides in the bean being build here,
    * taking several parameters. The <code>types</code> and <code>values</code>
    * arrays must have the same lengths, and <code>types[i]</code> is the type of
    * <code>values[i]</code>.
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param types the parameter types
    * @param values the string representations of the parameter values
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String[] types, String[] values)
   {
      return addInstall(methodName, null, types, values);
   }

   /**
    * Add an install where the install method resides in the bean being build here,
    * taking several parameters. The <code>types</code> and <code>values</code>
    * arrays must have the same lengths, and <code>types[i]</code> is the type of
    * <code>values[i]</code>.
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param types the parameter types
    * @param values the parameter values
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String[] types, ValueMetaData[] values)
   {
      return addInstall(methodName, null, types, values);
   }

   /**
    * Add an install, where the install method resides in another bean, passing in a single
    * parameter which is the 'this' pointer to the bean being created here.
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param bean the name of the bean containing the method to be called upon install
    * @return this builder
    */
   public BeanMetaDataBuilder addInstallWithThis(String methodName, String bean)
   {
      return addInstallWithThis(methodName, bean, null);
   }

   /**
    * Add an install, where the install method resides in another bean, passing in a single
    * parameter which is the 'this' pointer to the bean being created here
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param bean the name of the bean containing the method to be called upon install
    * @param state the required state of the bean containing the install method
    * @return this builder
    */
   public BeanMetaDataBuilder addInstallWithThis(String methodName, String bean, ControllerState state)
   {
      return addInstallWithThis(methodName, bean, state, null);
   }

   /**
    * Add an install, where the install method resides in another bean, passing in a single
    * parameter which is the 'this' pointer to the bean being created here
    *
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param bean the name of the bean containing the method to be called upon install
    * @param state the state of the bean
    * @param whenRequired the state when to call install
    * @return this builder
    */
   public BeanMetaDataBuilder addInstallWithThis(String methodName, String bean, ControllerState state, ControllerState whenRequired)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean, state, whenRequired);
      parameters.addParameterMetaData(null, createThis());
      return this;
   }

   /**
    * Add an install where the install method resides in another bean
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param bean the name of the bean containing the method to be called upon install
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean)
   {
      addInstallWithParameters(methodName, bean);
      return this;
   }

   /**
    * Add an install where the install method resides in another bean,
    * taking a single parameter
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param bean the name of the bean containing the method to be called upon install
    * @param type the parameter type
    * @param value the value
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean, String type, Object value)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean);
      parameters.addParameterMetaData(type, value);
      return this;
   }

   /**
    * Add an install where the install method resides in another bean,
    * taking a single parameter
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param bean the name of the bean containing the method to be called upon install
    * @param type the parameter type
    * @param value the string representation of the parameter value
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean, String type, String value)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean);
      parameters.addParameterMetaData(type, value);
      return this;
   }

   /**
    * Add an install where the install method resides in another bean,
    * taking a single parameter
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param bean the name of the bean containing the method to be called upon install
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean, String type, ValueMetaData value)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean);
      parameters.addParameterMetaData(type, value);
      return this;
   }

   /**
    * Add an install where the install method resides in another bean,
    * taking several parameters. The <code>types</code> and <code>values</code>
    * arrays must have the same lengths, and <code>types[i]</code> is the type of
    * <code>values[i]</code>.
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param bean the name of the bean containing the method to be called upon install
    * @param types the parameter types
    * @param values the parameter values
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean, String[] types, Object[] values)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean);
      for (int i = 0; i < types.length; i++)
         parameters.addParameterMetaData(types[i], values[i]);
      return this;
   }

   /**
    * Add an install where the install method resides in in another bean,
    * taking several parameters. The <code>types</code> and <code>values</code>
    * arrays must have the same lengths, and <code>types[i]</code> is the type of
    * <code>values[i]</code>.
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param bean the name of the bean containing the method to be called upon install
    * @param types the parameter types
    * @param values the string representations of the parameter values
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean, String[] types, String[] values)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean);
      for (int i = 0; i < types.length; i++)
         parameters.addParameterMetaData(types[i], values[i]);
      return this;
   }

   /**
    * Add an install where the install method resides in in another bean,
    * taking several parameters. The <code>types</code> and <code>values</code>
    * arrays must have the same lengths, and <code>types[i]</code> is the type of
    * <code>values[i]</code>.
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param bean the name of the bean containing the method to be called upon install
    * @param types the parameter types
    * @param values the parameter values
    * @return this builder
    */
   public BeanMetaDataBuilder addInstall(String methodName, String bean, String[] types, ValueMetaData[] values)
   {
      ParameterMetaDataBuilder parameters = addInstallWithParameters(methodName, bean);
      for (int i = 0; i < types.length; i++)
         parameters.addParameterMetaData(types[i], values[i]);
      return this;
   }

   /**
    * Add an install where the install method resides in the bean being built here,
    * returning a parameter builder that can be used to add parameters.
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @return the parameter builder
    */
   public ParameterMetaDataBuilder addInstallWithParameters(String methodName)
   {
      return addInstallWithParameters(methodName, null);
   }

   /**
    * Add an install where the install method resides in another bean,
    * returning a parameter builder that can be used to add parameters.
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param bean the name of the bean containing the method to be called upon install
    * @return the parameter builder
    */
   public ParameterMetaDataBuilder addInstallWithParameters(String methodName, String bean)
   {
      return addInstallWithParameters(methodName, bean, null);
   }

   /**
    * Add an install where the install method resides in another bean,
    * returning a parameter builder that can be used to add parameters.
    * 
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param bean the name of the bean containing the method to be called upon install
    * @param state the state of the bean
    * @return the parameter builder
    */
   public ParameterMetaDataBuilder addInstallWithParameters(String methodName, String bean, ControllerState state)
   {
      return addInstallWithParameters(methodName, bean, state, null);
   }

   /**
    * Add an install where the install method resides in another bean,
    * returning a parameter builder that can be used to add parameters.
    *
    * @see BeanMetaData#getInstalls()
    * @param methodName the name of the method to be called upon install
    * @param bean the name of the bean containing the method to be called upon install
    * @param state the state of the bean
    * @param whenRequired the state when to call install
    * @return the parameter builder
    */
   public abstract ParameterMetaDataBuilder addInstallWithParameters(String methodName, String bean, ControllerState state, ControllerState whenRequired);

   /**
    * Add an uninstall where the uninstall method resides in the bean being built here
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName)
   {
      return addUninstall(methodName, null);
   }

   /**
    * Add an uninstall where the uninstall method resides in the bean being build here,
    * taking a single parameter.
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String type, Object value)
   {
      return addUninstall(methodName, null, type, value);
   }

   /**
    * Add an uninstall where the uninstall method resides in the bean being build here,
    * taking a single parameter.
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param type the parameter type
    * @param value String representation of the parameter value
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String type, String value)
   {
      return addUninstall(methodName, null, type, value);
   }

   /**
    * Add an uninstall where the uninstall method resides in the bean being build here,
    * taking a single parameter.
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String type, ValueMetaData value)
   {
      return addUninstall(methodName, null, type, value);
   }

   /**
    * Add an uninstall where the uninstall method resides in the bean being build here,
    * taking several parameters. The <code>types</code> and <code>values</code>
    * arrays must have the same lengths, and <code>types[i]</code> is the type of
    * <code>values[i]</code>.
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param methodName the method name
    * @param types the parameter types
    * @param values the parameter values
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String[] types, Object[] values)
   {
      return addUninstall(methodName, null, types, values);
   }

   /**
    * Add an uninstall where the uninstall method resides in the bean being build here,
    * taking several parameters. The <code>types</code> and <code>values</code>
    * arrays must have the same lengths, and <code>types[i]</code> is the type of
    * <code>values[i]</code>.
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param types the parameter types
    * @param values the string representations of the parameter values
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String[] types, String[] values)
   {
      return addUninstall(methodName, null, types, values);
   }

   /**
    * Add an uninstall where the uninstall method resides in the bean being build here,
    * taking several parameters. The <code>types</code> and <code>values</code>
    * arrays must have the same lengths, and <code>types[i]</code> is the type of
    * <code>values[i]</code>.
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param types the parameter types
    * @param values the parameter values
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String[] types, ValueMetaData[] values)
   {
      return addUninstall(methodName, null, types, values);
   }

   /**
    * Add an uninstall, where the uninstall method resides in another bean, passing in a single
    * parameter which is the 'this' pointer to the bean being created here.
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param bean the name of the bean containing the method to be called upon uninstall
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstallWithThis(String methodName, String bean)
   {
      return addUninstallWithThis(methodName, bean, null);
   }

   /**
    * Add an uninstall, where the uninstall method resides in another bean, passing in a single
    * parameter which is the 'this' pointer to the bean being created here
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param bean the name of the bean containing the method to be called upon uninstall
    * @param state the required state of the bean containing the uninstall method
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstallWithThis(String methodName, String bean, ControllerState state)
   {
      return addUninstallWithThis(methodName, bean, state, null);
   }

   /**
    * Add an uninstall, where the uninstall method resides in another bean, passing in a single
    * parameter which is the 'this' pointer to the bean being created here
    *
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param bean the name of the bean containing the method to be called upon uninstall
    * @param state the state of the bean
    * @param whenRequired the state when to call uninstall
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstallWithThis(String methodName, String bean, ControllerState state, ControllerState whenRequired)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean, state, whenRequired);
      parameters.addParameterMetaData(null, createThis());
      return this;
   }

   /**
    * Add an uninstall where the uninstall method resides in another bean
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param bean the name of the bean containing the method to be called upon uninstall
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean)
   {
      addUninstallWithParameters(methodName, bean);
      return this;
   }

   /**
    * Add an uninstall where the uninstall method resides in another bean,
    * taking a single parameter
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param bean the name of the bean containing the method to be called upon uninstall
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean, String type, Object value)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean);
      parameters.addParameterMetaData(type, value);
      return this;
   }

   /**
    * Add an uninstall where the uninstall method resides in another bean,
    * taking a single parameter
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param bean the name of the bean containing the method to be called upon uninstall
    * @param type the parameter type
    * @param value the string representation of the parameter value
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean, String type, String value)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean);
      parameters.addParameterMetaData(type, value);
      return this;
   }

   /**
    * Add an uninstall where the uninstall method resides in another bean,
    * taking a single parameter
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param bean the name of the bean containing the method to be called upon uninstall
    * @param type the parameter type
    * @param value the parameter value
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean, String type, ValueMetaData value)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean);
      parameters.addParameterMetaData(type, value);
      return this;
   }

   /**
    * Add an uninstall where the uninstall method resides in another bean,
    * taking several parameters. The <code>types</code> and <code>values</code>
    * arrays must have the same lengths, and <code>types[i]</code> is the type of
    * <code>values[i]</code>.
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param bean the name of the bean containing the method to be called upon uninstall
    * @param types the parameter types
    * @param values the parameter values
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean, String[] types, Object[] values)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean);
      for (int i = 0; i < types.length; ++i)
         parameters.addParameterMetaData(types[i], values[i]);
      return this;
   }

   /**
    * Add an uninstall where the uninstall method resides in in another bean,
    * taking several parameters. The <code>types</code> and <code>values</code>
    * arrays must have the same lengths, and <code>types[i]</code> is the type of
    * <code>values[i]</code>.
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param bean the name of the bean containing the method to be called upon uninstall
    * @param types the parameter types
    * @param values the string representations of the parameter values
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean, String[] types, String[] values)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean);
      for (int i = 0; i < types.length; ++i)
         parameters.addParameterMetaData(types[i], values[i]);
      return this;
   }

   /**
    * Add an uninstall where the uninstall method resides in in another bean,
    * taking several parameters. The <code>types</code> and <code>values</code>
    * arrays must have the same lengths, and <code>types[i]</code> is the type of
    * <code>values[i]</code>.
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param bean the name of the bean containing the method to be called upon uninstall
    * @param types the parameter types
    * @param values the parameter values
    * @return this builder
    */
   public BeanMetaDataBuilder addUninstall(String methodName, String bean, String[] types, ValueMetaData[] values)
   {
      ParameterMetaDataBuilder parameters = addUninstallWithParameters(methodName, bean);
      for (int i = 0; i < types.length; ++i)
         parameters.addParameterMetaData(types[i], values[i]);
      return this;
   }

   /**
    * Add an uninstall where the uninstall method resides in the bean being built here,
    * returning a parameter builder that can be used to add parameters.
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @return the parameter builder
    */
   public ParameterMetaDataBuilder addUninstallWithParameters(String methodName)
   {
      return addUninstallWithParameters(methodName, null);
   }

   /**
    * Add an uninstall where the uninstall method resides in another bean,
    * returning a parameter builder that can be used to add parameters.
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param bean the name of the bean containing the method to be called upon uninstall
    * @return the parameter builder
    */
   public ParameterMetaDataBuilder addUninstallWithParameters(String methodName, String bean)
   {
      return addUninstallWithParameters(methodName, bean, null);
   }

   /**
    * Add an uninstall where the uninstall method resides in another bean,
    * returning a parameter builder that can be used to add parameters.
    * 
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param bean the name of the bean containing the method to be called upon uninstall
    * @param state the state of the bean
    * @return the parameter builder
    */
   public ParameterMetaDataBuilder addUninstallWithParameters(String methodName, String bean, ControllerState state)
   {
      return addUninstallWithParameters(methodName, bean, state, null);
   }
   
   /**
    * Add an uninstall where the uninstall method resides in another bean,
    * returning a parameter builder that can be used to add parameters.
    *
    * @see BeanMetaData#getUninstalls()
    * @param methodName the name of the method to be called upon uninstall
    * @param bean the name of the bean containing the method to be called upon uninstall
    * @param state the state of the bean
    * @param whenRequired the state when to call uninstall
    * @return the parameter builder
    */
   public abstract ParameterMetaDataBuilder addUninstallWithParameters(String methodName, String bean, ControllerState state, ControllerState whenRequired);

   /**
    * Add property install callback.
    *
    * @see BeanMetaData#getInstallCallbacks()
    * @param property the property name
    * @return this builder
    */
   public BeanMetaDataBuilder addPropertyInstallCallback(String property)
   {
      return addPropertyInstallCallback(property, null, null);
   }

   /**
    * Add property install callback.
    *
    * @see BeanMetaData#getInstallCallbacks()
    * @param property the property name
    * @param whenRequired the when required state
    * @return this builder
    */
   public BeanMetaDataBuilder addPropertyInstallCallback(String property, ControllerState whenRequired)
   {
      return addPropertyInstallCallback(property, whenRequired, null);
   }
         
   /**
    * Add property install callback.
    *
    * @see BeanMetaData#getInstallCallbacks()
    * @param property the property name
    * @param cardinality the cardinality
    * @return this builder
    */
   public BeanMetaDataBuilder addPropertyInstallCallback(String property, Cardinality cardinality)
   {
      return addPropertyInstallCallback(property, null, cardinality);
   }

   /**
    * Add property install callback.
    *
    * @see BeanMetaData#getInstallCallbacks()
    * @param property the property name
    * @param whenRequired the when required state
    * @param cardinality the cardinality
    * @return this builder
    */
   public BeanMetaDataBuilder addPropertyInstallCallback(String property, ControllerState whenRequired, Cardinality cardinality)
   {
      return addPropertyInstallCallback(property, null, whenRequired, null, cardinality);
   }

   /**
    * Add property install callback.
    *
    * @see BeanMetaData#getInstallCallbacks()
    * @param property the property name
    * @param signature the property signature
    * @param whenRequired the when required state
    * @param dependentState the dependant state
    * @param cardinality the cardinality
    * @return this builder
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
    * @see BeanMetaData#getUninstallCallbacks()
    * @param property the property name
    * @return this builder
    */
   public BeanMetaDataBuilder addPropertyUninstallCallback(String property)
   {
      return addPropertyUninstallCallback(property, null, null);
   }

   /**
    * Add property uninstall callback.
    *
    * @see BeanMetaData#getUninstallCallbacks()
    * @param property the property name
    * @param whenRequired the when required state
    * @return this builder
    */
   public BeanMetaDataBuilder addPropertyUninstallCallback(String property, ControllerState whenRequired)
   {
      return addPropertyUninstallCallback(property, whenRequired, null);
   }

   /**
    * Add property uninstall callback.
    *
    * @see BeanMetaData#getUninstallCallbacks()
    * @param property the property name
    * @param cardinality the cardinality
    * @return this builder
    */
   public BeanMetaDataBuilder addPropertyUninstallCallback(String property, Cardinality cardinality)
   {
      return addPropertyUninstallCallback(property, null, cardinality);
   }

   /**
    * Add property uninstall callback.
    *
    * @see BeanMetaData#getUninstallCallbacks()
    * @param property the property name
    * @param whenRequired the when required state
    * @param cardinality the cardinality
    * @return this builder
    */
   public BeanMetaDataBuilder addPropertyUninstallCallback(String property, ControllerState whenRequired, Cardinality cardinality)
   {
      return addPropertyUninstallCallback(property, null, whenRequired, null, cardinality);
   }

   /**
    * Add property uninstall callback.
    *
    * @see BeanMetaData#getUninstallCallbacks()
    * @param property the property name
    * @param signature the property signature
    * @param whenRequired the when required state
    * @param dependentState the dependant state
    * @param cardinality the cardinality
    * @return this builder
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
    * @see BeanMetaData#getInstallCallbacks()
    * @param method the method name
    * @return this builder
    */
   public BeanMetaDataBuilder addMethodInstallCallback(String method)
   {
      return addMethodInstallCallback(method, null, null);
   }

   /**
    * Add method install callback.
    *
    * @see BeanMetaData#getInstallCallbacks()
    * @param method the method name
    * @param whenRequired the when required state
    * @return this builder
    */
   public BeanMetaDataBuilder addMethodInstallCallback(String method, ControllerState whenRequired)
   {
      return addMethodInstallCallback(method, whenRequired, null);
   }

   /**
    * Add method install callback.
    *
    * @see BeanMetaData#getInstallCallbacks()
    * @param method the method name
    * @param cardinality the cardinality
    * @return this builder
    */
   public BeanMetaDataBuilder addMethodInstallCallback(String method, Cardinality cardinality)
   {
      return addMethodInstallCallback(method, null, cardinality);
   }

   /**
    * Add method install callback.
    *
    * @see BeanMetaData#getInstallCallbacks()
    * @param method the method name
    * @param whenRequired the when required state
    * @param cardinality the cardinality
    * @return this builder
    */
   public BeanMetaDataBuilder addMethodInstallCallback(String method, ControllerState whenRequired, Cardinality cardinality)
   {
      return addMethodInstallCallback(method, null, whenRequired, null, cardinality);
   }

   /**
    * Add method install callback.
    *
    * @see BeanMetaData#getInstallCallbacks()
    * @param method the method name
    * @param signature the method signature
    * @param whenRequired the when required state
    * @param dependentState the dependant state
    * @param cardinality the cardinality
    * @return this builder
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
    * @see BeanMetaData#getUninstallCallbacks()
    * @param method the method name
    * @return this builder
    */
   public BeanMetaDataBuilder addMethodUninstallCallback(String method)
   {
      return addMethodUninstallCallback(method, null, null);
   }

   /**
    * Add method uninstall callback.
    *
    * @see BeanMetaData#getUninstallCallbacks()
    * @param method the method name
    * @param whenRequired the when required state
    * @return this builder
    */
   public BeanMetaDataBuilder addMethodUninstallCallback(String method, ControllerState whenRequired)
   {
      return addMethodUninstallCallback(method, whenRequired, null);
   }

   /**
    * Add method uninstall callback.
    *
    * @see BeanMetaData#getUninstallCallbacks()
    * @param method the method name
    * @param cardinality the cardinality
    * @return this builder
    */
   public BeanMetaDataBuilder addMethodUninstallCallback(String method, Cardinality cardinality)
   {
      return addMethodUninstallCallback(method, null, cardinality);
   }

   /**
    * Add method uninstall callback.
    *
    * @see BeanMetaData#getUninstallCallbacks()
    * @param method the method name
    * @param whenRequired the when required state
    * @param cardinality the cardinality
    * @return this builder
    */
   public BeanMetaDataBuilder addMethodUninstallCallback(String method, ControllerState whenRequired, Cardinality cardinality)
   {
      return addMethodUninstallCallback(method, null, whenRequired, null, cardinality);
   }

   /**
    * Add method uninstall callback.
    *
    * @see BeanMetaData#getUninstallCallbacks()
    * @param method the method name
    * @param signature the method signature
    * @param whenRequired the when required state
    * @param dependentState the dependant state
    * @param cardinality the cardinality
    * @return this builder
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
    * @see BeanMetaData#getRelated()
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
    * Create a value containing an object
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
    * Create an injection using a bean as what is being injected
    * 
    * @param bean the name of bean to inject
    * @return the injection
    */
   public ValueMetaData createInject(Object bean)
   {
      return createInject(bean, null, null, null);
   }
   
   /**
    * Create an injection being able to specify the the property of a bean as what is being injected
    * 
    * @param bean the name of the bean 
    * @param property the name of the property of the bean to inject. If null, the bean itself will be injected
    * @return the injection
    */
   public ValueMetaData createInject(Object bean, String property)
   {
      return createInject(bean, property, null, null);
   }
   
   /**
    * Create an injection being able to specify the property of a bean as what is being injected
    * 
    * @param bean the name of the bean 
    * @param property the name of the property of the bean to inject. If null, the bean itself will be injected
    * @param whenRequired when the injection is required
    * @param dependentState the required state of the injected bean
    * @return the injection
    */
   public ValueMetaData createInject(Object bean, String property, ControllerState whenRequired, ControllerState dependentState)
   {
      return createInject(bean, property, whenRequired, dependentState, null);
   }

   /**
    * Create an injection being able to specify the property of a bean as what is being injected
    * 
    * @param bean the name of the bean 
    * @param property the name of the property of the bean to inject. If null, the bean itself will be injected
    * @param whenRequired when the injection is required
    * @param dependentState the required state of the injected bean
    * @param search the search info describing how to search for the injected bean if we have a hierarchy of 
    * {@link org.jboss.dependency.spi.Controller}s 
    * @return the injection
    */
   public abstract ValueMetaData createInject(Object bean, String property, ControllerState whenRequired, ControllerState dependentState, SearchInfo search);

   /**
    * Create contextual injection. This does not need to specify the name of the bean, 
    * but looks at the target property/parameter type for autowiring
    *
    * @see BeanMetaData#isAutowireCandidate()
    * @return the contextual injection
    */
   public ValueMetaData createContextualInject()
   {
      return createContextualInject(null, null);
   }

   /**
    * Create contextual injection. This does not need to specify the name of the bean, 
    * but looks at the target property/parameter type for autowiring
    *
    * @see BeanMetaData#isAutowireCandidate()
    * @param whenRequired when the injection is required
    * @param dependentState the state of the injected bean
    * @return the contextual injection
    */
   public ValueMetaData createContextualInject(ControllerState whenRequired, ControllerState dependentState)
   {
      return createContextualInject(whenRequired, dependentState, null, null);
   }

   /**
    * Create contextual injection. This does not need to specify the name of the bean, 
    * but looks at the target property/parameter type for autowiring
    *
    * @see BeanMetaData#isAutowireCandidate()
    * @see BeanMetaData#getAutowireType()
    * @param whenRequired when the injection is required
    * @param dependentState the state of the injected bean
    * @param autowire the autowire type. If null, the type is {@link AutowireType#BY_CLASS} 
    * @param option the inject option
    * @return the contextual injection
    */
   public ValueMetaData createContextualInject(ControllerState whenRequired, ControllerState dependentState, AutowireType autowire, InjectOption option)
   {
      return createContextualInject(whenRequired, dependentState, autowire, option, null);
   }

   /**
    * Create contextual injection. This does not need to specify the name of the bean, 
    * but looks at the target property/parameter type for autowiring
    *
    * @param whenRequired when the injection is required
    * @param dependentState the state of the injected bean
    * @param autowire the autowire type
    * @param option the inject option
    * @param search the search info describing how to search for the injected bean if we have a hierarchy of 
    * {@link org.jboss.dependency.spi.Controller}s 
    * @return the contextual injection
    */
   public abstract ValueMetaData createContextualInject(ControllerState whenRequired, ControllerState dependentState, AutowireType autowire, InjectOption option, SearchInfo search);

   /**
    * Inject values from the context of the bean we are creating
    *
    * @param fromContext enum specifying what to inject from the {@link org.jboss.kernel.spi.dependency.KernelControllerContext}
    * @return the from context injection
    */
   public ValueMetaData createFromContextInject(FromContext fromContext)
   {
      return createFromContextInject(fromContext, null);
   }

   /**
    * Inject values from the context of another bean
    *
    * @param fromContext enum specifying what to inject from the {@link org.jboss.kernel.spi.dependency.KernelControllerContext}
    * @param contextName the context name
    * @return the from context injection
    */
   public ValueMetaData createFromContextInject(FromContext fromContext, Object contextName)
   {
      return createFromContextInject(fromContext, contextName, null);
   }

   /**
    * Inject values from the context of another bean
    *
    * @param fromContext enum specifying what to inject from the {@link org.jboss.kernel.spi.dependency.KernelControllerContext}
    * @param contextName the context name
    * @param dependentState the state of the injected/other context
    * @return the from context injection
    */
   public ValueMetaData createFromContextInject(FromContext fromContext, Object contextName, ControllerState dependentState)
   {
      return createFromContextInject(fromContext, contextName, dependentState, null);
   }

   /**
    * Inject values from the context of another bean
    *
    * @param fromContext enum specifying what to inject from the {@link org.jboss.kernel.spi.dependency.KernelControllerContext}
    * @param contextName the context name
    * @param dependentState the state of the injected/other context
    * @param search the search info describing how to search for the injected bean if we have a hierarchy of 
    * {@link org.jboss.dependency.spi.Controller}s 
    * @return the from context injection
    */
   public abstract ValueMetaData createFromContextInject(FromContext fromContext, Object contextName, ControllerState dependentState, SearchInfo search);

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
    * Create a new collection, where we can specify the exact type of Collection we would like
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
    * Create a new list, where we can specify the exact type of List we would like
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
    * Create a new set, where we can specify the exact type of Set we would like
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
    * Create a new array, where we can specify the exact type of array we would like
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
    * Create a new map, where we can specify the exact type of Map we would like
    * 
    * @param mapType the map type
    * @param keyType the key type
    * @param valueType the value type
    * @return the map
    */
   public abstract Map<ValueMetaData, ValueMetaData> createMap(String mapType, String keyType, String valueType);
}
