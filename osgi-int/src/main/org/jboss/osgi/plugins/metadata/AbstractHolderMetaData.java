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

import java.io.Serializable;
import java.util.Set;

import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.osgi.spi.metadata.DependencyMetaData;
import org.jboss.osgi.spi.metadata.HolderMetaData;

/**
 * Common OSGi reference or service impl.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractHolderMetaData extends AbstractMetaDataVisitorNode
      implements HolderMetaData, Serializable
{
   private static final long serialVersionUID = 1l;

   private String id;
   private String anInterface;
   private Set<DependencyMetaData> depends;
   private ClassLoaderMetaData classLoaderMetaData;

   public String getId()
   {
      return id;
   }

   public String getInterface()
   {
      return anInterface;
   }

   public Set<DependencyMetaData> getDepends()
   {
      return depends;
   }

   public ClassLoaderMetaData getContextClassLoader()
   {
      return classLoaderMetaData;
   }

   public void setId(String id)
   {
      this.id = id;
   }

   public void setInterface(String anInterface)
   {
      this.anInterface = anInterface;
   }

   public void setDepends(Set<DependencyMetaData> depends)
   {
      this.depends = depends;
   }

   public void setClassLoaderMetaData(ClassLoaderMetaData classLoaderMetaData)
   {
      this.classLoaderMetaData = classLoaderMetaData;
   }
}
