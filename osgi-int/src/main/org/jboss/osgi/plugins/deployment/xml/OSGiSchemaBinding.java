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

import javax.xml.namespace.QName;

import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.binding.sunday.unmarshalling.TypeBinding;

/**
 * OSGi schema.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class OSGiSchemaBinding
{
   /**
    * The namespace
    */
   public static final String OSGI_DEPLOYER_NS = "urn:jboss:osgi-beans:1.0";

   /**
    * The service binding
    */
   public static final QName serviceTypeQName = new QName(OSGI_DEPLOYER_NS, "serviceType");

   /**
    * The service element name
    */
   public static final QName serviceQName = new QName(OSGI_DEPLOYER_NS, "service");

   /**
    * The reference binding
    */
   public static final QName referenceTypeQName = new QName(OSGI_DEPLOYER_NS, "referenceType");

   /**
    * The reference element name
    */
   public static final QName referenceQName = new QName(OSGI_DEPLOYER_NS, "reference");

   /**
    * The listener binding
    */
   public static final QName listenerTypeQName = new QName(OSGI_DEPLOYER_NS, "listenerType");

   /**
    * The listener element name
    */
   public static final QName listenerQName = new QName(OSGI_DEPLOYER_NS, "listener");

   public static void init(SchemaBinding schemaBinding)
   {
      // service binding
      TypeBinding serviceType = schemaBinding.getType(serviceTypeQName);
      OSGiSchemaBindingHelper.initServiceHandler(serviceType);

      // reference binding
      TypeBinding referenceType = schemaBinding.getType(referenceTypeQName);
      OSGiSchemaBindingHelper.initReferenceHandler(referenceType);

      // listener binding
      TypeBinding listenerType = schemaBinding.getType(listenerTypeQName);
      OSGiSchemaBindingHelper.initListenerHandler(listenerType);

   }

}
