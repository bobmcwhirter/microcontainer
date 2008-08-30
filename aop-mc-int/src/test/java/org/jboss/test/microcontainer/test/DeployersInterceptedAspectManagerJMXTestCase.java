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
package org.jboss.test.microcontainer.test;

import java.util.HashSet;

import junit.framework.Test;

import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.deployers.DeployerAspects;
import org.jboss.test.microcontainer.support.deployers.IMainDeployer;
import org.jboss.test.microcontainer.support.deployers.SampleDeployer;
import org.jboss.test.microcontainer.support.deployers.IDeployer.IDeployerMethod;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class DeployersInterceptedAspectManagerJMXTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(DeployersInterceptedAspectManagerJMXTestCase.class);
   }
   
   public DeployersInterceptedAspectManagerJMXTestCase(String name)
   {
      super(name);
   }
   
   public void testBean() throws Exception
   {
      IMainDeployer md = (IMainDeployer) getBean("MainDeployer");
      assertNotNull(md);

      assertEquals("2 deployers", 2, md.getDeployers().size());

      md.addDeployment("thing.xar");
      md.process();

      HashSet<IDeployerMethod> interceptedCalled = DeployerAspects.getCalled();
      SampleDeployer d1 = (SampleDeployer) getBean("SampleDeployer1");
      SampleDeployer d2 = (SampleDeployer) getBean("SampleDeployer2");
      assertTrue("SampleDeployer1.prepareDeploy",
            d1.getCalled().contains(IDeployerMethod.prepareDeploy));
      assertTrue("SampleDeployer1.commitDeploy",
            d1.getCalled().contains(IDeployerMethod.commitDeploy));
      assertTrue("SampleDeployer2.prepareDeploy",
            d2.getCalled().contains(IDeployerMethod.prepareDeploy));
      assertTrue("SampleDeployer2.commitDeploy",
            d2.getCalled().contains(IDeployerMethod.commitDeploy));

      assertTrue("DeployerAspects.prepareDeploy",
            interceptedCalled.contains(IDeployerMethod.prepareDeploy));
      assertTrue("DeployerAspects.commitDeploy",
            interceptedCalled.contains(IDeployerMethod.commitDeploy));

      md.removeDeployment("thing.xar");
      md.process();

      assertTrue("SampleDeployer1.prepareUndeploy",
            d1.getCalled().contains(IDeployerMethod.prepareUndeploy));
      assertTrue("SampleDeployer1.commitUndeploy",
            d1.getCalled().contains(IDeployerMethod.commitUndeploy));
      assertTrue("SampleDeployer2.prepareUndeploy",
            d2.getCalled().contains(IDeployerMethod.prepareUndeploy));
      assertTrue("SampleDeployer2.commitUndeploy",
            d2.getCalled().contains(IDeployerMethod.commitUndeploy));

      assertTrue("DeployerAspects.prepareUndeploy",
            interceptedCalled.contains(IDeployerMethod.prepareUndeploy));
      assertTrue("DeployerAspects.commitUndeploy",
            interceptedCalled.contains(IDeployerMethod.commitUndeploy));
}
}
