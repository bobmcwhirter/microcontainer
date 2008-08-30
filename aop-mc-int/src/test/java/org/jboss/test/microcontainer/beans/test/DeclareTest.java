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

import java.util.Iterator;

import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.DeclareDef;
import org.jboss.test.aop.junit.AOPMicrocontainerTest;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public abstract class DeclareTest extends AOPMicrocontainerTest
{
   public DeclareTest(String name)
   {
      super(name);
   }
   
   public void testDeclares()
   {
      AspectManager manager = AspectManager.instance();
      
      boolean doneAnonymous = false;
      int i = 0;
      Iterator<DeclareDef> it = manager.getDeclares();
      while (it.hasNext())
      {
         DeclareDef def = it.next();
         if (def.getName().equals("DeclareError"))
         {
            checkDeclare(def, false, "call(* org.acme.Foo->error(..))", "Error!!!");
         }
         else if (def.getName().equals("DeclareWarning"))
         {
            checkDeclare(def, true, "call(* org.acme.Foo->warning(..))", "Warning!!!");
         }
         else
         {
            //This one will have a GUID for its name when done from a deployment or aop xml
            if (doneAnonymous)
            {
               fail("Already done an anonymous DeclareDef in the deployment");
            }
            checkDeclare(def, false, "call(* org.acme.Foo->anonymous(..))", "Anon!!!");
            doneAnonymous = true;
         }
         i++;
      }
      
      assertEquals("Wrong number of DeclareDefs", 3, i);
   }
   
   private void checkDeclare(DeclareDef def, boolean isWarning, String expr, String msg)
   {
      assertEquals(isWarning, def.getWarning());
      assertEquals(expr, def.getExpr());
      assertEquals(msg, def.getMsg());
   }

}
