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
package org.jboss.test.kernel.dependency.test;

import junit.framework.Test;
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.test.kernel.dependency.support.SimplerBeanImpl2;
import org.jboss.test.AbstractTestDelegate;

/**
 * Property Field Dependency Test Case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
@SuppressWarnings("deprecation")
public class GenericBeanFactoryField2PropertyDependencyTestCase extends GenericBeanFactoryPropertyDependencyTestCase
{
   public static Test suite()
   {
      return suite(GenericBeanFactoryField2PropertyDependencyTestCase.class);
   }

   public GenericBeanFactoryField2PropertyDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   protected org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData createBeanFactory()
   {
      org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData beanFactoryMetaData = new org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData("Name2", SimplerBeanImpl2.class.getName());
      beanFactoryMetaData.setAccessMode(BeanAccessMode.ALL);
      return beanFactoryMetaData;
   }

   /**
    * Default setup with security manager disabled
    *
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      AbstractTestDelegate delegate = new AbstractTestDelegate(clazz);
      delegate.enableSecurity = false;
      return delegate;
   }
}
