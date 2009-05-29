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
package org.jboss.dependency.spi.helpers;

import java.util.List;
import java.util.Set;

import org.jboss.dependency.spi.CallbackItem;
import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.DependencyItem;
import org.jboss.dependency.spi.LifecycleCallbackItem;
import org.jboss.util.JBossObject;

/**
 * A wrapper around a {@link DependencyInfo} that throws UnsupportedOperationException when any
 * methods that might mutate the underlying {@link DependencyInfo} state is called. 
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class UnmodifiableDependencyInfo extends JBossObject implements DependencyInfo
{
   private DependencyInfo delegate;

   public UnmodifiableDependencyInfo(DependencyInfo delegate)
   {
      if (delegate == null)
         throw new IllegalArgumentException("Null dependency info!");
      this.delegate = delegate;
   }

   public Set<DependencyItem> getIDependOn(Class<?> type)
   {
      return delegate.getIDependOn(type);
   }

   public Set<DependencyItem> getDependsOnMe(Class<?> type)
   {
      return delegate.getDependsOnMe(type);
   }

   /**
    * Overrides {@link DependencyInfo#addIDependOn(DependencyItem)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param dependency the dependency
    * @throws UnsupportedOperationException when called
    */
   public void addIDependOn(DependencyItem dependency)
   {
      throw new UnsupportedOperationException("Cannot execute add on unmodifiable wrapper.");
   }

   /**
    * Overrides {@link DependencyInfo#removeIDependOn(DependencyItem)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param dependency the dependency
    * @throws UnsupportedOperationException when called
    */
   public void removeIDependOn(DependencyItem dependency)
   {
      throw new UnsupportedOperationException("Cannot execute remove on unmodifiable wrapper.");
   }

   /**
    * Overrides {@link DependencyInfo#addDependsOnMe(DependencyItem)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param dependency the dependency
    */
   public void addDependsOnMe(DependencyItem dependency)
   {
      throw new UnsupportedOperationException("Cannot execute add on unmodifiable wrapper.");
   }

   /**
    * Overrides {@link DependencyInfo#removeDependsOnMe(DependencyItem)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param dependency the dependency
    * @throws UnsupportedOperationException when called
    */
   public void removeDependsOnMe(DependencyItem dependency)
   {
      throw new UnsupportedOperationException("Cannot execute remove on unmodifiable wrapper.");
   }

   public boolean resolveDependencies(Controller controller, ControllerState state)
   {
      return delegate.resolveDependencies(controller, state);
   }

   public Set<DependencyItem> getUnresolvedDependencies(ControllerState state)
   {
      return delegate.getUnresolvedDependencies(state);
   }

   /**
    * Overrides {@link DependencyInfo#addInstallItem(CallbackItem)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param callbackItem the callback item
    * @throws UnsupportedOperationException when called
    */
   public <T> void addInstallItem(CallbackItem<T> callbackItem)
   {
      throw new UnsupportedOperationException("Cannot execute add on unmodifiable wrapper.");
   }

   /**
    * Overrides {@link DependencyInfo#removeInstallItem(CallbackItem)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param callbackItem the callback item
    * @throws UnsupportedOperationException when called
    */
   public <T> void removeInstallItem(CallbackItem<T> callbackItem)
   {
      throw new UnsupportedOperationException("Cannot execute remove on unmodifiable wrapper.");
   }

   public Set<CallbackItem<?>> getInstallItems()
   {
      return delegate.getInstallItems();
   }

   /**
    * Overrides {@link DependencyInfo#addUninstallItem(CallbackItem)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param callbackItem the callback item
    * @throws UnsupportedOperationException when called
    */
   public <T> void addUninstallItem(CallbackItem<T> callbackItem)
   {
      throw new UnsupportedOperationException("Cannot execute add on unmodifiable wrapper.");
   }

   /**
    * Overrides {@link DependencyInfo#removeUninstallItem(CallbackItem)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param callbackItem the callback item
    * @throws UnsupportedOperationException when called
    */
   public <T> void removeUninstallItem(CallbackItem<T> callbackItem)
   {
      throw new UnsupportedOperationException("Cannot execute remove on unmodifiable wrapper.");
   }

   public Set<CallbackItem<?>> getUninstallItems()
   {
      return delegate.getUninstallItems();
   }

   /**
    * Overrides {@link DependencyInfo#addLifecycleCallback(LifecycleCallbackItem)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param lifecycleCallbackItem the lifecycle callback item
    * @throws UnsupportedOperationException when called
    */
   public void addLifecycleCallback(LifecycleCallbackItem lifecycleCallbackItem)
   {
      throw new UnsupportedOperationException("Cannot execute add on unmodifiable wrapper.");
   }

   public List<LifecycleCallbackItem> getLifecycleCallbacks()
   {
      return delegate.getLifecycleCallbacks();
   }

   public boolean isAutowireCandidate()
   {
      return delegate.isAutowireCandidate();
   }

   /**
    * Overrides {@link DependencyInfo#setAutowireCandidate(boolean)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param candidate if we are an autowire candidate
    * @throws UnsupportedOperationException when called
    */
   public void setAutowireCandidate(boolean candidate)
   {
      throw new UnsupportedOperationException("Cannot execute set on unmodifiable wrapper.");
   }
}
