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

import org.jboss.kernel.plugins.deployment.props.TreeVertex;

/**
 * Keyword matching level vertex factory.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class KeywordLevelVertexFactory extends AbstractLevelVertexFactory
{
   private String keyword;

   protected KeywordLevelVertexFactory(String keyword)
   {
      if (keyword == null)
         throw new IllegalArgumentException("Null keyword.");
      this.keyword = keyword;
   }

   public TreeVertex<?> createVertex(String name)
   {
      int p = name.lastIndexOf(".") + 1;
      String keyword = name.substring(p);

      if (this.keyword.equalsIgnoreCase(keyword) == false)
         return null;

      return createVertex(name, keyword);
   }

   /**
    * Create vertex from name and keyword.
    *
    * @param name the name
    * @param keyword the keyword
    * @return new vertex instance
    */
   protected  abstract TreeVertex<?> createVertex(String name, String keyword);
}
