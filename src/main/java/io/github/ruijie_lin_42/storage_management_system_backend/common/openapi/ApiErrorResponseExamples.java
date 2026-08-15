package io.github.ruijie_lin_42.storage_management_system_backend.common.openapi;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface ApiErrorResponseExamples {

    ApiErrorResponseExample[] value();

}
