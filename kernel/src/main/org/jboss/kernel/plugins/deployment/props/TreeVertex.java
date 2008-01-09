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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.logging.Logger;
import org.jboss.util.graph.Edge;
import org.jboss.util.graph.Vertex;

/**
 * Tree vertex.
 *
 * @param <T> exact vertex type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class TreeVertex<T extends Vertex<String>> extends Vertex<String>
{
   /** The logger */
   protected Logger log = Logger.getLogger(getClass());
   /** The last token */
   private String lastToken;

   protected TreeVertex(String name)
   {
      super(name);
   }

   @SuppressWarnings("unchecked")
   public void visit()
   {
      Vertex<String> parent = getParent(this);
      Set<Vertex<String>> children = getChildren(this);

      if (log.isTraceEnabled())
         log.trace("Structure visit, parent: " + parent + ", children: " + children);

      visit((T)parent, children);
   }

   /**
    * Get the last token.
    *
    * @return last token string
    */
   protected String getLastToken()
   {
      if (lastToken == null)
      {
         String fullName = getName();
         int p = fullName.lastIndexOf('.') + 1;
         lastToken = fullName.substring(p);
      }
      return lastToken;
   }

   /**
    * Get the parent vertex from vertex param.
    *
    * @param vertex the vertex to check
    * @return parent vertex
    */
   @SuppressWarnings("unchecked")
   protected static Vertex<String> getParent(Vertex<String> vertex)
   {
      List<Edge<String>> edges = vertex.getIncomingEdges();

      Vertex<String> parent = null;
      if (edges.isEmpty() == false)
      {
         Set<Vertex<String>> previous = new HashSet<Vertex<String>>();
         for(Edge<String> edge : edges)
            previous.add(edge.getFrom());

         if (previous.size() > 1)
            throw new IllegalArgumentException("Multiple parents: " + vertex);

         return previous.iterator().next();
      }
      return parent;
   }

   /**
    * Get children from vertext param.
    *
    * @param vertex the vertex to check
    * @return children vertices
    */
   protected static Set<Vertex<String>> getChildren(Vertex<String> vertex)
   {
      Set<Vertex<String>> children = new HashSet<Vertex<String>>();
      for(int i = 0; i < vertex.getOutgoingEdgeCount(); i++)
      {
         Edge<String> edge = vertex.getOutgoingEdge(i);
         children.add(edge.getTo());
      }
      return children;
   }

   /**
    * Get previous from vertex param.
    *
    * @param vertex the vertex
    * @return previous vertex
    */
   protected static Vertex<String> getPrevious(Vertex<String> vertex)
   {
      return getParent(vertex);
   }

   /**
    * Get the next vertex.
    *
    * @param vertex the vertex
    * @return next vertex
    */
   protected static Vertex<String> getNext(Vertex<String> vertex)
   {
      Set<Vertex<String>> children = getChildren(vertex);

      if (children.isEmpty())
         return null;

      if (children.size() > 1)
         throw new IllegalArgumentException("Multiple children: " + vertex);

      return children.iterator().next();
   }

   /**
    * Do the visit.
    *
    * @param parent the parent vertex
    * @param children the children vertices
    */
   public abstract void visit(T parent, Set<Vertex<String>> children);
}
