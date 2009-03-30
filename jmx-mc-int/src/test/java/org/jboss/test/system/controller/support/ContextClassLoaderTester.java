/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.system.controller.support;

import javax.management.Attribute;
import javax.management.AttributeList;
import javax.management.AttributeNotFoundException;
import javax.management.DynamicMBean;
import javax.management.InvalidAttributeValueException;
import javax.management.ListenerNotFoundException;
import javax.management.MBeanAttributeInfo;
import javax.management.MBeanConstructorInfo;
import javax.management.MBeanException;
import javax.management.MBeanInfo;
import javax.management.MBeanNotificationInfo;
import javax.management.MBeanOperationInfo;
import javax.management.MBeanRegistration;
import javax.management.MBeanServer;
import javax.management.NotificationBroadcasterSupport;
import javax.management.NotificationFilter;
import javax.management.NotificationListener;
import javax.management.ObjectName;
import javax.management.ReflectionException;

/**
 * ContextClassLoaderTester.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ContextClassLoaderTester extends NotificationBroadcasterSupport implements DynamicMBean, MBeanRegistration
{
   public static ClassLoader classLoader;
   

   public Object getAttribute(String attribute) throws AttributeNotFoundException, MBeanException, ReflectionException
   {
      checkClassLoader();
      return null;
   }

   public AttributeList getAttributes(String[] attributes)
   {
      checkClassLoader();
      return null;
   }

   public MBeanInfo getMBeanInfo()
   {
      checkClassLoader();
      MBeanAttributeInfo[] attributes =
      {
         new MBeanAttributeInfo("test", String.class.getName(), "test", true, true, false)
      };
      MBeanConstructorInfo[] constructors = new MBeanConstructorInfo[0];
      MBeanOperationInfo[] operations = new MBeanOperationInfo[0];
      MBeanNotificationInfo[] notifications = new MBeanNotificationInfo[0];
      return new MBeanInfo(getClass().getName(), "Test", attributes, constructors, operations, notifications);
   }

   public Object invoke(String actionName, Object[] params, String[] signature) throws MBeanException,
         ReflectionException
   {
      checkClassLoader();
      return null;
   }

   public void setAttribute(Attribute attribute) throws AttributeNotFoundException, InvalidAttributeValueException,
         MBeanException, ReflectionException
   {
      checkClassLoader();
   }

   public AttributeList setAttributes(AttributeList attributes)
   {
      checkClassLoader();
      return null;
   }

   public ObjectName preRegister(MBeanServer server, ObjectName name) throws Exception
   {
      checkClassLoader();
      return name;
   }

   public void postRegister(Boolean registrationDone)
   {
      checkClassLoader();
   }

   public void preDeregister() throws Exception
   {
      checkClassLoader();
   }
   
   public void postDeregister()
   {
      checkClassLoader();
   }

   public void addNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback)
   {
      checkClassLoader();
      super.addNotificationListener(listener, filter, handback);
   }

   public void removeNotificationListener(NotificationListener listener, NotificationFilter filter, Object handback)
         throws ListenerNotFoundException
   {
      checkClassLoader();
      super.removeNotificationListener(listener, filter, handback);
   }

   public void removeNotificationListener(NotificationListener listener) throws ListenerNotFoundException
   {
      checkClassLoader();
      super.removeNotificationListener(listener);
   }

   protected void checkClassLoader()
   {
      ClassLoader expected = classLoader;
      ClassLoader actual = Thread.currentThread().getContextClassLoader();
      if (expected != actual)
         throw new Error("Expected tcl: " + expected + " actual: " + actual);
   }
}
