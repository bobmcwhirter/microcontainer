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
package org.jboss.reliance.identity;

import java.util.HashMap;
import javax.security.auth.login.AppConfigurationEntry;

/**
 * Factory for the JAAS Configuration used by Microcontainer Security.
 *
 * @author Shane Bryzak
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class Configuration
{
   static final String DEFAULT_JAAS_CONFIG_NAME = "default";

   private String loginModuleClassName = MicrocontainerLoginModule.class.getName();
   private javax.security.auth.login.Configuration configuration;

   public void create()
   {
      configuration = createConfiguration();
   }

   /**
    * Create configuration.
    *
    * @return the configuration
    */
   protected javax.security.auth.login.Configuration createConfiguration()
   {
      return new javax.security.auth.login.Configuration()
      {
         private AppConfigurationEntry[] aces = {createAppConfigurationEntry()};

         @Override
         public AppConfigurationEntry[] getAppConfigurationEntry(String name)
         {
            return DEFAULT_JAAS_CONFIG_NAME.equals(name) ? aces : null;
         }

         @Override
         public void refresh()
         {
         }
      };
   }

   /**
    * Create AppConfigurationEntry.
    *
    * @return app. configuration entry
    */
   protected AppConfigurationEntry createAppConfigurationEntry()
   {
      return new AppConfigurationEntry(
            loginModuleClassName,
            AppConfigurationEntry.LoginModuleControlFlag.REQUIRED,
            new HashMap<String, String>()
      );
   }

   /**
    * Get the configuration.
    *
    * @return the configuration
    */
   public javax.security.auth.login.Configuration getConfiguration()
   {
      return configuration;
   }

   /**
    * Set the login module class name.
    *
    * @param loginModuleClassName class name
    */
   public void setLoginModuleClassName(String loginModuleClassName)
   {
      this.loginModuleClassName = loginModuleClassName;
   }
}
