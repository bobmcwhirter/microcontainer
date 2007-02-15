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
package org.jboss.test.dependency.controller.test;

import java.util.HashMap;

import junit.framework.Test;
import org.jboss.dependency.plugins.AbstractControllerContext;
import org.jboss.dependency.plugins.AbstractControllerContextActions;
import org.jboss.dependency.plugins.spi.action.ControllerContextAction;
import org.jboss.dependency.spi.ControllerMode;
import org.jboss.dependency.spi.ControllerState;

/**
 * StateConsistencyUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class StateConsistencyUnitTestCase extends AbstractDependencyTest
{
   public int FOREGROUND = 0;
   public int BACKGROUND = 1;
   
   public static Test suite()
   {
      return suite(StateConsistencyUnitTestCase.class);
   }
   
   public StateConsistencyUnitTestCase(String name)
   {
      super(name);
   }
   
   public void testConsistency() throws Throwable
   {
      final StateConsistencyControllerContext ctx = new StateConsistencyControllerContext("test");
      assertInstall(ctx, ControllerState.NOT_INSTALLED);
      Thread thread = new Thread(new Runnable()
      {
         public void run()
         {
            try
            {
               assertChange(ctx, ControllerState.PRE_INSTALL);
            }
            catch (Throwable t)
            {
            }
         }
      });
      thread.start();
      ctx.doWait(FOREGROUND);
      
      assertChange(ctx, ControllerState.NOT_INSTALLED);
      
      ctx.doSet(FOREGROUND);
      thread.join();
   }
   
   public class StateConsistencyControllerContext extends AbstractControllerContext
   {
      private int waiter = FOREGROUND;
      
      public StateConsistencyControllerContext(Object name)
      {
         super(name, new AbstractControllerContextActions(new HashMap<ControllerState, ControllerContextAction>()));
         setMode(ControllerMode.MANUAL);
      }

      public void install(ControllerState fromState, ControllerState toState) throws Throwable
      {
         super.install(fromState, toState);
         if (toState.equals(ControllerState.NOT_INSTALLED))
            return;
         doSetAndWait(BACKGROUND);
      }
      
      public void doSetAndWait(int waiter)
      {
         synchronized(this)
         {
            this.waiter = waiter;
            notifyAll();
            while (this.waiter == waiter)
            {
               try
               {
                  wait();
                  break;
               }
               catch (InterruptedException ignored)
               {
               }
            }
         }
      }
      
      public void doWait(int waiter)
      {
         synchronized(this)
         {
            while (this.waiter == waiter)
            {
               try
               {
                  wait();
                  break;
               }
               catch (InterruptedException ignored)
               {
               }
            }
         }
      }
      
      public void doSet(int waiter)
      {
         synchronized(this)
         {
            this.waiter = waiter;
            notifyAll();
         }
      }
   }
}
