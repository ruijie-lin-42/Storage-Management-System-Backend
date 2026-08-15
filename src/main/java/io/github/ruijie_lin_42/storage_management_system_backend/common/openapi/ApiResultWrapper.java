package io.github.ruijie_lin_42.storage_management_system_backend.common.openapi;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.swagger.v3.oas.models.media.*;
import io.swagger.v3.oas.models.responses.ApiResponse;
import io.swagger.v3.oas.models.responses.ApiResponses;
import jakarta.annotation.Nonnull;
import jakarta.annotation.Nullable;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.properties.SpringDocConfigProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.HandlerMethod;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

@Configuration
public class ApiResultWrapper {

    @Bean
    public OperationCustomizer resultWrapper(SpringDocConfigProperties props) {
        return (operation, handlerMethod) -> {
            ApiResponses allResponses = operation.getResponses();
            Map<String, ResultCode> errorCodeConfig = getErrorCodeConfig(handlerMethod);
            allResponses.forEach((responseCode, apiResponse) -> {
                if (isSuccessResponse(responseCode)) {
                    wrapSuccessResponse(apiResponse);
                } else if (isClientOrServerError(responseCode)) {
                    ResultCode exampleResultCode = errorCodeConfig.get(responseCode);
                    if (exampleResultCode == null) {
                        wrapFailResponsesWithDefaultExamples(apiResponse, responseCode);
                    } else {
                        wrapFailResponsesWithExample(apiResponse, exampleResultCode);
                    }
                }
            });
            return operation;
        };
    }

    private Map<String, ResultCode> getErrorCodeConfig(@Nonnull HandlerMethod handlerMethod) {
        ApiErrorResponseExamples errorCodeConfigs = handlerMethod.getMethodAnnotation(ApiErrorResponseExamples.class);
        if (errorCodeConfigs == null) {
            ApiErrorResponseExample singleErrorCodeConfig = handlerMethod.getMethodAnnotation(ApiErrorResponseExample.class);
            if (singleErrorCodeConfig == null) return Collections.emptyMap();
            return Map.of(singleErrorCodeConfig.responseCode(), singleErrorCodeConfig.resultCode());
        }
        return Arrays.stream(errorCodeConfigs.value()).collect(Collectors.toMap(ApiErrorResponseExample::responseCode, ApiErrorResponseExample::resultCode));
    }

    private boolean isSuccessResponse(@Nonnull String responseCode) {
        return responseCode.startsWith("2");
    }

    private boolean isClientOrServerError(@Nonnull String responseCode) {
        return responseCode.startsWith("4") || responseCode.startsWith("5");
    }

    private void wrapSuccessResponse(@Nonnull ApiResponse apiResponse) {
        wrapResponsesUsingResultCode(apiResponse, ResultCode.SUCCESS);
    }

    private void wrapFailResponsesWithExample(@Nonnull ApiResponse apiResponse, @Nonnull ResultCode example) {
        wrapResponsesUsingResultCode(apiResponse, example);
    }

    private void wrapFailResponsesWithDefaultExamples(@Nonnull ApiResponse apiResponse, @Nonnull String responseCode) {
        ResultCode resultCode = mapDefaultResultCode(responseCode);
        wrapResponsesUsingResultCode(apiResponse, resultCode);
    }

    private void wrapResponsesUsingResultCode(@Nonnull ApiResponse apiResponse, @Nonnull ResultCode resultCode) {
        wrapResponses(apiResponse, resultCode.getCode(), resultCode.getMessage());
    }

    private void wrapResponses(ApiResponse apiResponse, int exampleCode, String exampleMessage) {
        if (apiResponse != null) {
            if (apiResponse.getContent() != null) {
                MediaType originalMediaType = getOriginalMediaType(apiResponse.getContent());
                if (originalMediaType != null) {
                    Schema<?> dataSchema = originalMediaType.getSchema();
                    boolean successResponse = exampleCode == ResultCode.SUCCESS.getCode();
                    Schema<?> schema = getSchema(exampleCode, exampleMessage, successResponse ? dataSchema : null);
                    apiResponse.setContent(getJsonContent(schema));
                }
            } else {
                Schema<Object> schema = getSchema(exampleCode, exampleMessage, null);
                Content content = getJsonContent(schema);
                apiResponse.setContent(content);
            }
        }
    }

    private MediaType getOriginalMediaType(@Nonnull Content content) {
        return content.get("*/*") != null ? content.get("*/*") : content.get("application/json");
    }

    private Content getJsonContent(@Nullable Schema<?> schema) {
        MediaType newMediaType = new MediaType().schema(schema);
        return new Content().addMediaType("application/json", newMediaType);
    }

    private Schema<Object> getSchema(int exampleCode, @Nonnull String exampleMessage, @Nullable Schema<?> exampleDataSchema) {
        Schema<Object> schema = new ObjectSchema();
        schema.name("Result")
                .title("Result")
                .description("Unified response body wrapper");
        schema.addProperty("code", new IntegerSchema()
                .description("Business-specific status code")
                .example(exampleCode));
        schema.addProperty("message", new StringSchema()
                .description("Human-readable message")
                .example(exampleMessage));
        schema.addProperty("data", (exampleDataSchema == null ? new ObjectSchema().nullable(true).example(null) : exampleDataSchema)
                .description("Response payload, structure varies by endpoint"));
        return schema;
    }

    private ResultCode mapDefaultResultCode(@Nonnull String responseCode) {
        return switch (responseCode) {
            case "400" -> ResultCode.PARAM_ERROR;
            case "401" -> ResultCode.UNAUTHORIZED;
            case "403" -> ResultCode.FORBIDDEN;
            case "404" -> ResultCode.NOT_FOUND;
            case "409" -> ResultCode.CONFLICT;
            case "500" -> ResultCode.SYSTEM_ERROR;
            default -> ResultCode.UNKNOWN_ERROR;
        };
    }

}