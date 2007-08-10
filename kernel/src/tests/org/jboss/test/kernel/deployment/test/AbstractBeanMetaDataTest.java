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
package org.jboss.test.kernel.deployment.test;

import java.util.List;

import org.jboss.test.kernel.deployment.support.SimpleBean;
import org.jboss.test.kernel.deployment.support.SimpleObjectWithBean;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractBeanMetaDataTest extends AbstractDeploymentTest
{
   protected AbstractBeanMetaDataTest(String name)
         throws Throwable
   {
      super(name);
   }

   // ---- tests

   public void doInnerBeanTests() throws Throwable
   {
      SimpleObjectWithBean sowb1 = (SimpleObjectWithBean) getBean("SimpleObject1");
      assertNotNull(sowb1);
      SimpleObjectWithBean simple1 = (SimpleObjectWithBean) getBean("simple1");
      assertNotNull(simple1);
      assertEquals(sowb1, simple1);

      SimpleObjectWithBean sowb2 = (SimpleObjectWithBean) getBean("SimpleObject2");
      assertNotNull(sowb2);
      SimpleBean simple2 = (SimpleBean) getBean("simple2");
      assertNotNull(simple2);
      assertNotNull(sowb2.getSimpleBean());
      assertEquals(sowb2.getSimpleBean(), simple2);

      SimpleObjectWithBean sowb3 = (SimpleObjectWithBean) getBean("SimpleObject3");
      assertNotNull(sowb3);
      SimpleBean simple3 = (SimpleBean) getBean("simple3");
      assertNotNull(simple3);
      assertNotNull(sowb3.getSimpleBean());
      assertEquals(sowb3.getSimpleBean(), simple3);

      SimpleObjectWithBean sowb4 = (SimpleObjectWithBean) getBean("SimpleObject4");
      assertNotNull(sowb4);
      List<SimpleBean> beans = sowb4.getBeans();
      assertFalse(beans.isEmpty());
      assertEquals(2, beans.size());

      SimpleObjectWithBean sowb6 = (SimpleObjectWithBean) getBean("SimpleObject6");
      assertNotNull(sowb6);
      List<SimpleBean> beans6 = sowb6.getBeans();
      assertFalse(beans6.isEmpty());
      assertEquals(2, beans6.size());
      SimpleBean inner61 = beans6.get(0);
      assertNotNull(inner61);
      SimpleBean inner71 = inner61.getBean();
      assertNotNull(inner71);
      assertEquals("InnerBean71", inner71.getString());
      SimpleBean inner62 = beans6.get(1);
      assertNotNull(inner62);
      SimpleBean inner72 = inner62.getBean();
      assertNotNull(inner72);
      assertEquals("InnerBean72", inner72.getString());
   }

}
