package org.jboss.example.microcontainer.aspects;

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
      return new CompoundName(name, syntax);
   }
}
