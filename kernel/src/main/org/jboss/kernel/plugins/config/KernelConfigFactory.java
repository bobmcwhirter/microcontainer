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
package org.jboss.kernel.plugins.config;

import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.kernel.plugins.config.property.PropertyKernelConfig;

import java.util.Properties;

/**
 * Implementation of the Factory design pattern used in constructing new
 * instances of {@link KernelConfig KernelConfig} objects based on various
 * configuration sources.
 *
 * @author <a href="mailto:les.hazlewood@jboss.org">Les A. Hazlewood</a>
 * @version <tt>$Revision$</tt>
 */
public class KernelConfigFactory {

   /**
    * Constructs a new KernelConfig instance based on the System properties.
    * <p>If any necessary KernelConfig properties are not found in the System
    * properties, sensible default implementations for the missing
    * properties will be used.
    * @return a new <tt>KernelConfig</tt> instance based on the
    * System properties.
    * @see System#getProperties()
    * @see #newInstance(Properties)
    */
   public static KernelConfig newInstance() {
      //for the values not found in the System properties, default values
      //used are from the KernelConstants interface.  See
      //the PropertyKernelConfig class implementation for more details.
      return newInstance( System.getProperties() );
   }

   /**
    * Constructs a new <tt>KernelConfig</tt> instance based on the specified
    * <tt>Properties</tt>.
    * <p>If any necessary KernelConfig properties are not found in the given
    * properties, sensible default implementations for the missing
    * properties will be used.
    * @param props the properties to use when creating a new
    *              <tt>KernelConfig</tt> instance.
    * @return a new <tt>KernelConfig</tt> instance based on the specified
    *         <tt>Properties</tt>
    * @see PropertyKernelConfig
    */
   public static KernelConfig newInstance( Properties props ) {
      KernelConfig cfg = null;
      try
      {
         //for the values not found in the given props, default values
         //used are from the KernelConstants interface.  See
         //the PropertyKernelConfig class implementation for more details.
         cfg = new PropertyKernelConfig( props );
      }
      catch ( Throwable t )
      {
         String msg = "Unable to construct a " +
            PropertyKernelConfig.class.getName() +
            " instance based on the specified properties.";
         throw new RuntimeException( msg, t );
      }

      return cfg;
   }
}
