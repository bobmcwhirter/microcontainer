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
package org.jboss.kernel.plugins.dependency;

import java.security.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.jboss.beans.info.spi.BeanInfo;
import org.jboss.beans.metadata.plugins.AbstractParameterMetaData;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.spi.ParameterMetaData;
import org.jboss.beans.metadata.spi.PropertyMetaData;
import org.jboss.dependency.plugins.spi.action.ControllerContextAction;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.DispatchContext;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.config.KernelConfigurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.logging.Logger;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.stack.MetaDataStack;
import org.jboss.reflect.spi.MethodInfo;
import org.jboss.reflect.spi.TypeInfo;

/**
 * KernelControllerContextAction.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class KernelControllerContextAction implements ControllerContextAction
{
   /**
    * Static log
    */
   private static final Logger staticLog = Logger.getLogger(KernelControllerContextAction.class);

   /**
    * The log
    */
   protected Logger log = Logger.getLogger(getClass());

   /**
    * Dispatch a joinpoint
    *
    * @param context   the context
    * @param joinpoint the joinpoint
    * @return the result
    * @throws Throwable for any error
    */
   static Object dispatchJoinPoint(final KernelControllerContext context, final Joinpoint joinpoint) throws Throwable
   {
      BeanMetaData metaData = context.getBeanMetaData();
      ClassLoader cl = Configurator.getClassLoader(metaData);
      AccessControlContext access = null;
      if (context instanceof AbstractKernelControllerContext)
      {
         AbstractKernelControllerContext theContext = (AbstractKernelControllerContext) context;
         access = theContext.getAccessControlContext();
      }

      KernelController controller = (KernelController) context.getController();
      KernelMetaDataRepository repository = controller.getKernel().getMetaDataRepository();
      MetaData md = repository.getMetaData(context);
      if (md != null)
         MetaDataStack.push(md);
      else
         staticLog.warn("NO METADATA! for " + context.getName() + " with scope " + context.getScope());
      try
      {

         // Dispatch with the bean class loader if it exists
         ClassLoader tcl = Thread.currentThread().getContextClassLoader();
         try
         {
            if (cl != null && access == null)
               Thread.currentThread().setContextClassLoader(cl);
            if (joinpoint instanceof KernelControllerContextAware)
               ((KernelControllerContextAware) joinpoint).setKernelControllerContext(context);

            if (access == null)
            {
               return joinpoint.dispatch();
            }
            else
            {
               DispatchJoinPoint action = new DispatchJoinPoint(joinpoint);
               try
               {
                  return AccessController.doPrivileged(action, access);
               }
               catch (PrivilegedActionException e)
               {
                  throw e.getCause();
               }
            }
         }
         finally
         {
            if (cl != null && access == null)
               Thread.currentThread().setContextClassLoader(tcl);
            if (joinpoint instanceof KernelControllerContextAware)
               ((KernelControllerContextAware) joinpoint).unsetKernelControllerContext(null);
         }
      }
      finally
      {
         if (md != null)
            MetaDataStack.pop();
      }
   }

   public void install(final ControllerContext context) throws Throwable
   {
      if (System.getSecurityManager() == null || context instanceof AbstractKernelControllerContext == false)
         installAction((KernelControllerContext) context);
      else
      {
         PrivilegedExceptionAction<Object> action = new PrivilegedExceptionAction<Object>()
         {
            public Object run() throws Exception
            {
               try
               {
                  installAction((KernelControllerContext) context);
                  return null;
               }
               catch (RuntimeException e)
               {
                  throw e;
               }
               catch (Exception e)
               {
                  throw e;
               }
               catch (Error e)
               {
                  throw e;
               }
               catch (Throwable t)
               {
                  throw new RuntimeException(t);
               }
            }
         };
         try
         {
            AccessController.doPrivileged(action);
         }
         catch (PrivilegedActionException e)
         {
            throw e.getCause();
         }
      }
   }

   public void uninstall(final ControllerContext context)
   {
      if (System.getSecurityManager() == null || context instanceof AbstractKernelControllerContext == false)
         uninstallAction((KernelControllerContext) context);
      else
      {
         PrivilegedAction<Object> action = new PrivilegedAction<Object>()
         {
            public Object run()
            {
               uninstallAction((KernelControllerContext) context);
               return null;
            }
         };
         AccessController.doPrivileged(action);
      }
   }

   public void installAction(KernelControllerContext context) throws Throwable
   {
      installActionInternal(context);
      Object target = context.getTarget();
      if (target != null)
      {
         Class<? extends KernelControllerContextAware> awareInterface = getActionAwareInterface();
         // only applying interfaces that explicitly extend KernelControllerContextAware
         if (awareInterface != null &&
               awareInterface.equals(KernelControllerContextAware.class) == false &&
               awareInterface.isAssignableFrom(target.getClass()))
         {
            ((KernelControllerContextAware) target).setKernelControllerContext(context);
         }
      }
   }

   protected void installActionInternal(KernelControllerContext context) throws Throwable
   {
   }

   protected Class<? extends KernelControllerContextAware> getActionAwareInterface()
   {
      return null;
   }

   public void uninstallAction(KernelControllerContext context)
   {
      Object target = context.getTarget();
      if (target != null)
      {
         Class<? extends KernelControllerContextAware> awareInterface = getActionAwareInterface();
         // only applying interfaces that explicitly extend KernelControllerContextAware
         if (awareInterface != null &&
               awareInterface.equals(KernelControllerContextAware.class) == false &&
               awareInterface.isAssignableFrom(target.getClass()))
         {
            try
            {
               ((KernelControllerContextAware) target).unsetKernelControllerContext(context);
            }
            catch (Exception ignored)
            {
               log.debug("Ignored error unsetting context ", ignored);
            }
         }
      }
      uninstallActionInternal(context);
   }

   protected void uninstallActionInternal(KernelControllerContext context)
   {
   }

   // DispatchContext util methods

   protected void set(KernelConfigurator configurator, DispatchContext context, PropertyMetaData property) throws Throwable
   {
      List<ParameterMetaData> params = new ArrayList<ParameterMetaData>(1);
      params.add(new AbstractParameterMetaData(property.getType(), property.getValue()));
      invoke(configurator, context, property.getName(), params);
   }

   protected Object invoke(KernelConfigurator configurator, DispatchContext context, String name, List<ParameterMetaData> params) throws Throwable
   {
      int size = (params != null) ? params.size() : 0;
      Object[] parameters = new Object[size];
      String[] signature = new String[size];
      if (size > 0)
      {
         // lazy cl lookup
         ClassLoader classLoader = context.getClassLoader();
         for (int i = 0; i < size; i++)
         {
            ParameterMetaData pmd = params.get(i);
            signature[i] = pmd.getType();
            TypeInfo typeInfo;
            if (signature[i] != null)
            {
               typeInfo = configurator.getClassInfo(signature[i], classLoader);
            }
            else
            {
               typeInfo = findTypeInfo(configurator, context.getTarget(), name, i);
               if (typeInfo != null)
               {
                  signature[i] = typeInfo.getName();
               }
            }
            parameters[i] = pmd.getValue().getValue(typeInfo, classLoader);
         }
      }
      return context.invoke(name, parameters, signature);
   }

   private TypeInfo findTypeInfo(KernelConfigurator configurator, Object target, String name, int index) throws Throwable
   {
      if (target == null)
      {
         return null;
      }
      BeanInfo beanInfo = configurator.getBeanInfo(target.getClass());
      Set<MethodInfo> methods = beanInfo.getMethods();
      Set<MethodInfo> possibleMethods = new HashSet<MethodInfo>();
      for (MethodInfo mi : methods)
      {
         if (name.equals(mi.getName()) && mi.getParameterTypes() != null && mi.getParameterTypes().length > index)
         {
            possibleMethods.add(mi);
         }
      }
      if (possibleMethods.isEmpty() || possibleMethods.size() > 1)
      {
         log.warn("Unable to determine parameter TypeInfo, method name: " + name + ", index: " + index + ", target: " + target);
         return null;
      }
      else
      {
         return possibleMethods.iterator().next().getParameterTypes()[index];
      }
   }

}