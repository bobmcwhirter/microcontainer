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
package org.jboss.test.kernel.deployment.support;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.jboss.kernel.api.dependency.Matcher;
import org.jboss.kernel.api.dependency.MatcherTransformer;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SetDemandTransformer implements MatcherTransformer
{
   public Matcher transform(Object value)
   {
      String string = value.toString();
      return new SetDemandMatcher(new HashSet<Object>(Arrays.asList(string.split(","))));
   }

   public class SetDemandMatcher implements Matcher
   {
      private Set<Object> set;

      public SetDemandMatcher(Set<Object> set)
      {
         if (set == null)
            throw new IllegalArgumentException("Null set");
         this.set = set;
      }

      public boolean match(Object other)
      {
         return set.contains(other);
      }
   }
}