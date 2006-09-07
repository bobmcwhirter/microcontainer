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
package org.jboss.deployers.plugins.deployer;

import java.io.InputStream;
import java.net.URL;

import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.deployers.spi.structure.DeploymentContext;

/**
 * AbstractDeploymentUnit.<p>
 * 
 * This is just a wrapper to the deployment context that
 * restricts people from "poking" behind the scenes.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AbstractDeploymentUnit implements DeploymentUnit
{
   /** The deployment context */
   private DeploymentContext deploymentContext;

   /**
    * Create a new AbstractDeploymentUnit.
    * 
    * @param deploymentContext the deployment context
    */
   public AbstractDeploymentUnit(DeploymentContext deploymentContext)
   {
      if (deploymentContext == null)
         throw new IllegalArgumentException("Null deployment context");
      this.deploymentContext = deploymentContext;
   }
   
   public String getName()
   {
      return deploymentContext.getName();
   }
   
   public ClassLoader getClassLoader()
   {
      ClassLoader cl = deploymentContext.getClassLoader();
      if (cl == null)
         throw new IllegalStateException("ClassLoader has not been set");
      return cl;
   }

   public URL getMetaData(String name)
   {
      return deploymentContext.getMetaData(name);
   }

   public InputStream getMetaDataAsStream(String name)
   {
      return deploymentContext.getMetaDataAsStream(name);
   }
}
