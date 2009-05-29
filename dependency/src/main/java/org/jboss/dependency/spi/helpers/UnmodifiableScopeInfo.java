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
import org.jboss.metadata.spi.loader.MutableMetaDataLoader;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.Scope;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * A wrapper around a {@link ScopeInfo} that throws UnsupportedOperationException when any
 * methods that might mutate the underlying {@link ScopeInfo} state is called. 
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

   /**
    * Overrides {@link ScopeInfo#addMetaData(MutableMetaDataRepository, ControllerContext)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param repository the MutableMetaDataRepository
    * @param context the controller context
    * @throws UnsupportedOperationException when called
    */
   public void addMetaData(MutableMetaDataRepository repository, ControllerContext context)
   {
      throw new UnsupportedOperationException("Cannot modify immutable");
   }

   /**
    * Overrides {@link ScopeInfo#removeMetaData(MutableMetaDataRepository, ControllerContext)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param repository the MutableMetaDataRepository
    * @param context the controller context
    * @throws UnsupportedOperationException when called
    */
   public void removeMetaData(MutableMetaDataRepository repository, ControllerContext context)
   {
      throw new UnsupportedOperationException("Cannot modify immutable");
   }

   /**
    * Overrides {@link ScopeInfo#initMetaDataRetrieval(MutableMetaDataRepository, ControllerContext, Scope)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param repository the MutableMetaDataRepository
    * @param context the controller context
    * @param scope the scope
    * @throws UnsupportedOperationException when called
    */
   public MetaDataRetrieval initMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context, Scope scope)
   {
      throw new UnsupportedOperationException("Cannot modify immutable");
   }

   /**
    * Overrides {@link ScopeInfo#initMetaDataRetrieval(MutableMetaDataRepository, ControllerContext)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param repository the MutableMetaDataRepository
    * @param context the controller context
    * @throws UnsupportedOperationException when called
    */
   public MetaDataRetrieval initMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context)
   {
      throw new UnsupportedOperationException("Cannot modify immutable");
   }

   /**
    * Overrides {@link ScopeInfo#initMutableMetaDataRetrieval(MutableMetaDataRepository, ControllerContext, ScopeKey)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param repository the MutableMetaDataRepository
    * @param context the controller context
    * @param scopeKet the scope key
    * @throws UnsupportedOperationException when called
    */
   public MutableMetaDataLoader initMutableMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context, ScopeKey scopeKey)
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

   /**
    * Overrides {@link ScopeInfo#setInstallScope(ScopeKey)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param key the scope key
    * @throws UnsupportedOperationException when called
    */
   public void setInstallScope(ScopeKey key)
   {
      throw new UnsupportedOperationException("Cannot modify immutable");
   }
}
