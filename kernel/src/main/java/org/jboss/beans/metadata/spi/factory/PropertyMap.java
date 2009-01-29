/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.beans.metadata.spi.factory;

import java.io.Serializable;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.plugins.TypeProvider;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.RelatedClassMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.config.plugins.property.PropertyConfiguration;
import org.jboss.config.spi.Configuration;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.TypeInfo;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * PropertyMap.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
class PropertyMap extends HashMap<String, ValueMetaData> implements MetaDataVisitorNode, TypeProvider
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -4295725682462294630L;

   /** The configuration */
   private static Configuration configuration;

   static
   {
      // get Configuration instance
      configuration = AccessController.doPrivileged(new PrivilegedAction<Configuration>()
      {
         public Configuration run()
         {
            return new PropertyConfiguration();
         }
      });
   }

   public void initialVisit(MetaDataVisitor visitor)
   {
      visitor.initialVisit(this);
   }

   public void describeVisit(MetaDataVisitor vistor)
   {
      vistor.describeVisit(this);
   }

   public Iterator<? extends MetaDataVisitorNode> getChildren()
   {
      return values().iterator();
   }

   public ValueMetaData put(String name, ValueMetaData value)
   {
      ValueInfo info = new ValueInfo(name, value);
      return super.put(name, info);
   }

   public TypeInfo getType(MetaDataVisitor visitor, MetaDataVisitorNode previous) throws Throwable
   {
      ValueInfo valueInfo = null;
      for (ValueMetaData value : values())
      {
         ValueInfo vi = ValueInfo.class.cast(value);
         if (vi.value == previous)
         {
            valueInfo = vi;
            break;
         }
      }
      if (valueInfo == null)
         throw new IllegalArgumentException("No matching value (" + previous + ") found: " + values());

      KernelControllerContext context = visitor.getControllerContext();
      BeanMetaData bmd = context.getBeanMetaData();
      Set<RelatedClassMetaData> related = bmd.getRelated();
      if (related == null || related.size() != 1)
         throw new IllegalArgumentException("Invalid information for contextual injection: " + bmd);
      // TODO - perhaps match which related metadata is the right one
      RelatedClassMetaData beanClassMetaData = related.iterator().next();
      BeanInfo beanInfo = configuration.getBeanInfo(beanClassMetaData.getClassName(), context.getClassLoader());
      PropertyInfo pi = beanInfo.getProperty(valueInfo.name);
      TypeInfo typeInfo = pi.getType();
      if (typeInfo.isCollection() || typeInfo.isMap())
      {
         throw new IllegalArgumentException("Cannot handle collection or map: " + valueInfo);
      }
      return typeInfo;
   }

   private static class ValueInfo extends JBossObject implements ValueMetaData, Serializable
   {
      private static final long serialVersionUID = 1L;

      private String name;
      private ValueMetaData value;

      private ValueInfo(String name, ValueMetaData value)
      {
         this.name = name;
         this.value = value;
      }

      public Object getUnderlyingValue()
      {
         return value.getUnderlyingValue();
      }

      public Object getValue(TypeInfo info, ClassLoader cl) throws Throwable
      {
         return value.getValue(info, cl);
      }

      public void initialVisit(MetaDataVisitor vistor)
      {
         value.initialVisit(vistor);
      }

      public void describeVisit(MetaDataVisitor vistor)
      {
         value.describeVisit(vistor);
      }

      public Iterator<? extends MetaDataVisitorNode> getChildren()
      {
         return value.getChildren();
      }

      public void toShortString(JBossStringBuilder buffer)
      {
         value.toShortString(buffer);
      }

      @Override
      public int hashCode()
      {
         return value.hashCode();
      }

      @Override
      public boolean equals(Object obj)
      {
         return value.equals(obj);
      }

      public String toShortString()
      {
         return value.toShortString();
      }
   }
}
