package io.github.ruijie_lin_42.storage_management_system_backend.common.advice;

import io.github.ruijie_lin_42.storage_management_system_backend.common.result.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class GlobalResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    // beforeBodyWrite should be invoked all the times
    @Override
    public boolean supports(MethodParameter returnType, @NonNull Class<? extends HttpMessageConverter<?>> converterType) {
        return !returnType.getContainingClass().getName().startsWith("org.springdoc");
    }

    @Override
    public Object beforeBodyWrite(Object body, @NonNull MethodParameter returnType, @NonNull MediaType selectedContentType, @NonNull Class<? extends HttpMessageConverter<?>> selectedConverterType, @NonNull ServerHttpRequest request, @NonNull ServerHttpResponse response) {
        if (body instanceof ResponseEntity || body instanceof Result) {
            return body;
        }
        return Result.success(body);
    }
}
