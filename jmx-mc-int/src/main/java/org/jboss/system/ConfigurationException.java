/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.system;

import org.jboss.util.NestedException;

/**
 * Thrown to indicate a non-fatal configuration related problem.
 *
 * @author  <a href="mailto:jason@planet57.com">Jason Dillon</a>
 * @version <tt>$Revision$</tt>
 */
public class ConfigurationException
   extends NestedException
{
   /** The serialVersionUID */
   private static final long serialVersionUID = -1841403351768037738L;
   
   /**
    * Rethrow a throwable as a configuration exception if it isn't already.
    *
    * @param message the message
    * @param t the throwable
    * @return never
    * @throws ConfigurationException always
    */
   public static ConfigurationException rethrowAsConfigurationException(String message, Throwable t)
      throws ConfigurationException
   {
      if (t instanceof ConfigurationException)
         throw (ConfigurationException) t;
      else
         throw new ConfigurationException(message, t);
   }

   /**
    * Construct a <tt>ConfigurationException</tt> with the specified detail 
    * message.
    *
    * @param msg  Detail message.
    */
   public ConfigurationException(String msg) {
      super(msg);
   }

   /**
    * Construct a <tt>ConfigurationException</tt> with the specified detail 
    * message and nested <tt>Throwable</tt>.
    *
    * @param msg     Detail message.
    * @param nested  Nested <tt>Throwable</tt>.
    */
   public ConfigurationException(String msg, Throwable nested) {
      super(msg, nested);
   }

   /**
    * Construct a <tt>ConfigurationException</tt> with the specified
    * nested <tt>Throwable</tt>.
    *
    * @param nested  Nested <tt>Throwable</tt>.
    */
   public ConfigurationException(Throwable nested) {
      super(nested);
   }

   /**
    * Construct a <tt>ConfigurationException</tt> with no detail.
    */
   public ConfigurationException() {
      super();
   }
}
