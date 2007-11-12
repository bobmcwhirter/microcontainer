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
package org.jboss.kernel.plugins.deployment.props;

import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import org.jboss.kernel.plugins.deployment.props.vertex.DefaultVertexFactory;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.util.graph.Graph;
import org.jboss.util.graph.Vertex;

/**
 * Properties to Graph factory.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class PropertiesGraphFactory
{
   /** The graph */
   private Graph<String> graph;

   /** The root */
   private DeploymentVertex root;

   /** The vertext factory */
   private VertexFactory vertexFactory = new DefaultVertexFactory();

   public PropertiesGraphFactory(Properties properties)
   {
      if (properties == null)
         throw new IllegalArgumentException("Null properties.");

      buildGraph(toMap(properties));
   }

   public PropertiesGraphFactory(Map<String, String> properties)
   {
      if (properties == null)
         throw new IllegalArgumentException("Null properties.");

      buildGraph(properties);
   }

   /**
    * Build the kernel deployment.
    *
    * @return KernelDeployment instance
    */
   public KernelDeployment build()
   {
      graph.depthFirstSearch(root, vertexFactory.visitor());
      return root.get();
   }

   /**
    * Transform properties to map.
    *
    * @param properties the properties
    * @return map
    */
   protected static Map<String, String> toMap(Properties properties)
   {
      Map<String, String> map = new TreeMap<String, String>();
      for(Object key : properties.keySet())
      {
         String ks = key.toString();
         map.put(ks, properties.getProperty(ks));
      }
      return map;
   }

   /**
    * Build graph from properties.
    *
    * @param properties the properties
    */
   protected void buildGraph(Map<String, String> properties)
   {
      graph = new Graph<String>();
      root = vertexFactory.rootVertex();
      graph.addVertex(root);
      for(String key : properties.keySet())
      {
         String value = properties.get(key);
         buildVertices(root, 0, key, value, 0);
      }
   }

   /**
    * Build vertices from property key and value.
    *
    * @param previous previous vertex
    * @param index current dot index
    * @param key the property key
    * @param value the value
    * @param level current level
    */
   @SuppressWarnings("unchecked")
   protected void buildVertices(Vertex<String> previous, int index, String key, String value, int level)
   {
      int p = key.indexOf('.', index + 1);
      int end = p > 0 ? p : key.length();
      String name = key.substring(0, end);

      Vertex<String> current = graph.findVertexByName(name);
      if (current == null)
      {
         current = vertexFactory.createVertex(level, name);
         graph.addVertex(current);
      }
      graph.addEdge(previous, current, level);

      if (p < 0)
      {
         Vertex<String> valueVertex = vertexFactory.valueVertex(value);
         graph.addVertex(valueVertex);
         graph.addEdge(current, valueVertex, -1);
      }
      else
         buildVertices(current, p, key, value, level + 1);
   }

   /**
    * Set the vertex factory.
    *
    * @param vertexFactory the vertex factory
    */
   public void setVertexFactory(VertexFactory vertexFactory)
   {
      this.vertexFactory = vertexFactory;
   }

   public String toString()
   {
      return graph.toString();
   }
}
