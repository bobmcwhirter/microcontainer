/*
* JBoss, Home of Professional Open Source
* Copyright 2005, JBoss Inc., and individual contributors as indicated
* by the @authors tag. See the copyright.txt in the distribution for a
* full listing of individual contributors.
*
* This is free software; you can redistribute it and/or modify it
* under the terms of the GNU Lesser General Public License as
* published by the Free Software Foundation; either version 2.1 of
* the License, or (at your option) any later version.
*
* This software is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
* Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public
* License along with this software; if not, write to the Free
* Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
* 02110-1301 USA, or see the FSF site: http://www.fsf.org.
*/
package org.jboss.test.kernel.metadata.test;

import java.lang.annotation.Annotation;

import org.jboss.kernel.spi.dependency.KernelControllerContext;
import org.jboss.metadata.spi.MetaData;
import org.jboss.metadata.spi.repository.MetaDataRepository;
import org.jboss.metadata.spi.scope.CommonLevels;
import org.jboss.metadata.spi.scope.ScopeKey;
import org.jboss.metadata.spi.stack.MetaDataStack;
import org.jboss.test.AbstractTestDelegate;
import org.jboss.test.kernel.junit.MicrocontainerTest;

/**
 * MetaData Test Case.
 * 
 * @author <a href="adrian@jboss.com">Adrian Brock</a>
 * @version $Revision: 40428 $
 */
public class AbstractMetaDataTest extends MicrocontainerTest
{
   public AbstractMetaDataTest(String name) throws Throwable
   {
      super(name);
   }
   
   private static MetaData peekedMetaData;

   public static void peekMetaData()
   {
      peekedMetaData = MetaDataStack.peek();
   }
   
   protected void setUp() throws Exception
   {
      super.setUp();
      peekedMetaData = null;
   }

   protected MetaData assertPeekedMetaData()
   {
      assertNotNull(peekedMetaData);
      return peekedMetaData;
   }
   
   protected ScopeKey assertRetrievals(String name)
   {
      KernelControllerContext context = getControllerContext(name);
      MetaDataRepository repository = getMetaDataRepository().getMetaDataRepository();
      ScopeKey result = context.getScopeInfo().getScope();
      assertNotNull(repository.getMetaDataRetrieval(result));
      assertNotNull(repository.getMetaDataRetrieval(new ScopeKey(CommonLevels.INSTANCE, name)));
      return result;
   }
   
   protected void assertNoRetrievals(String name, ScopeKey scope)
   {
      MetaDataRepository repository = getMetaDataRepository().getMetaDataRepository();
      assertNull(repository.getMetaDataRetrieval(scope));
      assertNull(repository.getMetaDataRetrieval(new ScopeKey(CommonLevels.INSTANCE, name)));
   }
   
   protected <T extends Annotation> T assertAnnotation(MetaData metaData, Class<T> annotationClass)
   {
      T result = metaData.getAnnotation(annotationClass);
      assertNotNull(result);
      return  result;
   }
   
   protected <T extends Annotation> void assertNoAnnotation(MetaData metaData, Class<T> annotationClass)
   {
      T result = metaData.getAnnotation(annotationClass);
      assertNull(result);
   }
   
   /**
    * Default setup with security manager enabled
    * 
    * @param clazz the class
    * @return the delegate
    * @throws Exception for any error
    */
   public static AbstractTestDelegate getDelegate(Class<?> clazz) throws Exception
   {
      AbstractTestDelegate delegate = MicrocontainerTest.getDelegate(clazz);
      //delegate.enableSecurity = true;
      return delegate;
   }

   protected void configureLogging()
   {
      //enableTrace("org.jboss.dependency");
      //enableTrace("org.jboss.kernel.plugins.dependency");
   }
}