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
package org.jboss.test.kernel.annotations.test.search;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import junit.framework.Test;
import org.jboss.dependency.plugins.graph.ScopeKeySearchInfo;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.graph.GraphController;
import org.jboss.dependency.spi.graph.SearchInfo;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.metadata.spi.scope.CommonLevels;

/**
 * Search scopes by scope key.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ScopeKeySearchTestCase extends AbstractSearchAnnotationSupportTest
{
   public ScopeKeySearchTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(ScopeKeySearchTestCase.class);
   }

   public void testScopeKeyControllers() throws Throwable
   {
      List<ControllerContext> contexts = new ArrayList<ControllerContext>();
      try
      {
         contexts.add(assertScopeBean("child", "main", "core", 1));
         contexts.add(assertScopeBean("parent", "main", null, -1));
         contexts.add(assertScopeBean("local", "main", "core", -1));
      }
      finally
      {
         ListIterator<ControllerContext> iter = contexts.listIterator(contexts.size());
         while (iter.hasPrevious())
         {
            Object name = iter.previous().getName();
            getController().uninstall(name);
         }
      }
   }

   protected ControllerContext assertScopeBean(String scope, String app, String deployment, int id) throws Throwable
   {
      ControllerContext context = install(scope, app, deployment, id);

      ScopeKey scopeKey = new ScopeKey();
      if (app != null)
         scopeKey.addScope(CommonLevels.APPLICATION, app);
      if (deployment != null)
         scopeKey.addScope(CommonLevels.DEPLOYMENT, deployment);
      if (id > 0)
         scopeKey.addScope(CommonLevels.INSTANCE, "id-" + id);

      SearchInfo searchInfo = new ScopeKeySearchInfo(scopeKey);
      GraphController gc = (GraphController)getController();
      ControllerContext lookup = gc.getContext("bean", null, searchInfo);
      assertSame(context, lookup);

      return context;
   }
}