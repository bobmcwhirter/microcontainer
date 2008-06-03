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

import org.jboss.aop.AspectManager;
import org.jboss.aop.microcontainer.lifecycle.LifecycleCallbackBinding;
import org.jboss.dependency.spi.ControllerState;
import org.jboss.util.id.GUID;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class LifecycleBinding
{
   protected String name;
   protected AspectManager manager;   
   private String classes;
   private String expr;
   private String callbackBean;
   private ControllerState state;
   private String installMethod = "install";
   private String uninstallMethod = "uninstall";

   public void setClasses(String classes)
   {
      this.classes = classes;
   }
   
   public String getExpr()
   {
      return expr;
   }

   public void setExpr(String classes)
   {
      this.expr = classes;
   }
   
   public void setCallbackBean(String name)
   {
      callbackBean = name;
   }
   
   public void setManager(AspectManager manager)
   {
      this.manager = manager;
   }
   
   public void setState(ControllerState state)
   {
      this.state = state;
   }
   
   public void setInstallMethod(String installMethod)
   {
      this.installMethod = installMethod;
   }

   public void setUninstallMethod(String uninstallMethod)
   {
      this.uninstallMethod = uninstallMethod;
   }

   public void start() throws Exception
   {
      if (expr == null && classes == null)
         throw new IllegalArgumentException("Null classes and null expr");
      if (expr != null && classes != null)
         throw new IllegalArgumentException("Both classes and expr were set");
      if (manager == null)
         throw new IllegalArgumentException("Null manager");
      if (callbackBean == null)
         throw new IllegalArgumentException("Null callback bean");
      if (state == null)
         throw new IllegalArgumentException("Null controller state");
      if (name == null)
         name = GUID.asString();

      
      LifecycleCallbackBinding binding = new LifecycleCallbackBinding(name, classes, expr, state);
      binding.addLifecycleCallback(callbackBean, installMethod, uninstallMethod);
      manager.addLifecycleBinding(binding);
   }

   
   public void stop() throws Exception
   {
      manager.removeLifecycleBinding(name);
   }

}
