/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.util.builder;

import java.security.PrivilegedAction;

/**
 * AbstractBuilder.
 * 
 * @param <T> the type to be built
 * @author <a href="adrian@jboss.org">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class AbstractBuilder<T> implements PrivilegedAction<T>
{
   /** The factory class */
   private Class<T> factoryClass;
   
   /** The default factory */
   private String defaultFactory;
   
   /**
    * Create a new AbstractBuilder.
    * 
    * @param factoryClass the factory class
    * @param defaultFactory the default factory
    * @throws IllegalArgumentException for a null parameter
    */
   public AbstractBuilder(Class<T> factoryClass, String defaultFactory)
   {
      if (factoryClass == null)
         throw new IllegalArgumentException("Null factory class");
      if (defaultFactory == null)
         throw new IllegalArgumentException("Null default factory");
      this.factoryClass = factoryClass;
      this.defaultFactory = defaultFactory;
   }
   
   public T run()
   {
      try
      {
         String className = System.getProperty(factoryClass.getName(), defaultFactory);
         Class clazz = Thread.currentThread().getContextClassLoader().loadClass(className);
         Object object = clazz.newInstance();
         return factoryClass.cast(object);
      }
      catch (Throwable t)
      {
         throw new RuntimeException("Error constructing " + factoryClass.getName(), t);
      }
   }
}
