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

import org.jboss.deployers.plugins.deployer.AbstractSimpleDeployer;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.virtual.VirtualFile;

/**
 * AbstractParsingDeployer.
 *
 * @param <T> the expected type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractParsingDeployer<T> extends AbstractSimpleDeployer
{
   /** The expected type */
   private final Class<T> expectedType;
   
   /**
    * Create a new AbstractParsingDeployer.
    * 
    * @param expectedType the expected type
    * @throws IllegalArgumentException if the expected type is null
    */
   protected AbstractParsingDeployer(Class<T> expectedType)
   {
      if (expectedType == null)
         throw new IllegalArgumentException("Null expectedType");
      this.expectedType = expectedType;
   }

   public int getRelativeOrder()
   {
      return PARSER_DEPLOYER;
   }

   /**
    * Get the expected type
    * 
    * @return the expected type
    */
   protected Class<T> getExpectedType()
   {
      return expectedType;
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
      return unit.getAttachment(key, expectedType);
   }
   
   /**
    * Create some meta data
    * 
    * @param unit the deployment unit
    * @param name the name
    * @param suffix the suffix
    * @throws DeploymentException for any error
    */
   protected void createMetaData(DeploymentUnit unit, String name, String suffix) throws DeploymentException
   {
      createMetaData(unit, name, suffix, expectedType.getName());
   }
   
   /**
    * Create some meta data
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
      if (result != null)
         return;

      // Create it
      try
      {
         if (suffix == null)
            result = parse(unit, name);
         else
            result = parse(unit, name, suffix);
      }
      catch (Exception e)
      {
         throw DeploymentException.rethrowAsDeploymentException("Error creating managed object for " + unit.getName(), e);
      }
      
      // Doesn't exist
      if (result == null)
         return;
      
      // Register it
      unit.getTransientManagedObjects().addAttachment(key, result, expectedType);
   }

   /**
    * Parse an exact file name
    * 
    * @param unit the unit
    * @param name the exact name to match
    * @return the metadata or null if it doesn't exist
    * @throws Exception for any error
    */
   protected T parse(DeploymentUnit unit, String name) throws Exception
   {
      // Try to find the metadata
      VirtualFile file = unit.getMetaDataFile(name);
      if (file == null)
         return null;
      
      T result = parse(unit, file);
      init(unit, result, file);
      return result;
   }
   
   /**
    * Parse an exact file name
    * 
    * @param unit the unit
    * @param name the exact name to match
    * @param suffix the suffix to match
    * @return the metadata or null if it doesn't exist
    * @throws Exception for any error
    */
   protected T parse(DeploymentUnit unit, String name, String suffix) throws Exception
   {
      // Try to find the metadata
      List<VirtualFile> files = unit.getMetaDataFiles(name, suffix);
      if (files.size() == 0)
         return null;
      
      // TODO remove this limitation
      if (files.size() > 1)
         throw new DeploymentException("Only one file is allowed, found=" + files);

      VirtualFile file = files.get(0);
      
      T result = parse(unit, file);
      init(unit, result, file);
      return result;
   }
   
   /**
    * Parse a deployment
    * 
    * @param unit the deployment unit
    * @param file the metadata file
    * @return the metadata
    * @throws Exception for any error
    */
   protected abstract T parse(DeploymentUnit unit, VirtualFile file) throws Exception;
   
   /**
    * Initialise the metadata
    * 
    * @param unit the unit
    * @param metaData the metadata
    * @param file the metadata file
    * @throws Exception for any error
    */
   protected abstract void init(DeploymentUnit unit, T metaData, VirtualFile file) throws Exception;
}