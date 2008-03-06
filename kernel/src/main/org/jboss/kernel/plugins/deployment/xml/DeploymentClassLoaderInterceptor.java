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

import java.util.List;
import java.util.ArrayList;
import javax.xml.namespace.QName;

import org.jboss.beans.metadata.plugins.AbstractClassLoaderMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.kernel.plugins.deployment.AbstractKernelDeployment;
import org.jboss.xb.binding.sunday.unmarshalling.DefaultElementInterceptor;

/**
 * DeploymentClassLoaderInterceptor.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class DeploymentClassLoaderInterceptor extends DefaultElementInterceptor
{
   /** The interceptor */
   public static final DeploymentClassLoaderInterceptor INTERCEPTOR = new DeploymentClassLoaderInterceptor();
   
   public void add(Object parent, Object child, QName name)
   {
      AbstractKernelDeployment deployment = (AbstractKernelDeployment) parent;
      AbstractClassLoaderMetaData classloader = (AbstractClassLoaderMetaData) child;
      deployment.setClassLoader(classloader);
      // add classloaders as value beans
      List<BeanMetaDataFactory> beans = deployment.getBeanFactories();
      if (beans == null)
      {
         beans = new ArrayList<BeanMetaDataFactory>();
         deployment.setBeanFactories(beans);
      }
   }
}
