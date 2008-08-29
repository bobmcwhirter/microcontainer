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
package org.jboss.kernel.plugins.deployment.xml;

import java.util.HashSet;
import java.util.Set;

import javax.xml.namespace.QName;

import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractDependencyMetaData;
import org.jboss.beans.metadata.spi.DependencyMetaData;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementInterceptor;

/**
 * BeanDependsInterceptor.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 55527 $
 */
public class BeanDependsInterceptor extends DefaultElementInterceptor
{
   /** The interceptor */
   public static final BeanDependsInterceptor INTERCEPTOR = new BeanDependsInterceptor();

   public void add(Object parent, Object child, QName name)
   {
      AbstractBeanMetaData bean = (AbstractBeanMetaData) parent;
      AbstractDependencyMetaData dependency = (AbstractDependencyMetaData) child;
      Set<DependencyMetaData> depends = bean.getDepends();
      if (depends == null)
      {
         depends = new HashSet<DependencyMetaData>();
         bean.setDepends(depends);
      }
      depends.add(dependency);
   }
}
