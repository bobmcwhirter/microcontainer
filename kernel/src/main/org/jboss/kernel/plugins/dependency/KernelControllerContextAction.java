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

import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedActionException;

import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.dependency.plugins.action.SimpleControllerContextAction;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.joinpoint.spi.Joinpoint;
import org.jboss.kernel.plugins.config.Configurator;
import org.jboss.kernel.spi.dependency.KernelController;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;
import org.jboss.kernel.spi.metadata.KernelMetaDataRepository;
import org.jboss.logging.Logger;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.stack.MetaDataStack;

/**
 * KernelControllerContextAction.
 *
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class KernelControllerContextAction extends SimpleControllerContextAction<KernelControllerContext>
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
         staticLog.warn("NO METADATA! for " + context.getName() + " with scope " + context.getScopeInfo().getScope());
      try
      {

         // Dispatch with the bean class loader if it exists
         ClassLoader tcl = Thread.currentThread().getContextClassLoader();
         try
         {
            if (cl != null && access == null)
               Thread.currentThread().setContextClassLoader(cl);

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
         }
      }
      finally
      {
         if (md != null)
            MetaDataStack.pop();
      }
   }

   protected boolean validateContext(ControllerContext context)
   {
      return (context instanceof AbstractKernelControllerContext);
   }

   protected KernelControllerContext contextCast(ControllerContext context)
   {
      return KernelControllerContext.class.cast(context);
   }

   public void installAction(KernelControllerContext context) throws Throwable
   {
      installActionInternal(context);
      setKernelControllerContext(context);
   }

   private void setKernelControllerContext(KernelControllerContext context) throws Throwable
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
            ((KernelControllerContextAware)target).setKernelControllerContext(context);
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
      unsetKernelControllerContext(context);
      uninstallActionInternal(context);
   }

   protected void uninstallActionInternal(KernelControllerContext context)
   {
   }

   private void unsetKernelControllerContext(KernelControllerContext context)
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
               log.debug("Ignored error unsetting context " + context.getName(), ignored);
            }
         }
      }
   }
}