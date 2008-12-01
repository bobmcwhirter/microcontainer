/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.aop.microcontainer.beans;

import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.kernel.spi.config.KernelConfigurator;

/**
 * CL aware GBF.
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ClassLoaderAwareGenericBeanFactory extends GenericBeanFactory
{
   private ThreadLocal<ClassLoader> pushedLoader = new ThreadLocal<ClassLoader>();
   
   public ClassLoaderAwareGenericBeanFactory(KernelConfigurator configurator)
   {
      super(configurator);
   }

   /**
    * Push classloader into thread local.
    *
    * @param loader the loader
    */
   public void pushLoader(ClassLoader loader)
   {
      pushedLoader.set(loader);
   }

   /**
    * Pop loader from thread local.
    */
   public void popLoader()
   {
      pushedLoader.set(null);
   }

   @Override
   public Object createBean() throws Throwable
   {
      ClassLoader loader = pushedLoader.get();
      if (loader == null)
      {
         return super.createBean();
      }
      else
      {
         return super.createBean(loader);
      }
   }
}
