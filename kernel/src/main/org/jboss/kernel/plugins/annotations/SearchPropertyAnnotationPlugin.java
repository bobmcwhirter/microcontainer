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

import java.util.HashMap;
import java.util.Map;
import java.util.Arrays;

import org.jboss.beans.metadata.api.annotations.Search;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.dependency.spi.ControllerState;

/**
 * Search value annotation plugin.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SearchPropertyAnnotationPlugin extends PropertyAnnotationPlugin<Search>
{
   public final static SearchPropertyAnnotationPlugin INSTANCE = new SearchPropertyAnnotationPlugin();

   private static final Map<String, org.jboss.dependency.plugins.graph.Search> types;

   static
   {
      types = new HashMap<String,org.jboss.dependency.plugins.graph.Search>();
      for (org.jboss.dependency.plugins.graph.Search search : org.jboss.dependency.plugins.graph.Search.values())
      {
         types.put(search.type().toUpperCase(), search);
      }
   }

   protected SearchPropertyAnnotationPlugin()
   {
      super(Search.class);
   }

   public ValueMetaData createValueMetaData(Search search)
   {
      String searchType = search.type();
      org.jboss.dependency.plugins.graph.Search type = types.get(searchType.toUpperCase());
      if (type == null)
         throw new IllegalArgumentException("No such search type: " + searchType + ", available: " + Arrays.toString(org.jboss.dependency.plugins.graph.Search.values()));

      ControllerState state = null;
      if (isAttributePresent(search.dependentState()))
         state = new ControllerState(search.dependentState());
      String property= null;
      if (isAttributePresent(search.property()))
         property = search.property();

      return new SearchValueMetaData(
            search.bean(),
            state,
            type,
            property
      );
   }
}