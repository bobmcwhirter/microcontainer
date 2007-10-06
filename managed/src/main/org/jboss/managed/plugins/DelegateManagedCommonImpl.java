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
package org.jboss.managed.plugins;

import java.util.Map;
import java.util.Set;

import org.jboss.managed.api.ManagedCommon;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedOperation;
import org.jboss.managed.api.ManagedProperty;

/**
 * Delegate managed common.
 *
 * @author ales.justin@jboss.org
 */
public class DelegateManagedCommonImpl implements ManagedCommon
{
   private static final long serialVersionUID = 1;
   private ManagedObject delegate;

   public DelegateManagedCommonImpl(ManagedObject delegate)
   {
      this.delegate = delegate;
   }

   public String getAttachmentName()
   {
      return delegate.getAttachmentName();
   }

   public String getName()
   {
      return delegate.getName();
   }

   public String getNameType()
   {
      return delegate.getNameType();
   }

   public Set<ManagedOperation> getOperations()
   {
      return delegate.getOperations();
   }

   public Map<String, ManagedProperty> getProperties()
   {
      return delegate.getProperties();
   }

   public ManagedProperty getProperty(String name)
   {
      return delegate.getProperty(name);
   }

   public Set<String> getPropertyNames()
   {
      return delegate.getPropertyNames();
   }
}
