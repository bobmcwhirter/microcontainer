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
import org.jboss.beans.metadata.plugins.AbstractConstructorMetaData;
import org.jboss.test.kernel.annotations.support.FactoryTesterCreator;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class FactoryAnnotationOverrideTestCase extends AbstractAnnotationOverrideTestCase
{
   public FactoryAnnotationOverrideTestCase(String name) throws Throwable
   {
      super(name);
   }

   public FactoryAnnotationOverrideTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(FactoryAnnotationOverrideTestCase.class);
   }

   protected String getType()
   {
      return "Factory";
   }

   protected void addMetaData(AbstractBeanMetaData beanMetaData)
   {
      AbstractConstructorMetaData constructor = new AbstractConstructorMetaData();
      constructor.setFactoryClass(FactoryTesterCreator.class.getName());
      constructor.setFactoryMethod("fromXML");
      beanMetaData.setConstructor(constructor);
   }

   public void testFactoryOverride() throws Throwable
   {
      checkOverride();
   }
}
