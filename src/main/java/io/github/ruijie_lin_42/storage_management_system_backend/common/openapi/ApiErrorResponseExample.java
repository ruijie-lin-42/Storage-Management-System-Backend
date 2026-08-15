package io.github.ruijie_lin_42.storage_management_system_backend.common.openapi;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;

import java.lang.annotation.*;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(ApiErrorResponseExamples.class)
public @interface ApiErrorResponseExample {

    String responseCode();
    ResultCode resultCode();

}
