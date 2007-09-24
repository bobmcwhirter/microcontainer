/*
* JBoss, Home of Professional Open Source
* Copyright 2006, JBoss Inc., and individual contributors as indicated
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
package org.jboss.test.kernel.config.test;

import junit.framework.Test;
import org.jboss.test.kernel.config.support.XMLUtil;
import org.jboss.test.kernel.config.support.SimpleBean;

/**
 * Value trim xml test cases.
 *
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
public class ValueTrimXMLTestCase extends ValueTrimTestCase
{
   public ValueTrimXMLTestCase(String name)
   {
      super(name, true);
   }

   public static Test suite()
   {
      return suite(ValueTrimXMLTestCase.class);
   }

   protected Object getTrimmedValue() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      SimpleBean bean = (SimpleBean)util.getBean("SimpleBean");
      return bean.getAnInt();
   }

   protected Object getPlainValue() throws Throwable
   {
      XMLUtil util = bootstrapXML(true);
      Object bean = util.getBean("SimpleBean");
      util.validate();
      return bean;
   }

   protected Class<? extends Exception> getExceptionClass()
   {
      return IllegalStateException.class;
   }
}
