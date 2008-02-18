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
package org.jboss.test.deployers.classloading.test;

import junit.framework.Test;
import org.jboss.deployers.client.spi.DeployerClient;
import org.jboss.deployers.client.spi.Deployment;
import org.jboss.deployers.spi.DeploymentException;
import org.jboss.deployers.structure.spi.classloading.ClassLoaderMetaData;
import org.jboss.deployers.structure.spi.classloading.VersionRange;
import org.jboss.deployers.structure.spi.classloading.helpers.VersionImpl;
import org.jboss.test.deployers.classloading.support.a.A;
import org.jboss.test.deployers.classloading.support.b.B;

/**
 * Undeploy order test case.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class UndeployOrderClassLoaderUnitTestCase extends ClassLoaderDependenciesTest
{
   public UndeployOrderClassLoaderUnitTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(UndeployOrderClassLoaderUnitTestCase.class);
   }

   public void testUndeployOrder() throws Exception
   {
      DeployerClient mainDeployer = getMainDeployer();
      VersionImpl v1 = VersionImpl.parseVersion("1");
      VersionImpl v2 = VersionImpl.parseVersion("2");

      Deployment ad = createSimpleDeployment("A");
      addClassLoaderMetaData(ad, v1, true, A.class);
      assertDeploy(mainDeployer, ad);

      Deployment bd = createSimpleDeployment("B");
      addClassLoaderMetaData(bd, v2, true, B.class);
      assertDeploy(mainDeployer, bd);
      mainDeployer.checkComplete(bd);

      Deployment cd = createSimpleDeployment("C");
      ClassLoaderMetaData clmd = addClassLoaderMetaData(cd, null);
      addRequirePackage(clmd, A.class, new VersionRange(v1, true, v2, true));
      addRequirePackage(clmd, B.class, new VersionRange(v1, true, v2, true));
      assertDeploy(mainDeployer, cd);

      mainDeployer.checkComplete();

      mainDeployer.undeploy(bd);
      try
      {
         mainDeployer.checkComplete();
      }
      catch (DeploymentException e)
      {
         e.printStackTrace();
      }
   }
}
