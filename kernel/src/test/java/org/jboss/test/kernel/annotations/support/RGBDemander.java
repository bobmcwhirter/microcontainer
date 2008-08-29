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
package org.jboss.test.kernel.annotations.support;

import org.jboss.beans.metadata.api.annotations.Constructor;
import org.jboss.beans.metadata.api.annotations.InstallMethod;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class RGBDemander
{
   @Red @Green private RGBSupplier rg;
   private RGBSupplier rb;
   private RGBSupplier gb;
   private RGBSupplier rgb;

   @Constructor
   public RGBDemander(@Green @Blue RGBSupplier gb)
   {
      this.gb = gb;
   }

   public RGBSupplier getRb()
   {
      return rb;
   }

   @Red @Blue
   public void setRb(RGBSupplier rb)
   {
      this.rb = rb;
   }

   public RGBSupplier getGb()
   {
      return gb;
   }

   public void setGb(RGBSupplier gb)
   {
      this.gb = gb;
   }

   @InstallMethod
   public void install(@Red @Green @Blue RGBSupplier rgb)
   {
      this.rgb = rgb;
   }

   public RGBSupplier getRgb()
   {
      return rgb;
   }

   public RGBSupplier getRg()
   {
      return rg;
   }
}