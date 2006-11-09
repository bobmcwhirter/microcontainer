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
package org.jboss.aop.microcontainer.integration;

import java.util.List;

import org.jboss.classadapter.plugins.dependency.AbstractDependencyBuilder;
import org.jboss.classadapter.spi.ClassAdapter;
import org.jboss.classadapter.spi.DependencyBuilder;

/**
 * The existence of this class is the signal to the kernel that we want to use the aop-mc integration.
 * When deployed in jboss the AOPDependencyBuilderDelegate will be deployed as part of the AspectDeployer,
 * so we use the "normal" dependecy builder until that has been deployed

 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AOPDependencyBuilder extends AbstractDependencyBuilder
{
   DependencyBuilder delegate;
   
   public List getDependencies(ClassAdapter classAdapter)
   {
      DependencyBuilder builder = getDependencyBuilderDelegate(classAdapter);
      if (builder == null)
      {
         return super.getDependencies(classAdapter);
      }
      return delegate.getDependencies(classAdapter);
   }
   
   private synchronized DependencyBuilder getDependencyBuilderDelegate(ClassAdapter classAdapter)
   {
      if (delegate != null)
      {
         return delegate;
      }
      
      Class clazz = AOPDeployedChecker.getClassIfExists(
            classAdapter.getClassLoader(), 
            "org.jboss.aop.microcontainer.integration.AOPDependencyBuilderDelegate");
      
      if (clazz == null)
      {
         return null;
      }
      
      try
      {
         delegate = (DependencyBuilder)clazz.newInstance();
      }
      catch (Exception e)
      {
         throw new RuntimeException("Error instantiating AOPDependencyBuilderDelegate", e);
      }
      
      return delegate;
   }
}
