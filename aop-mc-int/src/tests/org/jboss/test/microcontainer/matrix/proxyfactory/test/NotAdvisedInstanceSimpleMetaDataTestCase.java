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
package org.jboss.test.microcontainer.matrix.proxyfactory.test;


import junit.framework.Test;

import org.jboss.aop.Advised;
import org.jboss.aop.metadata.SimpleMetaData;
import org.jboss.aop.proxy.container.AspectManaged;
import org.jboss.test.aop.junit.AbstractProxyTest;
import org.jboss.test.microcontainer.matrix.Base;
import org.jboss.test.microcontainer.matrix.DynamicAspectDeployer;
import org.jboss.test.microcontainer.matrix.TestInterceptor;

/**
 * No MC counterpart since there is no way to create SimpleMetaData via MC
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class NotAdvisedInstanceSimpleMetaDataTestCase extends AbstractProxyTest
{
   public void testSimpleMetaData() throws Exception
   {
      Base base = new Base();
      SimpleMetaData metaData = new SimpleMetaData();
      metaData.addMetaData("Simple", "MetaData", "Value");
      Object proxy = createProxy(base, null, metaData);
      assertFalse(proxy instanceof Advised);
      assertTrue(proxy instanceof AspectManaged);

      //Not the main purpose of the test but being paranoid never hurt
      TestInterceptor.reset();
      ((Base)proxy).baseOnly();
      assertEquals(0, TestInterceptor.interceptions);
      
      String name = DynamicAspectDeployer.addBinding("execution(* org.jboss.test.microcontainer.matrix.Base->*(..))", TestInterceptor.class);
      TestInterceptor.reset();
      ((Base)proxy).baseOnly();
      assertEquals(1, TestInterceptor.interceptions);
      
      DynamicAspectDeployer.removeBinding(name);
      TestInterceptor.reset();
      ((Base)proxy).baseOnly();
      assertEquals(0, TestInterceptor.interceptions);      
   }
   
   public static Test suite()
   {
      return suite(NotAdvisedInstanceSimpleMetaDataTestCase.class);
   }

   public NotAdvisedInstanceSimpleMetaDataTestCase(String name)
   {
      super(name);
   }
}
