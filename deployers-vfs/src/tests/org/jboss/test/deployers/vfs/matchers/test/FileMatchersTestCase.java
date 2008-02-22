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
package org.jboss.test.deployers.vfs.matchers.test;

import java.util.Set;

import junit.framework.Test;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.plugins.main.MainDeployerImpl;
import org.jboss.deployers.spi.deployer.Deployers;
import org.jboss.deployers.vfs.plugins.structure.jar.JARStructure;
import org.jboss.deployers.vfs.spi.deployer.FileMatcher;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.test.deployers.vfs.matchers.support.ExposedFileStructure;
import org.jboss.test.deployers.vfs.matchers.support.FeedbackDeployer;
import org.jboss.test.deployers.vfs.matchers.support.TestBeanDeployer;
import org.jboss.test.deployers.vfs.matchers.support.TestBshDeployer;

/**
 * File matchers tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class FileMatchersTestCase extends KernelHolderDeployersTest
{
   public FileMatchersTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(FileMatchersTestCase.class);
   }

   public void testMatchers() throws Throwable
   {
      KernelController controller = getController();

      MainDeployerImpl main = new MainDeployerImpl();
      main.setStructuralDeployers(createStructuralDeployers());

      BeanMetaDataBuilder builder = BeanMetaDataBuilder.createBuilder("JARStructure", JARStructure.class.getName());
      controller.install(builder.getBeanMetaData());
      builder = BeanMetaDataBuilder.createBuilder("FileStructure", ExposedFileStructure.class.getName());
      controller.install(builder.getBeanMetaData());

      ControllerContext jarContext = controller.getInstalledContext("JARStructure");
      assertNotNull(jarContext);
      JARStructure jarStructure = (JARStructure)jarContext.getTarget();
      assertNotNull(jarStructure);
      addStructureDeployer(main, jarStructure);

      ControllerContext fileContext = controller.getInstalledContext("FileStructure");
      assertNotNull(fileContext);
      ExposedFileStructure fileStructure = (ExposedFileStructure)fileContext.getTarget();
      assertNotNull(fileStructure);
      addStructureDeployer(main, fileStructure);

      Set<FileMatcher> matchers = fileStructure.getMatchers();
      assertNotNull(matchers);
      assertEmpty(matchers);

      builder = BeanMetaDataBuilder.createBuilder("BeanDeployer", TestBeanDeployer.class.getName());
      controller.install(builder.getBeanMetaData());
      assertEquals(1, matchers.size());

      builder = BeanMetaDataBuilder.createBuilder("BshDeployer", TestBshDeployer.class.getName());
      controller.install(builder.getBeanMetaData());
      assertEquals(2, matchers.size());

      Deployers deployers = createDeployers();
      main.setDeployers(deployers);
      ControllerContext bdContext = controller.getInstalledContext("BeanDeployer");
      assertNotNull(bdContext);
      FeedbackDeployer beanDeployer = (FeedbackDeployer)bdContext.getTarget();
      assertNotNull(beanDeployer);
      addDeployer(main, beanDeployer);
      ControllerContext bsContext = controller.getInstalledContext("BshDeployer");
      assertNotNull(bsContext);
      FeedbackDeployer shellDeployer = (FeedbackDeployer)bsContext.getTarget();
      assertNotNull(shellDeployer);
      addDeployer(main, shellDeployer);

      Deployment deploymentBean = createDeployment("/matchers", "qwert.beans");
      assertDeploy(main, deploymentBean);

      Deployment deploymentShell = createDeployment("/matchers", "beanshell.jar");
      assertDeploy(main, deploymentShell);

      assertNotNull(beanDeployer.getFiles());
      assertEquals(new String[]{"some-beans.xml"}, beanDeployer.getFiles().toArray());
      assertNotNull(shellDeployer.getFiles());
      assertEquals(new String[]{"some.bsh"}, shellDeployer.getFiles().toArray());
   }
}
