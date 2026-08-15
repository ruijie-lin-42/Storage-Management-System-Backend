package io.github.ruijie_lin_42.storage_management_system_backend.common.openapi;

import io.github.ruijie_lin_42.storage_management_system_backend.common.result.Result;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@ApiResponses({
        @ApiResponse(responseCode = "400",
                description = "Invalid request argument, see request schema and response body errors."),
        @ApiResponse(responseCode = "500",
                description = "Unexpected internal server error, please retry or contact support."),
})
public @interface CommonErrorApiResponses {
}
