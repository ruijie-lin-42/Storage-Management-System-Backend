package io.github.ruijie_lin_42.storage_management_system_backend.common.openapi;

import io.github.ruijie_lin_42.storage_management_system_backend.common.result.Result;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponse(responseCode = "401", description = "Unauthenticated")
public @interface RequiresAuthApiResponses {
}
