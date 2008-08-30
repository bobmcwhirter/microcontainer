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
package org.jboss.kernel.plugins.deployment.props.vertex;

import java.util.Set;

import org.jboss.kernel.plugins.deployment.props.TreeVertex;
import org.jboss.util.graph.Vertex;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;

/**
 * Property vertex factory.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class PropertyVertexFactory extends AbstractLevelVertexFactory
{
   public PropertyVertexFactory()
   {
      super(Integer.MAX_VALUE);
   }

   public TreeVertex<?> createVertex(String name)
   {
      return new PropertyVertex(name);
   }

   /**
    * Property vertex.
    */
   class PropertyVertex extends ValueMetaDataAwareVertex<AbstractPropertyMetaData, BeanVertexFactory.BeanVertex>
   {
      public PropertyVertex(String name)
      {
         super(name);
      }

      protected AbstractPropertyMetaData createValueMetaDataAware()
      {
         return new AbstractPropertyMetaData();
      }

      public void visit(BeanVertexFactory.BeanVertex parent, Set<Vertex<String>> children)
      {
         delegate.setName(getLastToken());
         parent.getBeanMetaData().addProperty(delegate);
      }
   }
}
