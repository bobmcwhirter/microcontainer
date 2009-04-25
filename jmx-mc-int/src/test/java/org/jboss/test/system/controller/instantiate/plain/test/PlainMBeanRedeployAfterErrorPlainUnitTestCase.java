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
package org.jboss.test.system.controller.instantiate.plain.test;

import javax.management.NotCompliantMBeanException;

import junit.framework.Test;

import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.system.controller.support.BrokenDynamicMBean;

/**
 * PlainMBeanPlainUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 82920 $
 */
public class PlainMBeanRedeployAfterErrorPlainUnitTestCase extends PlainMBeanTest
{
   public static Test suite()
   {
      return suite(PlainMBeanRedeployAfterErrorPlainUnitTestCase.class);
   }

   public PlainMBeanRedeployAfterErrorPlainUnitTestCase(String name)
   {
      super(name);
   }

   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      return getPlainControllerDelegate(clazz);
   }
   
   // The JDK MBeanServer eats the error message :-(
   public void testPlainMBeanGetMBeanInfoError() throws Exception
   {
      try
      {
         assertMaybeDeployFailure(BrokenDynamicMBean.OBJECT_NAME, NotCompliantMBeanException.class);
      }
      catch (Throwable t)
      {
         // in JDK6 we get different expected Throwable == Error
         assertInstanceOf(t, Error.class, false);
         assertEquals("BROKEN", t.getMessage());
      }
   }
}
