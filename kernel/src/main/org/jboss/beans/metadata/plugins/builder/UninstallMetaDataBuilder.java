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
package org.jboss.beans.metadata.plugins.builder;

import java.util.ArrayList;
import java.util.List;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractInstallMetaData;
import org.jboss.beans.metadata.spi.InstallMetaData;

/**
 * UninstallMetaDataBuilder.
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class UninstallMetaDataBuilder extends AbstractInstallMetaDataBuilder
{
   /**
    * Create a new StartLifecycleMetaDataBuilder.
    *
    * @param beanMetaData the bean meta data
    */
   public UninstallMetaDataBuilder(AbstractBeanMetaData beanMetaData)
   {
      super(beanMetaData);
   }

   protected void setLifecycle(AbstractBeanMetaData beanMetaData, AbstractInstallMetaData lifecycle)
   {
      List<InstallMetaData> uninstalls = beanMetaData.getUninstalls();
      if (uninstalls == null)
      {
         uninstalls = new ArrayList<InstallMetaData>();
         beanMetaData.setUninstalls(uninstalls);
      }
      uninstalls.add(lifecycle);
   }
}
