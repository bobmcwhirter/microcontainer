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

import org.jboss.metadata.spi.repository.MetaDataRepository;
import org.jboss.metadata.spi.repository.MutableMetaDataRepository;
import org.jboss.repository.plugins.basic.BasicMetaDataContextFactory;
import org.jboss.repository.spi.MetaDataContext;
import org.jboss.repository.spi.MetaDataContextFactory;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision$
 */
public class AOPMetaDataContextFactory extends BasicMetaDataContextFactory implements MetaDataContextFactory
{
   MetaDataContextFactory delegate;
   public MetaDataContext getMetaDataContext(ClassLoader beanLoader, MetaDataRepository repository, String beanName)
   {
      MetaDataContextFactory factoryDelegate = getMetaDataContextFactoryDelegate(beanLoader);
      if (factoryDelegate == null)
      {
         return super.getMetaDataContext(beanLoader, repository, beanName);
      }
      else
      {
         return factoryDelegate.getMetaDataContext(beanLoader, repository, beanName);
      }
   }
   
   private synchronized MetaDataContextFactory getMetaDataContextFactoryDelegate(ClassLoader beanLoader)
   {
      if (delegate != null)
      {
         return delegate;
      }
      
      Class clazz = AOPDeployedChecker.getClassIfExists(
            beanLoader, 
            "org.jboss.aop.microcontainer.integration.AOPMetaDataContextFactoryDelegate");
      
      if (clazz == null)
      {
         return null;
      }
      
      try
      {
         delegate = (MetaDataContextFactory)clazz.newInstance();
      }
      catch (Exception e)
      {
         throw new RuntimeException("Error instantiating AOPMetaDataContextFactoryDelegate", e);
      }
      
      return delegate;
   }
   
   
}
