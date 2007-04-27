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
package org.jboss.classloader.spi.base;

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderPolicy;

/**
 * Base ClassLoaderSystem.
 * 
 * TODO Permissions
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class BaseClassLoaderSystem
{
   /**
    * A long version of toString()
    * 
    * @return the long string
    */
   public String toLongString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(getClass().getSimpleName());
      builder.append("@").append(Integer.toHexString(System.identityHashCode(this)));
      builder.append("{");
      toLongString(builder);
      builder.append('}');
      return builder.toString();
   }
   
   /**
    * For subclasses to add information for toLongString()
    * 
    * @param builder the builder
    */
   protected void toLongString(StringBuilder builder)
   {
   }

   @Override
   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(getClass().getSimpleName());
      builder.append("@").append(Integer.toHexString(System.identityHashCode(this)));
      return builder.toString();
   }

   /**
    * Register a policy with a domain
    * 
    * @param domain the domain
    * @param policy the policy
    * @return the classloader
    * @throws IllegalArgumentException if a parameter is null
    * @throws IllegalStateException if the policy is already registered with a domain  
    */
   protected ClassLoader registerClassLoaderPolicy(BaseClassLoaderDomain domain, ClassLoaderPolicy policy)
   {
      if (domain == null)
         throw new IllegalArgumentException("Null domain");

      BaseClassLoader classLoader = createClassLoader(policy);
      domain.registerClassLoader(classLoader);
      return classLoader;
   }
   
   /**
    * Unregister a policy with a domain
    * 
    * @param domain the domain
    * @param policy the policy
    * @throws IllegalArgumentException if a parameter is null
    * @throws IllegalStateException if the policy is not registered with the domain  
    */
   protected void unregisterClassLoaderPolicy(BaseClassLoaderDomain domain, ClassLoaderPolicy policy)
   {
      if (domain == null)
         throw new IllegalArgumentException("Null domain");
      if (policy == null)
         throw new IllegalArgumentException("Null policy");
      
      BaseClassLoaderPolicy basePolicy = policy;
      BaseClassLoader classLoader = basePolicy.getClassLoader();
      if (classLoader == null)
         throw new IllegalStateException("Policy has no associated classloader, it is therefore not registered with a domain. " + policy.toLongString());
      domain.unregisterClassLoader(classLoader);
   }
   
   /**
    * Unregister a policy with a domain
    * 
    * @param domain the domain
    * @param classLoader the class loader
    * @throws IllegalArgumentException if a parameter is null
    * @throws IllegalStateException if the policy is not registered with the domain  
    */
   protected void unregisterClassLoader(ClassLoaderDomain domain, ClassLoader classLoader)
   {
      if (domain == null)
         throw new IllegalArgumentException("Null domain");
      if (classLoader == null)
         throw new IllegalArgumentException("Null classLoader");
      
      if (classLoader instanceof BaseClassLoader == false)
         throw new IllegalStateException("ClassLoader is not the correct type and therefore not registered: " + classLoader);
      
      BaseClassLoader baseClassLoader = (BaseClassLoader) classLoader;
      unregisterClassLoaderPolicy(domain, baseClassLoader.getPolicy());
   }

   /**
    * Create a classloader for the given policy
    * 
    * @param policy the policy
    * @return the classloader
    * @throws IllegalArgumentException for a null policy
    */
   protected BaseClassLoader createClassLoader(ClassLoaderPolicy policy)
   {
      return new BaseClassLoader(policy);
   }
}
