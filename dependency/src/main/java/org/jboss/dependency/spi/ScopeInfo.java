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
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.Scope;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * ScopeInfo.
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
    * Add metadata
    * 
    * @param repository the repository
    * @param context the controller context
    */
   void addMetaData(MutableMetaDataRepository repository, ControllerContext context);

   /**
    * Add metadata
    * 
    * @param repository the repository
    * @param context the controller context
    */
   void removeMetaData(MutableMetaDataRepository repository, ControllerContext context);

   /**
    * Initialise the metadata retrieval
    * 
    * @param repository the repository
    * @param context the context
    * @param scope the scope level
    * @return the retrieval
    */
   MetaDataRetrieval initMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context, Scope scope);

   /**
    * Initialise the main metadata retrieval
    * 
    * @param repository the repository
    * @param context the context
    * @return the retrieval
    */
   MetaDataRetrieval initMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context);

   /**
    * Initialise the main mutable metadata retrieval.
    *
    * @param repository the mutable metadata repository
    * @param context the controller context
    * @param scopeKey the scope key
    * @return new mutable metadata loader instance
    */
   MutableMetaDataLoader initMutableMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context, ScopeKey scopeKey);

   /**
    * Get the scope
    * 
    * @return the scope
    */
   ScopeKey getScope();

   /**
    * Set the scope
    * 
    * @param key the scope key
    */
   void setScope(ScopeKey key);

   /**
    * Get the mutable scope
    * 
    * @return the scope
    */
   ScopeKey getMutableScope();

   /**
    * Set the mutable scope
    * 
    * @param key the scope key
    */
   void setMutableScope(ScopeKey key);

   /**
    * Get the install scope
    *
    * @return the scope
    */
   ScopeKey getInstallScope();

   /**
    * Set the install scope
    *
    * @param key the scope key
    */
   void setInstallScope(ScopeKey key);
}
