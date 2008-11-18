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
package org.jboss.test.kernel.controller.test;

import java.security.AccessControlException;
import java.util.Collections;

import junit.framework.Test;

import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.factory.BeanFactory;
import org.jboss.beans.metadata.spi.factory.GenericBeanFactoryMetaData;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.test.kernel.controller.support.PrivilegedBean;

/**
 * AccessControl Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 38046 $
 */
public class GenericBeanFactoryAccessControlTestCase extends AbstractControllerTest
{
   public static Test suite()
   {
      return suite(GenericBeanFactoryAccessControlTestCase.class);
   }

   public GenericBeanFactoryAccessControlTestCase(String name) throws Throwable
   {
      super(name);
   }

   public void testPrivilegedBean() throws Throwable
   {
      BeanFactory factory = assertBean("Factory", BeanFactory.class);
      Object object = factory.createBean();
      
      ClassLoader cl = assertBean("ClassLoader", ClassLoader.class);
      assertEquals(cl, object.getClass().getClassLoader());
   }

   public void testUnPrivilegedBean() throws Throwable
   {
      GenericBeanFactoryMetaData gbfmd = new GenericBeanFactoryMetaData();
      gbfmd.setName("NonPrivileged");
      gbfmd.setBean(PrivilegedBean.class.getName());
      
      AbstractKernelDeployment deployment = new AbstractKernelDeployment();
      deployment.setName("test");
      deployment.setBeanFactories(Collections.singletonList((BeanMetaDataFactory) gbfmd));
      deploy(deployment);
      try
      {
         BeanFactory factory = assertBean("NonPrivileged", BeanFactory.class);
         try
         {
            factory.createBean();
            fail("Should not be here!");
         }
         catch (Throwable t)
         {
            checkThrowable(AccessControlException.class, t);
         }
      }
      finally
      {
         undeploy(deployment);
      }
   }
}