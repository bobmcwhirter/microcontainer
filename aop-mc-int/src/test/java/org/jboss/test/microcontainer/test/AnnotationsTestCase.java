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
import org.jboss.aop.microcontainer.annotations.DisabledType;

/**
 * Annotations test case.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class AnnotationsTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(AnnotationsTestCase.class);
   }

   public AnnotationsTestCase(String name)
   {
      super(name);
   }

   public void testDisabledType() throws Exception
   {
      DisabledType all = DisabledType.ALL;
      assertTrue(all.isDisabled(DisabledType.ALL));
      assertTrue(all.isDisabled(DisabledType.LIFECYCLE));
      assertTrue(all.isDisabled(DisabledType.POINTCUTS));
      assertFalse(all.isEnabled(DisabledType.ALL));
      assertFalse(all.isEnabled(DisabledType.LIFECYCLE));
      assertFalse(all.isEnabled(DisabledType.POINTCUTS));

      DisabledType lifecycle = DisabledType.LIFECYCLE;
      assertFalse(lifecycle.isDisabled(DisabledType.ALL));
      assertTrue(lifecycle.isDisabled(DisabledType.LIFECYCLE));
      assertFalse(lifecycle.isDisabled(DisabledType.POINTCUTS));
      assertTrue(lifecycle.isEnabled(DisabledType.ALL));
      assertFalse(lifecycle.isEnabled(DisabledType.LIFECYCLE));
      assertTrue(lifecycle.isEnabled(DisabledType.POINTCUTS));

      DisabledType pointcut = DisabledType.POINTCUTS;
      assertFalse(pointcut.isDisabled(DisabledType.ALL));
      assertFalse(pointcut.isDisabled(DisabledType.LIFECYCLE));
      assertTrue(pointcut.isDisabled(DisabledType.POINTCUTS));
      assertTrue(pointcut.isEnabled(DisabledType.ALL));
      assertTrue(pointcut.isEnabled(DisabledType.LIFECYCLE));
      assertFalse(pointcut.isEnabled(DisabledType.POINTCUTS));

      DisabledType[] three = {all, lifecycle, pointcut};
      assertTrue(DisabledType.isDisabled(three, DisabledType.ALL));
      assertTrue(DisabledType.isDisabled(three, DisabledType.LIFECYCLE));
      assertTrue(DisabledType.isDisabled(three, DisabledType.POINTCUTS));
      assertFalse(DisabledType.isEnabled(three, DisabledType.ALL));
      assertFalse(DisabledType.isEnabled(three, DisabledType.LIFECYCLE));
      assertFalse(DisabledType.isEnabled(three, DisabledType.POINTCUTS));

      DisabledType[] two = {all, lifecycle};
      assertTrue(DisabledType.isDisabled(two, DisabledType.ALL));
      assertTrue(DisabledType.isDisabled(two, DisabledType.LIFECYCLE));
      assertTrue(DisabledType.isDisabled(two, DisabledType.POINTCUTS));
      assertFalse(DisabledType.isEnabled(two, DisabledType.ALL));
      assertFalse(DisabledType.isEnabled(two, DisabledType.LIFECYCLE));
      assertFalse(DisabledType.isEnabled(two, DisabledType.POINTCUTS));
      two = new DisabledType[]{all, pointcut};
      assertTrue(DisabledType.isDisabled(two, DisabledType.ALL));
      assertTrue(DisabledType.isDisabled(two, DisabledType.LIFECYCLE));
      assertTrue(DisabledType.isDisabled(two, DisabledType.POINTCUTS));
      assertFalse(DisabledType.isEnabled(two, DisabledType.ALL));
      assertFalse(DisabledType.isEnabled(two, DisabledType.LIFECYCLE));
      assertFalse(DisabledType.isEnabled(two, DisabledType.POINTCUTS));
      two = new DisabledType[]{lifecycle, pointcut};
      assertFalse(DisabledType.isDisabled(two, DisabledType.ALL));
      assertTrue(DisabledType.isDisabled(two, DisabledType.LIFECYCLE));
      assertTrue(DisabledType.isDisabled(two, DisabledType.POINTCUTS));
      assertTrue(DisabledType.isEnabled(two, DisabledType.ALL));
      assertFalse(DisabledType.isEnabled(two, DisabledType.LIFECYCLE));
      assertFalse(DisabledType.isEnabled(two, DisabledType.POINTCUTS));
   }
}