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

import org.jboss.beans.metadata.api.annotations.Depends;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.DependencyMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.reflect.spi.ClassInfo;

/**
 * Depends annotation plugin.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class DependsAnnotationPlugin extends ClassAnnotationPlugin<Depends>
{
   public static final DependsAnnotationPlugin INSTANCE = new DependsAnnotationPlugin();

   protected DependsAnnotationPlugin()
   {
      super(Depends.class);
   }

   protected List<? extends MetaDataVisitorNode> internalApplyAnnotation(ClassInfo info, Depends annotation, BeanMetaData beanMetaData)
   {
      Set<DependencyMetaData> dependencies = beanMetaData.getDepends();
      if (dependencies == null)
      {
         AbstractBeanMetaData abmd = checkIfNotAbstractBeanMetaDataSpecific(beanMetaData);
         dependencies = new HashSet<DependencyMetaData>();
         abmd.setDepends(dependencies);
      }
      List<MetaDataVisitorNode> nodes = new ArrayList<MetaDataVisitorNode>();
      for(String depends : annotation.value())
      {
         AbstractDependencyMetaData dependency = new AbstractDependencyMetaData(depends);
         if (dependencies.add(dependency))
            nodes.add(dependency);
      }
      return nodes;
   }
}
