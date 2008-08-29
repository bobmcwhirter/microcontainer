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
package org.jboss.test.microcontainer.annotatedaop.test;

import java.util.Iterator;

import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.DeclareDef;
import org.jboss.test.aop.junit.AnnotatedAOPMicrocontainerTest;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class DeclareTestCase extends AnnotatedAOPMicrocontainerTest
{
   public DeclareTestCase(String name)
   {
      super(name);
   }

   public void testDeclare()
   {
      AspectManager am = (AspectManager)getBean("AspectManager");
      Iterator<DeclareDef> it = am.getDeclares();
      int declares = 0;
      boolean gotError = false;
      boolean gotWarning = false;
      
      while (it.hasNext())
      {
         DeclareDef def = it.next();
         declares++;
         
         if (def.getWarning())
         {
            gotWarning = true;
            checkDeclare(def,"call(void org.acme.Warning->warning())", "warning");
         }
         else
         {
            gotError = true;
            checkDeclare(def, "call(void org.acme.Error->error())", "error");
         }
      }
      
      assertEquals(2, declares);
      assertTrue(gotError);
      assertTrue(gotWarning);
   }
   
   private void checkDeclare(DeclareDef def, String pointcut, String msg)
   {
      assertEquals(def.getExpr(), pointcut);
      assertEquals(def.getMsg(), msg);
   }

}
