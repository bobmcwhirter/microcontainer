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
package org.jboss.deployers.plugins.deployers.helpers;

import org.jboss.deployers.plugins.deployer.AbstractSimpleDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.classloader.ClassLoaderFactory;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.deployers.spi.structure.DeploymentContext;

/**
 * AbstractClassLoaderDeployer.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractClassLoaderDeployer extends AbstractSimpleDeployer implements ClassLoaderFactory
{
   /**
    * Set the default relative order to CLASSLOADER_DEPLOYER
    *
    */
   public AbstractClassLoaderDeployer()
   {
      super.setRelativeOrder(CLASSLOADER_DEPLOYER);
   }

   /**
    * Look for an attachment of type ClassLoaderFactory, and if its not
    * found, use this as the factory.
    */
   public void deploy(DeploymentUnit unit) throws DeploymentException
   {
      ClassLoaderFactory factory = unit.getAttachment(ClassLoaderFactory.class);
      if( factory == null )
         factory = this;
      unit.createClassLoader(factory);
   }

   public void undeploy(DeploymentUnit unit)
   {
   }

   public void removeClassLoader(DeploymentContext context) throws Exception
   {
   }
}
