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
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.test.kernel.annotations.support.SearchInjection;

/**
 * Order search annotation IoC support
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class OrderSearchAnnotationSupportTestCase extends AbstractSearchAnnotationSupportTest
{
   public OrderSearchAnnotationSupportTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(OrderSearchAnnotationSupportTestCase.class);
   }

   public void testWrongOrder() throws Throwable
   {
      KernelController controller = getController();
      ControllerContext context = controller.install(new AbstractBeanMetaData("si", SearchInjection.class.getName()));
      assertEquals(ControllerState.INSTANTIATED, context.getState());

      List<ControllerContext> contexts = new ArrayList<ControllerContext>();
      try
      {
         contexts.add(install("child", "main", "core", 1));
         contexts.add(install("parent", "main", null, -1));
         contexts.add(install("local", "main", "core", -1));
         contexts.add(install("top", null, null, -1));

         // here we need a little push - since by default we only do parent hierarchy resolution
         controller.change(context, ControllerState.INSTALLED);

         assertEquals(ControllerState.INSTALLED, context.getState());
         doTestAfterInstall(context.getTarget());
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

   protected void doTestAfterInstall(Object target)
   {
      SearchInjection si = (SearchInjection)target;
      // ony check exact one's
      assertScopeTester(si.getTop(), "top");
      assertScopeTester(si.getParent(), "parent");
      assertScopeTester(si.getLocal(), "local");
      assertScopeTester(si.getChildrenOnly(), "child");
      assertScopeTester(si.getLeaves(), "child");
   }
}