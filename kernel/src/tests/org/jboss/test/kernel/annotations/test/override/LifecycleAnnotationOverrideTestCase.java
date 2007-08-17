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

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractLifecycleMetaData;
import org.jboss.test.kernel.annotations.support.AnnotationTester;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class LifecycleAnnotationOverrideTestCase extends AbstractAnnotationOverrideTestCase
{
   private Controller controller;
   private ControllerContext context;

   public LifecycleAnnotationOverrideTestCase(String name) throws Throwable
   {
      super(name);
   }

   public LifecycleAnnotationOverrideTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(LifecycleAnnotationOverrideTestCase.class);
   }

   protected String getType()
   {
      return "Lifecycle";
   }

   protected void addMetaData(AbstractBeanMetaData beanMetaData)
   {
      beanMetaData.setCreate(new AbstractLifecycleMetaData("fromXMLCreate"));
      beanMetaData.setStart(new AbstractLifecycleMetaData("fromXMLStart"));
      beanMetaData.setStop(new AbstractLifecycleMetaData("fromXMLStop"));
      beanMetaData.setDestroy(new AbstractLifecycleMetaData("fromXMLDestroy"));
   }

   public void testLifecycleOverride() throws Throwable
   {
      AnnotationTester tester = getTester();
      Object[] values = (Object[])tester.getValue();
      assertEquals(new Object[]{FROM_XML, FROM_XML, null, null}, values);

      doUndeploy();

      values = (Object[])tester.getValue();
      assertEquals(new Object[]{null, null, FROM_XML, FROM_XML}, values);
   }

   protected void afterInstall(KernelController controller, KernelControllerContext context)
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
