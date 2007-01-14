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
package org.jboss.managed.plugins;

import java.io.Serializable;
import java.util.HashMap;

import org.jboss.managed.api.Fields;

/**
 * A default implementation of the Fields interface.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class DefaultFieldsImpl
   implements Fields
{
   private static final long serialVersionUID = 1;

   private HashMap<String, Serializable> fields = new HashMap<String, Serializable>();

   public Serializable getField(String name)
   {
      return fields.get(name);
   }

   public void setField(String name, Serializable value)
   {
      fields.put(name, value);
   }

}
