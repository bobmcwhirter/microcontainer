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

import java.util.List;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.virtual.VirtualFile;

/**
 * AbstractParsingDeployer. Extends AbstractTypedDeployer to add a notion of obtaining an instance of the
 * deploymentType by parsing a metadata file. Subclasses need to override
 * parse(DeploymentUnit, VirtualFile) to define this behavior. 
 *
 * @param <T> the expected type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractParsingDeployer<T> extends AbstractTypedDeployer<T>
{
   /**
    * Create a new AbstractParsingDeployer.
    * 
    * @param deploymentType the deployment type
    * @throws IllegalArgumentException if the deployment type is null
    */
   protected AbstractParsingDeployer(Class<T> deploymentType)
   {
      super(deploymentType);
   }

   public int getRelativeOrder()
   {
      return PARSER_DEPLOYER;
   }

   /**
    * A flag indicating whether createMetaData should execute a parse even if a non-null metadata value exists.
    * 
    * @return false if a parse should be performed only if there is no existing metadata value. True indicates
    * that parse should be done regardless of an existing metadata value.
    */
   protected boolean allowsReparse()
   {
      return false;
   }

   /**
    * Get some meta data
    * 
    * @param unit the deployment unit
    * @param key the key into the managed objects
    * @return the metadata or null if it doesn't exist
    */
   protected T getMetaData(DeploymentUnit unit, String key)
   {
      return unit.getAttachment(key, getDeploymentType());
   }
   
   /**
    * Create some meta data. Calls createMetaData(unit, name, suffix, getDeploymentType().getName()).
    * 
    * @param unit the deployment unit
    * @param name the name
    * @param suffix the suffix
    * @throws DeploymentException for any error
    */
   protected void createMetaData(DeploymentUnit unit, String name, String suffix) throws DeploymentException
   {
      createMetaData(unit, name, suffix, getDeploymentType().getName());
   }
   
   /**
    * Create some meta data. Invokes parse(unit, name, suffix) if there is not already a
    * metadata
    * 
    * @param unit the deployment unit
    * @param name the name
    * @param suffix the suffix
    * @param key the key into the managed objects
    * @throws DeploymentException for any error
    */
   protected void createMetaData(DeploymentUnit unit, String name, String suffix, String key) throws DeploymentException
   {
      // First see whether it already exists
      T result = getMetaData(unit, key);
      if (result != null && allowsReparse() == false)
         return;

      // Create it
      try
      {
         if (suffix == null)
            result = parse(unit, name, result);
         else
            result = parse(unit, name, suffix, result);
      }
      catch (Exception e)
      {
         throw DeploymentException.rethrowAsDeploymentException("Error creating managed object for " + unit.getName(), e);
      }
      
      // Doesn't exist
      if (result == null)
         return;
      
      // Register it
      unit.getTransientManagedObjects().addAttachment(key, result, getDeploymentType());
   }

   /**
    * Parse an exact file name
    * 
    * @param unit the unit
    * @param name the exact name to match
    * @param root - possibly null pre-existing root
    * @return the metadata or null if it doesn't exist
    * @throws Exception for any error
    */
   protected T parse(DeploymentUnit unit, String name, T root) throws Exception
   {
      // Try to find the metadata
      VirtualFile file = unit.getMetaDataFile(name);
      if (file == null)
         return null;
      
      T result = parse(unit, file, root);
      init(unit, result, file);
      return result;
   }
   
   /**
    * Parse an exact file name
    * 
    * @param unit the unit
    * @param name the exact name to match
    * @param suffix the suffix to match
    * @param root - possibly null pre-existing root
    * @return the metadata or null if it doesn't exist
    * @throws Exception for any error
    */
   protected T parse(DeploymentUnit unit, String name, String suffix, T root) throws Exception
   {
      // Try to find the metadata
      List<VirtualFile> files = unit.getMetaDataFiles(name, suffix);
      if (files.size() == 0)
         return null;
      
      // TODO remove this limitation
      if (files.size() > 1)
         throw new DeploymentException("Only one file is allowed, found=" + files);

      VirtualFile file = files.get(0);
      
      T result = parse(unit, file, root);
      init(unit, result, file);
      return result;
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
   protected abstract T parse(DeploymentUnit unit, VirtualFile file, T root) throws Exception;
   
   /**
    * Initialise the metadata
    * 
    * @param unit the unit
    * @param metaData the metadata
    * @param file the metadata file
    * @throws Exception for any error
    */
   protected void init(DeploymentUnit unit, T metaData, VirtualFile file) throws Exception
   {
   }
}