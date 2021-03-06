package za.co.webler.advices;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface XRay {
    String value() default "";

    TimeScale timeScale() default TimeScale.MILLISECONDS;

    boolean logAllParams() default false;

    int[] paramsToLog() default {};
}