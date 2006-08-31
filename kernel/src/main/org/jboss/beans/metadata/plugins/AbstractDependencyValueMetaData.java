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

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.joinpoint.spi.TargettedJoinpoint;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossStringBuilder;

/**
 * Dependency value.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractDependencyValueMetaData extends AbstractValueMetaData
{
   /** The controller */
   protected KernelController controller;
   
   /** The property name */
   protected String property;

   /** The when required state of the dependency or null to use current context state */
   protected ControllerState whenRequiredState;

   /** The required state of the dependency or null to look in the registry */
   protected ControllerState dependentState = ControllerState.INSTALLED;

   /**
    * Create a new dependency value
    */
   public AbstractDependencyValueMetaData()
   {
   }

   /**
    * Create a new dependency value
    * 
    * @param value the value
    */
   public AbstractDependencyValueMetaData(Object value)
   {
      super(value);
   }

   /**
    * Create a new dependency value
    * 
    * @param value the value
    * @param property the property
    */
   public AbstractDependencyValueMetaData(Object value, String property)
   {
      super(value);
      this.property = property;
   }

   /**
    * Get the property
    * 
    * @return the property
    */
   public String getProperty()
   {
      return property;
   }
   
   /**
    * Set the property
    * 
    * @param property the property name
    */
   public void setProperty(String property)
   {
      this.property = property;
   }
   
   /**
    * Set the when required state of the dependency
    * 
    * @param whenRequiredState the when required state or null if it uses current context state
    */
   public void setWhenRequiredState(ControllerState whenRequredState)
   {
      this.whenRequiredState = whenRequredState;
      flushJBossObjectCache();
   }

   public ControllerState getWhenRequiredState()
   {
      return whenRequiredState;
   }
   
   /**
    * Set the required state of the dependency
    *
    * @param dependentState the required state or null if it must be in the registry
    */
   public void setDependentState(ControllerState dependentState)
   {
      this.dependentState = dependentState;
      flushJBossObjectCache();
   }

   public ControllerState getDependentState()
   {
      return dependentState;
   }

   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      ControllerState state = dependentState;
      if (state == null)
         state = ControllerState.INSTALLED;
      ControllerContext context = controller.getContext(value, state);
      if (context == null)
         throw new Error("Should not be here - dependency failed! " + this);
      Object result = context.getTarget();
      if (result != null && property != null)
      {
         KernelConfigurator configurator = controller.getKernel().getConfigurator();
         BeanInfo beanInfo = configurator.getBeanInfo(result.getClass());
         TargettedJoinpoint joinpoint = configurator.getPropertyGetterJoinPoint(beanInfo, property);
         joinpoint.setTarget(result);
         return joinpoint.dispatch();
      }
      return result;
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      KernelControllerContext controllerContext = visitor.getControllerContext();
      controller = (KernelController) controllerContext.getController();
      Object name = controllerContext.getName();
      Object iDependOn = getUnderlyingValue();
      ControllerState whenRequired = whenRequiredState;
      if (whenRequired == null)
      {
         whenRequired = visitor.getContextState();
      }

      DependencyItem item = new AbstractDependencyItem(name, iDependOn, whenRequired, dependentState);
      visitor.addDependency(item);

      super.initialVisit(visitor);
   }
      
   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      if (property != null)
         buffer.append(" property=").append(property);
      if (whenRequiredState != null)
         buffer.append(" whenRequiredState=").append(whenRequiredState.getStateString());
      if (dependentState != null)
         buffer.append(" dependentState=").append(dependentState.getStateString());
   }
}
