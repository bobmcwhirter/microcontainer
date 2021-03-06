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
import java.util.Set;
import java.util.HashSet;
import java.util.List;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.factory.GenericBeanFactoryMetaData;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.Kernel;
import org.jboss.test.kernel.AbstractKernelTest;
import org.jboss.test.kernel.dependency.support.TestUtil;

/**
 * An abstract kernel config test.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractKernelDependencyTest extends AbstractKernelTest
{
   /** The test utility */
   private TestUtil util;
   
   /** Whether this is an xml test */
   private boolean xmltest;
   
   /** The BeanMetaDatas */
   private BeanMetaData[] beanMetaDatas;
   
   public AbstractKernelDependencyTest(String name) throws Throwable
   {
      this(name, false);
   }
   
   public AbstractKernelDependencyTest(String name, boolean xmltest) throws Throwable
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
      return assertInstall(number, name, ControllerState.INSTALLED);
   }

   protected ControllerContext assertInstall(int number, String name, ControllerState expected) throws Throwable
   {
      ControllerContext context = install(number, name);
      assertNotNull("Missing context with name: " + name, context);
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
   
   protected void addBeanProperty(GenericBeanFactoryMetaData factory, PropertyMetaData property)
   {
      Set<PropertyMetaData> propertys = factory.getProperties();
      if (propertys == null)
      {
         propertys = new HashSet<PropertyMetaData>();
         factory.setProperties(propertys);
      }
      propertys.add(property);
   }

   protected BeanMetaData getBeanMetaData(GenericBeanFactoryMetaData factory)
   {
      List<BeanMetaData> beans = factory.getBeans();
      assertNotNull(beans);
      assertEquals(1, beans.size());
      return beans.get(0);
   }

   protected void deploy(int number) throws Throwable
   {
      String name = getClass().getName();
      int dot = name.lastIndexOf('.');
      if (dot != -1)
         name = name.substring(dot + 1);
      name = name + number + ".xml";
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
