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
package org.jboss.beans.metadata.plugins.factory;

import java.util.Map;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.factory.AbstractBeanFactory;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;
import org.jboss.logging.Logger;

/**
 * Bean factory metadata.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @version $Revision$
 */
public class GenericBeanFactory extends AbstractBeanFactory implements KernelControllerContextAware
{
   /** The log */
   private static final Logger log = Logger.getLogger(GenericBeanFactory.class);

   /** Our context */
   protected KernelControllerContext context;

   /**
    * Create a new generic bean factory
    * 
    * @param configurator the configurator
    */
   public GenericBeanFactory(KernelConfigurator configurator)
   {
      super(configurator);
   }
   
   /**
    * Create a new bean
    *
    * @return the bean
    * @throws Throwable for any error
    */
   public Object createBean() throws Throwable
   {
      ClassLoader cl = null;
      if (classLoader == null && context != null)
      {
         try
         {
            cl = context.getClassLoader();
         }
         catch (Throwable t)
         {
            log.trace("Unable to retrieve classloader from " + context);
         }
      }
      
      if (cl == null)
         cl = Configurator.getClassLoader(classLoader);
      BeanInfo info = null;
      if (bean != null)
         info = configurator.getBeanInfo(bean, cl, accessMode);

      Joinpoint joinpoint = configurator.getConstructorJoinPoint(info, constructor, null);
      Object result = joinpoint.dispatch();
      if (info == null && result != null)
         info = configurator.getBeanInfo(result.getClass(), accessMode);

      if (properties != null && properties.size() > 0)
      {
         for (Map.Entry<String, ValueMetaData> entry : properties.entrySet())
         {
            String property = entry.getKey();
            ValueMetaData vmd = entry.getValue();
            PropertyInfo pi = info.getProperty(property);
            pi.set(result, vmd.getValue(pi.getType(), cl));
         }
      }
      invokeLifecycle("create", create, info, cl, result);
      invokeLifecycle("start", start, info, cl, result);
      return result;
   }
   
   public void setKernelControllerContext(KernelControllerContext context) throws Exception
   {
      this.context = context;
   }

   public void unsetKernelControllerContext(KernelControllerContext context) throws Exception
   {
      this.context = null;
   }
}