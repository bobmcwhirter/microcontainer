/*
 * JBoss, Home of Professional Open Source
 * Copyright 2006, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.example.microcontainer.aspects;

import java.util.Hashtable;
import java.util.List;
import javax.management.MBeanServerFactory;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.naming.spi.InitialContextFactory;

/**
 * We check if we are in AS or standalone.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class EnvInitialContextFactory implements InitialContextFactory
{
   public Context getInitialContext(Hashtable<?, ?> env) throws NamingException
   {
      return isApplicationServer() ? new InitialContext(env) : new MockJndiProvider(env);
   }

   protected boolean isApplicationServer()
   {
      List servers = MBeanServerFactory.findMBeanServer("jboss");
      return servers != null && servers.isEmpty() == false;
   }
}
