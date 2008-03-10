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
package org.jboss.osgi.plugins.deployment.xml;

import org.jboss.kernel.plugins.deployment.xml.BeanPropertyInterceptor;
import org.jboss.kernel.plugins.deployment.xml.BeanSchemaBinding20;
import org.jboss.kernel.plugins.deployment.xml.ValueMetaDataElementInterceptor;
import org.jboss.xb.binding.sunday.unmarshalling.TypeBinding;

/**
 * OSGi schema binding.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class OSGiSchemaBindingHelper
{

   public static void initServiceHandler(TypeBinding serviceType)
   {
      serviceType.setHandler(ServiceHandler.HANDLER);
      // handle interfaces
      serviceType.pushInterceptor(BeanSchemaBinding20.setQName, ValueMetaDataElementInterceptor.VALUES);
      // handle service-properties
      serviceType.pushInterceptor(BeanSchemaBinding20.mapQName, ValueMetaDataElementInterceptor.VALUES);
   }

   public static void initReferenceHandler(TypeBinding referenceType)
   {
      referenceType.setHandler(ReferenceHandler.HANDLER);
      // handle properties
      referenceType.pushInterceptor(BeanSchemaBinding20.propertyQName, BeanPropertyInterceptor.INTERCEPTOR);
      // handle listener
      referenceType.pushInterceptor(OSGiSchemaBinding.listenerQName, ListenerInterceptor.INTERCEPTOR);
   }

   public static void initListenerHandler(TypeBinding listenerType)
   {
      listenerType.setHandler(ListenerHandler.HANDLER);
   }

}
