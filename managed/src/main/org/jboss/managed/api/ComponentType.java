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
package org.jboss.managed.api;

import java.io.Serializable;

/**
 * A simple type/subtype key for a ManagedComponent. Example
 * type/subtypes include: DataSource/{XA,LocalTx,NoTX},
 * JMSDestination/{Queue,Topic},
 * EJB/{StatelessSession,StatefulSession,Entity,MDB},
 * MBean/{Standard,XMBean,Dynamic},
 * ...
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision$
 */
public class ComponentType
   implements Serializable
{
   private static final long serialVersionUID = 1;
   private String type;
   private String subtype;

   public ComponentType()
   {
      
   }
   public ComponentType(String type, String subtype)
   {
      this.type = type;
      this.subtype = subtype;
   }

   public String getType()
   {
      return type;
   }
   public void setType(String type)
   {
      this.type = type;
   }

   public String getSubtype()
   {
      return subtype;
   }
   public void setSubtype(String subtype)
   {
      this.subtype = subtype;
   }

   @Override
   public int hashCode()
   {
      int hashCode = 1;
      if( type != null )
         hashCode += type.hashCode();
      if( subtype != null )
         hashCode += subtype.hashCode();
      return hashCode;
   }
   @Override
   public boolean equals(Object obj)
   {
      if( this == obj )
         return true;
      if( (obj instanceof ComponentType) == false )
         return false;

      boolean equals = false;
      final ComponentType other = (ComponentType) obj;
      // type
      if( type != null )
      {
         equals = type.equals(other.getType());
      }
      else
      {
         equals = type == other.getType();
      }
      // subtype
      if( equals )
      {
         if( subtype != null )
         {
            equals = subtype.equals(other.getSubtype());
         }
         else
         {
            equals = subtype == other.getSubtype();
         }         
      }
      return equals;
   }

   public String toString()
   {
      StringBuilder tmp = new StringBuilder("ComponentType");
      tmp.append('{');
      tmp.append("type=");
      tmp.append(type);
      tmp.append(", subtype=");
      tmp.append(subtype);
      tmp.append('}');
      return tmp.toString();
   }
}
