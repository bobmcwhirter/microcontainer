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
package org.jboss.test.spring.support;

import java.util.List;
import java.util.Set;
import java.util.Map;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class SimpleBean
{

   private int x;
   private double y;
   private String s;
   private List mylist;
   private Set myset;
   private Map mymap;

   public SimpleBean()
   {
   }

   public SimpleBean(int x, double y, String s)
   {
      this.x = x;
      this.y = y;
      this.s = s;
   }

   public List getMylist()
   {
      return mylist;
   }

   public void setMylist(List mylist)
   {
      this.mylist = mylist;
   }

   public Set getMyset()
   {
      return myset;
   }

   public void setMyset(Set myset)
   {
      this.myset = myset;
   }

   public Map getMymap()
   {
      return mymap;
   }

   public void setMymap(Map mymap)
   {
      this.mymap = mymap;
   }

   public String toString()
   {
      StringBuilder builder = new StringBuilder();
      builder.append(x).append(",");
      builder.append(y).append(",");
      builder.append(s).append(",");
      builder.append(mylist).append(",");
      builder.append(myset).append(",");
      builder.append(mymap);
      return builder.toString();
   }

}
