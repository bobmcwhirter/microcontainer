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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDemandMetaData;
import org.jboss.beans.metadata.plugins.annotations.Demand;
import org.jboss.beans.metadata.plugins.annotations.Demands;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.ClassInfo;

/**
 * Demands annotation plugin.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class DemandsAnnotationPlugin extends ClassAnnotationPlugin<Demands>
{
   public DemandsAnnotationPlugin()
   {
      super(Demands.class);
   }

   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(ClassInfo info, Demands annotation, KernelControllerContext context)
   {
      BeanMetaData beanMetaData = context.getBeanMetaData();
      Set<DemandMetaData> demands = beanMetaData.getDemands();
      if (demands == null)
      {
         demands = new HashSet<DemandMetaData>();
         ((AbstractBeanMetaData)beanMetaData).setDemands(demands);
      }
      List<MetaDataVisitorNode> nodes = new ArrayList<MetaDataVisitorNode>();
      for(Demand demand : annotation.value())
      {
         AbstractDemandMetaData admd = new AbstractDemandMetaData(demand.value());
         admd.setWhenRequired(new ControllerState(demand.whenRequired()));
         if (demands.add(admd))
            nodes.add(admd);
      }
      return nodes;
   }
}
