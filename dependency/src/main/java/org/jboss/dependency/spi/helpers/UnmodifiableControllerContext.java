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

import java.util.Collections;
import java.util.Set;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.ScopeInfo;
import org.jboss.dependency.spi.ErrorHandlingMode;
import org.jboss.util.JBossObject;

/**
 * A wrapper around a {@link ControllerContext} that throws UnsupportedOperationException when any
 * methods that might mutate the underlying context's state is called. 
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class UnmodifiableControllerContext extends JBossObject implements ControllerContext
{
   private ControllerContext delegate;

   public UnmodifiableControllerContext(ControllerContext delegate)
   {
      if (delegate == null)
         throw new IllegalArgumentException("Null delegate");
      this.delegate = delegate;
   }

   /**
    * Get the delegate.
    *
    * @return the delegate
    */
   protected ControllerContext getDelegate()
   {
      return delegate;
   }

   public Object getName()
   {
      return delegate.getName();
   }

   public Set<Object> getAliases()
   {
      Set<Object> aliases = delegate.getAliases();
      return aliases != null ? Collections.unmodifiableSet(aliases) : null;
   }

   public DependencyInfo getDependencyInfo()
   {
      DependencyInfo dependencyInfo = delegate.getDependencyInfo();
      return dependencyInfo != null ? new UnmodifiableDependencyInfo(dependencyInfo) : null;
   }

   public ScopeInfo getScopeInfo()
   {
      ScopeInfo scopeInfo = delegate.getScopeInfo();
      return scopeInfo != null ? new UnmodifiableScopeInfo(scopeInfo) : null;
   }

   public Object getTarget()
   {
      return delegate.getTarget();
   }

   public Controller getController()
   {
      return delegate.getController();
   }

   /**
    * Overrides {@link ControllerContext#setController(Controller)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param state the controller
    * @throws UnsupportedOperationException when called
    */
   public void setController(Controller controller)
   {
      throw new UnsupportedOperationException("Cannot invoke set on unmodifiable wrapper.");
   }

   public void install(ControllerState fromState, ControllerState toState) throws Throwable
   {
      delegate.install(fromState, toState);
   }

   public void uninstall(ControllerState fromState, ControllerState toState)
   {
      delegate.uninstall(fromState, toState);
   }

   public ControllerState getState()
   {
      return delegate.getState();
   }

   /**
    * Overrides {@link ControllerContext#setState(ControllerState)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param state the state
    * @throws UnsupportedOperationException when called
    */
   public void setState(ControllerState state)
   {
      throw new UnsupportedOperationException("Cannot invoke set on unmodifiable wrapper.");
   }

   public ControllerState getRequiredState()
   {
      return delegate.getRequiredState();
   }

   /**
    * Overrides {@link ControllerContext#setRequiredState(ControllerState)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param state the required state
    * @throws UnsupportedOperationException when called
    */
   public void setRequiredState(ControllerState state)
   {
      throw new UnsupportedOperationException("Cannot invoke set on unmodifiable wrapper.");
   }

   public ControllerMode getMode()
   {
      return delegate.getMode();
   }

   /**
    * Overrides {@link ControllerContext#setMode(ControllerMode)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param mode the mode
    * @throws UnsupportedOperationException when called
    */
   public void setMode(ControllerMode mode)
   {
      throw new UnsupportedOperationException("Cannot invoke set on unmodifiable wrapper.");
   }

   public ErrorHandlingMode getErrorHandlingMode()
   {
      return delegate.getErrorHandlingMode();
   }

   public Throwable getError()
   {
      return delegate.getError();
   }

   /**
    * Overrides {@link ControllerContext#setError(Throwable)} to throw an {@link UnsupportedOperationException}
    * when called.
    * 
    * @param error the error
    * @throws UnsupportedOperationException when called
    */
   public void setError(Throwable error)
   {
      throw new UnsupportedOperationException("Cannot invoke set on unmodifiable wrapper.");
   }
}
