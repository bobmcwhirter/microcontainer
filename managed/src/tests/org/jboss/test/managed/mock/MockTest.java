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
package org.jboss.test.managed.mock;

import java.io.Serializable;
import java.util.HashMap;

import junit.framework.Test;

import org.jboss.managed.api.Fields;
import org.jboss.managed.api.ManagedObject;
import org.jboss.managed.api.ManagedProperty;
import org.jboss.managed.plugins.advice.WrapperAdvice;
import org.jboss.test.managed.ManagedTest;

/**
 * MockTest
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 1.1 $
 */
public class MockTest extends ManagedTest
{
   /**
    * Create a testsuite for this this
    * 
    * @return the testsuite
    */
   public static Test suite()
   {
      return suite(MockTest.class);
   }
   
   /**
    * Create a new MockTest.
    * 
    * @param name the test name
    */
   public MockTest(String name)
   {
      super(name);
   }

   /**
    * Test the mock
    * 
    * @throws Exception for any problem
    */
   public void testMock() throws Exception
   {
      MockDataSourceManagedObject mock = new MockDataSourceManagedObject();

      ManagedObject mo = WrapperAdvice.wrapManagedObject(mock);
      
      getLog().debug("MockDataSourceManagedObject, available propertes...");
      getLog().debug(mock.getPropertyNames());

      getLog().debug("Initial MetaData...");
      getLog().debug(mock.prettyPrint());
      
      getLog().debug("Adding jndi-name...");
      mo.getProperty("jndi-name").setValue("DefaultDS");
      getLog().debug(mock.prettyPrint());

      getLog().debug("Adding user and password...");
      mo.getProperty("user").setValue("Scott");
      mo.getProperty("password").setValue("Tiger");
      getLog().debug(mock.prettyPrint());

      getLog().debug("Changing jndi-name...");
      mo.getProperty("jndi-name").setValue("ChangedDS");
      getLog().debug(mock.prettyPrint());

      getLog().debug("Removing jndi-name...");
      mo.getProperty("jndi-name").setValue(null);
      getLog().debug(mock.prettyPrint());
      
      getLog().debug("Displaying properties...");
      for (ManagedProperty property : mo.getProperties())
         getLog().debug(property.getName() + "=" + property.getValue());
      
      ManagedProperty jndiName = mo.getProperty("jndi-name");
      
      getLog().debug("Displaying jndi-name field values...");
      getLog().debug("jndi-name name  field is: " + jndiName.getFields().getField(Fields.NAME));
      getLog().debug("jndi-name value field is: " + jndiName.getFields().getField(Fields.VALUE));
      
      assertEquals(mo, jndiName.getManagedObject());
   }

   /**
    * Test the managed object serialization
    * 
    * @throws Exception for any problem
    */
   public void testManagedObjectSerialization()
      throws Exception
   {
      MockDataSourceManagedObject mock = new MockDataSourceManagedObject();
      ManagedObject mo = WrapperAdvice.wrapManagedObject(mock);

      mo.getProperty("jndi-name").setValue("DefaultDS");
      mo.getProperty("user").setValue("Scott");
      mo.getProperty("password").setValue("Tiger");
      mo.getProperty("jndi-name").setValue("ChangedDS");

      getLog().debug(mock.prettyPrint());
      
      byte[] data = super.serialize(mo);
      ManagedObject mo2 = (ManagedObject) super.deserialize(data);
      
      assertEquals("jndiName", "ChangedDS", mo2.getProperty("jndi-name").getValue());
      assertEquals("user", "Scott", mo2.getProperty("user").getValue());
      assertEquals("password", "Tiger", mo2.getProperty("password").getValue());
   }
   
   /**
    * Test the managed object property map serialization
    * 
    * @throws Exception for any problem
    */
   @SuppressWarnings("unchecked")
   public void testManagedPropertyMapSerialization()
      throws Exception
   {
      MockDataSourceManagedObject mock = new MockDataSourceManagedObject();
      ManagedObject mo = WrapperAdvice.wrapManagedObject(mock);

      ManagedProperty jndiName = mo.getProperty("jndi-name");
      jndiName.setValue("DefaultDS");
      ManagedProperty user = mo.getProperty("user");
      user.setValue("Scott");
      ManagedProperty password = mo.getProperty("password");
      password.setValue("Tiger");
      jndiName.setValue("ChangedDS");

      HashMap<String, ManagedProperty> props = new HashMap<String, ManagedProperty>();
      props.put(jndiName.getName(), jndiName);
      props.put(user.getName(), user);
      props.put(password.getName(), password);
     
      HashMap<String, ManagedProperty> props2 = serializeDeserialize(props, HashMap.class);

      ManagedProperty jndiName2 = props2.get("jndi-name");
      assertEquals("jndiName", "ChangedDS", jndiName2.getValue());
      ManagedProperty jndiName3 = jndiName2.getManagedObject().getProperty("jndi-name");
      assertEquals("jndiName", "ChangedDS", jndiName3.getValue());
      ManagedProperty user2 = props2.get("user");
      assertEquals("user", "Scott", user2.getValue());
      ManagedProperty password2 = props2.get("password");
      assertEquals("password", "Tiger", password2.getValue());
   }

   /**
    * Serialize/deserialize
    * 
    * TODO move to AbstractTestCase
    * @param <T> the expected type
    * @param value the value
    * @param expected the expected type
    * @return the result
    * @throws Exception for any problem
    */
   protected <T> T serializeDeserialize(Serializable value, Class<T> expected) throws Exception
   {
      byte[] bytes = serialize(value);
      Object result = deserialize(bytes);
      return assertInstanceOf(result, expected);
      
   }
   
   @Override
   protected void configureLogging()
   {
      enableTrace("org.jboss.managed.plugins.advice");
   }
}
