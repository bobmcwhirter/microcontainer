/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.kernel.deployment.test;

import java.util.Collections;
import java.util.List;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.kernel.deployment.support.AliasesImpl;
import org.jboss.test.kernel.deployment.support.ApplicationScopeImpl;
import org.jboss.test.kernel.deployment.support.SimpleBeanImpl;
import org.jboss.test.kernel.deployment.support.SimpleObjectWithBean;

/**
 * Scoping dependency tests.
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @version $Revision: 1.1 $
 */
public class ScopingDependencyTestCase extends ScopingDeploymentTest
{
   private final static String PARENT_CLASS = SimpleBeanImpl.class.getName();

   private final static String CHILD_CLASS = SimpleObjectWithBean.class.getName();

   private final static String PARENT = "Parent";

   private final static String CHILD = "Child";

   private final static String SCOPED_PARENT = "Scoped_Parent";

   private final static String APP_SCOPE_1 = "scoped1";

   private final static String APP_SCOPE_2 = "scoped2";

   public ScopingDependencyTestCase(String name) throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(ScopingDependencyTestCase.class);
   }

   public void testScopedChildDependencyOnParent() throws Throwable
   {
      KernelDeployment childDeployment = createChildDeployment(CHILD, PARENT, APP_SCOPE_1, null);
      deploy(childDeployment);

      try
      {
         assertNoSuchContext(CHILD);

         KernelDeployment parentDeployment = createParentDeployment();
         deploy(parentDeployment);

         try
         {
            ControllerContext parent = getControllerContext(PARENT);
            ControllerContext child = getControllerContext(CHILD);
            assertNotSame(parent.getController(), child.getController());
         }
         finally
         {
            undeploy(parentDeployment);
         }
         assertNoSuchContext(PARENT);
         assertNoSuchContext(CHILD);
      }
      finally
      {
         undeploy(childDeployment);
      }
   }

   public void testScopedChildWithDependencyOnAliasInScopedControllerWithSameNameAsParent() throws Throwable
   {
      KernelDeployment childDeployment = createChildDeployment(CHILD, PARENT, APP_SCOPE_1, null);
      deploy(childDeployment);

      try
      {
         assertNoSuchContext(CHILD);

         KernelDeployment parentInChildScopeDeployment =  createDeployment(SCOPED_PARENT, PARENT_CLASS, null, APP_SCOPE_1, PARENT);

         deploy(parentInChildScopeDeployment);
         try
         {
            ControllerContext scopedParent = getControllerContext(SCOPED_PARENT);
            ControllerContext child = getControllerContext(CHILD);
            assertSame(scopedParent.getController(), child.getController());

            KernelDeployment parentDeployment = createParentDeployment();
            deploy(parentDeployment);
            try
            {
               ControllerContext parent = getControllerContext(PARENT);
               assertNotSame(parent.getController(), child.getController());
            }
            finally
            {
               undeploy(parentDeployment);
            }
            scopedParent = getControllerContext(SCOPED_PARENT);
            child = getControllerContext(CHILD);
         }
         finally
         {
            undeploy(parentInChildScopeDeployment);
         }
         assertNoSuchContext(CHILD);
      }
      finally
      {
         undeploy(childDeployment);
      }
   }

   public void testScopedChildWithNoDependencyAndGlobalChildWithDependencyOnParent() throws Throwable
   {
      KernelDeployment childDeploymentNoDependencies = createChildDeployment(CHILD, null, APP_SCOPE_2, null);
      deploy(childDeploymentNoDependencies);
      try
      {
         List<KernelControllerContext> contextsNoDependencies = childDeploymentNoDependencies.getInstalledContexts();
         assertEquals(1, contextsNoDependencies.size());
         KernelControllerContext childNoDependencies = contextsNoDependencies.get(0);
         assertEquals(ControllerState.INSTALLED, childNoDependencies.getState());

         KernelDeployment childDeploymentWithDependencies = createChildDeployment(CHILD + 1, CHILD, PARENT, APP_SCOPE_1, null);
         deploy(childDeploymentWithDependencies);
         try
         {
            List<KernelControllerContext> contextsWithDependencies = childDeploymentWithDependencies.getInstalledContexts();
            assertEquals(1, contextsWithDependencies.size());
            KernelControllerContext childWithDependencies = contextsWithDependencies.get(0);
            assertFalse(ControllerState.INSTALLED.equals(childWithDependencies.getState()));
            assertNotSame(childNoDependencies.getController(), childWithDependencies.getController());

            KernelDeployment parentDeployment = createParentDeployment();
            deploy(parentDeployment);
            try
            {
               ControllerContext parent = getControllerContext(PARENT);
               ControllerContext child = getControllerContext(CHILD);
               assertNotSame(parent.getController(), child.getController());
               assertNotSame(parent.getController(), childNoDependencies.getController());
               assertNotSame(parent.getController(), childWithDependencies.getController());
               assertEquals(ControllerState.INSTALLED, childNoDependencies.getState());
            }
            finally
            {
               undeploy(parentDeployment);
            }
            assertNoSuchContext(PARENT);
            assertFalse(ControllerState.INSTALLED.equals(childWithDependencies.getState()));
            // unwinded due to the same name
            assertFalse(ControllerState.INSTALLED.equals(childNoDependencies.getState()));
         }
         finally
         {
            undeploy(childDeploymentWithDependencies);
         }
      }
      finally
      {
         undeploy(childDeploymentNoDependencies);
      }
   }

   public void testScopedChildWithNoDependencyAndGlobalChildWithDependencyOnParentViaAliases() throws Throwable
   {
      KernelDeployment childDeploymentNoDependencies = createChildDeployment(CHILD + 1, null, APP_SCOPE_2, CHILD);
      deploy(childDeploymentNoDependencies);
      try
      {
         List<KernelControllerContext> contextsNoDependencies = childDeploymentNoDependencies.getInstalledContexts();
         assertEquals(1, contextsNoDependencies.size());
         KernelControllerContext childNoDependencies = contextsNoDependencies.get(0);
         assertEquals(ControllerState.INSTALLED, childNoDependencies.getState());

         KernelDeployment childDeploymentWithDependencies = createChildDeployment(CHILD + 2, CHILD + 2, PARENT, APP_SCOPE_1, CHILD);
         deploy(childDeploymentWithDependencies);
         try
         {
            List<KernelControllerContext> contextsWithDependencies = childDeploymentWithDependencies.getInstalledContexts();
            assertEquals(1, contextsWithDependencies.size());
            KernelControllerContext childWithDependencies = contextsWithDependencies.get(0);
            assertFalse(ControllerState.INSTALLED.equals(childWithDependencies.getState()));
            assertNotSame(childNoDependencies.getController(), childWithDependencies.getController());

            KernelDeployment parentDeployment = createParentDeployment();
            deploy(parentDeployment);
            try
            {
               ControllerContext parent = getControllerContext(PARENT);
               ControllerContext child = getControllerContext(CHILD);
               assertNotSame(parent.getController(), child.getController());
               assertNotSame(parent.getController(), childNoDependencies.getController());
               assertNotSame(parent.getController(), childWithDependencies.getController());
               assertEquals(ControllerState.INSTALLED, childNoDependencies.getState());
            }
            finally
            {
               undeploy(parentDeployment);
            }
            assertNoSuchContext(PARENT);
            assertFalse(ControllerState.INSTALLED.equals(childWithDependencies.getState()));
            assertEquals(ControllerState.INSTALLED, childNoDependencies.getState());
         }
         finally
         {
            undeploy(childDeploymentWithDependencies);
         }
      }
      finally
      {
         undeploy(childDeploymentNoDependencies);
      }
   }

   public void testTwoScopedChildrenOneWithDependencyOnParent() throws Throwable
   {
      runTestTwoScopedChildrenWithDependencyOnParent(false);
   }

   public void testTwoScopedChildrenOneWithDependencyOnParentReverseOrder() throws Throwable
   {
      runTestTwoScopedChildrenWithDependencyOnParent(true);
   }

   private void runTestTwoScopedChildrenWithDependencyOnParent(boolean withDependencyFirst) throws Throwable
   {
      //TODO this will not work until Abstract
   }

   private KernelDeployment createParentDeployment() throws Throwable
   {
      return createDeployment(PARENT, PARENT_CLASS, null, null, null);
   }

   private KernelDeployment createChildDeployment(String beanName, String dependency, String scope, String alias)
   {
      return createChildDeployment(beanName, beanName, dependency, scope, alias);
   }

   private KernelDeployment createChildDeployment(String deploymentName, String beanName, String dependency, String scope, String alias)
   {
      return createDeployment(deploymentName, beanName, CHILD_CLASS, dependency, scope, alias);
   }

   private KernelDeployment createDeployment(String beanName, String clazz, String dependency, String scope, String alias)
   {
      return createDeployment(beanName, beanName, clazz, dependency, scope, alias);
   }

   @SuppressWarnings({"deprecation"})
   private KernelDeployment createDeployment(String deploymentName, String beanName, String clazz, String dependency, String scope, String alias)
   {
      BeanMetaDataBuilder builder = BeanMetaDataBuilderFactory.createBuilder(beanName, clazz);
      if (dependency != null)
      {
         ValueMetaData inject = builder.createInject(dependency);
         builder.addPropertyMetaData("simpleBean", inject);
      }
      if (scope != null)
      {
         builder.addAnnotation(new ApplicationScopeImpl(scope));
      }
      if (alias != null)
      {
         builder.addAnnotation(new AliasesImpl(alias));
      }

      AbstractKernelDeployment deployment = new AbstractKernelDeployment();
      deployment.setName(deploymentName);
      List<BeanMetaData> beans = Collections.singletonList(builder.getBeanMetaData());
      deployment.setBeans(beans);
      return deployment;
   }

   /**
    * assertNoControllerContext() never fails for scoped beans whether installed or not, so use this instead
    */
   private void assertNoSuchContext(String name)
   {
      try
      {
         getControllerContext(name);
         fail(name + " should not be found");
      }
      catch (IllegalStateException e)
      {
      }
   }
}
