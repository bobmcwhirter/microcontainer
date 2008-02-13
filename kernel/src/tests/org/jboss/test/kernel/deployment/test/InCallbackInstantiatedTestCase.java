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
package org.jboss.test.kernel.deployment.test;

import java.util.List;

import junit.framework.Test;

import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import org.jboss.test.kernel.deployment.support.InCallbackInstantiated;
import org.jboss.test.kernel.deployment.support.InCallbackInstantiatedRepository;
import org.jboss.test.kernel.deployment.support.WildcardClassLoader;

/**
 * Deployment ClassLoader Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 40742 $
 */
public class InCallbackInstantiatedTestCase extends AbstractDeploymentTest
{
   public static Test suite()
   {
      return suite(InCallbackInstantiatedTestCase.class);
   }

   public InCallbackInstantiatedTestCase(String name) throws Throwable
   {
      super(name);
   }

   public void testWildcardClassLoader() throws Throwable
   {
      InCallbackInstantiatedRepository repository = assertInstanceOf(getBean("Repository"), InCallbackInstantiatedRepository.class);
      KernelDeployment deployment = deploy("InCallbackInstantiatedTestCase_NotAutomatic.xml");
      try
      {
         validate();
         
         List<InCallbackInstantiated> registered = repository.registered;
         assertEquals(1, registered.size());
         InCallbackInstantiated bean = registered.get(0);
         assertEquals("Bean", bean.getConfigured());
      }
      finally
      {
         undeploy(deployment);
      }
   }
}