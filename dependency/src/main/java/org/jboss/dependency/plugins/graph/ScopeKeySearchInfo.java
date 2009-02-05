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
package org.jboss.dependency.plugins.graph;

import java.io.Serializable;
import java.util.Collections;
import java.util.Map;

import org.jboss.dependency.spi.graph.LookupStrategy;
import org.jboss.dependency.spi.graph.SearchInfo;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * ScopeKey search info.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ScopeKeySearchInfo implements SearchInfo, Serializable
{
   private static final long serialVersionUID = 1L;

   public static final String SCOPE_KEY = "ScopeKey";
   private ScopeKey scopeKey;
   private Map<String, ?> info;
   private transient LookupStrategy strategy;

   public ScopeKeySearchInfo(ScopeKey scopeKey)
   {
      if (scopeKey == null)
         throw new IllegalArgumentException("Null scope key");
      this.scopeKey = scopeKey;
   }

   public String getType()
   {
      return SCOPE_KEY;
   }

   public Map<String, ?> getInfo()
   {
      if (info == null)
         info = Collections.singletonMap(SCOPE_KEY, scopeKey);

      return info;
   }

   public LookupStrategy getStrategy()
   {
      if (strategy == null)
         strategy = new ScopeKeyLookupStrategy(scopeKey);

      return strategy;
   }
}