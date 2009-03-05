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
package org.jboss.system.deployers;

import org.jboss.deployers.vfs.spi.deployer.XSLDeployer;
import org.jboss.deployers.vfs.spi.structure.VFSDeploymentUnit;
import org.jboss.system.metadata.ServiceDeployment;
import org.jboss.system.metadata.ServiceDeploymentParser;
import org.jboss.virtual.VirtualFile;
import org.w3c.dom.Document;

public class ServiceXSLDeployer extends XSLDeployer<ServiceDeployment>
{
   /**
    * Create a new ServiceXSLDeployer.
    */
   public ServiceXSLDeployer()
   {
      super(ServiceDeployment.class);
   }

   protected boolean allowsReparse()
   {
      return true;
   }

   protected ServiceDeployment parse(VFSDeploymentUnit unit, VirtualFile file, Document document) throws Exception
   {
      ServiceDeploymentParser parser = new ServiceDeploymentParser(document);
      ServiceDeployment parsed = parser.parse();
      String name = file.toURI().toString();
      parsed.setName(name);
      return parsed;
   }
}
