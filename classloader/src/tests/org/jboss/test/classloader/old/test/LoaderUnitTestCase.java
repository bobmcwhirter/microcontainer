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
package org.jboss.test.classloader.old.test;

import junit.framework.Test;

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.test.classloader.AbstractClassLoaderTestWithSecurity;
import org.jboss.test.classloader.old.support.Support;

/**
 * LoaderUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public class LoaderUnitTestCase extends AbstractClassLoaderTestWithSecurity
{
   public static Test suite()
   {
      return suite(LoaderUnitTestCase.class);
   }

   public LoaderUnitTestCase(String name)
   {
      super(name);
   }

   public void testInterruptThread() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPaths(Support.class);
      final ClassLoader cl = system.registerClassLoaderPolicy(policy);

      // Set our interrupted flag
      getLog().debug("Setting interrupt flag");
      Thread.currentThread().interrupt();
      try
      {
         assertLoadClass(Support.class, cl);
      }
      finally
      {
         assertTrue("Interrupted state not restored", Thread.currentThread().interrupted());
      }
   }
   
   public void testLoadingArrayClass() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy policy = createMockClassLoaderPolicy();
      policy.setPaths(Support.class);
      ClassLoader cl = system.registerClassLoaderPolicy(policy);

      assertLoadClass("[Ljava.lang.String;", cl, null);
      assertLoadClass("[L" + Support.class.getName() + ";", cl);
      assertLoadClass("[[L" + Support.class.getName() + ";", cl);
   }
}
