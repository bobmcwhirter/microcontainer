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
package org.jboss.managed.api.annotation;

import org.jboss.managed.api.ManagedProperty;

/**
 * An enum for the types of management views a {@link ManagedProperty property}
 * is associated with.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public enum ViewUse
{
   /** A read-write property used for creating/updating a config */
   CONFIGURATION,
   /** A read-only type of property */
   RUNTIME,
   /** A read-only type of property that provides runtime stats */
   STATISTIC
}
