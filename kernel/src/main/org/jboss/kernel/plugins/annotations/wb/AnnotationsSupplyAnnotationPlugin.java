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
package org.jboss.kernel.plugins.annotations.wb;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractSupplyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.kernel.plugins.annotations.ClassAnnotationPlugin;
import org.jboss.metadata.spi.MetaData;
import org.jboss.reflect.spi.ClassInfo;

/**
 * Generic annotations supply metadata value creator.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AnnotationsSupplyAnnotationPlugin extends ClassAnnotationPlugin<Annotation>
{
   @SuppressWarnings("unchecked")
   public AnnotationsSupplyAnnotationPlugin(Class annotation)
   {
      super(annotation);
   }

   /**
    * Find the annotations supply.
    *
    * @param supplies the supplies
    * @return matcher supply or null if not found
    */
   protected AnnotationsSupply findAnnotationsSupply(Set<SupplyMetaData> supplies)
   {
      for(SupplyMetaData smd : supplies)
      {
         final Object supply = smd.getSupply();
         if (supply instanceof AnnotationsSupply)
         {
            return (AnnotationsSupply) supply;
         }
      }
      return null;
   }

   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(ClassInfo info, MetaData retrieval, Annotation annotation, BeanMetaData beanMetaData) throws Throwable
   {
      Set<SupplyMetaData> supplies = beanMetaData.getSupplies();
      if (supplies == null)
      {
         AbstractBeanMetaData abmd = checkIfNotAbstractBeanMetaDataSpecific(beanMetaData);
         supplies = new HashSet<SupplyMetaData>();
         abmd.setSupplies(supplies);
         return applyAnnotationsSupply(annotation, supplies);
      }
      AnnotationsSupply as = findAnnotationsSupply(supplies);
      if (as == null)
         return applyAnnotationsSupply(annotation, supplies);
      else
      {
         as.addAnnotation(annotation);
         return null;
      }
   }

   /**
    * Apply annotations supply.
    *
    * @param annotation the annotation
    * @param supplies the supplies
    * @return new supply
    */
   protected List<? extends MetaDataVisitorNode> applyAnnotationsSupply(Annotation annotation, Set<SupplyMetaData> supplies)
   {
      SupplyMetaData supply = new AbstractSupplyMetaData(new AnnotationsSupply(annotation));
      supplies.add(supply);
      return Collections.singletonList(supply);
   }
}