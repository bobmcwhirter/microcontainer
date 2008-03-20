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
package org.jboss.test.microcontainer.test;

import java.util.ArrayList;

import junit.framework.Test;

import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.Configure;
import org.jboss.test.microcontainer.support.Create;
import org.jboss.test.microcontainer.support.Describe;
import org.jboss.test.microcontainer.support.Install;
import org.jboss.test.microcontainer.support.Instantiate;
import org.jboss.test.microcontainer.support.LifecycleCallback;
import org.jboss.test.microcontainer.support.Start;

public class LifecycleTestCase extends AOPMicrocontainerTest
{
   public static Test suite()
   {
      return suite(LifecycleTestCase.class);
   }
   
   public LifecycleTestCase(String name)
   {
      super(name);
   }

   public void testLifecycleInterceptions() throws Exception
   {
      boolean undeployed = false;
      try
      {
         LifecycleCallback.interceptions.clear();
         deploy("LifecycleTestCaseNotAutomatic.xml");
         
         //Verify the beans exist
         checkBeanExists("ConfigureBean");
         checkBeanExists("CreateBean");
         checkBeanExists("DescribeBean"); 
         checkBeanExists("InstallBean");
         checkBeanExists("InstantiateBean"); 
         checkBeanExists("StartBean"); 

         //Now check the expected lifecycle events for each bean
         checkExpectedAnnotations("ConfigureBean", Configure.class);
         checkExpectedAnnotations("CreateBean", Create.class);
         checkExpectedAnnotations("DescribeBean", Describe.class);
         checkExpectedAnnotations("InstallBean", Install.class);
         checkExpectedAnnotations("InstantiateBean", Instantiate.class);
         checkExpectedAnnotations("StartBean", Start.class);
         
         
         LifecycleCallback.interceptions.clear();
         
         undeploy("LifecycleTestCaseNotAutomatic.xml");
         undeployed = true;
         
         checkExpectedAnnotations("ConfigureBean", Configure.class);
         checkExpectedAnnotations("CreateBean", Create.class);
         checkExpectedAnnotations("DescribeBean", Describe.class);
         checkExpectedAnnotations("InstallBean", Install.class);
         checkExpectedAnnotations("InstantiateBean", Instantiate.class);
         checkExpectedAnnotations("StartBean", Start.class);
         
      }
      finally
      {
         if (!undeployed)
         {
            undeploy("LifecycleTestCaseNotAutomatic.xml");
         }
      }
   }
   
   public void checkBeanExists(String name)
   {
      Object bean = getBean(name);
      assertNotNull(bean);
   }
   
   public void checkExpectedAnnotations(String name, Class<?> annotation)
   {
      ArrayList<Class<?>> events = LifecycleCallback.interceptions.get(name);
      assertNotNull(events);
      assertEquals("Wrong events, expected one element only " + events, 1, events.size());
      Class<?> actualAnnotation = events.get(0);
      assertEquals(annotation, actualAnnotation);
   }
}
