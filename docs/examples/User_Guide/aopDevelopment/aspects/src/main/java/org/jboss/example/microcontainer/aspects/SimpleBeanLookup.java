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
package org.jboss.example.microcontainer.aspects;

import org.jboss.aop.proxy.container.AspectManaged;
import java.util.Properties;
import javax.naming.Context;
import javax.naming.InitialContext;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SimpleBeanLookup
{
   private Properties env;

   public String getName()
   {
      return getClass().getName();
   }

   public void setEnv(Properties env)
   {
      this.env = env;
   }

   public void start() throws Exception
   {
      System.out.println("======== SimpleBeanLookup: Looking up beans bound in JNDI");
      Context context = new InitialContext(env);
      lookupAndOutput(context, "beans/SimpleBean");
      lookupAndOutput(context, "beans/AnnotatedSimpleBean");
      lookupAndOutput(context, "beans/XmlAnnotatedSimpleBean");
   }
   
   private void lookupAndOutput(Context context, String name) throws Exception
   {
      Object o = context.lookup(name);
      System.out.println("Found bean bound under: " + name);
      System.out.println("\tType: " + getClassInformation(o.getClass()));
      System.out.println("\ttoString on found bean: " + o.toString());
   }
   
   private String getClassInformation(Class<?> clazz)
   {
      if (AspectManaged.class.isAssignableFrom(clazz))
      {
         //Classes with aspects will be an instance of AspectManaged
         Class<?> superClass = clazz.getSuperclass();
         return "AOP proxy for " + superClass.getName();
      }
      else 
      {
         return clazz.getName();
      }
   }
}
