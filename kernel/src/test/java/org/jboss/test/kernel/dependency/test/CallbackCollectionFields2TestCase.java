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
import org.jboss.beans.info.spi.BeanAccessMode;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.InstallCallbackMetaData;
import org.jboss.beans.metadata.plugins.UninstallCallbackMetaData;
import org.jboss.beans.metadata.spi.CallbackMetaData;
import org.jboss.dependency.spi.Cardinality;
import org.jboss.test.kernel.dependency.support.SimpleFieldsBeanRepository2;
import org.jboss.test.AbstractTestDelegate;

/**
 * Callback collection tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class CallbackCollectionFields2TestCase extends CallbackTestCase
{
   public CallbackCollectionFields2TestCase(String name)
         throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(CallbackCollectionFields2TestCase.class);
   }

   protected AbstractBeanMetaData buildRepository(Cardinality cardinality)
   {
      AbstractBeanMetaData repository = new AbstractBeanMetaData("Name1", SimpleFieldsBeanRepository2.class.getName());
      repository.setAccessMode(BeanAccessMode.ALL);
      List<CallbackMetaData> installs = new ArrayList<CallbackMetaData>();
      repository.setInstallCallbacks(installs);
      InstallCallbackMetaData install = new InstallCallbackMetaData();
      install.setProperty("beans");
      if (cardinality != null)
         install.setCardinality(cardinality);
      installs.add(install);
      List<CallbackMetaData> unstalls = new ArrayList<CallbackMetaData>();
      repository.setUninstallCallbacks(unstalls);
      UninstallCallbackMetaData uninstall = new UninstallCallbackMetaData();
      uninstall.setProperty("beans");
      unstalls.add(uninstall);
      return repository;
   }

   /**
    * Default setup with security manager disabled
    *
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      AbstractTestDelegate delegate = new AbstractTestDelegate(clazz);
      delegate.enableSecurity = false;
      return delegate;
   }
}
