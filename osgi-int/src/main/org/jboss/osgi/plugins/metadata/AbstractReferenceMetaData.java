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

import java.util.List;
import java.util.Set;

import org.jboss.aop.proxy.container.GeneratedAOPProxyFactory;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.osgi.spi.metadata.ListenerMetaData;
import org.jboss.osgi.spi.metadata.ReferenceMetaData;
import org.jboss.osgi.spi.metadata.ServiceMetaDataVisitor;
import org.osgi.framework.BundleContext;

/**
 * OSGi reference meta data.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractReferenceMetaData extends AbstractHolderMetaData implements ReferenceMetaData
{
   private static final long serialVersionUID = 1l;

   private String filter;
   private Cardinality cardinality = Cardinality.ONE_TO_ONE;
   private Integer timeout;
   private List<ListenerMetaData> listeners;
   private Set<PropertyMetaData> properties;

   public Object getUnderlyingValue()
   {
      return getId();
   }

   public Object getValue(BundleContext bundleContext) throws Throwable
   {
      // todo - add dynamic behaviour
      return GeneratedAOPProxyFactory.createProxy(null, null);
   }

   public void visit(ServiceMetaDataVisitor visitor)
   {
      // todo - add dependency
      super.visit(visitor);
   }

   public String getFilter()
   {
      return filter;
   }

   public Cardinality getCardinality()
   {
      return cardinality;
   }

   public Integer getTimeout()
   {
      return timeout;
   }

   public List<ListenerMetaData> getListeners()
   {
      return listeners;
   }

   public Set<PropertyMetaData> getProperties()
   {
      return properties;
   }

   public Class<?> getType(MetaDataVisitor visitor, MetaDataVisitorNode previous) throws Throwable
   {
      return null;
   }

   public void setFilter(String filter)
   {
      this.filter = filter;
   }

   public void setCardinality(Cardinality cardinality)
   {
      this.cardinality = cardinality;
   }

   public void setTimeout(Integer timeout)
   {
      this.timeout = timeout;
   }

   public void setListeners(List<ListenerMetaData> listeners)
   {
      this.listeners = listeners;
   }

   public void setProperties(Set<PropertyMetaData> properties)
   {
      this.properties = properties;
   }
}
