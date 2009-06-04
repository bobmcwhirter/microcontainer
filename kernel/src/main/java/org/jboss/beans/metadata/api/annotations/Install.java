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
 * Define an Install callback on a bean to be notified whenever beans
 * of a particular type are installed into the Microcontainer.
 *
 * <pre>
 * &#64;Bean(name="SomeBean")
 * public class MyBean
 * {
 *    &#64;Install
 *    public void addDatasource(Datasource ds)
 *    {
 *       ...
 *    }
 * }
 * </pre>
 * Whenever a bean of type <code>Datasource</code> is installed, <code>MyBean</code>'s
 * <code>addDataSource</code> method gets called with the <code>Datasource</code> bean as the parameter.
 * 
 * @see Uninstall
 * @see org.jboss.beans.metadata.spi.BeanMetaData#getInstallCallbacks()
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.FIELD})
public @interface Install
{
   /**
    * Get the cardinality.
    * Default is no cardinality.
    *
    * @see org.jboss.dependency.spi.Cardinality
    * @return cardinality
    */
   String cardinality() default "";

   /**
    * Get when required. i.e. my state.
    * Default is Configured.
    *
    * @return String representation of the {@link org.jboss.dependency.spi.ControllerState} when required.
    */
   String whenRequired() default "Installed";

   /**
    * Get dependent state, i.e. the state the beans we are listening for must be in
    * Default is Installed.
    *
    * @return String representation of the dependenct {@link org.jboss.dependency.spi.ControllerState}.
    */
   String dependentState() default "Installed";
}
