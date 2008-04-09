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
package org.jboss.beans.metadata.plugins;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.MetaDataVisitorNode;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.logging.Logger;

/**
 * Handle nested beans util class.
 * Hidding the impl details from AbstractBeanMetaData
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class NestedBeanHandler
{
   private static Logger log = Logger.getLogger(NestedBeanHandler.class);

   private BeanMetaData root;
   private int counter = 1;

   public NestedBeanHandler(BeanMetaData root)
   {
      this.root = root;
   }

   /**
    * Check for nested beans.
    *
    * @return list of bean meta data
    */
   public List<BeanMetaData> checkForNestedBeans()
   {
      List<BeanMetaData> nestedBeans = findNestedBeans();
      if (nestedBeans.isEmpty())
      {
         return Collections.singletonList(prepareRoot(root));
      }
      else
      {
         BeanMetaData clone = (BeanMetaData)root.clone();
         List<Pair> pairs = new ArrayList<Pair>();
         addPairs(clone, pairs);
         List<BeanMetaData> result = new ArrayList<BeanMetaData>();
         for (Pair pair : pairs)
         {
            MetaDataVisitorNode previous = pair.getPrevious();
            BeanMetaData bean = pair.getBean();
            if (bean.getName() == null)
               bean.setName(generateName(previous));
            replaceWithInjection(previous, bean);
            result.add(bean);
         }
         result.add(prepareRoot(clone));
         return result;
      }
   }

   /**
    * Prepare root.
    * 
    * Could be overriden to prehaps
    * allow null name on the root as well.
    *
    * @param bean the root bean or its clone
    * @return root
    */
   protected BeanMetaData prepareRoot(BeanMetaData bean)
   {
      return bean;
   }

   /**
    * Fins the nested beans.
    *
    * @return list of nested beans
    */
   private List<BeanMetaData> findNestedBeans()
   {
      List<BeanMetaData> allBeans = new ArrayList<BeanMetaData>();
      addBeans(root, allBeans);
      return allBeans;
   }

   /**
    * Add all nested beans to the list.
    *
    * @param current the current meta data visitor node child
    * @param list    the nested beans list
    */
   private void addBeans(MetaDataVisitorNode current, List<BeanMetaData> list)
   {
      for (Iterator<? extends MetaDataVisitorNode> children = current.getChildren(); children != null && children.hasNext();)
      {
         MetaDataVisitorNode next = children.next();
         addBeans(next, list);
         if (next instanceof BeanMetaData)
         {
            list.add((BeanMetaData)next);
         }
      }
   }

   /**
    * Add all nested beans to the list.
    *
    * @param current the current meta data visitor node child
    * @param list    the nested beans pair list
    */
   private void addPairs(MetaDataVisitorNode current, List<Pair> list)
   {
      for (Iterator<? extends MetaDataVisitorNode> children = current.getChildren(); children != null && children.hasNext();)
      {
         MetaDataVisitorNode next = children.next();
         addPairs(next, list);
         if (next instanceof BeanMetaData)
         {
            BeanMetaData bean = (BeanMetaData)next;
            list.add(new Pair(current, bean));
         }
      }
   }

   /**
    * Generate the name.
    *
    * @param previous the previous / parent node
    * @return generated name
    */
   protected String generateName(MetaDataVisitorNode previous)
   {
      String name;
      if (previous instanceof PropertyMetaData)
      {
         PropertyMetaData pmd = (PropertyMetaData)previous;
         name = root.getName() + "$" + pmd.getName();
      }
      else
      {
         name = root.getName() + "#" + counter;
         counter++;
      }
      return name;
   }

   /**
    * Replace bean with injection.
    *
    * @param previous the previous / parent node
    * @param bean     the bean to replace
    */
   private static void replaceWithInjection(MetaDataVisitorNode previous, BeanMetaData bean)
   {
      ValueMetaData injection = new AbstractDependencyValueMetaData(bean.getName());

      if (previous instanceof ValueMetaDataAware)
      {
         ValueMetaDataAware vmda = (ValueMetaDataAware)previous;
         vmda.setValue(injection);
      }
      else if (previous instanceof AbstractClassLoaderMetaData)
      {
         AbstractClassLoaderMetaData aclmd = (AbstractClassLoaderMetaData)previous;
         aclmd.setClassLoader(injection);
      }
      else if (previous instanceof AbstractListMetaData)
      {
         AbstractListMetaData almd = (AbstractListMetaData)previous;
         int index = almd.indexOf(bean);
         almd.remove(index);
         almd.add(index, injection);
      }
      else if (previous instanceof AbstractCollectionMetaData)
      {
         AbstractCollectionMetaData acmd = (AbstractCollectionMetaData)previous;
         acmd.remove(bean);
         acmd.add(injection);
      }
      else if (previous instanceof AbstractMapMetaData)
      {
         AbstractMapMetaData ammd = (AbstractMapMetaData)previous;
         for (Map.Entry<MetaDataVisitorNode, MetaDataVisitorNode> entry : ammd.entrySet())
         {
            MetaDataVisitorNode key = entry.getKey();
            MetaDataVisitorNode value = entry.getValue();

            if (key.equals(bean))
            {
               ammd.remove(key);
               ammd.put(injection, value);
            }
            if (value.equals(bean))
            {
               ammd.put(key, injection);
            }
         }
      }
      else if (previous instanceof AbstractValueMetaData)
      {
         AbstractValueMetaData avmd = (AbstractValueMetaData)previous;
         avmd.setValue(injection);
      }
      else
      {
         log.warn("Unknown previous type to do injection replacement: " + previous);
      }
   }

   /**
    * Simple pair class.
    */
   private class Pair
   {
      private MetaDataVisitorNode previous;
      private BeanMetaData bean;

      private Pair(MetaDataVisitorNode previous, BeanMetaData bean)
      {
         this.previous = previous;
         this.bean = bean;
      }

      public MetaDataVisitorNode getPrevious()
      {
         return previous;
      }

      public BeanMetaData getBean()
      {
         return bean;
      }
   }
}
