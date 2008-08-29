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
package org.jboss.test.kernel.dependency.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.Test;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.InstallCallbackMetaData;
import org.jboss.beans.metadata.plugins.UninstallCallbackMetaData;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.test.kernel.dependency.support.SimpleBeanRepository;

/**
 * Callback collection tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class CallbackCollectionTestCase extends CallbackTestCase
{
   public CallbackCollectionTestCase(String name)
         throws Throwable
   {
      super(name);
   }

   public CallbackCollectionTestCase(String name, boolean xmltest)
         throws Throwable
   {
      super(name, xmltest);
   }

   public static Test suite()
   {
      return suite(CallbackCollectionTestCase.class);
   }

   protected AbstractBeanMetaData buildRepository(Cardinality cardinality)
   {
      AbstractBeanMetaData repository = new AbstractBeanMetaData("Name1", SimpleBeanRepository.class.getName());
      List<CallbackMetaData> installs = new ArrayList<CallbackMetaData>();
      repository.setInstallCallbacks(installs);
      InstallCallbackMetaData install = new InstallCallbackMetaData();
      install.setMethodName("setBeans");
      if (cardinality != null)
         install.setCardinality(cardinality);
      installs.add(install);
      List<CallbackMetaData> unstalls = new ArrayList<CallbackMetaData>();
      repository.setUninstallCallbacks(unstalls);
      UninstallCallbackMetaData uninstall = new UninstallCallbackMetaData();
      uninstall.setMethodName("setBeans");
      unstalls.add(uninstall);
      return repository;
   }
}
