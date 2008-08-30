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
package org.jboss.kernel.api.dependency;

import org.jboss.kernel.plugins.dependency.MatcherFactoryBuilder;

/**
 * Matcher factory.
 * Holding the obvious Matcher implementations.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class MatcherFactory
{
   /** The singleton builder */
   private static final MatcherFactoryBuilder builder = new MatcherFactoryBuilder();

   /**
    * Get the matcher factory
    *
    * @return the instance
    */
   public static final MatcherFactory getInstance()
   {
      return builder.create();
   }

   /**
    * Add matcher transfomer.
    *
    * @param key the key
    * @param transformer the transfomer
    */
   public abstract void addMatcherTransfomer(String key, MatcherTransformer transformer);

   /**
    * Create the Matcher.
    *
    * @param transformer the transformer
    * @param value the value to wrap
    * @return new Matcher instance
    */
   public abstract Matcher createMatcher(String transformer, Object value);
}
