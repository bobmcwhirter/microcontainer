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
import org.jboss.beans.metadata.plugins.InstallCallbackMetaData;
import org.jboss.beans.metadata.plugins.UninstallCallbackMetaData;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.dependency.spi.Controller;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.annotations.support.AnnotationTester;
import org.jboss.test.kernel.annotations.support.MyDeployer;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class CallbackAnnotationOverrideTestCase extends AbstractAnnotationOverrideTestCase
{
   private Controller controller;

   public CallbackAnnotationOverrideTestCase(String name) throws Throwable
   {
      super(name);
   }

   public CallbackAnnotationOverrideTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(CallbackAnnotationOverrideTestCase.class);
   }

   protected String getType()
   {
      return "Callback";
   }

   protected void addMetaData(AbstractBeanMetaData beanMetaData)
   {
      List<CallbackMetaData> installs = new ArrayList<CallbackMetaData>();
      InstallCallbackMetaData in = new InstallCallbackMetaData();
      in.setMethodName("addMyDeployer");
      installs.add(in);
      List<CallbackMetaData> uninstalls = new ArrayList<CallbackMetaData>();
      UninstallCallbackMetaData un = new UninstallCallbackMetaData();
      un.setMethodName("removeMyDeployer");
      uninstalls.add(un);
      beanMetaData.setInstallCallbacks(installs);
      beanMetaData.setUninstallCallbacks(uninstalls);
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
      controller.install(new AbstractBeanMetaData("deployer", MyDeployer.class.getName()));
      this.controller = controller;
   }

   protected void doUndeploy()
   {
      controller.uninstall("deployer");
      controller = null;
   }
}
