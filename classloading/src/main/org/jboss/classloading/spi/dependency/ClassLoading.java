/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.classloading.spi.dependency;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.jboss.classloader.spi.ClassLoaderSystem;

/**
 * ClassLoading.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoading
{
   /** The classloading domains by name */
   private Map<String, Domain> domains = new ConcurrentHashMap<String, Domain>();
   
   /**
    * Add a module
    * 
    * @param module the module
    * @throws IllegalArgumentException for a null module
    */
   public void addModule(Module module)
   {
      if (module == null)
         throw new IllegalArgumentException("Null module");
      
      String domainName = module.getDeterminedDomainName();
      if (domainName == null)
         domainName = ClassLoaderSystem.DEFAULT_DOMAIN_NAME;
      
      Domain domain;
      synchronized (domains)
      {
         domain = domains.get(domainName);
         if (domain == null)
         {
            domain = new Domain(domainName);
            domains.put(domainName, domain);
         }
      }
      
      domain.addModule(module);
   }
   
   /**
    * Remove a module
    * 
    * @param module the module
    * @throws IllegalArgumentException for a null module
    */
   public void removeModule(Module module)
   {
      if (module == null)
         throw new IllegalArgumentException("Null module");
      module.release();
   }
}
