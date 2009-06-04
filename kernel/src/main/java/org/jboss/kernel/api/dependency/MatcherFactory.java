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

import org.jboss.kernel.plugins.dependency.IntervalMatcherTransformer;
import org.jboss.kernel.plugins.dependency.MatcherFactoryBuilder;
import org.jboss.kernel.plugins.dependency.RegexpMatcherTransformer;

/**
 * Matcher factory singleton holding the obvious Matcher implementations.
 * The default implementation comes with the following built in matchers:
 * <ul>
 *   <li><b>default</b> - A {@link Matcher} that requires an exact match between the names</li>
 *   <li><b>regexp</b> -  A {@link Matcher} that matches on regular expressions</li>
 *   <li><b>interval</b> - A {@link Matcher} that matches on an interval</li>
 * </ul>

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
    * Create the Matcher. The <code>transformer</code> String can contain one of the predefined
    * matcher implementations <code>default</code>, <code>regexp</code> or <code>interval</code>,
    * the key of a {@link MatcherTransformer} added using the {@link #addMatcherTransfomer(String, MatcherTransformer)}
    * method, or it can contain the name of a class implementing the {@link MatcherTransformer} interface that has a default
    * constructor. 
    *
    * @param transformer the transformer key
    * @param value the value to wrap
    * @return new Matcher instance
    * @throws IllegalArgumentException if no matcher could be found
    */
   public abstract Matcher createMatcher(String transformer, Object value);
}
