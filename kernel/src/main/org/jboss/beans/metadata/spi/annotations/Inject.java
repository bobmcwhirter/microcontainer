/*
 * 
 */

package org.jboss.beans.metadata.spi.annotations;

/**
 * @author <a href="mailto:ales.justin@genera-lynx.com">Ales Justin</a>
 */
public @interface Inject
{

   String bean() default "";

   String property() default "";

   String state() default "Installed";

   String mode() default "byType";

   String type() default "strict";

}
