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
package org.jboss.test.deployers.attachments.test;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.structure.AbstractDeploymentContext;
import org.jboss.deployers.spi.attachments.Attachments;

/**
 * TransientAttachmentsUnitTestCase.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class TransientAttachmentsUnitTestCase extends AttachmentsTest
{
   public static Test suite()
   {
      return new TestSuite(TransientAttachmentsUnitTestCase.class);
   }
   
   private Attachments impl;
   
   public TransientAttachmentsUnitTestCase(String name)
   {
      super(name);
      AbstractDeploymentContext context = new AbstractDeploymentContext("attachments");
      impl = context.getTransientAttachments();
   }
   
   protected Attachments getAttachments()
   {
      return impl;
   }

   protected Attachments getMutable()
   {
      return impl;
   }

}
