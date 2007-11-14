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
package org.jboss.aop.microcontainer.beans2;

import java.io.StringReader;
import java.util.List;

import org.jboss.aop.AspectManager;
import org.jboss.aop.introduction.InterfaceIntroduction;
import org.jboss.aop.pointcut.ast.ASTStart;
import org.jboss.aop.pointcut.ast.TypeExpressionParser;
import org.jboss.util.id.GUID;

/**
 * An AspectBinding.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 58323 $
 */
public class IntroductionBinding 
{
   protected AspectManager manager;
   protected String name = GUID.asString();
   protected String classes;
   protected String expr;
   protected List<String> interfaces;
   protected List<MixinEntry> mixins;

   public IntroductionBinding()
   {
      super();
   }

   /**
    * Get the interfaces.
    * 
    * @return the interfaces.
    */
   public List<String> getInterfaces()
   {
      return interfaces;
   }

   /**
    * Set the interfaces.
    * 
    * @param interfaces The interfaces to set.
    */
   public void setInterfaces(List<String> interfaces)
   {
      this.interfaces = interfaces;
   }

   public List<MixinEntry> getMixins()
   {
      return mixins;
   }

   public void setMixins(List<MixinEntry> mixins)
   {
      this.mixins = mixins;
   }

   public String getName()
   {
      return name;
   }

   public void setName(String name)
   {
      this.name = name;
   }

   /**
    * Get the classes.
    * 
    * @return the classes.
    */
   public String getClasses()
   {
      return classes;
   }

   /**
    * Set the classes.
    * 
    * @param classes The classes to set.
    */
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

   /**
    * Get the manager.
    * 
    * @return the manager.
    */
   public AspectManager getManager()
   {
      return manager;
   }

   /**
    * Set the manager.
    * 
    * @param manager The manager to set.
    */
   public void setManager(AspectManager manager)
   {
      this.manager = manager;
   }

   public void start() throws Exception
   {
      if (manager == null)
         throw new IllegalArgumentException("Null manager");
      if (classes == null && expr == null)
         throw new IllegalArgumentException("Neither classes nor expr set");
      if (classes != null && expr != null)
         throw new IllegalArgumentException("Cannot set both classes and expr");
      if (interfaces == null && mixins == null)
         throw new IllegalArgumentException("Neither interfaces nor mixins set");
      
      String[] intfs = (interfaces != null) ? interfaces.toArray(new String[interfaces.size()]) : null;
      
      InterfaceIntroduction introduction = null;
      if (classes != null)
      {
         introduction = new InterfaceIntroduction(name, classes, intfs);
      }
      else
      {
         ASTStart start = new TypeExpressionParser(new StringReader(expr)).Start();
         introduction = new InterfaceIntroduction(name, start, intfs);
      }
      
      if (mixins != null)
      {
         for (MixinEntry entry : mixins)
         {
            if (entry.getInterfaces() == null)
               throw new IllegalArgumentException("MixinEntry with null interfaces");
            if (entry.getMixin() == null)
               throw new IllegalArgumentException("MixinEntry with null mixin");
            String[] intfaces = entry.getInterfaces().toArray(new String[entry.getInterfaces().size()]);
            
            introduction.addMixin(new InterfaceIntroduction.Mixin(entry.getMixin(), intfaces, entry.getConstruction(), entry.isTransient()));
         }
      }      
      manager.addInterfaceIntroduction(introduction);
   }
   
   public void stop() throws Exception
   {
      manager.removeInterfaceIntroduction(name);
   }
}
