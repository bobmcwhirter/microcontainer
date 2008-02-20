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
package org.jboss.test.kernel.dependency.support;

import java.io.Serializable;

import org.jboss.beans.metadata.api.annotations.ExternalInstall;
import org.jboss.beans.metadata.api.annotations.ExternalInstalls;
import org.jboss.beans.metadata.api.annotations.ExternalUninstalls;
import org.jboss.beans.metadata.api.annotations.FromContext;
import org.jboss.beans.metadata.api.annotations.Inject;
import org.jboss.beans.metadata.api.annotations.Value;

/**
 * A simple bean
 *
 * @author <a href="ales.justin@jboss.com">Ales Justin</a>
 */
@ExternalInstalls
(
   {
      @ExternalInstall(bean = "Name1", method = "addState", parameters = {@Value(inject = @Inject(fromContext = FromContext.STATE))}, whenRequired = "PreInstall"),
      @ExternalInstall(bean = "Name1", method = "addState", parameters = {@Value(inject = @Inject(fromContext = FromContext.STATE))}, whenRequired = "Described"),
      @ExternalInstall(bean = "Name1", method = "addState", parameters = {@Value(inject = @Inject(fromContext = FromContext.STATE))}, whenRequired = "Instantiated"),
      @ExternalInstall(bean = "Name1", method = "addState", parameters = {@Value(inject = @Inject(fromContext = FromContext.STATE))}, whenRequired = "Configured"),
      @ExternalInstall(bean = "Name1", method = "addState", parameters = {@Value(inject = @Inject(fromContext = FromContext.STATE))}, whenRequired = "Create"),
      @ExternalInstall(bean = "Name1", method = "addState", parameters = {@Value(inject = @Inject(fromContext = FromContext.STATE))}, whenRequired = "Start")
   }
)
@ExternalUninstalls
(
      {
         @ExternalInstall(bean = "Name1", method = "removeState", parameters = {@Value(inject = @Inject(fromContext = FromContext.STATE))}, whenRequired = "PreInstall"),
         @ExternalInstall(bean = "Name1", method = "removeState", parameters = {@Value(inject = @Inject(fromContext = FromContext.STATE))}, whenRequired = "Described"),
         @ExternalInstall(bean = "Name1", method = "removeState", parameters = {@Value(inject = @Inject(fromContext = FromContext.STATE))}, whenRequired = "Instantiated"),
         @ExternalInstall(bean = "Name1", method = "removeState", parameters = {@Value(inject = @Inject(fromContext = FromContext.STATE))}, whenRequired = "Configured"),
         @ExternalInstall(bean = "Name1", method = "removeState", parameters = {@Value(inject = @Inject(fromContext = FromContext.STATE))}, whenRequired = "Create"),
         @ExternalInstall(bean = "Name1", method = "removeState", parameters = {@Value(inject = @Inject(fromContext = FromContext.STATE))}, whenRequired = "Start")
      }
)
public class ExternalInstallWithStateSimpleBeanImpl implements Serializable, SimpleBean
{
   // Constants -----------------------------------------------------

   private static final long serialVersionUID = 3258132440433243443L;

   // Attributes ----------------------------------------------------

   private String constructorString;

   private String string;

   // Static --------------------------------------------------------

   // Constructors --------------------------------------------------

   public ExternalInstallWithStateSimpleBeanImpl()
   {
      constructorString = "()";
   }

   public ExternalInstallWithStateSimpleBeanImpl(String string)
   {
      constructorString = string;
   }

   // Public --------------------------------------------------------

   public String getConstructorString()
   {
      return constructorString;
   }

   public String getString()
   {
      return string;
   }

   public void setString(String string)
   {
      this.string = string;
   }
}
