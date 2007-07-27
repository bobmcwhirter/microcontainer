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

import org.jboss.beans.metadata.plugins.AbstractSupplyMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.annotations.Supplys;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.reflect.spi.ClassInfo;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SupplysAnnotationPlugin extends ClassAnnotationPlugin<Supplys>
{
   public SupplysAnnotationPlugin()
   {
      super(Supplys.class);
   }

   protected void internalApplyAnnotation(ClassInfo info, Supplys annotation, BeanMetaData beanMetaData)
   {
      Set<SupplyMetaData> supplies = beanMetaData.getSupplies();
      if (supplies == null)
      {
         supplies = new HashSet<SupplyMetaData>();
         ((AbstractBeanMetaData)beanMetaData).setSupplies(supplies);
      }
      for(String supply : annotation.value())
      {
         supplies.add(new AbstractSupplyMetaData(supply));
      }
   }
}
