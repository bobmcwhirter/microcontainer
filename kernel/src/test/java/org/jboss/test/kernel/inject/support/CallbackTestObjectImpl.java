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
package org.jboss.test.kernel.inject.support;

import java.util.HashSet;
import java.util.Set;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class CallbackTestObjectImpl implements CallbackTestObject
{
   private Set<TesterInterface> testerInterfaces;

   protected void init()
   {
      if (testerInterfaces == null)
      {
         testerInterfaces = new HashSet<TesterInterface>();
      }
   }

   public void addTesterInterface(TesterInterface tester)
   {
      init();
      testerInterfaces.add(tester);
   }

   public void removeTesterInterface(TesterInterface tester)
   {
      init();
      testerInterfaces.remove(tester);
      if (testerInterfaces.isEmpty())
         testerInterfaces = null;
   }

   public Set<TesterInterface> getTesterInterfaces()
   {
      return testerInterfaces;
   }

   public void setTesterInterfaces(Set<TesterInterface> testerInterfaces)
   {
      this.testerInterfaces = testerInterfaces;
   }
}
