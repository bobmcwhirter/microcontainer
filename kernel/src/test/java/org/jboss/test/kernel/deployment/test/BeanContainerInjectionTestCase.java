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
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.kernel.spi.annotations.AnnotationToBeanMetaDataFactory;
import org.jboss.kernel.spi.annotations.BeanMetaDataAnnotationAdapter;
import org.jboss.test.kernel.deployment.support.container.TestSessionBean;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BeanContainerInjectionTestCase extends AbstractDeploymentTest
{
   public BeanContainerInjectionTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BeanContainerInjectionTestCase.class);
   }

   public void testContainerInjection() throws Throwable
   {
      BeanMetaDataAnnotationAdapter adapter = assertBean("BMDAdapter", BeanMetaDataAnnotationAdapter.class);
      BeanMetaData bmd = AnnotationToBeanMetaDataFactory.createBeanMetaData(TestSessionBean.class, BeanAccessMode.ALL, adapter);
      bmd.setName("test");
      SecurityManager sm = suspendSecurity();
      try
      {
         deploy(bmd);
      }
      finally
      {
         resumeSecurity(sm);   
      }
      TestSessionBean tsb = assertBean("test", TestSessionBean.class);
      tsb.validate();
   }
}
