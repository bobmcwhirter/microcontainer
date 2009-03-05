/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.system.microcontainer;

import javax.management.MBeanServer;
import javax.management.ObjectName;

import org.jboss.system.ServiceCreator;
import org.jboss.system.ServiceInstance;
import org.jboss.system.metadata.ServiceMetaData;

/**
 * InstantiateAction.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class InstantiateAction extends ServiceControllerContextAction
{
   public void installAction(ServiceControllerContext context) throws Throwable
   {
      MBeanServer server = context.getServiceController().getMBeanServer();
      ObjectName objectName = context.getObjectName();
      ServiceMetaData metaData = context.getServiceMetaData();
      Object mbean = context.getTarget();
      ServiceInstance instance = ServiceCreator.install(server, objectName, metaData, mbean);
      context.setTarget(instance.getResource());
   }

   public void uninstallAction(ServiceControllerContext context)
   {
      MBeanServer server = context.getServiceController().getMBeanServer();
      ObjectName objectName = context.getObjectName();
      ServiceCreator.uninstall(server, objectName);
      ServiceMetaData metaData = context.getServiceMetaData();
      if (metaData != null)
         context.setTarget(null);
   }
}
