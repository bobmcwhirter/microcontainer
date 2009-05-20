/*
 * JBoss, Home of Professional Open Source.
 * Copyright 2008, Red Hat Middleware LLC, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
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
package org.jboss.aop.microcontainer.annotations;

/**
 * Fine grained disable type.
 * e.g. LIFECYCLE only disable aop lifecycle callbacks
 *
 * @author <a href="mailto:ales.justin@jboss.org">Ales Justin</a>
 */
public enum DisabledType
{
   ALL(~0),
   LIFECYCLE(1),
   POINTCUTS(1 << 1);

   private int bits;

   DisabledType(int bits)
   {
      this.bits = bits;
   }

   /**
    * Is the type disabled for this constraint.
    *
    * @param constraint the constraint
    * @return true if disabled for this constraint
    */
   public boolean isDisabled(DisabledType constraint)
   {
      int and = bits & constraint.bits;
      return and == constraint.bits;
   }

   /**
    * Do values mark disabled usage.
    *
    * @param values the disabled values
    * @param constraint the constraint
    * @return true if disabled for this constraint
    */
   public static boolean isDisabled(DisabledType[] values, DisabledType constraint)
   {
      if (values == null || values.length == 0)
         return false;

      for (DisabledType value : values)
      {
         if (value.isDisabled(constraint))
            return true;
      }

      return false;
   }
}
