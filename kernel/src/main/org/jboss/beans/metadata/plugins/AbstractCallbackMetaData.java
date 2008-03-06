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

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlTransient;

import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.dependency.spi.CallbackItem;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.plugins.dependency.CallbackDependencyItem;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.util.JBossStringBuilder;

/**
 * Metadata for callback.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractCallbackMetaData extends AbstractLifecycleMetaData
   implements CallbackMetaData, Serializable
{
   private static final long serialVersionUID = 2L;

   /** The cardinality */
   protected Cardinality cardinality;

   /** The property name */
   protected String property;

   /** The required state of the dependency */
   protected ControllerState whenRequired = ControllerState.INSTALLED;

   /** The required state of the dependency */
   protected ControllerState dependentState = ControllerState.INSTALLED;

   /** The signature */
   protected String signature;

   /** The property info */
   protected transient PropertyInfo propertyInfo;

   /** The method info */
   protected transient MethodInfo methodInfo;

   public AbstractCallbackMetaData()
   {
      setState(ControllerState.INSTALLED);
   }

   public String getProperty()
   {
      return property;
   }

   /**
    * Set the property.
    *
    * @param property property name
    */
   @XmlAttribute
   public void setProperty(String property)
   {
      this.property = property;
      flushJBossObjectCache();
   }

   public Cardinality getCardinality()
   {
      return cardinality;
   }

   /**
    * Set the cardinality.
    *
    * @param cardinality the cardinality
    */
   @XmlAttribute
   public void setCardinality(Cardinality cardinality)
   {
      this.cardinality = cardinality;
      flushJBossObjectCache();
   }

   public ControllerState getWhenRequired()
   {
      return whenRequired;
   }

   /**
    * Set when required state.
    *
    * @param whenRequired when is this call back required (default Configured)
    */
   @XmlAttribute(name="whenRequired")
   public void setWhenRequired(ControllerState whenRequired)
   {
      this.whenRequired = whenRequired;
      flushJBossObjectCache();
   }

   public String getSignature()
   {
      return signature;
   }

   /**
    * Set the signature.
    *
    * @param signature method / property parameter signature
    */
   @XmlAttribute
   public void setSignature(String signature)
   {
      this.signature = signature;
      flushJBossObjectCache();
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

   public ControllerState getDependentState()
   {
      return dependentState;
   }

   @XmlTransient
   public void setPropertyInfo(PropertyInfo propertyInfo)
   {
      this.propertyInfo = propertyInfo;
      this.property = propertyInfo.getName();
   }

   @XmlTransient
   public void setMethodInfo(MethodInfo methodInfo)
   {
      this.methodInfo = methodInfo;
      this.methodName = methodInfo.getName();
   }

   /**
    * Add install / uninstrall callback.
    * 
    * @param visitor the meta data visitor
    * @param callback the callback item
    */
   protected abstract void addCallback(MetaDataVisitor visitor, CallbackItem<?> callback);

   public void describeVisit(MetaDataVisitor vistor)
   {
      try
      {
         KernelControllerContext context = vistor.getControllerContext();
         CallbackItem<?> callback;
         if (property != null)
         {
            if (propertyInfo == null)
            {
               ClassLoader cl = Configurator.getClassLoader(context.getBeanMetaData());
               propertyInfo = Configurator.resolveProperty(log.isTraceEnabled(), context.getBeanInfo(), cl, property, signature);
            }
            callback = CallbackCreatorUtil.createCallback(context, propertyInfo, whenRequired, dependentState, cardinality);
         }
         else if (methodName != null)
         {
            if (methodInfo == null)
               methodInfo = Configurator.findMethodInfo(getClassInfo(context), methodName, new String[]{signature});
            callback = CallbackCreatorUtil.createCallback(context, methodInfo, whenRequired, dependentState, cardinality);
         }
         else
            throw new IllegalArgumentException("Illegal usage - not property or method:" + this);

         // add callback
         addCallback(vistor, callback);

         // demand name is Class in this case
         if (cardinality != null)
         {
            vistor.addDependency(new CallbackDependencyItem(context.getName(), (Class<?>)callback.getIDependOn(), whenRequired, dependentState, cardinality));
         }
      }
      catch (Throwable t)
      {
         throw new Error(t);
      }
      super.describeVisit(vistor);
   }

   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      if (property != null)
         buffer.append(" property=").append(property);
      if (cardinality != null)
         buffer.append(" cardinality=").append(cardinality);
      if (signature != null)
         buffer.append(" signature=").append(signature);
      if (ControllerState.INSTALLED.equals(dependentState) == false)
         buffer.append(" dependentState=" + dependentState);
      if (ControllerState.INSTALLED.equals(whenRequired) == false)
         buffer.append(" whenRequired=" + whenRequired);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      if (property != null)
         buffer.append("property=").append(property);
      if (methodName != null)
         buffer.append("method=").append(methodName);
   }
}
