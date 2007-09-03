/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.reliance.drools.core;

import java.io.Serializable;
import java.util.Map;

import org.drools.RuleBase;
import org.drools.StatefulSession;
import org.drools.event.AgendaEventListener;
import org.drools.event.RuleFlowEventListener;
import org.drools.event.WorkingMemoryEventListener;
import org.drools.spi.GlobalResolver;
import org.jboss.beans.metadata.plugins.annotations.Destroy;
import org.jboss.beans.metadata.plugins.annotations.Inject;
import org.jboss.beans.metadata.plugins.annotations.Install;
import org.jboss.beans.metadata.plugins.annotations.Uninstall;
import org.jboss.beans.metadata.plugins.annotations.Constructor;
import org.jboss.dependency.spi.Controller;
import org.jboss.kernel.plugins.bootstrap.basic.KernelConstants;

/**
 * Managed working memory.
 *
 * @author Gavin King
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ManagedWorkingMemory implements Serializable
{
   private static final long serialVersionUID = -1746942080571374743L;

   private transient RuleBase ruleBase;
   private transient Controller controller;

   private transient Map<String, String> names;

   private StatefulSession statefulSession;

   @Constructor
   public ManagedWorkingMemory(
         @Inject RuleBase ruleBase,
         @Inject(bean = KernelConstants.KERNEL_CONTROLLER_NAME) Controller controller)
   {
      this.ruleBase = ruleBase;
      this.controller = controller;
   }

   public void setNames(Map<String, String> names)
   {
      this.names = names;
   }

   public StatefulSession getStatefulSession()
   {
      if (statefulSession == null)
      {
         statefulSession = ruleBase.newStatefulSession();
         statefulSession.setGlobalResolver(createGlobalResolver(statefulSession.getGlobalResolver()));
      }
      return statefulSession;
   }

   @Install
   public void addEventListener(AgendaEventListener agendaEventListener)
   {
      getStatefulSession().addEventListener(agendaEventListener);
   }

   @Install
   public void addEventListener(WorkingMemoryEventListener workingMemoryEventListener)
   {
      getStatefulSession().addEventListener(workingMemoryEventListener);
   }

   @Install
   public void addEventListener(RuleFlowEventListener ruleFlowEventListener)
   {
      getStatefulSession().addEventListener(ruleFlowEventListener);
   }

   @Uninstall
   public void removeEventListener(AgendaEventListener agendaEventListener)
   {
      getStatefulSession().removeEventListener(agendaEventListener);
   }

   @Uninstall
   public void removeEventListener(WorkingMemoryEventListener workingMemoryEventListener)
   {
      getStatefulSession().removeEventListener(workingMemoryEventListener);
   }

   @Uninstall
   public void removeEventListener(RuleFlowEventListener ruleFlowEventListener)
   {
      getStatefulSession().removeEventListener(ruleFlowEventListener);
   }

   protected GlobalResolver createGlobalResolver(GlobalResolver delegate)
   {
      NamingKernelGlobalResolver resolver = new ConstantsKernelGlobalResolver(delegate, controller);
      if (names != null)
         resolver.addNames(names);
      return resolver;
   }

   @Destroy
   public void destroy()
   {
      if (statefulSession != null)
         statefulSession.dispose();
   }

}
