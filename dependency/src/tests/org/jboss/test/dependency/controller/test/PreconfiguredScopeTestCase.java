/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.dependency.controller.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;

import org.jboss.dependency.plugins.AbstractControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.ScopeInfo;
import org.jboss.metadata.plugins.context.AbstractMetaDataContext;
import org.jboss.metadata.plugins.loader.memory.MemoryMetaDataLoader;
import org.jboss.metadata.plugins.repository.basic.BasicMetaDataRepository;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.context.MetaDataContext;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.metadata.spi.retrieval.MetaDataRetrieval;
import org.jboss.metadata.spi.scope.Scope;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * PreconfiguredScopeTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class PreconfiguredScopeTestCase extends AbstractDependencyTest
{
   public static Test suite()
   {
      return suite(PreconfiguredScopeTestCase.class);
   }
   
   protected MutableMetaDataRepository repository = new BasicMetaDataRepository();
   
   public PreconfiguredScopeTestCase(String name)
   {
      super(name);
   }
   
   public void testPreconfiguredScope() throws Throwable
   {
      TestControllerContext test = new TestControllerContext("test");
      
      ScopeInfo scopeInfo = test.getScopeInfo();

      // Preconfigure the mutable scope
      ScopeKey mutable = scopeInfo.getMutableScope();
      MemoryMetaDataLoader loader = new MemoryMetaDataLoader(mutable);
      loader.addMetaData("Preconfigured", "testPreconfigured", String.class);
      repository.addMetaDataRetrieval(loader);
      
      // Setup the full scope
      ScopeKey scopeKey = scopeInfo.getScope();
      List<MetaDataRetrieval> retrievals = new ArrayList<MetaDataRetrieval>();
      for (Scope scope : scopeKey.getScopes())
      {
         ScopeKey thisScope = new ScopeKey(scope);
         MetaDataRetrieval retrieval = repository.getMetaDataRetrieval(thisScope);
         if (retrieval == null)
            retrieval = new MemoryMetaDataLoader(thisScope);
         retrievals.add(0, retrieval);
      }
      MetaDataContext metaDataContext = new AbstractMetaDataContext(null, retrievals);
      repository.addMetaDataRetrieval(metaDataContext);
      
      try
      {
         controller.install(test);
         
         MetaData metaData = scopeInfo.getMetaData();
         assertNotNull(metaData);
         
         assertEquals("testPreconfigured", metaData.getMetaData("Preconfigured"));
         assertEquals("testFromInstall", metaData.getMetaData("FromInstall"));
      }
      finally
      {
         controller.uninstall(test.getName());
      }
      
      assertNotNull(repository.getMetaDataRetrieval(mutable));
      assertNotNull(repository.getMetaDataRetrieval(scopeKey));
      
      MetaData metaData = repository.getMetaData(scopeKey);
      assertNotNull(metaData);
      assertEquals("testPreconfigured", metaData.getMetaData("Preconfigured"));
      assertNull(metaData.getMetaData("FromInstall"));
   }
   
   public void testNotPreconfiguredScope() throws Throwable
   {
      TestControllerContext test = new TestControllerContext("test");
      
      ScopeInfo scopeInfo = test.getScopeInfo();
      ScopeKey mutable = scopeInfo.getMutableScope();
      ScopeKey scopeKey = scopeInfo.getScope();
      try
      {
         controller.install(test);
         
         MetaData metaData = scopeInfo.getMetaData();
         assertNotNull(metaData);
         assertEquals("testFromInstall", metaData.getMetaData("FromInstall"));
      }
      finally
      {
         controller.uninstall(test.getName());
      }
      
      assertNull(repository.getMetaDataRetrieval(mutable));
      assertNull(repository.getMetaDataRetrieval(scopeKey));
   }
   
   public class TestControllerContext extends AbstractControllerContext
   {
      public TestControllerContext(Object name)
      {
         super(name, new Object());
      }

      @Override
      public void install(ControllerState fromState, ControllerState toState) throws Throwable
      {
         if (toState.equals(ControllerState.INSTANTIATED))
         {
            ScopeInfo scopeInfo = getScopeInfo();
            scopeInfo.addMetaData(repository, this);
            ScopeKey scope = scopeInfo.getMutableScope();
            MemoryMetaDataLoader loader = (MemoryMetaDataLoader) repository.getMetaDataRetrieval(scope);
            loader.addMetaData("FromInstall", "testFromInstall", String.class);
            
            // Initialise the full key
            ScopeKey full = scopeInfo.getScope();
            if (repository.getMetaDataRetrieval(full) == null)
               scopeInfo.initMetaDataRetrieval(repository, this);
         }
      }

      @Override
      public void uninstall(ControllerState fromState, ControllerState toState)
      {
         if (fromState.equals(ControllerState.INSTANTIATED))
         {
            ScopeInfo scopeInfo = getScopeInfo();
            ScopeKey scope = scopeInfo.getMutableScope();
            MemoryMetaDataLoader loader = (MemoryMetaDataLoader) repository.getMetaDataRetrieval(scope);
            loader.removeMetaData("FromInstall", String.class);
            scopeInfo.removeMetaData(repository, this);
         }
      }
   }
}
