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
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.kernel.plugins.dependency.ClassContextDependencyItem;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossStringBuilder;

/**
 * Injection value.
 *
 * @author <a href="ales.justin@gmail.com">Ales Justin</a>
 */
public class AbstractInjectionValueMetaData extends AbstractDependencyValueMetaData
{
   private static final long serialVersionUID = 1L;

   protected InjectionType injectionType = InjectionType.BY_CLASS;

   /**
    * Simplyifies things with InjectionType.BY_NAME
    */
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
         Controller controller = context.getController();
         ControllerContext lookup = controller.getInstalledContext(info.getType());
         if (lookup == null)
         {
            throw new IllegalArgumentException("Possible multiple matching beans, see log for info.");
         }
         // TODO - add progression here, then fix BeanMetaData as well
         return lookup.getTarget();
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

         visitor.initialVisit(this);
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
            context = visitor.getControllerContext();

            // we pop it so that parent node has the same semantics as this one
            // meaning that his current peek is also his parent
            // and all other nodes that cannot determine type follow the same
            // contract - popping and pushing
            // maybe the whole thing can be rewritten to LinkedList
            // or simply using the fact that Stack is also a Vector?
            MetaDataVisitorNode node = visitor.visitorNodeStack().pop();
            try
            {
               if (node instanceof TypeProvider)
               {
                  TypeProvider typeProvider = (TypeProvider) node;
                  DependencyItem item = new ClassContextDependencyItem(
                        context.getName(),
                        typeProvider.getType(visitor, this),
                        visitor.getContextState(),
                        dependentState);
                  visitor.addDependency(item);
               }
               else
               {
                  throw new Error(TypeProvider.ERROR_MSG);
               }
            }
            catch (Error error)
            {
               throw error;
            }
            catch (Throwable throwable)
            {
               throw new Error(throwable);
            }
            finally
            {
               visitor.visitorNodeStack().push(node);
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

}
