/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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

import org.jboss.deployers.spi.deployer.DeploymentUnit;
import org.jboss.deployers.spi.structure.DeploymentContext;

/**
 * Search a DeploymentUnit structure from child to parent for a matching
 * attachment.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class AttachmentLocator
{
   /**
    * Get a named attachment
    * 
    * @param name the name of the attachment
    * @return the attachment or null if not present
    * @throws IllegalArgumentException for a null name
    */
   static public Object search(DeploymentUnit unit, String name)
   {
      Object attachment = null;
      DeploymentContext ctx = unit.getDeploymentContext();
      while( attachment == null && ctx != null )
      {
         attachment = ctx.getDeploymentUnit().getAttachment(name);
         ctx = ctx.getParent();
      }
      return attachment;
   }

   /**
    * Get named attachment of a given type
    * 
    * @param <T> the expected type
    * @param name the name of the attachment
    * @param expectedType the expected type
    * @return the attachment or null if not present
    * @throws IllegalArgumentException for a null name or expectedType
    */
   static public <T> T search(DeploymentUnit unit, String name, Class<T> expectedType)
   {
      Object result = search(unit, name);
      if (result == null)
         return null;
      return expectedType.cast(result);
   }

   /**
    * Get an attachment of the given type
    * 
    * @param <T> the expected type
    * @param type the type
    * @return the attachment or null if not present
    * @throws IllegalArgumentException for a null name or type
    */
   static public <T> T search(DeploymentUnit unit, Class<T> type)
   {
      return search(unit, type.getName(), type);
   }
}
