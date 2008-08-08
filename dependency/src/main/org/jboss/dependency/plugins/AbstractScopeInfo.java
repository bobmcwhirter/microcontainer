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
package org.jboss.dependency.plugins;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ScopeInfo;
import org.jboss.logging.Logger;
import org.jboss.metadata.plugins.context.AbstractMetaDataContext;
import org.jboss.metadata.plugins.loader.memory.MemoryMetaDataLoader;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.context.MetaDataContext;
import org.jboss.metadata.spi.loader.MutableMetaDataLoader;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.CommonLevels;
import org.jboss.metadata.spi.scope.Scope;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * AbstractScopeInfo.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AbstractScopeInfo implements ScopeInfo
{
   /** The log */
   private static final Logger log = Logger.getLogger(AbstractScopeInfo.class);
   
   /** The scope */
   private ScopeKey scopeKey;
   
   /** The mutable scope */
   private ScopeKey mutableScopeKey;

   /** The install scope */
   private ScopeKey installScope;
   
   /** The repository */
   private MutableMetaDataRepository repository;

   /** The added scopes */
   private CopyOnWriteArraySet<ScopeKey> addedScopes = new CopyOnWriteArraySet<ScopeKey>();
   
   /**
    * Create a new AbstractScopeInfo.
    * 
    * @param name the name
    */
   public AbstractScopeInfo(Object name)
   {
      this(name, null);
   }
   
   /**
    * Create a new AbstractScopeInfo.
    * 
    * @param name the name
    * @param className the class name
    */
   public AbstractScopeInfo(Object name, String className)
   {
      if (name == null)
         throw new IllegalArgumentException("Null scope");

      ScopeKey scopeKey = ScopeKey.DEFAULT_SCOPE.clone();
      scopeKey.addScope(CommonLevels.INSTANCE, name.toString());
      if (className != null)
         scopeKey.addScope(CommonLevels.CLASS, className);
      // todo - some other level
      scopeKey.addScope(CommonLevels.WORK, String.valueOf(hashCode()));
      setScope(scopeKey);
      setMutableScope(new ScopeKey(CommonLevels.INSTANCE, name.toString()));
   }
   
   /**
    * Create a new AbstractScopeInfo.
    * 
    * @param key the scope key
    * @param mutable the mutable scope key
    */
   public AbstractScopeInfo(ScopeKey key, ScopeKey mutable)
   {
      setScope(key);
      setMutableScope(mutable);
   }

   public MetaData getMetaData()
   {
      if (repository == null)
         return null;
      return repository.getMetaData(getScope());
   }

   public void addMetaData(MutableMetaDataRepository repository, ControllerContext context)
   {
      this.repository = repository;
      ScopeKey scope = getMutableScope();
      MetaDataRetrieval retrieval = repository.getMetaDataRetrieval(scope);
      MutableMetaDataLoader mutable = null;
      if (retrieval == null)
      {
         mutable = initMutableMetaDataRetrieval(repository, context, scope);
         repository.addMetaDataRetrieval(mutable);
         addedScopes.add(scope);
      }
      else
      {
         mutable = getMutableMetaDataLoader(retrieval);
      }
      
      if (mutable == null)
      {
         log.warn("MetaData context is not mutable: " + retrieval + " for " + context.toShortString());
         return;
      }
      
      updateMetaData(repository, context, mutable, true);
   }
   
   /**
    * Update metadata
    * 
    * @param repository the repository
    * @param context the context
    * @param mutable the mutable
    * @param add true for add, false for remove
    */
   protected void updateMetaData(MutableMetaDataRepository repository, ControllerContext context, MutableMetaDataLoader mutable, boolean add)
   {
      // nothing
   }

   public void removeMetaData(MutableMetaDataRepository repository, ControllerContext context)
   {
      ScopeKey mutableScope = getMutableScope();
      MetaDataRetrieval retrieval = repository.getMetaDataRetrieval(mutableScope);
      if (retrieval != null)
      {
         MutableMetaDataLoader mutable = getMutableMetaDataLoader(retrieval);
         if (mutable != null)
            updateMetaData(repository, context, mutable, false);
      }

      for (ScopeKey scope : addedScopes)
      {
         try
         {
            repository.removeMetaDataRetrieval(scope);
         }
         catch (Exception e)
         {
            log.trace("Ignored", e);
         }
      }
      addedScopes.clear();
      this.repository = null;
      
   }

   protected MutableMetaDataLoader getMutableMetaDataLoader(MetaDataRetrieval retrieval)
   {
      if (retrieval == null)
         return null;

      if (retrieval instanceof MutableMetaDataLoader)
      {
         return (MutableMetaDataLoader) retrieval;
      }
      else if (retrieval instanceof MetaDataContext)
      {
         MetaDataContext metaDataContext = (MetaDataContext) retrieval;
         List<MetaDataRetrieval> locals = metaDataContext.getLocalRetrievals();
         if (locals != null)
         {
            for (MetaDataRetrieval local : locals)
            {
               if (local instanceof MutableMetaDataLoader)
                  return (MutableMetaDataLoader) local;
            }
         }
      }
      return null;
   }

   public MutableMetaDataLoader initMutableMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context, ScopeKey scopeKey)
   {
      return new MemoryMetaDataLoader(scopeKey);
   }

   public MetaDataRetrieval initMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context, Scope scope)
   {
      // Nothing
      return null;
   }

   public MetaDataRetrieval initMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context)
   {
      ScopeKey scopeKey = getScope();
      List<MetaDataRetrieval> retrievals = new ArrayList<MetaDataRetrieval>();
      for (Scope scope : scopeKey.getScopes())
      {
         ScopeKey thisScope = new ScopeKey(scope);
         MetaDataRetrieval retrieval = repository.getMetaDataRetrieval(thisScope);
         if (retrieval == null)
         {
            retrieval = initMetaDataRetrieval(repository, context, scope);
            if (retrieval == null)
            {
               retrieval = initMutableMetaDataRetrieval(repository, context, thisScope);
               repository.addMetaDataRetrieval(retrieval);
               addedScopes.add(thisScope);
            }
         }
         retrievals.add(0, retrieval);
      }
      MetaDataContext metaDataContext = createMetaDataContext(retrievals);
      repository.addMetaDataRetrieval(metaDataContext);
      addedScopes.add(metaDataContext.getScope());
      return metaDataContext;
   }
   
   public ScopeKey getScope()
   {
      return scopeKey;
   }

   public void setScope(ScopeKey key)
   {
      if (key == null)
         throw new IllegalArgumentException("Null scope key");
      this.scopeKey = key;
   }

   public ScopeKey getMutableScope()
   {
      return mutableScopeKey;
   }

   public void setMutableScope(ScopeKey key)
   {
      if (key == null)
         throw new IllegalArgumentException("Null scope key");
      this.mutableScopeKey = key;
   }

   public ScopeKey getInstallScope()
   {
      return installScope;
   }

   public void setInstallScope(ScopeKey key)
   {
      this.installScope = key;
   }

   /**
    * Create metadata context.
    *
    * @param retrievals the retrievals
    * @return new metadata context instance
    */
   protected MetaDataContext createMetaDataContext(List<MetaDataRetrieval> retrievals)
   {
      return new AbstractMetaDataContext(null, retrievals);
   }
}
