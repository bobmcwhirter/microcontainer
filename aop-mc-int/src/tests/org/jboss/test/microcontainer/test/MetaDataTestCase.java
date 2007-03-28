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
package org.jboss.test.microcontainer.test;

import junit.framework.Test;

import org.jboss.aop.microcontainer.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.MetaDataContextInterceptor;
import org.jboss.test.microcontainer.support.PropertyBean;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class MetaDataTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(MetaDataTestCase.class);
   }
   
   public MetaDataTestCase(String name)
   {
      super(name);
   }
   
   /**
    * Test that the two beans indeed have different metadata values
    * 
    * @throws Exception for any error
    */
   public void testBeanMetaData() throws Exception
   {
      PropertyBean bean = (PropertyBean)getBean("Bean");

      MetaDataContextInterceptor.reset();
      assertEquals(5, bean.getIntProperty());
      assertNotNull(MetaDataContextInterceptor.classAnnotation);
      assertNull(MetaDataContextInterceptor.joinpointAnnotation);

      MetaDataContextInterceptor.reset();
      assertEquals("Bean", bean.getStringProperty());
      assertNotNull(MetaDataContextInterceptor.classAnnotation);
      assertNull(MetaDataContextInterceptor.joinpointAnnotation);
   }
   
   /**
    * Test that the two beans indeed have different metadata values
    * 
    * @throws Exception for any error
    */
   public void testPropertyMetaData() throws Exception
   {
      
      PropertyBean bean1 = (PropertyBean)getBean("Bean1");

      MetaDataContextInterceptor.reset();
      assertEquals(10, bean1.getIntProperty());
      assertNull(MetaDataContextInterceptor.classAnnotation);
      assertNotNull(MetaDataContextInterceptor.joinpointAnnotation);
      
      MetaDataContextInterceptor.reset();
      assertEquals("Bean1", bean1.getStringProperty());
      assertNull(MetaDataContextInterceptor.classAnnotation);
      assertNull(MetaDataContextInterceptor.joinpointAnnotation);
   }
   
   /**
    * Test that the two beans indeed have different metadata values
    * 
    * @throws Exception for any error
    */
   public void testNoMetaData() throws Exception
   {
      
      PropertyBean bean1 = (PropertyBean)getBean("NoMetaDataBean");

      MetaDataContextInterceptor.reset();
      assertEquals(50, bean1.getIntProperty());
      assertNull(MetaDataContextInterceptor.classAnnotation);
      assertNull(MetaDataContextInterceptor.joinpointAnnotation);
      
      MetaDataContextInterceptor.reset();
      assertEquals("NoMetaDataBean", bean1.getStringProperty());
      assertNull(MetaDataContextInterceptor.classAnnotation);
      assertNull(MetaDataContextInterceptor.joinpointAnnotation);
   }
}
