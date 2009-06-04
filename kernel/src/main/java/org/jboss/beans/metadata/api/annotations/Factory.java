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
 * Define a static or non-static factory method to instantiate a bean. The factory method
 * can exist outside the bean class. If the method has parameters, use the 
 * <code>parameters</code> attribute to set the parameters in the following annotations wrapped in @{@link Value}
 * annotations. 
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
 * For example the following configuration for a static factory method:<p>
 * <pre>
 * &#64;Bean(name="SomeBean")
 * &#64;Factory (
 *    factoryClass="org.jboss.example.StaticFactory", 
 *    factoryMethod="createMyBeanInstance",
 *    parameters={&#64;Value(string=&#64;StringValue(value="25", type="long")), &#64;Inject(bean="OtherBean"))})
 * public class MyBean
 * {
 *   
 *   public MyBean(long age, Object other)
 *   {
 *      ...
 *   }
 *   
 *   &#64;FactoryMethod
 *   public static MyBean createInstance(&#64;StringValue(value="25", type="long") long age, &#64;Inject(bean="OtherBean") Object other)
 *   {
 *       return new MyBean(age, other);
 *   }
 * }
 * </pre>
 * will call the static method <code>createMyBeanInstance</code> on the class <code>org.jboss.example.StaticFactory</code> to create the 
 * <code>MyBean</code> instance, passing in the long value <code>25</code> for the first parameter, and inject 
 * the bean <code>OtherBean</code> into the second parameter.
 * 
 * This configuration for a non-static factory method:
 * <pre>
 * &#64;Bean(name="SomeBean")
 * &#64;Factory (
 *    factory=@Value(javabean=@JavaBeanValue("org.jboss.example.NonStaticFactory")),
 *    factoryMethod="createMyBeanInstance",
 *    parameters={&#64;Value(string=&#64;StringValue(value="25", type="long")), &#64;Inject(bean="OtherBean"))})
 * public class MyBean
 * {
 *   
 *   public MyBean(long age, Object other)
 *   {
 *      ...
 *   }
 *   
 *   &#64;FactoryMethod
 *   public static MyBean createInstance(&#64;StringValue(value="25", type="long") long age, &#64;Inject(bean="OtherBean") Object other)
 *   {
 *       return new MyBean(age, other);
 *   }
 * }
 * </pre>
 * will instantiate an object of type <code>org.jboss.example.NonStaticFactory</code> and call the method <code>createMyBeanInstance</code> 
 * it to create the <code>MyBean</code> instance, passing in the long value <code>25</code> for the first parameter, and inject 
 * the bean <code>OtherBean</code> into the second parameter.
 *
 *
 * @see Constructor
 * @see FactoryMethod
 * @see org.jboss.beans.metadata.spi.BeanMetaData#getConstructor()
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Factory
{
   /**
    * Get the non-static factory. Only used if we want to 
    * use a non-static factory
    *
    * @return the factory value
    */
   Value factory() default @Value();

   /**
    * Get the static factory class. Only used if we want to 
    * use a static factory
    *
    * @return the factory class
    */
   Class<?> factoryClass() default void.class;

   /**
    * Get the factory method name.
    *
    * @return the factory method
    */
   String factoryMethod();

   /**
    * Get parameters.
    *
    * @return the parameters
    */
   Value[] parameters() default {};
}
