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

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.jboss.deployers.plugins.deployer.AbstractSimpleDeployer;
import org.jboss.deployers.spi.deployer.DeploymentUnit;

/**
 * AbstractRealDeployer.
 * 
 * @param <T> the expected type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AbstractRealDeployer<T> extends AbstractSimpleDeployer
{
   /** The expected type */
   private Class<T> expectedType;

   /**
    * Create a new AbstractRealDeployer.
    * 
    * @param expectedType the expected type
    * @throws IllegalArgumentException if the expected type is null
    */
   public AbstractRealDeployer(Class<T> expectedType)
   {
      if (expectedType == null)
         throw new IllegalArgumentException("Null expectedType");
      this.expectedType = expectedType;
   }
   
   /**
    * Get all the metadata for the expected type
    * 
    * @param unit the unit
    * @return a set of metadata matching the type
    */
   @SuppressWarnings("unchecked")
   protected Set<T> getAllMetaData(DeploymentUnit unit)
   {
      if (unit == null)
         throw new IllegalArgumentException("Null unit");
      
      Set<T> result = new HashSet<T>();
      Map<String, Object> attachments = unit.getAttachments();
      for (Object object : attachments.values())
      {
         if (expectedType.isInstance(object))
            result.add((T) object);
      }
      return result;
   }
}
