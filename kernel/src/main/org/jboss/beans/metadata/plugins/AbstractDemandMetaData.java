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
import java.util.Iterator;

import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.dependency.plugins.AbstractDependencyItem;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * A demand.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractDemandMetaData extends JBossObject
   implements DemandMetaData, Serializable
{
   private static final long serialVersionUID = 1L;

   /** The demand */
   protected Object demand;
   
   /** When the dependency is required */
   protected ControllerState whenRequired = ControllerState.DESCRIBED;
   
   /** The dependency delegate */
   protected AbstractDependencyItem dependencyItem;

   /**
    * Create a new demand
    */
   public AbstractDemandMetaData()
   {
   }

   /**
    * Create a new demand
    * 
    * @param demand the demand
    */
   public AbstractDemandMetaData(Object demand)
   {
      this.demand = demand;
   }
   
   /**
    * Set the required state of the dependency
    * 
    * @param whenRequired when the dependecy is required
    */
   public void setWhenRequired(ControllerState whenRequired)
   {
      this.whenRequired = whenRequired;
      flushJBossObjectCache();
   }
   
   /**
    * Set the demand
    * 
    * @param demand the demand
    */
   public void setDemand(Object demand)
   {
      this.demand = demand;
      flushJBossObjectCache();
   }

   public Object getDemand()
   {
      return demand;
   }

   public ControllerState getWhenRequired()
   {
      return whenRequired;
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      KernelControllerContext context = visitor.getControllerContext();
      DependencyItem item = new DemandDependencyItem(context.getName());
      visitor.addDependency(item);
      visitor.initialVisit(this);
   }

   public void describeVisit(MetaDataVisitor vistor)
   {
      vistor.describeVisit(this);
   }

   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      return null;
   }
   
   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("demand=").append(demand);
      if (whenRequired != null)
         buffer.append(" whenRequired=").append(whenRequired.getStateString());
   }
   
   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(demand);
   }

   /**
    * Information about a demand dependency.
    */
   public class DemandDependencyItem extends AbstractDependencyItem 
   {
      /**
       * Create a new demand dependecy
       * 
       * @param name my name
       */
      public DemandDependencyItem(Object name)
      {
         super(name, null, whenRequired, null);
      }
      
      public boolean resolve(Controller controller)
      {
         ControllerContext context = controller.getInstalledContext(demand);
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

      public void unresolved()
      {
         setIDependOn(null);
         setResolved(false);
      }
      
      public void toString(JBossStringBuilder buffer)
      {
         super.toString(buffer);
         buffer.append(" demand=").append(demand);
      }
      
      public void toShortString(JBossStringBuilder buffer)
      {
         buffer.append(getName()).append(" demands ").append(demand);
      }

      @Override
      public String toHumanReadableString()
      {
         StringBuilder builder = new StringBuilder();
         builder.append("Demands '").append(getDemand());
         return builder.toString();
      }
   }
}
