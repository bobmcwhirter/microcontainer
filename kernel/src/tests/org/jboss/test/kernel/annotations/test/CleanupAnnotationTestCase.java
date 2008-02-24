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
import org.jboss.kernel.plugins.annotations.BeanAnnotationAdapter;
import org.jboss.test.kernel.annotations.support.TestAnnotationAdapter;
import org.jboss.test.kernel.annotations.support.TestCleanupOnlyPlugin;
import org.jboss.test.kernel.annotations.support.TestCleanupPlugin;
import org.jboss.test.kernel.annotations.support.TestApplyOnlyPlugin;
import org.jboss.test.kernel.annotations.support.TestCleanupBean;
import org.jboss.test.kernel.annotations.support.TestCleanupOnlyBean;
import org.jboss.test.kernel.annotations.support.TestApplyOnlyBean;

/**
 * Test the @Cleanup and @CleanupOnly
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class CleanupAnnotationTestCase extends AbstractBeanAnnotationAdapterTest
{
   public CleanupAnnotationTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(CleanupAnnotationTestCase.class);
   }

   protected BeanAnnotationAdapter getBeanAnnotationAdapterClass()
   {
      return TestAnnotationAdapter.INSTANCE;
   }

   protected void reset()
   {
      TestCleanupPlugin.INSTANCE.reset();
      TestCleanupOnlyPlugin.INSTANCE.reset();
      TestApplyOnlyPlugin.INSTANCE.reset();
   }

   public void testCleanup() throws Throwable
   {
      reset();
      runAnnotationsOnTarget(new TestCleanupBean());
      assertTrue(TestCleanupPlugin.INSTANCE.isApplied());
      assertTrue(TestCleanupPlugin.INSTANCE.isCleaned());
      assertFalse(TestCleanupOnlyPlugin.INSTANCE.isApplied());
      assertFalse(TestCleanupOnlyPlugin.INSTANCE.isCleaned());
      assertFalse(TestApplyOnlyPlugin.INSTANCE.isApplied());
      assertFalse(TestApplyOnlyPlugin.INSTANCE.isCleaned());
   }

   public void testCleanupOnly() throws Throwable
   {
      reset();
      runAnnotationsOnTarget(new TestCleanupOnlyBean());
      assertFalse(TestCleanupPlugin.INSTANCE.isApplied());
      assertFalse(TestCleanupPlugin.INSTANCE.isCleaned());
      assertFalse(TestCleanupOnlyPlugin.INSTANCE.isApplied());
      assertTrue(TestCleanupOnlyPlugin.INSTANCE.isCleaned());
      assertFalse(TestApplyOnlyPlugin.INSTANCE.isApplied());
      assertFalse(TestApplyOnlyPlugin.INSTANCE.isCleaned());
   }

   public void testApplyOnly() throws Throwable
   {
      reset();
      runAnnotationsOnTarget(new TestApplyOnlyBean());
      assertFalse(TestCleanupPlugin.INSTANCE.isApplied());
      assertFalse(TestCleanupPlugin.INSTANCE.isCleaned());
      assertFalse(TestCleanupOnlyPlugin.INSTANCE.isApplied());
      assertFalse(TestCleanupOnlyPlugin.INSTANCE.isCleaned());
      assertTrue(TestApplyOnlyPlugin.INSTANCE.isApplied());
      assertFalse(TestApplyOnlyPlugin.INSTANCE.isCleaned());
   }
}
