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

import java.util.Set;

import org.jboss.beans.metadata.injection.InjectionMode;
import org.jboss.beans.metadata.injection.InjectionType;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.plugins.injection.InjectionUtil;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossStringBuilder;

/**
 * Injection value.
 *
 * @author <a href="ales.justin@gmail.com">Ales Justin</a>
 */
public class AbstractInjectionValueMetaData extends AbstractDependencyValueMetaData
{
   protected InjectionMode injectionMode = InjectionMode.BY_TYPE;

   protected InjectionType injectionType = InjectionType.STRICT;

   protected AbstractPropertyMetaData propertyMetaData;

   /**
    * Create a new injection value
    */
   public AbstractInjectionValueMetaData()
   {
   }

   /**
    * Create a new injection value
    *
    * @param value the value
    */
   public AbstractInjectionValueMetaData(Object value)
   {
      super(value);
   }

   /**
    * Create a new injection value
    *
    * @param value    the value
    * @param property the property
    */
   public AbstractInjectionValueMetaData(Object value, String property)
   {
      super(value, property);
   }

   public InjectionMode getInjectionMode()
   {
      return injectionMode;
   }

   public void setInjectionMode(InjectionMode injectionMode)
   {
      this.injectionMode = injectionMode;
   }

   public InjectionType getInjectionType()
   {
      return injectionType;
   }

   public void setInjectionType(InjectionType injectionType)
   {
      this.injectionType = injectionType;
   }

   public AbstractPropertyMetaData getPropertyMetaData()
   {
      return propertyMetaData;
   }

   public void setPropertyMetaData(AbstractPropertyMetaData propertyMetaData)
   {
      this.propertyMetaData = propertyMetaData;
   }

   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      if (value == null)
      {
         ControllerState state = dependentState;
         if (state == null)
         {
            state = ControllerState.INSTALLED;
         }
         // what else to use here - if not info.getType?
         return InjectionUtil.resolveInjection(
               controller,
               info.getType(),
               propertyMetaData.getName(), // should not be used - value set before, see visit(MDV visitor)
               state,
               getInjectionMode(),
               getInjectionType(),
               this);
      }
      return super.getValue(info, cl);
   }

   public void visit(MetaDataVisitor visitor)
   {
      // determine value
      if (getUnderlyingValue() == null)
      {
         // check for property
         if (property != null)
         {
            property = null;
            log.warn("Ignoring property - contextual injection: " + this);
         }

         if (InjectionMode.BY_NAME.equals(injectionMode))
         {
            setValue(propertyMetaData.getName());
         }
         else if (InjectionMode.BY_TYPE.equals(injectionMode))
         {
            // set controller
            KernelControllerContext controllerContext = visitor.getControllerContext();
            controller = (KernelController) controllerContext.getController();
            visitor.visit(this); // as in AbstractValueMetaData
            // skip AbstractDependencyVMD.visit() - no value defined
            return;
         }
         else
         {
            throw new IllegalArgumentException("Unknown injection mode=" + injectionMode);
         }
      }
      super.visit(visitor);
   }

   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      if (injectionMode != null)
         buffer.append(" injectionMode=").append(injectionMode);
      if (injectionType != null)
         buffer.append(" injectionType=").append(injectionType);
   }
}
