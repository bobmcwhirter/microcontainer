/*
 * JBoss, Home of Professional Open Source
 * Copyright 2008, JBoss Inc., and individual contributors as indicated
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
package org.jboss.osgi.plugins.facade;

import org.osgi.framework.Bundle;

/**
 * A BundleState enum to wrap the non-type-safe Bundle state constants
 * 
 * @author <a href="baileyje@gmail.com">John Bailey</a>
 * @version $Revision: 1.1 $
 */
public enum BundleState {

   UNINSTALLED(Bundle.UNINSTALLED),
   INSTALLED(Bundle.INSTALLED),
   RESOLVED(Bundle.RESOLVED),
   STARTING(Bundle.STARTING),
   STOPPING(Bundle.STOPPING),
   ACTIVE(Bundle.ACTIVE);

   private final int state;

   /**
    * 
    * Create a new BundleState.
    * 
    * @param state the state number
    */
   private BundleState(final int state)
   {
      this.state = state;
   }

   /** 
    * Get Bundle state
    * 
    * @return the state
    */
   public int getState()
   {
      return state;
   }
}
