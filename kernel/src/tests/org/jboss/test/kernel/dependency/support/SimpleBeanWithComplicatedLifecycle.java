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

/**
 * A simple bean with a complicated lifecycle
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class SimpleBeanWithComplicatedLifecycle implements Serializable
{
   private static final long serialVersionUID = 3258132440433243443L;

   public int createOrder = -1;

   public int startOrder = -1;

   public int stopOrder = -1;

   public int destroyOrder = -1;

   public SimpleBeanWithLifecycle createBean;

   public SimpleBeanWithLifecycle startBean;
   
   public SimpleBeanWithLifecycle stopBean;
   
   public SimpleBeanWithLifecycle destroyBean;
   
   public void notCreate(SimpleBeanWithLifecycle bean)
   {
      createBean = bean;
      destroyBean = null;
      createOrder = SimpleBeanWithLifecycle.order.increment();
   }
   
   public void notStart(SimpleBeanWithLifecycle bean)
   {
      startBean = bean;
      stopBean = null;
      startOrder = SimpleBeanWithLifecycle.order.increment();
   }
   
   public void notStop(SimpleBeanWithLifecycle bean)
   {
      stopBean = bean;
      startBean = null;
      stopOrder = SimpleBeanWithLifecycle.order.increment();
   }
   
   public void notDestroy(SimpleBeanWithLifecycle bean)
   {
      destroyBean = bean;
      createBean = null;
      destroyOrder = SimpleBeanWithLifecycle.order.increment();
   }
}