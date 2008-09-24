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

import junit.framework.Test;
import org.jboss.beans.metadata.api.model.InjectOption;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.dependency.support.SimpleBean;
import org.jboss.test.kernel.dependency.support.SimpleBeanDelegate;
import org.jboss.test.kernel.dependency.support.SimpleBeanImpl;

/**
 * Optional tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class OptionalTestCase extends OldAbstractKernelDependencyTest
{
   public OptionalTestCase(String name) throws Throwable
   {
      super(name);
   }

   public OptionalTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(OptionalTestCase.class);
   }

   public void testOptionalCorrectOrder() throws Throwable
   {
      optionalCorrectOrder();

      ControllerContext context1 = assertInstall(0, "Name1");
      SimpleBeanDelegate repository = (SimpleBeanDelegate)context1.getTarget();
      assertNotNull(repository);
      assertNull(repository.getDelegate());

      ControllerContext context2 = assertInstall(1, "Name2");
      SimpleBean bean = (SimpleBean)context2.getTarget();
      assertNotNull(bean);

      // still null
      assertNull(repository.getDelegate());
   }

   protected void optionalCorrectOrder() throws Throwable
   {
      buildMetaData(buildRepository());
   }

   public void testOptionalWrongOrder() throws Throwable
   {
      optionalWrongOrder();

      ControllerContext context2 = assertInstall(1, "Name2");
      SimpleBean bean = (SimpleBean)context2.getTarget();
      assertNotNull(bean);

      ControllerContext context1 = assertInstall(0, "Name1");
      SimpleBeanDelegate repository = (SimpleBeanDelegate)context1.getTarget();
      assertNotNull(repository);
      assertSame(bean, repository.getDelegate());
   }

   protected void optionalWrongOrder() throws Throwable
   {
      buildMetaData(buildRepository());
   }

   public void testOptionalReinstall() throws Throwable
   {
      optionalReinstall();

      ControllerContext context1 = assertInstall(0, "Name1");
      SimpleBeanDelegate repository = (SimpleBeanDelegate)context1.getTarget();
      assertNotNull(repository);
      assertNull(repository.getDelegate());

      ControllerContext context2 = assertInstall(1, "Name2");
      SimpleBean bean = (SimpleBean)context2.getTarget();
      assertNotNull(bean);

      // still null
      assertNull(repository.getDelegate());

      assertUninstall("Name1");
      assertEquals(ControllerState.ERROR, context1.getState());

      context1 = assertInstall(0, "Name1");
      repository = (SimpleBeanDelegate)context1.getTarget();
      assertNotNull(repository);
      assertSame(bean, repository.getDelegate());

      assertUninstall("Name2");
      assertEquals(ControllerState.ERROR, context2.getState());
      // should be unwinded
      assertEquals(ControllerState.INSTANTIATED, context1.getState());            
      assertNull(repository.getDelegate());
   }

   protected void optionalReinstall() throws Throwable
   {
      buildMetaData(buildRepository());
   }

   protected void buildMetaData(BeanMetaData repository)
   {
      setBeanMetaDatas(new BeanMetaData[]{
            repository,
            new AbstractBeanMetaData("Name2", SimpleBeanImpl.class.getName()),
      });
   }

   protected AbstractBeanMetaData buildRepository()
   {
      AbstractBeanMetaData repository = new AbstractBeanMetaData("Name1", SimpleBeanDelegate.class.getName());
      AbstractInjectionValueMetaData vmd = new AbstractInjectionValueMetaData("Name2");
      vmd.setInjectionOption(InjectOption.OPTIONAL);
      repository.addProperty(new AbstractPropertyMetaData("delegate", vmd));
      return repository;
   }
}