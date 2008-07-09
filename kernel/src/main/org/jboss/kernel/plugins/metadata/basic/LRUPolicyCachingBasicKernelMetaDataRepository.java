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
package org.jboss.kernel.plugins.metadata.basic;

import org.jboss.metadata.plugins.cache.CachePolicyFactory;
import org.jboss.metadata.plugins.cache.ConcurrentLRUCachePolicyFactory;

/**
 * LRUPolicyCachingBasicKernelMetaDataRepository.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class LRUPolicyCachingBasicKernelMetaDataRepository extends PolicyCachingBasicKernelMetaDataRepository
{
   private Integer min;
   private Integer max;

   public LRUPolicyCachingBasicKernelMetaDataRepository()
   {
   }

   public LRUPolicyCachingBasicKernelMetaDataRepository(Integer min, Integer max)
   {
      this.min = min;
      this.max = max;
   }

   protected CachePolicyFactory createCachePolicyFactory()
   {
      if (min == null)
         min = parseInteger(readSystemProperty("LRUPolicyCaching.min", null));
      if (max == null)
         max = parseInteger(readSystemProperty("LRUPolicyCaching.max", null));

      if (min == null || max == null)
         throw new IllegalArgumentException("Missing min (" + min + ") or max (" + max + ").");

      return new ConcurrentLRUCachePolicyFactory(min, max);
   }

   /**
    * Set min.
    *
    * @param min the min
    */
   public void setMin(Integer min)
   {
      this.min = min;
   }

   /**
    * set max.
    *
    * @param max the max
    */
   public void setMax(Integer max)
   {
      this.max = max;
   }
}