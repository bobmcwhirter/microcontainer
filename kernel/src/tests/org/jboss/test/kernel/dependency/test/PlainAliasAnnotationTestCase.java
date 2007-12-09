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
package org.jboss.test.kernel.dependency.test;

import junit.framework.Test;
import org.jboss.beans.metadata.spi.BeanMetaData;
import org.jboss.beans.metadata.plugins.AbstractBeanMetaData;
import org.jboss.test.kernel.dependency.support.AliasSimpleBeanImpl;
import org.jboss.dependency.spi.ControllerContext;
import org.jboss.dependency.spi.ControllerState;

/**
 * Plain alias tests.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class PlainAliasAnnotationTestCase extends PlainAliasTestCase
{
   public PlainAliasAnnotationTestCase(String name)
         throws Throwable
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(PlainAliasAnnotationTestCase.class);
   }

   protected void checkLastSimpleAliasInstall(ControllerContext context) throws Throwable
   {
      // do nothing
   }

   protected void buildMetaData() throws Throwable
   {
      setBeanMetaDatas(new BeanMetaData[]{new AbstractBeanMetaData("OriginalBean", AliasSimpleBeanImpl.class.getName())});
   }

   protected void checkDirectAlias() throws Throwable
   {
      // not yet registered
   }

   protected void installAlias() throws Throwable
   {
      // do nothing should be part of @annotations
   }

   protected ControllerState getDirectAliasUnistallState()
   {
      return ControllerState.ERROR;
   }
}
