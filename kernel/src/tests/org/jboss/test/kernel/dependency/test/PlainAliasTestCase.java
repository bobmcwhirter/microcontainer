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
package org.jboss.test.kernel.dependency.test;

import java.util.HashSet;

import junit.framework.Test;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.Controller;
import org.jboss.test.kernel.dependency.support.SimpleBean;
import org.jboss.test.kernel.dependency.support.SimpleBeanImpl;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;

/**
 * Plain alias tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class PlainAliasTestCase extends AbstractKernelDependencyTest
{
   public PlainAliasTestCase(String name)
         throws Throwable
   {
      super(name);
   }

   public PlainAliasTestCase(String name, boolean xmltest)
         throws Throwable
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(PlainAliasTestCase.class);
   }

   protected ControllerContext getAlias(String name) throws Throwable
   {
      installAlias();
      return getUtil().getContext(name);
   }

   protected String  getControllerId()
   {
      Controller controller = getUtil().getKernel().getController();
      StringBuffer buffer = new StringBuffer();
      buffer.append(controller.getClass().getSimpleName());
      buffer.append("[").append(System.identityHashCode(controller)).append("]");
      return buffer.toString();
   }

   public void testPlainAliasCorrectOrder() throws Throwable
   {
      buildMetaData();

      ControllerContext context1 = assertInstall(0, "OriginalBean");
      ControllerContext context2 = getAlias("MyAlias");
      assertContext("MyAlias_Alias_" + getControllerId());

      SimpleBean bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      assertEquals(ControllerState.INSTALLED, context2.getState());

      ControllerContext alias = assertContext("MyAlias");
      assertEquals(alias, context1);
   }

   public void testPlainAliasWrongOrder() throws Throwable
   {
      buildMetaData();

      ControllerContext context1 = getAlias("MyAlias");
      assertNull(context1);

      checkDirectAlias();

      ControllerContext context2 = assertInstall(0, "OriginalBean");
      context1 = assertContext("MyAlias");
      assertEquals(ControllerState.INSTALLED, context1.getState());
      getDirectAlias(ControllerState.INSTALLED);

      SimpleBean bean1 = (SimpleBean) context2.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());

      ControllerContext alias = assertContext("MyAlias");
      assertEquals(alias, context1);      
   }

   protected void checkDirectAlias() throws Throwable
   {
      getDirectAlias(ControllerState.START).getState();
   }

   protected ControllerContext getDirectAlias(ControllerState state) throws Throwable
   {
      return assertContext("MyAlias_Alias_" + getControllerId(), state);
   }

   public void testPlainAliasReinstall() throws Throwable
   {
      buildMetaData();

      ControllerContext context1 = assertInstall(0, "OriginalBean");
      ControllerContext context2 = getAlias("MyAlias");

      SimpleBean bean1 = (SimpleBean) context1.getTarget();
      assertNotNull(bean1);
      assertEquals("String1", bean1.getString());
      assertEquals(ControllerState.INSTALLED, context2.getState());

      ControllerContext alias = assertContext("MyAlias");
      assertEquals(alias, context1);

      ControllerContext directAlias = assertContext("MyAlias_Alias_" + getControllerId());
      assertEquals(ControllerState.INSTALLED, directAlias.getState());

      assertUninstall("OriginalBean");
      assertEquals(ControllerState.ERROR, context1.getState());
      assertEquals(ControllerState.ERROR, alias.getState());
      assertEquals(getDirectAliasUnistallState(), directAlias.getState());
      assertNull(getUtil().getContext("OriginalBean"));
      assertNull(getUtil().getContext("MyAlias"));

      context1 = assertInstall(0, "OriginalBean");
      directAlias = assertContext("MyAlias_Alias_" + getControllerId());
      assertEquals(ControllerState.INSTALLED, directAlias.getState());
      assertEquals(context1, assertContext("MyAlias"));

      assertUninstall("MyAlias_Alias_" + getControllerId());
      assertEquals(ControllerState.ERROR, directAlias.getState());
      assertEquals(ControllerState.INSTALLED, context1.getState());
      assertNull(getUtil().getContext("MyAlias"));

      checkLastSimpleAliasInstall(context1);
   }

   protected ControllerState getDirectAliasUnistallState()
   {
      return ControllerState.START;
   }

   protected void checkLastSimpleAliasInstall(ControllerContext context) throws Throwable
   {
      installAlias();
      assertEquals(context, assertContext("MyAlias"));
   }

   protected void buildMetaData() throws Throwable
   {
      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("OriginalBean", SimpleBeanImpl.class.getName());
      HashSet<PropertyMetaData> attributes1 = new HashSet<PropertyMetaData>();
      attributes1.add(new AbstractPropertyMetaData("string", "String1"));
      metaData1.setProperties(attributes1);
      setBeanMetaDatas(new BeanMetaData[]{metaData1});
   }

   protected void installAlias() throws Throwable
   {
      Controller controller = getUtil().getKernel().getController();
      controller.addAlias("MyAlias", "OriginalBean");
   }
}
