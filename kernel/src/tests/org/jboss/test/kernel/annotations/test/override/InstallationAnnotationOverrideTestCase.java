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
package org.jboss.test.kernel.annotations.test.override;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.annotations.support.AnnotationTester;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class InstallationAnnotationOverrideTestCase extends AbstractAnnotationOverrideTestCase
{
   private Controller controller;
   private ControllerContext context;

   public InstallationAnnotationOverrideTestCase(String name) throws Throwable
   {
      super(name);
   }

   public InstallationAnnotationOverrideTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(InstallationAnnotationOverrideTestCase.class);
   }

   protected String getType()
   {
      return "Installation";
   }

   protected void addMetaData(AbstractBeanMetaData beanMetaData)
   {
      List<InstallMetaData> installs = new ArrayList<InstallMetaData>();
      AbstractInstallMetaData in = new AbstractInstallMetaData();
      in.setMethodName("addMyDeployer");
      installs.add(in);
      List<InstallMetaData> uninstalls = new ArrayList<InstallMetaData>();
      AbstractInstallMetaData un = new AbstractInstallMetaData();
      un.setMethodName("removeMyDeployer");
      uninstalls.add(un);
      beanMetaData.setInstalls(installs);
      beanMetaData.setUninstalls(uninstalls);
   }

   public void testCallbackOverride() throws Throwable
   {
      AnnotationTester tester = getTester();
      Object val = tester.getValue();
      assertInstanceOf(val, Integer.class);
      Integer count = (Integer)val;
      assertNotNull(count);
      assertEquals(1, count.intValue());

      doUndeploy();

      val = tester.getValue();
      assertInstanceOf(val, Integer.class);
      count = (Integer)val;
      assertNotNull(count);
      assertEquals(0, count.intValue());
   }

   protected void afterInstall(KernelController controller, KernelControllerContext context) throws Throwable
   {
      this.controller = controller;
      this.context = context;
   }

   protected void doUndeploy()
   {
      controller.uninstall(context.getName());
      controller = null;
      context = null;
   }
}
