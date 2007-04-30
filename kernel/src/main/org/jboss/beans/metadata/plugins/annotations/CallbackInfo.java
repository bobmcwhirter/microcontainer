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
package org.jboss.beans.metadata.plugins.annotations;

import java.io.Serializable;

import org.jboss.dependency.spi.Cardinality;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * Callback info holder.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
class CallbackInfo extends JBossObject implements Serializable
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -2213756676713799799L;

   /* The cardinality */
   protected Cardinality cardinality;

    /* When required state */
   protected ControllerState whenRequired;

    /* Dependent state */
   protected ControllerState dependentState;

   /* Is install phase */
   protected boolean isInstallPhase;

   CallbackInfo(Callback callback)
   {
      this(callback.cardinality(), callback.whenRequired(), callback.dependentState(), CallbackType.INSTALL.equals(callback.type()));
   }

   CallbackInfo(Install callback)
   {
      this(callback.cardinality(), callback.whenRequired(), callback.dependentState(), true);
   }

   CallbackInfo(Uninstall callback)
   {
      this(callback.cardinality(), callback.whenRequired(), callback.dependentState(), false);
   }

   private CallbackInfo(String cardinality, String whenRequired, String dependentState, boolean isInstallPhase)
   {
      this(
            cardinality == null || cardinality.length() == 0 ? null : Cardinality.toCardinality(cardinality),
            new ControllerState(whenRequired),
            new ControllerState(dependentState),
            isInstallPhase
      );
   }

   private CallbackInfo(Cardinality cardinality, ControllerState whenRequired, ControllerState dependentState, boolean isInstallPhase)
   {
      this.cardinality = cardinality;
      this.whenRequired = whenRequired;
      this.dependentState = dependentState;
      this.isInstallPhase = isInstallPhase;
   }

   /**
    * Get the cardinality.
    *
    * @return cardinality
    */
   public Cardinality getCardinality()
   {
      return cardinality;
   }

   /**
    * Get when required state.
    *
    * @return when required state
    */
   public ControllerState getWhenRequired()
   {
      return whenRequired;
   }

   /**
    * Get dependent state.
    *
    * @return dependent state
    */
   public ControllerState getDependentState()
   {
      return dependentState;
   }

   /**
    * Is install or uninstall.
    *
    * @return true for install, false for uninstall
    */
   public boolean isInstallPhase()
   {
      return isInstallPhase;
   }

   protected void toString(JBossStringBuilder buffer)
   {
      buffer.append("isInstallPhase=").append(isInstallPhase);
      buffer.append(" cardinality=").append(cardinality);
      buffer.append(" whenRequired=").append(whenRequired);
      buffer.append(" dependentState=").append(dependentState);
   }
}
