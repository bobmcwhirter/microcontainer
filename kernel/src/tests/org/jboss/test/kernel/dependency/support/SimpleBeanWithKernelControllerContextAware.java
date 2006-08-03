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
package org.jboss.test.kernel.dependency.support;

import java.io.Serializable;

import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.kernel.spi.dependency.KernelControllerContextAware;
import org.jboss.logging.Logger;

/**
 * A simple bean that is kernel controllercontext aware
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class SimpleBeanWithKernelControllerContextAware implements Serializable, KernelControllerContextAware
{
   private static final long serialVersionUID = 3258132440433243443L;
   
   private static final Logger log = Logger.getLogger(SimpleBeanWithKernelControllerContextAware.class);

   public KernelControllerContext context;
   
   public void setKernelControllerContext(KernelControllerContext context) throws Exception
   {
      log.info("Setting context " + context);
      this.context = context;
   }
   
   public void unsetKernelControllerContext(KernelControllerContext context) throws Exception
   {
      log.info("Unsetting context " + context);
      this.context = null;
   }
}