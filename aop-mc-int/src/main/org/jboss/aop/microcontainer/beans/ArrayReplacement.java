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
package org.jboss.aop.microcontainer.beans;

import java.io.StringReader;

import org.jboss.aop.AspectManager;
import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.ParseException;
import org.jboss.aop.pointcut.ast.TypeExpressionParser;
import org.jboss.util.id.GUID;

/**
 *
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ArrayReplacement
{
   AspectManager manager;
   String name = GUID.asString();
   String classes;
   String expr;

   public AspectManager getManager()
   {
      return manager;
   }

   public void setManager(AspectManager manager)
   {
      this.manager = manager;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   public String getClasses()
   {
      return classes;
   }

   public void setClasses(String classes)
   {
      this.classes = classes;
   }

   public String getExpr()
   {
      return expr;
   }

   public void setExpr(String expr)
   {
      this.expr = expr;
   }

   public void start()
   {
      if (manager == null)
      {
         throw new IllegalArgumentException("Null manager");
      }
      if (classes == null && expr == null)
      {
         throw new IllegalArgumentException("Must define either expr or classes");
      }
      if (classes != null && expr != null)
      {
         throw new IllegalArgumentException("Cannot define both expr and classes");
      }

      org.jboss.aop.array.ArrayReplacement pcut = null;
      if (classes != null)
      {
         pcut = new org.jboss.aop.array.ArrayReplacement(name, classes);
      }
      else
      {
         try
         {
            ASTStart start = new TypeExpressionParser(new StringReader(expr)).Start();
            pcut = new org.jboss.aop.array.ArrayReplacement(name, start);
         }
         catch (ParseException e)
         {
            throw new IllegalArgumentException("Could not parse type expression " + expr, e);
         }
      }
      manager.addArrayReplacement(pcut);

   }

   public void stop()
   {
      manager.removeArrayReplacement(name);
   }

}
