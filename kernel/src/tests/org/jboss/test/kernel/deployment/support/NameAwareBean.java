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
package org.jboss.test.kernel.deployment.support;

import java.util.Set;

import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;

/**
 * A simple bean with name
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class NameAwareBean
{
   private String name;
   private Set<Object> aliases;
   private MetaData metadata;
   private BeanInfo beaninfo;
   private ScopeKey scopeKey;
   private Object dynamic;
   private ControllerContext context;
   private ControllerState state;

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public void applyName(String name)
   {
      this.name = name;
   }

   public MetaData getMetadata()
   {
      return metadata;
   }

   public void setMetadata(MetaData metadata)
   {
      this.metadata = metadata;
   }

   public ScopeKey getScopeKey()
   {
      return scopeKey;
   }

   public void setScopeKey(ScopeKey scopeKey)
   {
      this.scopeKey = scopeKey;
   }

   public Object getDynamic()
   {
      return dynamic;
   }

   public void setDynamic(Object dynamic)
   {
      this.dynamic = dynamic;
   }

   public Set<Object> getAliases()
   {
      return aliases;
   }

   public void setAliases(Set<Object> aliases)
   {
      this.aliases = aliases;
   }

   public BeanInfo getBeaninfo()
   {
      return beaninfo;
   }

   public void setBeaninfo(BeanInfo beaninfo)
   {
      this.beaninfo = beaninfo;
   }

   public ControllerContext getContext()
   {
      return context;
   }

   public void setContext(ControllerContext context)
   {
      this.context = context;
   }

   public ControllerState getState()
   {
      return state;
   }

   public void setState(ControllerState state)
   {
      this.state = state;
   }
}
