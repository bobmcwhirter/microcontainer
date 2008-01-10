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
package org.jboss.classloader.plugins.jdk;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import org.jboss.classloader.spi.base.BaseClassLoader;
import org.jboss.classloader.spi.base.BaseClassLoaderDomain;
import org.jboss.classloader.spi.base.BaseClassLoaderPolicy;
import org.jboss.classloader.spi.jdk.JDKChecker;

/**
 * AbstractJDKChecker.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AbstractJDKChecker implements JDKChecker
{
   /** The hack to the security manager */
   private static final Hack hack;
   
   /** Classes in the classpath that should be excluded: FOR TESTING ONLY */
   private static Set<Class<?>> excluded = new CopyOnWriteArraySet<Class<?>>();
   
   static
   {
      hack = AccessController.doPrivileged(new PrivilegedAction<Hack>()
      {
         public Hack run()
         {
            return new Hack();
         }
      });
   }

   /**
    * Whether the class is excluded
    * 
    * @param clazz the class
    * @return whether the class is excluded
    */
   public boolean isExcluded(Class<?> clazz)
   {
      // No excludes
      if (excluded.isEmpty())
         return false;
      
      for (Class<?> exclude : excluded)
      {
         if (exclude.isAssignableFrom(clazz))
            return true;
      }
      
      // Not excluded
      return false;
   }
   
   /**
    * The excluded classes 
    * 
    * @return the excluded classes
    */
   public static Set<Class<?>> getExcluded()
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm != null)
         sm.checkCreateClassLoader();
      return excluded;
   }
   
   public boolean isJDKRequest(String name)
   {
      Class<?> requesting = getRequestingClass(hack.getClassContext());
      return isJDKRequestingClass(name, requesting);
   }

   /**
    * Get the  requesting class
    * 
    * @param stack the class stack 
    * @return the requesting class
    */
   protected Class<?> getRequestingClass(Class<?>[] stack)
   {
      for (Class<?> clazz : stack)
      {
         if (Hack.class.isAssignableFrom(clazz) == false &&
             JDKChecker.class.isAssignableFrom(clazz) == false &&
             BaseClassLoaderDomain.class.isAssignableFrom(clazz) == false &&
             BaseClassLoaderPolicy.class.isAssignableFrom(clazz) == false &&
             ClassLoader.class.isAssignableFrom(clazz) == false &&
             Class.class.isAssignableFrom(clazz) == false)
         {
            return clazz;
         }
      }
      throw new RuntimeException("Should not be here!");
   }
   
   /**
    * Whether the requesting class is from the JDK<p>
    * 
    * The default implementation returns true if the requesting class is not loaded from a
    * {@link BaseClassLoader}
    * 
    * @param name the name of the class being loaded
    * @param requesting the requesting class
    * @return true it is from the JDK
    */
   protected boolean isJDKRequestingClass(String name, Class<?> requesting)
   {
      if (isExcluded(requesting))
         return false;
      ClassLoader cl = requesting.getClassLoader();
      if (cl == null)
         return true;
      return (cl instanceof BaseClassLoader == false);
   }
   
   /**
    * Extend the security manager so we can get access to the stacktrace
    */
   private static class Hack extends SecurityManager
   {
      @Override
      public Class<?>[] getClassContext()
      {
         return super.getClassContext();
      }
   }
}
