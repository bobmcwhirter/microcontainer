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
import java.util.Stack;

import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.util.JBossStringBuilder;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.plugins.config.Configurator;

/**
 * Metadata for a parameter.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractParameterMetaData extends AbstractFeatureMetaData implements ParameterMetaData
{
   /**
    * The parameter type
    */
   protected String type;

   /**
    * The parameter value
    */
   protected ValueMetaData value;

   /**
    * The index in parameter list
    */
   protected int index;

   /**
    * Create a new parameter meta data
    */
   public AbstractParameterMetaData()
   {
   }

   /**
    * Create a new parameter meta data
    *
    * @param value the value
    */
   public AbstractParameterMetaData(Object value)
   {
      this.type = value.getClass().getName();
      this.value = new AbstractValueMetaData(value);
   }

   /**
    * Create a new parameter meta data
    *
    * @param value the value metadata
    */
   public AbstractParameterMetaData(ValueMetaData value)
   {
      this.value = value;
   }

   /**
    * Create a new parameter meta data
    *
    * @param type  the type
    * @param value the value
    */
   public AbstractParameterMetaData(String type, Object value)
   {
      this.type = type;
      this.value = new AbstractValueMetaData(value);
   }

   /**
    * Create a new parameter meta data
    *
    * @param type  the type
    * @param value the string value
    */
   public AbstractParameterMetaData(String type, String value)
   {
      this.type = type;
      this.value = new StringValueMetaData(value);
   }

   /**
    * Create a new parameter meta data
    *
    * @param type  the type
    * @param value the value meta data
    */
   public AbstractParameterMetaData(String type, ValueMetaData value)
   {
      this.type = type;
      this.value = value;
   }

   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
      this.type = type;
      flushJBossObjectCache();
   }

   public ValueMetaData getValue()
   {
      return value;
   }

   public int getIndex()
   {
      return index;
   }

   public void setIndex(int index)
   {
      this.index = index;
   }

   public void setValue(ValueMetaData value)
   {
      this.value = value;
      flushJBossObjectCache();
   }

   protected void addChildren(Set<MetaDataVisitorNode> children)
   {
      super.addChildren(children);
      if (value != null)
         children.add(value);
   }

   public Class getType(MetaDataVisitor visitor, MetaDataVisitorNode previous) throws Throwable
   {
      if (type != null)
      {
         KernelControllerContext context = visitor.getControllerContext();
         ClassLoader cl = Configurator.getClassLoader(context.getBeanMetaData());
         KernelController controller = (KernelController) context.getController();
         KernelConfigurator configurator = controller.getKernel().getConfigurator();
         return applyCollectionOrMapCheck(configurator.getClassInfo(type, cl).getType());
      }
      else
      {
         Stack visitorNodeStack = visitor.visitorNodeStack();
         TypeProvider typeProvider = (TypeProvider) visitorNodeStack.pop();
         try
         {
            return typeProvider.getType(visitor, this);
         }
         finally
         {
            visitorNodeStack.push(typeProvider);
         }
      }
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("type=").append(type);
      buffer.append(" value=").append(value);
      super.toString(buffer);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(type);
   }
}
