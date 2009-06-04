package org.jboss.beans.metadata.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark destroy lifecycle method.
 * 
 * For example this configuration:
 * <pre>
 * &#64;Bean(name="SomeBean")
 * public class MyBean
 * {
 *    &#64;Destroy
 *    public void remove()
 *    {
 *    }
 * }
 * </pre>
 * When <code>SomeBean</code> is uninstalled from the {@link org.jboss.dependency.spi.ControllerState#CREATE}
 * state, the <code>remove</code> method is called by the Microcontainer. You can also specify parameters 
 * if necessary, see {@link Constructor} for an example.
 *
 * @see org.jboss.beans.metadata.spi.BeanMetaData#getDestroy()
 * @author <a href="mailto:ales.justin@jboss.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Destroy
{
   /**
    * Is this lifecycle callback ignored.
    *
    * @return ignored
    */
   boolean ignored() default false;
}
