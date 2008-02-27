/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.classloading.spi.dependency;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

import org.jboss.classloading.spi.metadata.Capability;
import org.jboss.classloading.spi.metadata.Requirement;
import org.jboss.dependency.spi.Controller;

/**
 * Domain.
 * 
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class Domain
{
   /** The domain name */
   private String name;
   
   /** The registered modules in registration order */
   private List<Module> modules = new CopyOnWriteArrayList<Module>();
   
   /** The registered modules by name */
   private Map<String, Module> modulesByName = new ConcurrentHashMap<String, Module>();

   /**
    * Create a new Domain.
    * 
    * @param name the name
    * @throws IllegalArgumentException for a null domain
    */
   public Domain(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null name");
      this.name = name;
   }

   /**
    * Get the name.
    * 
    * @return the name.
    */
   public String getName()
   {
      return name;
   }

   /**
    * Add a module
    * 
    * @param module the module
    * @throws IllegalStateException if the module is already registered
    * @throws IllegalArgumentException for a null parameter
    */
   public synchronized void addModule(Module module)
   {
      if (module == null)
         throw new IllegalArgumentException("Null module");
      Domain domain = module.getDomain();
      if (domain != null)
         throw new IllegalArgumentException("The module is already registered with the domain " + domain.getName());
      String contextName = module.getContextName();
      if (modulesByName.containsKey(contextName))
         throw new IllegalArgumentException("The context " + contextName + " is already registered in domain " + getName());
      module.setDomain(this);
      modulesByName.put(contextName, module);
      modules.add(module);
      try
      {
         module.createDependencies();
      }
      catch (Throwable t)
      {
         removeModule(module);
         if (t instanceof RuntimeException)
            throw (RuntimeException) t;
         else if (t instanceof Error)
            throw (Error) t;
         else
            throw new RuntimeException("Error adding module " + module, t);
      }
   }

   /**
    * Get a module for a context name
    * 
    * @param name the context name
    * @return the module
    */
   public Module getModule(String name)
   {
      if (name == null)
         throw new IllegalArgumentException("Null module name");
      return modulesByName.get(name);
   }
   
   /**
    * Resolve the requirement
    * 
    * @param controller the controller
    * @param module the module
    * @param requirement the requirement
    * @return the resolved name or null if not resolved
    */
   protected Object resolve(Controller controller, Module module, Requirement requirement)
   {
      // TODO JBMICROCONT-182 include parent domains in requirements
      // TODO JBMICROCONT-182 check consistency of re-exports
      // TODO JBMICROCONT-182 check for self-dependency
      // TODO JBMICROCONT-182 test circularity
      for (Module other : modules)
      {
         List<Capability> capabilities = other.getCapabilities();
         if (capabilities != null)
         {
            for (Capability capability : capabilities)
            {
               if (capability.resolves(module, requirement))
                  return other.getContextName();
            }
         }
      }
      return null;
   }
   
   /**
    * Remove a deployment
    * 
    * @param module the module
    * @throws IllegalArgumentException for a null parameter
    */
   protected synchronized void removeModule(Module module)
   {
      if (module == null)
         throw new IllegalArgumentException("Null module");
      modulesByName.remove(module.getContextName());
      modules.remove(module);
      module.setDomain(null);
      module.removeDependencies();
   }
}
