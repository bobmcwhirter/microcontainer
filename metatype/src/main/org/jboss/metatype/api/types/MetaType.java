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
package org.jboss.metatype.api.types;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import org.jboss.metatype.api.values.CompositeValue;
import org.jboss.metatype.api.values.SimpleValue;
import org.jboss.metatype.api.values.TableValue;

/**
 * MetaType.
 * 
 * @param <T> the underlying type
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public interface MetaType<T extends Serializable> extends Serializable
{
   /**
    * The allowed classnames.<p>
    *
    * One of<br>
    * java.lang.Void<br>
    * java.lang.Boolean<br>
    * java.lang.Character<br>
    * java.lang.Byte<br>
    * java.lang.Short<br>
    * java.lang.Integer<br>
    * java.lang.Long<br>
    * java.lang.Float<br>
    * java.lang.Double<br>
    * java.lang.String<br>
    * java.lang.Date<br>
    * java.math.BigDecimal<br>
    * java.math.BigInteger<br>
    * {@link SimpleValue}<br>
    * {@link CompositeValue}<br>
    * {@link TableValue}
    */
   String[] ALLOWED_CLASSNAMES =
   {
      Void.class.getName(),
      Boolean.class.getName(),
      Character.class.getName(),
      Byte.class.getName(),
      Short.class.getName(),
      Integer.class.getName(),
      Long.class.getName(),
      Float.class.getName(),
      Double.class.getName(),
      String.class.getName(),
      Date.class.getName(),
      BigDecimal.class.getName(),
      BigInteger.class.getName(),
      SimpleValue.class.getName(),
      CompositeValue.class.getName(),
      TableValue.class.getName()
   };

   /**
    * Retrieve the class name of the values of this meta
    * type. It is one of those listed in ALLOWED_CLASSNAMES or
    * a (multi-dimensional) array of one of those classes.
    *
    * @return the class name
    */
   String getClassName();

   /**
    * Retrieve the name of the meta type
    *
    * @return the type name
    */
   String getTypeName();

   /**
    * Retrieve the description of the type
    *
    * @return the description
    */
   String getDescription();

   /**
    * Retrieve whether the class name of the type is an array
    *
    * @return true when it is an array or false otherwise
    */
   boolean isArray();

   /**
    * Whether the passed value is one of those described by this meta type.
    *
    * @param obj the object to test
    * @return true when it is value for this meta type, false otherwise
    */
   boolean isValue(Object obj);
}
