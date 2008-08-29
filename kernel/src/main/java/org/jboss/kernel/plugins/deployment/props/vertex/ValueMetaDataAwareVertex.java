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
package org.jboss.kernel.plugins.deployment.props.vertex;

import org.jboss.beans.metadata.plugins.ValueMetaDataAware;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.kernel.plugins.deployment.props.TreeVertex;
import org.jboss.util.graph.Vertex;

/**
 * Value aware vertex.
 *
 * @param <U> exact ValueMetaDataAware type
 * @param <T> exact parent type
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public abstract class ValueMetaDataAwareVertex<U extends ValueMetaDataAware, T extends Vertex<String>> extends TreeVertex<T> implements ValueMetaDataAware
{
   protected U delegate;

   protected ValueMetaDataAwareVertex(String n)
   {
      super(n);
      delegate = createValueMetaDataAware();
   }

   /**
    * Create value meta data aware instance.
    *
    * @return ValueMetaDataAware instance
    */
   protected abstract U createValueMetaDataAware();

   public ValueMetaData getValue()
   {
      return delegate.getValue();
   }

   public void setValue(ValueMetaData value)
   {
      delegate.setValue(value);
   }
}
