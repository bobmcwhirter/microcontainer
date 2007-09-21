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
package org.jboss.test.kernel.annotations.support;

import org.jboss.beans.metadata.api.annotations.Create;
import org.jboss.beans.metadata.api.annotations.Start;
import org.jboss.beans.metadata.api.annotations.Stop;
import org.jboss.beans.metadata.api.annotations.Destroy;

/**
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class LifecycleAnnotationTester implements AnnotationTester
{
   private static String FROM_XML = "FromXML";
   private Object[] value = new Object[4];

   public void fromXMLCreate()
   {
      value[0] = FROM_XML;
   }

   public void fromXMLStart()
   {
      value[1] = FROM_XML;
   }

   public void fromXMLStop()
   {
      value[1] = null;
      value[2] = FROM_XML;
   }

   public void fromXMLDestroy()
   {
      value[0] = null;
      value[3] = FROM_XML;
   }

   @Create
   public void fromAnnCreate()
   {
      value[0] = "dew";
   }

   @Start
   public void fromAnnStart()
   {
      value[1] = "dewdew";
   }

   @Stop
   public void fromAnnStop()
   {
      value[1] = null;
      value[2] = "dewdwe";
   }

   @Destroy
   public void fromAnnDestroy()
   {
      value[0] = null;
      value[3] = "dwefwf";
   }

   public Object[] getValue()
   {
      return value;
   }
}
