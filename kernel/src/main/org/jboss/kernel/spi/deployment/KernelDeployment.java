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
package org.jboss.kernel.spi.deployment;

import java.util.List;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.BeanMetaDataFactory;
import org.jboss.beans.metadata.spi.ClassLoaderMetaData;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.util.JBossInterface;

/**
 * A kernel deployment.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface KernelDeployment extends JBossInterface
{
   /**
    * Get the name of the deployment
    *
    * @return the name
    */
   String getName();
   
   /**
    * Set the name of the deployment
    *
    * @param name the name
    */
   void setName(String name);
   
   /**
    * Whether the deployment is installed
    *
    * @return true when installed
    */
   boolean isInstalled();
   
   /**
    * Set the intalled state
    *
    * @param installed true when installed
    */
   void setInstalled(boolean installed);
   
   /**
    * Get the classloader for this deployment
    *
    * @return the classloader 
    */
   ClassLoaderMetaData getClassLoader();
   
   /**
    * Get the beans in the deployment
    *
    * @return List<BeanMetaData> 
    */
   List<BeanMetaData> getBeans();
   
   /**
    * Get the bean factories in the deployment
    *
    * @return List<BeanMetaDataFactory> 
    */
   List<BeanMetaDataFactory> getBeanFactories();
   
   /**
    * Get the installed contexts
    *
    * @return the installed contexts 
    */
   List<KernelControllerContext> getInstalledContexts();
   
   /**
    * Add an installed context
    *
    * @param context the context to add 
    */
   void addInstalledContext(KernelControllerContext context);
   
   /**
    * Remove an installed context
    *
    * @param context the context to add 
    */
   void removeInstalledContext(KernelControllerContext context);
}
