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

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.jboss.osgi.spi.metadata.AttributeMetaData;
import org.jboss.osgi.spi.metadata.ServiceMetaData;
import org.jboss.osgi.spi.metadata.ServiceMetaDataVisitor;

/**
 * OSGi service meta data.
 * Register ref bean as an OSGi service with interface or interfaces.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractServiceMetaData extends AbstractHolderMetaData implements ServiceMetaData
{
   private static final long serialVersionUID = 1l;

   private Set<String> interfaces;
   private Map<String, String> serviceProperties;
   private String ref;
   private boolean lazyInit;

   /** The references */
   private List<AttributeMetaData> attributes = Collections.emptyList();

   /** The dependencies */
   // todo

   public void visit(ServiceMetaDataVisitor visitor)
   {
      // todo - add children, depend on ref bean from MC
      super.visit(visitor);
   }

   public List<AttributeMetaData> getAttributes()
   {
      return attributes;
   }

   public Set<String> getInterfaces()
   {
      return interfaces;
   }

   public Map<String, String> getServiceProperties()
   {
      return serviceProperties;
   }

   public String getRef()
   {
      return ref;
   }

   public boolean getLazyInit()
   {
      return lazyInit;
   }

   public void setAttributes(List<AttributeMetaData> attributes)
   {
      this.attributes = attributes;
   }

   public void setInterfaces(Set<String> interfaces)
   {
      this.interfaces = interfaces;
   }

   public void setServiceProperties(Map<String, String> serviceProperties)
   {
      this.serviceProperties = serviceProperties;
   }

   public void setRef(String ref)
   {
      this.ref = ref;
   }

   public void setLazyInit(boolean lazyInit)
   {
      this.lazyInit = lazyInit;
   }

}
