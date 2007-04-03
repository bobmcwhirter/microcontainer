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
package org.jboss.dependency.plugins;

import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;

/**
 * Hacky Class to fixup "object names" to their canonical form
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class JMXObjectNameFix
{
   /**
    * Whether the given name needs an alias<p>
    * 
    * By default we just add aliases for JMX like ObjectNames to have a canonical name alias
    * 
    * @param original the original name
    * @return the alias if required or null if no alias required
    */
   public static Object needsAnAlias(Object original)
   {
      // Not a string
      if (original == null || original instanceof String == false)
         return null;
      
      try
      {
         ObjectName canonical = ObjectName.getInstance((String) original);
         String alias = canonical.getCanonicalName();
         if (original.equals(alias) == false)
            return alias;
      }
      catch (MalformedObjectNameException ignored)
      {
         // Not a JMX ObjectName
      }
      return null;
   }
}
