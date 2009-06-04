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
package org.jboss.beans.metadata.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Used to specify a bean's demands if it has more than one demand.
 * 
 * <pre>
 * &#64;Bean(name="SomeBean")
 * &#64;Demands({&#64;Demand("OtherBean"), &#64;Demand("java:/something")})
 * public class MyBean
 * {
 * }
 * </pre>
 * <code>SomeBean</code> cannot be installed until another bean called, or another bean that supplies something
 * called, <code>OtherBean</code> has been installed AND until another bean called, or another bean that supplies something
 * called, <code>java:/something</code> has been installed.  
 *  
 * @see Supplys
 * @see Supply
 * @see Demand
 * @see org.jboss.beans.metadata.spi.BeanMetaData#getDemands()
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Demands
{
   /**
    * Get demands.
    *
    * @return the demands
    */
   Demand[] value();
}
