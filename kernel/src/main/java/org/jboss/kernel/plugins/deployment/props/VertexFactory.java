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

import org.jboss.util.graph.Visitor;

/**
 * Vertex factory.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public interface VertexFactory
{
   /**
    * Create root/deploymenent vertex.
    *
    * @return DeploymentVertex instance
    */
   DeploymentVertex rootVertex();

   /**
    * Create simple vertex.
    *
    * @param level current level
    * @param name full name
    * @return new tree vertex
    */
   TreeVertex<?> createVertex(int level, String name);

   /**
    * Create value vertext.
    *
    * @param value the text value
    * @return new value vertex
    */
   TreeVertex<?> valueVertex(String value);

   /**
    * Get the visitor.
    *
    * @return visitor instance
    */
   Visitor<String> visitor();
}
