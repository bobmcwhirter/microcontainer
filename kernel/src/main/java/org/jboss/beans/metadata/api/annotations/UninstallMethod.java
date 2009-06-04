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
 * Unnstall method defined within the bean class itself. Several methods within the bean can be
 * annotated with this annotation. By default these will be called when the bean is uninstalled from the
 * {@link org.jboss.dependency.spi.ControllerState#INSTALLED} state. 
 *
 * For example this configuration:
 * <pre>
 * &#64;Bean(name="SomeBean")
 * public class MyBean
 * {
 *    &#64;Uninstall
 *    public void uninstallation()
 *    {
 *    }
 * }
 * </pre>
 * When SomeBean enters the INSTALLED state, the Microcontainer calls the <code>uninstallation</code> method.
 * You can also specify parameters if necessary, see {@link Constructor} for an example.
 *
 * @see ExternalInstall
 * @see ExternalInstalls
 * @see org.jboss.beans.metadata.spi.BeanMetaData#getUninstalls()
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface UninstallMethod
{
   /**
    * Get the dependant state.
    *
    * @return the dependant state
    */
   String dependantState() default "";

   /**
    * Get when required state.
    *
    * @return the when required state
    */
   String whenRequired() default "";
}
