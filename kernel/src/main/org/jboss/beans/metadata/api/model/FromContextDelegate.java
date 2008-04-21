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

import java.io.Serializable;
import java.io.ObjectStreamException;
import java.util.Collections;
import java.util.Set;
import java.util.Map;
import java.util.HashMap;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.helpers.UnmodifiableBeanInfo;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.helpers.UnmodifiableControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.helpers.UnmodifiableKernelControllerContext;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

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
abstract class FromContextDelegate extends JBossObject implements Serializable
{
   private static final long serialVersionUID = 1L;

   /** name */
   static final FromContextDelegate NOOP = new NoopFromContext("noop");

   /** name */
   static final FromContextDelegate NAME = new NameFromContext(MicrocontainerConstants.NAME);

   /** alias */
   static final FromContextDelegate ALIASES = new AliasesFromContext(MicrocontainerConstants.ALIASES);

   /** metadata */
   static final FromContextDelegate METADATA = new MetaDataFromContext(MicrocontainerConstants.METADATA);

   /** beaninfo */
   static final FromContextDelegate BEANINFO = new BeanInfoFromContext(MicrocontainerConstants.BEANINFO);

   /** scope */
   static final FromContextDelegate SCOPE = new ScopeFromContext(MicrocontainerConstants.SCOPE);

   /** state */
   static final FromContextDelegate STATE = new StateFromContext(MicrocontainerConstants.STATE);

   /** id */
   static final FromContextDelegate ID = new IdFromContext(MicrocontainerConstants.ID);

   /** context */
   static final FromContextDelegate CONTEXT = new ThisContext(MicrocontainerConstants.CONTEXT);

   /** The type string */
   protected final String fromString;

   /** The when valid state */
   protected ControllerState whenValid;

   /** The values */
   private static Map<String, FromContextDelegate> values = new HashMap<String, FromContextDelegate>();

   static
   {
      values.put(NOOP.getFromString(), NOOP);
      values.put(NAME.getFromString(), NAME);
      values.put(ALIASES.getFromString(), ALIASES);
      values.put(METADATA.getFromString(), METADATA);
      values.put(BEANINFO.getFromString(), BEANINFO);
      values.put(SCOPE.getFromString(), SCOPE);
      values.put(STATE.getFromString(), STATE);
      values.put(ID.getFromString(), ID);
      values.put(CONTEXT.getFromString(), CONTEXT);
   }

   /**
    * Create a new state
    *
    * @param fromString the string representation
    */
   protected FromContextDelegate(String fromString)
   {
      this(fromString, null);
   }

   /**
    * Create new state
    *
    * @param fromString the string representation
    * @param whenValid the when valid state
    */
   protected FromContextDelegate(String fromString, ControllerState whenValid)
   {
      if (fromString == null)
         throw new IllegalArgumentException("Null from string");
      this.fromString = fromString;
      if (whenValid == null)
         whenValid = ControllerState.PRE_INSTALL;
      this.whenValid = whenValid;
   }

   /**
    * Get when valid state.
    *
    * @return the when required state
    */
   public ControllerState getWhenValid()
   {
      return whenValid;
   }

   /**
    * Validate context before execution.
    * After validation we must be able to cast context to T instance.
    *
    * @param context the context
    */
   protected void validate(ControllerContext context)
   {
   }

   /**
    * Execute injection on context.
    *
    * @param context the target context
    * @return lookup value
    * @throws Throwable for any error
    */
   @SuppressWarnings("unchecked")
   public Object executeLookup(ControllerContext context) throws Throwable
   {
      validate(context);
      return internalExecute(context);
   }

   /**
    * Execute internal lookup on context.
    *
    * @param context the target context
    * @return lookup value
    * @throws Throwable for any error
    */
   public abstract Object internalExecute(ControllerContext context) throws Throwable;

   /**
    * Get the from string
    *
    * @return the state string
    */
   public String getFromString()
   {
      return fromString;
   }

