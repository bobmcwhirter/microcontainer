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

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.jboss.metadata.plugins.cache.CacheFactory;
import org.jboss.metadata.plugins.cache.CachePolicyCacheFactory;
import org.jboss.metadata.plugins.cache.CachePolicyFactory;

/**
 * PolicyCachingBasicKernelMetaDataRepository.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class PolicyCachingBasicKernelMetaDataRepository extends CachingBasicKernelMetaDataRepository
{
   protected CacheFactory createCacheFactory()
   {
      return new CachePolicyCacheFactory(createCachePolicyFactory());
   }

   /**
    * Create cache policy factory.
    *
    * @return the cache policy factory
    */
   protected abstract CachePolicyFactory createCachePolicyFactory();

   /**
    * Read system property.
    *
    * @param key the property key
    * @param defaultValue the default value
    * @return system property or default value
    */
   protected static String readSystemProperty(final String key, final String defaultValue)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm == null)
         return System.getProperty(key, defaultValue);
      else
         return AccessController.doPrivileged(new PrivilegedAction<String>()
         {
            public String run()
            {
               return System.getProperty(key, defaultValue);
            }
         });
   }

   /**
    * Parse integer.
    *
    * @param value the string int value
    * @return integer value of null
    */
   protected static Integer parseInteger(String value)
   {
      if (value == null)
         return null;

      return Integer.parseInt(value);
   }
}