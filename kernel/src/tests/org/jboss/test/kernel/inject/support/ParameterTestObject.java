package org.jboss.test.kernel.inject.support;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class ParameterTestObject
{
   
   private DuplicateTester duplicateTester;

   public ParameterTestObject(DuplicateTester duplicateTester)
   {
      this.duplicateTester = duplicateTester;
   }

}
