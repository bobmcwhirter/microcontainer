/*
 * 
 */

package org.jboss.test.kernel.inject.support;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class LifecycleInjectTestObject
{

   private TesterInterface testerInterface;

   public void create(TesterInterface ti)
   {
      testerInterface = ti;
   }

   public void startMeUp(TesterInterface ti)
   {
      testerInterface = ti;
   }

   public void stop(TesterInterface ti)
   {
      testerInterface = ti;
   }

   public void destruction(TesterInterface ti)
   {
      testerInterface = ti;
   }

   public void installIt(TesterInterface ti)
   {
      testerInterface = ti;
   }

   public void fromOutside(TesterInterface ti)
   {
      testerInterface = ti;
   }

   public TesterInterface getTesterInterface()
   {
      return testerInterface;
   }

}
