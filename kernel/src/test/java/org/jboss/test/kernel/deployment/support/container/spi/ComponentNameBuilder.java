/*
 * JBoss, Home of Professional Open Source
 * Copyright 2007, Red Hat Middleware LLC, and individual contributors
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
package org.jboss.test.kernel.deployment.support.container.spi;

/**
 * Abstraction for building/parsing component names
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public interface ComponentNameBuilder
{
   /**
    * Create a globally unique mc bean name
    * @param baseName - base name used to derive unique bean name
    * @param compName - the component name used to derive unique bean name
    * @param compID - component id used to derive unique bean name
    * @return the unique mc bean name
    */
   public String buildName(String baseName, String compName, long compID);
   /**
    * Parse a mc bean name for the component id
    * @param name - the mc bean name
    * @return the component id
    * @throws NumberFormatException
    */
   public long getComponentID(String name) throws NumberFormatException;
}
