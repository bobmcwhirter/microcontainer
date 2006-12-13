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
package org.jboss.aop.microcontainer.integration;

import org.jboss.joinpoint.plugins.BasicJoinpointFactory;
import org.jboss.joinpoint.spi.ConstructorJoinpoint;
import org.jboss.joinpoint.spi.JoinpointException;
import org.jboss.reflect.spi.ClassInfo;
import org.jboss.reflect.spi.ConstructorInfo;

/**
 * The existence of this class is the signal to the kernel that we want to use the aop-mc integration.
 * When deployed in jboss the AOPConstructorJoinpoint will be deployed as part of the AspectDeployer,
 * so we use "normal" constructor joinpoints until that has been deployed
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public class AOPJoinpointFactory extends BasicJoinpointFactory
{
   /**
    * Create a new AOPJoinpointFactory.
    * 
    * @param classInfo
    */
   public AOPJoinpointFactory(ClassInfo classInfo)
   {
      super(classInfo);
   }

   public ConstructorJoinpoint getConstructorJoinpoint(ConstructorInfo constructorInfo) throws JoinpointException
   {
      return new AOPConstructorJoinpoint(constructorInfo);
   }
}
