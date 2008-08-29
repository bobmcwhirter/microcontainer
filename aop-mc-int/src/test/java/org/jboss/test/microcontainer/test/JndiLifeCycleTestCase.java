package org.jboss.test.microcontainer.test;

import java.util.Properties;

import javax.naming.Context;
import javax.naming.InitialContext;

import junit.framework.Test;

import org.jboss.test.aop.junit.AOPMicrocontainerTest;
import org.jboss.test.microcontainer.support.jndi.SimpleBean;
import org.jboss.test.microcontainer.support.jndi.SimpleBeanAnnotatedImpl;

public class JndiLifeCycleTestCase extends AOPMicrocontainerTest
{

   public JndiLifeCycleTestCase(String name)
   {
      super(name);
   }

   public static Test suite()
   {
      return suite(JndiLifeCycleTestCase.class);
   }

   /**
    * Validate that the 
    * @throws Exception
    */
   public void testJndi() throws Exception
   {
      super.enableTrace("org.jboss");
      Properties env = new Properties();
      env.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.jboss.test.microcontainer.support.jndi.MockInitialContextFactory");
      InitialContext ctx = new InitialContext(env);

      SimpleBean bean0 = (SimpleBean) ctx.lookup("beans/SimpleBean0");
      assertNotNull(bean0);
      assertEquals("bean0.prop1", bean0.getProp1());
      SimpleBean alias0 = (SimpleBean) ctx.lookup("beans/XmlAnnotatedSimpleBean0");
      assertNotNull(alias0);
      assertEquals("bean0.prop1", alias0.getProp1());
      SimpleBeanAnnotatedImpl bean1 = (SimpleBeanAnnotatedImpl) ctx.lookup("beans/SimpleBean1");
      assertEquals("bean1.prop1", bean1.getProp1());
      SimpleBean alias1 = (SimpleBean) ctx.lookup("beans/AnnotatedSimpleBean1");
      assertNotNull(alias1);
      assertEquals("bean1.prop1", alias1.getProp1());
   }
}
