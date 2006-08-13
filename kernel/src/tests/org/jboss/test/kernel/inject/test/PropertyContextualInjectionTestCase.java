package org.jboss.test.kernel.inject.test;

import org.jboss.test.kernel.junit.MicrocontainerTest;
import org.jboss.test.kernel.inject.support.TesterInterfaceGetter;
import org.jboss.kernel.spi.deployment.KernelDeployment;
import junit.framework.Test;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
public class PropertyContextualInjectionTestCase extends MicrocontainerTest
{
   private static final String[] PREFIX = new String[]{"", "Ann"};
   private static final String[] NUMBER = new String[]{"Single", "Multiple", "Null"};
   private static final String[] MODE = new String[]{"Type", "Name"};
   private static final String[] TYPE = new String[]{"Loose", "Strict"};
   private static final String TEST_CASE_SUFFIX = "TestCase.xml";

   public PropertyContextualInjectionTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(PropertyContextualInjectionTestCase.class);
   }

   public void testContextualInjection() throws Throwable
   {
      for(int i = 0; i < PropertyContextualInjectionTestCase.PREFIX.length; i++)
      {
         for(int j = 0; j < PropertyContextualInjectionTestCase.NUMBER.length; j++)
         {
            for(int k = 0; k < PropertyContextualInjectionTestCase.MODE.length; k++)
            {
               for(int l = 0; l < PropertyContextualInjectionTestCase.TYPE.length; l++)
               {
                  executeTestCase(PropertyContextualInjectionTestCase.PREFIX[i] + PropertyContextualInjectionTestCase.NUMBER[j] + PropertyContextualInjectionTestCase.MODE[k] + PropertyContextualInjectionTestCase.TYPE[l] + PropertyContextualInjectionTestCase.TEST_CASE_SUFFIX);
               }
            }
         }
      }
   }

   private void executeTestCase(String testCaseName) throws Throwable
   {
      getLog().info("Testing contextual injection: "+ testCaseName);
      KernelDeployment deployment = null;
      try
      {
         deployment = deploy(testCaseName);

         validate();

         TesterInterfaceGetter interfaceGetter = (TesterInterfaceGetter) getBean("testObject");
         boolean isSingle = testCaseName.indexOf("Single") >= 0;
         assertFalse(interfaceGetter == null && isStrict(testCaseName) && isSingle);
      }
      catch(Throwable t)
      {
         // check if we expect Throwable - e.g. NullTypeStrictTestCase, ...
         if (isThrowableExpected(testCaseName) == false)
         {
            throw t;
         }
         // get some info about 'failure'
         getLog().info(t);
      }
      finally {
         if (deployment != null)
         {
            undeploy(deployment);
         }
      }
   }

   // MultipleNameStrictTC is excluded - unique name requirement
   private boolean isThrowableExpected(String testCaseName)
   {
      boolean isMultiple = testCaseName.indexOf("Multiple") >= 0;
      boolean isNull = testCaseName.indexOf("Null") >= 0;
      return isMultiple || (isNull && isStrict(testCaseName));
   }

   private boolean isStrict(String testCaseName)
   {
      return testCaseName.indexOf("Strict") >= 0;
   }

}
