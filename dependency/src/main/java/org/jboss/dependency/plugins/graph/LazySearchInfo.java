/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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

import java.util.Collections;
import java.util.Map;
import java.io.Serializable;

import org.jboss.dependency.spi.graph.LookupStrategy;
import org.jboss.dependency.spi.graph.SearchInfo;
import org.jboss.reflect.plugins.introspection.ReflectionUtils;

/**
 * Lazy search info.
 * The type is the class.
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public class LazySearchInfo implements SearchInfo, Serializable
{
   private static final long serialVersionUID = 1L;

   private String className;
   private transient LookupStrategy strategy;

   public LazySearchInfo(String className)
   {
      if (className == null)
         throw new IllegalArgumentException("Null class name");

      this.className = className;
   }

   public String getType()
   {
      return className;
   }

   public Map<String, ?> getInfo()
   {
      return Collections.emptyMap();
   }

   public LookupStrategy getStrategy()
   {
      if (strategy == null)
      {
         Object result;
         try
         {
            result = ReflectionUtils.newInstance(className);
         }
         catch (Throwable t)
         {
            throw new RuntimeException(t);
         }

         if (LookupStrategy.class.isInstance(result) == false)
            throw new IllegalArgumentException("Result is not LookupStrategy instance: " + result);

         strategy = LookupStrategy.class.cast(result);
      }
      return strategy;
   }

   @Override
   public String toString()
   {
      return "LazySearchInfo: " + className;
   }
}
