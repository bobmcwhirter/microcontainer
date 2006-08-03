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
package org.jboss.kernel.plugins.bootstrap.basic;

import org.jboss.kernel.plugins.bootstrap.AbstractBootstrap;
import org.jboss.kernel.plugins.config.property.PropertyKernelConfig;
import org.jboss.kernel.spi.config.KernelConfig;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

/**
 * Bootstrap the kernel.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="mailto:les.hazlewood@jboss.org">Les A. Hazlewood</a>
 * @version $Revision$
 */
public class BasicBootstrap extends AbstractBootstrap
{
   /**
    * Bootstrap the kernel from the command line
    * 
    * @param args the command line arguments
    * @throws Exception for any error
    */
   public static void main(String[] args) throws Exception
   {
      BasicBootstrap bootstrap = new BasicBootstrap();
      bootstrap.run();
   }

   /**
    * Constructs a new Bootstrap which constructs a Kernel based on
    * the system properties.
    */
   public BasicBootstrap()
   {
      Properties props = getConfigProperties();
      if (props == null)
         props = getSystemProperties();
      final PropertyKernelConfig config = new PropertyKernelConfig(props);
      PrivilegedAction<?> action = new PrivilegedAction()
      {
         public Object run()
         {
            setConfig(config);
            return null;
         }
      };
      AccessController.doPrivileged(action);
   }

   /**
    * Constructs a new Bootstrap which constructs a Kernel based
    *  
    * @param config the configure
    * @throws Exception for any error
    */
   public BasicBootstrap(final KernelConfig config) throws Exception
   {
      PrivilegedAction<?> action = new PrivilegedAction()
      {
         public Object run()
         {
            setConfig(config);
            return null;
         }
      };
      AccessController.doPrivileged(action);
   }
   
   /**
    * Load bootstrap specific properties
    * 
    * @return the bootstrap specific properties
    */
   protected Properties getConfigProperties()
   {
      return null;
   }
   
   private Properties getSystemProperties()
   {
      if (System.getSecurityManager() == null)
         return System.getProperties();
      else
      {
         return AccessController.doPrivileged(GetSystemProperties.instance);
      }
   }
   
   private static class GetSystemProperties implements PrivilegedAction<Properties>
   {
      private static GetSystemProperties instance = new GetSystemProperties();
      
      public Properties run()
      {
         return System.getProperties();
      }
   }
}
