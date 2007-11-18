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
package org.jboss.test.deployers.vfs.structure.file.test;

import java.net.URI;

import junit.framework.Test;
import junit.framework.TestSuite;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.plugins.bootstrap.basic.BasicBootstrap;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.BaseTestCase;
import org.jboss.test.deployers.vfs.structure.file.support.BshFileMatcher;
import org.jboss.test.deployers.vfs.structure.file.support.TmpFileStructure;
import org.jboss.virtual.VFS;
import org.jboss.virtual.VirtualFile;

/**
 * FileMatcherTestCase.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class FileMatcherTestCase extends BaseTestCase
{
   public static Test suite()
   {
      return new TestSuite(FileMatcherTestCase.class);
   }

   public FileMatcherTestCase(String name)
   {
      super(name);
   }

   public void testMatcher() throws Throwable
   {
      BasicBootstrap bootstrap = new BasicBootstrap();
      bootstrap.run();
      Kernel kernel = bootstrap.getKernel();
      KernelController controller = kernel.getController();
      try
      {
         BeanMetaData fsMD = new AbstractBeanMetaData("fileStructure", TmpFileStructure.class.getName());
         KernelControllerContext fsCC = controller.install(fsMD);
         assertEquals(fsCC.getState(), ControllerState.INSTALLED);
         TmpFileStructure fs = (TmpFileStructure)fsCC.getTarget();
         assertNotNull(fs);

         VirtualFile file = VFS.getRoot(new URI("vfsmemory://somefile.bsh"));
         assertFalse(fs.checkFileMatchers(file));

         BeanMetaData fmMD = new AbstractBeanMetaData("bshFileMatcher", BshFileMatcher.class.getName());
         controller.install(fmMD);

         assertTrue(fs.checkFileMatchers(file));

         controller.uninstall(fmMD.getName());

         assertFalse(fs.checkFileMatchers(file));
      }
      finally
      {
         controller.shutdown();
      }
   }
}
