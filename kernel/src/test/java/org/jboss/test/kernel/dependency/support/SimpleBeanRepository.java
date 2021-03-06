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
package org.jboss.test.kernel.dependency.support;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.HashSet;

import org.jboss.dependency.spi.ControllerState;

public class SimpleBeanRepository implements StatesAndBeanRepository
{
   List<SimpleBean> beans = new ArrayList<SimpleBean>();
   Set<ControllerState> states = new HashSet<ControllerState>();

   public List<SimpleBean> getBeans()
   {
      return beans;
   }

   public void setBeans(List<SimpleBean> beans)
   {
      this.beans = beans;
   }

   public void addSimpleBean(SimpleBean bean)
   {
      beans.add(bean);
   }
   
   public void removeSimpleBean(SimpleBean bean)
   {
      beans.remove(bean);
   }

   public Set<ControllerState> getStates()
   {
      return states;
   }

   public boolean addState(ControllerState state)
   {
      return states.add(state);
   }

   public boolean removeState(ControllerState state)
   {
      return states.remove(state);
   }
}
