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
package org.jboss.aop.microcontainer.aspects.jmx;

import javax.management.MBeanServer;
import javax.management.NotCompliantMBeanException;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import org.jboss.aop.advice.Interceptor;
import org.jboss.aop.joinpoint.Invocation;
import org.jboss.aop.joinpoint.MethodInvocation;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.logging.Logger;

/**
 * A JMXIntroduction.
 * 
 * @TODO This is a very stupid implementation. It should be based on
 * annotations but they are not there yet, either in the integration
 * or even the basic xml parsing. 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 * @deprecated Should use JMXLifecycleCallback instead
 */
public class JMXIntroduction implements Interceptor
{
   private static final Logger log = Logger.getLogger(JMXIntroduction.class);
   private MBeanServer server;
   
   public String getName()
   {
      return getClass().getName();
   }
   
   public void setMbeanServer(MBeanServer server)
   {
      this.server = server;
   }
 
   public Object invoke(Invocation invocation) throws Throwable
   {
      MethodInvocation mi = (MethodInvocation) invocation;
      KernelControllerContext context = (KernelControllerContext) mi.getArguments()[0];

      JMX jmx = (JMX)invocation.resolveClassAnnotation(JMX.class);
      
      ObjectName objectName = null;
      if (jmx != null)
      {
         String jmxName = jmx.name();
         if (jmxName != null && jmxName.length() > 0)
            objectName = new ObjectName(jmxName);
      }
      
      if (objectName == null)
      {
         // try to build one from the bean name
         String name = (String) context.getName();
         
         if (name.contains(":"))
         {
            objectName = new ObjectName(name);
         }
         else
         {
            objectName = new ObjectName("test:name='" + name + "'");            
         }
      }
      
      if (server == null)
      {
         throw new RuntimeException("No MBeanServer was injected");
      }
      
      if ("setKernelControllerContext".equals(mi.getMethod().getName()))
      {
         Class intfClass = null;
         boolean registerDirectly = false;
         if (jmx != null)
         {
            intfClass = jmx.exposedInterface();
            registerDirectly = jmx.registerDirectly();
         }
         Object mbean = (registerDirectly ? context.getTarget() 
                                          : new StandardMBean(context.getTarget(), intfClass));
         server.registerMBean(mbean, objectName);
         log.info("Registered MBean " + objectName);
      }
      else
      {
         log.info("Unregistering MBean " + objectName);
         server.unregisterMBean(objectName);
      }

      return null;
   }
}
