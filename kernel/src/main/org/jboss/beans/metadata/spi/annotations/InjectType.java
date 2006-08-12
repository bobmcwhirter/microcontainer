package org.jboss.beans.metadata.spi.annotations;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public enum InjectType
{
   STRICT("Strinct"),
   LOOSE("Loose");

   private String typeString;

   InjectType(String modeString)
   {
      this.typeString = modeString;
   }

   public String toString()
   {
      return typeString;
   }

}
