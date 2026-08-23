package io.github.ruijie_lin_42.storage_management_system_backend.common.exceptions;

import io.github.ruijie_lin_42.storage_management_system_backend.common.enums.ResultCode;
import io.github.ruijie_lin_42.storage_management_system_backend.common.result.Result;
import org.springframework.dao.DataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.Map;

// TODO: change e.printStackTrace to formal loggers
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomException.class)
    public ResponseEntity<Result<Void>> handleCustomException(CustomException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(e.getCode().getHttpStatus()).
                body(Result.fail(e.getCode()));
    }
    
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Result<Void>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        e.printStackTrace();

        return ResponseEntity
                .status(ResultCode.JSON_SYNTAX_ERROR.getHttpStatus())
                .body(Result.fail(ResultCode.JSON_SYNTAX_ERROR));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<Result<Map<String, String>>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(ResultCode.ARGUMENT_TYPE_MISMATCH.getHttpStatus())
                .body(Result.fail(ResultCode.ARGUMENT_TYPE_MISMATCH));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Result<Void>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e){
        e.printStackTrace();
        return ResponseEntity
                .status(ResultCode.ARGUMENT_NOT_VALID.getHttpStatus())
                .body(Result.fail(ResultCode.ARGUMENT_NOT_VALID));
    }

    @ExceptionHandler(DataAccessException.class)
    public ResponseEntity<Result<Void>> handleDataAccessException(DataAccessException e){
        e.printStackTrace();
        return ResponseEntity
                .status(ResultCode.DATABASE_CONNECTION_FAILED.getHttpStatus())
                .body(Result.fail(ResultCode.DATABASE_CONNECTION_FAILED));
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<Result<Void>> handleAccessDeniedException(AuthorizationDeniedException e) {
        e.printStackTrace();
        return ResponseEntity
                .status(ResultCode.INSUFFICIENT_PRIVILEGE.getHttpStatus())
                .body(Result.fail(ResultCode.INSUFFICIENT_PRIVILEGE));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Result<Void>> handleException(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(ResultCode.SYSTEM_ERROR.getHttpStatus())
                .body(Result.fail(ResultCode.INSUFFICIENT_PRIVILEGE));
    }

}
