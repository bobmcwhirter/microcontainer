/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.microcontainer.support.deployers;

import java.util.HashSet;

import org.jboss.logging.Logger;

/**
 * Sample IDeployer.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class SampleDeployer implements IDeployer
{
   private static Logger log = Logger.getLogger(SampleDeployer.class);
   private HashSet<IDeployerMethod> called = new HashSet<IDeployerMethod>();
   private String type;

   public SampleDeployer()
   {
      log.debug("ctor");
   }

   public HashSet<IDeployerMethod> getCalled()
   {
      return called;
   }

   public String getType()
   {
      return type;
   }

   public void setType(String type)
   {
      this.type = type;
   }

   public void commitDeploy(String unit) throws Exception
   {
      called.add(IDeployerMethod.commitDeploy);
   }

   public void commitUndeploy(String unit)
   {
      called.add(IDeployerMethod.commitUndeploy);
   }

   public void prepareDeploy(String unit) throws Exception
   {
      called.add(IDeployerMethod.prepareDeploy);
   }

   public void prepareUndeploy(String unit)
   {
      called.add(IDeployerMethod.prepareUndeploy);
   }

}
