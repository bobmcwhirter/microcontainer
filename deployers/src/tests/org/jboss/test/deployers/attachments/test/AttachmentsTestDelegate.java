/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
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

import java.net.URL;

import org.jboss.aop.AspectXmlLoader;
import org.jboss.test.AbstractTestDelegate;

/**
 * 
 * AttachmentsTestDelegate overrides the AbstractTestDelegate to
 * deploy/undeploy test specific aop descriptors in setUp/tearDown.
 * 
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @author Scott.Stark@jboss.com
 * @version $Revision:59255 $
 */
public class AttachmentsTestDelegate extends AbstractTestDelegate
{
   /** The AOP URL used */
   private URL aopURL;
   
   /**
    * Create a new ManagedTestDelegate.
    * 
    * @param clazz the class
    * @throws Exception for any error
    */
   public AttachmentsTestDelegate(Class clazz) throws Exception
   {
      super(clazz);
   }

   /**
    * Look for a test specific aop descriptor based on the ctor
    * class first, and if none is found, the ManagedTest.class.
    */
   public void setUp() throws Exception
   {
      super.setUp();
      if (deployAOP(clazz) == false)
         deployAOP(AttachmentsTest.class);
   }

   /**
    * Undeployment any test specific aop descriptor deployed in setUp.
    */
   public void tearDown() throws Exception
   {
      super.tearDown();
      undeployAOP();
   }

   /**
    * Look for a test specific resource name by appending "-aop.xml"
    * to the referenceClass name as a resource. For example, a.b.SomeTest
    * would produce a a/b/SomeTest-aop.xml resource that is queried
    * for using clazz.getClassLoader().getResource("a/b/SomeTest-aop.xml");
    *  
    * @param referenceClass - the class to use as the aop descriptor base name.
    * @return true if the aop descriptor was found and deployed,
    *    false otherwise.
    * @throws Exception on failure to deploy the aop descriptor.
    */
   protected boolean deployAOP(Class referenceClass) throws Exception
   {
      String testName = referenceClass.getName();
      testName = testName.replace('.', '/') + "-aop.xml";
      URL url = clazz.getClassLoader().getResource(testName);
      if (url != null)
      {
         log.debug("Deploying " + url);
         aopURL = url;
         try
         {
            AspectXmlLoader.deployXML(aopURL);
         }
         catch (Throwable t)
         {
            throw new RuntimeException("Error deploying: " + url, t);
         }
         return true;
      }
      else
      {
         log.debug("No test specific deployment " + testName);
         return false;
      }
   }

   /**
    * Undeploy the aop descriptor deployed in deployAOP if
    * one was found.
    *
    */
   protected void undeployAOP()
   {
      if (aopURL == null)
         return;
      try
      {
         log.debug("Undeploying " + aopURL);
         AspectXmlLoader.undeployXML(aopURL);
      }
      catch (Exception e)
      {
         log.warn("Ignored error undeploying " + aopURL, e);
      }
   }

}
