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
package org.jboss.dependency.spi.helpers;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ScopeInfo;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.Scope;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * UnmodifiableScopeInfo.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class UnmodifiableScopeInfo implements ScopeInfo
{
   /** The delegate */
   private ScopeInfo delegate;

   /**
    * Create a new UnmodifiableScopeInfo.
    * 
    * @param delegate the delegate
    */
   public UnmodifiableScopeInfo(ScopeInfo delegate)
   {
      if (delegate == null)
         throw new IllegalArgumentException("Null delegate");
      this.delegate = delegate;
   }

   public void addMetaData(MutableMetaDataRepository repository, ControllerContext context)
   {
      throw new UnsupportedOperationException("Cannot modify immutable");
   }

   public void removeMetaData(MutableMetaDataRepository repository, ControllerContext context)
   {
      throw new UnsupportedOperationException("Cannot modify immutable");
   }

   public MetaDataRetrieval initMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context, Scope scope)
   {
      throw new UnsupportedOperationException("Cannot modify immutable");
   }

   public MetaData getMetaData()
   {
      return delegate.getMetaData();
   }

   public ScopeKey getScope()
   {
      return delegate.getScope();
   }

   public void setScope(ScopeKey key)
   {
      throw new UnsupportedOperationException("Cannot modify immutable");
   }

   public ScopeKey getMutableScope()
   {
      return delegate.getScope();
   }

   public void setMutableScope(ScopeKey key)
   {
      throw new UnsupportedOperationException("Cannot modify immutable");
   }

   public ScopeKey getInstallScope()
   {
      return delegate.getInstallScope();
   }

   public void setInstallScope(ScopeKey key)
   {
      throw new UnsupportedOperationException("Cannot modify immutable");
   }
}
