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

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.annotations.support.AnnotationTester;
import org.jboss.test.kernel.config.test.AbstractKernelConfigTest;
import org.jboss.test.kernel.config.support.XMLUtil;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class AbstractAnnotationOverrideTestCase extends AbstractKernelConfigTest
{
   protected static final String FROM_XML = "FromXML";

   protected AbstractAnnotationOverrideTestCase(String name) throws Throwable
   {
      super(name);
   }

   protected AbstractAnnotationOverrideTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   protected abstract void addMetaData(AbstractBeanMetaData beanMetaData);

   protected abstract String getType();

   protected String getClasName()
   {
      String name = AnnotationTester.class.getName();
      int p = name.lastIndexOf(".") + 1;
      return new StringBuilder(name).insert(p, getType()).toString();
   }

   protected Object getBean(String name, ControllerState state) throws Throwable
   {
      if (xmltest)
      {
         XMLUtil util = bootstrapXML(true);
         return util.getBean(name);
      }
      else
      {
         AbstractBeanMetaData bean = new AbstractBeanMetaData("Tester", getClasName());
         addMetaData(bean);
         return instantiate(bean, state);
      }
   }

   protected String getTesterName()
   {
      return "Tester";
   }

   protected AnnotationTester getTester() throws Throwable
   {
      return getTester(getTesterName());
   }

   protected AnnotationTester getTester(String name) throws Throwable
   {
      return getTester(name, null);     
   }

   protected AnnotationTester getTester(String name, ControllerState state) throws Throwable
   {
      Object tester = getBean(name, state);
      assertNotNull(tester);
      assertInstanceOf(tester, AnnotationTester.class, false);
      Class<?> clazz = Class.forName(getClasName());
      assertInstanceOf(tester, clazz);
      return (AnnotationTester)tester;
   }

   protected void assertOverride(Object value)
   {
      assertEquals("Should be from xml.", FROM_XML, value);
   }

   // simple check
   protected void checkOverride() throws Throwable
   {
      AnnotationTester tester = getTester();
      assertOverride(tester.getValue());
   }
}
