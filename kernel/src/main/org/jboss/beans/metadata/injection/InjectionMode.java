/*
 * 
 */

package org.jboss.beans.metadata.injection;

import org.jboss.util.JBossObject;
import org.jboss.util.JBossStringBuilder;

/**
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
public class InjectionMode extends JBossObject
{
   /** ByType */
   public static final InjectionMode BY_TYPE = new InjectionMode("ByType");

   /** ByName */
   public static final InjectionMode BY_NAME = new InjectionMode("ByName");

   /** The state string */
   protected final String modeString;

   /**
    * Create a new state
    *
    * @param modeString the string representation
    */
   public InjectionMode(String modeString)
   {
      if (modeString == null)
         throw new IllegalArgumentException("Null mode string");
      this.modeString = modeString;
   }

   /**
    * Get the state string
    *
    * @return the state string
    */
   public String getModeString()
   {
      return modeString;
   }

   public boolean equals(Object object)
   {
      if (object == null || object instanceof InjectionMode == false)
         return false;
      InjectionMode other = (InjectionMode) object;
      return modeString.equals(other.getModeString());
   }

   public void toString(JBossStringBuilder buffer)
   {
      buffer.append(modeString);
   }

   protected int getHashCode()
   {
      return modeString.hashCode();
   }

}
