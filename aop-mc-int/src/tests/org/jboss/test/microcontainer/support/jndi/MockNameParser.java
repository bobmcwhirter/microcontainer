package org.jboss.test.microcontainer.support.jndi;

import java.util.Properties;

import javax.naming.CompoundName;
import javax.naming.Name;
import javax.naming.NameParser;
import javax.naming.NamingException;

public class MockNameParser implements NameParser
{
   private static final Properties syntax = new Properties();
   static
   {
      syntax.setProperty("jndi.syntax.direction", "flat");
   }

   public Name parse(String name) throws NamingException
   {
      CompoundName cn = new CompoundName(name, syntax);
      return cn;
   }

}
