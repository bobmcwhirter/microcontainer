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
import org.jboss.metadata.plugins.cache.TimedCachePolicyFactory;

/**
 * TimedPolicyCachingBasicKernelMetaDataRepository.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class TimedPolicyCachingBasicKernelMetaDataRepository extends PolicyCachingBasicKernelMetaDataRepository
{
   private Integer defaultLifetime;
   private Boolean threadSafe;
   private Integer resolution;

   public TimedPolicyCachingBasicKernelMetaDataRepository()
   {
   }

   public TimedPolicyCachingBasicKernelMetaDataRepository(Integer defaultLifetime)
   {
      this(defaultLifetime, null, null);
   }

   public TimedPolicyCachingBasicKernelMetaDataRepository(Integer defaultLifetime, Boolean threadSafe, Integer resolution)
   {
      this.defaultLifetime = defaultLifetime;
      this.threadSafe = threadSafe;
      this.resolution = resolution;
   }

   protected CachePolicyFactory createCachePolicyFactory()
   {
      if (defaultLifetime == null)
         defaultLifetime = parseInteger(readSystemProperty("TimedPolicyCaching.lifetime", null));
      if (threadSafe == null)
         threadSafe = Boolean.valueOf(readSystemProperty("TimedPolicyCaching.threadSafe", Boolean.TRUE.toString()));
      if (resolution == null)
         resolution = parseInteger(readSystemProperty("TimedPolicyCaching.resolution", null));

      log.debug("Creating timed cache policy, lifetime: " + defaultLifetime + ", threadSafe: " + threadSafe + ", resolution: " + resolution);

      if (defaultLifetime == null)
         return new TimedCachePolicyFactory();
      else if (resolution != null)
         return new TimedCachePolicyFactory(defaultLifetime, threadSafe, resolution);
      else
         return new TimedCachePolicyFactory(defaultLifetime);
   }

   /**
    * Set default lifetime.
    *
    * @param defaultLifetime the default lifetime
    */
   public void setDefaultLifetime(Integer defaultLifetime)
   {
      this.defaultLifetime = defaultLifetime;
   }

   /**
    * Set threadsafe flag.
    *
    * @param threadSafe the threadsafe flag
    */
   public void setThreadSafe(Boolean threadSafe)
   {
      this.threadSafe = threadSafe;
   }

   /**
    * The resollution.
    *
    * @param resolution the resolution
    */
   public void setResolution(Integer resolution)
   {
      this.resolution = resolution;
   }
}