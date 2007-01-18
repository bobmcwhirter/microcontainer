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
package org.jboss.kernel.plugins.config.xml;

import java.security.AccessController;
import java.security.PrivilegedExceptionAction;

import org.jboss.kernel.plugins.config.property.PropertyKernelConfig;
import org.jboss.kernel.spi.config.KernelConfig;
import org.jboss.reflect.plugins.introspection.IntrospectionTypeInfoFactory;
import org.jboss.reflect.spi.TypeInfoFactory;
import org.jboss.util.propertyeditor.PropertyEditors;

/**
 * Initialize the KernelConfig and TypeInfoFactory.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class KernelConfigInit
{
   /** The kernel config */
   static KernelConfig config;
   /** The type info factory */
   static final TypeInfoFactory typeInfoFactory = new IntrospectionTypeInfoFactory();

   static synchronized void init()
   {
      if( config == null )
      {
         try
         {
            config = AccessController.doPrivileged(new PrivilegedExceptionAction<KernelConfig>()
            {
               public KernelConfig run() throws Exception
               {
                  return new PropertyKernelConfig(System.getProperties());
               }
            });
         }
         catch (RuntimeException e)
         {
            throw e;
         }
         catch (Exception e)
         {
            throw new RuntimeException("Error getting configuration", e);
         }
         
         PropertyEditors.init();
      }
   }
}
