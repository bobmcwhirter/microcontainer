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
package org.jboss.test.microcontainer.support.deployers;

import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;

import javax.management.MBeanServer;
import javax.management.MBeanServerFactory;

import org.jboss.kernel.Kernel;


/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class JMXKernel
{
   Kernel kernel;
   JBossServer serverImpl;
   MBeanServer mbeanServer = MBeanServerFactory.createMBeanServer();
   
   public JMXKernel() throws Exception
   {
      try
      {
         mbeanServer = AccessController.doPrivileged(new PrivilegedExceptionAction<MBeanServer>() {
               public MBeanServer run() throws Exception 
               {
                  return MBeanServerFactory.createMBeanServer();
               }
            });
      }
      catch (PrivilegedActionException e)
      {
         throw (Exception)e.getCause();
      }
   }

   public Kernel getKernel()
   {
      return kernel;
   }

   public void setKernel(Kernel kernel)
   {
      this.kernel = kernel;
   }

   public void setServerImpl(JBossServer serverImpl)
   {
      this.serverImpl = serverImpl;
   }
   
   public MBeanServer getMbeanServer()
   {
      return mbeanServer;
   }
   
}
