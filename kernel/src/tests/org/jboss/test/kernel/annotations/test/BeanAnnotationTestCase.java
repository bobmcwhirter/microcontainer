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
package org.jboss.test.kernel.annotations.test;

import junit.framework.Test;
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.api.model.AutowireType;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ErrorHandlingMode;
import org.jboss.kernel.spi.annotations.AnnotationToBeanMetaDataFactory;
import org.jboss.test.kernel.annotations.support.BeanAnnotationHolder;
import org.jboss.test.kernel.config.test.AbstractKernelConfigTest;

/**
 * BeanAnnotationTestCase.
 * 
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class BeanAnnotationTestCase extends AbstractKernelConfigTest
{
   public BeanAnnotationTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(BeanAnnotationTestCase.class);
   }

   public void testInitialValues() throws Throwable
   {
      SecurityManager sm = suspendSecurity();
      try
      {
         BeanMetaData bmd = AnnotationToBeanMetaDataFactory.createBeanMetaData(BeanAnnotationHolder.class);
         assertEquals(AutowireType.CONSTRUCTOR, bmd.getAutowireType());
         assertEquals(ControllerMode.ASYNCHRONOUS, bmd.getMode());
         assertEquals(ErrorHandlingMode.MANUAL, bmd.getErrorHandlingMode());
         assertEquals(BeanAccessMode.STANDARD, bmd.getAccessMode()); // standard, since we set it in A2BMD
      }
      finally
      {
         resumeSecurity(sm);
      }
   }

   public void testOverriddenValues() throws Throwable
   {
      SecurityManager sm = suspendSecurity();
      try
      {
         AbstractBeanMetaData abmd = new AbstractBeanMetaData();
         abmd.setAutowireType(AutowireType.BY_NAME);
         abmd.setMode(ControllerMode.DISABLED);
         abmd.setErrorHandlingMode(ErrorHandlingMode.DISCARD);
         abmd.setAccessMode(BeanAccessMode.FIELDS);

         BeanMetaData bmd = AnnotationToBeanMetaDataFactory.fillBeanMetaData(BeanAnnotationHolder.class, BeanAccessMode.FIELDS, abmd);
         assertEquals(AutowireType.BY_NAME, bmd.getAutowireType());
         assertEquals(ControllerMode.DISABLED, bmd.getMode());
         assertEquals(ErrorHandlingMode.DISCARD, bmd.getErrorHandlingMode());
         assertEquals(BeanAccessMode.FIELDS, bmd.getAccessMode());
      }
      finally
      {
         resumeSecurity(sm);
      }
   }
}
