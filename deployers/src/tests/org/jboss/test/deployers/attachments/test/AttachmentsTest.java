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

import org.jboss.deployers.spi.attachments.Attachments;
import org.jboss.test.AbstractTestCaseWithSetup;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.BaseTestCase;

/**
 * Attachments Test.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public abstract class AttachmentsTest extends AbstractTestCaseWithSetup
{
   public static Test suite()
   {
      return new TestSuite(AttachmentsTest.class);
   }
   /**
    * A static getDelegate method that is called by the AbstractTestDelegate
    * getDelegate logic to obtain the test specific delegate. This sets the
    * default delegate for ManagedTests to ManagedTestDelegate.
    * 
    * @param clazz the test class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class clazz) throws Exception
   {
      return new AttachmentsTestDelegate(clazz);
   }

   public AttachmentsTest(String name)
   {
      super(name);
   }
   
   protected abstract Attachments getAttachments();
   
   protected abstract Attachments getMutable();

   /**
    * Adds a call to configureLogging after super.setUp.
    */
   protected void setUp() throws Exception
   {
      super.setUp();
      configureLogging();
   }

   public void testAddAttachmentErrors() throws Exception
   {
      Attachments mutable = getMutable();
      
      try
      {
         mutable.addAttachment((String) null, "attachment");
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         mutable.addAttachment("name", null);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         mutable.addAttachment(null, "attachment", String.class);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         mutable.addAttachment("name", null, String.class);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         mutable.addAttachment("name", "attachment", null);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         mutable.addAttachment((Class<String>) null, "attachment");
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         mutable.addAttachment(String.class, null);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   public void testAddAttachmentByName() throws Exception
   {
      ExpectedAttachments expected = new ExpectedAttachments();
      assertAddFreshAttachmentByName(expected, "name1", "attachment1");
      assertAddFreshAttachmentByName(expected, "name2", "attachment2");
      
      assertAddReplaceAttachmentByName(expected, "name1", "different1", "attachment1");
   }

   protected void assertAddFreshAttachmentByName(ExpectedAttachments expected, String name, Object attachment)
   {
      assertAddReplaceAttachmentByName(expected, name, attachment, null);
   }

   protected void assertAddReplaceAttachmentByName(ExpectedAttachments expected, String name, Object attachment, Object replaced)
   {
      Object result = assertAddAttachmentByName(expected, name, attachment);
      assertEquals(replaced, result);
   }

   protected Object assertAddAttachmentByName(ExpectedAttachments expected, String name, Object attachment)
   {
      Object result = getMutable().addAttachment(name, attachment);
      checkAdd(getAttachments(), expected, name, attachment, result);
      return result;
   }
      
   protected Object checkAdd(Attachments attachments, ExpectedAttachments expected, String name, Object attachment, Object result)
   {
      assertEquals(attachment, attachments.getAttachment(name));
      assertTrue(attachments.isAttachmentPresent(name));
      Object expectedResult = assertExpectedAdd(attachments, expected, name, attachment);
      assertEquals(expectedResult, result);
      return result;
   }
   
   protected Object assertExpectedAdd(Attachments attachments, ExpectedAttachments expected, String name, Object attachment)
   {
      Object result = expected.add(name, attachment);
      assertEquals(expected.expected, attachments.getAttachments());
      return result;
   }
   
   public void testAddAttachmentByNameAndType() throws Exception
   {
      ExpectedAttachments expected = new ExpectedAttachments();
      assertAddFreshAttachmentByNameAndType(expected, "name1", "attachment1", String.class);
      assertAddFreshAttachmentByNameAndType(expected, "name2", "attachment2", String.class);
      
      assertAddReplaceAttachmentByNameAndType(expected, "name1", "different1", "attachment1", String.class);
   }

   protected <T> void assertAddFreshAttachmentByNameAndType(ExpectedAttachments expected, String name, T attachment, Class<T> expectedType)
   {
      assertAddReplaceAttachmentByNameAndType(expected, name, attachment, null, expectedType);
   }

   protected <T> void assertAddReplaceAttachmentByNameAndType(ExpectedAttachments expected, String name, T attachment, Object replaced, Class<T> expectedType)
   {
      Object result = assertAddAttachmentByNameAndType(expected, name, attachment, expectedType);
      assertEquals(replaced, result);
   }

   protected <T> Object assertAddAttachmentByNameAndType(ExpectedAttachments expected, String name, T attachment, Class<T> expectedType)
   {
      Object result = getMutable().addAttachment(name, attachment, expectedType);
      Attachments attachments = getAttachments();
      checkAdd(attachments, expected, name, attachment, result);
      assertEquals(attachment, attachments.getAttachment(name, expectedType));
      assertTrue(attachments.isAttachmentPresent(name, expectedType));
      return result;
   }
   
   public void testAddAttachmentByType() throws Exception
   {
      ExpectedAttachments expected = new ExpectedAttachments();
      assertAddFreshAttachmentByType(expected, Integer.class, 1);
      assertAddFreshAttachmentByType(expected, String.class, "attachment");
      
      assertAddReplaceAttachmentByType(expected, String.class, "different", "attachment");
   }

   protected <T> void assertAddFreshAttachmentByType(ExpectedAttachments expected, Class<T> type, T attachment)
   {
      assertAddReplaceAttachmentByType(expected, type, attachment, null);
   }

   protected <T> void assertAddReplaceAttachmentByType(ExpectedAttachments expected, Class<T> type, T attachment, Object replaced)
   {
      Object result = assertAddAttachmentByType(expected, type, attachment);
      assertEquals(replaced, result);
   }

   protected <T> Object assertAddAttachmentByType(ExpectedAttachments expected, Class<T> type, T attachment)
   {
      Object result = getMutable().addAttachment(type, attachment);
      String name = type.getName();
      Attachments attachments = getAttachments();
      checkAdd(attachments, expected, name, attachment, result);
      assertEquals(attachment, attachments.getAttachment(type));
      assertTrue(attachments.isAttachmentPresent(type));
      assertEquals(attachment, attachments.getAttachment(name, type));
      assertTrue(attachments.isAttachmentPresent(name, type));
      return result;
   }
   
   public void testRemoveAttachmentErrors() throws Exception
   {
      Attachments mutable = getMutable();
      
      try
      {
         mutable.removeAttachment((String) null);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         mutable.removeAttachment(null, String.class);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         mutable.removeAttachment("name", null);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         mutable.removeAttachment((Class<String>) null);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   public void testRemoveAttachmentByName() throws Exception
   {
      ExpectedAttachments expected = new ExpectedAttachments();
      assertRemoveNotPresentAttachmentByName(expected, "name1");
      
      assertAddAttachmentByName(expected, "name1", "attachment1");
      assertAddAttachmentByName(expected, "name2", "different2");
      assertRemoveAttachmentByName(expected, "name1", "attachment1");
   }

   protected void assertRemoveNotPresentAttachmentByName(ExpectedAttachments expected, String name)
   {
      assertRemoveAttachmentByName(expected, name, null);
   }

   protected void assertRemoveAttachmentByName(ExpectedAttachments expected, String name, Object removed)
   {
      Object result = assertRemoveAttachmentByName(expected, name);
      assertEquals(removed, result);
   }

   protected Object assertRemoveAttachmentByName(ExpectedAttachments expected, String name)
   {
      Object result = getMutable().removeAttachment(name);
      checkRemove(getAttachments(), expected, name, result);
      return result;
   }
      
   protected Object checkRemove(Attachments attachments, ExpectedAttachments expected, String name, Object result)
   {
      assertNull(attachments.getAttachment(name));
      assertFalse(attachments.isAttachmentPresent(name));
      Object expectedResult = assertExpectedRemove(attachments, expected, name);
      assertEquals(expectedResult, result);
      return result;
   }
   
   protected Object assertExpectedRemove(Attachments attachments, ExpectedAttachments expected, String name)
   {
      Object result = expected.remove(name);
      assertEquals(expected.expected, attachments.getAttachments());
      return result;
   }
   
   public void testRemoveAttachmentByNameAndType() throws Exception
   {
      ExpectedAttachments expected = new ExpectedAttachments();
      assertRemoveNotPresentAttachmentByNameAndType(expected, "name1", String.class);
      
      assertAddAttachmentByNameAndType(expected, "name1", "attachment1", String.class);
      assertAddAttachmentByNameAndType(expected, "name2", "different2", String.class);
      assertRemoveAttachmentByNameAndType(expected, "name1", "attachment1", String.class);
   }

   protected <T> void assertRemoveNotPresentAttachmentByNameAndType(ExpectedAttachments expected, String name, Class<T> expectedType)
   {
      assertRemoveAttachmentByNameAndType(expected, name, null, expectedType);
   }

   protected <T> void assertRemoveAttachmentByNameAndType(ExpectedAttachments expected, String name, Object removed, Class<T> expectedType)
   {
      Object result = assertRemoveAttachmentByNameAndType(expected, name, expectedType);
      assertEquals(removed, result);
   }

   protected <T> Object assertRemoveAttachmentByNameAndType(ExpectedAttachments expected, String name, Class<T> expectedType)
   {
      Object result = getMutable().removeAttachment(name, expectedType);
      Attachments attachments = getAttachments();
      checkRemove(attachments, expected, name, result);
      assertNull(attachments.getAttachment(name, expectedType));
      assertFalse(attachments.isAttachmentPresent(name, expectedType));
      return result;
   }
   
   public void testRemoveAttachmentByType() throws Exception
   {
      ExpectedAttachments expected = new ExpectedAttachments();
      assertRemoveNotPresentAttachmentByType(expected, Integer.class);
      
      assertAddAttachmentByType(expected, Integer.class, 1);
      assertAddAttachmentByType(expected, String.class, "attachment");
      assertRemoveAttachmentByType(expected, String.class, "attachment");
   }

   protected <T> void assertRemoveNotPresentAttachmentByType(ExpectedAttachments expected, Class<T> type)
   {
      assertRemoveAttachmentByType(expected, type, null);
   }

   protected <T> void assertRemoveAttachmentByType(ExpectedAttachments expected, Class<T> type, Object removed)
   {
      Object result = assertRemoveAttachmentByType(expected, type);
      assertEquals(removed, result);
   }

   protected <T> Object assertRemoveAttachmentByType(ExpectedAttachments expected, Class<T> type)
   {
      Object result = getMutable().removeAttachment(type);
      String name = type.getName();
      Attachments attachments = getAttachments();
      checkRemove(attachments, expected, name, result);
      assertNull(attachments.getAttachment(type));
      assertFalse(attachments.isAttachmentPresent(type));
      assertNull(attachments.getAttachment(name, type));
      assertFalse(attachments.isAttachmentPresent(name, type));
      return result;
   }
   
   public void testGetAttachmentErrors() throws Exception
   {
      Attachments attachments = getAttachments();
      
      try
      {
         attachments.getAttachment((String) null);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         attachments.getAttachment(null, String.class);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         attachments.getAttachment("name", null);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         attachments.getAttachment((Class<String>) null);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
   
   public void testIsAttachmentPresentErrors() throws Exception
   {
      Attachments attachments = getAttachments();
      
      try
      {
         attachments.isAttachmentPresent((String) null);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         attachments.isAttachmentPresent(null, String.class);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         attachments.isAttachmentPresent("name", null);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
      
      try
      {
         attachments.isAttachmentPresent((Class<String>) null);
         fail("Should not be here!");
      }
      catch (Throwable t)
      {
         checkThrowable(IllegalArgumentException.class, t);
      }
   }
}
