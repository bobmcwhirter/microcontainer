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
import java.util.List;
import java.util.Set;

import javax.xml.bind.annotation.XmlType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElements;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlAttribute;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.plugins.builder.MutableParameterizedMetaData;
import org.jboss.beans.metadata.spi.ConstructorMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.ConstructorInfo;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;
import org.jboss.managed.api.annotation.ManagementProperty;

/**
 * Metadata for construction.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@XmlType(name="constructorType", propOrder={"annotations", "factory", "parameters", "value"})
public class AbstractConstructorMetaData extends AbstractFeatureMetaData
   implements ConstructorMetaData, MutableParameterizedMetaData, ValueMetaDataAware, Serializable
{
   private static final long serialVersionUID = 2L;

   /**
    * The paramaters List<ParameterMetaData>
    */
   protected List<ParameterMetaData> parameters;

   /**
    * The value
    */
   protected ValueMetaData value;

   /**
    * The factory
    */
   protected ValueMetaData factory;

   /**
    * The factory class name
    */
   protected String factoryClassName;

   /**
    * The factory method
    */
   protected String factoryMethod;

   /**
    * Create a new constructor meta data
    */
   public AbstractConstructorMetaData()
   {
   }

   /**
    * Set the parameters
    *
    * @param parameters List<ParameterMetaData>
    */
   @XmlElement(name="parameter", type=AbstractParameterMetaData.class)
   public void setParameters(List<ParameterMetaData> parameters)
   {
      this.parameters = parameters;
      flushJBossObjectCache();
   }

   /**
    * Set the value
    *
    * @param value the value
    */
   @XmlElements
   ({
      @XmlElement(name="bean", type=AbstractBeanMetaData.class),
      @XmlElement(name="lazy", type=AbstractLazyMetaData.class),
      @XmlElement(name="array", type=AbstractArrayMetaData.class),
      @XmlElement(name="collection", type=AbstractCollectionMetaData.class),
      @XmlElement(name="list", type=AbstractListMetaData.class),
      @XmlElement(name="map", type=AbstractMapMetaData.class),
      @XmlElement(name="set", type=AbstractSetMetaData.class),
      @XmlElement(name="value", type=StringValueMetaData.class),
      @XmlElement(name="inject", type=AbstractInjectionValueMetaData.class),
      @XmlElement(name="value-factory", type=AbstractValueFactoryMetaData.class)
   })
   public void setValue(ValueMetaData value)
   {
      this.value = value;
      flushJBossObjectCache();
   }

   @XmlAnyElement
   @ManagementProperty(ignored = true)
   public void setValueObject(Object value)
   {
      if (value == null)
         setValue(null);
      else if (value instanceof ValueMetaData)
         setValue((ValueMetaData) value);
      else
         setValue(new AbstractValueMetaData(value));
   }

   /**
    * Set the factory
    *
    * @param factory the factory
    */
   @XmlElement(name="factory", type=AbstractDependencyValueMetaData.class)
   public void setFactory(ValueMetaData factory)
   {
      // HACK to have wildcard factories
      if (factory != null && factory instanceof AbstractDependencyValueMetaData)
      {
         Object underlying = factory.getUnderlyingValue();
         if (underlying != null && underlying instanceof ValueMetaData)
            factory = (ValueMetaData) underlying;
      }

      this.factory = factory;
      flushJBossObjectCache();
   }

   /**
    * Set the factory class name
    *
    * @param name the factory class name
    */
   @XmlAttribute(name="factoryClass")
   public void setFactoryClass(String name)
   {
      this.factoryClassName = name;
      flushJBossObjectCache();
   }

   /**
    * Set the factory method
    *
    * @param name the factory method
    */
   @XmlAttribute(name="factoryMethod")
   public void setFactoryMethod(String name)
   {
      this.factoryMethod = name;
      flushJBossObjectCache();
   }

   public List<ParameterMetaData> getParameters()
   {
      return parameters;
   }

   public ValueMetaData getValue()
   {
      return value;
   }

   public ValueMetaData getFactory()
   {
      return factory;
   }

   public String getFactoryClass()
   {
      return factoryClassName;
   }

   public String getFactoryMethod()
   {
      return factoryMethod;
   }

   protected void addChildren(Set<MetaDataVisitorNode> children)
   {
      super.addChildren(children);
      if (parameters != null)
         children.addAll(parameters);
      if (value != null)
         children.add(value);
      if (factory != null)
         children.add(factory);
   }

   public TypeInfo getType(MetaDataVisitor visitor, MetaDataVisitorNode previous) throws Throwable
   {
      if (factory != null || factoryClassName != null)
      {
         KernelControllerContext context = visitor.getControllerContext();
         ClassLoader cl = Configurator.getClassLoader(context.getBeanMetaData());
         KernelConfigurator configurator = context.getKernel().getConfigurator();
         ClassInfo classInfo;
         if (factory != null)
         {
            Object target = factory.getValue(null, cl);
            classInfo = configurator.getClassInfo(target.getClass());
         }
         else
         {
            classInfo = configurator.getClassInfo(factoryClassName, cl);
         }
         // should be parameter
         ParameterMetaData parameter = (ParameterMetaData) previous;
         String[] parameterTypes = Configurator.getParameterTypes(false, parameters);
         MethodInfo methodInfo = Configurator.findMethodInfo(classInfo, factoryMethod, parameterTypes, factoryClassName != null, true);
         return applyCollectionOrMapCheck(methodInfo.getParameterTypes()[parameter.getIndex()]);
      }
      else
      {
         KernelControllerContext context = visitor.getControllerContext();
         BeanInfo beanInfo = context.getBeanInfo();
         // find matching parameter
         if (previous instanceof ParameterMetaData)
         {
            ParameterMetaData parameter = (ParameterMetaData) previous;
            String[] paramTypes = Configurator.getParameterTypes(false, parameters);
            ConstructorInfo ci = Configurator.findConstructorInfo(beanInfo.getClassInfo(), paramTypes);
            return applyCollectionOrMapCheck(ci.getParameterTypes()[parameter.getIndex()]);
         }
         else
         {
            // currently value constructor supports only values that are instances of class itself
            // this will add another instance with the same class to context
            ClassInfo type = beanInfo.getClassInfo();
            log.warn("Constructing bean from injection value: results in multiple beans with same class type - " + type);
            return type;
/*
            // find all constructors with single value
            Set<ConstructorInfo> constructors = beanInfo.getConstructors();
            Set<ConstructorInfo> matchingConstructorInfos = new HashSet<ConstructorInfo>();
            if (constructors != null)
            {
               for (ConstructorInfo ci : constructors)
               {
                  if (ci.getParameters() != null && ci.getParameters().length == 1)
                  {
                     matchingConstructorInfos.add(ci);
                  }
               }
            }
            if (matchingConstructorInfos.size() != 1)
            {
               throw new IllegalArgumentException("Should not be here - illegal size of matching constructors: " + this);
            }
            return applyCollectionOrMapCheck(matchingConstructorInfos.iterator().next().getParameterTypes()[0].getType());
*/
         }
      }
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("parameters=");
      JBossObject.list(buffer, parameters);
      if (value != null)
         buffer.append(" value=").append(value);
      if (factory != null)
         buffer.append(" factory=").append(factory);
      if (factoryClassName != null)
         buffer.append(" factoryClass=").append(factoryClassName);
      if (factoryMethod != null)
         buffer.append(" factoryMethod=").append(factoryMethod);
      super.toString(buffer);
   }
}
