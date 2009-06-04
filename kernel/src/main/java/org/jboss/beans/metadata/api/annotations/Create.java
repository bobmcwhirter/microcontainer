package org.jboss.beans.metadata.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark lifecycle create method.
 * 
 * For example this configuration:
 * <pre>
 * &#64;Bean(name="SomeBean")
 * public class MyBean
 * {
 *    &#64;Create
 *    public void init()
 *    {
 *    }
 * }
 * </pre>
 * When <code>SomeBean</code> is installed to the {@link org.jboss.dependency.spi.ControllerState#CREATE}
 * state, the <code>init</code> method is called by the Microcontainer. You can also specify parameters 
 * if necessary, see {@link Constructor} for an example.
 *
 * @see org.jboss.beans.metadata.spi.BeanMetaData#getCreate()
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Create
{
   /**
    * Is this lifecycle callback ignored.
    *
    * @return ignored
    */
   boolean ignored() default false;
}
