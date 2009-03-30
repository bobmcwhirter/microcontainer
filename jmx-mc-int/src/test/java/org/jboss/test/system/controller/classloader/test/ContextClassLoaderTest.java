/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2009, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.system.controller.classloader.test;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.MBeanServer;
import javax.management.Notification;
import javax.management.NotificationListener;
import javax.management.ObjectName;

import org.jboss.test.system.controller.AbstractControllerTest;
import org.jboss.test.system.controller.support.ContextClassLoaderMBean;
import org.jboss.test.system.controller.support.ContextClassLoaderTester;

/**
 * ContextClassLoaderTest.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class ContextClassLoaderTest extends AbstractControllerTest implements NotificationListener
{
   public ContextClassLoaderTest(String name)
   {
      super(name);
   }
   
   public void testContextClassLoader() throws Exception
   {
      ContextClassLoaderTester.classLoader = getMBean(ClassLoader.class, ContextClassLoaderMBean.OBJECT_NAME, "Instance");
      
      ObjectName test = new ObjectName("jboss.test:type=test");
      
      MBeanServer server = getServer();
      server.createMBean(ContextClassLoaderTester.class.getName(), test, ContextClassLoaderMBean.OBJECT_NAME);
      server.getAttribute(test, "test");
      server.setAttribute(test, new Attribute("test", "hello"));
      server.getAttributes(test, new String[] { "test" });
      AttributeList attributes = new AttributeList();
      attributes.add(new Attribute("test", "hello"));
      server.setAttributes(test, attributes);
      server.invoke(test, "hello", null, null);
      
      server.addNotificationListener(test, this, null, null);
      server.removeNotificationListener(test, this);
      
      assertEquals(ContextClassLoaderTester.classLoader, server.getClassLoaderFor(test));
   }

   public void handleNotification(Notification notification, Object handback)
   {
   }
}
