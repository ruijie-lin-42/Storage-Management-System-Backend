package io.github.ruijie_lin_42.storage_management_system_backend.interceptors;

import io.github.ruijie_lin_42.storage_management_system_backend.common.utils.SecurityUtils;
import jakarta.annotation.Nonnull;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.util.UUID;

@Component
@Slf4j
public class TraceIdInterceptor implements HandlerInterceptor {

    private final String START_TIME = "startTime";

    @Override
    public boolean preHandle(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler) {
        String traceId = UUID.randomUUID().toString();
        MDC.put("traceId", traceId);
        log.info("Request start, userId={}, method={}, uri={}, ip={}", SecurityUtils.getUserIdFromContext(), request.getMethod(), request.getRequestURI(), request.getRemoteAddr());
        long startTime = System.nanoTime();
        request.setAttribute(START_TIME, startTime);
        return true;
    }

    @Override
    public void afterCompletion(@Nonnull HttpServletRequest request, @Nonnull HttpServletResponse response, @Nonnull Object handler, Exception ex) {
        long startTime = (long) request.getAttribute(START_TIME);
        long cost = (System.nanoTime() - startTime) / 1000000;
        if (cost < 1000) {
            log.info("Request end, method={}, uri={}, status={}, cost={}ms", request.getMethod(), request.getRequestURI(), response.getStatus(), cost);
        } else {
            log.warn("Slow request end, method={}, uri={}, status={}, cost={}ms", request.getMethod(), request.getRequestURI(), response.getStatus(), cost);
        }
        MDC.clear();
    }
}
