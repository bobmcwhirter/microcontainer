/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.classloading.spi.dependency.policy;

import org.jboss.classloader.spi.ClassLoaderPolicy;
import org.jboss.classloader.spi.ClassLoaderPolicyFactory;
import org.jboss.classloading.spi.dependency.Domain;
import org.jboss.classloading.spi.dependency.Module;
import org.jboss.classloading.spi.dependency.RequirementDependencyItem;
import org.jboss.dependency.spi.Controller;

/**
 * DynamicClassLoaderPolicyFactory.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class DynamicClassLoaderPolicyFactory implements ClassLoaderPolicyFactory
{
   /** The controller */
   private Controller controller;

   /** The domain */
   private Domain domain;
   
   /** The requirement dependency item */
   private RequirementDependencyItem item;

   /**
    * Create a new DynamicClassLoaderPolicyFactory.
    * 
    * @param controller the controller
    * @param domain the domain
    * @param item the item
    */
   public DynamicClassLoaderPolicyFactory(Controller controller, Domain domain, RequirementDependencyItem item)
   {
      if (controller == null)
         throw new IllegalArgumentException("Null controller");
      if (domain == null)
         throw new IllegalArgumentException("Null domain");
      if (item == null)
         throw new IllegalArgumentException("Null item");
      this.controller = controller;
      this.domain = domain;
      this.item = item;
   }

   public ClassLoaderPolicy createClassLoaderPolicy()
   {
      // Still undetermined
      String name = (String) item.getIDependOn();
      if (name == null)
      {
         // Try to resolve
         item.resolve(controller);
         name = (String) item.getIDependOn();
         if (name == null)
            return null;
      }

      Module iDependOnModule = domain.getModule(name);
      if (iDependOnModule == null)
         throw new IllegalStateException("Module not found with name: " + name);

      // Get the policy for the module
      if (iDependOnModule instanceof ClassLoaderPolicyModule == false)
         throw new IllegalStateException("Unable to determine ClassLoaderPolicy from module: " + iDependOnModule);
      ClassLoaderPolicyModule classLoaderPolicyModule = (ClassLoaderPolicyModule) iDependOnModule;
      return classLoaderPolicyModule.getPolicy();
   }
}
