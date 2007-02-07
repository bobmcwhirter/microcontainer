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
package org.jboss.beans.metadata.plugins.policy;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jboss.beans.metadata.plugins.AbstractFeatureMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitor;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.policy.BindingMetaData;
import org.jboss.beans.metadata.spi.policy.PolicyMetaData;
import org.jboss.beans.metadata.spi.policy.ScopeMetaData;
import org.jboss.util.JBossStringBuilder;

/**
 * Meta data for policy.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class AbstractPolicyMetaData extends AbstractFeatureMetaData implements PolicyMetaData
{
   private static final long serialVersionUID = 1;

   private String name;
   private String ext;
   private ScopeMetaData scope;
   private Set<BindingMetaData> bindings;

   public String getName()
   {
      return name;
   }

   public String getExtends()
   {
      return ext;
   }

   public ScopeMetaData getScope()
   {
      return scope;
   }

   public Set<BindingMetaData> getBindings()
   {
      return bindings;
   }

   public List<BeanMetaData> getBeans()
   {
      List<BeanMetaData> beans = new ArrayList<BeanMetaData>();
      for(BindingMetaData binding : bindings)
      {
         ValueMetaData value = binding.getValue();
         if (value instanceof BeanMetaData)
         {
            beans.add((BeanMetaData)value);
         }
      }
      return beans;
   }

   protected void addChildren(Set<MetaDataVisitorNode> children)
   {
      if (scope != null)
         children.add(scope);
      super.addChildren(children);
      if (bindings != null && bindings.size() > 0)
         children.addAll(bindings);
   }

   public Class getType(MetaDataVisitor visitor, MetaDataVisitorNode previous) throws Throwable
   {
      throw new IllegalArgumentException("Cannot determine inject class type: " + this);
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public void setExtends(String ext)
   {
      this.ext = ext;
   }

   public void setScope(ScopeMetaData scope)
   {
      this.scope = scope;
   }

   public void setBindings(Set<BindingMetaData> bindings)
   {
      this.bindings = bindings;
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("name=").append(name);
      buffer.append(" extends=").append(ext);
      buffer.append(" scope=").append(scope);
      super.toString(buffer);
      buffer.append(" bindings=").append(bindings);
   }

   public void toShortString(JBossStringBuilder buffer)
   {
      buffer.append(name);
      buffer.append('/');
      buffer.append(ext);
      buffer.append('/');
      buffer.append(scope);
      buffer.append('/');
      super.toShortString(buffer);
      buffer.append('/');
      buffer.append(bindings);
   }

}
