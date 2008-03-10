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
package org.jboss.test.classloader.policy.support;

import java.io.FilePermission;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.CodeSource;
import java.security.PermissionCollection;
import java.security.Permissions;
import java.security.ProtectionDomain;
import java.security.cert.Certificate;
import java.util.List;
import java.util.Set;

import javax.naming.Context;

import org.jboss.classloader.plugins.ClassLoaderUtils;
import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.DelegateLoader;
import org.jboss.classloader.spi.PackageInformation;

/**
 * TestClassLoaderPolicy.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class TestClassLoaderPolicy extends ClassLoaderPolicy
{
   public static String testClassPath = ClassLoaderUtils.classNameToPath(TestClass.class.getName());
   public static URL sealBase;
   public static URL codeSourceURL;
   public String getResourceInvoked;
   public String getResourceAsStreamInvoked;
   public String getResourcesInvoked;

   public List<? extends DelegateLoader> delegates;
   public String[] packageNames;
   public boolean isImportAll = false;
   public DelegateLoader exported;
   
   static
   {
      try
      {
         sealBase = new URL("http://localhost:9090");
         codeSourceURL = new URL("http://localhost:8080");
      }
      catch (MalformedURLException e)
      {
         throw new Error("Unexpected", e);
      }
   }
   
   @Override
   protected List<? extends DelegateLoader> getDelegates()
   {
      return delegates;
   }
   
   public void setDelegates(List<? extends DelegateLoader> delegates)
   {
      this.delegates = delegates;
   }

   @Override
   public String[] getPackageNames()
   {
      return packageNames;
   }

   @Override
   public DelegateLoader getExported()
   {
      if (exported != null)
         return exported;
      return super.getExported();
   }

   @Override
   protected boolean isImportAll()
   {
      return isImportAll;
   }

   @Override
   public PackageInformation getPackageInformation(String packageName)
   {
      PackageInformation pi = new PackageInformation(ClassLoaderUtils.getClassPackageName(TestClass.class.getName()));
      pi.specTitle = "SpecTitle";
      pi.specVendor = "SpecVendor";
      pi.specVersion = "SpecVersion";
      pi.implTitle = "ImplTitle";
      pi.implVendor = "ImplVendor";
      pi.implVersion = "ImplVersion";
      pi.sealBase = sealBase;
      return pi;
   }

   @Override
   protected ProtectionDomain getProtectionDomain(String className, String path)
   {
      CodeSource codeSource = new CodeSource(codeSourceURL, (Certificate[]) null);
      PermissionCollection permissions = new Permissions();
      permissions.add(new FilePermission("<<ALL FILES>>", "read"));
      return new ProtectionDomain(codeSource, permissions);
   }

   @Override
   public URL getResource(String path)
   {
      getResourceInvoked = path;
      if (testClassPath.equals(path))
         return getClass().getClassLoader().getResource(path);
      return null;
   }

   @Override
   public InputStream getResourceAsStream(String path)
   {
      getResourceAsStreamInvoked = path;
      return super.getResourceAsStream(path);
   }

   @Override
   public void getResources(String name, Set<URL> urls) throws IOException
   {
      getResourcesInvoked = name;
   }

   @Override
   protected ClassLoader isJDKRequest(String name)
   {
      if (Context.class.getName().equals(name))
         return getClass().getClassLoader();
      return super.isJDKRequest(name);
   }
}
