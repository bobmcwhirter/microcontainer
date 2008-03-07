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
package org.jboss.beans.metadata.api.enums;

import org.jboss.dependency.spi.ControllerContext;

/**
 * Inject from controller context:
 *  * name - controller context name
 *  * aliases - aliases
 *  * metadata - inject MetaData
 *  * beaninfo - BeanInfo
 *  * scope - ScopeKey
 *  * id - identifier
 *  * dynamic - method specific
 *  * ...
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public enum FromContext
{
   NOOP(FromContextDelegate.NOOP),
   NAME(FromContextDelegate.NAME),
   ALIASES(FromContextDelegate.ALIASES),
   METADATA(FromContextDelegate.METADATA),
   BEANINFO(FromContextDelegate.BEANINFO),
   SCOPE(FromContextDelegate.SCOPE),
   STATE(FromContextDelegate.STATE),
   ID(FromContextDelegate.ID),
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
   // TODO - remove this once JBMICROCONT-219 is done
   public static FromContext getInstance(String fromString)
   {
      if (fromString == null)
         throw new IllegalArgumentException("Null from string.");
      
      for(FromContext fromContext : values())
      {
         if (fromString.equalsIgnoreCase(fromContext.delegate.getFromString()))
            return fromContext;
      }
      throw new IllegalArgumentException("No such FromContext for from string: " + fromString);
   }
}
