/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.kernel.deployment.support;

import java.io.Serializable;

/**
 * A simple annotated bean
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class FromAppBean implements Serializable
{
   private static final long serialVersionUID = 1L;

   private String constructorString;

   private String string;

   private SimpleBean bean;

   public FromAppBean()
   {
      constructorString = "()";
   }

   public FromAppBean(String string)
   {
      constructorString = string;
   }

   public String getConstructorString()
   {
      return constructorString;
   }

   public String getString()
   {
      return string;
   }

   public void setString(String string)
   {
      this.string = string;
   }

   public SimpleBean getBean()
   {
      return bean;
   }

   public void setBean(SimpleBean bean)
   {
      this.bean = bean;
   }
}
