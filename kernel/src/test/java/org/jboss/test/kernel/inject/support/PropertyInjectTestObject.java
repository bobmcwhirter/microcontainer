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
package org.jboss.test.kernel.inject.support;

import java.util.Collection;
import java.util.Map;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class PropertyInjectTestObject
{

   private TesterInterface testerInterface;
   private DuplicateTester duplicateTester;
   private Collection<?> collection;
   private Map<?,?> map;

   public TesterInterface getTesterInterface()
   {
      return testerInterface;
   }

   public void setTesterInterface(TesterInterface testerInterface)
   {
      this.testerInterface = testerInterface;
   }

   public DuplicateTester getDuplicateTester()
   {
      return duplicateTester;
   }

   public void setDuplicateTester(DuplicateTester duplicateTester)
   {
      this.duplicateTester = duplicateTester;
   }

   public Collection<?> getCollection()
   {
      return collection;
   }

   public void setCollection(Collection<?> collection)
   {
      this.collection = collection;
   }

   public Map<?,?> getMap()
   {
      return map;
   }

   public void setMap(Map<?,?> map)
   {
      this.map = map;
   }

}
