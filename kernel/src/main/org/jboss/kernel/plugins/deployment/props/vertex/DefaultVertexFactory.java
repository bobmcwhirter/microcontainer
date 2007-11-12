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

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.jboss.kernel.plugins.deployment.props.DeploymentVertex;
import org.jboss.kernel.plugins.deployment.props.TreeVertex;
import org.jboss.kernel.plugins.deployment.props.VertexFactory;
import org.jboss.logging.Logger;
import org.jboss.util.graph.Graph;
import org.jboss.util.graph.Vertex;
import org.jboss.util.graph.Visitor;

/**
 * Default vertex factory.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class DefaultVertexFactory implements VertexFactory
{
   /** The logger */
   protected Logger logger = Logger.getLogger(getClass());
   /** The level factories */
   protected Map<Integer, Set<LevelVertexFactory>> levelFactories = new HashMap<Integer, Set<LevelVertexFactory>>();

   public DefaultVertexFactory()
   {
      addLevelFactory(0, new BeanVertexFactory());
      addLevelFactory(1, new ClassVertexFactory());
      addLevelFactory(1, new PropertyVertexFactory());
      addLevelFactory(2, new HolderVertexFactory());
   }

   public void addLevelFactory(int level, LevelVertexFactory factory)
   {
      Set<LevelVertexFactory> factories = levelFactories.get(level);
      if (factories == null)
      {
         factories = new TreeSet<LevelVertexFactory>(LevelVertexFactory.COMPARATOR);
         levelFactories.put(level, factories);
      }
      factories.add(factory);
   }

   public DeploymentVertex rootVertex()
   {
      return new BaseDeploymentVertex();
   }

   public TreeVertex createVertex(int level, String name)
   {
      Set<LevelVertexFactory> factories = levelFactories.get(level);
      if (factories == null)
         throw new IllegalArgumentException("No matching level factories: " + name + " / " + level);

      for(LevelVertexFactory factory : factories)
      {
         TreeVertex vertex = factory.createVertex(name);
         if (vertex != null)
            return vertex;
      }
      throw new IllegalArgumentException("No matching level factory: " + name + " / " + level);
   }

   public TreeVertex valueVertex(String value)
   {
      return new ValueVertex(value);
   }

   public Visitor<String> visitor()
   {
      return new Visitor<String>()
      {
         public void visit(Graph<String> g, Vertex<String> v)
         {
            if (logger.isTraceEnabled())
               logger.trace(v);
         }
      };
   }
}

