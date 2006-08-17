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

import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossStringBuilder;

/**
 * Injection value.
 *
 * @author <a href="ales.justin@gmail.com">Ales Justin</a>
 */
public class AbstractInjectionValueMetaData extends AbstractDependencyValueMetaData
{
   protected InjectionType injectionType = InjectionType.BY_CLASS;

   /** Simplyifies things with InjectionType.BY_NAME */
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
      }
      // check if was maybe set with by_name
      if (getUnderlyingValue() != null)
      {
         super.initialVisit(visitor);
      }
   }

   public void describeVisit(MetaDataVisitor visitor)
   {
      if (getUnderlyingValue() == null)
      {
         if (InjectionType.BY_CLASS.equals(injectionType))
         {
            KernelControllerContext context = visitor.getControllerContext();
            controller = (KernelController) context.getController(); // set controller
            TypeProvider typeProvider = (TypeProvider) visitor.visitorNodeStack().pop();
            try
            {
               DependencyItem item = new ClassContextDependencyItem(context.getName(), typeProvider.getType(visitor, this));
               visitor.addDependency(item);
            }
            catch (Throwable throwable)
            {
               throw new Error(throwable);
            } finally
            {
               visitor.visitorNodeStack().push(typeProvider);
            }
         }
         else
         {
            throw new IllegalArgumentException("Unknown injection type=" + injectionType);
         }
      }
      super.describeVisit(visitor);
   }

   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      if (injectionType != null)
         buffer.append(" injectionType=").append(injectionType);
      if (propertyMetaData != null)
         buffer.append(" propertyMetaData=").append(propertyMetaData.getName()); //else overflow - indefinite recursion
   }

   public class ClassContextDependencyItem extends AbstractDependencyItem
   {
      public ClassContextDependencyItem(Object name, Class demandClass)
      {
         super(name, demandClass, ControllerState.INSTANTIATED, dependentState);
      }

      public boolean resolve(Controller controller)
      {
         ControllerContext context = controller.getInstalledContext(getIDependOn());
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
         return isResolved();
      }

      public void toString(JBossStringBuilder buffer)
      {
         super.toString(buffer);
         buffer.append(" demandClass=").append(getIDependOn());
      }

      public void toShortString(JBossStringBuilder buffer)
      {
         buffer.append(getName()).append(" demands ").append(getIDependOn());
      }

   }

}
