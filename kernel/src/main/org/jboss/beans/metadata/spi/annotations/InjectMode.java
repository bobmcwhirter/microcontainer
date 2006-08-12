package org.jboss.beans.metadata.spi.annotations;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public enum InjectMode
{
   BY_TYPE("ByType"),
   BY_NAME("ByName");

   private String modeString;

   InjectMode(String modeString)
   {
      this.modeString = modeString;
   }

   public String toString()
   {
      return modeString;
   }

}
