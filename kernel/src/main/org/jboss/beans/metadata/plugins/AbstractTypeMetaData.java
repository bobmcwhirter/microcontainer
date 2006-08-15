/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.beans.metadata.plugins;

import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.util.JBossStringBuilder;

/**
 * A typed value.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public abstract class AbstractTypeMetaData extends AbstractValueMetaData
{
   /** The type */
   protected String type;
   
   /** The configurator */
   protected KernelConfigurator configurator;
   /**
    * Create a new typed value
    */
   public AbstractTypeMetaData()
   {
   }

   /**
    * Create a new typed value
    * 
    * @param value the value
    */
   public AbstractTypeMetaData(String value)
   {
      super(value);
   }

   /**
    * Set the type
    * 
    * @param type the type
    */
   public void setType(String type)
   {
      this.type = type;
   }

   public String getType()
   {
      return type;
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      configurator = visitor.getControllerContext().getKernel().getConfigurator();
      visitor.initialVisit(this);
   }

   /**
    * Set the configurator
    * 
    * @param configurator the configurator
    */
   public void setConfigurator(KernelConfigurator configurator)
   {
      this.configurator = configurator;
   }
   
   public void toString(JBossStringBuilder buffer)
   {
      super.toString(buffer);
      if (type != null)
         buffer.append(" type=").append(type);
   }
   
   /**
    * Get the class info for this type
    * 
    * @param cl classloader
    * @return the class info
    * @throws Throwable for any error
    */
   protected ClassInfo getClassInfo(ClassLoader cl) throws Throwable
   {
      if (type == null)
         return null;
      
      return configurator.getClassInfo(type, cl);
   }
}
