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
package org.jboss.test.dependency.controller.support;

import java.util.HashSet;
import java.util.Set;

import org.jboss.dependency.plugins.AbstractDependencyInfo;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.DependencyItem;

/**
 * A TestDelegate.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class TestDelegate extends Ordering
{
   private Object name;
   
   private Set<Object> aliases;
   
   public AbstractDependencyInfo dependencies = new AbstractDependencyInfo();

   public ControllerMode mode = ControllerMode.AUTOMATIC;
   
   public int describeInstallOrder = -1;
   public int describeUninstallOrder = -1;
   public int instantiateInstallOrder = -1;
   public int instantiateUninstallOrder = -1;
   public int configureInstallOrder = -1;
   public int configureUninstallOrder = -1;
   public int createInstallOrder = -1;
   public int createUninstallOrder = -1;
   public int startInstallOrder = -1;
   public int startUninstallOrder = -1;
   public int installInstallOrder = -1;
   public int installUninstallOrder = -1;
   
   public TestDelegate(Object name)
   {
      this(name, null);
   }
   
   public TestDelegate(Object name, Object... aliases)
   {
      this.name = name;
      if (aliases != null)
      {
         this.aliases = new HashSet<Object>();
         for (int i = 0; i < aliases.length; ++i)
            this.aliases.add(aliases[i]);
      }
   }
   
   public Object getName()
   {
      return name;
   }
   
   public Set<Object> getAliases()
   {
      return aliases;
   }
   
   public void describeInstall()
   {
      describeInstallOrder = order.incrementAndGet();
   }
   
   public void describeUninstall()
   {
      describeUninstallOrder = order.incrementAndGet();
   }
   
   public void instantiateInstall()
   {
      instantiateInstallOrder = order.incrementAndGet();
   }
   
   public void instantiateUninstall()
   {
      instantiateUninstallOrder = order.incrementAndGet();
   }
   
   public void configureInstall()
   {
      configureInstallOrder = order.incrementAndGet();
   }
   
   public void configureUninstall()
   {
      configureUninstallOrder = order.incrementAndGet();
   }
   
   public void createInstall()
   {
      createInstallOrder = order.incrementAndGet();
   }
   
   public void createUninstall()
   {
      createUninstallOrder = order.incrementAndGet();
   }
   
   public void startInstall()
   {
      startInstallOrder = order.incrementAndGet();
   }
   
   public void startUninstall()
   {
      startUninstallOrder = order.incrementAndGet();
   }
   
   public void installInstall()
   {
      installInstallOrder = order.incrementAndGet();
   }
   
   public void installUninstall()
   {
      installUninstallOrder = order.incrementAndGet();
   }
   
   public void addDependency(DependencyItem dependency)
   {
      dependencies.addIDependOn(dependency);
   }
   
   public void setMode(ControllerMode mode)
   {
      this.mode = mode;
   }
}
