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
package org.jboss.test.classloader.system.support;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.classloader.spi.ClassLoaderDomain;
import org.jboss.classloader.spi.ClassLoaderPolicy;

/**
 * MockClassLoaderDomain.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockClassLoaderDomain extends ClassLoaderDomain
{
   public List<ClassLoader> added = new CopyOnWriteArrayList<ClassLoader>();
   public List<ClassLoader> removed = new CopyOnWriteArrayList<ClassLoader>();
   public boolean shutdown = false;
   
   public MockClassLoaderDomain()
   {
      super("mock");
   }
   
   public MockClassLoaderDomain(String name)
   {
      super(name);
   }
   
   protected void afterRegisterClassLoader(ClassLoader classLoader, ClassLoaderPolicy policy)
   {
      added.add(classLoader);
   }

   protected void afterUnregisterClassLoader(ClassLoader classLoader, ClassLoaderPolicy policy)
   {
      removed.add(classLoader);
   }

   protected void shutdownDomain()
   {
      shutdown = true;
      super.shutdownDomain();
   }
}
