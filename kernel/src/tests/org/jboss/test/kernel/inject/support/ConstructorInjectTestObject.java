/*
 * 
 */

package org.jboss.test.kernel.inject.support;

import java.util.List;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class ConstructorInjectTestObject
{

   private TesterInterface testerInterface;
   private List<TesterInterface> testerInterfaces;

   public ConstructorInjectTestObject()
   {
   }

   public ConstructorInjectTestObject(TesterInterface testerInterface)
   {
      this.testerInterface = testerInterface;
   }

   public ConstructorInjectTestObject(String someString, List<TesterInterface> testerInterfaces)
   {
      this.testerInterfaces = testerInterfaces;
   }

   public ConstructorInjectTestObject(int x, String someString, TesterInterface testerInterface)
   {
      this.testerInterface = testerInterface;
   }

   public static ConstructorInjectTestObject getInstance(TesterInterface ti)
   {
      return new ConstructorInjectTestObject(ti);
   }

   public ConstructorInjectTestObject withParameter(TesterInterface ti)
   {
      this.testerInterface = ti;
      return this;
   }

   public TesterInterface getTesterInterface()
   {
      return testerInterface;
   }

}
