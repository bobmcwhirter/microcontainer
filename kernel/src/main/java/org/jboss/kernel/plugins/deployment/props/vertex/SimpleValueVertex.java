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

import org.jboss.beans.metadata.plugins.StringValueMetaData;
import org.jboss.beans.metadata.plugins.ValueMetaDataAware;
import org.jboss.kernel.plugins.deployment.props.TreeVertex;
import org.jboss.util.graph.Vertex;

/**
 * Simple value vertex.
 * Single string value.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SimpleValueVertex extends TreeVertex<Vertex<String>>
{
   public SimpleValueVertex(String value)
   {
      super(value);
   }

   public void visit(Vertex<String> parent, Set<Vertex<String>> children)
   {
      if (parent instanceof ValueMetaDataAware)
      {
         StringValueMetaData value = new StringValueMetaData(getName());
         Set<Vertex<String>> parentsChildren = getChildren(parent);
         for (Vertex<String> vertex : parentsChildren)
         {
            if (vertex == this)
               continue;

            String name = vertex.getName();
            if (name.endsWith("type"))
            {
               Vertex<String> next = getNext(vertex);
               value.setType(next.getName());
            }
            else if (name.endsWith("trim"))
            {
               Vertex<String> next = getNext(vertex);
               value.setTrim(Boolean.parseBoolean(next.getName()));
            }
            else if (name.endsWith("replace"))
            {
               Vertex<String> next = getNext(vertex);
               value.setReplace(Boolean.parseBoolean(next.getName()));
            }
         }
         ValueMetaDataAware valueMetaDataAware = (ValueMetaDataAware)parent;
         valueMetaDataAware.setValue(value);
      }
   }
}
