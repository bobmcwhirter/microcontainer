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
package org.jboss.beans.metadata.plugins;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlTransient;
import javax.xml.bind.annotation.XmlType;

import org.jboss.beans.metadata.plugins.builder.MutableParameterizedMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.dispatch.InvokeDispatchHelper;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossStringBuilder;

/**
 * ValueFactory value.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
@XmlType(name="valueFactoryType")
public class AbstractValueFactoryMetaData extends AbstractValueMetaData implements MutableParameterizedMetaData
{
   private static final long serialVersionUID = 2L;

   /**
    * The context
    */
   protected transient KernelControllerContext context;

   /**
    * The property name
    */
   protected String method;

   /**
    * The parameter
    */
   protected String parameter;

   /**
    * The defaultValue
    */
   protected String defaultValue;

   /**
    * The when required state of the dependency or null to use current context state
    */
   protected ControllerState whenRequiredState;

   /**
    * The required state of the dependency or null to look in the registry
    */
   protected ControllerState dependentState;

   /** The parameters */
   protected List<ParameterMetaData> parameters;

   /**
    * Create a new dependency value
    */
   public AbstractValueFactoryMetaData()
   {
   }

   /**
    * Create a new dependency value
    *
    * @param value the value
    */
   public AbstractValueFactoryMetaData(Object value)
   {
      super(value);
   }

   /**
    * Create a new value-factory value
    *
    * @param value    the value
    * @param method   the method
    */
   public AbstractValueFactoryMetaData(Object value, String method)
   {
      this(value, method, null);
   }

   /**
    * Create a new value-factory value
    *
    * @param value    the value
    * @param method   the method
    * @param defaultValue   the default
    */
   public AbstractValueFactoryMetaData(Object value, String method, String defaultValue)
   {
      super(value);
      this.method = method;
      this.defaultValue = defaultValue;
   }

   @XmlAttribute(name="bean", required = true)
   public void setBean(String bean)
   {
      setValue(bean);
   }

   /**
    * Get the method
    *
    * @return the method
    */
   public String getMethod()
   {
      return method;
   }

   /**
    * Set the property
    *
    * @param method the property name
    */
   @XmlAttribute(required = true)
   public void setMethod(String method)
   {
      this.method = method;
   }

   /**
    * Get the parameter.
    *
    * @return the parameter
    */
   public String getParameter()
   {
      return parameter;
   }

   /**
    * Set the parameter.
    *
    * @param parameter the parameter
    */
   @XmlAttribute
   public void setParameter(String parameter)
   {
      this.parameter = parameter;
   }

   /**
    * Set the when required state of the dependency
    *
    * @param whenRequiredState the when required state or null if it uses current context state
    */
   @XmlAttribute(name="whenRequired")
   public void setWhenRequiredState(ControllerState whenRequiredState)
   {
      this.whenRequiredState = whenRequiredState;
      flushJBossObjectCache();
   }

   /**
    * Get when required state.
    *
    * @return when required state
    */
   public ControllerState getWhenRequiredState()
   {
      return whenRequiredState;
   }

   /**
    * Set the required state of the dependency
    *
    * @param dependentState the required state or null if it must be in the registry
    */
   @XmlAttribute(name="state")
   public void setDependentState(ControllerState dependentState)
   {
      this.dependentState = dependentState;
      flushJBossObjectCache();
   }

   /**
    * Get the dependant state.
    *
    * @return the dependant state
    */
   public ControllerState getDependentState()
   {
      return dependentState;
   }

   /**
    * Get the default value.
    *
    * @return the default value
    */
   public String getDefaultValue()
   {
      return defaultValue;
   }

   /**
    * Set the default value.
    *
    * @param defaultValue default value
    */
   @XmlAttribute(name="default")
   public void setDefaultValue(String defaultValue)
   {
      this.defaultValue = defaultValue;
   }

   /**
    * Get the parameters.
    *
    * @return the parameters
    */
   public List<ParameterMetaData> getParameters()
   {
      return parameters;
   }

   /**
    * Set the parameters.
    *
    * @param parameters the parameters
    */
   @XmlElement(name="parameter", type=AbstractParameterMetaData.class)
   public void setParameters(List<ParameterMetaData> parameters)
   {
      this.parameters = parameters;
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      if (getParameter() != null && getParameters() != null)
         throw new IllegalArgumentException("Both parameter and parameters cannot be set: " + this);
      if (getParameter() != null)
      {
         List<ParameterMetaData> parameters = new ArrayList<ParameterMetaData>();
         parameters.add(new AbstractParameterMetaData(String.class.getName(), getParameter()));
         setParameters(parameters);
         setParameter(null);
      }

      context = visitor.getControllerContext();
      Object name = context.getName();
      Object iDependOn = getUnderlyingValue();

      ControllerState whenRequired = whenRequiredState;
      if (whenRequired == null)
      {
         whenRequired = visitor.getContextState();
      }
      ControllerState state = dependentState;
      if (state == null)
      {
         state = ControllerState.INSTALLED;
      }

      DependencyItem item = new AbstractDependencyItem(name, iDependOn, whenRequired, state);
      visitor.addDependency(item);
      super.initialVisit(visitor);
   }

   @XmlTransient
   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      if (getParameters() != null)
         return getParameters().iterator();
      return null;
   }

   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      ControllerState state = dependentState;
      if (state == null)
         state = ControllerState.INSTALLED;
      KernelController controller = (KernelController)context.getController();
      ControllerContext lookup = controller.getContext(getUnderlyingValue(), state);
      if (lookup == null)
         throw new Error("Should not be here - dependency failed - " + this);
      if (lookup instanceof InvokeDispatchContext == false)
         throw new IllegalArgumentException("Underlying context is not InvokeDispatchContext: " + context);

      Kernel kernel = controller.getKernel();
      KernelConfigurator configurator = kernel.getConfigurator();
      Object result = InvokeDispatchHelper.invoke(
            configurator,
            lookup.getTarget(),
            (InvokeDispatchContext)lookup,
            getMethod(),
            getParameters()
      );
      if (result == null)
         result = defaultValue;
      return info != null ? info.convertValue(result) : result;
   }

   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      if (method != null)
         buffer.append(" method=").append(method);
      if (whenRequiredState != null)
         buffer.append(" whenRequiredState=").append(whenRequiredState.getStateString());
      if (dependentState != null)
         buffer.append(" dependentState=").append(dependentState.getStateString());
      if (defaultValue != null)
         buffer.append(" default=").append(defaultValue);
      if (parameters != null && parameters.isEmpty() == false)
         buffer.append(" parameters=").append(parameters);
   }
}
