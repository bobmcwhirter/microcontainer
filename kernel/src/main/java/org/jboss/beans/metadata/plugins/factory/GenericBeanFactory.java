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

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Map;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.info.spi.PropertyInfo;
import org.jboss.beans.metadata.spi.ValueMetaData;
import org.jboss.beans.metadata.spi.factory.AbstractBeanFactory;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.plugins.dependency.AbstractKernelControllerContext;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;
import org.jboss.logging.Logger;

/**
 * Bean factory metadata.
 * 
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
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
      final ClassLoader cl = getControllerContextClassLoader();
      
      AccessControlContext acc = getAccessControlContext();

      if (acc == null || System.getSecurityManager() == null)
         return createBean(cl);

      try
      {
         return AccessController.doPrivileged(new PrivilegedExceptionAction<Object>() 
         {
            public Object run() throws Exception
            {
               try
               {
                  return createBean(cl);
               }
               catch (Error e)
               {
                  throw e;
               }
               catch (Exception e)
               {
                  throw e;
               }
               catch (Throwable t)
               {
                  throw new RuntimeException("Error creating bean", t);
               }
            }
         }, acc);
      }
      catch (PrivilegedActionException e)
      {
         throw e.getCause();
      }
   }
   
   public void setKernelControllerContext(KernelControllerContext context) throws Exception
   {
      this.context = context;
   }

   public void unsetKernelControllerContext(KernelControllerContext context) throws Exception
   {
      this.context = null;
   }

   /**
    * Get the classloader from the controller context
    * 
    * @return the controller context
    */
   private ClassLoader getControllerContextClassLoader() throws Throwable
   {
      if (context != null)
      {
         if (System.getSecurityManager() == null)
         {
            try
            {
               return context.getClassLoader();
            }
            catch (Throwable t)
            {
               log.trace("Unable to retrieve classloader from " + context);
               return null;
            }
         }

         return AccessController.doPrivileged(new PrivilegedAction<ClassLoader>() 
         {
            public ClassLoader run()
            {
               try
               {
                  return context.getClassLoader();
               }
               catch (Throwable t)
               {
                  log.trace("Unable to retrieve classloader from " + context);
                  return null;
               }
            }
         });
      }
      return null;
   }

   /**
    * Get the access control context from the controller context
    * 
    * @return the access control
    */
   private AccessControlContext getAccessControlContext() throws Throwable
   {
      if (context != null)
      {
         if (context instanceof AbstractKernelControllerContext == false)
            return null;
         
         final AbstractKernelControllerContext akcc = (AbstractKernelControllerContext) context;
         if (System.getSecurityManager() == null)
            return akcc.getAccessControlContext();

         return AccessController.doPrivileged(new PrivilegedAction<AccessControlContext>() 
         {
            public AccessControlContext run()
            {
               return akcc.getAccessControlContext();
            }
         });
      }
      return null;
   }
   
   /**
    * Create a new bean
    *
    * @param cl the classloader to use
    * @return the bean
    * @throws Throwable for any error
    */
   private Object createBean(ClassLoader cl) throws Throwable
   {
      ClassLoader cl2 = cl;
      if (cl2 == null)
         cl2 = Configurator.getClassLoader(classLoader);
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
}