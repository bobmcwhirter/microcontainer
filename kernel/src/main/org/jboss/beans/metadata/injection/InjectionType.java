/*
 * 
 */

package org.jboss.beans.metadata.injection;

import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
public class InjectionType extends JBossObject
{
   /** Strict */
   public static final InjectionType STRICT = new InjectionType("Strict");

   /** Loose */
   public static final InjectionType LOOSE = new InjectionType("Loose");

   /** The type string */
   protected final String typeString;

   /**
    * Create a new state
    *
    * @param modeString the string representation
    */
   public InjectionType(String typeString)
   {
      if (typeString == null)
         throw new IllegalArgumentException("Null type string");
      this.typeString = typeString;
   }

   /**
    * Get the state string
    *
    * @return the state string
    */
   public String getTypeString()
   {
      return typeString;
   }

   public boolean equals(Object object)
   {
      if (object == null || object instanceof InjectionType == false)
         return false;
      InjectionType other = (InjectionType) object;
      return typeString.equals(other.getTypeString());
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append(typeString);
   }

   protected int getHashCode()
   {
      return typeString.hashCode();
   }

}
