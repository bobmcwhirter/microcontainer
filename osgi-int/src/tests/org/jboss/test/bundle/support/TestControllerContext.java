/*
 * JBoss, the OpenSource J2EE webOS
 * 
 * Distributable under LGPL license.
 * See terms of license at gnu.org.
 */
package org.jboss.test.bundle.support;

import java.util.Set;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.DependencyInfo;
import org.jboss.dependency.spi.ScopeInfo;
import org.jboss.deployers.structure.spi.DeploymentContext;
import org.jboss.util.JBossStringBuilder;

public class TestControllerContext implements ControllerContext
{
   private ControllerState state;

   private DeploymentContext context;

   public TestControllerContext(DeploymentContext context)
   {
      this.context = context;
   }

   public String toShortString()
   {
      return null;
   }

   public void toShortString(JBossStringBuilder arg0)
   {

   }

   @Override
   public Object clone()
   {
      return null;
   }

   public Set<Object> getAliases()
   {
      return null;
   }

   public Controller getController()
   {
      return null;
   }

   public DependencyInfo getDependencyInfo()
   {
      return null;
   }

   public Throwable getError()
   {
      return null;
   }

   public ControllerMode getMode()
   {
      return null;
   }

   public Object getName()
   {
      return context.getName();
   }

   public ControllerState getRequiredState()
   {
      return null;
   }

   public ScopeInfo getScopeInfo()
   {
      return null;
   }

   public ControllerState getState()
   {
      return state;
   }

   public Object getTarget()
   {
      return null;
   }

   public void install(ControllerState fromState, ControllerState toState) throws Throwable
   {
   }

   public void setController(Controller controller)
   {
   }

   public void setError(Throwable error)
   {
   }

   public void setMode(ControllerMode mode)
   {
   }

   public void setRequiredState(ControllerState state)
   {
   }

   public void setState(ControllerState state)
   {
      this.state = state;
   }

   public void uninstall(ControllerState fromState, ControllerState toState)
   {
   }

}