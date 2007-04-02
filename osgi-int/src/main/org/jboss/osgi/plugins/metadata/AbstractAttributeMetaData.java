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

import java.io.Serializable;

import org.jboss.deployers.spi.DeploymentException;
import org.jboss.osgi.spi.metadata.AttributeMetaData;
import org.jboss.osgi.spi.metadata.ServiceValueMetaData;
import org.jboss.util.UnreachableStatementException;
import org.osgi.framework.BundleContext;

/**
 * ServiceAttributeMetaData.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractAttributeMetaData extends AbstractMetaDataVisitorNode implements AttributeMetaData, Serializable
{
   private static final long serialVersionUID = 1L;

   /** The attribute name */
   private String name;

   /** The value */
   private ServiceValueMetaData value;

   public String getName()
   {
      return name;
   }

   /**
    * Set the name.
    *
    * @param name the name.
    */
   public void setName(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      this.name = name;
   }

   public ServiceValueMetaData getValue()
   {
      return value;
   }

   /**
    * Set the value.
    *
    * @param value the value.
    */
   public void setValue(ServiceValueMetaData value)
   {
      if (value == null)
         throw new IllegalArgumentException("Null value");
      this.value = value;
   }

   /**
    * Get the value
    *
    * @param bundleContext the bundle context
    * @return the value
    * @throws Exception for any error
    */
   public Object getValue(BundleContext bundleContext) throws Exception
   {
      try
      {
         return value.getValue(bundleContext);
      }
      catch (Throwable t)
      {
         DeploymentException.rethrowAsDeploymentException("Error configuring attribute " + name, t);
         throw new UnreachableStatementException();
      }
   }
}
