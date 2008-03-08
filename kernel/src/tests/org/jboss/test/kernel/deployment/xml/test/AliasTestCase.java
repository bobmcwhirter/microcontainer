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
package org.jboss.test.kernel.deployment.xml.test;

import java.util.ArrayList;
import java.util.Set;

import junit.framework.Test;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.spi.AliasMetaData;
import org.jboss.beans.metadata.spi.NamedAliasMetaData;
import org.jboss.beans.metadata.spi.factory.GenericBeanFactoryMetaData;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;

/**
 * AliasTestCase.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class AliasTestCase extends AbstractXMLTest
{
   public AliasTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(AliasTestCase.class);
   }

   protected Object getAlias(String name) throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean(name);
      Set<AliasMetaData> aliases = bean.getAliasMetaData();
      assertNotNull(aliases);
      assertEquals(1, aliases.size());
      AliasMetaData alias = aliases.iterator().next();
      assertNotNull(alias);
      Object theAlias = alias.getAliasValue();
      assertNotNull(theAlias);
      return theAlias;
   }

   public void testAlias() throws Exception
   {
      Object alias = getAlias("Alias.xml");
      assertEquals("SimpleAlias", alias);
   }

   public void testAliasWithjavaBean() throws Exception
   {
      Object alias = getAlias("AliasWithJavaBean.xml");
      assertInstanceOf(alias, ArrayList.class);
   }

   public void testMultipleAliases() throws Exception
   {
      AbstractBeanMetaData bean = unmarshalBean("MultipleAlias.xml");
      Set<AliasMetaData> aliases = bean.getAliasMetaData();
      assertNotNull(aliases);
      int size = aliases.size();
      assertTrue(size > 1);
      for(AliasMetaData alias : aliases)
      {
         assertNotNull(alias);
         assertNotNull(alias.getAliasValue());
      }
   }

   public void testAliasAndBeanFactory() throws Exception
   {
      GenericBeanFactoryMetaData bean = unmarshalBeanFactory("AliasWithBeanFactory.xml");
      Set<AliasMetaData> aliases = bean.getAliases();
      assertNotNull(aliases);
      assertFalse(aliases.isEmpty());
      assertEquals("SimpleAliasWithBF", aliases.iterator().next().getAliasValue());
   }

   public void testAliasAndBeanFactoryJavaBean() throws Exception
   {
      GenericBeanFactoryMetaData bean = unmarshalBeanFactory("AliasWithBeanFactoryJavaBean.xml");
      Set<AliasMetaData> aliases = bean.getAliases();
      assertNotNull(aliases);
      assertFalse(aliases.isEmpty());
      assertInstanceOf(aliases.iterator().next().getAliasValue(), ArrayList.class);
   }

   protected NamedAliasMetaData getNamedAlias(String name) throws Exception
   {
      AbstractKernelDeployment deployment = unmarshalDeployment(name);
      Set<NamedAliasMetaData> aliases = deployment.getAliases();
      assertNotNull(aliases);
      assertEquals(1, aliases.size());
      NamedAliasMetaData alias = aliases.iterator().next();
      assertNotNull(alias);
      return alias;
   }
}
