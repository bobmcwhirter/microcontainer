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
/*
      SimpleObjectWithBean sowb1 = (SimpleObjectWithBean) getBean("SimpleObject1");
      ClassLoader cl = (ClassLoader) getBean("simple1");
      assertNotNull(sowb1);
      assertNotNull(cl);
      assertNotNull(sowb1.getClassLoader());

      SimpleObjectWithBean sowb2 = (SimpleObjectWithBean) getBean("SimpleObject2");
      SimpleBean simple2 = (SimpleBean) getBean("simple2");
      assertNotNull(sowb2);
      assertNotNull(simple2);
      assertNotNull(sowb2.getSimpleBean());

      SimpleObjectWithBean sowb3 = (SimpleObjectWithBean) getBean("SimpleObject3");
      SimpleBean simple3 = (SimpleBean) getBean("simple3");
      assertNotNull(sowb3);
      assertNotNull(simple3);
      assertNotNull(sowb3.getSimpleBean());

      SimpleObjectWithBean sowb4 = (SimpleObjectWithBean) getBean("SimpleObject4");
      SimpleBean simple4 = (SimpleBean) getBean("simple4");
      assertNotNull(sowb4);
      assertNotNull(simple4);
      assertNotNull(sowb4.getSimpleBean());
*/
   }

}
