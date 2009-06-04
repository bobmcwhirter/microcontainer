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
 * Used to define one or more uninstall methods defined in other beans.<p>
 * 
 * By default these will be called when the bean is uninstalled from the
 * {@link org.jboss.dependency.spi.ControllerState#INSTALLED} state, and the other bean must be in
 * the INSTALLED state. 
 *
 * For example this configuration:
 * <pre>
 * &#64;Bean(name="SomeBean")
 * &#64;ExternalUninstalls(&#64;ExternalInstall(bean="OtherBean", method="someMethod"))
 * public class MyBean
 * {
 * }
 * </pre>
 * When SomeBean is uninstalled from the INSTALLED state, the Microcontainer calls the <code>someMethod</code> method on OtherBean.
 * You can also specify parameters if necessary:
 * <pre>
 * &#64;Bean(name="SomeBean")
 * &#64;ExternalUninstalls(&#64;ExternalInstall(bean="OtherBean", method="otherMethod", parameters={@Value(thisValue=@ThisValue)}))
 * public class MyBean
 * {
 * }
 * </pre>
 * When SomeBean is uninstalled from the INSTALLED state, the Microcontainer calls the <code>otherMethod</code> method on OtherBean
 * passing in a reference to the SomeBean being uninstalled.
 *
 * @see ExternalInstall
 * @see InstallMethod
 * @see org.jboss.beans.metadata.spi.BeanMetaData#getUninstalls()
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface ExternalUninstalls
{
   /**
    * Get external uninstalls.
    *
    * @return the external uninstalls
    */
   ExternalInstall[] value();
}
