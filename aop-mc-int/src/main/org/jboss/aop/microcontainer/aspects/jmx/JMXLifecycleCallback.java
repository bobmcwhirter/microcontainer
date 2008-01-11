/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.aop.microcontainer.aspects.jmx;

import javax.management.MBeanServer;
import javax.management.ObjectName;
import javax.management.StandardMBean;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.logging.Logger;
import org.jboss.metadata.spi.MetaData;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class JMXLifecycleCallback
{
   private static final Logger log = Logger.getLogger(JMXLifecycleCallback.class);
   private MBeanServer server;
   
   public void setMbeanServer(MBeanServer server)
   {
      this.server = server;
   }

   @SuppressWarnings("unchecked")
   public void install(ControllerContext context) throws Exception
   {
      validateServer();
      JMX jmx = readJmxAnnotation(context);
      ObjectName objectName = createObjectName(context, jmx); 

      Class intfClass = null;
      boolean registerDirectly = false;
      if (jmx != null)
      {
         intfClass = jmx.exposedInterface();
         registerDirectly = jmx.registerDirectly();
      }
      Object mbean = (registerDirectly ? context.getTarget() : new StandardMBean(context.getTarget(), intfClass));
      server.registerMBean(mbean, objectName);
      log.debug("Registered MBean " + objectName);
   }
   
   public void uninstall(ControllerContext context) throws Exception
   {
      validateServer();
      JMX jmx = readJmxAnnotation(context);
      ObjectName objectName = createObjectName(context, jmx); 

      log.debug("Unregistering MBean " + objectName);
      server.unregisterMBean(objectName);
   }
   
   private void validateServer()
   {
      if (server == null)
      {
         throw new RuntimeException("No MBeanServer was injected");
      }
   }
   
   private JMX readJmxAnnotation(ControllerContext context) throws Exception
   {
      MetaData metaData = context.getScopeInfo().getMetaData();
      if (metaData != null)
         return metaData.getAnnotation(JMX.class);
      return null;
   }
   
   private ObjectName createObjectName(ControllerContext context, JMX jmx) throws Exception
   {
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
      
      return objectName;
   }
}
