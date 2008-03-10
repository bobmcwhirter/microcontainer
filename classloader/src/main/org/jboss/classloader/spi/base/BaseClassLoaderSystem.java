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

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;

import org.jboss.classloader.spi.ClassLoaderPolicy;

/**
 * Base ClassLoaderSystem.
 * 
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
    * Register a domain
    * 
    * @param domain the domain to register
    * @throws IllegalArgumentException for a null domain
    */
   protected void registerDomain(BaseClassLoaderDomain domain)
   {
      if (domain == null)
         throw new IllegalArgumentException("Null domain");
      domain.setClassLoaderSystem(this);
   }

   /**
    * Unregister a domain
    * 
    * @param domain the domain to unregister
    * @throws IllegalArgumentException for a null domain
    */
   protected void unregisterDomain(BaseClassLoaderDomain domain)
   {
      if (domain == null)
         throw new IllegalArgumentException("Null domain");
      domain.setClassLoaderSystem(null);
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
   protected ClassLoader registerClassLoaderPolicy(final BaseClassLoaderDomain domain, final ClassLoaderPolicy policy)
   {
      if (domain == null)
         throw new IllegalArgumentException("Null domain");
      if (policy == null)
         throw new IllegalArgumentException("Null policy");

      return AccessController.doPrivileged(new PrivilegedAction<BaseClassLoader>()
      {
         public BaseClassLoader run()
         {
            BaseClassLoader classLoader = createClassLoader(policy);
            beforeRegisterClassLoader(classLoader);
            domain.registerClassLoader(classLoader);
            afterRegisterClassLoader(classLoader);
            return classLoader;
         }
      }, policy.getAccessControlContext());
   }
   
   /**
    * Unregister a policy with a domain
    * 
    * @param policy the policy
    * @throws IllegalArgumentException if a parameter is null
    * @throws IllegalStateException if the policy is not registered with the domain  
    */
   protected void unregisterClassLoaderPolicy(ClassLoaderPolicy policy)
   {
      if (policy == null)
         throw new IllegalArgumentException("Null policy");
      
      BaseClassLoaderPolicy basePolicy = policy;
      BaseClassLoader classLoader = basePolicy.getClassLoader();
      if (classLoader == null)
         throw new IllegalStateException("Policy has no associated classloader, it is therefore not registered with a domain. " + policy.toLongString());
      BaseClassLoaderDomain domain = basePolicy.getClassLoaderDomain();
      if (domain == null)
         throw new IllegalStateException("Policy has no domain " + policy.toLongString());
      beforeUnregisterClassLoader(classLoader);
      domain.unregisterClassLoader(classLoader);
      afterUnregisterClassLoader(classLoader);
   }

   /**
    * Unregister a policy with a domain
    * 
    * @param classLoader the class loader
    * @throws IllegalArgumentException if a parameter is null
    * @throws IllegalStateException if the policy is not registered with the domain  
    */
   protected void unregisterClassLoader(ClassLoader classLoader)
   {
      if (classLoader == null)
         throw new IllegalArgumentException("Null classLoader");
      
      if (classLoader instanceof BaseClassLoader == false)
         throw new IllegalStateException("ClassLoader is not the correct type and therefore not registered: " + classLoader);
      
      BaseClassLoader baseClassLoader = (BaseClassLoader) classLoader;
      unregisterClassLoaderPolicy(baseClassLoader.getPolicy());
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
   
   /**
    * Before register classloader
    * 
    * @param classLoader the classloader
    */
   protected void beforeRegisterClassLoader(ClassLoader classLoader)
   {
      // Nothing
   }
   
   /**
    * After register classloader
    * 
    * @param classLoader the classloader
    */
   protected void afterRegisterClassLoader(ClassLoader classLoader)
   {
      // Nothing
   }
   
   /**
    * Before unregister classloader
    * 
    * @param classLoader the classloader
    */
   protected void beforeUnregisterClassLoader(ClassLoader classLoader)
   {
      // Nothing
   }
   
   /**
    * After unregister classloader
    * 
    * @param classLoader the classloader
    */
   protected void afterUnregisterClassLoader(ClassLoader classLoader)
   {
      // Nothing
   }
   
   /**
    * Transform the byte code<p>
    *
    * By default this does nothing
    *
    * @param classLoader the classLoader
    * @param className the class name
    * @param byteCode the byte code
    * @param protectionDomain the protection domain
    * @return the transformed byte code
    * @throws Exception for any error
    */
   protected byte[] transform(ClassLoader classLoader, String className, byte[] byteCode, ProtectionDomain protectionDomain) throws Exception
   {
      return byteCode;
   }
}
