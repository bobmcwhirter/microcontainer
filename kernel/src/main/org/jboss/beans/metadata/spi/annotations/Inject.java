/*
 * 
 */

package org.jboss.beans.metadata.spi.annotations;

import java.lang.annotation.Target;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface Inject
{
   String bean() default "";

   String property() default "";

   String state() default "Installed";

   InjectMode mode() default InjectMode.BY_TYPE;

   InjectType type() default InjectType.STRICT;
   
}
