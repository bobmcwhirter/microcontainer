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

/**
 * ClassLoaderInformation.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoaderInformation
{
   /** The classloader */
   private BaseClassLoader classLoader;
   
   /** The policy */
   private BaseClassLoaderPolicy policy;

   /** The order */
   private int order;
   
   /** The exports of the classloader */
   private BaseDelegateLoader exported;
   
   /**
    * Create a new ClassLoaderInformation.
    * 
    * @param classLoader the classloader
    * @param policy the policy
    * @param order the added order
    * @throws IllegalArgumentException for a null parameter
    */
   public ClassLoaderInformation(BaseClassLoader classLoader, BaseClassLoaderPolicy policy, int order)
   {
      if (classLoader == null)
         throw new IllegalArgumentException("Null classloader");
      if (policy == null)
         throw new IllegalArgumentException("Null policy");
      this.classLoader = classLoader;
      this.policy = policy;
      this.order = order;
      this.exported = policy.getExported();
   }

   /**
    * Get the classLoader.
    * 
    * @return the classLoader.
    */
   public BaseClassLoader getClassLoader()
   {
      return classLoader;
   }

   /**
    * Get the policy.
    * 
    * @return the policy.
    */
   public BaseClassLoaderPolicy getPolicy()
   {
      return policy;
   }

   /**
    * Get the order.
    * 
    * @return the order.
    */
   public int getOrder()
   {
      return order;
   }

   /**
    * Get the exported.
    * 
    * @return the exported.
    */
   public BaseDelegateLoader getExported()
   {
      return exported;
   }
   
   @Override
   public String toString()
   {
      return policy.toString();
   }
}
