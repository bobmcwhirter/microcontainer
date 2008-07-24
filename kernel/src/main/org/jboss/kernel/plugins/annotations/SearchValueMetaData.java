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
package org.jboss.kernel.plugins.annotations;

import org.jboss.beans.metadata.plugins.AbstractValueMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.plugins.graph.Search;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.dependency.spi.dispatch.AttributeDispatchContext;
import org.jboss.dependency.spi.graph.GraphController;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossStringBuilder;

/**
 * Search value metadata.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SearchValueMetaData extends AbstractValueMetaData
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;
   
   private ControllerState state;
   private org.jboss.dependency.plugins.graph.Search search;
   private String property;

   private ControllerContext context;

   public SearchValueMetaData(Object value, ControllerState state, Search search, String property)
   {
      super(value);
      if (search == null)
         throw new IllegalArgumentException("Null search type");
      
      this.state = state;
      this.search = search;
      this.property = property;
   }

   public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
   {
      // we're here, so it must be GraphController instance
      Controller controller = context.getController();
      GraphController gc = (GraphController)controller;

      ControllerState dependentState = state;
      if (dependentState == null)
         dependentState = ControllerState.INSTALLED;
      ControllerContext context = gc.getContext(getUnderlyingValue(), dependentState, search);

      Object result;
      if (property != null && property.length() > 0)
      {
         if (context instanceof AttributeDispatchContext)
         {
            AttributeDispatchContext adc = (AttributeDispatchContext)context;
            result = adc.get(property);
         }
         else
            throw new IllegalArgumentException(
                  "Cannot use property attribute, context is not AttributeDispatchContext: " + context +
                  ", metadata: " + this);
      }
      else
      {
         result = context.getTarget();
      }

      return info != null ? info.convertValue(result) : result;
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      context = visitor.getControllerContext();

      super.initialVisit(visitor);
   }

   public void describeVisit(MetaDataVisitor visitor)
   {
      Object name = context.getName();
      Object iDependOn = getUnderlyingValue();

      ControllerState whenRequired = visitor.getContextState();
      ControllerState dependentState = state;
      if (dependentState == null)
         dependentState = ControllerState.INSTALLED;

      DependencyItem item = new SearchDependencyItem(name, iDependOn, whenRequired, dependentState);
      visitor.addDependency(item);

      super.describeVisit(visitor);
   }

   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      buffer.append("search=").append(search);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      super.toShortString(buffer);
      buffer.append("search=").append(search);
   }

   private class SearchDependencyItem extends AbstractDependencyItem
   {
      private SearchDependencyItem(Object name, Object iDependOn, ControllerState whenRequired, ControllerState dependentState)
      {
         super(name, iDependOn, whenRequired, dependentState);
      }

      public boolean resolve(Controller controller)
      {
         if (controller instanceof GraphController)
         {
            GraphController gc = (GraphController)controller;
            ControllerContext context = gc.getContext(getIDependOn(), getDependentState(), search);
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
         return false;
      }

      protected void toHumanReadableString(StringBuilder builder)
      {
         super.toHumanReadableString(builder);
         builder.append("search=").append(search);
      }
   }
}
