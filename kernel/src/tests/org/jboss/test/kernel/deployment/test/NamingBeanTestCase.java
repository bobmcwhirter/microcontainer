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
package org.jboss.test.kernel.deployment.test;

import java.util.Set;

import junit.framework.Test;
import org.jboss.test.kernel.deployment.support.NameAwareBean;
import org.jboss.beans.metadata.spi.factory.BeanFactory;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.scope.ScopeKey;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class NamingBeanTestCase extends AbstractDeploymentTest
{
   public NamingBeanTestCase(String name)
         throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(NamingBeanTestCase.class);
   }

   public void testNameInjection() throws Throwable
   {
      NameAwareBean nsb = (NameAwareBean)getBean("set_name_bean");
      assertNotNull(nsb);
      assertEquals("set_name_bean", nsb.getName());

      BeanFactory nsf = (BeanFactory)getBean("set_name_factory");
      assertNotNull(nsf);
      NameAwareBean b3 = (NameAwareBean)nsf.createBean();
      assertNotNull(b3);
      assertEquals("set_name_factory", b3.getName());
      NameAwareBean b4 = (NameAwareBean)nsf.createBean();
      assertNotNull(b4);
      assertEquals("set_name_factory", b4.getName());

      NameAwareBean alias = (NameAwareBean)getBean("alias");
      assertNotNull(alias);
      Set<Object> aliases = alias.getAlias();
      assertNotNull(aliases);
      assertFalse(aliases.isEmpty());
      assertTrue(aliases.contains("a1"));
      assertTrue(aliases.contains("a2"));
      assertTrue(aliases.contains("a3"));      

      NameAwareBean metadata = (NameAwareBean)getBean("metadata");
      assertNotNull(metadata);
      assertNotNull(metadata.getMetadata());
      assertInstanceOf(metadata.getMetadata(), MetaData.class);

      NameAwareBean beaninfo = (NameAwareBean)getBean("beaninfo");
      assertNotNull(beaninfo);
      assertNotNull(beaninfo.getBeaninfo());
      assertInstanceOf(beaninfo.getBeaninfo(), BeanInfo.class);

      NameAwareBean scopekey = (NameAwareBean)getBean("scopekey");
      assertNotNull(scopekey);
      assertNotNull(scopekey.getScopeKey());
      assertInstanceOf(scopekey.getScopeKey(), ScopeKey.class);

      NameAwareBean dynamic = (NameAwareBean)getBean("dynamic");
      assertNotNull(dynamic);
      assertNotNull(dynamic.getDynamic());
      assertInstanceOf(dynamic.getDynamic(), BeanMetaData.class);

      NameAwareBean other = (NameAwareBean)getBean("other");
      assertNotNull(other);
      assertEquals("set_name_bean", other.getName());
   }
}
