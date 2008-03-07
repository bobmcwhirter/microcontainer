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
import java.util.HashSet;
import java.util.Arrays;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractInjectionValueMetaData;
import org.jboss.beans.metadata.api.enums.FromContext;
import org.jboss.beans.metadata.plugins.AbstractPropertyMetaData;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactoryMetaData;
import org.jboss.beans.metadata.plugins.builder.BeanMetaDataBuilderFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.builder.BeanMetaDataBuilder;
import org.jboss.beans.metadata.spi.factory.BeanFactory;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.test.kernel.deployment.support.NameAwareBean;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class FromContextTestCase extends AbstractKernelDependencyTest
{
   private boolean isXML;

   public FromContextTestCase(String name)
         throws Throwable
   {
      super(name);
   }

   public FromContextTestCase(String name, boolean xmltest)
         throws Throwable
   {
      super(name, xmltest);
      isXML = xmltest;
   }

   public static Test suite()
   {
      return suite(FromContextTestCase.class);
   }

   protected Object getBean(int number, String name) throws Throwable
   {
      ControllerContext context;
      if (isXML)
         context = assertContext(name);
      else
         context = assertInstall(number, name);
      assertNotNull(context);
      return context.getTarget();
   }

   public void testNameInjection() throws Throwable
   {
      setBeanMetaDatas();

      NameAwareBean nsb = (NameAwareBean)getBean(0, "set_name_bean");
      assertNotNull(nsb);
      assertEquals("set_name_bean", nsb.getName());

      BeanFactory nsf = (BeanFactory)getBean(1, "set_name_factory");
      assertNotNull(nsf);
      NameAwareBean b3 = (NameAwareBean)nsf.createBean();
      assertNotNull(b3);
      assertEquals("set_name_factory", b3.getName());
      NameAwareBean b4 = (NameAwareBean)nsf.createBean();
      assertNotNull(b4);
      assertEquals("set_name_factory", b4.getName());

      NameAwareBean alias = (NameAwareBean)getBean(2, "aliases");
      assertNotNull(alias);
      Set<Object> aliases = alias.getAliases();
      checkAliases(aliases);

      NameAwareBean metadata = (NameAwareBean)getBean(3, "metadata");
      assertNotNull(metadata);
      assertNotNull(metadata.getMetadata());

      NameAwareBean beaninfo = (NameAwareBean)getBean(4, "beaninfo");
      assertNotNull(beaninfo);
      assertNotNull(beaninfo.getBeaninfo());

      NameAwareBean scopekey = (NameAwareBean)getBean(5, "scopekey");
      assertNotNull(scopekey);
      assertNotNull(scopekey.getScopeKey());

      NameAwareBean other = (NameAwareBean)getBean(6, "other");
      assertNotNull(other);
      assertEquals("set_name_bean", other.getName());

      NameAwareBean context = (NameAwareBean)getBean(7, "context");
      assertNotNull(context);
      assertNotNull(context.getContext());

      NameAwareBean state = (NameAwareBean)getBean(8, "state");
      assertNotNull(state);
      assertEquals(ControllerState.INSTANTIATED, state.getState());
   }

   protected void checkAliases(Set<Object> aliases) throws Throwable
   {
      assertNotNull(aliases);
      assertFalse(aliases.isEmpty());
      assertTrue(aliases.contains("a1"));
      assertTrue(aliases.contains("a2"));
      assertTrue(aliases.contains("a3"));
   }

   protected void setBeanMetaDatas() throws Throwable
   {
      BeanMetaDataBuilder b1 = BeanMetaDataBuilderFactory.createBuilder("set_name_bean", NameAwareBean.class.getName());
      AbstractInjectionValueMetaData v1 = new AbstractInjectionValueMetaData();
      v1.setFromContext(FromContext.NAME);
      b1.addPropertyMetaData("name", v1);

      GenericBeanFactoryMetaData b2 = new GenericBeanFactoryMetaData("set_name_factory", NameAwareBean.class.getName());
      AbstractInjectionValueMetaData v2 = new AbstractInjectionValueMetaData();
      v2.setFromContext(FromContext.NAME);
      b2.addBeanProperty(new AbstractPropertyMetaData("name", v2));

      BeanMetaDataBuilder b3 = BeanMetaDataBuilderFactory.createBuilder("aliases", NameAwareBean.class.getName());
      Set<Object> aliases = new HashSet<Object>();
      aliases.addAll(Arrays.asList("a1", "a2", "a3"));
      b3.setAliases(aliases);
      AbstractInjectionValueMetaData v3 = new AbstractInjectionValueMetaData();
      v3.setFromContext(FromContext.ALIASES);
      b3.addPropertyMetaData("aliases", v3);

      BeanMetaDataBuilder b4 = BeanMetaDataBuilderFactory.createBuilder("metadata", NameAwareBean.class.getName());
      AbstractInjectionValueMetaData v4 = new AbstractInjectionValueMetaData();
      v4.setFromContext(FromContext.METADATA);
      b4.addPropertyMetaData("metadata", v4);

      BeanMetaDataBuilder b5 = BeanMetaDataBuilderFactory.createBuilder("beaninfo", NameAwareBean.class.getName());
      AbstractInjectionValueMetaData v5 = new AbstractInjectionValueMetaData();
      v5.setFromContext(FromContext.BEANINFO);
      b5.addPropertyMetaData("beaninfo", v5);

      BeanMetaDataBuilder b6 = BeanMetaDataBuilderFactory.createBuilder("scopekey", NameAwareBean.class.getName());
      AbstractInjectionValueMetaData v6 = new AbstractInjectionValueMetaData();
      v6.setFromContext(FromContext.SCOPE);
      b6.addPropertyMetaData("scopeKey", v6);

      BeanMetaDataBuilder b8 = BeanMetaDataBuilderFactory.createBuilder("other", NameAwareBean.class.getName());
      AbstractInjectionValueMetaData v8 = new AbstractInjectionValueMetaData();
      v8.setValue("set_name_bean");
      v8.setFromContext(FromContext.NAME);
      b8.addPropertyMetaData("name", v8);

      BeanMetaDataBuilder b9 = BeanMetaDataBuilderFactory.createBuilder("context", NameAwareBean.class.getName());
      AbstractInjectionValueMetaData v9 = new AbstractInjectionValueMetaData();
      v9.setFromContext(FromContext.CONTEXT);
      b9.addPropertyMetaData("context", v9);

      BeanMetaDataBuilder b10 = BeanMetaDataBuilderFactory.createBuilder("state", NameAwareBean.class.getName());
      AbstractInjectionValueMetaData v10 = new AbstractInjectionValueMetaData();
      v10.setFromContext(FromContext.STATE);
      b10.addPropertyMetaData("state", v10);

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
