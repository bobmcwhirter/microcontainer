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
package org.jboss.kernel.plugins.dependency;

import java.util.HashMap;
import java.util.Map;

import org.jboss.kernel.api.dependency.Matcher;
import org.jboss.kernel.api.dependency.MatcherFactory;
import org.jboss.kernel.api.dependency.MatcherTransformer;
import org.jboss.logging.Logger;
import org.jboss.reflect.plugins.introspection.ReflectionUtils;

/**
 * Default Matcher factory.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class DefaultMatcherFactory extends MatcherFactory implements MatcherTransformer
{
   /** The log */
   protected Logger log = Logger.getLogger(getClass());

   /** The matcher transformers map */
   private Map<String, MatcherTransformer> transfomers = new HashMap<String, MatcherTransformer>();

   public DefaultMatcherFactory()
   {
      addMatcherTransfomer("default", this);
      addMatcherTransfomer("regexp", new RegexpMatcherTransformer());
      addMatcherTransfomer("interval", new IntervalMatcherTransformer());
   }

   public void addMatcherTransfomer(String key, MatcherTransformer transformer)
   {
      if (key == null && transformer != null)
         transfomers.put(transformer.getClass().getName(), transformer);
      
      if (key != null)
      {
         if (transformer == null)
            transfomers.remove(key);
         else
            transfomers.put(key, transformer);
      }
   }

   /**
    * Create the Matcher.
    *
    * @param transformer the transformer
    * @param value the value to wrap
    * @return new Matcher instance
    */
   public Matcher createMatcher(String transformer, Object value)
   {
      if (value instanceof Matcher)
         return (Matcher)value;

      if (transformer == null)
         transformer = "default";

      MatcherTransformer mt = transfomers.get(transformer);
      if (mt != null)
         return mt.transform(value);

      if (log.isTraceEnabled())
         log.trace("No matching transfomer key '" + transformer + "', trying to instantiate new.");

      try
      {
         mt = (MatcherTransformer)ReflectionUtils.newInstance(transformer);
         return mt.transform(value);
      }
      catch(Throwable t)
      {
         throw new IllegalArgumentException("Cannot create Matcher instance: " + t);
      }
   }

   public Matcher transform(Object value)
   {
      return new DefaultMatcher(value);
   }
}
