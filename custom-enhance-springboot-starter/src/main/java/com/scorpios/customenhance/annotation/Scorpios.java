package com.scorpios.customenhance.annotation;

import java.lang.annotation.*;

@Documented
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface Scorpios {

    boolean registerBean() default false;

}