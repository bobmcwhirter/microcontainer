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

import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;

import org.jboss.beans.metadata.plugins.AbstractSupplyMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.api.annotations.Supplys;
import org.jboss.beans.metadata.api.annotations.Supply;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.reflect.spi.ClassInfo;

/**
 * Supplys annotation plugin.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SupplysAnnotationPlugin extends ClassAnnotationPlugin<Supplys>
{
   public static final SupplysAnnotationPlugin INSTANCE = new SupplysAnnotationPlugin();

   protected SupplysAnnotationPlugin()
   {
      super(Supplys.class);
   }

   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(ClassInfo info, Supplys annotation, BeanMetaData beanMetaData)
   {
      Set<SupplyMetaData> supplies = beanMetaData.getSupplies();
      if (supplies == null)
      {
         AbstractBeanMetaData abmd = checkIfNotAbstractBeanMetaDataSpecific(beanMetaData);
         supplies = new HashSet<SupplyMetaData>();
         abmd.setSupplies(supplies);
      }
      List<MetaDataVisitorNode> nodes = new ArrayList<MetaDataVisitorNode>();
      for(Supply supply : annotation.value())
      {
         AbstractSupplyMetaData asmd = new AbstractSupplyMetaData(supply.value());
         if (isAttributePresent(supply.type()))
            asmd.setType(supply.type().getName());
         
         if (supplies.add(asmd))
            nodes.add(asmd);
      }
      return nodes;
   }
}
