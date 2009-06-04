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
package org.jboss.beans.metadata.spi;


/**
 * Metadata about construction of a bean. If absent from the BeanMetaData, the default constructor will be called.
 * This is used to specify the following types of construction, with a description of what values are set.
 * 
 * <table border="1">
 * <tr valign="top">
 *   <td><b><i>Type of construction/Values set</i></b></td>
 *   <td><b>{@link #getValue()}</b></td>
 *   <td><b>{@link #getFactory()}</b></td>
 *   <td><b>{@link #getFactoryClass()}</b></td>
 *   <td><b>{@link #getFactoryMethod()}</b></td>
 *   <td><b>{@link #getParameters()}</b></td>
 *   <td><b>{@link #getAnnotations()}</b></td>
 * </tr>
 * <tr valign="top">
 *   <td><b>Normal constructor</b> - The constructor on the bean class will be called</td>
 *   <td>No</td>
 *   <td>No</td>
 *   <td>No</td>
 *   <td>No</td>
 *   <td>Optional, present if we want to pass parameters in</td>
 *   <td>Optional, present if we want to annotate the contructor</td>
 * </tr>
 * <tr valign="top">
 *   <td><b>Static Factory</b> - A static method is called to create the bean</td>
 *   <td>No</td>
 *   <td>No</td>
 *   <td>Yes, if the factory method is not on the bean clas itself</td>
 *   <td>Yes</td>
 *   <td>Optional, present if we want to pass parameters in</td>
 *   <td>Optional, present if we want to annotate the contructor</td>
 * </tr>
 * <tr valign="top">
 *   <td><b>Non-Static Factory</b> - a method on another bean is called to create the bean instance</td>
 *   <td>No</td>
 *   <td>Yes, contains the name of the Microcontainer bean containing the factory method</td>
 *   <td>No</td>
 *   <td>Yes</td>
 *   <td>Optional, present if we want to pass parameters in</td>
 *   <td>Optional, present if we want to annotate the contructor</td>
 * </tr>
 * <tr valign="top">
 *   <td><b>Value</b> - Used to create a bean from a value. This can be another bean, an injection or a Set or Map</td>
 *   <td>Yes</td>
 *   <td>No</td>
 *   <td>No</td>
 *   <td>No</td>
 *   <td>Optional, present if we want to pass parameters in</td>
 *   <td>Optional, present if we want to annotate the contructor</td>
 * </tr>
 * </table>
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision$
 */
public interface ConstructorMetaData extends ParameterizedMetaData, FeatureMetaData
{
   /**
    * Get the value shat should be used to create the bean
    *
    * @return the value, or null if not present
    */
   ValueMetaData getValue();
   
   /**
    * Get the non-static factory that should be used to create the bean
    *
    * @return the factory, or null if not present
    */
   ValueMetaData getFactory();

   /**
    * Get the static factory class that should be used to create the bean
    *
    * @return the class name of the factory, or null if not present
    */
   String getFactoryClass();
   
   /**
    * Get the factory method that should be used to create the bean
    *
    * @return the factory method, or null if not present
    */
   String getFactoryMethod();
}
