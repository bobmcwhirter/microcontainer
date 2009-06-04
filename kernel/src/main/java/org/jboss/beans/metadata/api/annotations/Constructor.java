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
package org.jboss.beans.metadata.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark the constructor used to instantiate a bean. If absent, the default constructor 
 * of the @{@link Bean} annotated class will be used. If the constructor has parameters,
 * use the following annotations on the parameters to set their values:
 * <ul>
 *   <li>@{@link ArrayValue}</li>
 *   <li>@{@link CollectionValue}</li>
 *   <li>@{@link Inject}</li>
 *   <li>@{@link JavaBeanValue}</li>
 *   <li>@{@link ListValue}</li> 
 *   <li>@{@link MapValue}</li> 
 *   <li>@{@link NullValue}</li> 
 *   <li>@{@link SetValue}</li> 
 *   <li>@{@link StringValue}</li>
 * </ul>
 * 
 * For example the following configuration:<p>
 * <pre>
 * &#64;Bean(name="SomeBean")
 * public class MyBean
 * {
 *   public MyBean()
 *   {
 *   }
 * 
 *   &#64;Constructor
 *   public MyBean(&#64;StringValue(value="25", type="long") long age, &#64;Inject(bean="OtherBean") Object other)
 *   {
 *      ...
 *   }
 * }
 * </pre>
 * uses the non-default constructor as its constructor and uses the long value <code>25</code> for the <code>age</code> parameter, and injects
 * the bean <code>OtherBean</code> into the <code>other</code> parameter. 
 * 
 * @see Factory
 * @see FactoryMethod
 * @see org.jboss.beans.metadata.spi.BeanMetaData#getConstructor()
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.CONSTRUCTOR})
public @interface Constructor
{
}
