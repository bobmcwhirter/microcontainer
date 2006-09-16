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

import java.io.InputStream;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.virtual.VirtualFile;
import org.jboss.xb.binding.Unmarshaller;
import org.jboss.xb.binding.UnmarshallerFactory;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBindingResolver;
import org.jboss.xb.binding.sunday.unmarshalling.SingletonSchemaResolverFactory;

/**
 * SchemaResolverDeployer.
 * 
 * @param <T> the expected type 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class SchemaResolverDeployer<T> extends AbstractParsingDeployer<T>
{
   /** Unmarshaller factory */
   private static final UnmarshallerFactory factory = UnmarshallerFactory.newInstance();

   /** The resolver */
   private static final SchemaBindingResolver resolver = SingletonSchemaResolverFactory.getInstance().getSchemaBindingResolver();
   
   /**
    * Create a new SchemaResolverDeployer.
    * 
    * @param deploymentType the deployment type
    * @throws IllegalArgumentException for a null deployment type
    */
   public SchemaResolverDeployer(Class<T> deploymentType)
   {
      super(deploymentType);
   }

   /**
    * Parse a deployment
    * 
    * @param unit the deployment unit
    * @param file the metadata file
    * @return the metadata
    * @throws Exception for any error
    */
   protected T parse(DeploymentUnit unit, VirtualFile file) throws Exception
   {
      if (file == null)
         throw new IllegalArgumentException("Null file");
      
      Unmarshaller unmarshaller = factory.newUnmarshaller();
      InputStream is = file.openStream();
      Object parsed = null;
      try
      {
         parsed = unmarshaller.unmarshal(is, resolver);
      }
      finally
      {
         try
         {
            is.close();
         }
         catch (Exception ignored)
         {
         }
      }
      if (parsed == null)
         throw new DeploymentException("The xml " + file.getPathName() + " is not well formed!");

      return getDeploymentType().cast(parsed);
   }
}
