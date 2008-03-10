/*
* JBoss, Home of Professional Open Source
* Copyright 2008, JBoss Inc., and individual contributors as indicated
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
package org.jboss.classloading.spi.metadata;

import java.security.AccessController;

import org.jboss.classloading.plugins.metadata.DefaultClassLoadingMetaDataFactory;
import org.jboss.util.builder.AbstractBuilder;

/**
 * ClassLoadingMetaDataFactoryBuilder.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
class ClassLoadingMetaDataFactoryBuilder
{
   /** The default factory */
   private static String DEFAULT_FACTORY = DefaultClassLoadingMetaDataFactory.class.getName(); 
   
   /** The singleton */
   private static ClassLoadingMetaDataFactory singleton;

   /**
    * Get the instance
    * 
    * @return the instance
    */
   static synchronized ClassLoadingMetaDataFactory getInstance()
   {
      if (singleton == null)
      {
         AbstractBuilder<ClassLoadingMetaDataFactory> builder = new AbstractBuilder<ClassLoadingMetaDataFactory>(ClassLoadingMetaDataFactory.class, DEFAULT_FACTORY);
         singleton = AccessController.doPrivileged(builder);
      }
      return singleton;
   }
}
