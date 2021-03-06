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

import java.util.Set;

import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.metadata.plugins.loader.memory.MemoryMetaDataLoader;
import org.jboss.metadata.spi.MutableMetaData;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.MetaDataItem;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.Scope;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * POJO binding bean meta data to scoped repository.
 * TODO - a lot more work (once we finalize scoped BeanMD deployment) 
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class MetaDataRepositoryInstaller
{
   private Kernel underlyingKernel;
   private Set<Scope> scopes;
   
   /* TODO What is this for, it is unreferenced? */
   // private Map<String, Object> bindings;

   public void setKernel(Kernel kernel)
   {
      this.underlyingKernel = kernel;
   }

   public void setScopes(Set<Scope> scopes)
   {
      this.scopes = scopes;
   }

   /* TODO What is this for?
   public void setBindings(Map<String, Object> bindings)
   {
      this.bindings = bindings;
   }
   */

   public void start()
   {
      KernelMetaDataRepository kmdr = underlyingKernel.getMetaDataRepository();
      MutableMetaDataRepository mmdr = kmdr.getMetaDataRepository();
      ScopeKey scopeKey = new ScopeKey(scopes);
      MetaDataRetrieval retrieval = mmdr.getMetaDataRetrieval(scopeKey);
      // Not found create it
      if (retrieval == null)
      {
         retrieval = new MemoryMetaDataLoader(scopeKey);
         mmdr.addMetaDataRetrieval(retrieval);
      }
      MutableMetaData mmd = (MutableMetaData)retrieval;
      // deploy against this kernel
      Kernel kernel = underlyingKernel;
      // Get the parent scope - if exists
      ScopeKey parent = scopeKey.getParent();
      if (parent != null)
      {
         MetaDataRetrieval pretrieval = mmdr.getMetaDataRetrieval(parent);
         MetaDataItem<Kernel> item = pretrieval.retrieveMetaData(Kernel.class);
         if (item != null)
            kernel = item.getValue();
      }
      // Create a scoped kernel Kernel
      Kernel scopedKernel = kernel;// TODO new ScopedKernel(kernel);

      mmd.addMetaData(scopedKernel, Kernel.class);

   }

   public void stop()
   {

   }

}
