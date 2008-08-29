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

import java.util.Collections;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.deployment.support.SimpleBeanImpl;

/**
 * Duplicate alias tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class DuplicateAliasTestCase extends AbstractKernelDependencyTest
{
   public DuplicateAliasTestCase(String name) throws Throwable
   {
      super(name);
   }

   protected DuplicateAliasTestCase(String name, boolean xmltest) throws Throwable
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(DuplicateAliasTestCase.class);
   }

   public void testDuplicate() throws Throwable
   {
      buildMetaData();

      assertInstall(0, "OriginalBean");
      try
      {
         assertInstall(1, "DuplicateBean", ControllerState.ERROR);
      }
      catch (Throwable t)
      {
         assertInstanceOf(t, getFailureCause());
      }
   }

   protected Class<? extends Throwable> getFailureCause()
   {
      return IllegalStateException.class;
   }

   protected void buildMetaData() throws Throwable
   {
      Object alias = "alias";

      AbstractBeanMetaData metaData1 = new AbstractBeanMetaData("OriginalBean", SimpleBeanImpl.class.getName());
      metaData1.setAliases(Collections.singleton(alias));
      addAnnotation(metaData1);

      AbstractBeanMetaData metaData2 = new AbstractBeanMetaData("DuplicateBean", SimpleBeanImpl.class.getName());
      metaData2.setAliases(Collections.singleton(alias));
      addAnnotation(metaData2);

      setBeanMetaDatas(new BeanMetaData[]{metaData1, metaData2});
   }

   protected void addAnnotation(AbstractBeanMetaData bmd)
   {
      // noop
   }
}