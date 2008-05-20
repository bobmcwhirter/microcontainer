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
import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.beans.metadata.api.annotations.Start;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class WBTester
{
   @Red
   @Green
   @Inject
   private Provider rg_provider;
   private Provider rb_provider;
   private Provider gb_provider1;
   private Provider gb_provider2;

   @Constructor
   public WBTester(@Green @Blue @Inject Provider gb_provider1)
   {
      this.gb_provider1 = gb_provider1;
   }

   public Provider getRg_provider()
   {
      return rg_provider;
   }

   public Provider getRb_provider()
   {
      return rb_provider;
   }

   @Red
   @Blue
   @Inject
   public void setRb_provider(Provider rb_provider)
   {
      this.rb_provider = rb_provider;
   }

   public Provider getGb_provider1()
   {
      return gb_provider1;
   }

   public Provider getGb_provider2()
   {
      return gb_provider2;
   }

   @Start
   public void doStart(@Green @Blue @Inject Provider gb_provider2)
   {
      this.gb_provider2 = gb_provider2;
   }
}
