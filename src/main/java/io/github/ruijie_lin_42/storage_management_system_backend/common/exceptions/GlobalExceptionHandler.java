package io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.result.Result;
import org.springframework.dao.DataAccessException;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.nio.file.AccessDeniedException;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public Result<Void> handleCustomException(CustomException e) {
        return Result.fail(e.getCode());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public Result<Void> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return Result.fail(ResultCode.JSON_SYNTAX_ERROR);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public Result<Map<String, String>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        return Result.fail(ResultCode.ARGUMENT_TYPE_MISMATCH);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Result<Void> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        return Result.fail(ResultCode.ARGUMENT_NOT_VALID);
    }

    @ExceptionHandler(DataAccessException.class)
    public Result<Void> handleDataAccessException(DataAccessException e){
        // TODO: change to formal loggers
        e.printStackTrace();
        return Result.fail(ResultCode.DATABASE_CONNECTION_FAILED);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public Result<Void> handleAccessDeniedException(AuthorizationDeniedException e) {
        e.printStackTrace();
        return Result.fail(ResultCode.INSUFFICIENT_PRIVILEGE);
    }

    @ExceptionHandler(Exception.class)
    public Result<Void> handleException(Exception e) {
        // TODO: change to formal loggers
        e.printStackTrace();
        return Result.fail(ResultCode.SYSTEM_ERROR);
    }

}
