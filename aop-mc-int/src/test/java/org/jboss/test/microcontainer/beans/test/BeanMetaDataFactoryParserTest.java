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
package org.jboss.test.microcontainer.beans.test;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.xb.binding.Unmarshaller;
import org.jboss.xb.binding.UnmarshallerFactory;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBindingResolver;
import org.jboss.xb.binding.sunday.unmarshalling.SingletonSchemaResolverFactory;


/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class BeanMetaDataFactoryParserTest extends AOPMicrocontainerTest
{
   public BeanMetaDataFactoryParserTest(String name)
   {
      super(name);
   }

   public void testNamesFromGetBeansRemainsTheSame() throws Exception
   {
      List<BeanMetaDataFactory> beanMetaDataFactories = loadBeanMetaDataFactories();
      
      List<BeanMetaData> names1 = loadBeans(beanMetaDataFactories);
      assertTrue(10 < names1.size());
      List<BeanMetaData> names2 = loadBeans(beanMetaDataFactories);
      assertEquals(names1.size(), names2.size());
      
      for (int i = 0 ; i < names1.size() ; i++)
      {
         assertEquals("Wrong name for bean number " + i + " type1: " + names1.get(i).getBean() + " " + names2.get(i).getBean() , names1.get(i).getName(), names2.get(i).getName());
      }
      
   }

   private List<BeanMetaDataFactory> loadBeanMetaDataFactories() throws Exception
   {
      SchemaBindingResolver resolver = SingletonSchemaResolverFactory.getInstance().getSchemaBindingResolver();
      UnmarshallerFactory factory = UnmarshallerFactory.newInstance();

      Unmarshaller unmarshaller = factory.newUnmarshaller();
      URL url = getClass().getResource(getFileName());
      KernelDeployment deployment = (KernelDeployment) unmarshaller.unmarshal(url.toString(), resolver);
      return deployment.getBeanFactories();
   }
   
   private List<BeanMetaData> loadBeans(List<BeanMetaDataFactory> beanMetaDataFactories) throws Exception
   {
      List<BeanMetaData> beans = new ArrayList<BeanMetaData>(); 
      for (BeanMetaDataFactory bmdf : beanMetaDataFactories)
      {
         for (BeanMetaData bmd : bmdf.getBeans())
         {
            beans.add(bmd);
         }
      }
      return beans;
   }
   
   
   protected abstract String getFileName();
}
