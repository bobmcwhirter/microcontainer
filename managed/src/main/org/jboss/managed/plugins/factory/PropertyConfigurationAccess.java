/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.managed.plugins.factory;

import java.security.AccessController;
import java.security.PrivilegedAction;

import org.jboss.config.plugins.property.PropertyConfiguration;
import org.jboss.config.spi.Configuration;

/**
 * A package protected PrivilegedAction<Configuration> impl
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
class PropertyConfigurationAccess
   implements PrivilegedAction<Configuration>
{
   public Configuration run()
   {
      return new PropertyConfiguration();
   }
   static Configuration getConfiguration()
   {
      SecurityManager secMgr = System.getSecurityManager();
      Configuration config = null;
      if(secMgr != null)
      {
         PropertyConfigurationAccess pca = new PropertyConfigurationAccess();
         config = AccessController.doPrivileged(pca);
      }
      else
      {
         config = new PropertyConfiguration();
      }
      return config;
   }
}
