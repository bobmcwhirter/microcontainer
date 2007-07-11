/*
* JBoss, Home of Professional Open Source
* Copyright 2007, JBoss Inc., and individual contributors as indicated
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
package org.jboss.metatype.plugins.types;

import org.jboss.metatype.api.types.ImmutableCompositeMetaType;
import org.jboss.metatype.api.types.MetaType;
import org.jboss.metatype.api.types.SimpleMetaType;
import org.jboss.metatype.spi.types.MetaTypeBuilder;

/**
 * ClassMetaTypeBuilder.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class ClassMetaTypeBuilder implements MetaTypeBuilder
{
   /** The singleton instance */
   public static final ClassMetaTypeBuilder INSTANCE = new ClassMetaTypeBuilder();
   
   /** The MetaType for Class */
   public static final MetaType CLASS_META_TYPE = new ImmutableCompositeMetaType(
       Class.class.getName(), 
       Class.class.getName(), 
       new String[] { "name" }, 
       new String[] { "The class name" },
       new MetaType[] { SimpleMetaType.STRING }
   );

   public MetaType buildMetaType()
   {
      return CLASS_META_TYPE;
   }
}
