/*
  * JBoss, Home of Professional Open Source
  * Copyright 2005, JBoss Inc., and individual contributors as indicated
  * by the @authors tag. See the copyright.txt in the distribution for a
  * full listing of individual contributors.
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
package org.jboss.test.microcontainer.test;

import javax.management.MBeanAttributeInfo;
import javax.management.MBeanInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanServer;
import javax.management.ObjectName;

import junit.framework.Test;

import org.jboss.aop.microcontainer.aspects.jmx.JMX;
import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.JmxRegistrant;
import org.jboss.test.microcontainer.support.MetaDataContextInterceptor;
import org.jboss.test.microcontainer.support.SimpleBean;
import org.jboss.test.microcontainer.support.SimpleBeanImplMBean;

public class JMXLifecycleTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(JMXLifecycleTestCase.class);
   }
   
   public JMXLifecycleTestCase(String name)
   {
      super(name);
   }
   
   public void testJMX() throws Exception
   {
      MBeanServer server = (MBeanServer) getBean("MBeanServer");
      assertNotNull(server);

      ObjectName name = new ObjectName("test:name='Bean'");
      MBeanInfo info = server.getMBeanInfo(name);
      assertNotNull(info);
      MBeanOperationInfo[] ops = info.getOperations();
      assertEquals(1, ops.length);
      assertEquals("someMethod", ops[0].getName());
      
      name = new ObjectName("test:name=NotBean1");
      info = server.getMBeanInfo(name);
      assertNotNull(info);
      ops = info.getOperations();
      assertEquals(1, ops.length);
      assertEquals("someOtherMethod", ops[0].getName());
      
      name = new ObjectName("test:name='AnnotatedBean'");
      info = server.getMBeanInfo(name);
      assertNotNull(info);
      ops = info.getOperations();
      assertEquals(1, ops.length);
      assertEquals("someOtherMethod", ops[0].getName());
      
      name = new ObjectName("test:name=AnnotatedNotBean");
      info = server.getMBeanInfo(name);
      assertNotNull(info);
      ops = info.getOperations();
      assertEquals(1, ops.length);
      assertEquals("someOtherMethod", ops[0].getName());
   }
   
   /**
    * Tests that adding the registerDirectly=true attribute allows a
    * standard MBean to be registered directly, while not specifying it
    * causes a proxy to be registered (even if the bean is a standard MBean).
    * Also confirms a bean that isn't an MBean will fail to deploy if
    * registerDirectly=true.
    * 
    * @throws Exception
    */
   public void testRegisterDirectly() throws Exception
   {
      MBeanServer server = (MBeanServer) getBean("MBeanServer");
      assertNotNull(server);

      ObjectName name = new ObjectName("test:name=DirectRegistrant");
      MBeanInfo info = server.getMBeanInfo(name);
      assertNotNull(info);
      MBeanAttributeInfo[] attrs = info.getAttributes();
      assertEquals(1, attrs.length);
      assertEquals("RegisteredInJmx", attrs[0].getName());
      assertTrue(attrs[0].isReadable());
      assertFalse(attrs[0].isWritable());
      
      JmxRegistrant bean = (JmxRegistrant) getBean("DirectRegistrant");
      assertTrue("MBeanRegistration callbacks invoked", bean.isRegisteredInJmx());
      
      name = new ObjectName("test:name=NotDirectRegistrant");
      info = server.getMBeanInfo(name);
      assertNotNull(info);
      attrs = info.getAttributes();
      assertEquals(1, attrs.length);
      assertEquals("RegisteredInJmx", attrs[0].getName());
      assertTrue(attrs[0].isReadable());
      assertFalse(attrs[0].isWritable());
      
      bean = (JmxRegistrant) getBean("NotDirectRegistrant");
      assertFalse("MBeanRegistration callbacks not invoked", bean.isRegisteredInJmx());
      
      // Deploy a bean that should fail as it's not an mbean
      
      deploy("JMXDecorated-flawed.xml");
      try
      {      
         name = new ObjectName("test:name=FlawedRegistrant");
         assertFalse(name + " not registered", server.isRegistered(name));
         
         Object broken = null;
         try
         {
            broken = getBean("FlawedRegistrant");
         }
         catch (Exception good) {}         
   
         assertNull("FlawedRegistrant did not deploy", broken);
      }
      finally
      {
         undeploy("JMXDecorated-flawed.xml");
      }
   }
}
