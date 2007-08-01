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
package org.jboss.dependency.plugins;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.CopyOnWriteArraySet;

import org.jboss.dependency.spi.CallbackItem;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.dependency.spi.LifecycleCallbackItem;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * A DependencyInfo.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AbstractDependencyInfo extends JBossObject implements DependencyInfo
{
   /** My dependencies */
   private Set<DependencyItem> iDependOn = new CopyOnWriteArraySet<DependencyItem>();

   /** Dependencies referencing me */
   private Set<DependencyItem> dependsOnMe = new CopyOnWriteArraySet<DependencyItem>();

   /** Unresolved dependencies */
   private Set<DependencyItem> unresolved = new CopyOnWriteArraySet<DependencyItem>();

   /** Install callbacks */
   private Set<CallbackItem> installCallbacks = new CopyOnWriteArraySet<CallbackItem>();

   /** Uninstall callbacks */
   private Set<CallbackItem> uninstallCallbacks = new CopyOnWriteArraySet<CallbackItem>();
   
   /** Lifecycle callbacks */
   private List<LifecycleCallbackItem> lifecycleCallbacks = new CopyOnWriteArrayList<LifecycleCallbackItem>();
   
   

   /**
    * Create an abstract dependency info
    */
   public AbstractDependencyInfo()
   {
   }

   public Set<DependencyItem> getIDependOn(Class type)
   {
      if (type == null || iDependOn.isEmpty())
         return iDependOn;
      else
      {
         HashSet<DependencyItem> set = new HashSet<DependencyItem>();
         for (DependencyItem item : iDependOn)
         {
            if (type.isInstance(item))
               set.add(item);
         }
         return set;
      }
   }
   
   public void addIDependOn(DependencyItem dependency)
   {
      iDependOn.add(dependency);
      unresolved.add(dependency);
      flushJBossObjectCache();
   }

   public void removeIDependOn(DependencyItem dependency)
   {
      iDependOn.remove(dependency);
      unresolved.remove(dependency);
      flushJBossObjectCache();
   }
   
   public Set<DependencyItem> getDependsOnMe(Class type)
   {
      if (type == null || dependsOnMe.isEmpty())
         return dependsOnMe;
      else
      {
         HashSet<DependencyItem> set = new HashSet<DependencyItem>();
         for (DependencyItem item : dependsOnMe)
         {
            if (type.isInstance(item))
               set.add(item);
         }
         return set;
      }
   }
   
   public void addDependsOnMe(DependencyItem dependency)
   {
      dependsOnMe.add(dependency);
      flushJBossObjectCache();
   }

   public void removeDependsOnMe(DependencyItem dependency)
   {
      dependsOnMe.remove(dependency);
      flushJBossObjectCache();
   }
   
   public boolean resolveDependencies(Controller controller, ControllerState state)
   {
      boolean resolved = true;
      if (unresolved.isEmpty() == false)
      {
         for (DependencyItem item : unresolved)
         {
            if (state.equals(item.getWhenRequired()) && item.resolve(controller) == false)
            {
               resolved = false;
               break;
            }
         }
      }
      return resolved;
   }

   public Set<DependencyItem> getUnresolvedDependencies()
   {
      return unresolved;
   }

   public void addInstallItem(CallbackItem callbackItem)
   {
      installCallbacks.add(callbackItem);
      flushJBossObjectCache();
   }

   public void removeInstallItem(CallbackItem callbackItem)
   {
      installCallbacks.remove(callbackItem);
      flushJBossObjectCache();
   }

   public Set<CallbackItem> getInstallItems()
   {
      return installCallbacks;
   }

   public void addUninstallItem(CallbackItem callbackItem)
   {
      uninstallCallbacks.add(callbackItem);
      flushJBossObjectCache();
   }

   public void removeUninstallItem(CallbackItem callbackItem)
   {
      uninstallCallbacks.remove(callbackItem);
      flushJBossObjectCache();
   }

   public Set<CallbackItem> getUninstallItems()
   {
      return uninstallCallbacks;
   }

   public void addLifecycleCallback(LifecycleCallbackItem lifecycleCallbackItem)
   {
      lifecycleCallbacks.add(lifecycleCallbackItem);
   }
   
   public List<LifecycleCallbackItem> getLifecycleCallbacks()
   {
      return lifecycleCallbacks;
   }
   
   public void toString(JBossStringBuilder buffer)
   {
      buffer.append("idependOn=").append(iDependOn);
      if (unresolved.isEmpty() == false)
         buffer.append(" unresolved=").append(unresolved);
   }
}
