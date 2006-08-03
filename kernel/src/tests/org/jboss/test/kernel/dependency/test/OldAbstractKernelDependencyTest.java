/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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

import java.net.URL;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.test.kernel.dependency.support.TestUtil;
import org.jboss.util.Classes;

/**
 * An abstract kernel config test.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class OldAbstractKernelDependencyTest extends AbstractKernelTest
{
   /** The test utility */
   private TestUtil util;
   
   /** Whether this is an xml test */
   private boolean xmltest;
   
   /** The BeanMetaDatas */
   private BeanMetaData[] beanMetaDatas;
   
   public OldAbstractKernelDependencyTest(String name) throws Throwable
   {
      this(name, false);
   }
   
   public OldAbstractKernelDependencyTest(String name, boolean xmltest) throws Throwable
   {
      super(name);
      this.xmltest = xmltest;
   }

   protected void setUp() throws Exception
   {
      super.setUp();
      try
      {
         Kernel kernel = bootstrap();
         this.util = new TestUtil(kernel, this);
      }
      catch (Exception e)
      {
         throw e;
      }
      catch (Throwable t)
      {
         throw new RuntimeException(t);
      }
   }

   protected TestUtil getUtil()
   {
      return util;
   }
   
   protected ControllerContext install(int number, String name) throws Throwable
   {
      if (xmltest)
      {
         deploy(number);
         return util.getContext(name);
      }
      else
      {
         return util.install(beanMetaDatas[number]);
      }
   }

   protected void uninstall(String name) throws Throwable
   {
      util.uninstall(name);
   }
   
   protected ControllerContext assertInstall(int number, String name) throws Throwable
   {
      ControllerContext result = assertInstall(number, name, ControllerState.INSTALLED);
      assertContext(name);
      return result;
   }

   protected ControllerContext assertInstall(int number, String name, ControllerState expected) throws Throwable
   {
      ControllerContext context = install(number, name);
      assertNotNull(context);
      assertEquals(expected, context.getState());
      return context;
   }

   protected ControllerContext assertContext(String name) throws Throwable
   {
      return assertContext(name, ControllerState.INSTALLED);
   }

   protected ControllerContext assertContext(String name, ControllerState expected) throws Throwable
   {
      ControllerContext context = util.getContext(name);
      assertNotNull("Expected " + name + " state=" + expected.toShortString(), context);
      assertEquals(expected, context.getState());
      return context;
   }

   protected void assertUninstall(String name) throws Throwable
   {
      uninstall(name);
      
      assertNotInstalled(name);

      ControllerContext context = util.getContext(name);
      assertNull(context);
   }

   protected void assertNotInstalled(String name) throws Throwable
   {
      if (util.getInstalledContext(name) != null)
         fail("Should not be installed");
   }
   
   protected void setBeanMetaDatas(BeanMetaData[] beanMetaDatas)
   {
      if (xmltest)
         return;
      this.beanMetaDatas = beanMetaDatas;
   }
   
   protected void deploy(int number) throws Throwable
   {
      String packageName = Classes.getPackageName(getClass());
      packageName = packageName.replace('.', '/');
      String name = "/xml-test/" + packageName + '/' + getName() + number + ".xml";
      getLog().debug("Using " + name);
      URL url = getResource(name);
      if (url == null)
         throw new RuntimeException("Resource not found: " + name);
      getLog().debug("url=" + url);
      util.deploy(url);

   }
   
   protected void configureLoggingAfterBootstrap()
   {
      //enableTrace("org.jboss.beans");
      //enableTrace("org.jboss.dependency");
      //enableTrace("org.jboss.joinpoint.plugins.config");
      //enableTrace("org.jboss.kernel.plugins.config");
      //enableTrace("org.jboss.kernel.plugins.dependency");
      //if (xmltest)
      //   enableTrace("org.jboss.xb");
   }
}
