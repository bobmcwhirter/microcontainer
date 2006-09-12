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
package org.jboss.test.deployers.attachments;

import junit.framework.Test;
import junit.framework.TestSuite;
import junit.textui.TestRunner;

import org.jboss.test.deployers.attachments.test.AttachmentsUnitTestCase;
import org.jboss.test.deployers.attachments.test.DeploymentUnitAttachmentHierarchyUnitTestCase;
import org.jboss.test.deployers.attachments.test.DeploymentUnitAttachmentsUnitTestCase;
import org.jboss.test.deployers.attachments.test.DeploymentUnitPredeterminedManagedObjectsUnitTestCase;
import org.jboss.test.deployers.attachments.test.DeploymentUnitTransientAttachmentsUnitTestCase;
import org.jboss.test.deployers.attachments.test.DeploymentUnitTransientManagedObjectsUnitTestCase;
import org.jboss.test.deployers.attachments.test.PredeterminedManagedObjectsUnitTestCase;
import org.jboss.test.deployers.attachments.test.TransientAttachmentsUnitTestCase;
import org.jboss.test.deployers.attachments.test.TransientManagedObjectsUnitTestCase;

/**
 * Attachments Test Suite.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 37459 $
 */
public class AttachmentsTestSuite extends TestSuite
{
   public static void main(String[] args)
   {
      TestRunner.run(suite());
   }

   public static Test suite()
   {
      TestSuite suite = new TestSuite("Attachments Tests");

      suite.addTest(AttachmentsUnitTestCase.suite());
      suite.addTest(PredeterminedManagedObjectsUnitTestCase.suite());
      suite.addTest(TransientManagedObjectsUnitTestCase.suite());
      suite.addTest(TransientAttachmentsUnitTestCase.suite());
      suite.addTest(DeploymentUnitAttachmentsUnitTestCase.suite());
      suite.addTest(DeploymentUnitPredeterminedManagedObjectsUnitTestCase.suite());
      suite.addTest(DeploymentUnitTransientManagedObjectsUnitTestCase.suite());
      suite.addTest(DeploymentUnitTransientAttachmentsUnitTestCase.suite());
      suite.addTest(DeploymentUnitAttachmentHierarchyUnitTestCase.suite());

      return suite;
   }
}
