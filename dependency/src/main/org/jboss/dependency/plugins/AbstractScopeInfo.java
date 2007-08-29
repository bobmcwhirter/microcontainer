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

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ScopeInfo;
import org.jboss.logging.Logger;
import org.jboss.metadata.plugins.loader.memory.MemoryMetaDataLoader;
import org.jboss.metadata.spi.MetaData;
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
      MemoryMetaDataLoader mutable = new MemoryMetaDataLoader(scope);
      repository.addMetaDataRetrieval(mutable);
      addMetaData(repository, context, mutable);
   }

   /**
    * Add metadata
    * 
    * @param repository the repository
    * @param context the context
    * @param mutable the mutable
    */
   protected void addMetaData(MutableMetaDataRepository repository, ControllerContext context, MemoryMetaDataLoader mutable)
   {
      // nothing
   }

   public void removeMetaData(MutableMetaDataRepository repository, ControllerContext context)
   {
      // Remove the read only/full scope
      try
      {
         ScopeKey scope = getScope();
         repository.removeMetaDataRetrieval(scope);
      }
      catch (Exception e)
      {
         log.trace("Ignored", e);
      }
      try
      {
         // Remove the mutable scope
         ScopeKey scope = getMutableScope();
         repository.removeMetaDataRetrieval(scope);
      }
      catch (Exception e)
      {
         log.trace("Ignored", e);
      }
      this.repository = null;
      
   }

   public MetaDataRetrieval initMetaDataRetrieval(MutableMetaDataRepository repository, ControllerContext context, Scope scope)
   {
      // Nothing
      return null;
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
}
