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
package org.jboss.test.microcontainer.validateschema.test;

import java.io.InputStream;

import junit.framework.Test;

import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBinding;
import org.jboss.xb.binding.sunday.unmarshalling.SchemaBindingResolver;

/**
 * 
 * @author <a href="kabir.khan@jboss.com">Kabir Khan</a>
 * @version $Revision: 1.1 $
 */
public class ValidateAOPSchemaTestCase extends MicrocontainerTest
{
   public static Test suite()
   {
      return suite(ValidateAOPSchemaTestCase.class);
   }
   
   public ValidateAOPSchemaTestCase(String name)
   {
      super(name);
   }

   public void testSchemaIsValid() throws Exception
   {
      InputStream aopbeans = Thread.currentThread().getContextClassLoader().getResourceAsStream("schema/aop-beans_1_0.xsd");
      assertNotNull(aopbeans);
      org.jboss.xb.binding.Util.loadSchema(aopbeans, null, new SchemaBindingResolver()
      {
         public String getBaseURI()
         {
            return null;
         }

         public SchemaBinding resolve(String nsUri, String baseURI, String schemaLocation)
         {
            return null;
         }

         public org.w3c.dom.ls.LSInput resolveAsLSInput(String nsUri, String baseUri, String schemaLocation)
         {
            if (schemaLocation.equals("jboss-beans-common_2_0.xsd"))
            {
               schemaLocation = "schema/" + schemaLocation;
            }
            InputStream stream = Thread.currentThread().getContextClassLoader().getResourceAsStream(schemaLocation);
            assertNotNull(stream);
            return new org.jboss.xb.binding.sunday.unmarshalling.LSInputAdaptor(stream, null);
         }

         public void setBaseURI(String baseURI)
         {
         }
      });
   }
}
