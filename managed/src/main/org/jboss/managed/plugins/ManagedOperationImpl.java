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

import org.jboss.managed.api.ManagedOperation;
import org.jboss.managed.api.ManagedParameter;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.api.values.MetaValue;

/**
 * A default implementation of ManagedOperation
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class ManagedOperationImpl implements ManagedOperation
{
   private static final long serialVersionUID = 1;
   private String description;
   private Impact impact;
   private String name;
   private ManagedParameter[] parameters;
   private MetaType returnType;


   public ManagedOperationImpl(String name, String description)
   {
      this(description, name, Impact.Unknown, new ManagedParameter[0], SimpleMetaType.VOID);
   }
   public ManagedOperationImpl(String name, String description, Impact impact,
         ManagedParameter[] parameters, MetaType returnType)
   {
      super();
      this.description = description;
      this.impact = impact;
      this.name = name;
      this.parameters = parameters;
      this.returnType = returnType;
   }

   public String getDescription()
   {
      return description;
   }

   public void setDescription(String description)
   {
      this.description = description;
   }

   public Impact getImpact()
   {
      return impact;
   }

   public void setImpact(Impact impact)
   {
      this.impact = impact;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public ManagedParameter[] getParameters()
   {
      return parameters;
   }

   public void setParameters(ManagedParameter[] parameters)
   {
      this.parameters = parameters;
   }

   public MetaType getReturnType()
   {
      return returnType;
   }

   public void setReturnType(MetaType returnType)
   {
      this.returnType = returnType;
   }

   /* (non-Javadoc)
    * @see org.jboss.managed.api.ManagedOperation#invoke(MetaValue[])
    */
   public Object invoke(MetaValue... param)
   {
      // TODO Auto-generated method stub
      return null;
   }

}
