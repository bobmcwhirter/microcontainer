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
import java.util.UUID;

import junit.framework.Test;
import org.jboss.beans.metadata.api.annotations.Aliases;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.kernel.plugins.annotations.AbstractBeanAnnotationAdapter;
import org.jboss.kernel.plugins.annotations.BasicBeanAnnotationAdapter;
import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapter;
import org.jboss.kernel.plugins.annotations.SearchPropertyAnnotationPlugin;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.metadata.plugins.scope.ApplicationScope;
import org.jboss.metadata.plugins.scope.DeploymentScope;
import org.jboss.metadata.plugins.scope.InstanceScope;
import org.jboss.test.kernel.annotations.support.ScopeTester;
import org.jboss.test.kernel.annotations.support.SearchInjection;
import org.jboss.test.kernel.annotations.test.AbstractBeanAnnotationAdapterTest;

/**
 * Basic search annotation IoC support
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BasicSearchAnnotationSupportTestCase extends AbstractBeanAnnotationAdapterTest
{
   public BasicSearchAnnotationSupportTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BasicSearchAnnotationSupportTestCase.class);
   }

   protected BeanAnnotationAdapter getBeanAnnotationAdapterClass()
   {
      AbstractBeanAnnotationAdapter adapter = BasicBeanAnnotationAdapter.INSTANCE;
      adapter.addAnnotationPlugin(SearchPropertyAnnotationPlugin.INSTANCE);
      return adapter;
   }

   public void testSearchTypes() throws Throwable
   {
      List<ControllerContext> contexts = new ArrayList<ControllerContext>();
      try
      {
         contexts.add(install("top", null, null, -1));
         contexts.add(install("parent", "main", null, -1));
         contexts.add(install("local", "main", "core", -1));
         contexts.add(install("child", "main", "core", 1));

         runAnnotationsOnClass(SearchInjection.class);
      }
      finally
      {
         ListIterator<ControllerContext> iter = contexts.listIterator(contexts.size());
         while (iter.hasPrevious())
         {
            getController().uninstall(iter.previous().getName());
         }
      }
   }

   protected void doTestAfterInstall(Object target)
   {
      SearchInjection si = (SearchInjection)target;
      assertScopeTester(si.getTop(), "top");
      assertScopeTester(si.getParent(), "parent");
      assertScopeTester(si.getLocal(), "local");
      assertScopeTester(si.getNormal(), "local");
      assertScopeTester(si.getWithChildren(), "local");
      assertScopeTester(si.getChildrenOnly(), "child");
      assertScopeTester(si.getLeaves(), "child");      
   }

   protected void assertScopeTester(ScopeTester tester, String scope)
   {
      assertNotNull(scope, tester);
      assertEquals(scope, tester.getScope());
   }

   protected ControllerContext install(String scope, String app, String deployment, int id) throws Throwable
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder(UUID.randomUUID().toString(), ScopeTester.class.getName());
      builder.addConstructorParameter(String.class.getName(), scope);
      builder.addAnnotation("@" + Aliases.class.getName() + "({\"bean\"})");

      if (app != null)
         builder.addAnnotation("@" + ApplicationScope.class.getName() + "(\"" + app + "\")");
      if (deployment != null)
         builder.addAnnotation("@" + DeploymentScope.class.getName() + "(\"" + deployment + "\")");
      if (id > 0)
         builder.addAnnotation("@" + InstanceScope.class.getName() + "(\"id-" + Integer.toString(id) + "\")");

      KernelController controller = getController();
      return controller.install(builder.getBeanMetaData());
   }
}