   @SuppressWarnings("unchecked")
   public boolean equals(Object object)
   {
      if (object == null || object instanceof FromContextDelegate == false)
         return false;
      FromContextDelegate other = (FromContextDelegate) object;
      return fromString.equals(other.getFromString());
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append(fromString);
   }

   protected int getHashCode()
   {
      return fromString.hashCode();
   }

   protected Object readResolve() throws ObjectStreamException
   {
      return values.get(fromString);
   }

   private static abstract class KernelFromContextDelegate extends FromContextDelegate
   {
      private static final long serialVersionUID = 1L;

      protected KernelFromContextDelegate(String fromString)
      {
         super(fromString);
      }

      protected KernelFromContextDelegate(String fromString, ControllerState whenRequired)
      {
         super(fromString, whenRequired);
      }

      protected void validate(ControllerContext context)
      {
         if (context instanceof KernelControllerContext == false)
            throw new UnsupportedOperationException("Cannot execute " + getFromString() + " on underlying context: " + context);
      }
   }

   private static class NoopFromContext extends FromContextDelegate
   {
      private static final long serialVersionUID = 1L;

      public NoopFromContext(String fromString)
      {
         super(fromString);
      }

      public Object internalExecute(ControllerContext context)
      {
         throw new UnsupportedOperationException("Noop from context.");
      }
   }

   private static class NameFromContext extends FromContextDelegate
   {
      private static final long serialVersionUID = 1L;

      public NameFromContext(String fromString)
      {
         super(fromString);
      }

      public Object internalExecute(ControllerContext context)
      {
         return context.getName();
      }
   }

   private static class AliasesFromContext extends FromContextDelegate
   {
      private static final long serialVersionUID = 1L;

      public AliasesFromContext(String fromString)
      {
         super(fromString);
      }

      public Set<Object> internalExecute(ControllerContext context)
      {
         Set<Object> aliases = context.getAliases();
         return aliases != null ? Collections.unmodifiableSet(aliases) : null;
      }
   }

   private static class MetaDataFromContext extends FromContextDelegate
   {
      private static final long serialVersionUID = 1L;

      public MetaDataFromContext(String fromString)
      {
         super(fromString);
      }

      public MetaData internalExecute(ControllerContext context)
      {
         return context.getScopeInfo().getMetaData();
      }
   }

   private static class BeanInfoFromContext extends KernelFromContextDelegate
   {
      private static final long serialVersionUID = 1L;

      public BeanInfoFromContext(String fromString)
      {
         super(fromString, ControllerState.INSTANTIATED);
      }

      public BeanInfo internalExecute(ControllerContext context)
      {
         BeanInfo info = ((KernelControllerContext)context).getBeanInfo();
         return info != null ? new UnmodifiableBeanInfo(info) : null;
      }
   }

   private static class ScopeFromContext extends FromContextDelegate
   {
      private static final long serialVersionUID = 1L;

      public ScopeFromContext(String fromString)
      {
         super(fromString);
      }

      public ScopeKey internalExecute(ControllerContext context)
      {
         return context.getScopeInfo().getScope();
      }
   }

   private static class StateFromContext extends FromContextDelegate
   {
      private static final long serialVersionUID = 1L;

      public StateFromContext(String fromString)
      {
         super(fromString, ControllerState.NOT_INSTALLED);
      }

      public ControllerState internalExecute(ControllerContext context)
      {
         return context.getState();
      }
   }

   private static class IdFromContext extends FromContextDelegate
   {
      private static final long serialVersionUID = 1L;

      public IdFromContext(String fromString)
      {
         super(fromString);
      }

      public Object internalExecute(ControllerContext context)
      {
         // todo - change to actual id when impl
         return context.getName();
      }
   }

   private static class ThisContext extends FromContextDelegate
   {
      private static final long serialVersionUID = 1L;

      public ThisContext(String fromString)
      {
         super(fromString);
      }

      public ControllerContext internalExecute(ControllerContext context)
      {
         if (context instanceof KernelControllerContext)
            return new UnmodifiableKernelControllerContext((KernelControllerContext)context);
         return new UnmodifiableControllerContext(context);
      }
   }
}
