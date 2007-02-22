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

import junit.framework.Test;
import org.jboss.test.kernel.deployment.support.SimpleBean;
import org.jboss.test.kernel.deployment.support.SimpleObjectWithBean;

/**
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
public class BeanMetaDataWithClassloaderTestCase extends AbstractBeanMetaDataTest
{
   public BeanMetaDataWithClassloaderTestCase(String name)
         throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BeanMetaDataWithClassloaderTestCase.class);
   }

   // ---- tests

   public void testBeanAsValueMetaData() throws Throwable
   {
      ClassLoader cl = (ClassLoader) getBean("cl");
      assertNotNull(cl);

      doInnerBeanTests();

      ClassLoader cl2 = (ClassLoader) getBean("cl2");
      assertNotNull(cl2);

      SimpleObjectWithBean sowb5 = (SimpleObjectWithBean) getBean("SimpleObject5");
      assertNotNull(sowb5);
      SimpleBean simple5 = (SimpleBean) getBean("simple5");
      assertNotNull(simple5);
      assertNotNull(sowb5.getSimpleBean());
      assertEquals(sowb5.getSimpleBean(), simple5);
   }

}
