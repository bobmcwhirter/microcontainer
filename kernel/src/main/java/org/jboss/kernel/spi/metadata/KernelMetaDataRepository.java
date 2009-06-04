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
package org.jboss.kernel.spi.metadata;

import java.util.List;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.KernelObject;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * The entry point to the {@link MutableMetaDataRepository} used for a {@link Kernel}.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface KernelMetaDataRepository extends KernelObject
{
   /**
    * Get the meta data repository for the {@link Kernel}
    * 
    * @return the meta data repository
    */
   MutableMetaDataRepository getMetaDataRepository();

   /**
    * Get MetaData for a {@link ControllerContext} from the {@link Kernel}'s 
    * {@link MutableMetaDataRepository}.
    * 
    * @param context the context
    * @return the metadata
    */
   MetaData getMetaData(ControllerContext context);

   /**
    * Add metadata to a bean's {@link ControllerContext} from the {@link Kernel}'s 
    * {@link MutableMetaDataRepository}
    * 
    * @param context the context
    */
   void addMetaData(ControllerContext context);

   /**
    * Remove any previously added metadata from a {@link ControllerContext}.
    * 
    * @param context the context
    */
   void removeMetaData(ControllerContext context);

   /**
    * Get the default (non-mutable) scope key for a context
    * 
    * @param context the context
    * @return the default scope
    */
   ScopeKey getFullScope(ControllerContext context);
   
   /**
    * Get the mutable scope key for a context
    * 
    * @param context the context
    * @return the default scope
    */
   ScopeKey getMutableScope(ControllerContext context);

   /**
    * Create a metadata retrieval for a {@link ControllerContext}.
    * The returned metadata retrieval will be a composite containing the passed in retrievals. 
    * 
    * @param context the controller context
    * @param retrievals the retrievals
    * @return the metadata context
    */
   MetaDataRetrieval createMetaDataRetrieval(ControllerContext context, List<MetaDataRetrieval> retrievals);
}
