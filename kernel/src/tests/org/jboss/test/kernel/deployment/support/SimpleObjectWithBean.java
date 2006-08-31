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
package org.jboss.test.kernel.deployment.support;

import java.io.Serializable;

/**
 * A simple object with all possible bean as meta data
 *
 * @author <a href="ales.justin@gmail.com">Ales Justin</a>
 */
public class SimpleObjectWithBean implements Serializable
{
   // Constants -----------------------------------------------------

   private static final long serialVersionUID = 3258126972906387766L;

   // Attributes ----------------------------------------------------

   private SimpleBean simpleBean;

   public SimpleObjectWithBean()
   {
   }

   public SimpleObjectWithBean(SimpleBean simpleBean)
   {
      this.simpleBean = simpleBean;
   }

   public SimpleBean getSimpleBean()
   {
      return simpleBean;
   }

   public void setSimpleBean(SimpleBean simpleBean)
   {
      this.simpleBean = simpleBean;
   }

}
