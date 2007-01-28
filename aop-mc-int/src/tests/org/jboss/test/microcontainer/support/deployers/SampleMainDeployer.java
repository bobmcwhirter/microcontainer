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

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;

/**
 * Sample IMainDeployer
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class SampleMainDeployer implements IMainDeployer
{
   private static Logger log = Logger.getLogger(SampleMainDeployer.class);
   private ArrayList<IDeployer> deployers = new ArrayList<IDeployer>();
   private ArrayList<String> added = new ArrayList<String>();
   private ArrayList<String> removed = new ArrayList<String>();

   public List<IDeployer> getDeployers()
   {
      return new ArrayList<IDeployer>(deployers);
   }
   public void setDeployers(List<IDeployer> list)
   {
      this.deployers.clear();
      this.deployers.addAll(list);
      log.debug("setDeployers, size="+deployers.size());
   }
   public void addDeployer(IDeployer deployer)
   {
      deployers.add(deployer);
   }
   public void removeDeployer(IDeployer deployer)
   {
      deployers.remove(deployer);
   }

   public void addDeployment(String unit)
   {
      added.add(unit);
   }
   public void removeDeployment(String unit)
   {
      added.remove(unit);
      removed.add(unit);
   }

   public void process() throws Exception
   {
      for(IDeployer d : deployers)
      {
         for(String unit : removed)
         {
            d.prepareUndeploy(unit);
            d.commitUndeploy(unit);
         }
      }

      for(IDeployer d : deployers)
      {
         for(String unit : added)
         {
            d.prepareDeploy(unit);
            d.commitDeploy(unit);
         }
      }
   }

}
