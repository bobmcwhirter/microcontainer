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
package org.jboss.aop.microcontainer.beans;

import org.jboss.aop.AspectManager;
import org.jboss.aop.advice.AspectDefinition;
import org.jboss.aop.advice.GenericAspectFactory;
import org.jboss.aop.advice.Scope;
import org.jboss.aop.advice.ScopeUtil;
import org.jboss.aop.instrument.Untransformable;
import org.jboss.beans.metadata.plugins.factory.GenericBeanFactory;
import org.jboss.dependency.spi.Controller;
import org.jboss.kernel.Kernel;
import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;
import org.jboss.logging.Logger;
import org.jboss.util.id.GUID;

/**
 * An Aspect.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class Aspect implements KernelControllerContextAware, Untransformable
{
   private static final Logger log = Logger.getLogger(Aspect.class);
   
   protected AspectManager manager;
   
   protected String adviceName = GUID.asString();
   
   protected String adviceBean;
   
   protected String scope;
   
   protected ManagedAspectDefinition definition;
   
   protected GenericBeanFactory advice;

   protected Controller controller;

   /** The name of this bean */
   protected String myname;
   
   /** The name of the AspectDefinition. If not lazy, use myname. If lazy, use adviceBean */
   protected String aspectDefName;
   
   protected Kernel kernel;
   
   /**
    * Get the adviceName.
    * 
    * @return the adviceName.
    */
   public String getAdviceName()
   {
      return adviceName;
   }

   /**
    * Set the adviceName.
    * 
    * @param adviceName The adviceName to set.
    */
   public void setAdviceName(String adviceName)
   {
      this.adviceName = adviceName;
   }

   /**
    * Get the adviceBean.
    * 
    * @return the adviceBean.
    */
   public String getAdviceBean()
   {
      return adviceBean;
   }

   /**
    * Set the adviceBean.
    * 
    * @param adviceBean the adviceBean.
    */
   public void setAdviceBean(String adviceBean)
   {
      this.adviceBean = adviceBean;
   }

   /**
    * Get the definition.
    * 
    * @return the definition.
    */
   public AspectDefinition getDefinition()
   {
      return definition;
   }

   /**
    * Get the manager.
    * 
    * @return the manager.
    */
   public AspectManager getManager()
   {
      return manager;
   }

   /**
    * Set the manager.
    * 
    * @param manager The manager to set.
    */
   public void setManager(AspectManager manager)
   {
      this.manager = manager;
   }
   
   /**
    * Get the advice.
    * 
    * @return the advice.
    */
   public GenericBeanFactory getAdvice()
   {
      return advice;
   }

   /**
    * Set the advice.
    * 
    * @param advice The advice to set.
    */
   public void setAdvice(GenericBeanFactory advice)
   {
      this.advice = advice;
   }

   /**
    * Get the scope.
    * 
    * @return the scope.
    */
   public String getScope()
   {
      return scope;
   }

   /**
    * Set the scope.
    * 
    * @param scope The scope to set.
    */
   public void setScope(String scope)
   {
      this.scope = scope;
   }

   public void setKernelControllerContext(KernelControllerContext context) throws Exception
   {
      myname = (String)context.getName();
      controller = context.getController();
      kernel = context.getKernel();
   }

   public void unsetKernelControllerContext(KernelControllerContext context) throws Exception
   {
      controller = null;
   }

   public void install(GenericBeanFactory factory) throws Exception
   {
      this.advice = factory;
      start();
   }
   
   public void start() throws Exception
   {
      if (definition == null)
      {
         aspectDefName = (adviceBean != null) ? adviceBean : myname; 
         if (manager == null)
            throw new IllegalArgumentException("Null manager");
         Scope theScope = ScopeUtil.parse(scope);
         if (advice != null)
         {
            definition = new ManagedAspectDefinition(aspectDefName, theScope, new GenericBeanAspectFactory(adviceName, advice));
         }
         else if (adviceBean != null && controller != null)
         {
            definition = new ManagedAspectDefinition(aspectDefName, theScope, new GenericBeanAspectFactory(aspectDefName, advice), false);
         }
         else
         {
            definition = new ManagedAspectDefinition(aspectDefName, theScope, new GenericAspectFactory(adviceName, null));
         }
         manager.addAspectDefinition(definition);
      }
      
      if (adviceBean != null && advice != null) 
      {
         definition.setDeployed(true);
         GenericBeanAspectFactory factory = (GenericBeanAspectFactory)definition.getFactory();
         factory.setBeanFactory(advice);
      }
      log.debug("Bound aspect " + aspectDefName + "; deployed:" + definition.isDeployed());
   }
   
   
   public void uninstall() throws Exception
   {
      stop();
   }
   
   
   public void stop()
   {
      aspectDefName = (adviceBean != null) ? adviceBean : myname; 
      log.debug("Unbinding aspect " + aspectDefName);
      manager.removeAspectDefinition(aspectDefName);
      if (definition != null)
      {
         definition.undeploy();
         definition = null;
      }
   }   
}