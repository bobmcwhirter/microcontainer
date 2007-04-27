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
package org.jboss.classloader.plugins;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;

import org.jboss.classloader.spi.base.BaseClassLoader;
import org.jboss.classloader.spi.base.BaseClassLoaderDomain;
import org.jboss.util.UnreachableStatementException;

/**
 * ClassLoaderUtils.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoaderUtils
{
   /** The hack to the security manager */
   private static final Hack hack = new Hack();
   
   /**
    * Check the class name makes sense
    * 
    * REVIEW other checks besides null and empty?
    * @param className the class name
    * @throws ClassNotFoundException for a malformed class name
    */
   public static final void checkClassName(final String className) throws ClassNotFoundException
   {
      if (className == null)
         throw new ClassNotFoundException("Null class name");
      if (className.trim().length() == 0)
         throw new ClassNotFoundException("Empty class name '" + className + "'");
   }

   /**
    * Convert a class name into a path
    * 
    * @param className the class name
    * @return the path
    */
   public static final String classNameToPath(final String className)
   {
      return className.replace('.', '/') + ".class";
   }

   /**
    * Convert a class's package name into a path
    * 
    * @param className the class name
    * @return the path
    */
   public static final String packageNameToPath(final String className)
   {
      String packageName = getClassPackageName(className);
      return packageName.replace('.', '/');
   }

   /**
    * Get the package name for a class
    * 
    * @param className the class name
    * @return the package name or the empty string if there is no package
    */
   public static final String getClassPackageName(final String className)
   {
      int end = className.lastIndexOf('.');
      if (end == -1)
         return "";
      return className.substring(0, end);
   }

   /**
    * Get the package name for a class
    * 
    * @param className the class name
    * @return the package name or the empty string if there is no package
    */
   public static final String getResourcePackageName(final String className)
   {
      int i = className.lastIndexOf('/');
      if (i == -1)
         return "";
      return className.substring(0, i).replace('/', '.');
   }

   /**
    * Get the resource name in dot notation
    * 
    * @param name the resource name
    * @return the resource name with / replaced by .
    */
   public static final String getResourceNameInDotNotation(final String name)
   {
      return name.replace('/', '.');
   }
   
   /**
    * Load bytecode from a stream
    * 
    * @param name the class name
    * @param is the input stream
    * @return the byte code
    */
   public static final byte[] loadByteCode(String name, final InputStream is)
   {
      try
      {
         ByteArrayOutputStream baos = new ByteArrayOutputStream();
         byte[] tmp = new byte[1024];
         int read = 0;
         while ( (read = is.read(tmp)) >= 0 )
            baos.write(tmp, 0, read);
         return baos.toByteArray();
      }
      catch (IOException e)
      {
         throw new RuntimeException("Unable to load class byte code " + name, e);
      }
      finally
      {
         try
         {
            is.close();
         }
         catch (IOException e)
         {
            // pointless
         }
      }
   }
   
   /**
    * Used to check whether the classloading request is from the jdk<p>
    * 
    * This is a hack because of broken behaviour by the JDKs where they assume
    * they can load their own classes from any classloader.
    * 
    * @return true when it is a JDK request
    */
   public static boolean isRequestFromJDK()
   {
      Class[] context = hack.getClassContext();
      for (Class clazz : context)
      {
         // Review others?
         if (Hack.class.isAssignableFrom(clazz) == false &&
             ClassLoaderUtils.class.isAssignableFrom(clazz) == false &&
             BaseClassLoaderDomain.class.isAssignableFrom(clazz) == false &&
             BaseClassLoader.class.isAssignableFrom(clazz) == false &&
             ClassLoader.class.isAssignableFrom(clazz) == false &&
             Class.class.isAssignableFrom(clazz) == false)
         {
            ClassLoader cl = clazz.getClassLoader();
            // Review: I don't think this true for all JDKs? i.e. JDK classes have no classloader.
            return (cl == null);
         }
      }
      throw new UnreachableStatementException();
   }
   
   /**
    * Formats the class as a string
    * 
    * @param clazz the class
    * @return the string
    */
   public static final String classToString(final Class<?> clazz)
   {
      if (clazz == null)
         return "null";

      StringBuilder builder = new StringBuilder();
      classToString(clazz, builder);
      return builder.toString();
   }
   
   /**
    * Formats a class into a string builder
    * 
    * @param clazz the class
    * @param builder the builder
    */
   public static final void classToString(final Class<?> clazz, StringBuilder builder)
   {
      if (clazz == null)
      {
         builder.append("null");
         return;
      }

      builder.append(clazz);
      builder.append('{');
      ClassLoader cl = clazz.getClassLoader();
      builder.append("cl=").append(cl);
      builder.append(" codeSource=");
      builder.append(getCodeSource(clazz));
      builder.append("}");
   }
   
   /**
    * Get the protected domain for a class
    * 
    * @param clazz the class
    * @return the protected domain or null if it doesn't have one
    */
   private static final ProtectionDomain getProtectionDomain(final Class<?> clazz)
   {
      SecurityManager sm = System.getSecurityManager();
      if (sm == null)
         return clazz.getProtectionDomain();
      
      return AccessController.doPrivileged(new PrivilegedAction<ProtectionDomain>()
      {
         public ProtectionDomain run()
         {
            return clazz.getProtectionDomain();
         }
      });
   }
   
   /**
    * Get the code source for a class
    * 
    * @param clazz the class
    * @return the code source or null if it doesn't have one
    */
   private static final CodeSource getCodeSource(final Class<?> clazz)
   {
      ProtectionDomain protectionDomain = getProtectionDomain(clazz);
      if (protectionDomain == null)
         return null;
      return protectionDomain.getCodeSource();
   }
   
   /**
    * Extend the security manager so we can get access to the stacktrace
    * 
    * TODO move this hack somewhere else?
    */
   private static class Hack extends SecurityManager
   {
      @Override
      public Class[] getClassContext()
      {
         return super.getClassContext();
      }
   }
}
