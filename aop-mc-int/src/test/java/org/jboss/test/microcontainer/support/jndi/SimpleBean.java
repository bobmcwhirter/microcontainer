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
package org.jboss.test.microcontainer.support.jndi;

import java.io.Serializable;

/**
 * Test of the jndi binding aspect
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision:$
 */
public class SimpleBean implements ISimpleBean, Serializable
{
   public static final long serialVersionUID = 1;
   private String prop1;
   private int prop2;
   private float prop3;

   public String getProp1()
   {
      return prop1;
   }
   public void setProp1(String prop1)
   {
      this.prop1 = prop1;
   }
   public int getProp2()
   {
      return prop2;
   }
   public void setProp2(int prop2)
   {
      this.prop2 = prop2;
   }
   public float getProp3()
   {
      return prop3;
   }
   public void setProp3(float prop3)
   {
      this.prop3 = prop3;
   }
}
