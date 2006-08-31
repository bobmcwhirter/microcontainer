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
package org.jboss.test.kernel.deployment.test;

import org.jboss.test.kernel.deployment.support.SimpleObjectWithBean;
import org.jboss.test.kernel.deployment.support.SimpleBean;

import junit.framework.Test;

/**
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
public class BeanMetaDataTestCase extends AbstractDeploymentTest
{
   public BeanMetaDataTestCase(String name)
         throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BeanMetaDataTestCase.class);
   }

   // ---- tests

   public void testBeanAsValueMetaData() throws Throwable
   {
      ClassLoader cl = (ClassLoader) getBean("cl");
      assertNotNull(cl);

      SimpleObjectWithBean sowb1 = (SimpleObjectWithBean) getBean("SimpleObject1");
      SimpleObjectWithBean simple1 = (SimpleObjectWithBean) getBean("simple1");
      assertNotNull(sowb1);
      assertNotNull(simple1);
      assertEquals(sowb1, simple1);

      SimpleObjectWithBean sowb2 = (SimpleObjectWithBean) getBean("SimpleObject2");
      SimpleBean simple2 = (SimpleBean) getBean("simple2");
      assertNotNull(sowb2);
      assertNotNull(simple2);
      assertNotNull(sowb2.getSimpleBean());
      assertEquals(sowb2.getSimpleBean(), simple2);

      SimpleObjectWithBean sowb3 = (SimpleObjectWithBean) getBean("SimpleObject3");
      SimpleBean simple3 = (SimpleBean) getBean("simple3");
      assertNotNull(sowb3);
      assertNotNull(simple3);
      assertNotNull(sowb3.getSimpleBean());
      assertEquals(sowb3.getSimpleBean(), simple3);
   }

}
