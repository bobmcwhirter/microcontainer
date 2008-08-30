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

import org.jboss.beans.metadata.plugins.AbstractDependencyValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.plugins.ValueMetaDataAware;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.plugins.deployment.props.TreeVertex;
import org.jboss.util.graph.Vertex;

/**
 * Complex Value vertex.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ComplexValueVertex extends TreeVertex<Vertex<String>>
{
   public ComplexValueVertex(String value)
   {
      super(value);
   }

   public void visit(Vertex<String> parent, Set<Vertex<String>> children)
   {
      if (parent instanceof ValueMetaDataAware)
      {
         String[] tokens = getName().split("\\.");
         if ("(inject)".equals(tokens[0]) == false)
            throw new IllegalArgumentException("Currently only injection is supported.");

         ValueMetaData value;
         if ("autowire".equals(tokens[1]))
         {
            value = new AbstractInjectionValueMetaData();
         }
         else
         {
            if (tokens.length > 2)
            {
               value = new AbstractDependencyValueMetaData(tokens[1], tokens[2]);
            }
            else
            {
               value = new AbstractDependencyValueMetaData(tokens[1]);                  
            }
         }
         ValueMetaDataAware valueMetaDataAware = (ValueMetaDataAware)parent;
         valueMetaDataAware.setValue(value);
      }
   }
}
