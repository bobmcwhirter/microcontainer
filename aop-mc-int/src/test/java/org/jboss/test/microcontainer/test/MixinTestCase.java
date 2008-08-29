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

import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.OtherMixin;
import org.jboss.test.microcontainer.support.OtherMixinImpl;
import org.jboss.test.microcontainer.support.SimpleBean;
import org.jboss.test.microcontainer.support.SomeMixin;
import org.jboss.test.microcontainer.support.SomeMixinImpl;

public class MixinTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(MixinTestCase.class);
   }
   
   public MixinTestCase(String name)
   {
      super(name);
   }
   
   public void testMixin() throws Exception
   {
      SimpleBean bean = (SimpleBean) getBean("Bean");

      assertTrue(bean instanceof SomeMixin);
      assertTrue(bean instanceof OtherMixin);
      
      SomeMixinImpl.invoked = false;
      OtherMixinImpl.invoked = false;
      ((SomeMixin)bean).someMixinMethod();
      assertTrue(SomeMixinImpl.invoked);
      assertFalse(OtherMixinImpl.invoked);
      
      
      SomeMixinImpl.invoked = false;
      OtherMixinImpl.invoked = false;
      ((OtherMixin)bean).otherMixinMethod();
      assertFalse(SomeMixinImpl.invoked);
      assertTrue(OtherMixinImpl.invoked);
   }
   
   
   public void testNoMixin() throws Exception
   {
      SimpleBean bean = (SimpleBean) getBean("Bean1");
      assertFalse(bean instanceof SomeMixin);
      assertFalse(bean instanceof OtherMixin);
   }
   
}
