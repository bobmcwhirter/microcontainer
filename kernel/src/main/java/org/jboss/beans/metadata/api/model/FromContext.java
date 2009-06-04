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
package org.jboss.beans.metadata.api.model;

import java.util.Arrays;

import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.xb.annotations.JBossXmlEnum;

/**
 * Values available to inject from the controller context
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@JBossXmlEnum(ignoreCase=true)
public enum FromContext
{
   /** Does not get any values */
   NOOP(FromContextDelegate.NOOP),
   /** Gets the name from the controller context. The target property must be assignable from 
    * the actual name value, normally this means it must be a <code>java.util.Object</code> or 
    * <code>java.util.String</code> */
   NAME(FromContextDelegate.NAME),
   /** Gets the aliases from the controller context. The target property
    * must be a <code>java.util.Set&lt;Object&gt;</code>
    */
   ALIASES(FromContextDelegate.ALIASES),
   /** Gets the {@link org.jboss.beans.metadata.spi.BeanMetaData} from the controller context. The 
    * target property must be assignable from <code>org.jboss.beans.metadata.spi.BeanMetaData</code>
    */
   METADATA(FromContextDelegate.METADATA),
   /** Gets the {@link org.jboss.beans.info.spi.BeanInfo} from the controller context. The 
    * target property must be assignable from <code>org.jboss.beans.info.spi.BeanInfo</code>
    */
   BEANINFO(FromContextDelegate.BEANINFO),
   /** Gets the {@link org.jboss.dependency.spi.ScopeInfo} from the controller context. The 
    * target property must be assignable from <code>org.jboss.dependency.spi.ScopeInfo</code>
    */
   SCOPE(FromContextDelegate.SCOPE),
   /** Gets the {@link org.jboss.dependency.spi.ControllerState} from the controller context. The 
    * target property must be assignable from <code>org.jboss.dependency.spi.ControllerState</code>
    */
   STATE(FromContextDelegate.STATE),
   /**
    * Currently the same as {@link #NAME}. In the future will contain a unique id
    */
   ID(FromContextDelegate.ID),
   /**
    * Gets the whole {@link org.jboss.dependency.spi.ControllerContext}. The target property must be assignable 
    * from <code>org.jboss.dependency.spi.ControllerContext</code>
    * 
    */
   CONTEXT(FromContextDelegate.CONTEXT);

   /** The delegate */
   private final FromContextDelegate delegate;

   /**
    * Create a new state
    *
    * @param delegate the delegate
    */
   private FromContext(FromContextDelegate delegate)
   {
      if (delegate == null)
         throw new IllegalArgumentException("Null delegate");
      this.delegate = delegate;
   }

   /**
    * When valid state.
    *
    * @return the when valid state
    */
   public ControllerState getWhenValid()
   {
      return delegate.getWhenValid();
   }

   /**
    * Execute injection on context.
    *
    * @param context the target context
    * @return lookup value
    * @throws Throwable for any error
    */
   public Object executeLookup(ControllerContext context) throws Throwable
   {
      return delegate.executeLookup(context);
   }

   /**
    * Get the enum instance from string.
    *
    * @param fromString the type string
    * @return from context enum instance
    */
   public static FromContext getInstance(String fromString)
   {
      if (fromString == null)
         throw new IllegalArgumentException("Null from string.");
      
      for(FromContext fromContext : values())
      {
         if (fromString.equalsIgnoreCase(fromContext.delegate.getFromString()))
            return fromContext;
      }
      throw new IllegalArgumentException("No such FromContext for from string: " + fromString + ", available: " + Arrays.toString(values()));
   }
}
