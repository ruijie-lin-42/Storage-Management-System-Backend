package io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.Role;
import io.github.ruijie_lin_42.storage_management_system_backend.common.result.Result;
import io.github.ruijie_lin_42.storage_management_system_backend.common.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.stream.Collectors;

// TODO: change e.printStackTrace to formal loggers
@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    // ==================== Custom Exception Classes ====================

    // ==================== Expected Exceptions ====================

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<Result<Void>> handleBusinessException(BusinessException e) {
        log.warn("Business failed, code={}, message={}, operatorId={}, data={}",
                e.getResultCode().getCode(), e.getResultCode().getMessage(), SecurityUtils.getUserIdFromContext(), e.getContext());
        return getCustomExceptionErrorResponse(e);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Result<Void>> handleAuthenticationException(AuthenticationException e) {
        log.warn("Authentication failed, code={}, message={}, data={}",
                e.getResultCode().getCode(), e.getResultCode().getMessage(), e.getContext());
        return getCustomExceptionErrorResponse(e);
    }

    @ExceptionHandler(AuthorizationException.class)
    public ResponseEntity<Result<Void>> handleAuthorizationException(AuthorizationException e) {
        log.warn("Authorization failed, code={}, message={}, operatorId={}, operatorRole={} data={}",
                e.getResultCode().getCode(), e.getResultCode().getMessage(), SecurityUtils.getUserIdFromContext(), SecurityUtils.getCurrentUserHighestRole(), e.getContext());
        return getCustomExceptionErrorResponse(e);
    }

    // ==================== Unexpected Exceptions ====================

    @ExceptionHandler(DataIntegrityException.class)
    public ResponseEntity<Result<Void>> handleDataIntegrityException(DataIntegrityException e) {
        log.error("Database operation affected unexpected number of rows, code={}, message={}, operatorId={}, expectedRows={}, actualRows={}, data={}",
                e.getResultCode().getCode(), e.getResultCode().getMessage(), SecurityUtils.getUserIdFromContext(), e.getExpectedRows(), e.getActualRows(), e.getContext());
        return getCustomExceptionErrorResponse(e);
    }

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Result<Void>> handleCustomException(CustomException e) {
        log.error("Unknown exception, code={}, message={}, data={}",
                e.getResultCode().getCode(), e.getResultCode().getMessage(), e.getContext());
        return getCustomExceptionErrorResponse(e);
    }


    // ==================== Other Exception Classes ====================

    // ==================== Expected Exceptions ====================

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e, HttpServletRequest request) {
        log.warn("Failed to parse request body, method={}, path={}, message={}",
                request.getMethod(), request.getRequestURI(), e.getMessage());
        return getExceptionErrorResponse(ResultCode.JSON_SYNTAX_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e, HttpServletRequest request) {
        log.warn("Method argument type mismatch, method={}, path={}, param={}, value={}, requiredType={}",
                request.getMethod(), request.getRequestURI(), e.getName(), e.getValue(), e.getRequiredType() != null ? e.getRequiredType().getSimpleName() : "unknown");
        return getExceptionErrorResponse(ResultCode.ARGUMENT_TYPE_MISMATCH);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e, HttpServletRequest request) {
        String errors = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining("; "));
        log.warn("Method argument not valid, method={}, path={}, errors={}",
                request.getMethod(), request.getRequestURI(), errors);
        return getExceptionErrorResponse(ResultCode.ARGUMENT_NOT_VALID);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Result<Void>> handleAccessDeniedException(AuthorizationDeniedException e, HttpServletRequest request) {
        Long userId = SecurityUtils.getUserIdFromContext();
        Role userRole = SecurityUtils.getCurrentUserHighestRole();
        log.warn("Access denied, userId={}, userRole={}, method={}, path={}, message={}",
                userId, userRole, request.getMethod(), request.getRequestURI(), e.getMessage());
        return getExceptionErrorResponse(ResultCode.INSUFFICIENT_PRIVILEGE);
    }

    // ==================== Unexpected Exceptions ====================

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Result<Void>> handleDataAccessException(DataAccessException e, HttpServletRequest request) {
        log.error("Database connection failed, message={}, method={}, path={}",
                e.getMessage(), request.getMethod(), request.getRequestURI(), e);
        // response content doesn't show any internal detail of database connection,
        //  see (ResultCode.DATABASE_CONNECTION_FAILED)'s message
        return getExceptionErrorResponse(ResultCode.DATABASE_CONNECTION_FAILED);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e, HttpServletRequest request) {
        log.error("System error, method={}, path={}", request.getMethod(), request.getRequestURI(), e);
        return getExceptionErrorResponse(ResultCode.SYSTEM_ERROR);
    }

    // ==================== Helper Methods ====================

    private ResponseEntity<Result<Void>> getCustomExceptionErrorResponse(CustomException e) {
        return getExceptionErrorResponse(e.getResultCode());
    }

    private ResponseEntity<Result<Void>> getExceptionErrorResponse(ResultCode resultCode) {
        return ResponseEntity
                .status(resultCode.getHttpStatus())
                .body(Result.fail(resultCode));
    }

}
