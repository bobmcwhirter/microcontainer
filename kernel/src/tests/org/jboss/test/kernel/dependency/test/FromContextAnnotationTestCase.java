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
package org.jboss.test.kernel.dependency.test;

import java.util.Set;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.api.model.FromContext;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.test.kernel.deployment.support.NameAwareBean;
import org.jboss.test.kernel.deployment.support.BeanNameAwareBean;
import org.jboss.test.kernel.deployment.support.AliasesAwareBean;
import org.jboss.test.kernel.deployment.support.MetaDataAwareBean;
import org.jboss.test.kernel.deployment.support.BeanInfoAwareBean;
import org.jboss.test.kernel.deployment.support.ScopeAwareBean;
import org.jboss.test.kernel.deployment.support.ContextAwareBean;
import org.jboss.test.kernel.deployment.support.OtherAwareBean;
import org.jboss.test.kernel.deployment.support.StateAwareBean;
import org.jboss.dependency.spi.ControllerContext;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class FromContextAnnotationTestCase extends FromContextTestCase
{
   public FromContextAnnotationTestCase(String name)
         throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(FromContextAnnotationTestCase.class);
   }

   protected void checkAliases(Set<Object> aliases) throws Throwable
   {
      assertNull(aliases);
      ControllerContext c1 = assertContext("a1");
      ControllerContext c2 = assertContext("a2");
      ControllerContext c3 = assertContext("a3");
      assertEquals(c1, c2);
      assertEquals(c2, c3);
   }

   @SuppressWarnings("deprecation")
   protected void setBeanMetaDatas() throws Throwable
   {
      BeanMetaDataBuilder b1 = BeanMetaDataBuilderFactory.createBuilder("set_name_bean", BeanNameAwareBean.class.getName());

      org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData b2 = new org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData("set_name_factory", NameAwareBean.class.getName());
      AbstractInjectionValueMetaData v2 = new AbstractInjectionValueMetaData();
      v2.setFromContext(FromContext.NAME);
      b2.addBeanProperty(new AbstractPropertyMetaData("name", v2));

      BeanMetaDataBuilder b3 = BeanMetaDataBuilderFactory.createBuilder("aliases", AliasesAwareBean.class.getName());
      BeanMetaDataBuilder b4 = BeanMetaDataBuilderFactory.createBuilder("metadata", MetaDataAwareBean.class.getName());
      BeanMetaDataBuilder b5 = BeanMetaDataBuilderFactory.createBuilder("beaninfo", BeanInfoAwareBean.class.getName());
      BeanMetaDataBuilder b6 = BeanMetaDataBuilderFactory.createBuilder("scopekey", ScopeAwareBean.class.getName());

      BeanMetaDataBuilder b8 = BeanMetaDataBuilderFactory.createBuilder("other", OtherAwareBean.class.getName());
      BeanMetaDataBuilder b9 = BeanMetaDataBuilderFactory.createBuilder("context", ContextAwareBean.class.getName());
      BeanMetaDataBuilder b10 = BeanMetaDataBuilderFactory.createBuilder("state", StateAwareBean.class.getName());

      setBeanMetaDatas(new BeanMetaData[]
            {
               b1.getBeanMetaData(),
               b2,
               b3.getBeanMetaData(),
               b4.getBeanMetaData(),
               b5.getBeanMetaData(),
               b6.getBeanMetaData(),
               b8.getBeanMetaData(),
               b9.getBeanMetaData(),
               b10.getBeanMetaData(),
            }
      );
   }
}
