package org.jboss.beans.metadata.spi.annotations;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.annotation.ElementType;

/**
 * @author <a href="mailto:ales.justin@gmail.com">Ales Justin</a>
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface StartLifecycle
{
}
