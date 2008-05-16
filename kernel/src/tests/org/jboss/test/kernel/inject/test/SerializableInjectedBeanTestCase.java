/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.test.kernel.inject.test;

import java.rmi.MarshalledObject;

import junit.framework.Test;

import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.inject.support.SerializableInjectedBean;

/**
 * @author <a href="mailto:carlo.dewolf@jboss.com">Carlo de Wolf</a>
 * @version $Revision: $
 */
public class SerializableInjectedBeanTestCase extends AbstractManualInjectTest
{
   public SerializableInjectedBeanTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(SerializableInjectedBeanTestCase.class);
   }

   public void testInjectionValueMetaData() throws Throwable
   {
      KernelControllerContext context = getControllerContext("testObject1", ControllerState.NOT_INSTALLED);
      assertNull(context.getTarget());
      assertEquals(context.getState(), ControllerState.NOT_INSTALLED);
      
      change(context, ControllerState.INSTALLED);
      SerializableInjectedBean injectee = (SerializableInjectedBean)context.getTarget();
      assertNotNull(injectee);
      assertNotNull(injectee.getKernel());
      
      MarshalledObject<SerializableInjectedBean> mo = new MarshalledObject<SerializableInjectedBean>(injectee);
      SerializableInjectedBean unmarshalled = mo.get();
      
      assertNotNull(unmarshalled);
      assertNotNull("kernel property is not set", unmarshalled.getKernel());
   }

}
