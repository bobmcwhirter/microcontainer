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

import java.util.Iterator;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.kernel.plugins.dependency.UpdateableDependencyItem;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.util.JBossStringBuilder;
import org.jboss.reflect.spi.TypeInfo;

/**
 * Injection value.
 *
 * @author <a href="ales.justin@gmail.com">Ales Justin</a>
 */
public class AbstractInjectionValueMetaData extends AbstractDependencyValueMetaData
{
   protected InjectionType injectionType = InjectionType.BY_CLASS;

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
         ControllerContext context = controller.getInstalledContext(info.getType());
         return context.getTarget();
      }
      return super.getValue(info, cl);
   }

   public void initialVisit(MetaDataVisitor visitor)
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

         if (InjectionType.BY_NAME.equals(injectionType))
         {
            if (propertyMetaData == null)
            {
               throw new IllegalArgumentException("Illegal usage of type ByName - injection not used with property = " + this);
            }
            setValue(propertyMetaData.getName());
         }
         else if (InjectionType.BY_CLASS.equals(injectionType))
         {
            // set controller
            KernelControllerContext context = visitor.getControllerContext();
            controller = (KernelController) context.getController();
            if (propertyMetaData != null)
            {
               DependencyItem item = new PropertyPlaceholderDependencyItem(context.getName(), propertyMetaData.getName());
               visitor.addDependency(item);
            }
            visitor.initialVisit(this); // as in AbstractValueMetaData
            // skip AbstractDependencyVMD.initialVisit() - no value defined
            return;
         }
         else
         {
            throw new IllegalArgumentException("Unknown injection type=" + injectionType);
         }
      }
      super.initialVisit(visitor);
   }

   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      if (injectionType != null)
         buffer.append(" injectionType=").append(injectionType);
      if (propertyMetaData != null)
         buffer.append(" propertyMetaData=").append(propertyMetaData.getName()); //else overflow - indefinite recursion
   }

   public class PropertyPlaceholderDependencyItem extends AbstractDependencyItem implements UpdateableDependencyItem
   {
      private String propertyName;
      private Class demandClass;

      public PropertyPlaceholderDependencyItem(Object name, String propertyName)
      {
         super(name, null, ControllerState.CONFIGURED, dependentState);
         this.propertyName = propertyName;
      }

      public void update(BeanMetaData metaData, BeanInfo info)
      {
         Set propertyInfos = info.getProperties();
         if (propertyInfos != null)
         {
            for (Iterator it = propertyInfos.iterator(); it.hasNext();)
            {
               PropertyInfo pi = (PropertyInfo) it.next();
               if (propertyName.equals(pi.getName()))
               {
                  demandClass = pi.getType().getType();
                  break;
               }
            }
         }
      }

      public boolean resolve(Controller controller)
      {
         if (demandClass != null)
         {
            ControllerContext context = controller.getInstalledContext(demandClass);
            if (context != null)
            {
               setIDependOn(context.getName());
               addDependsOnMe(controller, context);
               setResolved(true);
            }
            else
            {
               setResolved(false);
            }
         }
         else
         {
            setResolved(true);
         }
         return isResolved();
      }

      public void toString(JBossStringBuilder buffer)
      {
         super.toString(buffer);
         buffer.append(" demandClass=").append(demandClass);
      }

      public void toShortString(JBossStringBuilder buffer)
      {
         buffer.append(getName()).append(" demands ").append(demandClass);
      }

   }

}
