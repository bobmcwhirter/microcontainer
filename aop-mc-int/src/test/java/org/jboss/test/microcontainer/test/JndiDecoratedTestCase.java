/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import junit.framework.Test;

import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.jndi.SimpleBean;
import org.jboss.test.microcontainer.support.jndi.SimpleBeanAnnotatedImpl;

public class JndiDecoratedTestCase extends AOPMicrocontainerTest
{
   public JndiDecoratedTestCase(String name)
   {
      super(name);
   }
   public static Test suite()
   {
      return suite(JndiDecoratedTestCase.class);
   }

   /**
    * Validate that the 
    * @throws Exception
    */
   public void testJndi() throws Exception
   {
      super.enableTrace("org.jboss");
      Properties env = new Properties();
      env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.test.microcontainer.support.jndi.MockInitialContextFactory");
      InitialContext ctx = new InitialContext(env);

      SimpleBean bean0 = (SimpleBean) ctx.lookup("beans/SimpleBean0");
      assertNotNull(bean0);
      assertEquals("bean0.prop1", bean0.getProp1());
      SimpleBean alias0 = (SimpleBean) ctx.lookup("beans/XmlAnnotatedSimpleBean0");
      assertNotNull(alias0);
      assertEquals("bean0.prop1", alias0.getProp1());
      SimpleBeanAnnotatedImpl bean1 = (SimpleBeanAnnotatedImpl) ctx.lookup("beans/SimpleBean1");
      assertEquals("bean1.prop1", bean1.getProp1());
      SimpleBean alias1 = (SimpleBean) ctx.lookup("beans/AnnotatedSimpleBean1");
      assertNotNull(alias1);
      assertEquals("bean1.prop1", alias1.getProp1());
   }
   
}
