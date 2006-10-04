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

import java.net.URL;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.virtual.VirtualFile;
import org.jboss.xb.binding.ObjectModelFactory;
import org.jboss.xb.binding.Unmarshaller;
import org.jboss.xb.binding.UnmarshallerFactory;

/**
 * SchemaResolverDeployer.
 * 
 * @param <T> the expected type 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class ObjectModelFactoryDeployer<T> extends AbstractParsingDeployer<T>
{
   /** Unmarshaller factory */
   private static final UnmarshallerFactory factory = UnmarshallerFactory.newInstance();
   
   /**
    * Create a new SchemaResolverDeployer.
    * 
    * @param deploymentType the deployment type
    * @throws IllegalArgumentException for a null deployment type
    */
   public ObjectModelFactoryDeployer(Class<T> deploymentType)
   {
      super(deploymentType);
   }

   /**
    * Parse a deployment
    * 
    * @param unit the deployment unit
    * @param file the metadata file
    * @param root - possibly null pre-existing root
    * @return the metadata
    * @throws Exception for any error
    */
   protected T parse(DeploymentUnit unit, VirtualFile file, T root) throws Exception
   {
      if (file == null)
         throw new IllegalArgumentException("Null file");

      Unmarshaller unmarshaller = factory.newUnmarshaller();
      Object parsed = null;
      try
      {
         ObjectModelFactory factory = getObjectModelFactory(root);
         URL url = file.toURL();
         parsed = unmarshaller.unmarshal(url.toString(), factory, root);
      }
      catch (Throwable t)
      {
         DeploymentException.rethrowAsDeploymentException("Error parsing meta data " + file.getPathName(), t);
      }
      if (parsed == null)
         throw new DeploymentException("The xml " + file.getPathName() + " is not well formed!");

      return getDeploymentType().cast(parsed);
   }

   /**
    * Get the object model factory 
    * 
    * @param root - possibly null pre-existing root
    * @return the object model factory
    */
   protected abstract ObjectModelFactory getObjectModelFactory(T root);
}
