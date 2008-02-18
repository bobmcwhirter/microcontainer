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
package org.jboss.test.kernel.config.support;

import java.util.Date;
import java.math.BigInteger;
import java.math.BigDecimal;

import org.jboss.beans.metadata.api.annotations.StringValue;

/**
 * A simple bean
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
public class FromStringSimpleBean extends SimpleBean
{
   /** The serialVersionUID */
   private static final long serialVersionUID = 1L;

   public FromStringSimpleBean()
   {
      super();
   }

   // Public --------------------------------------------------------

   @StringValue("Z")
   public void setEnumProperty(Alphabet enumProperty)
   {
      super.setEnumProperty(enumProperty);
   }

   @StringValue("StringValue")
   public void setOverloadedProperty(String overloadedProperty)
   {
      super.setOverloadedProperty(overloadedProperty);
   }

   @StringValue("StringValue")
   public void setAString(String string)
   {
      super.setAString(string);
   }

   @StringValue("123")
   public void setAShort(Short short1)
   {
      super.setAShort(short1);
   }

   @StringValue("123")
   public void setAshort(short ashort)
   {
      super.setAshort(ashort);
   }

   @StringValue("1234")
   public void setAnInt(Integer anInt)
   {
      super.setAnInt(anInt);
   }

   @StringValue("1234")
   public void setAnint(int anint)
   {
      super.setAnint(anint);
   }

   @StringValue("12345")
   public void setALong(Long long1)
   {
      super.setALong(long1);
   }

   @StringValue("12345")
   public void setAlong(long along)
   {
      super.setAlong(along);
   }

   @StringValue("3.14")
   public void setAFloat(Float float1)
   {
      super.setAFloat(float1);
   }

   @StringValue("3.14")
   public void setAfloat(float afloat)
   {
      super.setAfloat(afloat);
   }

   @StringValue("3.14e12")
   public void setADouble(Double double1)
   {
      super.setADouble(double1);
   }

   @StringValue("3.14e12")
   public void setAdouble(double adouble)
   {
      super.setAdouble(adouble);
   }

   @StringValue("Mon Jan 01 00:00:00 CET 2001")
   public void setADate(Date date)
   {
      super.setADate(date);
   }

   @StringValue("a")
   public void setACharacter(Character character)
   {
      super.setACharacter(character);
   }

   @StringValue("a")
   public void setAchar(char achar)
   {
      super.setAchar(achar);
   }

   @StringValue("12")
   public void setAByte(Byte byte1)
   {
      super.setAByte(byte1);
   }

   @StringValue("12")
   public void setAbyte(byte abyte)
   {
      super.setAbyte(abyte);
   }

   @StringValue(value="12345", type="java.lang.Long")
   public void setANumber(Number number)
   {
      super.setANumber(number);
   }

   @StringValue("true")
   public void setABoolean(Boolean boolean1)
   {
      super.setABoolean(boolean1);
   }

   @StringValue("true")
   public void setAboolean(boolean aboolean)
   {
      super.setAboolean(aboolean);
   }

   @StringValue("123456")
   public void setABigInteger(BigInteger bigInteger)
   {
      super.setABigInteger(bigInteger);
   }

   @StringValue("12e4")
   public void setABigDecimal(BigDecimal bigDecimal)
   {
      super.setABigDecimal(bigDecimal);
   }
}
