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
package org.jboss.dependency.spi;

import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.loader.MutableMetaDataLoader;
import org.jboss.metadata.spi.repository.MetaDataRepository;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.Scope;
import org.jboss.metadata.spi.scope.ScopeFactoryLookup;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * ScopeInfo belongs to a {@link ControllerContext}, and is the entry point into the 
 * {@link ControllerContext}'s data in the {@link MetaDataRepository}. Each {@link ControllerContext}
 * has data stored under a unique {@link ScopeKey}.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface ScopeInfo
{
   /**
    * Get the metadata
    * 
    * @return the metadata
    */
   MetaData getMetaData();

   /**
    * Associates the passed in metadata repository with the controller context.
    * The {@link ScopeKey} is created and the metadata repository location for the
    * controller context is created.
    * 
    * @param repository the repository
    * @param context the controller context
    */
   void addMetaData(MutableMetaDataRepository repository, ControllerContext context);

   /**
    * Disassociates the passed in metadata repository with the controller context.
    * The location of the {@link ScopeKey} for the passed in controller context
    * is removed from the metadata repository.
    *  
    * 
    * @param repository the repository
    * @param context the controller context
    */
   void removeMetaData(MutableMetaDataRepository repository, ControllerContext context);

   /**
    * Initialise the metadata retrieval for a given {@link Scope} in {@link ScopeKey}.
    * 
    * @param repository the repository
    * @param context the context
    * @param scope the scope level
    * @return the retrieval
    */
   MetaDataRetrieval initMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context, Scope scope);

   /**
    * Initialise the main metadata retrieval. The returned retrieval will contain metadata retrievals for 
    * each level of {@link Scope} in {@link ScopeKey}.
    * 
    * @param repository the repository
    * @param context the context
    * @return the retrieval
    */
   MetaDataRetrieval initMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context);

   /**
    * Initialise the main mutable metadata retrieval. The returned mutable metadata loader contains the
    * location of the context metadata where data can be added.
    *
    * @param repository the mutable metadata repository
    * @param context the controller context
    * @param scopeKey the scope key
    * @return new mutable metadata loader instance
    */
   MutableMetaDataLoader initMutableMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context, ScopeKey scopeKey);

   /**
    * Get the scope key for the {@link ControllerContext}'s metadata.
    * 
    * @return the scope key
    */
   ScopeKey getScope();

   /**
    * Set the scope key for the {@link ControllerContext}'s metadata. This method should only be called by the
    * {@link Controller}.
    * 
    * @param key the scope key
    * @throws IllegalArgumentException if null
    */
   void setScope(ScopeKey key);

   /**
    * Get the mutable scope key for the context
    * 
    * @return the scope key
    */
   ScopeKey getMutableScope();

   /**
    * Set the mutable scope This method should only be called by the
    * {@link Controller}.
    * 
    * @param key the scope key
    * @throws IllegalArgumentException if null
    */
   void setMutableScope(ScopeKey key);

   /**
    * Get the install scope. If a scoped {@link Controller} (i.e. a child of the main {@link Controller})
    * was used, by annotating the {@link ControllerContext} with annotations annotated with {@link ScopeFactoryLookup}
    * this is the location of the scoped {@link Controller} in the underlying metadata repository. 
    *
    * @return the scope
    */
   ScopeKey getInstallScope();

   /**
    * Get the install scope. If a scoped {@link Controller} (i.e. a child of the main {@link Controller})
    * was used, by annotating the {@link ControllerContext} with annotations annotated with {@link ScopeFactoryLookup}
    * this is the location of the scoped {@link Controller} in the underlying metadata repository. This method should 
    * only be called by the {@link Controller}. 
    *  
    *
    * @param key the scope key
    */
   void setInstallScope(ScopeKey key);
}
