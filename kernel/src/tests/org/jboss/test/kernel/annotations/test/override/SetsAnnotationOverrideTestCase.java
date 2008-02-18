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
package org.jboss.test.kernel.annotations.test.override;

import java.util.Collections;
import java.util.Set;
import java.util.HashSet;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDemandMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyMetaData;
import org.jboss.beans.metadata.plugins.AbstractSupplyMetaData;
import org.jboss.beans.metadata.spi.DemandMetaData;
import org.jboss.beans.metadata.spi.DependencyMetaData;
import org.jboss.beans.metadata.spi.SupplyMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.test.kernel.annotations.support.MyDeployer;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SetsAnnotationOverrideTestCase extends AbstractAnnotationOverrideTestCase
{
   private KernelControllerContext context;

   public SetsAnnotationOverrideTestCase(String name) throws Throwable
   {
      super(name);
   }

   public SetsAnnotationOverrideTestCase(String name, boolean xmltest)
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(SetsAnnotationOverrideTestCase.class);
   }

   protected String getType()
   {
      return "Sets";
   }

   protected void addMetaData(AbstractBeanMetaData beanMetaData)
   {
      Set<DemandMetaData> demands = Collections.singleton((DemandMetaData)new AbstractDemandMetaData("deployer"));
      beanMetaData.setDemands(new HashSet<DemandMetaData>(demands));

      Set<DependencyMetaData> depends = Collections.singleton((DependencyMetaData)new AbstractDependencyMetaData("deployer"));
      beanMetaData.setDepends(new HashSet<DependencyMetaData>(depends));

      Set<SupplyMetaData> supplys = Collections.singleton((SupplyMetaData)new AbstractSupplyMetaData("somesupply"));
      beanMetaData.setSupplies(new HashSet<SupplyMetaData>(supplys));
   }

   public void testSetsOverride() throws Throwable
   {
      getTester();
      BeanMetaData beanMetaData = getBeanMetaData();
      assertSet(beanMetaData.getDemands());
      assertSet(beanMetaData.getDepends());
      assertSet(beanMetaData.getSupplies());
   }

   protected BeanMetaData getBeanMetaData()
   {
      return context.getBeanMetaData();
   }

   protected void assertSet(Set<?> set)
   {
      assertNotNull(set);
      assertEquals(1, set.size());
   }

   protected void afterInstall(KernelController controller, KernelControllerContext context) throws Throwable
   {
      controller.install(new AbstractBeanMetaData("deployer", MyDeployer.class.getName()));
      this.context = context;
   }
}
