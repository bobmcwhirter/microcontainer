/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.osgi.plugins.metadata;

import java.util.List;

import org.jboss.osgi.spi.metadata.ServiceDeployment;
import org.jboss.osgi.spi.metadata.ServiceMetaData;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * An abstract service deployment
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractServiceDeployment extends JBossObject implements ServiceDeployment
{
   /** The name of the deployment */
   protected String name;

   /* The services list */
   protected List<ServiceMetaData> services;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
      flushJBossObjectCache();
   }

   public List<ServiceMetaData> getServices()
   {
      return services;
   }

   public void setServices(List<ServiceMetaData> services)
   {
      this.services = services;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("name=").append(name);
      if (services != null)
         buffer.append(" services=").append(services);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(name);
   }

}
