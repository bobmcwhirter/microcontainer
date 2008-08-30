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
package org.jboss.dependency.plugins;

import org.jboss.dependency.spi.Controller;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.dependency.spi.LifecycleCallbackItem;
import org.jboss.dependency.spi.dispatch.InvokeDispatchContext;
import org.jboss.logging.Logger;
import org.jboss.util.JBossStringBuilder;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class AbstractLifecycleCallbackItem implements LifecycleCallbackItem
{
   private static final Logger log = Logger.getLogger(AbstractLifecycleCallbackItem.class);
   
   ControllerState dependentState;
   ControllerState whenRequired;
   String installMethod;
   String uninstallMethod;
   Object bean;
   boolean installed;
   
   public AbstractLifecycleCallbackItem(
         Object bean, 
         ControllerState whenRequired, 
         ControllerState dependentState, 
         String installMethod, 
         String uninstallMethod)
   {
      this.bean = bean;
      this.whenRequired  = whenRequired;
      this.dependentState = dependentState;
      this.installMethod = installMethod;
      this.uninstallMethod = uninstallMethod;
   }
   
   public Object getBean()
   {
      return bean;
   }

   public ControllerState getDependentState()
   {
      return dependentState;
   }

   public ControllerState getWhenRequired()
   {
      return whenRequired;
   }

   public void install(ControllerContext ctx) throws Exception
   {
      log.trace("Invoking install callback '" + installMethod + "' on " + bean + " for target " + bean + " at " + whenRequired);
      installed = true;
      Controller controller = ctx.getController();
      ControllerContext callbackContext = controller.getContext(bean, dependentState);
      if (callbackContext instanceof InvokeDispatchContext)
      {
         try
         {
            ((InvokeDispatchContext)callbackContext).invoke(installMethod, new Object[]{ctx}, new String[]{ControllerContext.class.getName()});
         }
         catch(Throwable t)
         {
            throw new Exception("Error calling callback " + bean + " for target context " + ctx.getName(), t);
         }
      }
      else
      {
         throw new IllegalArgumentException("Cannot install " + ctx.getName() + ". Lifecycle callback context " + bean + " does not implement InvokeDispatchContext");
      }
   }

   public void uninstall(ControllerContext ctx)
   {
      if (installed)
      {
         log.trace("Invoking uninstall callback '" + installMethod + "' on " + bean + " for target " + bean + " at " + whenRequired);
         installed = false;
         Controller controller = ctx.getController();
         ControllerContext callbackContext = controller.getContext(bean, dependentState);
         if (callbackContext instanceof InvokeDispatchContext)
         {
            try
            {
               ((InvokeDispatchContext)callbackContext).invoke(uninstallMethod, new Object[]{ctx}, new String[]{ControllerContext.class.getName()});
            }
            catch (Throwable ignored)
            {
               log.warn("Ignored error uninstalling context " + ctx.getName() + "; callback=" + bean, ignored);
            }
         }
         else
         {
            log.warn("Cannot uninstall " + ctx.getName() + ". Lifecycle callback context " + bean + " does not implement InvokeDispatchContext");
         }
      }
   }
   
   public String toString()
   {
      JBossStringBuilder sb = new JBossStringBuilder("LifecycleCallbackItem-");
      sb.append(bean);
      sb.append(":");
      sb.append(whenRequired);
      return sb.toString();
   }
}
