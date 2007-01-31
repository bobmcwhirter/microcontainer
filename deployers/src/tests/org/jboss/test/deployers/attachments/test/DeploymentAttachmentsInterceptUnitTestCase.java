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

import java.util.Map;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.jboss.deployers.plugins.advice.TrackingAdvice;
import org.jboss.deployers.plugins.deployer.AbstractDeploymentUnit;
import org.jboss.deployers.plugins.structure.AbstractDeploymentContext;
import org.jboss.deployers.spi.attachments.Attachments;
import org.jboss.deployers.spi.deployer.DeploymentUnit;

/**
 * Tests of intercepting the Attachments add/remove to collect change sets.
 * 
 * @author Scott.Stark@jboss.org
 * @version $Revision: 1.1 $
 */
public class DeploymentAttachmentsInterceptUnitTestCase
   extends AttachmentsTest
{
   public static Test suite()
   {
      return new TestSuite(DeploymentUnitAttachmentsUnitTestCase.class);
   }
   
   private DeploymentUnit unit;

   private Attachments mutable; 
   
   public DeploymentAttachmentsInterceptUnitTestCase(String name)
   {
      super(name);
      AbstractDeploymentContext context = new AbstractDeploymentContext("attachments");
      unit = new AbstractDeploymentUnit(context);
      context.setDeploymentUnit(unit);
      // Integrate with aop
      mutable = TrackingAdvice.wrapAttachments(unit);
   }

   @Override
   public void testAddAttachmentByName() throws Exception
   {
      super.testAddAttachmentByName();
      // Validate that the attachments were captured
      Map<String, Object> map = TrackingAdvice.getAttachmentsForTarget(mutable);
      assertNotNull(map);
      assertEquals("advice.map.size", 2, map.size());
      Object name1 = map.get("name1");
      assertEquals("name1.attachment", "different1", name1);
      Object name2 = map.get("name2");
      assertEquals("name2.attachment", "attachment2", name2);
      map = TrackingAdvice.clearAttachmentsForTarget(mutable);
      assertNotNull(map);
      assertEquals("advice.map.size", 2, map.size());
      map = TrackingAdvice.getAttachmentsForTarget(mutable);
      assertNull(map);
   }

   @Override
   public void testAddAttachmentByNameAndType() throws Exception
   {
      super.testAddAttachmentByNameAndType();
      // Validate that the attachments were captured
      Map<String, Object> map = TrackingAdvice.getAttachmentsForTarget(mutable);
      assertNotNull(map);
      assertEquals("advice.map.size", 2, map.size());
      Object name1 = map.get("name1");
      assertEquals("name1.attachment", "different1", name1);
      Object name2 = map.get("name2");
      assertEquals("name2.attachment", "attachment2", name2);
      map = TrackingAdvice.clearAttachmentsForTarget(mutable);
      assertNotNull(map);
      assertEquals("advice.map.size", 2, map.size());
      map = TrackingAdvice.getAttachmentsForTarget(mutable);
      assertNull(map);
   }

   @Override
   public void testAddAttachmentByType() throws Exception
   {
      super.testAddAttachmentByType();
      // Validate that the attachments were captured
      Map<String, Object> map = TrackingAdvice.getAttachmentsForTarget(mutable);
      assertNotNull(map);
      assertEquals("advice.map.size", 2, map.size());
      Object name1 = map.get("java.lang.Integer");
      assertEquals("name1.attachment", 1, name1);
      Object name2 = map.get("java.lang.String");
      assertEquals("name2.attachment", "different", name2);
      map = TrackingAdvice.clearAttachmentsForTarget(mutable);
      assertNotNull(map);
      assertEquals("advice.map.size", 2, map.size());
      map = TrackingAdvice.getAttachmentsForTarget(mutable);
      assertNull(map);
   }

   @Override
   public void testAddAttachmentErrors() throws Exception
   {
      super.testAddAttachmentErrors();
      // Validate that no attachments were captured
      Map<String, Object> map = TrackingAdvice.getAttachmentsForTarget(mutable);
      assertNull(map);
   }

   @Override
   public void testRemoveAttachmentByName() throws Exception
   {
      super.testRemoveAttachmentByName();
      // Validate that the attachments were captured
      Map<String, Object> map = TrackingAdvice.getAttachmentsForTarget(mutable);
      assertNotNull(map);
      assertEquals("advice.map.size", 1, map.size());
      Object name1 = map.get("name1");
      assertNull("name1.attachment", name1);
      Object name2 = map.get("name2");
      assertEquals("name2.attachment", "different2", name2);
      map = TrackingAdvice.clearAttachmentsForTarget(mutable);
      assertNotNull(map);
      assertEquals("advice.map.size", 1, map.size());
      map = TrackingAdvice.getAttachmentsForTarget(mutable);
      assertNull(map);
   }

   @Override
   public void testRemoveAttachmentByNameAndType() throws Exception
   {
      super.testRemoveAttachmentByNameAndType();
      // Validate that the attachments were captured
      Map<String, Object> map = TrackingAdvice.getAttachmentsForTarget(mutable);
      assertNotNull(map);
      assertEquals("advice.map.size", 1, map.size());
      Object name1 = map.get("name1");
      assertNull("name1.attachment", name1);
      Object name2 = map.get("name2");
      assertEquals("name2.attachment", "different2", name2);
      map = TrackingAdvice.clearAttachmentsForTarget(mutable);
      assertNotNull(map);
      assertEquals("advice.map.size", 1, map.size());
      map = TrackingAdvice.getAttachmentsForTarget(mutable);
      assertNull(map);
   }

   @Override
   public void testRemoveAttachmentByType() throws Exception
   {
      super.testRemoveAttachmentByType();
      // Validate that the attachments were captured
      Map<String, Object> map = TrackingAdvice.getAttachmentsForTarget(mutable);
      assertEquals("advice.map.size", 1, map.size());
      Object name1 = map.get("java.lang.Integer");
      assertEquals("name1.attachment", 1, name1);
      Object name2 = map.get("java.lang.String");
      assertNull("name2.attachment", name2);
      map = TrackingAdvice.clearAttachmentsForTarget(mutable);
      assertNotNull(map);
      assertEquals("advice.map.size", 1, map.size());
      map = TrackingAdvice.getAttachmentsForTarget(mutable);
      assertNull(map);
   }

   protected Attachments getAttachments()
   {
      return unit;
   }

   protected Attachments getMutable()
   {
      return mutable;
   }

}
