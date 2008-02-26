/*
* JBoss, Home of Professional Open Source.
* Copyright 2006, Red Hat Middleware LLC, and individual contributors
* as indicated by the @author tags. See the copyright.txt file in the
* distribution for a full listing of individual contributors. 
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
package org.jboss.test.microcontainer.beans.test;

import java.util.List;

import org.jboss.aop.advice.AdviceType;
import org.jboss.aop.microcontainer.beans.AspectBinding;
import org.jboss.aop.microcontainer.beans.BindingEntry;
import org.jboss.aop.microcontainer.beans.InterceptorEntry;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class LightweightAspectTest extends AOPMicrocontainerTest
{

   public LightweightAspectTest(String name)
   {
      super(name);
   }

   public void testBinding() throws Exception
   {
      AspectBinding binding = (AspectBinding)getBean("Binding");
      assertNotNull(binding);
      List<BindingEntry> entries = binding.getAdvices();
      assertEquals(5, entries.size());
      int i = 0;
      for (BindingEntry entry : entries)
      {
         assertTrue(entry instanceof InterceptorEntry);
         InterceptorEntry ie = (InterceptorEntry)entry;
         i++;
         switch (i)
         {
            case 1:
               assertEquals(AdviceType.BEFORE, ie.getType());
               break;
            case 2:
               assertEquals(AdviceType.AROUND, ie.getType());
               break;
            case 3:
               assertEquals(AdviceType.AFTER, ie.getType());
               break;
            case 4:
               assertEquals(AdviceType.THROWING, ie.getType());
               break;
            case 5:
               assertEquals(AdviceType.FINALLY, ie.getType());
         }
      }
   }
   
// TODO this test should be run with weaving enabled   
//   public void testWovenClass() throws Exception
//   {
//      POJO pojo = (POJO)getBean("Bean");
//      if (pojo instanceof AspectManaged == false && pojo instanceof Advised == false)
//      {
//         fail("POJO was neither proxied nor woven");
//      }
//      
//      if (pojo instanceof Advised)
//      {
//         //Only weaving supports B/A/T/F aspects
//         pojo.method();
//         assertTrue(TestAspect.invoked);
//         List interceptions = Interceptions.interceptions();
//         assertEquals(4, interceptions.size());
//         assertTrue(interceptions.contains("BEFORE"));
//         assertTrue(interceptions.contains("AFTER"));
//         assertTrue(interceptions.contains("FINALLY"));
//      }
//   }

}