/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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
package org.jboss.beans.metadata.plugins;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlType;

import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.xb.annotations.JBossXmlChild;
import org.jboss.xb.annotations.JBossXmlChildWildcard;
import org.jboss.xb.annotations.JBossXmlChildren;
import org.jboss.xb.annotations.JBossXmlNoElements;

/**
 * Set metadata.
 * 
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
@XmlType(name="setType")
@JBossXmlNoElements
@JBossXmlChildren
({
   @JBossXmlChild(name="bean", type=AbstractBeanMetaData.class),
   @JBossXmlChild(name="array", type=AbstractArrayMetaData.class),
   @JBossXmlChild(name="collection", type=AbstractCollectionMetaData.class),
   @JBossXmlChild(name="inject", type=AbstractInjectionValueMetaData.class),
   @JBossXmlChild(name="list", type=AbstractListMetaData.class),
   @JBossXmlChild(name="map", type=AbstractMapMetaData.class),
   @JBossXmlChild(name="null", type=AbstractValueMetaData.class),
   @JBossXmlChild(name="set", type=AbstractSetMetaData.class),
   @JBossXmlChild(name="this", type=ThisValueMetaData.class),
   @JBossXmlChild(name="value", type=StringValueMetaData.class),
   @JBossXmlChild(name="value-factory", type=AbstractValueFactoryMetaData.class)
})
@JBossXmlChildWildcard(wrapper=AbstractValueMetaData.class, property="value")
public class AbstractSetMetaData extends AbstractCollectionMetaData
   implements Set<MetaDataVisitorNode>, Serializable
{
   private static final long serialVersionUID = 2L;

   /**
    * Create a new set value
    */
   public AbstractSetMetaData()
   {
   }

   public boolean add(MetaDataVisitorNode o)
   {
      if (collection.contains(o))
         return false;
      return super.add(o);
   }

   public boolean addAll(Collection<? extends MetaDataVisitorNode> c)
   {
      boolean changed = false;
      if (c != null && c.size() > 0)
      {
         for (MetaDataVisitorNode o : c)
         {
            if (collection.contains(o) == false)
            {
               if (super.add(o))
                  changed = true;
            }
         }
      }
      return changed;
   }

   protected Set<Object> getDefaultInstance()
   {
      return new HashSet<Object>();
   }

   public AbstractSetMetaData clone()
   {
      return (AbstractSetMetaData)super.clone();
   }
}