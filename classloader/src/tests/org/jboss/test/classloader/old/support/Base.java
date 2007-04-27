/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.classloader.old.support;

/**
 * Base.
 * 
 * @author Scott.Stark@jboss.org
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class Base
{
   public static void run() throws Exception
   {
      System.out.println("Base.CS: "+Base.class.getProtectionDomain().getCodeSource());
      Class2 o2 = new Class2();
      System.out.println("o2.CS: "+o2.getClass().getProtectionDomain().getCodeSource());
      Class0 o0 = o2.get0();
      System.out.println("o0.CS: "+o0.getClass().getProtectionDomain().getCodeSource());
   }
}
