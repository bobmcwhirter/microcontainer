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
package org.jboss.test.classloader.jmx.test;

import javax.management.MBeanInfo;
import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;
import javax.management.ObjectName;

import junit.framework.Test;

import org.jboss.classloader.spi.ClassLoaderSystem;
import org.jboss.classloader.spi.jmx.JMXClassLoader;
import org.jboss.classloader.test.support.MockClassLoaderPolicy;
import org.jboss.test.classloader.AbstractClassLoaderTest;
import org.jboss.test.classloader.jmx.support.Simple;

/**
 * JMXUnitTestCase
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class JMXUnitTestCase extends AbstractClassLoaderTest
{
   public static Test suite()
   {
      return suite(JMXUnitTestCase.class);
   }
   
   public JMXUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testJMXClassLoader() throws Exception
   {
      ClassLoaderSystem system = createClassLoaderSystemWithModifiedBootstrap();

      MockClassLoaderPolicy policy = createMockClassLoaderPolicy("simple");
      policy.setPathsAndPackageNames(Simple.class);
      JMXClassLoader cl = (JMXClassLoader) system.registerClassLoaderPolicy(policy);
      
      MBeanServer server = MBeanServerFactory.newMBeanServer();
      ObjectName clName = cl.getObjectName();
      server.registerMBean(cl, clName);
      getLog().debug("Registered classloader " + cl + " with name " + clName);
      
      ObjectName name = new ObjectName("test:test=simple");
      server.createMBean(Simple.class.getName(), name, clName);

      MBeanInfo info = server.getMBeanInfo(name);
      assertEquals(Simple.class.getName(), info.getClassName());
      
      Object actual = server.getAttribute(name, "ClassLoader");
      getLog().debug("Actual ClassLoader=" + actual + " expected " + cl);
      assertEquals(cl, actual);
   }
}
