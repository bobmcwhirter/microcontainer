package org.jboss.beans.metadata.api.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Mark stop lifecycle method.
 * 
 * For example this configuration:
 * <pre>
 * &#64;Bean(name="SomeBean")
 * public class MyBean
 * {
 *    &#64;Stop
 *    public void stopIt()
 *    {
 *    }
 * }
 * </pre>
 * When <code>SomeBean</code> is uninstalled from the {@link org.jboss.dependency.spi.ControllerState#START}
 * state, the <code>stopIt</code> method is called by the Microcontainer. You can also specify parameters 
 * if necessary, see {@link Constructor} for an example.
 *
 * @see org.jboss.beans.metadata.spi.BeanMetaData#getStop()
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface Stop
{
   /**
    * Is this lifecycle callback ignored.
    *
    * @return ignored
    */
   boolean ignored() default false;
}
