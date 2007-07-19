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
import java.util.HashSet;

import junit.framework.Test;
import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.MutableMetaData;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.test.kernel.deployment.support.NameAwareBean;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.reflect.spi.MethodInfo;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class FromContextUnsupportedTestCase extends AbstractDeploymentTest
{
   public FromContextUnsupportedTestCase(String name)
         throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(FromContextUnsupportedTestCase.class);
   }

   public void testUnsupportedInjection() throws Throwable
   {
      NameAwareBean alias = (NameAwareBean)getBean("aliases");
      assertNotNull(alias);
      Set<Object> aliases = alias.getAliases();
      assertNotNull(aliases);
      assertFalse(aliases.isEmpty());
      assertTrue(aliases.contains("a1"));
      assertTrue(aliases.contains("a2"));
      assertTrue(aliases.contains("a3"));
      try
      {
         aliases.add("failedAlias");
      }
      catch(Throwable t)
      {
         assertUnsupported(t);
      }

      NameAwareBean metadata = (NameAwareBean)getBean("metadata");
      assertNotNull(metadata);
      MetaData md = metadata.getMetadata();
      assertNotNull(md);
      assertFalse(md instanceof MutableMetaData);

      NameAwareBean beaninfo = (NameAwareBean)getBean("beaninfo");
      assertNotNull(beaninfo);
      BeanInfo info = beaninfo.getBeaninfo();
      assertNotNull(info);
      try
      {
         info.setMethods(new HashSet<MethodInfo>());
      }
      catch(Throwable t)
      {
         assertUnsupported(t);
      }

      NameAwareBean scopekey = (NameAwareBean)getBean("scopekey");
      assertNotNull(scopekey);
      ScopeKey key = scopekey.getScopeKey();
      assertNotNull(key);
      assertInstanceOf(key, ScopeKey.class);
      KernelControllerContext context = getControllerContext("scopekey");
      assertNotSame(key, context.getScope());

      NameAwareBean dynamic = (NameAwareBean)getBean("dynamic");
      assertNotNull(dynamic);
      Object dyna = dynamic.getDynamic();
      assertNotNull(dyna);
      assertInstanceOf(dyna, BeanMetaData.class);
      BeanMetaData bmd = (BeanMetaData)dyna;
      try
      {
         bmd.setName("failedName");   
      }
      catch(Throwable t)
      {
         assertUnsupported(t);
      }
   }

   protected void assertUnsupported(Throwable t)
   {
      assertInstanceOf(t, UnsupportedOperationException.class);
   }
}
